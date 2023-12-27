package com.rhymthwave.Utilities;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    public  static  String nameFile = "";

	public <T> void exportToExcel(List<T> data, HttpServletResponse response) throws IOException {
        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang trong workbook
        Sheet sheet = workbook.createSheet("Data");

        // Tạo dòng đầu tiên cho tiêu đề
        Row headerRow = sheet.createRow(0);

        // Sử dụng reflection để lấy danh sách các trường của đối tượng đầu tiên trong danh sách
        T firstObject = data.get(0);
        Field[] fields = firstObject.getClass().getDeclaredFields();

        // Đặt tiêu đề cho các cột dựa trên tên trường
        int colNum = 0;
        for (Field field : fields) {
            headerRow.createCell(colNum).setCellValue(field.getName());
            colNum++;
        }
        
        // Ghi dữ liệu từ danh sách các đối tượng vào các dòng
        int rowNum = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowNum);
            colNum = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    if (value != null) {
                        if (value instanceof String) {
                            row.createCell(colNum).setCellValue((String) value);
                        } else if (value instanceof Integer) {
                            row.createCell(colNum).setCellValue((Integer) value);
                        }else if (value instanceof Date) {
                            row.createCell(colNum).setCellValue((Date) value);
                        }
                        // Thêm kiểu dữ liệu khác nếu cần
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                colNum++;
            }
            rowNum++;
        }
        String excelFilePath = "data.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOut);
            workbook.write(response.getOutputStream());
        } finally {
            workbook.close();
        }
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//		response.setHeader("Content-Disposition", "attachment; filename="+ "data" + ".xlsx");
//        workbook.write(response.getOutputStream());
//        workbook.close();
    }
}
