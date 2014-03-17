package core;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Calculate class
 * 
 * @author Rafal
 * 
 */
public class Calculate {

	private List<Cost> inputData;
	private LinkedHashMap<String, BigDecimal> outputData;

	public LinkedHashMap<String, BigDecimal> getOutputData() {
		return outputData;
	}

	public void setOutputData(LinkedHashMap<String, BigDecimal> outputData) {
		this.outputData = outputData;
	}

	public List<Cost> getInputData() {
		return inputData;
	}

	public void setInputData(List<Cost> inputData) {
		this.inputData = inputData;
	}

	public Calculate(List<Cost> inData) {
		inputData = inData;
		outputData = new LinkedHashMap<String, BigDecimal>();
	}

	/**
	 * Group data by category and sum cost
	 * 
	 * @return Grouped categories LinkedHashMap
	 */
	public LinkedHashMap<String, BigDecimal> sumByCategory() {
		ListIterator<Cost> listIterator = inputData.listIterator();
		BigDecimal value = null;
		String key = null;
		while (listIterator.hasNext()) {
			Cost c = listIterator.next();
			key = c.getCategory();
			value = calculateValue(c);
			outputData.put(key, value);
		}
		return outputData;
	}

	/**
	 * Check if category exist in output data if no add new element if yes adds
	 * next amount to category
	 * 
	 * @param c
	 *            Coast to add
	 * @return BigDecimal
	 */
	public BigDecimal calculateValue(Cost c) {
		BigDecimal value = null;
		String key = null;
		key = c.getCategory();
		if (outputData.containsKey(key)) {
			value = outputData.get(key).add(c.getValue());
		} else {
			value = c.getValue();
		}
		return value;
	}
}
