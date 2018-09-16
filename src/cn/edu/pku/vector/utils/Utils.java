package cn.edu.pku.vector.utils;

public class Utils {

    public static String transClassName(String className){
        String result = className.replace(".", "/");
        result  = "L" + result + ";";
        return result;
    }

    public static String retransClassName(String className){
        if(className == null)
            return null;
        className = className.substring(1);
        className = className.replace("/",".");
        return className;
    }

}
