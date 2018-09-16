package cn.edu.pku.vector.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDUtil {
    public static String executeCmd(String cmd){
        System.out.println("to execute cmd: " + cmd);
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }finally {
            return "error";
        }
    }

    public static void executeCmdWithoutBlock(String cmd) throws IOException, InterruptedException {
        Process p0 = Runtime.getRuntime().exec(cmd);
        //读取标准输出流
        BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(p0.getInputStream()));
        String line;
        while ((line=bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        //读取标准错误流
        BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "gb2312"));
        String errline = null;
        while ((errline = brError.readLine()) != null) {
            System.out.println(errline);
        }
        //waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
        int c=p0.waitFor();
        if(c!=0){
            System.out.println("====execute error====");
        }
    }
}
