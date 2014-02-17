package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class CsvData extends Data {

	public CsvData(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public FileInputStream file = null;

	@Override
	public List<Cost> parseDate() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(file,
				"UTF8"));
		String line = "";
		String cvsSplitBy = ",";
		List<Cost> costList = new ArrayList<Cost>();
		while ((line = br.readLine()) != null) {
			String[] row = line.split(cvsSplitBy);
			Cost costObject = new Cost();
			costObject.setDescription(row[1]);
			costObject.setCategory(row[2]);
			costObject.setValue(new BigDecimal(row[3]));
			costObject.setCurrency(row[4]);
			costList.add(costObject);
		}
		file.close();
		return costList;

	}

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

	public void exportData(HashMap<String, BigDecimal> inputData) {
		String csv = "C:\\java\\output2.csv";
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(csv));
			List<String[]> data = new ArrayList<String[]>();
			for (String key : inputData.keySet()) {
				data.add(new String[] { key, inputData.get(key).toString() });
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void setFile(String fileName) throws FileNotFoundException {
//		file = new FileInputStream(fileName);
//	}

}
