package cn.edu.pku.vector.parser;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseParser {
    public String xmlPath;
    public Element root;
    Document document;

    public BaseParser(String xmlPath) {
        this.xmlPath = xmlPath;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new File(xmlPath));
            root = document.getRootElement();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //解析某个节点，获取所有的属性
    public void parseNode(Element element) {
        String name = element.getName();
        //System.out.println("element name: " + name);
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            String attName = attribute.getNamespacePrefix() + ":" + attribute.getName();
            String attValue = attribute.getText();
            //System.out.println(attName + " " + attValue);
        }

    }

    public boolean equal(BaseParser baseParser){
        return this.xmlPath.equals(baseParser.xmlPath);
    }

}
