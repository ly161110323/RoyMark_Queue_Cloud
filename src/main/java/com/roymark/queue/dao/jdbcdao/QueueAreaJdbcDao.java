//jdbcdao demo add by wfz 20200219
package com.roymark.queue.dao.jdbcdao;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@Repository
public class QueueAreaJdbcDao {

    public List<String> getAreaList()
    {
        Connection conn = null;
        List<String> areaNameList=new ArrayList<String>();
        try {
            ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
            DataSource jdbcDataSource = (DataSource) ac.getBean("jdbcDataSource");
            String sql = "select * from queue_area";
            conn=jdbcDataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
//                System.out.println("areaName:"+rs.getString("Area_Name"));
                areaNameList.add(rs.getString("Area_Name"));

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
        return areaNameList;
    }
}
