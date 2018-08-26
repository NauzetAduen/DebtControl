package com.aduen.nauzet.debtcontrol;

//Class with two purposes
//   Create new debts and update existing debts
//   We check in "onCreate" if the intent has an extraID
//   We try to recover data from SavedInstance in case of activity rotation
//   We check in "onSaveButtonClicked" witch case we are in

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;

import java.util.Date;

public class AddDebtActivity extends AppCompatActivity {


    public static final String EXTRA_DEBT_ID = "extraDebtId";
    public static final String INSTANCE_DEBT_ID = "instanceDebtId";
    private static final int DEFAULT_DEBT_ID = -1;
    private int mDebtId = DEFAULT_DEBT_ID;

    private EditText debtNameEditText, debtUserEditText, debtDescriptionEditText, debtQuantityEditText;
    private AppDatabase mDb;
    private Button addDebtButton;
    private RadioGroup stateRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        initViews();

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DEBT_ID)) {
            mDebtId = savedInstanceState.getInt(INSTANCE_DEBT_ID, DEFAULT_DEBT_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DEBT_ID)) {
            addDebtButton.setText(R.string.update_button);
            if (mDebtId == DEFAULT_DEBT_ID) {

                mDebtId = intent.getIntExtra(EXTRA_DEBT_ID, DEFAULT_DEBT_ID);

                AddDebtViewModelFactory factory = new AddDebtViewModelFactory(mDb, mDebtId);
                final AddDebtViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddDebtViewModel.class);

                viewModel.getDebt().observe(this, new Observer<DebtEntry>() {
                    @Override
                    public void onChanged(@Nullable DebtEntry debtEntry) {
                        viewModel.getDebt().removeObserver(this);
                        populateUI(debtEntry);
                    }
                });
            }
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(INSTANCE_DEBT_ID, mDebtId);
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
        debtNameEditText = findViewById(R.id.et_debt_name);
        debtUserEditText = findViewById(R.id.et_debt_user);
        debtDescriptionEditText = findViewById(R.id.et_debt_description);
        debtQuantityEditText = findViewById(R.id.et_debt_quantity);
        stateRadioGroup = findViewById(R.id.radioButton_group);
        addDebtButton = findViewById(R.id.b_add_debt);
        addDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(DebtEntry debtEntry){
        if (debtEntry == null) return;

        //to Update Debts
        debtNameEditText.setText(debtEntry.getDebtName());
        debtUserEditText.setText(debtEntry.getDebtUser());
        debtDescriptionEditText.setText(debtEntry.getDescription());
        debtQuantityEditText.setText(String.valueOf(debtEntry.getQuantity()));
        setStateInRadio(debtEntry.getState());
    }

    public void onSaveButtonClicked() {
        String name = debtNameEditText.getText().toString();
        String user = debtUserEditText.getText().toString();
        String description = debtDescriptionEditText.getText().toString();
        int quantity = Integer.valueOf(debtQuantityEditText.getText().toString());
        final DebtEntry debtEntry = new DebtEntry(name, user, description, new Date(), quantity, getStatePaid());

        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if( mDebtId == DEFAULT_DEBT_ID){
                    mDb.debtDao().insertDebt(debtEntry);
                }else{
                    debtEntry.setId(mDebtId);
                    mDb.debtDao().updateDebt(debtEntry);
                }
                finish();
            }
        });
    }

    public int getStatePaid(){
        int checkId = ((RadioGroup) findViewById(R.id.radioButton_group)).getCheckedRadioButtonId();
        if (checkId == R.id.radioButton_paid) return 1;
        return 0;
    }

    public void setStateInRadio(int state){
        if (state == 1) ((RadioGroup) findViewById(R.id.radioButton_group)).check(R.id.radioButton_paid);
        if (state == 0) ((RadioGroup) findViewById(R.id.radioButton_group)).check(R.id.radioButton_notpaid);
    }


}
