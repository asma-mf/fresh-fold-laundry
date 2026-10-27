package com.store.test_admin.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportDTO {
    private String reportType;
    private LocalDateTime generatedDate;
    private Map<String, Object> summary;
    private List<Map<String, Object>> details;
    private String startDate;
    private String endDate;

    // Constructors
    public ReportDTO() {}

    public ReportDTO(String reportType, LocalDateTime generatedDate, Map<String, Object> summary,
                     List<Map<String, Object>> details, String startDate, String endDate) {
        this.reportType = reportType;
        this.generatedDate = generatedDate;
        this.summary = summary;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }

    public Map<String, Object> getSummary() { return summary; }
    public void setSummary(Map<String, Object> summary) { this.summary = summary; }

    public List<Map<String, Object>> getDetails() { return details; }
    public void setDetails(List<Map<String, Object>> details) { this.details = details; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}