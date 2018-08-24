package com.aduen.nauzet.debtcontrol.database;

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



    private String state;

    @Ignore
    public DebtEntry(String debtName, String debtUser, String description, Date startDate, int quantity) {
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.startDate = startDate;
        this.quantity = quantity;
        state = "not paid";
    }

    public DebtEntry(int id, String debtName, String debtUser, String description, Date startDate, int quantity) {
        this.id = id;
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.startDate = startDate;
        this.quantity = quantity;
        state = "not paid";
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

    public String getState() { return state; }

    public void pay() { state = "paid"; }

    public void setState(String state) {
        this.state = state;
    }
}