package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

abstract class Data {

	public FileInputStream file;

	public Data(String fileName) throws FileNotFoundException {
		file = new FileInputStream(new File(fileName));
	}

//	public abstract void setFile(String sourceName)
//			throws FileNotFoundException;

	public abstract List<Cost> parseDate() throws IOException;

	public abstract HashMap<String, BigDecimal> sumByCategory(
			List<Cost> inputData);

	public abstract void exportData(HashMap<String, BigDecimal> inputData);
}
