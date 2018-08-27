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
    private int quantity;
    private int quantityPaid;
    private int state;

    @Ignore
    public DebtEntry(String debtName, String debtUser, String description, int quantity, int quantityPaid, int state) {
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.quantity = quantity;
        this.quantityPaid = quantityPaid;
        this.state = state;
    }

    public DebtEntry(int id, String debtName, String debtUser, String description, int quantity, int quantityPaid, int state) {
        this.id = id;
        this.debtName = debtName;
        this.debtUser = debtUser;
        this.description = description;
        this.quantity = quantity;
        this.quantityPaid = quantityPaid;
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

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getQuantityPaid() { return quantityPaid; }

    public void setQuantityPaid(int quantityPaid) { this.quantityPaid = quantityPaid; }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
