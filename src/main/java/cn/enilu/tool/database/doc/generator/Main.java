package cn.enilu.tool.database.doc.generator;

import cn.enilu.tool.database.doc.generator.bean.Constants;
import cn.enilu.tool.database.doc.generator.bean.DdgDataSource;
import cn.enilu.tool.database.doc.generator.database.*;
import org.nutz.dao.impl.SimpleDataSource;

import java.util.Scanner;

/**
 * Main
 *
 * @author zt
 * @version 2018/10/6 0006
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("choose database:\n1:MySQL\n2:Oracle\n3:PostgreSQL\n4:SQLServer\n5:MongoDB\n" +
                "Select the appropriate numbers choose database type\n" +
                "(Enter 'c' to cancel):\n ");
        int dbType = Integer.valueOf(sc.nextLine());
        if ("c".equals(dbType)) {
            System.exit(-1);
        }
        if (Integer.valueOf(dbType) < 1 || Integer.valueOf(dbType) > 5) {
            System.out.println("wrong number,will exit");
            System.exit(-1);
        }
        String serviceName = null;
        String dbName = null;
        if (Constants.DB_ORACLE == dbType) {
            System.out.println("input service name:");
            serviceName = sc.nextLine();
        } else {
            System.out.println("input database name:");
            dbName = sc.nextLine();
        }
        System.out.println("input host (default 127.0.0.1) :");
        String ip = sc.nextLine();
        if ("".equals(ip)) {
            ip = "127.0.0.1";
        }

        System.out.println("input port (default " + getDefaultPort(dbType) + ") :");
        String port = sc.nextLine();
        if ("".equals(port)) {
            port = getDefaultPort(dbType);
        }

        System.out.println("input username (default " + getDefaultUser(dbType) + ") :");
        String username = sc.nextLine();
        if ("".equals(username)) {
            username = getDefaultUser(dbType);
        }

        System.out.println("input password (default 123456) :");
        String passowrd = sc.nextLine();
        if ("".equals(passowrd)) {
            passowrd = "123456";
        }

//        SimpleDataSource dataSource = new SimpleDataSource();
//        if ("1".equals(dbType)) {
//            dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + dbName);
//        } else if ("2".equals(dbType)) {
//            dataSource.setJdbcUrl("jdbc:oracle:thin:@" + ip + ":" + port + ":" + serviceName);
//        } else if ("3".equals(dbType)) {
//            dataSource.setJdbcUrl("jdbc:postgresql://" + ip + ":" + port + "/" + dbName);
//        } else if ("4".equals(dbType)) {
//            dataSource.setJdbcUrl("jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName);
//        }else if("5".equalsIgnoreCase(dbType)){
//
//        }
//        dataSource.setUsername(username);
//        dataSource.setPassword(passowrd);
        DdgDataSource dataSource = new DdgDataSource();
        dataSource.setDbType(dbType);
        dataSource.setIp(ip);
        dataSource.setPort(port);
        dataSource.setDbName("2".equals(dbType) ? serviceName : dbName);
        dataSource.setUser(username);
        dataSource.setPass(passowrd);
        Generator generator = null;
        switch (dbType) {
            case Constants.DB_MYSQL:
                generator = new MySQL(dbName, dataSource);
                break;
            case  Constants.DB_ORACLE:
                generator = new Oracle(username, dataSource);
                break;
            case  Constants.DB_POSTGRESQL:
                generator = new PostgreSQL(dbName, dataSource);
                break;
            case Constants.DB_SQLSERVER:
                generator = new SqlServer(dbName, dataSource);
            case Constants.DB_MONGO:
                generator = new Mongo(dbName, dataSource);
            default:
                System.out.println("not support database");
                break;
        }

        generator.generateDoc();
    }

    private static String getDefaultPort(int dbType) {
        String defaultPort = "";

        switch (dbType) {
            case Constants.DB_MYSQL: {
                defaultPort = "3306";
                break;
            }
            case Constants.DB_ORACLE: {
                defaultPort = "1521";
                break;
            }
            case Constants.DB_POSTGRESQL: {
                defaultPort = "5432";
                break;
            }
            case Constants.DB_SQLSERVER: {
                defaultPort = "1433";
                break;
            }
            case Constants.DB_MONGO: {
                defaultPort = "27017";
                break;
            }
            default: {
                defaultPort = "-";
                break;
            }
        }

        return defaultPort;
    }

    private static String getDefaultUser(int dbType) {
        String defaultUser = "";

        switch (dbType) {
            case Constants.DB_MYSQL: {
                defaultUser = "root";
                break;
            }
            case Constants.DB_ORACLE: {
                defaultUser = "sytem";
                break;
            }
            case Constants.DB_POSTGRESQL: {
                defaultUser = "postgres";
                break;
            }
            case Constants.DB_SQLSERVER: {
                defaultUser = "sa";
                break;
            }
            default: {
                defaultUser = "";
                break;
            }
        }

        return defaultUser;
    }
}
