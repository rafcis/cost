package core;

import java.io.FileNotFoundException;

abstract class DataFactory {

	public static Data getFactory(String sourceType, String filename) throws FileNotFoundException {
		Data data = null;
		switch (sourceType) {
		case "csv":
			data = new CsvData(filename);
			break;
		case "xls":
			data = new XlsData(filename);
			break;
		default:
			data = new CsvData(filename);
		}
		return data;
	}

}
