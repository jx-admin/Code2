package wu.a.lib.data.json;



import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.PropertyFilter;

/**
 * * <b>function:</b> 用json-lib转换java对象到JSON字符串 *
 * 读取json字符串到java对象，序列化jsonObject到xml * json-lib-version: json-lib-2.3-jdk15.jar
 * * 依赖包: * commons-beanutils.jar * commons-collections-3.2.jar *
 * ezmorph-1.0.3.jar * commons-lang.jar * commons-logging.jar * @author hoojo * @createDate
 * Nov 28, 2010 2:28:39 PM * @file JsonlibTest.java * @package com.hoo.test * @project
 * WebHttpUtils * @blog http://blog.csdn.net/IBM_hoojo * @email hoojo_@126.com * @version
 * 1.0
 */
public class JsonlibTest {
	// private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private Student bean = null;

	public JsonlibTest() {
		// jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		bean = new Student();
		bean.setAddress("address");
		bean.setEmail("email");
		bean.setId(1);
		bean.setName("haha");
		Birthday day = new Birthday();
		day.setBirthday("2010-11-22");
		bean.setBirthday(day);
	}

	public void destory() {
		// jsonArray = null;
		jsonObject = null;
		bean = null;
		System.gc();
	}

	public final void fail(String string) {
		System.out.println(string);
	}

	public final void failRed(String string) {
		System.err.println(string);
	}

	public void parse() {
		fail("==============Java Bean >>> JSON Object==================");
		fail(JSONObject.fromObject(bean).toString());
		fail("==============Java Bean >>> JSON Array==================");
		fail(JSONArray.fromObject(bean).toString());
		// array会在最外层套上[]
		fail("==============Java Bean >>> JSON Object ==================");
		fail(JSONSerializer.toJSON(bean).toString());
		fail("========================JsonConfig========================");
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Birthday.class,
				new JsonValueProcessor() {
					public Object processArrayValue(Object value,
							JsonConfig jsonConfig) {
						if (value == null) {
							return new Date();
						}
						return value;
					}

					public Object processObjectValue(String key, Object value,
							JsonConfig jsonConfig) {
						fail("key:" + key);
						return value + "##修改过的日期";
					}
				});
		jsonObject = JSONObject.fromObject(bean, jsonConfig);
		fail(jsonObject.toString());
		Student student = (Student) JSONObject
				.toBean(jsonObject, Student.class);
		fail(jsonObject.getString("birthday"));
		fail(student.toString());
		fail("#####################JsonPropertyFilter############################");
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				fail(source + "%%%" + name + "--" + value);
				// 忽略birthday属性
				if (value != null
						&& Birthday.class.isAssignableFrom(value.getClass())) {
					return true;
				}
				return false;
			}
		});
		fail(JSONObject.fromObject(bean, jsonConfig).toString());
		fail("#################JavaPropertyFilter##################");
		jsonConfig.setRootClass(Student.class);
		jsonConfig.setJavaPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				fail(name + "@" + value + "#" + source);
				if ("id".equals(name) || "email".equals(name)) {
					value = name + "@@";
					return true;
				}
				return false;
			}
		});
		// jsonObject = JSONObject.fromObject(bean, jsonConfig);
		// student = (Student) JSONObject.toBean(jsonObject, Student.class);
		// fail(student.toString());
		student = (Student) JSONObject.toBean(jsonObject, jsonConfig);
		fail("Student:" + student.toString());
	}
}
