package cn.enilu.tool.database.doc.generator;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
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
        Scanner sc=new Scanner(System.in);
        System.out.println("input mysql host:");
        String ip = sc.nextLine();
        System.out.println("input mysql port:");
        String port = sc.nextLine();
        System.out.println("input database name:");
        String dbName = sc.nextLine();

        System.out.println("input mysql username:");
        String username = sc.nextLine();

        System.out.println("input mysql password:");
        String passowrd = sc.nextLine();

        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://"+ip+":"+port+"/"+dbName);
        dataSource.setUsername(username);
        dataSource.setPassword(passowrd);
        Dao dao = new NutDao(dataSource);
        Generator generator = new Generator(dao);
        generator.generateDoc(dbName,dbName+"-doc");
    }
}
