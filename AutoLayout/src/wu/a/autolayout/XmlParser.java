package wu.a.autolayout;

import java.io.InputStream;


/**
 * Xml parser 序列与反序列接口
 * @author junxu.wang
 *
 * @date : 2015年4月28日 下午12:04:07
 *
 * @param <T>
 */
public interface XmlParser<T> {
	/**
	 * 解析输入,得到T对象
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public T parse(InputStream is) throws Exception;

	/**
	 * 序列化T对象集合 得到XML形式的字符串
	 * 
	 * @param T
	 * @return
	 * @throws Exception
	 */
	public String serialize() throws Exception;
}
