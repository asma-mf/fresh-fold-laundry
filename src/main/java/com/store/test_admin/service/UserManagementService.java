package com.store.test_admin.service;

import com.store.test_admin.DTO.UserManagementDTO;
import com.store.test_admin.dao.UserManagementDAO;

import java.util.List;

public class UserManagementService {
    private UserManagementDAO userManagementDAO = new UserManagementDAO();

    public List<UserManagementDTO> getAllUsers() {
        return userManagementDAO.getAllUsers();
    }

    public List<UserManagementDTO> filterUsers(String type, String searchTerm) {
        return userManagementDAO.filterUsers(type, searchTerm);
    }
}