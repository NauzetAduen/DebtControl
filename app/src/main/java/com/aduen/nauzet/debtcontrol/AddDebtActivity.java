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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;


public class AddDebtActivity extends AppCompatActivity {


    public static final String EXTRA_DEBT_ID = "extraDebtId";
    public static final String INSTANCE_DEBT_ID = "instanceDebtId";
    private static final int DEFAULT_DEBT_ID = -1;
    private int mDebtId = DEFAULT_DEBT_ID;

    private EditText debtNameEditText, debtUserEditText, debtDescriptionEditText, debtQuantityEditText;
    private TextView alreadyPaidEditText;
    private int qunantityPaid;
    private AppDatabase mDb;
    private Button addDebtButton, payButton;
    private SeekBar mSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        initViewsAndListeners();

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

    private void initViewsAndListeners() {
        payButton = findViewById(R.id.fullPayment_button);
        alreadyPaidEditText = findViewById(R.id.already_paid_tv);
        debtNameEditText = findViewById(R.id.et_debt_name);
        debtUserEditText = findViewById(R.id.et_debt_user);
        debtDescriptionEditText = findViewById(R.id.et_debt_description);
        debtQuantityEditText = findViewById(R.id.et_debt_quantity);
        debtQuantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO FIX THIS SHIT
                if (s.toString().equals("")) return;
                else mSeekBar.setMax(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) return;
                else mSeekBar.setMax(Integer.parseInt(s.toString()));
                ;

            }
        });
        addDebtButton = findViewById(R.id.b_add_debt);
        addDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
        mSeekBar = findViewById(R.id.SB_add_debt);
        mSeekBar.setEnabled(true);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (debtQuantityEditText.getText().equals("")) return;
                alreadyPaidEditText.setText(String.valueOf(mSeekBar.getProgress()));
                if (progress == seekBar.getMax()) payButton.setEnabled(false);
                else payButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(mSeekBar.getMax());
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
        populateSeekBar(debtEntry.getQuantityPaid());
    }

    private void populateSeekBar(int quantityPaid) {
        mSeekBar.setMax(Integer.valueOf(debtQuantityEditText.getText().toString()));
        mSeekBar.setProgress(quantityPaid);
    }

    public void onSaveButtonClicked() {
        //TODO FIX EMPTY
        String name = debtNameEditText.getText().toString();
        String user = debtUserEditText.getText().toString();
        String description = debtDescriptionEditText.getText().toString();
        int quantity = Integer.valueOf(debtQuantityEditText.getText().toString());
        int qPaid = mSeekBar.getProgress();
        if (!areIntCorrect(quantity, qPaid) || !areStringCorrect(name,user,description)){
            Toast.makeText(this,"Empty Fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        final DebtEntry debtEntry = new DebtEntry(name, user, description, quantity, qPaid, calculateState(quantity, qPaid));

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

    private boolean areStringCorrect(String name, String user, String description){
        return !name.isEmpty() && !user.isEmpty() && !description.isEmpty();
    }
    private boolean areIntCorrect( int quantity, int qPaid){
        return  quantity != 0 && qPaid <= quantity;
    }
    private int calculateState(int quantity, int quantityPaid) {
        if (quantity == quantityPaid) return 2;
        if (quantityPaid > 0) return 1;
        return 0;
    }

}
