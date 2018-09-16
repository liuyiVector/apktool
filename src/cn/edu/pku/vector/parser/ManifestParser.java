package cn.edu.pku.vector.parser;

import cn.edu.pku.vector.model.Activity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManifestParser {
	
	
	public static String trans(String package_name){
		String result = package_name.replace(".", "/");
		result  = "L" + result + ";";
		return result;
	}
	
	@SuppressWarnings("finally")
	public static String getApplication(String appPath){
		String manifestPath = appPath + File.separator + "AndroidManifest.xml";
		String package_name;
		SAXReader reader = new SAXReader();
		String applicationName = null;
		Document document;
		try {
			document = reader.read(new File(manifestPath));
			Element root = document.getRootElement();
			String rootName = root.getName();
			package_name = root.attributeValue("package");
			Element applicationElement = root.element("application");
			applicationName = applicationElement.attributeValue("name");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return applicationName;
		}
	}
	
	
	@SuppressWarnings("finally")
	public static String getPackageName(String appPath){
		String manifestPath = appPath + File.separator + "AndroidManifest.xml";
		String package_name = null;
		SAXReader reader = new SAXReader();
		String applicationName = null;
		Document document;
		try {
			document = reader.read(new File(manifestPath));
			Element root = document.getRootElement();
			String rootName = root.getName();
			package_name = root.attributeValue("package");
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return package_name;
		}
	}


    public static List<Activity> getActivitiesWithTheme(String appPath){
        String manifestPath = appPath + File.separator + "AndroidManifest.xml";
        List<Activity> activities = new ArrayList<>();
        String package_name;

        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(new File(manifestPath));
            Element root = document.getRootElement();
            String rootName = root.getName();
            package_name = root.attributeValue("package");

            Element applicationElement = root.element("application");

            List<Element> activityElements = applicationElement.elements("activity");
            for(int i = 0; i < activityElements.size(); i++){
                Element activity = activityElements.get(i);
                //TODO style
                String theme = activity.attributeValue("theme");
                String name = activity.attributeValue("name");
                if(name.startsWith(".")){
                    name = package_name + name;
                }
                activities.add(new Activity(name,theme));
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            return activities;
        }
    }


    public static String[] getLauncherActivity(String appPath){
        String manifestPath = appPath + File.separator + "AndroidManifest.xml";
        String package_name;
        String[] results = {"",""};

        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(new File(manifestPath));
            Element root = document.getRootElement();
            String rootName = root.getName();
            package_name = root.attributeValue("package");


            Element applicationElement = root.element("application");
            String aname = applicationElement.attributeValue("name");
            if(aname != null && aname.startsWith(".")){
                aname = package_name + aname;
            }
            results[0] = aname;

            List<Element> activityElements = applicationElement.elements("activity");
            for(int i = 0; i < activityElements.size(); i++){
                Element activity = activityElements.get(i);
                String name = activity.attributeValue("name");
                if(name.startsWith(".")){
                    name = package_name + name;
                }
                List<Element> intent_filters = activity.elements("intent-filter");
                if(intent_filters == null)
                    continue;
                for(int j = 0; j < intent_filters.size(); j++){
                    Element intent_filter = intent_filters.get(j);

                    String content = intent_filter.asXML();
                    //System.out.println(content);
                    if(content.contains("android.intent.action.MAIN") && content.contains("android.intent.category.LAUNCHER")){
                        results[1] = name;
                        return results;
                    }
                }

            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
	
	
	@SuppressWarnings("finally")
	public static List<String> getActivities(String appPath){
		String manifestPath = appPath + File.separator + "AndroidManifest.xml";
		List<String> activities = new ArrayList<>();
		String package_name;
		
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(new File(manifestPath));
			Element root = document.getRootElement();
			String rootName = root.getName();
			package_name = root.attributeValue("package");
			
			Element applicationElement = root.element("application");
			
			List<Element> activityElements = applicationElement.elements("activity");
			for(int i = 0; i < activityElements.size(); i++){
				Element activity = activityElements.get(i);
				//TODO style
				String theme = activity.attributeValue("theme");
				String name = activity.attributeValue("name");
				if(name.startsWith(".")){
					name = package_name + name;
				}
				activities.add(name);
			}
			for(String activity : activities){
//				System.out.println(activity);
//				System.out.println(trans(activity));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return activities;
		}
	}
	
	public static void main(String[] args){
		ManifestParser.getActivities("/Users/vector/Desktop/com.douban.movie");
		//System.out.println(ManifestParser.getApplication("/Users/vector/Desktop/com.douban.movie"));
	}

}
