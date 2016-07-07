package com.euphor.paperpad.Beans;


import com.euphor.paperpad.utils.Increment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ApplicationBean extends RealmObject {


    @PrimaryKey
    private int id;

    private Application application;

    public int getId() {
        return id;
    }



    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {

      id= Increment.Primary_AppBean(application);
        this.application = application;
    }

    public ApplicationBean() {
    }
    public void setId(int id) {
        this.id = id;
    }

    public ApplicationBean(Application application) {
        this.application = application;
    }
}
