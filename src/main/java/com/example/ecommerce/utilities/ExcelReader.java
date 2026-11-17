package com.example.ecommerce.utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ExcelReader {
    private ExcelReader() {}

    public static List<Map<String, String>> readResource(String resourcePath, String sheetName) throws IOException {
        try (InputStream stream = ExcelReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) throw new IOException("Excel test data resource not found: " + resourcePath);
            try (Workbook workbook = WorkbookFactory.create(stream)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);
                Row header = sheet.getRow(0);
                if (header == null) throw new IllegalArgumentException("Header row is missing in sheet: " + sheetName);
                DataFormatter formatter = new DataFormatter();
                List<Map<String, String>> rows = new ArrayList<>();
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;
                    Map<String, String> values = new LinkedHashMap<>();
                    for (int column = 0; column < header.getLastCellNum(); column++) {
                        String name = formatter.formatCellValue(header.getCell(column)).trim();
                        if (!name.isEmpty()) values.put(name, formatter.formatCellValue(row.getCell(column)).trim());
                    }
                    if (values.values().stream().anyMatch(value -> !value.isBlank())) rows.add(values);
                }
                return rows;
            }
        }
    }
}
