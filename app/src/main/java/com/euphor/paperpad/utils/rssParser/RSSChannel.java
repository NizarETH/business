/**
 * 
 */
package com.euphor.paperpad.utils.rssParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class RSSChannel {

	private String title;
    private URL link;
    private String description;
    private List<Message> items;
	
	/**
	 * 
	 */
	public RSSChannel() {
		
	}
	
	 // getters and setters omitted for brevity
    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the link
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * @return the items
	 */
	public List<Message> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Message> items) {
		this.items = items;
	}

}
