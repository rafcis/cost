package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Process data from Xlsx file
 * 
 * @author Rafal
 * 
 */
public class XlsxData implements Data {

	public FileInputStream file;
	private static final Logger logger = Logger.getLogger(XlsxData.class);

	public XlsxData(String fileName) throws FileNotFoundException {
		logger.info("File name: " + fileName);
		file = new FileInputStream(new File(fileName));

	}

	/**
	 * Proccess data from XLSX file
	 */
	@Override
	public void process() {
		logger.info("Start processing XLSX file");
		try {
			List<Cost> list;
			list = parseDate();
			Calculate calculate = new Calculate(list);
			LinkedHashMap<String, BigDecimal> data = calculate.sumByCategory();
			exportData(data);
		} catch (IOException e) {
			logger.error("Process XLSX file error: " + e.toString());
		}
		logger.info("End processing XLSX file");

	}

	/**
	 * Read data from file
	 * 
	 * @return Cost list
	 * @throws IOException
	 */
	private List<Cost> parseDate() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		Iterator<Row> rows = sheet.rowIterator();
		List<Cost> costList = new ArrayList<Cost>();

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			BigDecimal val = new BigDecimal(row.getCell(3)
					.getNumericCellValue());
			val = val.setScale(2, BigDecimal.ROUND_HALF_UP);
			costList.add(new Cost(row.getCell(1).getStringCellValue(), row
					.getCell(2).getStringCellValue(), val, row.getCell(1)
					.getStringCellValue()));
			logger.debug("Added cost, category: "
					+ row.getCell(2).getStringCellValue() + ", value: "
					+ val.toString());
		}
		logger.debug("Imported data from file: " + costList.size());
		file.close();
		return costList;
	}

	/**
	 * Save processed data in file
	 * 
	 * @param inputData
	 */
	private void exportData(LinkedHashMap<String, BigDecimal> inputData)
			throws IOException {
		String fileName = "wynik.xlsx";
		logger.info("Start export data to file: " + fileName);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Wynik");
		int rownum = 0;
		for (String key : inputData.keySet()) {
			Row row = sheet.createRow(rownum++);
			Cell category = row.createCell(0);
			category.setCellValue(key);
			Cell value = row.createCell(1);
			value.setCellValue(inputData.get(key).doubleValue());
		}
		FileOutputStream out;
		out = new FileOutputStream(new File(fileName));
		workbook.write(out);
		out.close();
		logger.info("End export data to XLSX");
	}
}
