package com.euphor.paperpad.Beans;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AgendaGroup extends RealmObject  {
	
     @PrimaryKey
	/* private int agenda_group_id;*/
	
	private int id;
	private Application application;
	private String icon;
	private String title;
	private String color;

	private boolean isSelected;

/*    public int getAgenda_group_id() {
        return agenda_group_id;
    }

    public void setAgenda_group_id(int agenda_group_id) {
        this.agenda_group_id = agenda_group_id;
    }*/

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public AgendaGroup() {
    }

    public AgendaGroup(/*int agenda_group_id,*/ int id, Application application, String icon, String title, String color, boolean isSelected) {
       /* this.agenda_group_id = agenda_group_id;*/
        this.id = id;
        this.application = application;
        this.icon = icon;
        this.title = title;
        this.color = color;
        this.isSelected = isSelected;
    }

   /* @Override
    public RealmList<RelatedCatIds> getRelatedCategories() {
        RealmList<RelatedCatIds> relatedCatIds = new RealmList<RelatedCatIds>();
        return relatedCatIds;
    }*/
}
