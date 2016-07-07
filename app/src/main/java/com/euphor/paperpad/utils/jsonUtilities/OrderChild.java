package com.euphor.paperpad.utils.jsonUtilities;

import io.realm.RealmObject;

public class OrderChild extends RealmObject {

	private int page_id;
	private String price_id;
	private int quantity;
    private int id_price1;

    public OrderChild() {
    }

    public int getId_price1() {
        return id_price1;
    }

    public void setId_price1(int id_price1) {
        this.id_price1 = id_price1;
    }

    public OrderChild(int page_id, String price_id, int quantity, int id_price1) {
        this.page_id = page_id;
        this.price_id = price_id;
        this.quantity = quantity;
        this.id_price1 = id_price1;
    }

    /**
	 * @return the page_id
	 */
	public int getPage_id() {
		return page_id;
	}
	/**
	 * @param page_id the page_id to set
	 */
	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}
	/**
	 * @return the price_id
	 */
	public String getPrice_id() {
		return price_id;
	}
	/**
	 * @param price_id the price_id to set
	 */
	public void setPrice_id(String price_id) {
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
	
	/*public static OrderChild writeCartItem(CartItem cartItem) {
		OrderChild child = new OrderChild();
		if (cartItem.getChild_page() != null) {
			child.setPage_id(cartItem.getChild_page().getId());
		}
		child.setPrice_id(cartItem.getPrice_id());
		child.setQuantity(cartItem.getQuantity());
		return child;
	}*/
	/**
	 * @param page_id
	 * @param price_id
	 * @param quantity
	 */
	public OrderChild(int page_id, String price_id, int quantity) {
		super();
		this.page_id = page_id;
		this.price_id = price_id;
		this.quantity = quantity;
	}
    public OrderChild(int page_id, int id_price1, int quantity) {
        super();
        this.page_id = page_id;
        this.id_price1 = id_price1;
        this.quantity = quantity;
    }

}
