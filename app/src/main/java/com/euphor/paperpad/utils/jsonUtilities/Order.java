/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class Order {
	
	private int account_id;
	private int application_id;
	private String key;
	private String language;
	private List<OrderField> fields;
	private List<OrderProduct> products;
	private String number;
	private String application_push_token;
	private String application_unique_identifier;
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the fields
	 */
	public List<OrderField> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<OrderField> fields) {
		this.fields = fields;
	}
	/**
	 * @return the products
	 */
	public List<OrderProduct> getProducts() {
		return products;
	}
	/**
	 * @param products the products to set
	 */
	public void setProducts(List<OrderProduct> products) {
		this.products = products;
	}
	/**
	 * @return the account_id
	 */
	public int getAccount_id() {
		return account_id;
	}
	/**
	 * @param account_id the account_id to set
	 */
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
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
	 * @param account_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 */
	public Order(int account_id, int application_id, String key,
			String language, List<OrderField> fields,
			List<OrderProduct> products) {
		super();
		this.account_id = account_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.products = products;
	}
	public Order() {
		super();
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the application_push_token
	 */
	public String getApplication_push_token() {
		return application_push_token;
	}
	/**
	 * @param application_push_token the application_push_token to set
	 */
	public void setApplication_push_token(String application_push_token) {
		this.application_push_token = application_push_token;
	}
	
	
	
	/**
	 * @param account_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 * @param token
	 * @param number
	 */
	public Order(int account_id, int application_id, String key,
			String language, List<OrderField> fields, List<OrderProduct> products, String token, String number, String application_unique_identifier) {
		super();
		this.account_id = account_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.application_push_token = token;
		this.number = number;
		this.products = products;
		this.application_unique_identifier = application_unique_identifier;
	}
	
	/**
	 * @param account_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 * @param token
	 */
	public Order(int account_id, int application_id, String key,
			String language, List<OrderField> fields, List<OrderProduct> products, String token, String application_unique_identifier) {
		super();
		this.account_id = account_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.application_push_token = token;
		this.products = products;
		this.application_unique_identifier = application_unique_identifier;
		
	}
	/**
	 * @return the application_unique_identifier
	 */
	public String getApplication_unique_identifier() {
		return application_unique_identifier;
	}
	/**
	 * @param application_unique_identifier the application_unique_identifier to set
	 */
	public void setApplication_unique_identifier(
			String application_unique_identifier) {
		this.application_unique_identifier = application_unique_identifier;
	}
	

}
