package wu.a.lib.file;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 *  文件夹遍历
 * @author once
 *
 */
public class DirTraversal {
     
    /**
     * <pre>
     * 遍历文件夹，不是递归实现no recursion
     * @param strPath
     * @return
     * </pre>
     */
    public static LinkedList<File> listLinkedFiles(String strPath) {
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(strPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else
                System.out.println(file[i].getAbsolutePath());
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);
                    else
                        System.out.println(file[i].getAbsolutePath());
                }
            } else {
                System.out.println(tmp.getAbsolutePath());
            }
        }
        return list;
    }
 
     
    /**
     * <pre>
     * 递归文件夹recursion
     * @param strPath
     * @return
     * </pre>
     */
    public static ArrayList<File> listFiles(String strPath) {
        return refreshFileList(strPath);
    }
 
    /**
     * <pre>
     * 递归文件夹recursion
     * @param strPath
     * @return
     * </pre>
     */
    public static ArrayList<File> refreshFileList(String strPath) {
        ArrayList<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();
 
        if (files == null)
            return null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFileList(files[i].getAbsolutePath());
            } else {
                if(files[i].getName().toLowerCase().endsWith("zip"))
                    filelist.add(files[i]);
            }
        }
        return filelist;
    }
}