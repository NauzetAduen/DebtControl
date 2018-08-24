package com.aduen.nauzet.debtcontrol;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;

public class AddDebtViewModel extends ViewModel {

    private LiveData<DebtEntry> debt;

    public AddDebtViewModel (AppDatabase database, int debtId) {
        debt = database.debtDao().loadDebtById(debtId);
    }

    public LiveData<DebtEntry> getDebt() {
        return debt;
    }
}
