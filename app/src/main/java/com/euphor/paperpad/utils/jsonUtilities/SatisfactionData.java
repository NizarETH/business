/**
 * 
 */
package com.euphor.paperpad.utils.jsonUtilities;

import java.util.List;

/**
 * @author euphordev02
 *
 */
public class SatisfactionData {
	
	private int section_id;
	private int application_id;
	private String key;
	private String language;
	private int survey_id;
	private List<OrderField> fields;
	private List<Response> responses;
	private String number;
	private String application_push_token;
	
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
	 * @return the account_id
	 */
	public int getSection_id() {
		return section_id;
	}
	/**
	 * @param account_id the account_id to set
	 */
	public void setSection_id(int account_id) {
		this.section_id = account_id;
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
	 * @param section_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 */
	public SatisfactionData(int section_id, int application_id, String key,
			String language, List<OrderField> fields, List<Response> responses) {
		super();
		this.section_id = section_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.responses = responses;
	}
	public SatisfactionData() {
		super();
	}
	/**
	 * @return the survey_id
	 */
	public int getSurvey_id() {
		return survey_id;
	}
	/**
	 * @param survey_id the survey_id to set
	 */
	public void setSurvey_id(int survey_id) {
		this.survey_id = survey_id;
	}
	/**
	 * @return the responses
	 */
	public List<Response> getResponses() {
		return responses;
	}
	/**
	 * @param responses the responses to set
	 */
	public void setResponses(List<Response> responses) {
		this.responses = responses;
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
	 * @param section_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 * @param token
	 * @param number
	 */
	public SatisfactionData(int section_id, int application_id, String key,
			String language, List<OrderField> fields, String token, String number) {
		super();
		this.section_id = section_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.application_push_token = token;
		this.number = number;
	}
	
	/**
	 * @param section_id
	 * @param application_id
	 * @param key
	 * @param language
	 * @param fields
	 * @param products
	 * @param token
	 */
	public SatisfactionData(int section_id, int application_id, String key,
			String language, List<OrderField> fields, String token) {
		super();
		this.section_id = section_id;
		this.application_id = application_id;
		this.key = key;
		this.language = language;
		this.fields = fields;
		this.application_push_token = token;
		
	}
	

}
