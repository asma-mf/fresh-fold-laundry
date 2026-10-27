package com.store.test_admin.service;

import com.store.test_admin.DTO.ReportDTO;
import com.store.test_admin.dao.ReportDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public ReportDTO generateSalesReport(String startDate, String endDate) {
        Map<String, Object> data = reportDAO.getSalesReportData(startDate, endDate);
        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        List<Map<String, Object>> details = (List<Map<String, Object>>) data.get("details");

        return new ReportDTO("Sales Report", LocalDateTime.now(), summary, details, startDate, endDate);
    }

    public ReportDTO generateCustomerReport(String startDate, String endDate) {
        Map<String, Object> data = reportDAO.getCustomerReportData(startDate, endDate);
        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        List<Map<String, Object>> details = (List<Map<String, Object>>) data.get("details");

        return new ReportDTO("Customer Report", LocalDateTime.now(), summary, details, startDate, endDate);
    }

    public ReportDTO generateOrderReport(String startDate, String endDate) {
        Map<String, Object> data = reportDAO.getOrderReportData(startDate, endDate);
        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        List<Map<String, Object>> details = (List<Map<String, Object>>) data.get("details");

        return new ReportDTO("Order Report", LocalDateTime.now(), summary, details, startDate, endDate);
    }

    public ReportDTO generatePaymentReport(String startDate, String endDate) {
        Map<String, Object> data = reportDAO.getPaymentReportData(startDate, endDate);
        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        List<Map<String, Object>> details = (List<Map<String, Object>>) data.get("details");

        return new ReportDTO("Payment Report", LocalDateTime.now(), summary, details, startDate, endDate);
    }
}