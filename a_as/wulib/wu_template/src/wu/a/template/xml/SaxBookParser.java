package wu.a.template.xml;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;


public class SaxBookParser implements XmlParser<Book> {
    private static final boolean LOG = true;
    private static final String TAG = "sax";

    @Override
    public List<Book> parse(InputStream is) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();    //取得SAXParserFactory实例
        SAXParser parser = factory.newSAXParser();                    //从factory获取SAXParser实例
        MyHandler handler = new MyHandler();                        //实例化自定义Handler
        parser.parse(is, handler);                                    //根据自定义Handler规则解析输入流
        return handler.getBooks();
    }

    public void serialize(List<Book> books, File file) throws Exception {
        Result result = new StreamResult(file);
        serialize(books, result);
    }

    @Override
    public String serialize(List<Book> books) throws Exception {

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        serialize(books, result);
        return writer.toString();

    }

    public void serialize(List<Book> books, Result result) throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();//取得SAXTransformerFactory实例
        TransformerHandler handler = factory.newTransformerHandler();            //从factory获取TransformerHandler实例
        Transformer transformer = handler.getTransformer();                        //从handler获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");    // 是否忽略XML声明
        handler.setResult(result);

        String uri = "http://schemas.android.com/apk/res/android";    //代表命名空间的URI 当URI无值时 须置为空字符串
        String localName = "";    //命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串

        handler.startDocument();
        handler.startPrefixMapping("android", "http://schemas.android.com/apk/res/android2");
        handler.startElement(uri, localName, "books", null);
        String str = "this is ignorableWitespace";
        char[] bytes = new char[str.length()];
        str.getChars(0, str.length(), bytes, 0);
        handler.ignorableWhitespace(bytes, 0, bytes.length);
        str = "this is characters";
        bytes = new char[str.length()];
        str.getChars(0, str.length(), bytes, 0);
        handler.characters(bytes, 0, bytes.length);
        str = "this is comment";
        bytes = new char[str.length()];
        str.getChars(0, str.length(), bytes, 0);
        handler.comment(bytes, 0, bytes.length);

        AttributesImpl attrs = new AttributesImpl();    //负责存放元素的属性信息
        char[] ch = null;
        for (Book book : books) {
            attrs.clear();    //清空属性列表
            attrs.addAttribute(uri, localName, "id", "string", String.valueOf(book.getId()));//添加一个名为id的属性(type影响不大,这里设为string)
            attrs.addAttribute(uri, "layout_width", "android:layout_width", "string", "wrap_content");//添加一个名为id的属性(type影响不大,这里设为string)
            handler.startElement(uri, localName, "book", attrs);    //开始一个book元素 关联上面设定的id属性

            handler.startElement(uri, localName, "name", null);    //开始一个name元素 没有属性
            ch = String.valueOf(book.getName()).toCharArray();
            handler.characters(ch, 0, ch.length);    //设置name元素的文本节点
            handler.endElement(uri, localName, "name");

            handler.startElement(uri, localName, "price", null);//开始一个price元素 没有属性
            ch = String.valueOf(book.getPrice()).toCharArray();
            handler.characters(ch, 0, ch.length);    //设置price元素的文本节点
            handler.endElement(uri, localName, "price");

            handler.endElement(uri, localName, "book");
        }
        handler.endElement(uri, localName, "books");
        handler.endPrefixMapping("android");
        handler.endDocument();
    }

    private void d(String msg) {
        Log.e(TAG, msg);
    }

    //需要重写DefaultHandler的方法
    private class MyHandler extends DefaultHandler {

        private List<Book> books;
        private Book book;
        private StringBuilder builder;
        String currentElement;

        //返回解析后得到的Book对象集合
        public List<Book> getBooks() {
            return books;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            if (LOG) {
                d("startDocument");
            }
            books = new ArrayList<Book>();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            if (LOG) {
                d(String.format("startElement uri=%s, localName=%s, qName=%s, att=%s", uri, localName, qName, attributes == null ? "0" : String.valueOf(attributes.getLength())));
                if (attributes != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = attributes.getLength() - 1; i >= 0; i--) {
                        sb.append("localName=").append(attributes.getLocalName(i))
                                .append(", qName＝").append(attributes.getQName(i))
                                .append(", vulue=").append(attributes.getValue(i))
                                .append(", type=").append(attributes.getType(i))
                                .append(", uri=").append(attributes.getURI(i))
                                .append(" *||||* ");
                    }
                    d(sb.toString());
                }
            }
            if (localName.equals("book")) {
                book = new Book();
                String id = attributes.getValue("id");
                if (id != null && id.length() > 0) {
                    book.setId(Integer.parseInt(id));
                }
            }
            builder.setLength(0);    //将字符长度设置为0 以便重新开始读取元素内的字符节点
            currentElement = localName;
        }

        /**
         * 内容回到，节点内容，节点外内容［包括：节点外换行符、节点开始的空白符、不属于节点的内容,注释除外］都会出发回调；
         * ch[]是复用缓冲区。
         *
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (LOG) {
                d("ch[]=" + ch.length + ":" + start + "-" + length + "->" + new String(ch, start, length));
            }
            if (currentElement != null) {//过滤掉不属于节点内的内容，如节点后的换行符，节点开始的空白符等
                builder.append(ch, start, length);    //将读取的字符数组追加到builder中
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (LOG) {
                d(String.format("endElement uri=%s, localName=%s, qName=%s", uri, localName, qName));
            }
            if (localName.equals("id")) {
                book.setId(Integer.parseInt(builder.toString()));
            } else if (localName.equals("name")) {
                book.setName(builder.toString());
            } else if (localName.equals("price")) {
                book.setPrice(Float.parseFloat(builder.toString()));
            } else if (localName.equals("book")) {
                books.add(book);
            }
            builder.setLength(0);
        }

        /**
         * 开始前缀 URI 名称空间范围映射。
         * 此事件的信息对于常规的命名空间处理并非必需：
         * 当 http://xml.org/sax/features/namespaces 功能为 true（默认）时，
         * SAX XML 读取器将自动替换元素和属性名称的前缀。
         * 参数意义如下：
         * prefix ：前缀
         * uri ：命名空间
         */
        @Override
        public void startPrefixMapping(String prefix, String uri)
                throws SAXException {
            if (LOG) {
                d("startPrefixMapping >>> start prefix_mapping : xmlns:" + prefix + " = "
                        + "\"" + uri + "\"");
            }

        }

        /*
     * 结束前缀 URI 范围的映射。
     */
        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            if (LOG) {
                d("endPrefixMapping>>> end prefix_mapping : " + prefix);
            }
        }

