package com.yw56.javaservice;

import java.util.ArrayList;
/**
 * 所有的java服务信息的包装类
 * @author djl20
 *
 */
public class JavaServiceEntity {

	private static ArrayList<JavaServiceEntity> data;

	public static ArrayList<JavaServiceEntity> getData() {
		if (data == null) {
			data = new ArrayList<>();
			data.add(getHttpServiceEntity());
			data.add(getExtJSServiceEntity());

		}
		return data;
	}

	private static JavaServiceEntity getHttpServiceEntity() {
		JavaServiceEntity entity = new JavaServiceEntity();
		entity.setClassName(HttpService.class.getName());
		entity.setDes("支持http请求的服务");
		entity.setName("http-service");
		entity.getFields().add(new Field("url", "", "请求地址", FieldType.string));
		entity.getFields().add(new Field("request-method", "get,post", "请求方法", FieldType.Enum));
		entity.getFields().add(new Field("headers", "", "请求头", FieldType.map));
		entity.getFields().add(new Field("MediaType", "json,form", "请求的数据格式", FieldType.Enum));
		entity.getFields().add(new Field("postBody", "", "请求的数据", FieldType.map));
		return entity;
	}

	private static JavaServiceEntity getExtJSServiceEntity() {
		JavaServiceEntity entity = new JavaServiceEntity();
		entity.setClassName(ExtJSService.class.getName());
		entity.setDes("支持extjs系统已有的服务");
		entity.setName("extjs-service");
		entity.getFields().add(new Field("serviceId", "dataUrl", "extjs中服务的id", FieldType.EnumByUrlData));
		entity.getFields().add(new Field("postBody", "dataUrl", "请求的数据", FieldType.mapByUrlData));
		return entity;
	}

	private String name;
	private String className;
	private String des;
	private ArrayList<Field> fields = new ArrayList<JavaServiceEntity.Field>();

	public JavaServiceEntity(String name, String className, String des, ArrayList<Field> fields) {
		super();
		this.name = name;
		this.className = className;
		this.des = des;
		this.fields = fields;
	}

	public JavaServiceEntity() {
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public static class Field {
		private String name;
		private String value;
		private String des;
		private FieldType type = FieldType.string;

		public Field() {
			super();
		}

		public Field(String name, String value, String des, FieldType type) {
			super();
			this.name = name;
			this.value = value;
			this.des = des;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public FieldType getType() {
			return type;
		}

		public void setType(FieldType type) {
			this.type = type;
		}

	}

	public static enum FieldType {
		map, list, string, Enum,
		// 需要通过网络获取enum数据的类型，此时value为获取列表的接口
		EnumByUrlData,
		// 需要通过网络获取map数据的类型，此时value为获取列表的接口
		mapByUrlData
	}

}
