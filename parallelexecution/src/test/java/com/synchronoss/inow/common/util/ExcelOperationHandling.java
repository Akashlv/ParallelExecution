/**
 * 
 */
package com.synchronoss.inow.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.synchronoss.inow.common.constants.Constants;

/**
 * @author SLNP Team
 *
 */

public class ExcelOperationHandling {

	Logger logger = Logger.getLogger(ExcelOperationHandling.class.getName());
	Constants constantsPage = new Constants();

	int lastRowNum;

	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet ws;
	XSSFCellStyle cellStyle;
	Font font;
	XSSFCell Cell, Cell2;
	XSSFRow row;
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmms");

	public int excelInitialize(String testDataFileName, String sheetName) throws IOException {

		String testDataFile = constantsPage.gettestDataPath();

		fis = new FileInputStream(testDataFile);
		wb = new XSSFWorkbook(fis);
		ws = wb.getSheet(sheetName);
		lastRowNum = ws.getLastRowNum();
		logger.info("Sheet Name excelInitialize :" + sheetName);

		return lastRowNum;
	}

	public String getStringCellData(int i, int j) throws IOException {

		String stringCellValue = null;

		Cell = ws.getRow(i).getCell(j);

		stringCellValue = Cell.getStringCellValue();

		fis.close();

		return stringCellValue;

	}

	public int getNumericCellData(int i, int j) throws IOException {

		int numericCellValue;

		Cell = ws.getRow(i).getCell(j);

		numericCellValue = (int) Cell.getNumericCellValue();

		fis.close();

		return numericCellValue;

	}

	public Date getDateCellDatae(int i, int j) throws IOException {

		Date dateCellValue;

		Cell = ws.getRow(i).getCell(j);

		dateCellValue = Cell.getDateCellValue();

		fis.close();

		return dateCellValue;

	}

	public int getRowContains(String sTestCaseName, int colNum) throws Exception {

		int i;

		try {

			for (i = 1; i <= ws.getLastRowNum(); i++) {
				logger.info("Cell data :" + getStringCellData(i, 0));
				if (sTestCaseName.contains(getStringCellData(i, 0))) {
					break;
				}
			}
			return i;
		} catch (Exception e) {
			logger.error("Class ExcelUtility | Method getRowContains | Exception desc : " + e.getMessage());
			throw (e);
		}
	}

	public void writeTestResult(String testDataFileName, int i, int j, String result) throws IOException {

		String testDataFile = System.getProperty("user.dir").concat(constantsPage.gettestDataPath());
		row = ws.getRow(i);
		Cell2 = row.getCell(j, Row.RETURN_BLANK_AS_NULL);
		if (Cell2 == null) {
			Cell2 = row.createCell(j);
			Cell2.setCellValue(result);
		} else {

			Cell2.setCellValue(result);
		}

		cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		Cell2.setCellStyle(cellStyle);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		Cell2.setCellStyle(cellStyle);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		Cell2.setCellStyle(cellStyle);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		Cell2.setCellStyle(cellStyle);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		Cell2.setCellStyle(cellStyle);

		font = wb.createFont();
		if (result.equalsIgnoreCase("Pass")) {

			font.setColor(IndexedColors.GREEN.getIndex());

		} else {
			font.setColor(IndexedColors.RED.getIndex());

		}

		cellStyle.setFont(font);

		FileOutputStream fos = new FileOutputStream(testDataFile);
		wb.write(fos);
		fos.flush();
		fos.close();

	}

	public void writeDataInSheet(String fileName, String sheetName, String value, int rowNum, int colNum) {

		try {

			File excel = new File(fileName);
			fis = new FileInputStream(excel);
			wb = new XSSFWorkbook(fis);
			ws = wb.getSheet(sheetName);
			row = ws.getRow(rowNum);

			if (row == null) {
				logger.info("row is " + row);
				row = ws.createRow(rowNum);
			}

			XSSFCell cell = row.createCell(colNum);
			cell.setCellValue(value);

			FileOutputStream os = new FileOutputStream(new File(fileName));
			wb.write(os);

			os.flush();
			os.close();
			fis.close();

		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public void updateCSV(String fileToUpdate, int col, boolean changeDate) throws IOException, Exception {

		DateFormat dddStdDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String toDay = dddStdDateFormat.format(new Date());
		logger.info("Today date:" + toDay);

		Date tempDate = dddStdDateFormat.parse(toDay);
		Date addDays = DateUtils.addDays(tempDate, +5);
		String date = dddStdDateFormat.format(addDays);
		logger.info("from date:" + date);
		File inputFile = new File(fileToUpdate);

		CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
		List<String[]> csvBody = reader.readAll();
		int size = csvBody.size();
		String temp = sdf.format(new Date());
		String replace = temp.substring(0, 11);
		logger.info("replace:" + replace);

		for (int i = 1; i < size; i++) {

			csvBody.get(i)[col] = replace;
			logger.info("i" + i);
			if (changeDate) {

				csvBody.get(i)[24] = date;
			}

			reader.close();

			CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(csvBody);

			writer.flush();
			writer.close();

		}
		logger.info("file write complete");
	}

	public void updateCSV(String fileToUpdate, int col, String value) throws IOException, Exception {

		File inputFile = new File(fileToUpdate);

		// Read existing file
		CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
		List<String[]> csvBody = reader.readAll();
		int a = csvBody.size();

		String replace = value;
		logger.info("replace:" + replace);

		// get CSV row column and replace with by using row and column
		for (int i = 1; i < a; i++) {

			csvBody.get(i)[col] = replace;
			logger.info("i" + i);

			reader.close();

			// Write to CSV file which is open
			CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(csvBody);

			writer.flush();
			writer.close();

		}
		
		logger.info("file write complete");
	}
}
