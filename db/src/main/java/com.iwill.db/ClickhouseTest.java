package com.iwill.db;

import ru.yandex.clickhouse.ClickHouseDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ClickhouseTest {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:clickhouse://localhost:8123/test";
        Properties properties = new Properties();
        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, properties);
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from tbl_clickstream");
        while (rs.next()){
            String customerId = rs.getString("customer_id");
            System.out.println(customerId);
        }
        int result =stmt.executeUpdate("insert into tbl_clickstream(customer_id ,click_event_type,country_code,source_id) values('12','type','CH','123')");
        System.out.println(result);
    }
}
