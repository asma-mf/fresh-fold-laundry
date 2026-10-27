package com.store.test_admin.dao;

import com.store.test_admin.DTO.UserManagementDTO;
import com.store.test_admin.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDAO {
    public List<UserManagementDTO> getAllUsers() {
        List<UserManagementDTO> userList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.user_id, u.name, u.email, ");
        sql.append("CASE WHEN sd.staff_id IS NOT NULL THEN 'Staff' ");
        sql.append("WHEN cd.customer_id IS NOT NULL THEN 'Customer' END AS user_type, ");
        sql.append("sd.position, d.department_name, sd.hire_date, sd.salary, ");
        sql.append("cd.phone_number, cd.address, cd.loyalty_points ");
        sql.append("FROM users u ");
        sql.append("LEFT JOIN staffDetails sd ON u.user_id = sd.staff_id ");
        sql.append("LEFT JOIN department d ON sd.department_id = d.department_id ");
        sql.append("LEFT JOIN customersDetails cd ON u.user_id = cd.customer_id ");
        sql.append("WHERE u.user_id NOT IN (SELECT ur.user_id FROM userRole ur JOIN roles r ON ur.role_id = r.role_id WHERE r.role_name = 'Admin')");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserManagementDTO dto = new UserManagementDTO();
                dto.setId(rs.getInt("user_id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setType(rs.getString("user_type"));

                if ("Staff".equals(rs.getString("user_type"))) {
                    dto.setPosition(rs.getString("position"));
                    dto.setDepartment(rs.getString("department_name"));
                    dto.setHireDate(rs.getDate("hire_date"));
                    dto.setSalary(rs.getDouble("salary"));
                } else if ("Customer".equals(rs.getString("user_type"))) {
                    dto.setPhoneNumber(rs.getString("phone_number"));
                    dto.setAddress(rs.getString("address"));
                    dto.setLoyaltyPoints(rs.getInt("loyalty_points"));
                }
                userList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<UserManagementDTO> filterUsers(String type, String searchTerm) {
        List<UserManagementDTO> allUsers = getAllUsers();
        List<UserManagementDTO> filteredUsers = new ArrayList<>();

        for (UserManagementDTO user : allUsers) {
            boolean typeMatch = (type == null || type.isEmpty() || user.getType().equals(type));
            boolean searchMatch = (searchTerm == null || searchTerm.isEmpty() ||
                    user.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(searchTerm.toLowerCase()));

            if (typeMatch && searchMatch) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
}