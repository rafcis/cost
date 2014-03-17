package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Process data from CSV file
 * 
 * @author Rafal
 * 
 */
public class CsvData implements Data {

	public FileInputStream file;
	private static final Logger logger = Logger.getLogger(CsvData.class);

	public CsvData(String fileName) throws FileNotFoundException {
		logger.info("File name: " + fileName);
		file = new FileInputStream(new File(fileName));

	}

	/**
	 * Proccess data from CSV file
	 */
	@Override
	public void process() {
		logger.info("Start processing CSV file");
		try {
			List<Cost> list = parseDate();
			Calculate calculate = new Calculate(list);
			LinkedHashMap<String, BigDecimal> data = calculate.sumByCategory();
			exportData(data);
		} catch (IOException e) {
			logger.error("Process CSV file error: " + e.toString());
		}
		logger.info("End processing CSV file");
	}

	/**
	 * Read CSV file
	 * 
	 * @return cost list from file
	 * @throws IOException
	 */
	private List<Cost> parseDate() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(file,
				"UTF8"));
		String line = "";
		String cvsSplitBy = ",";
		List<Cost> costList = new LinkedList<Cost>();
		while ((line = br.readLine()) != null) {
			String[] row = line.split(cvsSplitBy);
			costList.add(new Cost(row[1], row[2], new BigDecimal(row[3]),
					row[4]));
			logger.debug("Added cost, category: " + row[2] + ", value: "
					+ row[3]);
		}
		logger.debug("Imported data from file: " + costList.size());
		file.close();
		return costList;

	}

	/**
	 * Save data to CSV file
	 * 
	 * @param inputData
	 *            Data to save in file
	 */
	private void exportData(LinkedHashMap<String, BigDecimal> inputData)
			throws IOException {
		String csv = "wynik.csv";
		logger.info("Start export data to file: " + csv);
		CSVWriter writer;
		writer = new CSVWriter(new FileWriter(csv));
		List<String[]> data = new ArrayList<String[]>();
		for (String key : inputData.keySet()) {
			data.add(new String[] { key, inputData.get(key).toString() });
			logger.debug("Added data to export");
		}
		writer.writeAll(data);
		writer.close();
		logger.debug("Exported data to file: " + data.size() + " rows");
		logger.info("End export data to CSV");
	}

}
