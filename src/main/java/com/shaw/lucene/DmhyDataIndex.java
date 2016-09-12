package com.shaw.lucene;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.DmhyData;
import com.shaw.utils.TimeUtils;

public class DmhyDataIndex {
    public static final String DEFAULT_PATH = "/lucene/anime";
    private Directory dir;
    private Analyzer analyzer;

    public DmhyDataIndex(Analyzer analyzer, String path) throws Exception {
        if (analyzer == null) {
            this.analyzer = new SmartChineseAnalyzer();
        } else {
            this.analyzer = analyzer;
        }
        if (StringUtils.isNotBlank(path)) {
            this.dir = FSDirectory.open(Paths.get(path));
        } else {
            this.dir = FSDirectory.open(Paths.get(DEFAULT_PATH));
        }

    }

    public DmhyDataIndex() throws Exception {
        this.analyzer = new SmartChineseAnalyzer();
        this.dir = FSDirectory.open(Paths.get(DEFAULT_PATH));
    }

    public IndexWriter getWriter() throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir, config);
    }

    public IndexReader getReader() throws Exception {
        return DirectoryReader.open(dir);
    }

    /**
     * Indexed, not tokenized, omits norms, indexes none, stored.
     */
    public static final FieldType TYPE_NOT_INDEX_STORED = new FieldType();
    public static final FieldType TIME_TYPE = new FieldType();

    static {
        TYPE_NOT_INDEX_STORED.setOmitNorms(true);
        TYPE_NOT_INDEX_STORED.setIndexOptions(IndexOptions.NONE);
        TYPE_NOT_INDEX_STORED.setTokenized(false);
        TYPE_NOT_INDEX_STORED.setStored(true);
        TYPE_NOT_INDEX_STORED.freeze();

        TIME_TYPE.setTokenized(true);
        TIME_TYPE.setOmitNorms(true);
        TIME_TYPE.setStored(false);
        TIME_TYPE.setIndexOptions(IndexOptions.DOCS);
        TIME_TYPE.setNumericType(FieldType.NumericType.LONG);
        TIME_TYPE.setDocValuesType(DocValuesType.NUMERIC);
        TIME_TYPE.freeze();
    }

    /**
     * 依据具体需求，判断字段是否需要索引或存储。 很多字段可以只索引不存储 title classi 用于搜索 索引建立。 time 用于关联时间排序。
     **/
    public void addIndex(DmhyData data) throws Exception {
        IndexWriter writer = getWriter();
        try {
            Document doc = new Document();
            // 索引字段
            doc.add(new TextField("title", data.getTitle(), Field.Store.NO));
            doc.add(new LongField("time", TimeUtils.formatDate(data.getTime(), "yyyy/MM/dd HH:mm").getTime(),
                    TIME_TYPE));
            doc.add(new StringField("id", data.getId().toString(), Field.Store.YES));
            // 存储，但是不做索引
            String luceneData = JSONObject.toJSONString(data);
            doc.add(new Field("data", luceneData, TYPE_NOT_INDEX_STORED));
            writer.addDocument(doc);
        } finally {
            writer.close();
        }
    }

    public void addIndexList(List<DmhyData> dataList) throws Exception {
        IndexWriter writer = getWriter();
        try {
            for (DmhyData data : dataList) {
                Document doc = new Document();
                // 索引字段
                doc.add(new TextField("title", data.getTitle(), Field.Store.NO));
                doc.add(new LongField("time", TimeUtils.formatDate(data.getTime(), "yyyy/MM/dd HH:mm").getTime(),
                        TIME_TYPE));
                doc.add(new StringField("id", data.getId().toString(), Field.Store.YES));
                // 存储，但是不做索引
                String luceneData = JSONObject.toJSONString(data);
                doc.add(new Field("data", luceneData, TYPE_NOT_INDEX_STORED));
                writer.addDocument(doc);
            }
        } finally {
            writer.close();
        }
    }

    public List<DmhyData> searchAnime(String keyword) throws Exception {
        IndexReader reader = getReader();
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser titleParser = new QueryParser("title", this.analyzer);
        Query titleQuery = titleParser.parse(keyword);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
        // 按时间和 title 关联度排序 关联度优先
        Sort sort = new Sort(new SortField("title", SortField.Type.SCORE), new SortField("time", Type.LONG, true));
        TopDocs hits = searcher.search(builder.build(), 10, sort);
        List<DmhyData> datas = new ArrayList<DmhyData>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String dataStr = doc.get("data");
            DmhyData dmhyData = JSONObject.parseObject(dataStr, DmhyData.class);
            datas.add(dmhyData);
        }
        return datas;

    }

    public void updateIndex(DmhyData data) throws Exception {
        IndexWriter writer = getWriter();
        try {
            Document doc = new Document();
            // 索引字段 title 用于搜索 ，time 用于搜索排序，id用于维护索引
            doc.add(new TextField("title", data.getTitle(), Field.Store.NO));
            doc.add(new LongField("time", TimeUtils.formatDate(data.getTime(), "yyyy/MM/dd HH:mm").getTime(),
                    TIME_TYPE));
            doc.add(new StringField("id", data.getId().toString(), Field.Store.YES));
            // 存储，但是不做索引
            String luceneData = JSONObject.toJSONString(data);
            doc.add(new Field("data", luceneData, TYPE_NOT_INDEX_STORED));
            writer.updateDocument(new Term("id", data.getId().toString()), doc);
        } finally {
            writer.close();
        }
    }

    public void updateIndexList(List<DmhyData> dataList) throws Exception {
        IndexWriter writer = getWriter();
        try {
            for (DmhyData data : dataList) {
                Document doc = new Document();
                // 索引字段
                doc.add(new TextField("title", data.getTitle(), Field.Store.NO));
                doc.add(new LongField("time", TimeUtils.formatDate(data.getTime(), "yyyy/MM/dd HH:mm").getTime(),
                        TIME_TYPE));
                doc.add(new StringField("id", data.getId().toString(), Field.Store.YES));
                // 存储，但是不做索引
                String luceneData = JSONObject.toJSONString(data);
                doc.add(new Field("data", luceneData, TYPE_NOT_INDEX_STORED));
                writer.updateDocument(new Term("id", data.getId().toString()), doc);
            }
        } finally {
            writer.close();
        }
    }

    public void deleteIndex(String id) throws Exception {
        IndexWriter writer = getWriter();
        try {
            writer.deleteDocuments(new Term("id", id));
            writer.forceMergeDeletes();
            writer.commit();
        } finally {
            writer.close();
        }
    }

    public void batchDeleteIndex(List<String> ids) throws Exception {
        IndexWriter writer = getWriter();
        try {
            for (String id : ids) {
                writer.deleteDocuments(new Term("id", id));
                writer.forceMergeDeletes();
            }
            writer.commit();
        } finally {
            writer.close();
        }
    }

}
