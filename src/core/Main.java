package core;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main {

	private String fileType, fileName;
	private static Scanner scan;
	private static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Start application...");
		BasicConfigurator.configure();
		Main obj = new Main();
		obj.run();

	}

	public void run() {

		String again;
		boolean yn = true;

		while (yn) {
			try {
				parseInputString();
				System.out.println("FileType: " + fileType + " FileName: "
						+ fileName);
				Data dataFactory;
				dataFactory = DataFactory.getFactory(fileType, fileName);
				dataFactory.process();
				System.out.println("Again (y/n):");
				again = scan.nextLine();
				if (again.equals("y")) {
					yn = true;
				} else {
					yn = false;
				}
			} catch (Exception e) {
				logger.error("Processing data error: " + e.toString());
			}
		}
		System.exit(0);
	}

	private void parseInputString() throws Exception {
		String mathPattern = "(^[A-Za-z]{3,6})([:]{1})([A-Za-z0-9_]+)([\\.]{1})([a-z]{2,4})$";

		System.out.println("\nInsert type and filename (\"type:filename\")\n");
		System.out.println("\nExample:\n\tcsv:koszt.csv\n\texcel:koszt.xls\n\texcel:koszt.xlsx\n\tsqlite:koszty.db\n");
		scan = new Scanner(System.in);

		String inputString = scan.nextLine();
		logger.debug("Input string: " + inputString);
		Pattern p = Pattern.compile(mathPattern);
		Matcher m = p.matcher(inputString);
		if (m.find()) {
			logger.debug("Input string is correct");
			fileType = m.group(1);
			if (fileType.equals("excel")) {
				fileType = m.group(5);
			}
			fileName = m.group(3) + "." + m.group(5);
			logger.info("FileType: " + fileType + ", FileName: " + fileName);
		} else {
			throw new Exception("Incorrect input string");
		}
	}
}
