package com.mooo.mytools.util;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParseHandler extends DefaultHandler {
	private StringBuilder sb = new StringBuilder(); 
	
	//解析开始
	public void startDocument() throws SAXException {
		System.err.println("开始");
	}
	
	//遍历开始标签
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		//(3) 开始收集新的标签的数据时，先清空历史数据  
        sb.setLength(0);
		System.out.println("--startElement()--"+qName);
		
		//判断正在解析的元素是否有属性值,如果有则将其全部取出并保存到map对象中，如:<person id="00001"></person>
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				System.out.println(attributes.getQName(i) + ":"
						+ attributes.getValue(i));
			}
		}
	}
	
	//获取文本信息
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
        //(2)不管在startElement到endElement的过程中，执行了多少次characters， 都会将内容添加到StringBuilder中，不会丢失内容  
        sb.append(ch, start, length);
	}
	
	//遍历结束标签
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
        //(4)原来在characters中取值，现改在此取值  
        String value = sb.toString();  
          
        if("V".equals(qName) || "N".equals(qName)) {
    		System.out.println("--endElement()\t\t"+value);
        }else if("Object".equals(qName)){
    		System.out.println("--endElement()\tObject");
        }else if("FieldName".equals(qName)){
    		System.out.println("--endElement()\tFieldName");
        }
	}

	//解析结束
	@Override
	public void endDocument() throws SAXException {
		System.out.println("--endDocument()--");
	}
	
	public static void main(String[] args) {
		File file = new File("E:\\20160631");
		File[] tempList = file.listFiles();
		for(int i=0;i<tempList.length;i++){
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try {
				SAXParser parse = factory.newSAXParser();
				SAXParseHandler handler = new SAXParseHandler();
				parse.parse(tempList[i], handler);
			} catch (Exception e) {    
				e.printStackTrace();
			}
		}	
	}
}
