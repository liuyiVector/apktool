import cn.edu.pku.vector.parser.DexParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {



    static class Info{
        public String sdView;
        public String appName;
        public String activityNAme;
        public String parent;

        public Info(String sdView, String appName, String activityNAme) {
            this.sdView = sdView;
            this.appName = appName;
            this.activityNAme = activityNAme;
        }
    }



    static void parse(){
        String infoPath = "/Users/vector/Desktop/all_tags.txt";
        String apkFolder = "/Users/vector/Desktop/apks";
        File file=new File(infoPath);
        BufferedReader reader=null;
        String temp=null;
        HashMap<String, List<Info>> toDealWith = new HashMap<>();
        int line=1;
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                String[] tmps = temp.split("\\s+");
                Info info = new Info(tmps[0], tmps[1], tmps[2]);
                List<Info> infos = toDealWith.getOrDefault(tmps[1], new ArrayList<>());
                infos.add(info);
                toDealWith.put(tmps[1], infos);
                line++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(reader!=null){
                try{
                    reader.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        for(String key : toDealWith.keySet()){
            String apkPath = apkFolder + File.separator + key + ".apk";
            if(!new File(apkPath).exists()) {
                continue;
            }
            DexParser dexParser = new DexParser(apkPath);
            List<Info> infos = toDealWith.get(key);
            for(Info info : infos){
                String parent = dexParser.getParent(info.sdView);
                info.parent = parent;
                System.out.println(info.sdView + " " + info.appName + " " + info.activityNAme + " " + info.parent);
            }

        }

    }

    public static void main(String[] args) {
        parse();
    }
}
