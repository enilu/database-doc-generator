package cn.enilu.tool.database.doc.generator.bean;

import java.util.List;

/**
 * TableVo
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class TableVo {
    private String table;
    private String comment;
    private List<ColumnVo> columns;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnVo> columns) {
        this.columns = columns;
    }
}
