package com.euphor.paperpad.utils;

public class VerifiedCard {
 
	public String card_token;
	public int application_id;
	public int order_id;
	public String email;
	/**
	 * @return the card_token
	 */
	public String getCard_token() {
		return card_token;
	}
	/**
	 * @param card_token the card_token to set
	 */
	public void setCard_token(String card_token) {
		this.card_token = card_token;
	}
	/**
	 * @return the application_id
	 */
	public int getApplication_id() {
		return application_id;
	}
	/**
	 * @param application_id the application_id to set
	 */
	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}
	/**
	 * @return the order_id
	 */
	public int getOrder_id() {
		return order_id;
	}
	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	public VerifiedCard(String card_token, int application_id, int order_id,
			String email) {
		super();
		this.card_token = card_token;
		this.application_id = application_id;
		this.order_id = order_id;
		this.email = email;
	}
}
