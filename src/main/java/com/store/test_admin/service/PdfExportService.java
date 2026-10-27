package com.store.test_admin.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.store.test_admin.DTO.ReportDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PdfExportService {

    // Method to generate PDF from HTML (better for complex layouts)
    public byte[] generatePdfFromHtml(String html) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.toStream(outputStream);
            builder.withHtmlContent(html, null);
            builder.run();
        } catch (Exception e) {
            throw new IOException("Failed to generate PDF from HTML", e);
        }

        return outputStream.toByteArray();
    }

    public byte[] generatePdfFromReportView(ReportDTO report, String htmlTemplate) throws IOException {
        // Replace placeholders in HTML template with actual data
        String processedHtml = htmlTemplate
                .replace("${report.reportType}", report.getReportType())
                .replace("${report.startDate}", report.getStartDate())
                .replace("${report.endDate}", report.getEndDate())
                .replace("${report.generatedDate}", report.getGeneratedDate().toString());

        return generatePdfFromHtml(processedHtml);
    }

    // Method to generate PDF using iText (better for programmatic control)
    public byte[] generatePdfReport(ReportDTO report) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph(report.getReportType(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add date range
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        Paragraph dateRange = new Paragraph("Period: " + report.getStartDate() + " to " + report.getEndDate(), dateFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);

        // Add generation date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Paragraph genDate = new Paragraph("Generated: " + report.getGeneratedDate().format(formatter), dateFont);
        genDate.setAlignment(Element.ALIGN_CENTER);
        document.add(genDate);

        // Add spacing
        document.add(Chunk.NEWLINE);

        // Add summary section
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Paragraph summaryTitle = new Paragraph("Summary", summaryFont);
        document.add(summaryTitle);

        // Create summary table
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);

        // Add summary data
        Map<String, Object> summary = report.getSummary();

        if (report.getReportType().equals("Sales Report")) {
            addSummaryRow(summaryTable, "Total Sales", "$" + summary.getOrDefault("totalSales", 0));
            addSummaryRow(summaryTable, "Total Orders", summary.getOrDefault("totalOrders", 0).toString());
            addSummaryRow(summaryTable, "Avg Order Value", "$" + summary.getOrDefault("avgOrderValue", 0));
        }
        else if (report.getReportType().equals("Customer Report")) {
            addSummaryRow(summaryTable, "Total Customers", summary.getOrDefault("totalCustomers", 0).toString());
            addSummaryRow(summaryTable, "New Customers", summary.getOrDefault("newCustomers", 0).toString());
            addSummaryRow(summaryTable, "Avg Customer Spent", "$" + summary.getOrDefault("avgCustomerSpent", 0));
        }
        else if (report.getReportType().equals("Order Report")) {
            addSummaryRow(summaryTable, "Total Orders", summary.getOrDefault("totalOrders", 0).toString());
            addSummaryRow(summaryTable, "Completed Orders", summary.getOrDefault("completedOrders", 0).toString());
            addSummaryRow(summaryTable, "Pending Orders", summary.getOrDefault("pendingOrders", 0).toString());
        }
        else if (report.getReportType().equals("Payment Report")) {
            addSummaryRow(summaryTable, "Total Revenue", "$" + summary.getOrDefault("totalRevenue", 0));
            addSummaryRow(summaryTable, "Total Payments", summary.getOrDefault("totalPayments", 0).toString());
            addSummaryRow(summaryTable, "Avg Payment Amount", "$" + summary.getOrDefault("avgPaymentAmount", 0));
        }

        document.add(summaryTable);

        // Add spacing
        document.add(Chunk.NEWLINE);

        // Add details section
        Paragraph detailsTitle = new Paragraph("Details", summaryFont);
        document.add(detailsTitle);

        // Create details table
        List<Map<String, Object>> details = report.getDetails();
        if (!details.isEmpty()) {
            // Determine number of columns based on report type
            int numColumns = 3;

            PdfPTable detailsTable = new PdfPTable(numColumns);
            detailsTable.setWidthPercentage(100);

            // Add headers based on report type
            if (report.getReportType().equals("Sales Report")) {
                addTableHeader(detailsTable, new String[]{"Category", "Sales Amount", "Number of Orders"});

                // Add data rows
                for (Map<String, Object> detail : details) {
                    detailsTable.addCell(detail.getOrDefault("categoryName", "").toString());
                    detailsTable.addCell("$" + detail.getOrDefault("categorySales", 0));
                    detailsTable.addCell(detail.getOrDefault("categoryOrders", 0).toString());
                }
            }
            else if (report.getReportType().equals("Customer Report")) {
                addTableHeader(detailsTable, new String[]{"Customer ID", "Total Spent", "Order Count"});

                // Add data rows
                for (Map<String, Object> detail : details) {
                    detailsTable.addCell(detail.getOrDefault("customerId", "").toString());
                    detailsTable.addCell("$" + detail.getOrDefault("totalSpent", 0));
                    detailsTable.addCell(detail.getOrDefault("orderCount", 0).toString());
                }
            }
            else if (report.getReportType().equals("Order Report")) {
                addTableHeader(detailsTable, new String[]{"Status", "Count", "Average Amount"});

                // Add data rows
                for (Map<String, Object> detail : details) {
                    detailsTable.addCell(detail.getOrDefault("status", "").toString());
                    detailsTable.addCell(detail.getOrDefault("count", 0).toString());
                    detailsTable.addCell("$" + detail.getOrDefault("avgAmount", 0));
                }
            }
            else if (report.getReportType().equals("Payment Report")) {
                addTableHeader(detailsTable, new String[]{"Payment Method", "Count", "Total Amount"});

                // Add data rows
                for (Map<String, Object> detail : details) {
                    detailsTable.addCell(detail.getOrDefault("paymentMethod", "").toString());
                    detailsTable.addCell(detail.getOrDefault("count", 0).toString());
                    detailsTable.addCell("$" + detail.getOrDefault("totalAmount", 0));
                }
            }

            document.add(detailsTable);
        } else {
            Paragraph noData = new Paragraph("No detailed data available for this report.");
            document.add(noData);
        }

        document.close();
        return outputStream.toByteArray();
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
}