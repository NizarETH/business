/**
 * 
 */
package com.euphor.paperpad.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author euphordev02
 *
 */
public class RestoredField implements Parcelable{
	private int id;
	private String data;
	private String extra;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public RestoredField(int id, String data, String extra) {
		super();
		this.id = id;
		this.data = data;
		this.extra = extra;
	}
	
	public RestoredField(Parcel in) {
		id = in.readInt();
		data = in.readString();
		extra = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(data);		
		dest.writeString(extra);	
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RestoredField createFromParcel(Parcel in) {
            return new RestoredField(in);
        }

        public RestoredField[] newArray(int size) {
            return new RestoredField[size];
        }
    };

}
