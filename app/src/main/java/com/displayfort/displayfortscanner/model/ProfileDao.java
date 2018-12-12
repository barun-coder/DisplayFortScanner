package com.displayfort.displayfortscanner.model;

/**
 * Created by pc on 12/12/2018 13:53.
 * DisplayFortScanner
 */
public class ProfileDao {
    public String id;
    public String UniqueID;
    public String Name;


    public ProfileDao(String id, String uniqueID, String name) {
        this.id = id;
        UniqueID = uniqueID;
        Name = name;
    }

    public ProfileDao(int i, String uniqueID) {
        this.id = i + "";
        this.UniqueID = uniqueID;
        this.Name = "NAME " + i;
    }

    @Override
    public String toString() {
        return this.UniqueID + " " + this.Name;
    }
}
