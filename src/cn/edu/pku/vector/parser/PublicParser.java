package cn.edu.pku.vector.parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;


public class PublicParser {
	
	//保存了R中的所有资源
	public static List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
	public static Map<String, ResourceInfo> resourceInfoMap = new HashMap<>();
	public static List<String> IGNORE_RESOURCES = Arrays.asList(new String[]{"id","attr","string","dimen","bool","integer","color","styleable"});
	public static class ResourceInfo{
		//<public type="attr" name="background" id="0x7f010000" />
		public String type;
		public String name;
		public String id;
		public long size;
		public String getAlias(){
			return type + "_" + name;
		}

		public ResourceInfo(){

        }

        public ResourceInfo(String type, String name){
		    this.type = type;
		    this.name = name;
        }

        public String toString(){
        	return type + " " + name + " " + id;
		}

        @Override
        public boolean equals(Object o) {
            ResourceInfo target = (ResourceInfo) o;
            return target.getAlias().equals(this.getAlias());
        }
    }
	

	public static List<ResourceInfo> getResourceInfo(String apkFolder){
	    resourceInfoMap.clear();
	    resourceInfos.clear();
		SAXReader reader = new SAXReader();
		String xmlPath;
		Element root;
		Document document;
		try {
			document = reader.read(new File(apkFolder + "/res/values/public.xml"));
			root = document.getRootElement();
			List<Element> publics = root.elements("public");
			for(int i = 0; i < publics.size();i++){
				if(IGNORE_RESOURCES.contains(publics.get(i).attributeValue("type"))){
					//对于一些资源我们不需要考虑
					continue;
				}
				//这里我们只要获取layout
				if("layout".equals(publics.get(i).attributeValue("type"))) {
					ResourceInfo resourceInfo = new ResourceInfo();
					resourceInfo.type = publics.get(i).attributeValue("type");
					resourceInfo.name = publics.get(i).attributeValue("name");
					resourceInfo.id = publics.get(i).attributeValue("id");
					resourceInfo.size = 0;
					resourceInfos.add(resourceInfo);
					resourceInfoMap.put(resourceInfo.getAlias(), resourceInfo);
				}
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resourceInfos;
	}

	
	public static void main(String[] args){
		//getResourceInfo();
	}
}
