package cn.enilu.tool.database.doc.generator.database;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Oracle
 *
 * @author zt
 * @version 2019/1/6 0006
 */
public class Oracle  extends Generator{
    private String sqlTables = "select * from user_tab_comments";
    private String sqlColumns = "select column_name,data_type,data_length,nullable from user_tab_columns where " +
            "Table_Name='@tablename'";
    private String sqlColumnComments = "select column_name,comments from user_col_comments where TABLE_NAME='@tablename'";
    public Oracle(String dbName, DdgDataSource dataSource) {
        super(dbName, dataSource);
    }

    @Override
    public List<TableVo> getTableData() {
        List<Record> list = getList(sqlTables);
        List<TableVo> tables = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Record record = list.get(i);
            String table = record.getString("table_name");
            String comment =record.getString("comments");
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
        String sql2 = sqlColumnComments.replace("@tablename",table);
        List<Record> columns = getList(sql);
        List<Record> columnComments = getList(sql2);
        List<ColumnVo> columnVoList =  new ArrayList<>();
        for(int i=0;i<columns.size();i++){
            Record record = columns.get(i);
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("column_name"));
            column.setType(record.getString("data_type"));
            column.setIsNullable(record.getString("nullable").equals("Y")?"是":"否");

            for(Record comment:columnComments){
                if(comment.getString("column_name").equals(column.getName())){
                    column.setComment(comment.getString("comments"));
                }
            }
            columnVoList.add(column);
        }
        tableVo.setColumns(columnVoList);
        return tableVo;
    }

}
