package cn.enilu.tool.database.doc.generator.doc;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;

/**
 * Html2DocConverter
 *
 * @author zt
 * @version 2019/1/12 0012
 */
public class Html2DocConverter {

    private String inputPath;    // 输入文件路径，以.html结尾
    private String outputPath;    // 输出文件路径，以.doc结尾

    public Html2DocConverter(String inputPath, String outputPath) {
        super();
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    /**
     * 读取html文件到word
     *
     * @param filepath
     *            html文件的路径
     * @return
     * @throws Exception
     */
    public boolean writeWordFile() throws Exception {

        InputStream is = null;
        FileOutputStream fos = null;

        // 1 找不到源文件, 则返回false
        File inputFile = new File(this.inputPath);
        if (!inputFile.exists()) {
            return false;
        }

        File outputFile = new File(this.outputPath);
        // 2 如果目标路径不存在 则新建该路径
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        try {

            // 3 将html文件内容写入doc文件
            is = new FileInputStream(inputFile);
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            directory.createDocument(
                    "WordDocument", is);

            fos = new FileOutputStream(this.outputPath);
            poifs.writeFilesystem(fos);

            System.out.println("转换word文件完成!");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }

        return false;
    }
    public static void main(String[] args) throws Exception {

        new Html2DocConverter("G:/123.html" , "G:/temp5.doc").writeWordFile();
    }
}
