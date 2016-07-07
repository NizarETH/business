/**
 * 
 */
package com.euphor.paperpad.utils.rssParser;

import android.net.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author euphordev02
 *
 */
public class Message implements Comparable<Message>{
    static SimpleDateFormat FORMATTER = 
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.US);
    private String title;
    private URL link;
    private String description;
    private Date date;

      // getters and setters omitted for brevity
    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
        // pad the date if necessary
        while (!date.endsWith("00")){
            date += "0";
        }
        try {
            this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (java.text.ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    @Override
    public String toString() {
		return "";
             // omitted for brevity
    }

    @Override
    public int hashCode() {
		return 0;
            // omitted for brevity
    }
    
    @Override
    public boolean equals(Object obj) {
		return false;
            // omitted for brevity
    }
      // sort by date
    public int compareTo(Message another) {
        if (another == null) return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
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
	 * @return the link
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 *
	public void setLink(URL link) {
		this.link = link;
	}*/

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
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
