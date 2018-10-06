package cn.enilu.tool.database.doc.generator;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseDocService
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class Generator {
    String sqlTables = "select table_name,table_comment from information_schema.tables where table_schema = '@dbname'" +
            " order by table_name asc";
    String sqlColumns = "select column_name,column_type,column_key,is_nullable,column_comment from information_schema" +
            ".columns where table_schema = '@dbname'  and table_name " +
            "='@tablename'";

    private String dbName = null;
    private String docPath = null;
    private Dao dao = null;
    public Generator(Dao dao){
        this.dao = dao;
    }
    public void generateDoc(String dbName,String docPath){
        this.docPath = docPath;
        File docDir = new File(docPath);
        if(docDir.exists()){
            throw  new RuntimeException("该文件夹"+docPath+"已存在");
        }else{
            docDir.mkdirs();
        }
        this.dbName = dbName;
        String sql = sqlTables.replace("@dbname",dbName);
        List<Record> list = getList(sql);
        List<TableVo> tables = new ArrayList<>();
        for(int i=0;i<list.size();i++){
          Record record = list.get(i);
            String table = record.getString("table_name");
            String comment =record.getString("table_comment");
            TableVo tableVo = getTableInfo(table,comment,dbName);
            tables.add(tableVo);
        }
        System.out.println(Json.toJson(tables));
        save2File(tables);
    }
    public TableVo getTableInfo(String table,String comment,String dbName){
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(comment);
        String sql = sqlColumns.replace("@dbname",dbName);
        sql = sql.replace("@tablename",table);
        List<Record> list =getList(sql);
        List<ColumnVo> columns = new ArrayList<>();
        for(int i=0;i<list.size();i++){
           Record record = list.get(i);
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("column_name"));
            column.setType(record.getString("column_type"));
            column.setKey(record.getString("column_key"));
            column.setIsNullable(record.getString("is_nullable"));
            column.setComment(record.getString("column_comment"));
            columns.add(column);
        }
        tableVo.setColumns(columns);
        return tableVo;
    }

    public void save2File(List<TableVo> tables){
        saveSummary(tables);
        saveReadme(tables);
        for(TableVo tableVo:tables){
            saveTableFile(tableVo);
        }

    }
    private void saveSummary(List<TableVo> tables){
        StringBuilder builder = new StringBuilder("# Summary").append("\r\n").append("* [Introduction](README.md)")
                .append("\r\n");
        for(TableVo tableVo:tables){
            String name = Strings.isEmpty(tableVo.getComment())?tableVo.getTable():tableVo.getComment();
            builder.append("* ["+name+"]("+tableVo.getTable()+".md)").append("\r\n");
        }
        try {
            Files.write( new File(docPath+File
                    .separator+"SUMMARY.md"),builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveReadme(List<TableVo> tables){
        StringBuilder builder = new StringBuilder("# "+dbName+"数据库文档").append("\r\n");
        for(TableVo tableVo:tables){
            builder.append("- ["+(Strings.isEmpty(tableVo.getComment())?tableVo.getTable():tableVo.getComment())
                    +"]" +
                    "("+tableVo
                    .getTable()+".md)")
                    .append
                    ("\r\n");
        }
        try {
            Files.write(new File(docPath+File
                    .separator+"README.md"),builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveTableFile(TableVo table){
        StringBuilder builder =new StringBuilder("# "+table.getComment()+"("+table.getTable()+")").append("\r\n");
        builder.append("| 列名   | 类型   | KEY  | 是否为空 | 注释   |").append("\r\n");
        builder.append("| ---- | ---- | ---- | ---- | ---- |").append("\r\n");
        List<ColumnVo> columnVos = table.getColumns();
        for(int i=0;i<columnVos.size();i++){
            ColumnVo column = columnVos.get(i);
            builder.append("|").append(column.getName()).append("|").append(column.getType()).append("|").append
                    (column.getKey()).append("|").append(column.getIsNullable()).append("|").append(column.getComment
                    ()).append("|\r\n");
        }
        try {
            Files.write(new File(docPath+File
                    .separator+table.getTable()+".md"),builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Record> getList(String sqlStr){
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<Record> list = sql.getList(Record.class);
        return list;
    }
}
