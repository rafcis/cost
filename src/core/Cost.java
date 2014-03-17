package core;

import java.math.BigDecimal;

/**
 * Helper Class
 * 
 * @author Rafal
 *
 */
public class Cost {

	/**
	 * Description of cost
	 */
	private String description;
	
	/**
	 * Cost category
	 */
	private String category;
	
	/**
	 * Amount amount
	 */
	private BigDecimal value;
	
	/**
	 * Currency of cost
	 */
	private String currency;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Cost() {

	}

	public Cost(String description, String category, BigDecimal value,
			String currency) {
		this.category = category;
		this.value = value;
		this.currency = currency;
		this.description = description;
	}

	public String toString() {
		return "[" + category + "] : " + value;
	}

}
