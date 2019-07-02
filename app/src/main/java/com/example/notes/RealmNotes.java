package com.example.notes;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class RealmNotes extends RealmObject {
    @Required
    String myNotes;

    public RealmNotes()
    {

    }


    public RealmNotes(String myNotes) {
        this.myNotes = myNotes;
    }

    public String getMyNotes() {
        return myNotes;
    }

    public void setMyNotes(String myNotes) {
        this.myNotes = myNotes;
    }
}
