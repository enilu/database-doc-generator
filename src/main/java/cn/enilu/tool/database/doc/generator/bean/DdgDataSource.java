package cn.enilu.tool.database.doc.generator.bean;

import org.nutz.dao.impl.SimpleDataSource;

/**
 * 自定义数据源
 *
 * @Author enilu
 * @Date 2021/4/28 10:14
 * @Version 1.0
 */

public class DdgDataSource {
    private int dbType;
    private String ip;
    private String port;
    private String user;
    private String pass;
    private String dbName;

    public SimpleDataSource getDs(){
        SimpleDataSource dataSource = new SimpleDataSource();

        if (Constants.DB_MYSQL == dbType) {
            dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + dbName);
        } else if (Constants.DB_ORACLE == dbType) {
            dataSource.setJdbcUrl("jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName);
        } else if (Constants.DB_POSTGRESQL == dbType) {
            dataSource.setJdbcUrl("jdbc:postgresql://" + ip + ":" + port + "/" + dbName);
        } else if (Constants.DB_SQLSERVER == dbType) {
            dataSource.setJdbcUrl("jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName);
        }
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        return dataSource;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
