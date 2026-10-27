package com.store.test_admin.dao;

import com.store.test_admin.DTO.PaymentManagementDTO;
import com.store.test_admin.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentManagementDAO {
    public List<PaymentManagementDTO> getAllPayments() {
        List<PaymentManagementDTO> paymentList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.payment_id, p.order_id, u.name as customer_name, ");
        sql.append("p.payment_method, p.amount, p.payment_date, p.status, ");
        sql.append("p.transaction_id, p.card_type, p.payment_notes ");
        sql.append("FROM payments p ");
        sql.append("JOIN orders o ON p.order_id = o.order_id ");
        sql.append("JOIN users u ON o.customer_id = u.user_id ");
        sql.append("ORDER BY p.payment_date DESC");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentManagementDTO dto = new PaymentManagementDTO();
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setPaymentMethod(rs.getString("payment_method"));
                dto.setAmount(rs.getBigDecimal("amount"));

                Timestamp timestamp = rs.getTimestamp("payment_date");
                if (timestamp != null) {
                    dto.setPaymentDate(timestamp.toLocalDateTime());
                }

                dto.setStatus(rs.getString("status"));
                dto.setTransactionId(rs.getString("transaction_id"));
                dto.setCardType(rs.getString("card_type"));
                dto.setPaymentNotes(rs.getString("payment_notes"));

                paymentList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    // Simple method to filter payments by status only
    public List<PaymentManagementDTO> filterPaymentsByStatus(String status) {
        List<PaymentManagementDTO> paymentList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.payment_id, p.order_id, u.name as customer_name, ");
        sql.append("p.payment_method, p.amount, p.payment_date, p.status, ");
        sql.append("p.transaction_id, p.card_type, p.payment_notes ");
        sql.append("FROM payments p ");
        sql.append("JOIN orders o ON p.order_id = o.order_id ");
        sql.append("JOIN users u ON o.customer_id = u.user_id ");

        // Add status filter if provided
        if (status != null && !status.isEmpty()) {
            sql.append(" WHERE p.status = ?");
        }

        sql.append(" ORDER BY p.payment_date DESC");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Set status parameter if provided
            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PaymentManagementDTO dto = new PaymentManagementDTO();
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setPaymentMethod(rs.getString("payment_method"));
                dto.setAmount(rs.getBigDecimal("amount"));

                Timestamp timestamp = rs.getTimestamp("payment_date");
                if (timestamp != null) {
                    dto.setPaymentDate(timestamp.toLocalDateTime());
                }

                dto.setStatus(rs.getString("status"));
                dto.setTransactionId(rs.getString("transaction_id"));
                dto.setCardType(rs.getString("card_type"));
                dto.setPaymentNotes(rs.getString("payment_notes"));

                paymentList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentList;
    }
}