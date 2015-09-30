package wu.a.lib.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.annotation.SuppressLint;

/**
 * Java utils 实现的Zip工具
 *
 * @author once
 */
public class ZipUtils {
	private static final String _8859_1 = "8859_1";
	private static final String GB2312 = "GB2312";
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @throws IOException
	 *             当压缩过程出错时抛出
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile)
			throws IOException {
		zipFiles(resFileList, zipFile, null);
	}

	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @param comment
	 *            压缩文件的注释
	 * @throws IOException
	 *             当压缩过程出错时抛出
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile,
			String comment) throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		if (comment != null) {
			zipout.setComment(comment);
		}
		zipout.close();
	}

	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @param comment
	 *            压缩文件的注释
	 * @throws IOException
	 *             当压缩过程出错时抛出
	 */
	public static void zipFiles(File[] resFileList, File zipFile, String comment)
			throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		if (comment != null) {
			zipout.setComment(comment);
		}
		zipout.close();
	}

	/**
	 * 批量压缩文件（夹）
	 *
	 * @param resFileList
	 *            要压缩的文件（夹）列表
	 * @param zipFile
	 *            生成的压缩文件
	 * @param comment
	 *            压缩文件的注释
	 * @throws IOException
	 *             当压缩过程出错时抛出
	 */
	public static void zipFiles(File resFile, File zipFile, String comment)
			throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		// zipFile(resFile, zipout, null);
		zipFile(resFile, zipout);
		if (comment != null) {
			zipout.setComment(comment);
		}
		zipout.close();
	}

	/**
	 * <pre>
	 * 压缩文件-递归法压缩文件（加）
	 * @param resFile 需要压缩的文件（夹）
	 * @param zipout 压缩的目的文件流
	 * @param rootpath 压缩文件内的目的路径--压缩文件内部的文件路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 * </pre>
	 */
	private static void zipFile(File resFile, ZipOutputStream zipout,
			String rootpath) throws FileNotFoundException, IOException {
		rootpath = (rootpath == null || rootpath.trim().length() == 0 ? resFile
				.getName() : rootpath + File.separator + resFile.getName());
		rootpath = new String(rootpath.getBytes(_8859_1), GB2312);
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			for (File file : fileList) {
				zipFile(file, zipout, rootpath);
			}
		} else {
			byte buffer[] = new byte[BUFF_SIZE];
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(resFile), BUFF_SIZE);
			zipout.putNextEntry(new ZipEntry(rootpath));
			int realLength;
			while ((realLength = in.read(buffer)) != -1) {
				zipout.write(buffer, 0, realLength);
			}
			in.close();
			zipout.flush();
			zipout.closeEntry();
		}
	}

	/**
	 * <pre>
	 * 多文件压缩--非递归法
	 * @param zipFile 压缩目的文件
	 * @param comment 注释
	 * @param resFiles 压缩文件源，多个
	 * @throws FileNotFoundException
	 * @throws IOException
	 * </pre>
	 */
	public static void zipFile(File zipFile, String comment, File... resFiles)
			throws FileNotFoundException, IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		zipFile(zipout, resFiles);
		if (comment != null) {
			zipout.setComment(comment);
		}
		zipout.close();
	}

	/**
	 * <pre>
	 * 压缩文件-非递归（循环法）压缩文件（夹）
	 * @param resFile 需要压缩的文件（夹）
	 * @param zipout 压缩的目的文件流
	 * @throws FileNotFoundException
	 * @throws IOException
	 * </pre>
	 */
	@SuppressLint("NewApi")
	private static void zipFile(ZipOutputStream zipout, File... resFiles)
			throws FileNotFoundException, IOException {
		LinkedList<File> fileTask = new LinkedList<File>();
		for (File resFile : resFiles) {
			String parentPath = resFile.getParent();
			int currentPathIndex = parentPath == null ? 0 : parentPath.length();
			fileTask.add(resFile);
			File file = null;
			do {
				file = fileTask.pop();
				if (file.isDirectory()) {
					File[] childFils = file.listFiles();
					for (File cf : childFils) {
						if (cf.isDirectory()) {
							fileTask.add(cf);
						} else {
							String zipRootPath = cf.getAbsolutePath()
									.substring(currentPathIndex);
							write(cf, zipout, zipRootPath);
						}
					}

				} else {
					String zipRootPath = file.getAbsolutePath().substring(
							currentPathIndex);
					write(file, zipout, zipRootPath);
				}
			} while (fileTask.size() > 0);
		}
	}

	/**
	 * <pre>
	 * 压缩文件-非递归（循环法）压缩文件（夹）
	 * @param resFile 需要压缩的文件（夹）
	 * @param zipout 压缩的目的文件流
	 * @throws FileNotFoundException
	 * @throws IOException
	 * </pre>
	 */
	@SuppressLint("NewApi")
	private static void zipFile(File resFile, ZipOutputStream zipout)
			throws FileNotFoundException, IOException {
		String parentPath = resFile.getParent();
		int currentPathIndex = parentPath == null ? 0 : parentPath.length();
		LinkedList<File> fileTask = new LinkedList<File>();
		fileTask.add(resFile);
		File file = null;
		do {
			file = fileTask.pop();
			if (file.isDirectory()) {
				File[] childFils = file.listFiles();
				for (File cf : childFils) {
					if (cf.isDirectory()) {
						fileTask.add(cf);
					} else {
						String zipRootPath = cf.getAbsolutePath().substring(
								currentPathIndex);
						write(cf, zipout, zipRootPath);
					}
				}

			} else {
				String zipRootPath = file.getAbsolutePath().substring(
						currentPathIndex);
				write(file, zipout, zipRootPath);
			}
		} while (fileTask.size() > 0);
	}

	/**
	 * <pre>
	 * 压缩写文件
	 * @param resFile 源文件
	 * @param zipout 压缩文件的输出流
	 * @param pathToZip 压缩文件路径
	 * @throws IOException
	 * </pre>
	 */
	private static void write(File resFile, ZipOutputStream zipout,
			String pathToZip) throws FileNotFoundException, IOException {
		byte buffer[] = new byte[BUFF_SIZE];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				resFile), BUFF_SIZE);
		zipout.putNextEntry(new ZipEntry(pathToZip));
		int realLength;
		while ((realLength = in.read(buffer)) != -1) {
			zipout.write(buffer, 0, realLength);
		}
		in.close();
		zipout.flush();
		zipout.closeEntry();
	}

	/**
	 * 解压缩一个文件
	 *
	 * @param zipFile
	 *            压缩文件
	 * @param folderPath
	 *            解压缩的目标目录
	 * @throws IOException
	 *             当解压缩过程出错时抛出
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			String str = folderPath + File.separator + entry.getName();
			str = new String(str.getBytes(_8859_1), GB2312);
			File desFile = new File(str);
			if (!desFile.exists()) {
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				desFile.createNewFile();
			}
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
		zf.close();
	}

	/**
	 * 解压文件名包含传入文字的文件
	 *
	 * @param zipFile
	 *            压缩文件
	 * @param folderPath
	 *            目标文件夹
	 * @param nameContains
	 *            传入的文件匹配名
	 * @throws ZipException
	 *             压缩格式有误时抛出
	 * @throws IOException
	 *             IO错误时抛出
	 */
	public static ArrayList<File> upZipSelectedFile(File zipFile,
			String folderPath, String nameContains) throws ZipException,
			IOException {
		ArrayList<File> fileList = new ArrayList<File>();

		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdir();
		}

		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			if (entry.getName().contains(nameContains)) {
				InputStream in = zf.getInputStream(entry);
				String str = folderPath + File.separator + entry.getName();
				str = new String(str.getBytes(_8859_1), GB2312);
				// str.getBytes("GB2312"),"8859_1" 输出
				// str.getBytes("8859_1"),"GB2312" 输入
				File desFile = new File(str);
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
					desFile.createNewFile();
				}
				OutputStream out = new FileOutputStream(desFile);
				byte buffer[] = new byte[BUFF_SIZE];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}
				in.close();
				out.close();
				fileList.add(desFile);
			}
		}
		zf.close();
		return fileList;
	}

	/**
	 * 获得压缩文件内文件列表
	 *
	 * @param zipFile
	 *            压缩文件
	 * @return 压缩文件内文件名称
	 * @throws ZipException
	 *             压缩文件格式有误时抛出
	 * @throws IOException
	 *             当解压缩过程出错时抛出
	 */
	public static ArrayList<String> getEntriesNames(File zipFile)
			throws ZipException, IOException {
		ArrayList<String> entryNames = new ArrayList<String>();
		Enumeration<?> entries = getEntriesEnumeration(zipFile);
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			entryNames.add(new String(getEntryName(entry).getBytes(GB2312),
					_8859_1));
		}
		return entryNames;
	}

	/**
	 * 获得压缩文件内压缩文件对象以取得其属性
	 *
	 * @param zipFile
	 *            压缩文件
	 * @return 返回一个压缩文件列表
	 * @throws ZipException
	 *             压缩文件格式有误时抛出
	 * @throws IOException
	 *             IO操作有误时抛出
	 */
	public static Enumeration<?> getEntriesEnumeration(File zipFile)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(zipFile);
		return zf.entries();

	}

	/**
	 * 取得压缩文件对象的注释
	 *
	 * @param entry
	 *            压缩文件对象
	 * @return 压缩文件对象的注释
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryComment(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getComment().getBytes(GB2312), _8859_1);
	}

	/**
	 * 取得压缩文件对象的名称
	 *
	 * @param entry
	 *            压缩文件对象
	 * @return 压缩文件对象的名称
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryName(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getName().getBytes(GB2312), _8859_1);
	}
}