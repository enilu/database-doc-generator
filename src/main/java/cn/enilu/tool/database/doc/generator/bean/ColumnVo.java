package cn.enilu.tool.database.doc.generator.bean;

/**
 * ColumnVo
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class ColumnVo {
    private String name;
    private String type;
    private String key;
    private String isNullable;
    private String comment;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
