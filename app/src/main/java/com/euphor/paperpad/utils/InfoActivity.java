/**
 * 
 */
package com.euphor.paperpad.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**store here important information on current activity and previous one
 * @author euphordev02
 *
 */
public class InfoActivity implements Parcelable {


	private int idCurrentTab;
	private int idPreviousTab;
	private String type;
	
	
	
	/**
	 * @param idCurrentTab
	 * @param idPreviousTab
	 * @param type
	 */
	public InfoActivity(int idCurrentTab, int idPreviousTab, String type) {
		super();
		this.idCurrentTab = idCurrentTab;
		this.idPreviousTab = idPreviousTab;
		this.type = type;
	}
	
	/**
	 * 
	 */
	public InfoActivity() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	public InfoActivity(Parcel parcel) {
		Bundle data = parcel.readBundle();
		idCurrentTab = data.getInt("idCurrentTab");
		idPreviousTab = data.getInt("idPreviousTab");
		type = data.getString("type");
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle data = new Bundle();
		data.putInt("idCurrentTab", idCurrentTab);
		data.putInt("idPreviousTab", idPreviousTab);
		data.putString("type", type);
		dest.writeBundle(data);

	}
	public static final Parcelable.Creator<InfoActivity> CREATOR = new Parcelable.Creator<InfoActivity>() {
		public InfoActivity createFromParcel(Parcel data) {
			return new InfoActivity(data);
		}
		public InfoActivity[] newArray(int size) {
			return new InfoActivity[size];
		}
	};

	/**
	 * @return the idCurrentTab
	 */
	public int getIdCurrentTab() {
		return idCurrentTab;
	}

	/**
	 * @param idCurrentTab the idCurrentTab to set
	 */
	public void setIdCurrentTab(int idCurrentTab) {
		this.idCurrentTab = idCurrentTab;
	}

	/**
	 * @return the idPreviousTab
	 */
	public int getIdPreviousTab() {
		return idPreviousTab;
	}

	/**
	 * @param idPreviousTab the idPreviousTab to set
	 */
	public void setIdPreviousTab(int idPreviousTab) {
		this.idPreviousTab = idPreviousTab;
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
	
	public void switchCurrentPrev() {
		idPreviousTab  = idCurrentTab;
	}

}
