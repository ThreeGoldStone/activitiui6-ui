package com.yw56.javaservice;
/**
 * java服务中的ext服务的信息包装类
 * @author djl20
 *
 */

import java.util.HashMap;

public class ExtJSServiceEntity {
	private String name;
	private String id;
	private String des;
	private HashMap<String, Object> bodyDatas = new HashMap<String, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public HashMap<String, Object> getBodyDatas() {
		return bodyDatas;
	}

	public void setBodyDatas(HashMap<String, Object> bodyDatas) {
		this.bodyDatas = bodyDatas;
	}

}
