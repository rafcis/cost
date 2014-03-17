package test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import core.Calculate;
import core.Cost;

public class CalculateTest {

	@org.junit.Test
	public void testCalculateValue() {
		List<Cost> inData = new LinkedList<Cost>();
		BigDecimal price = new BigDecimal("99.30");
		String category = "category1";

		Cost c = new Cost("description", category, price, "PLN");
		Calculate calc = new Calculate(inData);
		BigDecimal result = calc.calculateValue(c);
		assertEquals(result, price);

		BigDecimal price2 = new BigDecimal("0.70");
		calc.getOutputData().put(category, price2);
		BigDecimal result2 = calc.calculateValue(c);
		assertEquals(result2, price.add(price2));
	}

	@org.junit.Test
	public void testSumByCategory() {
		List<Cost> inData = new LinkedList<Cost>();
		inData.add(new Cost("description", "category1",
				new BigDecimal("99.30"), "PLN"));
		inData.add(new Cost("description", "category2",
				new BigDecimal("22.00"), "PLN"));
		Calculate calc = new Calculate(inData);
		calc.sumByCategory();
		assertEquals(calc.getOutputData().size(), 2);

		inData.add(new Cost("description", "category2",
				new BigDecimal("15.99"), "PLN"));
		calc.setInputData(inData);
		calc.sumByCategory();
		assertEquals(calc.getOutputData().size(), 2);
	}

}
