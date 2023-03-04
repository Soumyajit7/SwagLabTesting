package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.Status;

public class ExcelDataReaderWriter {

	public static LinkedHashSet<LinkedHashMap<String, String>> readDataFromExcel(String filePath, String sheetName)
			throws IOException {
		LinkedHashSet<LinkedHashMap<String, String>> dataSet = new LinkedHashSet<LinkedHashMap<String, String>>();
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheet(sheetName);

			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			short firstCellNum = sheet.getRow(firstRowNum).getFirstCellNum();
			short lastCellNum = sheet.getRow(firstRowNum).getLastCellNum();

			ArrayList<String> list = new ArrayList<String>();
			for (int j = firstCellNum; j < lastCellNum; ++j) {
				Cell cellValue = sheet.getRow(0).getCell(j);
				String value = "";
				switch (cellValue.getCellType()) {
				case STRING:
					value = cellValue.getStringCellValue();
					break;
				case NUMERIC:
					value = String.valueOf(cellValue.getNumericCellValue());
					break;
				default:
					value = "";
					break;
				}
				list.add(value);
			}

			for (int i = firstRowNum + 1; i <= lastRowNum; ++i) {
				LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
				for (int j = firstCellNum; j < lastCellNum; ++j) {
					Cell cellValue = sheet.getRow(i).getCell(j);
//					System.out.println("++"+cellValue+"++");
					if (cellValue != null) {
						String value = "";
						switch (cellValue.getCellType()) {
						case STRING:
							value = cellValue.getStringCellValue();
							break;
						case NUMERIC:
							value = String.valueOf(cellValue.getNumericCellValue());
							break;
						default:
							value = "";
							break;
						}
						dataMap.put(list.get(j), value);
					}
				}
				dataSet.add(dataMap);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return dataSet;
	}

//	"TestCase", "Status", testCase, status.toString(), filepath, "Regreesion"
	public static void excelDataInWriteBySearch(String columnBySearch, String writableColumn, String searchByData,
			String writableData, String filePath, String sheetName) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);
		int columnBySearchIndex = -1, writableColumnIndex = -1, rowToWrite = -1;

		// Get the column index to search for
		Row headerRow = sheet.getRow(0);
		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
			if (headerRow.getCell(i).getStringCellValue().equalsIgnoreCase(columnBySearch)) {
				columnBySearchIndex = i;
				break;
			}
		}

		// Get the column index to write to
		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
			if (headerRow.getCell(i).getStringCellValue().equalsIgnoreCase(writableColumn)) {
				writableColumnIndex = i;
				break;
			}
		}

		// Find the row to write to
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(columnBySearchIndex).getStringCellValue().equalsIgnoreCase(searchByData)) {
				rowToWrite = i;
				break;
			}
		}

		// Write data to cell
		if (rowToWrite != -1 && writableColumnIndex != -1) {
			Row row = sheet.getRow(rowToWrite);
			if (row == null) {
				row = sheet.createRow(rowToWrite);
			}
			Cell cell = row.getCell(writableColumnIndex);
			if (cell == null) {
				cell = row.createCell(writableColumnIndex);
			}
			cell.setCellValue(writableData);
		}

		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

//	public static void main(String[] args) throws IOException {
//		String filePath = System.getProperty("user.dir") + "/src/main/resources/file.xlsx";
//		String sheetName = "Sheet1";
//		LinkedHashSet<LinkedHashMap<String, String>> dataSet = readDataFromExcel(filePath, sheetName);
//		System.out.println(dataSet);
//	}
}
