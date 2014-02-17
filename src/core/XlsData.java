package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsData extends Data {

	public XlsData(String fileName) throws FileNotFoundException {
		super(fileName);

	}

	@Override
	public List<Cost> parseDate() throws IOException {
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
		 Cost costObject = new Cost();
		 costObject.setDescription(row.getCell(1).getStringCellValue());
		 costObject.setCategory(row.getCell(2).getStringCellValue());
		 BigDecimal val = new BigDecimal(row.getCell(3)
		 .getNumericCellValue());
		 val = val.setScale(2, BigDecimal.ROUND_HALF_UP);
		 costObject.setValue(val);
		 costObject.setCurrency(row.getCell(1).getStringCellValue());
		 costList.add(costObject);
		 }
		 file.close();
		return costList;
	}

	@Override
	public HashMap<String, BigDecimal> sumByCategory(List<Cost> inputData) {
		HashMap<String, BigDecimal> data = new HashMap<String, BigDecimal>();
		String key = null;
		BigDecimal value = null;
		for (int i = 0; i < inputData.size(); i++) {
			key = inputData.get(i).getCategory();
			if (data.containsKey(key)) {
				value = data.get(key).add(inputData.get(i).getValue());
			} else {
				value = inputData.get(i).getValue();
			}
			data.put(key, value);
		}

		return data;
	}

	@Override
	public void exportData(HashMap<String, BigDecimal> inputData) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Sample sheet");
			int rownum = 0;
			for (String key : inputData.keySet()) {
				Row row = sheet.createRow(rownum++);
				Cell category = row.createCell(0);
				category.setCellValue(key);
				Cell value = row.createCell(1);
				value.setCellValue(inputData.get(key).doubleValue());
			}
			FileOutputStream out;
			out = new FileOutputStream(new File("C:\\java\\new.xlsx"));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
