/**
 * 
 */
package com.euphor.paperpad.utils.actionsSummary;

import com.euphor.paperpad.Beans.Tab;

/**
 * @author euphordev02
 *
 */
public class ElementSummary {
	/**
	 *  titre à afficher
	 */
	String title; 
	/**
	 * type de l'element
	 */
	String type;
	
	/**
	 * display type 1 for top category, 2 for category, 3 for sub-category 
	 */
	int display;  
	
	/**
	 * id to use to open the element if gallery it will be id gallery...
	 */
	int id;
	
	/**
	 * the associated bean object with the element
	 */
	Object object;
	
	/**
	 * the tab associated with the section
	 */
	Tab tab;
	
	/**
	 * 
	 */
	public ElementSummary() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the display
	 */
	public int getDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(int display) {
		this.display = display;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	public ElementSummary(String title, String type, int display, int id,
			Object object) {
		super();
		this.title = title;
		this.type = type;
		this.display = display;
		this.id = id;
		this.object = object;
	}
	
	public ElementSummary(String title, String type, int display, int id,
			Object object, Tab tab) {
		super();
		this.title = title;
		this.type = type;
		this.display = display;
		this.id = id;
		this.object = object;
		this.tab = tab;
	}

	/**
	 * @return the tab
	 */
	public Tab getTab() {
		return tab;
	}

	/**
	 * @param tab the tab to set
	 */
	public void setTab(Tab tab) {
		this.tab = tab;
	}


}
