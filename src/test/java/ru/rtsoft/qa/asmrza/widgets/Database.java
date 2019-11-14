package ru.rtsoft.qa.asmrza.widgets;

import java.sql.*;

public class Database {

    private static final String DB_URL = "jdbc:postgresql://192.168.10.61:5432/asmdb";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    private Connection connection;

    public void connect() throws SQLException {
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

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
    }

    public int getNumberOfItems(String sqlQuery) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select * from asm_substation");

        ResultSetMetaData rsmd = rs.getMetaData();
        int row = 0;
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            row = row + 1;
            System.out.println("");
        }
        System.out.println(row);
        return row;
    }
}
