package cn.enilu.tool.database.doc.generator.database;
import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseDocService
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class MySQL extends Generator {
    String sqlTables = "select table_name,table_comment,table_rows,data_length from information_schema.tables where table_schema = '@dbname'" +
            " order by table_name asc";
    String sqlColumns = "select column_name,column_type,column_key,is_nullable,column_comment from information_schema" +
            ".columns where table_schema = '@dbname'  and table_name " +
            "='@tablename'";

    public MySQL(String dbName, DdgDataSource dataSource){
        super(dbName,dataSource);
    }
    @Override
    public List<TableVo> getTableData(){
        String sql = sqlTables.replace("@dbname",dbName);
        List<Record> list = getList(sql);
        List<TableVo> tables = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Record record = list.get(i);
            String table = record.getString("table_name");
            String comment =record.getString("table_comment");
            Long rows = record.getLong("table_rows");
            Long dataLength = record.getLong("data_length");
            TableVo tableVo = getTableInfo(table,comment,rows,dataLength);
            tables.add(tableVo);
        }
        return tables;
    }
    public TableVo getTableInfo(String table,String comment){
        return getTableInfo(table, comment,null,null);
    }
    public TableVo getTableInfo(String table,String comment,Long rows,Long dataLength){
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(comment);
        tableVo.setRows(rows);
        tableVo.setDataLength(dataLength);
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
            column.setIsNullable(record.getString("is_nullable").equals("NO")?"否":"是");
            column.setComment(record.getString("column_comment"));
            columns.add(column);
        }
        tableVo.setColumns(columns);
        return tableVo;
    }
}