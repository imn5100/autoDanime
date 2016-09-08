package com.shaw.utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

public class XmlUtils {

	private ParamVO vo;
	private static Format oneUTFLineformat;

	public XmlUtils(String base) {
		vo = new ParamVO(base);
	}

	public void setStringValue(String path, String value) {
		vo.setStringValue(path, value);
	}

	public ParamVO addXmlNode(String path) {
		return vo.addXmlNode(path);
	}

	public Element getXmlRoot() {
		return vo.getXmlRoot();
	}

	public ParamVO getXmlNode(String path) {
		return vo.getXmlNode(path);
	}

	public String getStringValue(String path) {
		return vo.getStringValue(path);
	}

	public static String xml2String(Element root) {
		return xml2String(root, getOnelineUTFXmlFormat());
	}

	public static String xml2String(Document doc) {
		return xml2String(doc, getOnelineUTFXmlFormat());
	}

	public static String xml2String(Element root, Format format) {
		Element tmp = (Element) root.clone();
		tmp.detach();
		Document doc = new Document(tmp);
		return xml2String(doc, format);
	}

	public static String xml2String(Document doc, Format format) {
		ByteArrayOutputStream byteRep = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(byteRep);
		XMLOutputter docWriter = new XMLOutputter(format);
		try {
			docWriter.output(doc, out);
		} catch (IOException e) {
			throw new RuntimeException(ParamVO.XML_FORMAT_ERR + ParamVO.SPLIT + "Xml文档转换--->异常", e);
		}
		return byteRep.toString();
	}

	public static Format getOnelineUTFXmlFormat() {
		if (oneUTFLineformat == null) {
			oneUTFLineformat = Format.getRawFormat();
			oneUTFLineformat.setEncoding("UTF-8");
			oneUTFLineformat.setLineSeparator("");
		}
		return oneUTFLineformat;
	}

	public static Element string2Xml(String xml) {
		Document doc;
		SAXBuilder builder = new SAXBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		try {
			doc = builder.build(is);
			return doc.getRootElement();
		} catch (Exception e) {
			throw new RuntimeException(ParamVO.XML_FORMAT_ERR + ParamVO.SPLIT + "Xml文档转换--->异常", e);
		}

	}

	public static void main(String[] args) {
		XmlUtils vo = new XmlUtils("xml");
		vo.addXmlNode("appid");
		vo.setStringValue("appid", "123");
		vo.addXmlNode("mch_id");
		vo.setStringValue("mch_id", "mch_id");
		vo.addXmlNode("nonce_str");
		vo.setStringValue("nonce_str", "nonce_str");
		vo.addXmlNode("out_trade_no");
		vo.setStringValue("out_trade_no", "out_trade_no");
		vo.addXmlNode("sign");
		vo.setStringValue("sign", "sign");

		String sendXml = XmlUtils.xml2String(vo.getXmlRoot(), XmlUtils.getOnelineUTFXmlFormat()).trim();

		System.out.println(sendXml);

		// 解析xml
		String returnXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "<appid><![CDATA[wx2421b1c4370ec43b]]></appid>"
				+ "<mch_id><![CDATA[10000100]]></mch_id>" + "<device_info><![CDATA[1000]]></device_info>"
				+ "<nonce_str><![CDATA[TN55wO9Pba5yENl8]]></nonce_str>"
				+ "<sign><![CDATA[BDF0099C15FF7BC6B1585FBB110AB635]]></sign>"
				+ "<result_code><![CDATA[SUCCESS]]></result_code>"
				+ "<openid><![CDATA[oUpF8uN95-Ptaags6E_roPHg7AG0]]></openid>"
				+ "<is_subscribe><![CDATA[Y]]></is_subscribe>" + "<trade_type><![CDATA[MICROPAY]]></trade_type>"
				+ "<bank_type><![CDATA[CCB_DEBIT]]></bank_type>" + "<total_fee>1</total_fee>"
				+ "<fee_type><![CDATA[CNY]]></fee_type>"
				+ "<transaction_id><![CDATA[1008450740201411110005820873]]></transaction_id>"
				+ "<out_trade_no><![CDATA[1415757673]]></out_trade_no>" + "<attach><![CDATA[订单额外描述]]></attach>"
				+ "<time_end><![CDATA[20141111170043]]></time_end>" + "<trade_state><![CDATA[SUCCESS]]></trade_state>"
				+ "</xml>";

		ParamVO param = new ParamVO(XmlUtils.string2Xml(returnXml));
		String returnCode = param.getStringValue("return_code");
		if ("SUCCESS".equals(returnCode)) {
			String resultCode = param.getStringValue("result_code");
			if ("SUCCESS".equals(resultCode)) {
				String tradeNo = param.getStringValue("out_trade_no");
				String tradeState = param.getStringValue("trade_state");

				System.out.println("tradeNo:" + tradeNo);
				System.out.println("tradeState:" + tradeState);

			}
		}

	}
}
