package cn.enilu.tool.database.doc.generator.database;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Sqlserver
 *
 * @author konglinghai
 * @version 2019/1/11 11:03
 */
public class SqlServer extends Generator {
    private String sqlTables = "SELECT\n" + "  cast(tbs.name AS VARCHAR(255))AS NAME,\n"
            + "  cast(ds.value AS VARCHAR(255)) AS COMMENT\n" + "FROM sys.extended_properties ds\n"
            + "  LEFT JOIN sysobjects tbs ON\n" + "  ds.major_id=tbs.id\n" + "WHERE  ds.minor_id=0";
    private String sqlColumns = "\n" + "SELECT\n" + "    column_name=C.name,\n"
            + "    column_key=ISNULL(IDX.PrimaryKey,N''),\n" + "    column_type=T.name,\n"
            + "    is_nullable=CASE WHEN C.is_nullable=1 THEN N'是'ELSE N'否' END,\n"
            + "    column_comment=ISNULL(cast(PFD.[value] AS VARCHAR(500)),N'')\n" + "FROM sys.columns C\n"
            + "  INNER JOIN sys.objects O ON C.object_id=O.object_id\n"
            + "                              AND O.type='U' AND O.is_ms_shipped=0\n"
            + "  INNER JOIN sys.types T ON C.user_type_id=T.user_type_id\n"
            + "  LEFT JOIN sys.default_constraints D ON C.object_id=D.parent_object_id\n"
            + "                                         AND C.column_id=D.parent_column_id AND C.default_object_id=D.[object_id]\n"
            + "  LEFT JOIN sys.extended_properties PFD ON PFD.class=1\n"
            + "                                           AND C.[object_id]=PFD.major_id AND C.column_id=PFD.minor_id\n"
            + "  -- AND PFD.name='Caption' -- 字段说明对应的描述名称(一个字段可以添加多个不同name的描述)\n"
            + "  LEFT JOIN sys.extended_properties PTB ON PTB.class=1\n"
            + "                                           AND PTB.minor_id=0 AND C.[object_id]=PTB.major_id\n"
            + "  -- AND PFD.name='Caption' -- 表说明对应的描述名称(一个表可以添加多个不同name的描述)\n" + "  LEFT JOIN -- 索引及主键信息\n" + "  (\n"
            + "    SELECT\n" + "      IDXC.[object_id],\n" + "      IDXC.column_id,\n"
            + "        Sort=CASE INDEXKEY_PROPERTY(IDXC.[object_id],IDXC.index_id,IDXC.index_column_id,'IsDescending')\n"
            + "             WHEN 1 THEN 'DESC' WHEN 0 THEN 'ASC' ELSE '' END,\n"
            + "        PrimaryKey=CASE WHEN IDX.is_primary_key=1 THEN N'PRI'ELSE N'' END,\n"
            + "        IndexName=IDX.Name\n" + "    FROM sys.indexes IDX\n"
            + "      INNER JOIN sys.index_columns IDXC ON IDX.[object_id]=IDXC.[object_id]\n"
            + "                                           AND IDX.index_id=IDXC.index_id\n"
            + "      LEFT JOIN sys.key_constraints KC ON IDX.[object_id]=KC.[parent_object_id]\n"
            + "                                          AND IDX.index_id=KC.unique_index_id\n"
            + "      INNER JOIN -- 对于一个列包含多个索引的情况,只显示第1个索引信息\n" + "      (\n"
            + "        SELECT [object_id], Column_id, index_id=MIN(index_id)\n" + "        FROM sys.index_columns\n"
            + "        GROUP BY [object_id], Column_id\n" + "      ) IDXCUQ ON IDXC.[object_id]=IDXCUQ.[object_id]\n"
            + "                  AND IDXC.Column_id=IDXCUQ.Column_id    AND IDXC.index_id=IDXCUQ.index_id\n"
            + "  ) IDX ON C.[object_id]=IDX.[object_id]\n" + "           AND C.column_id=IDX.column_id\n"
            + "WHERE O.name='@tablename' -- 如果只查询指定表,加上此条件\n" + "ORDER BY O.name,C.column_id";

    public SqlServer(String dbName, SimpleDataSource dataSource) {
        super(dbName, dataSource);
    }

    @Override
    public List<TableVo> getTableData() {
        List<Record> list = getList(sqlTables);
        List<TableVo> tables = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            String table = record.getString("name");
            String comment = record.getString("comment");
            TableVo tableVo = getTableInfo(table, comment);
            tables.add(tableVo);
        }
        return tables;
    }

    public TableVo getTableInfo(String table,String tableComment){
        TableVo tableVo = new TableVo();
        tableVo.setTable(table);
        tableVo.setComment(tableComment);
        String sql = sqlColumns.replace("@tablename",table);
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
}