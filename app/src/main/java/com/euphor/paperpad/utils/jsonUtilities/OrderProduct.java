/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class OrderProduct {
	private int price_id;
	private int quantity;
	private List<OrderChild> children;
	/**
	 * @return the price_id
	 */
	public int getPrice_id() {
		return price_id;
	}
	/**
	 * @param price_id the price_id to set
	 */
	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the children
	 */
	public List<OrderChild> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<OrderChild> children) {
		this.children = children;
	}
	/**
	 * @param price_id
	 * @param quantity
	 * @param children
	 */
	public OrderProduct(int price_id, int quantity, List<OrderChild> children) {
		super();
		this.price_id = price_id;
		this.quantity = quantity;
		this.children = children;
	}
	/**
	 * 
	 */
	public OrderProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

}
