package com.iwill.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraTest {

    public static void main(String[] args) {
        String querySql = "select * from tbl_emp";

        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("test");
        ResultSet rs = session.execute(querySql);

        System.out.println(rs);
        for (Row row : rs.all()) {
            Integer id = row.getInt("id");
            String name = row.getString("name");
            System.out.println("id : " + id + ",name : " + name);
        }

    }
}
