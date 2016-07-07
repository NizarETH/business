/**
 * 
 */
package com.euphor.paperpad.Beans;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Home_swipe extends RealmObject {

  @PrimaryKey
	private int id;

    private Parameters_swipe parameters;

	private RealmList<ElementSwipe> elements = new RealmList<ElementSwipe>();

	//private Application application;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parameters_swipe getParameters() {
        return parameters;
    }

    public void setParameters(Parameters_swipe parameters) {
        this.parameters = parameters;
    }

    public RealmList<ElementSwipe> getElements() {
        return elements;
    }

    public void setElements(RealmList<ElementSwipe> elements) {
        this.elements = elements;
    }

/*    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }*/

    public Home_swipe() {
    }

    public Home_swipe(int id, Parameters_swipe parameters, RealmList<ElementSwipe> elements/*, Application application*/) {
        this.id = id;
        this.parameters = parameters;
        this.elements = elements;
        //this.application = application;
    }
}