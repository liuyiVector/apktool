package cn.edu.pku.vector.model;

import org.dom4j.Element;

public class Resource {

    public enum ResourceType {LAYOUT, IMAGE, ANIM};
    String name;
    String path;


    public void setName(){
        if(!path.contains("/")) {
            String[] tmp = path.split("/");
            name = tmp[tmp.length - 1];
        }else{
            name = path;
        }

    }
    public String getName(){
        return name;
    }

    public Resource(String path) {
        this.path = path;
        setName();
    }
}
