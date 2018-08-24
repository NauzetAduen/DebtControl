package com.aduen.nauzet.debtcontrol;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.aduen.nauzet.debtcontrol.database.AppDatabase;
import com.aduen.nauzet.debtcontrol.database.DebtEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DebtAdapter.ItemClickListener{

    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private DebtAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFloatingActionButton = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recyclerViewDebts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DebtAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<DebtEntry> debts = mAdapter.getmDebtEntries();
                        mDb.debtDao().deleteDebt(debts.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDebtActivity.class));
            }
        });
        mDb = AppDatabase.getsInstance(getApplicationContext());
        setUpViewModel();
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getDebts().observe(this, new Observer<List<DebtEntry>>() {
            @Override
            public void onChanged(@Nullable List<DebtEntry> taskEntries) {
                mAdapter.setDebts(taskEntries);
            }
        });


    }

    @Override
    public void onItemClickListener(int itemId) {
        Toast.makeText(this, "BORRADO", Toast.LENGTH_SHORT).show();
    }
}
