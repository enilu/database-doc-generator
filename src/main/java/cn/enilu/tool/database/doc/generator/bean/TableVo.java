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
    /**
     * 表的记录数
     */
    private Long rows;
    /**
     * 表数据占用空间大小，单位（字节数)
     */
    private Long dataLength=0L;
    /**
     *  表数据占用空间大小，易于阅读的格式XX GB XX Mb XX KB
     */
    public String dataLengthHuman="0";
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

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public String getDataLengthHuman() {
        long temp = 0;
        String humanLength = "0KB";
        if(dataLength>1024){
            temp = dataLength/1024;
            humanLength = temp+"KB";
        }
        if(temp>1024){
            temp = temp/1024;
            humanLength = temp+"MB";
        }
        if(temp>1024){
            temp = temp/1024;
            humanLength = temp+"GB";
        }
        setDataLengthHuman(humanLength);
        return dataLengthHuman;
    }

    public void setDataLengthHuman(String dataLengthHuman) {
        this.dataLengthHuman = dataLengthHuman;
    }
}
