package com.aduen.nauzet.debtcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;

import java.util.Date;

// TODO Create a new Debt
// TODO show those new debts in main activity
public class AddDebtActivity extends AppCompatActivity {

   private EditText debtNameEditText, debtUserEditText, debtDescriptionEditText, debtQuantityEditText;
   private AppDatabase mDb;
   private Button addDebtButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt);
        mDb = AppDatabase.getsInstance(getApplicationContext());
        initValues();
    }

    private void initValues() {
        debtNameEditText = findViewById(R.id.et_debt_name);
        debtUserEditText = findViewById(R.id.et_debt_user);
        debtDescriptionEditText = findViewById(R.id.et_debt_description);
        debtQuantityEditText = findViewById(R.id.et_debt_quantity);
        addDebtButton = findViewById(R.id.b_add_debt);
        addDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    public void onSaveButtonClicked() {
        String name = debtNameEditText.getText().toString();
        String user = debtUserEditText.getText().toString();
        String description = debtDescriptionEditText.getText().toString();
        int quantity = Integer.valueOf(debtQuantityEditText.getText().toString());
        final DebtEntry debtEntry = new DebtEntry(name, user, description, new Date(), quantity);


        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                mDb.debtDao().insertDebt(debtEntry);

                //TODO UPDATE
                /*
                if (mTaskId == DEFAULT_TASK_ID){
                    mDb.taskDao().insertTask(taskEntry);
                }else{
                    taskEntry.setId(mTaskId);
                    mDb.taskDao().updateTask(taskEntry);
                }*/

                finish();
            }
        });
    }





}
