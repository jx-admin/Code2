package wu.a.template.xml;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * <pre>
 * SAX解析器：
 *         SAX(Simple API for XML)解析器是一种基于事件的解析器，它的核心是事件处理模式，主要是围绕着事件源以及事件处理器来工作的
 *         。当事件源产生事件后，调用事件处理器相应的处理方法
 *         ，一个事件就可以得到处理。在事件源调用事件处理器中特定方法的时候，还要传递给事件处理器相应事件的状态信息
 *         ，这样事件处理器才能够根据提供的事件信息来决定自己的行为。
 *         SAX解析器的优点是解析速度快，占用内存少。非常适合在Android移动设备中使用。
 * DOM解析器：
 *         DOM是基于树形结构的的节点或信息片段的集合，允许开发人员使用DOM
 *         API遍历XML树、检索所需数据。分析该结构通常需要加载整个文档和构造树形结构，然后才可以检索和更新节点信息。
 *         由于DOM在内存中以树形结构存放，因此检索和更新效率会更高。但是对于特别大的文档，解析和加载整个文档将会很耗资源。 
 * PULL解析器：
 *         PULL解析器的运行方式和SAX类似，都是基于事件的模式。不同的是，在PULL解析过程中，我们需要自己获取产生的事件然后做相应的操作，
 *         而不像SAX那样由处理器触发一种事件的方法
 *         ，执行我们的代码。PULL解析器小巧轻便，解析速度快，简单易用，非常适合在Android移动设备中使用
 *         ，Android系统内部在解析各种XML时也是用PULL解析器。
 *         
 *         对于这三种解析器各有优点，我个人比较倾向于PULL解析器，因为SAX解析器操作起来太笨重，DOM不适合文档较大，内存较小的场景，唯有PULL轻巧灵活，
 *         速度快，占用内存小，使用非常顺手。读者也可以根据自己的喜好选择相应的解析技术。
 * @author junxu.wang 
 * @date : 2015年4月9日 下午5:07:33
 *
 */
public class XMLParserSerializerActivity extends Activity {

	private static final String TAG = "XML";

	private XmlParser<Book> parser;
	private List<Book> books;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xml_parser_serialize_layout);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sax_parser_btn:
			try {
				InputStream is = getAssets().open("books.xml");
				parser = new SaxBookParser(); // 创建SaxBookParser实例
				books = parser.parse(is); // 解析输入流
				for (Book book : books) {
					Log.i(TAG, book.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.sax_serializer:
			try {
				String xml = parser.serialize(books); // 序列化
				FileOutputStream fos = openFileOutput("books.xml",
						Context.MODE_PRIVATE);
				fos.write(xml.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.dom_parser_btn:
			try {
				InputStream is = getAssets().open("books.xml");
				parser = new DomBookParser();
				books = parser.parse(is); // 解析输入流
				for (Book book : books) {
					Log.i(TAG, book.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.dom_serializer_btn:
			try {
				String xml = parser.serialize(books); // 序列化
				FileOutputStream fos = openFileOutput("books.xml",
						Context.MODE_PRIVATE);
				fos.write(xml.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.pull_parser_btn:
			try {
				InputStream is = getAssets().open("books.xml");
				parser = new PullBookParser();
				books = parser.parse(is); // 解析输入流
				for (Book book : books) {
					Log.i(TAG, book.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.pull_serializer_btn:
			try {
				String xml = parser.serialize(books); // 序列化
				FileOutputStream fos = openFileOutput("books.xml",
						Context.MODE_PRIVATE);
				fos.write(xml.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
