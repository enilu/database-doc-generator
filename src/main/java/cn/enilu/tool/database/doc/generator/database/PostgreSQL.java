package cn.enilu.tool.database.doc.generator.database;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgreSQL
 *
 * @author zt
 * @version 2019/1/8 0008
 */
public class PostgreSQL extends Generator {
    private String sqlTables = "SELECT A .oid, A .relname AS NAME, b.description AS COMMENT " +
            "FROM pg_class A LEFT OUTER JOIN pg_description b ON b.objsubid = 0 AND A .oid = b.objoid " +
            "WHERE A .relnamespace = ( SELECT oid FROM pg_namespace WHERE nspname = 'public' ) AND A .relkind = 'r' ORDER BY A .relname";
    private String sqlColumns = "SELECT a.attname AS field,t.typname AS type,a.attlen AS length,a.atttypmod AS lengthvar,a.attnotnull AS notnull,b.description AS comment" +
            " FROM pg_class c,pg_attribute a LEFT OUTER JOIN pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid,pg_type t" +
            " WHERE c.relname = '@tablename' and a.attnum > 0 and a.attrelid = c.oid and a.atttypid = t.oid" +
            " ORDER BY a.attnum";

    public PostgreSQL(String dbName, DdgDataSource dataSource) {
        super(dbName, dataSource);
    }

    @Override
    public List<TableVo> getTableData() {
        List<Record> list = getList(sqlTables);
        List<TableVo> tables = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Record record = list.get(i);
            String table = record.getString("name");
            String comment =record.getString("comment");
            TableVo tableVo = getTableInfo(table,comment);
            tables.add(tableVo);
        }
        return tables;
    }
    public TableVo getTableInfo(String table,String tableComment){
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(tableComment);
        String sql = sqlColumns.replace("@tablename",table);

        List<Record> columns = getList(sql);

        List<ColumnVo> columnVoList =  new ArrayList<>();
        for(int i=0;i<columns.size();i++){
            Record record = columns.get(i);
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("field"));
            column.setType(record.getString("type"));
            column.setIsNullable(record.getString("notnull").equals("true")?"否":"是");
            column.setComment(record.getString("comment"));
            columnVoList.add(column);
        }
        tableVo.setColumns(columnVoList);
        return tableVo;
    }
}
