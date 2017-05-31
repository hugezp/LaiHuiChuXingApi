package com.lhcx.utils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Created by zhu on 2016/12/30.
 */
public class XmlParse {
  
    public static Map<String,String> parse(String protocolXML) {
        Map<String,String>  paraMap=new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder
                    .parse(new InputSource(new StringReader(protocolXML)));

            Element root = doc.getDocumentElement();
            NodeList books = root.getChildNodes();
            if (books != null) {
                for (int i = 0; i < books.getLength(); i++) {
                    Node book = books.item(i);
                    String key=book.getNodeName();
                    String value=book.getNodeValue();
                    if(!key.equals("#text")){
                        paraMap.put(key,value);
                        System.out.println(key+ " : " + value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paraMap;
    }
}
