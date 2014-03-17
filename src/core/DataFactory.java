package core;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

abstract class DataFactory {

	private static final Logger logger = Logger.getLogger(DataFactory.class);
	
	public static Data getFactory(String sourceType, String filename)
			throws FileNotFoundException {
		Data data = null;
		logger.debug("File type: " + sourceType);
		switch (sourceType) {
		case "csv":
			data = new CsvData(filename);
			break;
		case "xlsx":
			data = new XlsxData(filename);
			break;
		case "xls":
			data = new XlsData(filename);
			break;
		case "sqlite":
			data = new SqlliteData(filename);
			break;
		default:
			logger.error("Incorrect file type: " + sourceType);
			System.exit(0);
		}
		return data;
	}

}
