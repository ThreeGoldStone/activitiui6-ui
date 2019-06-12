package com.yw56.javaservice.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Classname JsonTreeBean
 * @Description 用于描述json数据结构
 * @Date 2019/6/11 18:16
 * @Created by djl20
 */
public class JsonTreeBean {
    /**
     * 字段名
     */
    private String name;
    /**
     * 数据类型
     */
    private JsonNodeType type;
    /**
     * 数据路径
     */
    private String path;
    /**
     * 数据样例值
     */
    private Object templateValue;
    /**
     * 属相列表，OBJ 和list有此类型
     */
    private ArrayList<JsonTreeBean> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<JsonTreeBean> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<JsonTreeBean> children) {
        this.children = children;
    }

    public JsonNodeType getType() {
        return type;
    }

    public void setType(JsonNodeType type) {
        this.type = type;
    }

    public Object getTemplateValue() {
        return templateValue;
    }

    public void setTemplateValue(Object templateValue) {
        this.templateValue = templateValue;
    }

    /**
     * 递归遍历生成json树状结构描述
     * 自动区分array和object
     *
     * @param json
     * @return
     */
    public static ArrayList<JsonTreeBean> parse(String json) {
        if (json.startsWith("[")) {
            return parseArray(json);
        } else {
            return parseObject(json);
        }
    }

    public static ArrayList<JsonTreeBean> parseObject(String json) {
        JSONObject object = JSON.parseObject(json);
        return parseObject("", object);
    }

    public static ArrayList<JsonTreeBean> parseArray(String json) {
        JSONArray array = JSON.parseArray(json);
        return parseArray("", array);
    }

    public static ArrayList<JsonTreeBean> parseObject(String parentPath, JSONObject object) {
        if (parentPath == null) parentPath = "";
        ArrayList<JsonTreeBean> treeBeans = new ArrayList<>();
        for (Map.Entry<String, Object> stringObjectEntry : object.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if (value != null) {
                JsonTreeBean treeBean = parseJsonTreeBean(parentPath, key, value);
                if (treeBean == null) continue;
                treeBeans.add(treeBean);
            }
        }
        return treeBeans;
    }

    /**
     * 解析json数组
     *
     * @param parentPath
     * @param value
     * @return
     */
    public static ArrayList<JsonTreeBean> parseArray(String parentPath, JSONArray value) {
        if (parentPath == null) parentPath = "";
        if (value != null && value.size() > 0) {
            ArrayList<JsonTreeBean> list = new ArrayList<>();
            for (int i = 0; i < value.size(); i++) {
                Object oo = value.get(i);
                JsonTreeBean treeBean = parseJsonTreeBean(parentPath, "" + i, oo);
                list.add(treeBean);
            }
            return list;

        }
        return null;
    }

    /**
     * 解析一个jsonObject 的一个子属相 或 JsonArray 的一条子类
     *
     * @param parentPath 父路径
     * @param key        属相key，或array的index
     * @param value      属相的值，或者array的index对应的值
     * @return 一个子属相的树状描述
     */
    private static JsonTreeBean parseJsonTreeBean(String parentPath, String key, Object value) {
        JsonTreeBean treeBean = new JsonTreeBean();
        treeBean.setName(key);
        String path = parentPath + "." + key;
        treeBean.setPath(path);
        treeBean.setTemplateValue(value);
//                System.out.println("key "+key+"= "+value.getClass().getName());
        JsonNodeType type = parseJsonNodeType(value);
        if (type == null) {
            System.out.println("json树状解析，未知类型，跳过" + "key " + key + "= " + value.getClass().getName());
            return null;
        }
        switch (type) {
            case obj:
                JSONObject value1 = (JSONObject) value;
                JSONObject clone = (JSONObject) value1.clone();
                treeBean.setTemplateValue(clone);
                ArrayList<JsonTreeBean> objChildren = parseObject(path, value1);
                treeBean.setChildren(objChildren);
                break;
            case list:
                JSONArray array = (JSONArray) value;
                JSONArray arrayClone = (JSONArray) array.clone();
                treeBean.setTemplateValue(arrayClone);
                ArrayList<JsonTreeBean> arrayChildren = parseArray(path, array);
                treeBean.setChildren(arrayChildren);
                break;
        }
        treeBean.setType(type);
        return treeBean;
    }

    /**
     * 根据值的类型返回JsonNodeType
     *
     * @param value
     * @return
     */
    private static JsonNodeType parseJsonNodeType(Object value) {
        JsonNodeType type = null;
        if (value instanceof BigDecimal) {
            type = JsonNodeType.number;
        } else if (value instanceof Boolean) {
            type = JsonNodeType.bool;
        } else if (value instanceof Long || value instanceof Integer) {
            type = JsonNodeType.Int;
        } else if (value instanceof JSONArray) {
            type = JsonNodeType.list;
        } else if (value instanceof JSONObject) {
            type = JsonNodeType.obj;
        } else if (value instanceof String) {
            type = JsonNodeType.string;
        }
        return type;
    }


}
