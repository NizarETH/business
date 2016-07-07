/**
 * 
 */
package com.euphor.paperpad.utils.rssParser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class XmlPullFeedParser extends BaseFeedParser {
    public XmlPullFeedParser(String feedUrl) {
        super(feedUrl);
    }
    public RSSChannel parse() {
    	RSSChannel channel = new RSSChannel();
        List<Message> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            parser.setInput(this.getInputStream(), null);
            int eventType = parser.getEventType();
            Message currentMessage = null;
            boolean done = false;
            boolean firstHere = true;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                    	name = parser.getName();
                        messages = new ArrayList<Message>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        /*if (name.equalsIgnoreCase(CHANNEL) && firstHere) {
							eventType = parser.next();
							name = parser.getName(); 
							while (!name.equalsIgnoreCase(ITEM)) {
								if (name.equalsIgnoreCase(LINK)){
									channel.setLink(parser.nextText());
	                            } else if (name.equalsIgnoreCase(DESCRIPTION)){
	                            	channel.setDescription(parser.nextText());
	                            } else if (name.equalsIgnoreCase(TITLE)){
	                            	channel.setTitle(parser.nextText());
	                            } 
								
							}
							firstHere = false;
						}
                        else*/ if (name.equalsIgnoreCase(ITEM)){
                            currentMessage = new Message();
                        } else if (currentMessage != null){
                            if (name.equalsIgnoreCase(LINK)){
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)){
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)){
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)){
                                currentMessage.setTitle(parser.nextText());
                            }    
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) && currentMessage != null){
                            messages.add(currentMessage);
                        } else if (name.equalsIgnoreCase(CHANNEL)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        channel.setItems(messages);
        return channel;
    }
}