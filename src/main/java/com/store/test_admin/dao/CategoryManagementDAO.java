package com.store.test_admin.dao;

import com.store.test_admin.DTO.CategoryManagementDTO;
import com.store.test_admin.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManagementDAO {
    public List<CategoryManagementDTO> getAllCategories() {
        List<CategoryManagementDTO> categoryList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.category_id, c.category_name, c.description, c.icon_class, ");
        sql.append("(SELECT COUNT(*) FROM clothes WHERE clothes.category_id = c.category_id) as clothes_count ");
        sql.append("FROM categories c ORDER BY c.category_name");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryManagementDTO dto = new CategoryManagementDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setCategoryName(rs.getString("category_name"));
                dto.setDescription(rs.getString("description"));
                dto.setIconClass(rs.getString("icon_class"));
                dto.setClothesCount(rs.getInt("clothes_count"));

                // Fetch clothes names for this category
                dto.setClothesNames(getClothesNamesForCategory(con, dto.getCategoryId()));

                categoryList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    private List<String> getClothesNamesForCategory(Connection con, int categoryId) throws SQLException {
        List<String> clothesNames = new ArrayList<>();
        String sql = "SELECT TOP 5 name FROM clothes WHERE category_id = ? ORDER BY name";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                clothesNames.add(rs.getString("name"));
            }
        }
        return clothesNames;
    }

    public boolean addCategory(String categoryName, String description, String iconClass) {
        String sql = "INSERT INTO categories (category_name, description, icon_class) VALUES (?, ?, ?)";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);
            ps.setString(2, description);
            ps.setString(3, iconClass);

            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}