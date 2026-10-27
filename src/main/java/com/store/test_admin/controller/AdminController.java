package com.store.test_admin.controller;

import com.store.test_admin.model.Admin;
import com.store.test_admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService = new AdminService();

    @RequestMapping("/")
    public String index() {
        return "AdminLoginForm";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "AdminLoginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if (adminService.login(username, password)) {
            return "AdminDashboard";
        } else {
            return "AdminLoginForm";

        }
    }

    @GetMapping("/AdminDashboard")
    public String adminDashboard() {
        return "AdminDashboard";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "AdminRegisterForm";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName, @RequestParam String userName, @RequestParam String password, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String role) {
        Admin admin = new Admin();
        admin.setFullName(fullName);
        admin.setUserName(userName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        admin.setRole(role);

        if (adminService.addAdmin(admin)) {
            return "AdminLoginForm"; // success
        } else {
            return "AdminRegisterForm"; // fail
        }
    }

    @GetMapping("/admins")
    public String viewAdmin(Model model) {
        List<Admin> adminList = adminService.getAllAdmin();
        model.addAttribute("adminList", adminList);
        return "AdminList";
    }

    // method for view specific admin
    @GetMapping("/admin/{id}")
    public String viewAdminProfile(@PathVariable ("id") int id, Model model) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            model.addAttribute("admin", admin);
            return "AdminProfile";
        }
        return "redirect:/admins";
    }

    // method to handle admin deletion
    @GetMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable ("id") int id) {
        adminService.deleteAdmin(id);
        return "redirect:/admins";
    }

    // method to show edit form
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable ("id") int id, Model model) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            model.addAttribute("admin", admin);
            return "AdminEditForm";
        }
        return "redirect:/admins";
    }

    // method for handle admin update
    @PostMapping("/admin/update/{id}")
    public String updateAdmin(@PathVariable ("id") int id, @RequestParam String fullName, @RequestParam String userName, @RequestParam String password, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String role, @RequestParam String status){
        Admin admin = new Admin();
        admin.setAdminid(id);
        admin.setFullName(fullName);
        admin.setUserName(userName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        admin.setRole(role);
        admin.setStatus(status);

        adminService.updateAdmin(admin);
        return "redirect:/admins";

    }

    // Add logout mapping
    @PostMapping("/logout")
    public String logout() {
        // Invalidate session or perform logout operations
        return "redirect:/login";
    }

}
