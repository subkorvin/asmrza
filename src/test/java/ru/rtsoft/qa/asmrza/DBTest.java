package ru.rtsoft.qa.asmrza;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;

public class DBTest {

    static public String DB_URL = "jdbc:postgresql://192.168.10.61:5432/asmdb";
    static public String USER = "postgres";
    static public String PASS = "postgres";

    @Test
    public void testDb() throws ClassNotFoundException, SQLException {

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select name from asm_substation");

//        ResultSetMetaData rsmd = rs.getMetaData();
//        int row = 0;
//        int columnsNumber = rsmd.getColumnCount();
        ArrayList<String> items = new ArrayList<String>();
        while (rs.next()) {
            int i = 1;
            //            for (int i = 1; i <= columnsNumber; i++) {
//            if (i > 1) System.out.print(",  ");
            String columnValue = rs.getString(i);
//            System.out.print(columnValue);  // + " " + rsmd.getColumnName(i));
            items.add(columnValue);
        }
//        row = row + 1;
        System.out.println("");
//    }
//        System.out.println(row);
        System.out.println(items);
        rs.close();
        st.close();
        connection.close();
//}
    }
}
