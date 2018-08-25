package com.aduen.nauzet.debtcontrol.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DebtDao {

    @Query("SELECT * from debt ORDER BY state ASC, quantity DESC")
    LiveData<List<DebtEntry>> loadAllDebts();

    @Insert
    void insertDebt(DebtEntry debtEntry);

    @Delete
    void deleteDebt(DebtEntry debtEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDebt(DebtEntry debtEntry);

    @Query("SELECT * FROM debt WHERE id = :id")
    LiveData<DebtEntry> loadDebtById(int id);

}
