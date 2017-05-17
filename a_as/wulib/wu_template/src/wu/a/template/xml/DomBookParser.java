package wu.a.template.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomBookParser implements XmlParser<Book> {

    @Override
    public List<Book> parse(InputStream is) throws Exception {
        List<Book> books = new ArrayList<Book>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    //取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder();    //从factory获取DocumentBuilder实例
        Document doc = builder.parse(is);    //解析输入流 得到Document实例
        Element rootElement = doc.getDocumentElement();
        NodeList items = rootElement.getElementsByTagName("book");
        for (int i = 0; i < items.getLength(); i++) {
            Book book = new Book();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String nodeName = property.getNodeName();
                NamedNodeMap nodeMaps = property.getAttributes();
                if (nodeMaps != null) {
                    for (int ai = nodeMaps.getLength() - 1; ai >= 0; ai--) {
                        Node artt = nodeMaps.item(ai);
                    }
                }
                if (nodeName.equals("id")) {
                    book.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
                } else if (nodeName.equals("name")) {
                    book.setName(property.getFirstChild().getNodeValue());
                } else if (nodeName.equals("price")) {
                    book.setPrice(Float.parseFloat(property.getFirstChild().getNodeValue()));
                }
            }
            books.add(book);
        }
        return books;
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();    //由builder创建新文档


        Element rootElement = doc.createElement("books");

        String url = "http://schemas.android.com/apk/res/android";
        Element elementNs = doc.createElementNS("http://schemas.android.com/apk/res/element", "element");
        rootElement.appendChild(elementNs);

        rootElement.setAttributeNS(url, null, null);
        Attr arrtNs = doc.createAttributeNS(url, "android");
        rootElement.setAttributeNodeNS(arrtNs);

        for (Book book : books) {
            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", book.getId() + "");
            bookElement.setAttributeNS(url, "android:layout_width", "wrap_content");

            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(book.getName());
            bookElement.appendChild(nameElement);

            Element priceElement = doc.createElement("price");
            priceElement.setTextContent(book.getPrice() + "");
            bookElement.appendChild(priceElement);

            rootElement.appendChild(bookElement);
        }

        doc.appendChild(rootElement);

        TransformerFactory transFactory = TransformerFactory.newInstance();//取得TransformerFactory实例
        Transformer transformer = transFactory.newTransformer();    //从transFactory获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");    // 是否忽略XML声明

        Source source = new DOMSource(doc);    //表明文档来源是doc
        //result表明目标结果为writer
        transformer.transform(source, result);    //开始转换
    }

}

