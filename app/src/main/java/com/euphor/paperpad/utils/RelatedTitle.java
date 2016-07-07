package com.euphor.paperpad.utils;

public abstract class RelatedTitle {
	boolean isLink = false;
	boolean isPage = false;
	boolean isCategory = false;
	public abstract String getRelatedTitle();
	/**
	 * @return the isPage
	 */
	public boolean isPage() {
		return isPage;
	}
	/**
	 * @param isPage the isPage to set
	 */
	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}
	/**
	 * @return the isLink
	 */
	public boolean isLink() {
		return isLink;
	}
	/**
	 * @param isLink the isLink to set
	 */
	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
	/**
	 * @return the isCategory
	 */
	public boolean isCategory() {
		return isCategory;
	}
	/**
	 * @param isCategory the isCategory to set
	 */
	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}
}
