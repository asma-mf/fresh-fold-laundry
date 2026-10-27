package com.store.test_admin.controller;

import com.store.test_admin.DTO.UserManagementDTO;
import com.store.test_admin.service.UserManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserManagementController {
    private UserManagementService userManagementService = new UserManagementService();

    @GetMapping("/user-management")
    public String userManagement(Model model) {
        List<UserManagementDTO> userList = userManagementService.getAllUsers();
        model.addAttribute("userList", userList);
        return "user-management";
    }

    @GetMapping("/user-management/filter")
    public String filterUsers(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            Model model) {

        List<UserManagementDTO> userList = userManagementService.filterUsers(type, search);
        model.addAttribute("userList", userList);
        model.addAttribute("selectedType", type);
        model.addAttribute("searchTerm", search);
        return "user-management";
    }
}