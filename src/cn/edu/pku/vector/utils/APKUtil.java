package cn.edu.pku.vector.utils;

public class APKUtil {
    public static final String APKTOOL = "/usr/local/bin/apktool";
    public static boolean decompile(String apkPath, String targetFolder){
        if(!apkPath.endsWith(".apk")){
            System.out.println("not a apk file, exist");
            return false;
        }
        String[] tmps = apkPath.split("/");
        String apkName = tmps[tmps.length - 1];
        if(targetFolder == null){
            targetFolder = apkPath.replace(apkName, "");
        }
        String cmdStr = "sh " + APKTOOL  + " -f d " + apkPath + " -o " + targetFolder;
        String output = CMDUtil.executeCmd(cmdStr);
        System.out.print(output);
        if(output.contains("I: Copying assets and libs...\n" +
                "I: Copying unknown files...\n" +
                "I: Copying original files..."))
            return true;
        return false;
    }


    public static void recompile(String sourceFolder, String targetFoler){
        String cmdStr = "sh " + APKTOOL  + " b " + sourceFolder + " -o " + targetFoler;
        CMDUtil.executeCmd(cmdStr);
    }
}
