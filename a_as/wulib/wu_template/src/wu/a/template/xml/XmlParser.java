package wu.a.template.xml;

import java.io.InputStream;
import java.util.List;


public interface XmlParser<T> {
	/**
	 * 解析输入流 得到Book对象集合
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public List<T> parse(InputStream is) throws Exception;

	/**
	 * 序列化Book对象集合 得到XML形式的字符串
	 * 
	 * @param T
	 * @return
	 * @throws Exception
	 */
	public String serialize(List<T> books) throws Exception;
}
