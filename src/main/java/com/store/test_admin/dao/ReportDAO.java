package com.store.test_admin.dao;

import com.store.test_admin.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {
    // Sales Report Data
    public Map<String, Object> getSalesReportData(String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();

        // Initialize with default values
        summary.put("totalSales", 0.0);
        summary.put("totalOrders", 0);
        summary.put("avgOrderValue", 0.0);

        // Get total sales and order count
        String summarySql = "SELECT SUM(CAST(p.amount AS DECIMAL(18,2))) as totalSales, " +
                "COUNT(*) as totalOrders, " +
                "AVG(CAST(p.amount AS DECIMAL(18,2))) as avgOrderValue " +
                "FROM payments p " +
                "WHERE p.payment_date BETWEEN ? AND ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(summarySql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                summary.put("totalSales", rs.getDouble("totalSales"));
                summary.put("totalOrders", rs.getInt("totalOrders"));
                summary.put("avgOrderValue", rs.getDouble("avgOrderValue"));
            }
        } catch (SQLException e) {
            System.err.println("Error in sales report summary query: " + e.getMessage());
            e.printStackTrace();
        }

        // Get sales by category
        String detailSql = "SELECT c.category_name, " +
                "SUM(CAST(p.amount AS DECIMAL(18,2))) as categorySales, " +
                "COUNT(p.order_id) as categoryOrders " +
                "FROM payments p " +
                "JOIN orders o ON p.order_id = o.order_id " +
                "JOIN orderItems oi ON o.order_id = oi.order_id " +
                "JOIN clothes cl ON oi.clothes_id = cl.clothes_id " +
                "JOIN categories c ON cl.category_id = c.category_id " +
                "WHERE p.payment_date BETWEEN ? AND ? " +
                "GROUP BY c.category_name " +
                "ORDER BY categorySales DESC";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(detailSql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("categoryName", rs.getString("category_name"));
                detail.put("categorySales", rs.getDouble("categorySales"));
                detail.put("categoryOrders", rs.getInt("categoryOrders"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error in sales report detail query: " + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("summary", summary);
        result.put("details", details);
        return result;
    }

    // Customer Report Data
    public Map<String, Object> getCustomerReportData(String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();

        // Initialize with default values
        summary.put("totalCustomers", 0);
        summary.put("newCustomers", 0);
        summary.put("avgCustomerSpent", 0.0);

        // Get customer statistics
        String summarySql = "SELECT COUNT(DISTINCT cd.customer_id) as totalCustomers, " +
                "SUM(CAST(o.total_amount AS DECIMAL(18,2))) as totalSpent " +
                "FROM customersDetails cd " +
                "LEFT JOIN orders o ON cd.customer_id = o.customer_id " +
                "WHERE o.order_date BETWEEN ? AND ? OR o.order_date IS NULL";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(summarySql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                summary.put("totalCustomers", rs.getInt("totalCustomers"));
                summary.put("totalSpent", rs.getDouble("totalSpent"));

                // Calculate average customer spent
                if (rs.getInt("totalCustomers") > 0) {
                    summary.put("avgCustomerSpent", rs.getDouble("totalSpent") / rs.getInt("totalCustomers"));
                } else {
                    summary.put("avgCustomerSpent", 0.0);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in customer report summary query: " + e.getMessage());
            e.printStackTrace();
        }

        // Get top customers by spending
        String detailSql = "SELECT TOP 10 cd.customer_id, " +
                "SUM(CAST(o.total_amount AS DECIMAL(18,2))) as totalSpent, " +
                "COUNT(o.order_id) as orderCount " +
                "FROM customersDetails cd " +
                "JOIN orders o ON cd.customer_id = o.customer_id " +
                "WHERE o.order_date BETWEEN ? AND ? " +
                "GROUP BY cd.customer_id " +
                "ORDER BY totalSpent DESC";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(detailSql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("customerId", rs.getInt("customer_id"));
                detail.put("totalSpent", rs.getDouble("totalSpent"));
                detail.put("orderCount", rs.getInt("orderCount"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error in customer report detail query: " + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("summary", summary);
        result.put("details", details);
        return result;
    }

    // Order Report Data
    public Map<String, Object> getOrderReportData(String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();

        // Initialize with default values
        summary.put("totalOrders", 0);
        summary.put("avgOrderValue", 0.0);
        summary.put("completedOrders", 0);
        summary.put("pendingOrders", 0);

        // Get order statistics
        String summarySql = "SELECT COUNT(*) as totalOrders, " +
                "AVG(CAST(total_amount AS DECIMAL(18,2))) as avgOrderValue, " +
                "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completedOrders, " +
                "SUM(CASE WHEN status = 'Pending' THEN 1 ELSE 0 END) as pendingOrders " +
                "FROM orders " +
                "WHERE order_date BETWEEN ? AND ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(summarySql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                summary.put("totalOrders", rs.getInt("totalOrders"));
                summary.put("avgOrderValue", rs.getDouble("avgOrderValue"));
                summary.put("completedOrders", rs.getInt("completedOrders"));
                summary.put("pendingOrders", rs.getInt("pendingOrders"));
            }
        } catch (SQLException e) {
            System.err.println("Error in order report summary query: " + e.getMessage());
            e.printStackTrace();
        }

        // Get orders by status
        String detailSql = "SELECT status, COUNT(*) as count, AVG(CAST(total_amount AS DECIMAL(18,2))) as avgAmount " +
                "FROM orders " +
                "WHERE order_date BETWEEN ? AND ? " +
                "GROUP BY status";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(detailSql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("status", rs.getString("status"));
                detail.put("count", rs.getInt("count"));
                detail.put("avgAmount", rs.getDouble("avgAmount"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error in order report detail query: " + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("summary", summary);
        result.put("details", details);
        return result;
    }

    // Payment Report Data
    public Map<String, Object> getPaymentReportData(String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();

        // Initialize with default values
        summary.put("totalPayments", 0);
        summary.put("totalRevenue", 0.0);
        summary.put("avgPaymentAmount", 0.0);

        // Get payment statistics
        String summarySql = "SELECT COUNT(*) as totalPayments, " +
                "SUM(CAST(amount AS DECIMAL(18,2))) as totalRevenue, " +
                "AVG(CAST(amount AS DECIMAL(18,2))) as avgPaymentAmount " +
                "FROM payments " +
                "WHERE payment_date BETWEEN ? AND ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(summarySql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                summary.put("totalPayments", rs.getInt("totalPayments"));
                summary.put("totalRevenue", rs.getDouble("totalRevenue"));
                summary.put("avgPaymentAmount", rs.getDouble("avgPaymentAmount"));
            }
        } catch (SQLException e) {
            System.err.println("Error in payment report summary query: " + e.getMessage());
            e.printStackTrace();
        }

        // Get payments by method
        String detailSql = "SELECT payment_method, COUNT(*) as count, SUM(CAST(amount AS DECIMAL(18,2))) as totalAmount " +
                "FROM payments " +
                "WHERE payment_date BETWEEN ? AND ? " +
                "GROUP BY payment_method";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(detailSql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("paymentMethod", rs.getString("payment_method"));
                detail.put("count", rs.getInt("count"));
                detail.put("totalAmount", rs.getDouble("totalAmount"));
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error in payment report detail query: " + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("summary", summary);
        result.put("details", details);
        return result;
    }
}