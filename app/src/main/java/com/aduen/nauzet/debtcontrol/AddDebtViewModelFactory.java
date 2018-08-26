package com.aduen.nauzet.debtcontrol;

//

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;

public class AddDebtViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mDebtId;

    public AddDebtViewModelFactory(AppDatabase database, int debtId){
        mDb = database;
        mDebtId = debtId;
    }

    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new AddDebtViewModel(mDb, mDebtId);
    }
}
