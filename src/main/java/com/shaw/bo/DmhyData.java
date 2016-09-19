package com.shaw.bo;

import java.io.Serializable;

public class DmhyData implements Serializable, Comparable<DmhyData> {
	private static final long serialVersionUID = 923336378712006176L;
	private Integer id;
	private String time;
	private String classi;
	private String title;
	private String magnetLink;
	private String size;
	private String publisher;
	private Integer seedNum;
	private Integer comNum;
	private Integer downNum;
	private String simpleName;
	private Long createTime;

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getClassi() {
		return classi;
	}

	public void setClassi(String classi) {
		this.classi = classi;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMagnetLink() {
		return magnetLink;
	}

	public void setMagnetLink(String magnetLink) {
		this.magnetLink = magnetLink;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getSeedNum() {
		return seedNum;
	}

	public void setSeedNum(Integer seedNum) {
		this.seedNum = seedNum;
	}

	public Integer getComNum() {
		return comNum;
	}

	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}

	public Integer getDownNum() {
		return downNum;
	}

	public void setDownNum(Integer downNum) {
		this.downNum = downNum;
	}

	@Override
	public int compareTo(DmhyData o) {
		// 需要逆序排 DESC，质量最好的有限排前面。
		return (downNum + comNum + seedNum - (o.getComNum() + o.getDownNum() + o.getSeedNum())) * -1;
	}

	@Override
	public String toString() {
		return "DmhyData [id=" + id + ", time=" + time + ", classi=" + classi + ", title=" + title + ", magnetLink="
				+ magnetLink + ", size=" + size + ", publisher=" + publisher + ", seedNum=" + seedNum + ", comNum="
				+ comNum + ", downNum=" + downNum + ", simpleName=" + simpleName + "]";
	}

	// 判断是否需要更新操作
	public boolean commissionEqual(DmhyData data) {
		if (data == null) {
			return false;
		}
		if (magnetLink.equals(data.getMagnetLink()) && title.equals(data.getTitle())
				&& downNum.equals(data.getDownNum()) && comNum.equals(data.getComNum())
				&& seedNum.equals(data.getSeedNum())) {
			return true;
		} else {
//			System.out.println("update:oldData:" + this.toString() + " newData:" + data);
			return false;
		}
	}

}
