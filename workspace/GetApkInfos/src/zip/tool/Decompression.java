package zip.tool;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * 解压缩指定的文件信息
 * @author Administrator
 *
 */
public class Decompression {

	private String path;
	
	public Decompression(String path){
		this.path = path;
	}
	
	
	public void read(){
		
	}
	
	//获得AndroidManifest.xml文件流
	public InputStream getAndroidManifest(){
		ZipFile zFile;
		try {
			zFile = new ZipFile(this.path);
			ZipEntry entry = zFile.getEntry("AndroidManifest.xml"); //指定解压缩Android的配置文件
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			return zFile.getInputStream(entry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
