package com.store.test_admin;

import com.store.test_admin.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {

        // Get connection
        Connection conn = null;
        conn = DBConnect.getConnection();
        if (conn == null) {
            System.out.println("Failed to connect to database");
            return;
        }

        // Get data
        String sql = "SELECT * FROM staffMembers;";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);

            // Get first row
            boolean isExist = rs.next();


            if (isExist) {
                System.out.println(rs.getInt("staffid"));
                System.out.println(rs.getString("firstName"));
            }

            // get second row
            isExist = rs.next();

            if (isExist) {
                System.out.println(rs.getInt("staffid"));
                System.out.println(rs.getString("firstName"));
            }

            // Get all the rows
            while (rs.next()){
                System.out.print(rs.getInt("staffid") + "  ");
                System.out.println(rs.getString("firstName"));
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);

            // Get first row
            boolean isExist = rs.next();

            if (isExist) {
                System.out.println(rs.getInt("staffid"));
                System.out.println(rs.getString("firstName"));
            }

            // get second row
            isExist = rs.next();

            if (isExist) {
                System.out.println(rs.getInt("staffid"));
                System.out.println(rs.getString("firstName"));
            }

            // Get all the rows
            while (rs.next()){
                System.out.print(rs.getInt("staffid") + "  ");
                System.out.println(rs.getString("firstName"));
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////////////////
        System.out.println("\n\n\n\n\n\n");
        sql = "SELECT * FROM staffMembers WHERE firstName = ?";
        try (Connection con2 = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setString(1,"Mohomed");

            ResultSet rs = ps.executeQuery();

            // Get all the rows
            while (rs.next()){
                System.out.print(rs.getInt("staffid") + "  ");
                System.out.println(rs.getString("firstName"));
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
