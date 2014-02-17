package core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Main obj = new Main();
		obj.run();
	}

	public void run() {

		String csvFile = "kosztorys.xlsx";
		System.out.println("Nazwa pliku: " + csvFile);
		Data df;
		try {
			df = DataFactory.getFactory("xls", csvFile);
			List<Cost> list = df.parseDate();
			HashMap<String, BigDecimal> data = df.sumByCategory(list);
			df.exportData(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// df.initSource(csvFile);
		// HashMap<String, BigDecimal> data = df.sumByCategory(list);
		// df.exportToCsv(data);
	}
}
