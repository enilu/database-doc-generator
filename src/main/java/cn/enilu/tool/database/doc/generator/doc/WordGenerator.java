package cn.enilu.tool.database.doc.generator.doc;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WordGenerator
 *
 * @author zt
 * @version 2019/1/12 0012
 */
public class WordGenerator {
    private static Configuration configuration = null;

    static {

        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        try {
            configuration.setDirectoryForTemplateLoading(new File("./"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WordGenerator() {
        throw new AssertionError();
    }

    public static void createDoc(String dbName, List<TableVo> list) {
        Map map = new HashMap();
        map.put("dbName", dbName);
        map.put("tables", list);
        try {
            Template template = configuration.getTemplate("database.html");
            String name = dbName + "-doc" + File.separator + dbName + ".html";
            File f = new File(name);
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            template.process(map, w);
            w.close();
            new Html2DocConverter(dbName + "-doc" + File.separator + dbName + ".html", dbName + "-doc" + File
                    .separator + dbName + ".doc")
                    .writeWordFile();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }


    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TableVo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TableVo tableVo = new TableVo();
            tableVo.setTable("表" + i);
            tableVo.setComment("注释" + i);
            List<ColumnVo> columns = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ColumnVo columnVo = new ColumnVo();
                columnVo.setName("name" + j);
                columnVo.setComment("注释" + j);
                columnVo.setKey("PRI");
                columnVo.setIsNullable("是");
                columnVo.setType("varchar(2");
                columns.add(columnVo);

            }
            tableVo.setColumns(columns);
            list.add(tableVo);
        }

        createDoc("test", list);

    }

}
