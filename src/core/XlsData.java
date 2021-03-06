package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Process data from Xls file
 * 
 * @author Rafal
 * 
 */
public class XlsData implements Data {

	public FileInputStream file;
	private static final Logger logger = Logger.getLogger(XlsData.class);

	public XlsData(String fileName) throws FileNotFoundException {
		logger.info("File name: " + fileName);
		file = new FileInputStream(new File(fileName));

	}

	/**
	 * Proccess data from XLS file
	 */
	@Override
	public void process() {
		logger.info("Start processing XLS file");
		try {
			List<Cost> list;
			list = parseDate();
			Calculate calculate = new Calculate(list);
			LinkedHashMap<String, BigDecimal> data = calculate.sumByCategory();
			exportData(data);
		} catch (IOException e) {
			logger.error("Process XLS file error: " + e.toString());
		}
		logger.info("End processing XLS file");

	}

	/**
	 * Read data from file
	 * 
	 * @return Cost list
	 * @throws IOException
	 */
	private List<Cost> parseDate() throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook(file);
		HSSFSheet sheet = wb.getSheetAt(0);
		Row row;
		Iterator<Row> rows = sheet.rowIterator();
		List<Cost> costList = new LinkedList<Cost>();
		while (rows.hasNext()) {
			row = (Row) rows.next();
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
	 * @throws IOException
	 */
	private void exportData(LinkedHashMap<String, BigDecimal> inputData)
			throws IOException {
		String fileName = "wynik.xls";
		logger.info("Start export data to file: " + fileName);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Wynik");
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
		logger.info("End export data to XLS");
	}
}
