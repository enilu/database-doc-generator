package cn.enilu.tool.database.doc.generator;

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
        System.out.print("choose database:\n1:MySQL\n2:Oracle\n3:PostgreSQL\n4:SQLServer\n" +
                "Select the appropriate numbers choose database type\n" +
                "(Enter 'c' to cancel):\n ");
        String dbType = sc.nextLine();
        if ("c".equals(dbType)) {
            System.exit(-1);
        }
        if (Integer.valueOf(dbType) < 1 || Integer.valueOf(dbType) > 4) {
            System.out.println("wrong number,will exit");
            System.exit(-1);
        }
        String serviceName = null;
        if ("2".equals(dbType)) {
            System.out.println("input service name:");
            serviceName = sc.nextLine();
        }
        String dbName = null;
        if ("1".equals(dbType) || "3".equals(dbType) || "4".equals(dbType)) {
            System.out.println("input database name:");
            dbName = sc.nextLine();
        }
        System.out.println("input host (default 127.0.0.1) :");
        String ip = sc.nextLine();
        if("".equals(ip))
        {
            ip = "127.0.0.1";
        }

        System.out.println("input port (default " + getDefaultPort(dbType) + ") :");
        String port = sc.nextLine();
        if("".equals(port))
        {
            port = getDefaultPort(dbType);
        }

        System.out.println("input username (default " + getDefaultUser(dbType) + ") :");
        String username = sc.nextLine();
        if("".equals(username))
        {
            username = getDefaultUser(dbType);
        }

        System.out.println("input password (default 123456) :");
        String passowrd = sc.nextLine();
        if("".equals(passowrd))
        {
            passowrd = "123456";
        }

        SimpleDataSource dataSource = new SimpleDataSource();
        if ("1".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + dbName);
        } else if ("2".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:oracle:thin:@" + ip + ":" + port + ":" + serviceName);
        } else if ("3".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:postgresql://" + ip + ":" + port + "/" + dbName);
        } else if ("4".equals(dbType)) {
            dataSource.setJdbcUrl("jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName);
        }
        dataSource.setUsername(username);
        dataSource.setPassword(passowrd);
        Generator generator = null;
        switch (dbType) {
            case "1":
                generator = new MySQL(dbName, dataSource);
                break;
            case "2":
                generator = new Oracle(username, dataSource);
                break;
            case "3":
                generator = new PostgreSQL(dbName, dataSource);
                break;
            case "4":
                generator = new SqlServer(dbName, dataSource);
            default:
                System.out.println("not support database");
                break;
        }

        generator.generateDoc();
    }

    private static String getDefaultPort(String dbType) {
        String defaultPort = "";

        switch (dbType) {
            case "1": {
                defaultPort = "3306";
                break;
            }
            case "2": {
                defaultPort = "1521";
                break;
            }
            case "3": {
                defaultPort = "5432";
                break;
            }
            case "4": {
                defaultPort = "1433";
                break;
            }
            default: {
                defaultPort = "-";
                break;
            }
        }

        return defaultPort;
    }

    private static String getDefaultUser(String dbType) {
        String defaultUser = "";

        switch (dbType) {
            case "1": {
                defaultUser = "root";
                break;
            }
            case "2": {
                defaultUser = "not support";
                break;
            }
            case "3": {
                defaultUser = "postgres";
                break;
            }
            case "4": {
                defaultUser = "sa";
                break;
            }
            default: {
                defaultUser = "-";
                break;
            }
        }

        return defaultUser;
    }
}
