package cn.edu.pku.vector.parser;

import cn.edu.pku.vector.utils.APKUtil;
import cn.edu.pku.vector.utils.Utils;
import com.googlecode.d2j.Field;
import com.googlecode.d2j.Method;
import com.googlecode.d2j.node.DexClassNode;
import com.googlecode.d2j.node.DexFieldNode;
import com.googlecode.d2j.node.DexFileNode;
import com.googlecode.d2j.node.DexMethodNode;
import com.googlecode.d2j.reader.DexFileReader;
import com.googlecode.d2j.reader.Op;
import com.googlecode.d2j.visitors.DexCodeVisitor;
import com.googlecode.d2j.visitors.DexMethodVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DexParser {

    public String packageName;
    public String apk;
    public HashMap<String, String> relationships;
    HashMap<String, PublicParser.ResourceInfo> layouts;
    HashMap<String, List<PublicParser.ResourceInfo>> act2layouts;
    List<String> activities;
    String nowAct;

    public DexParser(String apk) {
        this.apk = apk;
        relationships = new HashMap<>();
    }


    public HashMap<String, Boolean> getWebViews(){
        HashMap<String, Boolean> isWebViews = new HashMap<>();
        for(String key : relationships.keySet()){
            if(isWebView(key)){
                isWebViews.put(Utils.retransClassName(key), true);
            }
        }
        return isWebViews;
    }

    void processAPK(HashMap<String, PublicParser.ResourceInfo> layouts, List<String> activities){
        this.layouts = layouts;
        this.activities = activities;
        this.act2layouts = new HashMap<>();
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(apk));
            processZip(zin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void processZip(ZipInputStream zin) throws IOException {
        ZipEntry ze;
        for (; (ze = zin.getNextEntry()) != null;){
            String entryName = ze.getName();
            if(entryName.endsWith(".dex") && entryName.startsWith("classes")){
                //解析dex文件，先考虑只有一个dex的情况
                DexFileReader dfr = new DexFileReader(zin);
                DexFileNode dfn = new DexFileNode();
                dfr.accept(dfn);
                analyze(dfn);
            }
        }
        //System.out.println("find classes: " + relationships.keySet().size());
    }

    void analyze(DexFileNode dfn){
        for(DexClassNode dcn : dfn.clzs){
            String parent = dcn.superClass;
            //找到继承关系
            relationships.put(dcn.className, parent);
            String nName = Utils.retransClassName(dcn.className);
            if(activities.contains(nName)){
                System.out.println(nName);
                nowAct = nName;
                getLayout(dcn);
            }
        }
    }


    //获取到自定义控件对应的安卓定义的控件
    String getAncestor(String name){
        if(name == null)
            return null;
        if(name.startsWith("Landroid")){
            return name;
        }
        return getAncestor(relationships.get(name));
    }


    public boolean isWebView(String sdView){
        String parent = getParent(sdView);
        return "android.webkit.WebView".equals(parent);
    }


    public String getParent(String sdView){
        String cn = Utils.transClassName(sdView);
        return Utils.retransClassName(getAncestor(cn));
    }

    public void printInfo(Set<String> sdViews){
        for(String sdView : sdViews){
            String cn = Utils.transClassName(sdView);
            System.out.println(cn + "  " + Utils.retransClassName(getAncestor(cn)));
        }
    }


    class MyMethodVisitor extends DexMethodVisitor{
        @Override
        public DexCodeVisitor visitCode() {
            return new MyCodeVisitor();
        }

    }


    class MyCodeVisitor extends DexCodeVisitor{

        @Override
        public void visitTypeStmt(Op op, int a, int b, String type) {
            super.visitTypeStmt(op, a, b, type);
        }

        @Override
        public void visitConstStmt(Op op, int ra, Object value) {
            super.visitConstStmt(op, ra, value);
            if (op == Op.CONST_CLASS) {

            }else if(op == Op.CONST){
                if(value instanceof Integer) {
                    String id = "0x" + Integer.toHexString((Integer) value);
                    if(layouts.containsKey(id)){
                        List<PublicParser.ResourceInfo> list = act2layouts.getOrDefault(id, new ArrayList<>());
                        list.add(layouts.get(id));
                        act2layouts.put(nowAct, list);
                    }
                }
            }
        }

        @Override
        public void visitFieldStmt(Op op, int a, int b, Field field) {
            super.visitFieldStmt(op, a, b, field);
        }

        @Override
        public void visitMethodStmt(Op op, int[] args, Method method) {
            super.visitMethodStmt(op, args, method);
        }
    }



    List<String> getLayout(DexClassNode dcn){

        List<DexFieldNode> dexFileNode = dcn.fields;
        List<DexMethodNode> dexMethodNodes = dcn.methods;
        for(DexMethodNode dexMethodNode : dexMethodNodes){
            dexMethodNode.accept(new MyMethodVisitor());
        }
        return null;
    }


    public static void main(String[] args){
        //String testPath = "/Users/vector/Desktop/testFolder/com.douban.movie";
        String testPath = "/Users/vector/Desktop/testFolder/com.zongheng.reader";
        String layoutPath = testPath + "/res/layout/";
        DexParser dexParser = new DexParser(testPath+".apk");
        List<PublicParser.ResourceInfo> resourceInfos = PublicParser.getResourceInfo(testPath);
        HashMap<String, PublicParser.ResourceInfo> layoutMaps = new HashMap<>();
        for(PublicParser.ResourceInfo resourceInfo : resourceInfos){
            layoutMaps.put(resourceInfo.id, resourceInfo);
        }

        List<String> activities = ManifestParser.getActivities(testPath);
        dexParser.processAPK(layoutMaps, activities);
        HashMap<String, List<PublicParser.ResourceInfo>> act2layouts = dexParser.act2layouts;
        HashMap<String, Boolean> isWebViews = dexParser.getWebViews();


        HashMap<String, Boolean> layout2WebView = new HashMap<>();



        //get layouts that contain WebView
        File file = new File(layoutPath);
        if(file.isDirectory()){
            String[] items = file.list();
            for(String item : items){
                if(item.endsWith(".xml")){
                    String target = layoutPath + File.separator + item;
                    LayoutParser parser = new LayoutParser(target);
                    parser.isWebView = isWebViews;
                    parser.parseNode(parser.root);
                    if(parser.containWebView){
                        System.out.println(item + " has WebView");
                        layout2WebView.put(item, true);
                    }else{
                        layout2WebView.put(item, false);
                    }
                }
            }
        }


        //get activities that contain WebView, but it is insufficient to figure out layouts that an activity contains
        for(String key : act2layouts.keySet()){
            List<PublicParser.ResourceInfo> list = act2layouts.get(key);
            for(PublicParser.ResourceInfo resourceInfo : list){
                if(layout2WebView.getOrDefault(resourceInfo.name+".xml", false)){
                    System.out.println(key + " has WebView");
                }
            }
        }
    }


}
