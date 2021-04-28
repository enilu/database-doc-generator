package cn.enilu.tool.database.doc.generator.database;

import cn.enilu.tool.database.doc.generator.bean.ColumnVo;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.bean.TableVo;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mongo.ZMoCo;
import org.nutz.mongo.ZMoDB;
import org.nutz.mongo.ZMoDoc;
import org.nutz.mongo.ZMongo;

import java.util.*;

/**
 * Mongo
 *
 * @Author enilu
 * @Date 2021/4/28 10:07
 * @Version 1.0
 */
public class Mongo extends Generator {
    private ZMongo zMongo;
    private ZMoDB zMoDB;

    public Mongo(String dbName, DdgDataSource dataSource) {
        super(dbName, dataSource);
        if (Strings.isNotBlank(dataSource.getUser()) && Strings.isNotBlank(dataSource.getPass())) {
            zMongo = ZMongo.me(dataSource.getUser(), dataSource.getPass(),
                    dataSource.getIp(), Integer.valueOf(dataSource.getPort()));
        } else {
            zMongo = ZMongo.me(dataSource.getIp(), Integer.valueOf(dataSource.getPort()));
        }
        zMoDB = zMongo.db(dataSource.getDbName());
    }

    @Override
    public List<TableVo> getTableData() {
        List<TableVo> tables = new ArrayList<>();
        Set<String> collectionNames = zMoDB.cNames();
        Iterator<String> iter = collectionNames.iterator();
        while (iter.hasNext()) {
            String collectionname = iter.next();
            ZMoCo zMoCo = zMoDB.c(collectionname);
            ZMoDoc doc = zMoCo.findOne();
            TableVo tableVo = getTableInfo(doc, collectionname);
            if (tableVo != null) {
                tables.add(tableVo);
            }
        }


        return tables;
    }

    private TableVo getTableInfo(ZMoDoc doc, String tableName) {
        TableVo tableVo = new TableVo();
        tableVo.setTable(tableName);
        if (doc == null) {
            return null;
        }
        Set<String> keys = doc.keySet();
        Iterator<String> iter = keys.iterator();
        List<ColumnVo> columns = new ArrayList<>();
        while (iter.hasNext()) {
            ColumnVo column = new ColumnVo();
            String key = iter.next();
            column.setName(key);
            Object object = doc.get(key);
            getColumnInfo(null, key, object, columns);

        }
        tableVo.setColumns(columns);
        return tableVo;
    }

    private void getColumnInfo(String parent, String key, Object object, List<ColumnVo> columns) {
        if (object == null) {
            return;
        }
        if("__v".equalsIgnoreCase(key) || "_class".equalsIgnoreCase(key)){
            return ;
        }

        String currentKey =   Strings.isNotBlank(parent) ? (parent + "." + key) : key;

        ColumnVo column = new ColumnVo();
        column.setName(currentKey);
        columns.add(column);
        if (object instanceof String) {

            column.setType("string");
        } else if (object instanceof Integer) {

            column.setType("integer");
        } else if (object instanceof BasicDBList) {
            column.setType("list");
            currentKey = key+"[]";
            column.setName(currentKey);
            BasicDBList list = (BasicDBList) object;
            if (list.size() > 0) {

                Object object2 = list.get(0);
                getColumnInfo(currentKey, currentKey, object2, columns);
            } else {
                columns.remove(column);
            }

        } else if (object instanceof BasicDBObject) {
            column.setType("object");
            if(parent!=null&&parent.contains("[]")){
                columns.remove(column);
            }

            BasicDBObject dbObject = (BasicDBObject) object;
            Iterator<String> iter = dbObject.keySet().iterator();
            while (iter.hasNext()) {
                String key2 = iter.next();
                if(parent!=null&&parent.contains("[]")){
                    getColumnInfo(parent, key2, dbObject.get(key2), columns);
                }else {
                    getColumnInfo(currentKey, key2, dbObject.get(key2), columns);
                }
            }
        } else if (object instanceof Boolean) {
            column.setType("boolean");
        } else if (object instanceof Double) {
            column.setType("double");
        } else if (object instanceof ObjectId) {
            columns.remove(column);
        } else if (object instanceof Date) {
            column.setType("date");
        } else if (object instanceof Long) {
            column.setType("long");
        } else {
            column.setType(object.getClass().getName());
        }


    }
}
