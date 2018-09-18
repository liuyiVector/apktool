import cn.edu.pku.vector.parser.DexParser;
import cn.edu.pku.vector.parser.LayoutParser;

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

    public static String[][] targetFolder = new String[][]{
            {"com-sankuai-meituan_2ad28d12d916aa92484356ffda41c745", "com-youku-phone_85842b07729d99c345943b1accbe4951", "com-wochacha_ca0593c7574a74ae902df07365e2f661", "PackageName_MD5", "com-kiloo-subwaysurf_b2d6a1b38ed56a732a174f9f594d4904", "com-qiyi-video_82000a7a63f56cb546e9be21bece0d92", "com-google-android-apps-maps_44b89ecbbd77113d0d1535239df9ac6f", "com-UCMobile_9b646c28b5b74ffc5bbdd3ab17b0c7c5", "vStudio-Android-Camera360_f95fa230f170cd2bdb817a30e13a862a", "com-snda-wifilocating_7a19cc2765781efc457fe83252e76be1", "com-storm-smart_b095fd31e456a7a066a62a619c5df416", "com-gau-go-launcherex_81a7524631df2e066e11ed165ba642c6", "com-tencent-qqmusic_923450ba85f2a99a8fd3116287e83cf4", "com-sina-weibo_de37789380ca443a4607c9d8f282d0dc", "com-eg-android-AlipayGphone_7c4172fd83308b084eddc12b2bde22e3", "com-youdao-dict_d768039b8d853237d5bf0cd5972b836e", "cn-kuwo-player_d648cc23b45a43fc58e89649bbe102df", "com-pplive-androidphone_5967a4ad2b78dc7c69efa9bfaa1d7367", "com-changba_b4963ff6ab481acf06ee6419f6a5d242", "com-estrongs-android-pop_8fd9787c72934f9d95c492afd00181e1", "com-moji-mjweather_f222204e28e58a742fb749b063e75980", "com-baidu-video_389deaa2433f8ad5b681541986832cee", "com-taobao-taobao_1f78e9891bbbc58516eb94b7f34efdf6", "com-ijinshan-kbatterydoctor_d98785d62b0cac1d37a47ee391e89861", "com-autonavi-minimap_d65e302626ecff4a87e0c33e3212c977", "com-halfbrick-fruitninja_c349b73d6964abbed41f2080b62b96bc", "com-tencent-mtt_3258d779236f2b2d09afd58fdd873c8a", "com-tencent-mm_e2aa12c9f231d40a772d85c55e0e2bbc", "com-adobe-flashplayer_eab156b0fa36ee0f18487fd6e0286148", "org-cocos2dx-FishGame_5d93075a93440d6b4d1d5899aa996c40", "com-kugou-android_810827d14b86c9e92485216f4640a1c9", "cn-com-fetion_70c3d6632e8a1934387e78ad0d02daa3", "com-baidu-input_482ac5e1d0a79882f2c094eb3c5dd299", "com-dianping-v1_282ba9817da512d22d3ebfa10fc974c1", "com-tencent-qqpimsecure_0340bab83da255ad4d8874f5b7b621a5", "com-imangi-templerun2_3b8019a16d016f7085ca7581368e102c", "com-tencent-qqlive_1ced1a6456e6e538db1aa489e7485326", "cn-etouch-ecalendar_f47b13464d1c9089556d6e008635ca99", "com-chaozh-iReaderFree_7ae9141db9d34a649ef227ee68874c98", "com-sds-android-ttpod_20d8cf57c59768d1d17b57b79f91d6f2", "com-sohu-inputmethod-sogou_58b8c3ebfa8a596b962071a46c6c2a60", "com-immomo-momo_3bc450b8cae93527242bf356778ed605", "com-imangi-templerun_04781b0c8d88bffd7d1c7980046149ec", "com-tencent-mobileqq_803b8f2d904297586768df3f25483050", "com-renren-mobile-android_86630f00e49f542e71b8342f42528ef4", "tv-pps-mobile_27e3b845a95cfb99c6a5f9d30e21a414", "com-outfit7-talkingtom2free_b9b18fe3a9c229e05c52fb4d827c715c", "com-qzone_61fd83fd00b5bb1d97060d20420f5fab", "org-funship-findsomething-channel_91_fd1057ac0d539247cd977e040ad81f2a", "com-androidesk_6dbac9b3cf854b4179a90d2805e6013d", "com-mt-mtxx-mtxx_7b168853d7d7cabedb27ed18b3c6ae21"},
            {"com-sankuai-meituan_4f46ab5f5a7ee9080d1548b9f52552be", "com-youku-phone_7444204c5de28732685a327d1c828458", "com-wochacha_4362ffbe2eba75029948f72faa13398b", "PackageName_MD5", "com-kiloo-subwaysurf_46027f9426b1674c68288048ffe44b04", "com-qiyi-video_61b872608eb76d496ef307cce2f60b74", "com-google-android-apps-maps_0143ddbb1ee2f19df2a6463e5849c82a", "com-UCMobile_6158ce9db243fb2ec9cc78ed4c68b7b0", "vStudio-Android-Camera360_6e6db7e5692cf4af0cf77499d35cb7b6", "com-snda-wifilocating_0e9f8510055f2996cd9eb82c61d55368", "com-storm-smart_e1e202120ed769ad6a23217035e728fc", "com-gau-go-launcherex_4b91a14aa4cea94ba702c75baa80dc05", "com-tencent-qqmusic_061834f688ff9bf4debb0c1e3d89e3fb", "com-sina-weibo_25933ec85e8d3877e3fe820772206e53", "com-eg-android-AlipayGphone_53868105120595c3b294bb6677c0ea62", "com-youdao-dict_daec6b9d36ea27dab44ebe3d711e6a67", "cn-kuwo-player_9deca92a9a6806963eb770382c82a8d4", "com-pplive-androidphone_3767c14a61708b893e4568d5c9aa7653", "com-changba_80e84ed77c45c63247da29e168672788", "com-estrongs-android-pop_b499c6f677369044ba0387530bf5e127", "com-moji-mjweather_080297e41320706868dd335d7f2cb9b6", "com-baidu-video_06a30a1f96157ebb7237c2007c6e3f8d", "com-taobao-taobao_2700236f33e11e423246c281cf720887", "com-ijinshan-kbatterydoctor_72ce592d980cbd93d69370fe358dde65", "com-autonavi-minimap_a535474935f58ef9f6f94ae4a90c6dcf", "com-halfbrick-fruitninja_1f5079e29e3e465203c93455d0a681cb", "com-tencent-mtt_4fec89bd2b4ffa6ca7a7dddb835d502f", "com-tencent-mm_80d6e6123862c8eeaf9b97d03cee71b5", "com-adobe-flashplayer_af594b0d793d47b798b2c574b2f17e91", "org-cocos2dx-FishGame_77b39c613c34245e5e2c929234b9390b", "com-kugou-android_b1d73d8a30ed50437bbc8dbd6914e536", "cn-com-fetion_c9eb4e6b7b204e175dcd83d540587c21", "com-baidu-input_fffeb6a6b1f4af4286d468457fdc3d3b", "com-dianping-v1_2dc809cf8d85a92aed1509e0e59b6052", "com-tencent-qqpimsecure_125e9eaa565ceb8193fcac3805508136", "com-imangi-templerun2_d8ae1e8e7a11fe290761540ed7aa7764", "com-tencent-qqlive_fdf5b89c1ea2ac084e55bc09ee23cd8a", "cn-etouch-ecalendar_6bc5834a216ced7b134c97b217dba500", "com-chaozh-iReaderFree_3c46aced74cc11102e8b4d834e6d28d9", "com-sds-android-ttpod_c1b96242a6c215f0ff158eca48bba44f", "com-sohu-inputmethod-sogou_01ec728f2a88eca0af3facf10021f156", "com-immomo-momo_f6df44cd301746ddfb6a199a2d534c7f", "com-imangi-templerun_04781b0c8d88bffd7d1c7980046149ec", "com-tencent-mobileqq_78dedcca434e84df235be8830044b68b", "com-renren-mobile-android_c56f8812e81d1c995b2539d929110122", "tv-pps-mobile_73c2d96a3c1c3fe3005d72906c3c1795", "com-outfit7-talkingtom2free_e965cd3965d24564626b2ceba4178971", "com-qzone_a2a6c3c2484f3bcc4c57d7b9a2798a5a", "org-funship-findsomething-channel_91_1d472652e5b896b81983e9e3fe399882", "com-androidesk_103bf0cfeaa4159dedb74d6e507521c2", "com-mt-mtxx-mtxx_a5a933c2233d39c85192f4e5d749efeb"}
    };

    public static void main(String[] args) {
        for(int i = 0; i < 2; i++){
            String[] folders = targetFolder[i];
            List<Integer> maxCounts = new ArrayList<>();
            for(int j = 0; j < folders.length; j++){
                String target = "/Users/vector/Desktop/crawler/apkAnalysis/"+folders[j]+"/res/layout";
                maxCounts.add(LayoutParser.getMaxCount(target));
            }
            System.out.println(maxCounts);
        }
    }
}
