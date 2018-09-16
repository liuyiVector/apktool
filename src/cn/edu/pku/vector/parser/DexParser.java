package cn.edu.pku.vector.parser;

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DexParser {

    public static List<String> activities = new ArrayList<>();
    public String packageName;
    public String apk;
    HashMap<String, String> relationships;

    public DexParser(String apk) {
        this.apk = apk;
        relationships = new HashMap<>();
        processAPK();
    }

    void processAPK(){
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
            relationships.put(dcn.className, parent);
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



    public static class MyCodeVisitor extends DexCodeVisitor{

        @Override
        public void visitTypeStmt(Op op, int a, int b, String type) {
            super.visitTypeStmt(op, a, b, type);
        }

        @Override
        public void visitConstStmt(Op op, int ra, Object value) {
            super.visitConstStmt(op, ra, value);
            if (op == Op.CONST_CLASS) {

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



        return null;
    }


}
