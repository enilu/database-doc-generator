package cn.enilu.tool.database.doc.generator.database;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 达梦数据库 DM
 * 达梦兼容 Oracle 语法，使用 USER_TAB_COMMENTS、USER_TAB_COLUMNS、USER_COL_COMMENTS 等系统视图
 *
 * @author db2doc
 */
public class Dameng extends Generator {

    /** 当前用户的表及注释 */
    private String sqlTables = "SELECT TABLE_NAME, COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_TYPE = 'TABLE' ORDER BY TABLE_NAME";
    /** 表字段信息 */
    private String sqlColumns = "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, NULLABLE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '@tablename' ORDER BY COLUMN_ID";
    /** 表字段注释 */
    private String sqlColumnComments = "SELECT COLUMN_NAME, COMMENTS FROM USER_COL_COMMENTS WHERE TABLE_NAME = '@tablename'";

    public Dameng(String dbName, DdgDataSource dataSource) {
        super(dbName, dataSource);
    }

    @Override
    public List<TableVo> getTableData() {
        List<Record> list = getList(sqlTables);
        List<TableVo> tables = new ArrayList<>();
        for (Record record : list) {
            String table = record.getString("TABLE_NAME");
            String comment = record.getString("COMMENTS");
            TableVo tableVo = getTableInfo(table, comment);
            tables.add(tableVo);
        }
        return tables;
    }

    public TableVo getTableInfo(String table, String tableComment) {
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(tableComment);

        String sql = sqlColumns.replace("@tablename", table.toUpperCase());
        String sql2 = sqlColumnComments.replace("@tablename", table.toUpperCase());
        List<Record> columns = getList(sql);
        List<Record> columnComments = getList(sql2);
        List<ColumnVo> columnVoList = new ArrayList<>();

        for (Record record : columns) {
            ColumnVo column = new ColumnVo();
            column.setName(record.getString("COLUMN_NAME"));
            column.setType(record.getString("DATA_TYPE"));
            column.setIsNullable("Y".equalsIgnoreCase(record.getString("NULLABLE")) ? "是" : "否");
            for (Record comment : columnComments) {
                if (comment.getString("COLUMN_NAME").equals(column.getName())) {
                    column.setComment(comment.getString("COMMENTS"));
                    break;
                }
            }
            columnVoList.add(column);
        }
        tableVo.setColumns(columnVoList);
        return tableVo;
    }
}
