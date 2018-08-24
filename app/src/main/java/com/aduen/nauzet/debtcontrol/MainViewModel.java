package com.aduen.nauzet.debtcontrol;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<DebtEntry>> debts;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getsInstance(this.getApplication());
        debts = db.debtDao().loadAllDebts();

    }



    public LiveData<List<DebtEntry>> getDebts() {
        return debts;
    }
}
