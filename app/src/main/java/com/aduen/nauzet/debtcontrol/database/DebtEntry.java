package com.aduen.nauzet.debtcontrol.database;

// Class used to map object from our database
// We use annotations
//    @Entity determines its an mapped object
//    @Ignore (without id) so SQLite don't use it

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "debt")
public class DebtEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String debtName;
    private String debtUser;
    private String description;
    private Date startDate;
    private int quantity;
    private int state;

    @Ignore
    public DebtEntry(String debtName, String debtUser, String description, Date startDate, int quantity, int state) {
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.startDate = startDate;
        this.quantity = quantity;
        this.state = state;
    }

    public DebtEntry(int id, String debtName, String debtUser, String description, Date startDate, int quantity, int state) {
        this.id = id;
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.startDate = startDate;
        this.quantity = quantity;
        this.state = state;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDebtName() { return debtName; }

    public void setDebtName(String debtName) { this.debtName = debtName; }

    public String getDebtUser() { return debtUser; }

    public void setDebtUser(String debtUser) { this.debtUser = debtUser; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getState() { return state; }

    public void setState(int state) {
        this.state = state;
    }
}
