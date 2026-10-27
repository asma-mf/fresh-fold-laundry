package com.store.test_admin.service;

import com.store.test_admin.DTO.OrderManagementDTO;
import com.store.test_admin.dao.OrderManagementDAO;

import java.util.List;

public class OrderManagementService {
    private OrderManagementDAO orderManagementDAO = new OrderManagementDAO();

    public List<OrderManagementDTO> getAllOrders() {
        return orderManagementDAO.getAllOrders();
    }

    public List<OrderManagementDTO> filterOrders(String status, String searchTerm) {
        return orderManagementDAO.filterOrders(status, searchTerm);
    }
}
