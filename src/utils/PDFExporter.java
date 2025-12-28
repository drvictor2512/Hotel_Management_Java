package utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import dao.PhongDAO;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PDFExporter {
    
    // Màu sắc theo giao diện
    private static final Color BLUE_COLOR = new DeviceRgb(0, 102, 204); // #0066cc
    private static final Color DARK_BLUE_COLOR = new DeviceRgb(40, 80, 130); // #285082
    private static final Color RED_COLOR = new DeviceRgb(220, 20, 60); // #dc143c
    private static final Color GRAY_COLOR = new DeviceRgb(80, 80, 80); // #505050
    private static final Color WHITE_COLOR = DeviceRgb.WHITE;
    
    // Font Unicode
    private static PdfFont fontRegular;
    private static PdfFont fontBold;
    private static PdfFont fontItalic;
    
    static {
        try {
            // Sử dụng font hệ thống có hỗ trợ tiếng Việt
            String fontPath = "c:/windows/fonts/arial.ttf";
            String fontBoldPath = "c:/windows/fonts/arialbd.ttf";
            String fontItalicPath = "c:/windows/fonts/ariali.ttf";
            
            fontRegular = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
            fontBold = PdfFontFactory.createFont(fontBoldPath, PdfEncodings.IDENTITY_H);
            fontItalic = PdfFontFactory.createFont(fontItalicPath, PdfEncodings.IDENTITY_H);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean xuatHoaDonPDF(
            String maHD, String tenKH, String maNV, Date ngayLap,
            ArrayList<Object[]> dsCTHD, double tongTien, 
            double tienTra, double tienThoi, String phuongThuc,
            ArrayList<Object[]> dsDV,
            double tienDV, double tongGiam, double tongThue) {
        
        try {
            // Tạo thư mục nếu chưa tồn tại
            File folder = new File("hoadon");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            String filePath = "hoadon/HoaDon_" + maHD + ".pdf";
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // ====== HEADER ======
            Table headerTable = new Table(2);
            headerTable.setWidth(UnitValue.createPercentValue(100));
            
            // Thông tin khách sạn (bên trái)
            Cell hotelCell = new Cell();
            hotelCell.setBorder(Border.NO_BORDER);
            hotelCell.add(new Paragraph("KHÁCH SẠN VICTORY")
                    .setFont(fontBold)
                    .setFontSize(12));
            hotelCell.add(new Paragraph("Địa chỉ: An Nhơn, TP. Hồ Chí Minh")
                    .setFont(fontRegular)
                    .setFontSize(11));
            hotelCell.add(new Paragraph("Hotline: 0939 799 999")
                    .setFont(fontRegular)
                    .setFontSize(11));
            
            // Tiêu đề (bên phải, căn giữa)
            Cell titleCell = new Cell();
            titleCell.setBorder(Border.NO_BORDER);
            titleCell.setTextAlignment(TextAlignment.RIGHT);
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN")
                    .setFont(fontBold)
                    .setFontSize(22)
                    .setFontColor(BLUE_COLOR);
            titleCell.add(title);
            
            headerTable.addCell(hotelCell);
            headerTable.addCell(titleCell);
            
            document.add(headerTable);
            document.add(new Paragraph("\n"));
            
            // ====== THÔNG TIN CHUNG ======
            document.add(createInfoLine("Mã hóa đơn:", maHD));
            document.add(createInfoLine("Khách hàng:", tenKH));
            document.add(createInfoLine("Nhân viên lập:", maNV));
            document.add(createInfoLine("Ngày lập:", sdf.format(ngayLap)));
            document.add(createInfoLine("Phương thức:", phuongThuc));
            
            document.add(new Paragraph("\n"));
            
            // Đường kẻ ngang
            Paragraph line1 = new Paragraph("_________________________________________________________________")
                    .setFont(fontRegular)
                    .setFontColor(GRAY_COLOR);
            document.add(line1);
            document.add(new Paragraph("\n"));
            
            // ====== BẢNG CHI TIẾT PHÒNG ======
            Paragraph detailTitle = new Paragraph("CHI TIẾT PHÒNG")
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setFontColor(DARK_BLUE_COLOR);
            document.add(detailTitle);
            document.add(new Paragraph("\n"));
            
            // Tạo bảng chi tiết phòng - 7 columns
            Table table = new Table(new float[]{1.5f, 1.2f, 1.8f, 1.8f, 1.5f, 1.5f, 1.8f});
            table.setWidth(UnitValue.createPercentValue(100));
            
            // Header bảng
            table.addHeaderCell(createHeaderCell("Phòng"));
            table.addHeaderCell(createHeaderCell("Số ngày"));
            table.addHeaderCell(createHeaderCell("Ngày nhận"));
            table.addHeaderCell(createHeaderCell("Ngày trả"));
            table.addHeaderCell(createHeaderCell("Đơn giá"));
            table.addHeaderCell(createHeaderCell("Tiền cọc"));
            table.addHeaderCell(createHeaderCell("Thanh toán"));
            
            // Dữ liệu bảng
            for (Object[] ct : dsCTHD) {
                String maP = String.valueOf(ct[1]);
                double donGia = PhongDAO.getMaP(maP).getDonGia();
                double coc = donGia * 0.3;
                
                table.addCell(createTableCell(ct[1].toString(), TextAlignment.CENTER));
                table.addCell(createTableCell(ct[2].toString(), TextAlignment.CENTER));
                table.addCell(createTableCell(ct[3].toString(), TextAlignment.CENTER));
                table.addCell(createTableCell(ct[4].toString(), TextAlignment.CENTER));
                table.addCell(createTableCell(String.format("%,.0f", donGia), TextAlignment.RIGHT));
                table.addCell(createTableCell(String.format("%,.0f", coc), TextAlignment.RIGHT));
                table.addCell(createTableCell(String.format("%,.0f", ct[5]), TextAlignment.RIGHT));
            }
            
            document.add(table);
            document.add(new Paragraph("\n"));
            
            // Đường kẻ ngang
            Paragraph line2 = new Paragraph("_________________________________________________________________")
                    .setFont(fontRegular)
                    .setFontColor(GRAY_COLOR);
            document.add(line2);
            document.add(new Paragraph("\n"));
            
            // ====== BẢNG CHI TIẾT DỊCH VỤ ======
            Paragraph detailTitle1 = new Paragraph("CHI TIẾT DỊCH VỤ")
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setFontColor(DARK_BLUE_COLOR);
            document.add(detailTitle1);
            document.add(new Paragraph("\n"));
            
            // Tạo bảng chi tiết dịch vụ - 4 columns
            Table table1 = new Table(new float[]{3, 1.5f, 2, 2.5f});
            table1.setWidth(UnitValue.createPercentValue(100));
            
            // Header bảng
            table1.addHeaderCell(createHeaderCell("Dịch vụ"));
            table1.addHeaderCell(createHeaderCell("Số lượng"));
            table1.addHeaderCell(createHeaderCell("Đơn giá"));
            table1.addHeaderCell(createHeaderCell("Thanh toán"));
            
            // Dữ liệu bảng
            for (Object[] dv : dsDV) {
                double donGia = Double.parseDouble(dv[1].toString());
                int soLuong = Integer.parseInt(dv[2].toString());
                double tong = donGia * soLuong;
                
                table1.addCell(createTableCell(dv[0].toString(), TextAlignment.LEFT));
                table1.addCell(createTableCell(dv[2].toString(), TextAlignment.CENTER));
                table1.addCell(createTableCell(String.format("%,.0f", donGia), TextAlignment.RIGHT));
                table1.addCell(createTableCell(String.format("%,.0f", tong), TextAlignment.RIGHT));
            }
            
            document.add(table1);
            document.add(new Paragraph("\n"));
            
            // Đường kẻ ngang
            Paragraph line3 = new Paragraph("_________________________________________________________________")
                    .setFont(fontRegular)
                    .setFontColor(GRAY_COLOR);
            document.add(line3);
            document.add(new Paragraph("\n"));
            
            // ====== TỔNG TIỀN VÀ CÁC KHOẢN PHỤ ======
            document.add(createInfoLine("Tiền dịch vụ:", String.format("%,.0f VNĐ", tienDV)));
            document.add(createInfoLine("Khuyến mãi:", String.format("-%,.0f VNĐ", tongGiam)));
            document.add(createInfoLine("Tiền thuế:", String.format("%,.0f VNĐ", tongThue)));
            
            document.add(new Paragraph("\n"));
            
            // ====== TỔNG CỘNG ======
            Table totalTable = new Table(new float[]{1, 2});
            totalTable.setWidth(UnitValue.createPercentValue(100));
            
            Cell leftCell = new Cell();
            leftCell.setBorder(Border.NO_BORDER);
            leftCell.setPaddingBottom(10);
            leftCell.add(new Paragraph("TỔNG CỘNG:")
                    .setFont(fontBold)
                    .setFontSize(18));
            
            Cell rightCell = new Cell();
            rightCell.setBorder(Border.NO_BORDER);
            rightCell.setPaddingBottom(10);
            rightCell.setTextAlignment(TextAlignment.RIGHT);
            rightCell.add(new Paragraph(String.format("%,.0f VNĐ", tongTien))
                    .setFont(fontBold)
                    .setFontSize(18)
                    .setFontColor(RED_COLOR));
            
            totalTable.addCell(leftCell);
            totalTable.addCell(rightCell);
            document.add(totalTable);
            
            // Thông tin thanh toán
            document.add(createInfoLine("Khách trả:", String.format("%,.0f VNĐ", tienTra)));
            document.add(createInfoLine("Tiền thối:", String.format("%,.0f VNĐ", tienThoi)));
            
            document.add(new Paragraph("\n\n"));
            
            // Lời cảm ơn
            Paragraph thanks = new Paragraph("Cảm ơn quý khách đã sử dụng dịch vụ!")
                    .setFont(fontItalic)
                    .setFontColor(GRAY_COLOR)
                    .setFontSize(15)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(thanks);
            
            document.close();
            
            // Mở file PDF
            openPDFFile(filePath);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ====== CÁC PHƯƠNG THỨC HỖ TRỢ ======
    
    private static Table createInfoLine(String label, String value) {
        Table infoTable = new Table(new float[]{1, 2});
        infoTable.setWidth(UnitValue.createPercentValue(100));
        
        Cell labelCell = new Cell();
        labelCell.setBorder(Border.NO_BORDER);
        labelCell.setPaddingBottom(5);
        labelCell.add(new Paragraph(label)
                .setFont(fontRegular)
                .setFontSize(14));
        
        Cell valueCell = new Cell();
        valueCell.setBorder(Border.NO_BORDER);
        valueCell.setPaddingBottom(5);
        valueCell.setTextAlignment(TextAlignment.RIGHT);
        valueCell.add(new Paragraph(value)
                .setFont(fontBold)
                .setFontSize(14));
        
        infoTable.addCell(labelCell);
        infoTable.addCell(valueCell);
        
        return infoTable;
    }
    
    private static Cell createHeaderCell(String text) {
        Cell cell = new Cell();
        cell.setBackgroundColor(DARK_BLUE_COLOR);
        cell.setBorder(new SolidBorder(WHITE_COLOR, 1));
        cell.add(new Paragraph(text)
                .setFont(fontBold)
                .setFontSize(13)
                .setFontColor(WHITE_COLOR)
                .setTextAlignment(TextAlignment.CENTER));
        cell.setPadding(8);
        return cell;
    }
    
    private static Cell createTableCell(String text, TextAlignment alignment) {
        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(GRAY_COLOR, 0.5f));
        cell.add(new Paragraph(text)
                .setFont(fontRegular)
                .setFontSize(12)
                .setTextAlignment(alignment));
        cell.setPadding(6);
        return cell;
    }
    
    private static void openPDFFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}