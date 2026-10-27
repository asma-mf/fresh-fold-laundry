package com.store.test_admin.service;

import com.store.test_admin.DTO.PaymentManagementDTO;
import com.store.test_admin.dao.PaymentManagementDAO;

import java.util.List;

public class PaymentManagementService {
    private PaymentManagementDAO paymentDAO = new PaymentManagementDAO();

    public List<PaymentManagementDTO> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    public List<PaymentManagementDTO> filterPaymentsByStatus(String status) {
        return paymentDAO.filterPaymentsByStatus(status);
    }
}