//        /*
//         * 接收元素内容中可忽略的空白的通知。
//         * 参数意义如下：
//         *     ch : 来自 XML 文档的字符
//         *     start : 数组中的开始位置
//         *     length : 从数组中读取的字符的个数
//         */
//        @Override
//        public void ignorableWhitespace(char[] ch, int begin, int length)
//                throws SAXException {
//            if (LOG) {
//                d("ignorableWhitespace>>> ch " + ch.length + ",  begin=" + begin + " length=" + length);
//            }
//        }
//
//        /*
//         * 接收处理指令的通知。
//         * 参数意义如下：
//         *     target : 处理指令目标
//         *     data : 处理指令数据，如果未提供，则为 null。
//         */
//        @Override
//        public void processingInstruction(String target, String data)
//                throws SAXException {
//
//            if (LOG) {
//                d("processingInstruction>>>  " + target + "\",data = \"" + data + "\")");
//            }
//        }
//
//        /*
//         * 接收用来查找 SAX 文档事件起源的对象。
//         * 参数意义如下：
//         *     locator : 可以返回任何 SAX 文档事件位置的对象
//         */
//        @Override
//        public void setDocumentLocator(Locator locator) {
//
//            if (LOG) {
//                d("setDocumentLocator>>>  " +
//                        ">>> set document_locator : (lineNumber = " + locator.getLineNumber()
//                        + ",columnNumber = " + locator.getColumnNumber()
//                        + ",systemId = " + locator.getSystemId()
//                        + ",publicId = " + locator.getPublicId() + ")");
//            }
//
//        }
//
//        /*
//         * 接收跳过的实体的通知。
//         * 参数意义如下：
//         *     name : 所跳过的实体的名称。如果它是参数实体，则名称将以 '%' 开头，
//         *            如果它是外部 DTD 子集，则将是字符串 "[dtd]"
//         */
//        @Override
//        public void skippedEntity(String name) throws SAXException {
//            if (LOG) {
//                d(">>> skipped_entity : " + name);
//            }
//        }
    }
}

