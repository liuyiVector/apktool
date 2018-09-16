package cn.edu.pku.vector.parser;

import cn.edu.pku.vector.utils.APKUtil;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.File;
import java.util.*;
import java.util.stream.StreamSupport;

public class LayoutParser extends BaseParser {

    boolean containWebView;

    public Set<String> selfDefinedView;


    public Map<String, Boolean> isWebView;

    public LayoutParser(String xmlPath) {
        super(xmlPath);
        selfDefinedView = new HashSet<>();
    }
    @Override
    public void parseNode(Element element){
        String name = element.getName();
        if(name.contains(".")){
            //TODO 非relativelayout之类的控件，是第三方的控件或者自定义View
            selfDefinedView.add(name);
            if(isWebView.getOrDefault(name, false)){
                System.out.println("webView: " + xmlPath + " " + name);
                this.containWebView = true;
            }
        }

        if(name.contains("WebView")){
            this.containWebView = true;
            System.out.println("webView: " + xmlPath + " " + name);
        }

        List<Attribute> attributes = element.attributes();
        for(Attribute attribute : attributes){
            String attName = attribute.getNamespacePrefix() + ":" + attribute.getName();
            String attValue = attribute.getText();
            //TODO 判断涉及到了drawable和layout的资源
            if(attValue.startsWith("@layout")){
                //TODO 还依赖其他的页面，继续分析
                String includePath = APKUtil.getLayoutPath(xmlPath, attValue.replace("@layout/",""));
                LayoutParser layoutParser = new LayoutParser(includePath);
                layoutParser.isWebView = isWebView;
                layoutParser.parseNode(layoutParser.root);
                this.containWebView |= layoutParser.containWebView;
            }
        }

        for(Object child : element.elements()){
            Element childEle = (Element) child;
            parseNode(childEle);
        }
    }


    public static Set<String> parseLayout(String path){
        LayoutParser parser = new LayoutParser(path);
        parser.parseNode(parser.root);
        return parser.selfDefinedView;
    }


    public static Set<String> parseLayouts(String folder){
        Set<String> sdViews = new HashSet<>();
        File file = new File(folder);
        if(file.isDirectory()){
            String[] items = file.list();
            for(String item : items){
                if(item.endsWith(".xml")){
                    String target = folder + File.separator + item;
                    Set<String> sd = parseLayout(target);
                    sdViews.addAll(sd);
                }
            }
        }
        for(String item : sdViews){
            System.out.println(item);
        }
        return sdViews;
    }

    public static void main(String[] args){
        String apk = "/Users/vector/Desktop/testFolder/com.douban.movie.apk";
        String testFolder = "/Users/vector/Desktop/testFolder/com.douban.movie/res/layout";
        Set<String> sdViews = parseLayouts(testFolder);
        DexParser dexParser = new DexParser(apk);
        dexParser.printInfo(sdViews);

    }
}
