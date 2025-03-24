//package com.stp.utility;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
////public class ExcelService {
////
////	public byte[] generateExcel(List<String[]> data) throws IOException {
////		Workbook workbook = new XSSFWorkbook();
////		Sheet sheet = workbook.createSheet("Data");
////
////		// Create a header row
////		Row headerRow = sheet.createRow(0);
////		String[] header = { "Column 1", "Column 2", "Column 3" };
////		for (int i = 0; i < header.length; i++) {
////			headerRow.createCell(i).setCellValue(header[i]);
////		}
////
////		// Populate the Excel file with data
////		int rowNum = 1;
////		for (String[] rowData : data) {
////			Row row = sheet.createRow(rowNum++);
////			for (int i = 0; i < rowData.length; i++) {
////				row.createCell(i).setCellValue(rowData[i]);
////			}
////		}
////
////		// Write the workbook to a byte array output stream
////		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////		workbook.write(byteArrayOutputStream);
////		workbook.close();
////
////		return byteArrayOutputStream.toByteArray();
////	}
//}
