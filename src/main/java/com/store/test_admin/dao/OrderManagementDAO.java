package com.store.test_admin.dao;

import com.store.test_admin.DTO.OrderManagementDTO;
import com.store.test_admin.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementDAO {
    public List<OrderManagementDTO> getAllOrders() {
        List<OrderManagementDTO> orderList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.order_id, u.name as customer_name, u.email as customer_email, ");
        sql.append("o.order_date, o.total_amount, o.status, o.delivery_address, ");
        sql.append("d.name as driver_name, o.order_notes ");
        sql.append("FROM orders o ");
        sql.append("JOIN users u ON o.customer_id = u.user_id ");
        sql.append("LEFT JOIN users d ON o.assigned_driver_id = d.user_id");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderManagementDTO dto = new OrderManagementDTO();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerEmail(rs.getString("customer_email"));

                // Convert SQL timestamp to LocalDateTime
                Timestamp timestamp = rs.getTimestamp("order_date");
                if (timestamp != null) {
                    dto.setOrderDate(timestamp.toLocalDateTime());
                }

                dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                dto.setStatus(rs.getString("status"));
                dto.setDeliveryAddress(rs.getString("delivery_address"));
                dto.setDriverName(rs.getString("driver_name"));
                dto.setOrderNotes(rs.getString("order_notes"));
                orderList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<OrderManagementDTO> filterOrders(String status, String searchTerm) {
        List<OrderManagementDTO> allOrders = getAllOrders();
        List<OrderManagementDTO> filteredOrders = new ArrayList<>();

        for (OrderManagementDTO order : allOrders) {
            boolean statusMatch = (status == null || status.isEmpty() ||
                    order.getStatus().equalsIgnoreCase(status));

            boolean searchMatch = (searchTerm == null || searchTerm.isEmpty() ||
                    order.getCustomerName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    order.getCustomerEmail().toLowerCase().contains(searchTerm.toLowerCase()));

            if (statusMatch && searchMatch) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}