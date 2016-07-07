package com.euphor.paperpad.Beans.roomServices;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RoomService extends RealmObject {


    @PrimaryKey
    private int id;

private RealmList<Request> requests = new RealmList<Request>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<Request> getRequests() {
        return requests;
    }

    public void setRequests(RealmList<Request> requests) {
        this.requests = requests;
    }

    public RoomService() {
    }

    public RoomService(int id, RealmList<Request> requests) {
        this.id = id;
        this.requests = requests;
    }
}