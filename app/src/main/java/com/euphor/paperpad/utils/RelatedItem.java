package com.euphor.paperpad.utils;



import java.util.List;

import io.realm.RealmObject;

public interface RelatedItem {
	String getItemTitle();
	String getItemIntro();
	String getItemIllustration();
	String getItemType();
	/*List<RelatedCatIds> getRelatedCategories();*/
	int getItemExtra();
}
