package com.store.test_admin.service;

import com.store.test_admin.DTO.CategoryManagementDTO;
import com.store.test_admin.dao.CategoryManagementDAO;

import java.util.List;

public class CategoryManagementService {
    private CategoryManagementDAO categoryDAO = new CategoryManagementDAO();

    public List<CategoryManagementDTO> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public boolean addCategory(String categoryName, String description, String iconClass) {
        return categoryDAO.addCategory(categoryName, description, iconClass);
    }
}
