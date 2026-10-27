package com.store.test_admin.controller;

import com.store.test_admin.DTO.ReportDTO;
import com.store.test_admin.service.PdfExportService;
import com.store.test_admin.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class ReportController {
    private final ReportService reportService;
    private final PdfExportService pdfExportService;

    public ReportController() {
        this.reportService = new ReportService();
        this.pdfExportService = new PdfExportService();
    }

    @GetMapping("/reports")
    public String showReportPage() {
        return "report-generation";
    }

    @PostMapping("/generate-report")
    public String generateReport(@RequestParam String reportType,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 Model model) {
        try {
            ReportDTO report = null;

            switch (reportType) {
                case "sales":
                    report = reportService.generateSalesReport(startDate, endDate);
                    break;
                case "customer":
                    report = reportService.generateCustomerReport(startDate, endDate);
                    break;
                case "order":
                    report = reportService.generateOrderReport(startDate, endDate);
                    break;
                case "payment":
                    report = reportService.generatePaymentReport(startDate, endDate);
                    break;
                default:
                    model.addAttribute("error", "Invalid report type selected");
                    return "report-generation";
            }

            if (report != null && report.getSummary() != null && !report.getSummary().isEmpty()) {
                model.addAttribute("report", report);
                return "report-view";
            } else {
                model.addAttribute("error", "Failed to generate report. No data found for the selected period.");
                return "report-generation";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while generating the report: " + e.getMessage());
            return "report-generation";
        }
    }

    @GetMapping("/export-report")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String reportType,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            // Decode URL parameters
            String decodedReportType = URLDecoder.decode(reportType, StandardCharsets.UTF_8);
            String decodedStartDate = URLDecoder.decode(startDate, StandardCharsets.UTF_8);
            String decodedEndDate = URLDecoder.decode(endDate, StandardCharsets.UTF_8);

            // Generate the report data
            ReportDTO report = null;

            switch (decodedReportType) {
                case "Sales Report":
                    report = reportService.generateSalesReport(decodedStartDate, decodedEndDate);
                    break;
                case "Customer Report":
                    report = reportService.generateCustomerReport(decodedStartDate, decodedEndDate);
                    break;
                case "Order Report":
                    report = reportService.generateOrderReport(decodedStartDate, decodedEndDate);
                    break;
                case "Payment Report":
                    report = reportService.generatePaymentReport(decodedStartDate, decodedEndDate);
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            if (report == null) {
                return ResponseEntity.badRequest().build();
            }

            // Generate PDF
            byte[] pdfBytes = pdfExportService.generatePdfReport(report);

            // Create filename
            String filename = decodedReportType.replace(" ", "_") + "_Report_" +
                    decodedStartDate + "_to_" + decodedEndDate + ".pdf";

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}