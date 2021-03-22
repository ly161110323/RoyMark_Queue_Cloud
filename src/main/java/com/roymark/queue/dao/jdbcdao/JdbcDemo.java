package com.roymark.queue.dao.jdbcdao;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcDemo {

    public static void main(String[] args)
    {
        //生成服务之星太复杂只能考虑用Jdbc直连操作。
        Connection conn = null;

        try {
            ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
            DataSource jdbcDataSource = (DataSource) ac.getBean("jdbcDataSource");
            String sql = "select * from queue_area";
            conn=jdbcDataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
              System.out.println("areaName:"+rs.getString("Area_Name"));

            }
            rs.close();
            ps.close();

        }
        catch(Exception ex)
        {
            System.out.println("ex:"+ex.toString());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception  e)
                {
                    System.out.println("e:"+e.toString());
                }
            }
        }
    }
}
