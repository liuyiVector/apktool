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
        className = className.replace("/",".").replace(";","");
        return className;
    }

    public static String join( Object[] o , String flag ){
        StringBuffer str_buff = new StringBuffer();

        for(int i=0 , len=o.length ; i<len ; i++){
            str_buff.append( String.valueOf( o[i] ) );
            if(i<len-1)str_buff.append( flag );
        }

        return str_buff.toString();
    }

}
