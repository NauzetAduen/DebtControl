package com.aduen.nauzet.debtcontrol;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aduen.nauzet.debtcontrol.database.DebtEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    final private ItemClickListener mItemClickListener;
    private List<DebtEntry> mDebtEntries;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public DebtAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.debt_layout, parent, false);

        return new DebtViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(DebtViewHolder holder, int position) {
        // Determine the values of the wanted data
        DebtEntry debtEntry = mDebtEntries.get(position);
        String name = debtEntry.getDebtName();
        String user = debtEntry.getDebtUser();
        int quantity = debtEntry.getQuantity();
        String state = debtEntry.getState();

        //Set values
        holder.debtUser.setText(user);
        holder.debtName.setText(name);
        holder.debtQuantity.setText(String.valueOf(quantity));

        //TODO CONTROL STATE
        // Programmatically set the text and color for the priority TextView
        /*String priorityString = "" + priority; // converts int to String
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        // Get the appropriate background color based on the priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);*/
    }

    @Override
    public int getItemCount() {
        if (mDebtEntries == null) {
            return 0;
        }
        return mDebtEntries.size();
    }

    public List<DebtEntry> getDebts() {
        return mDebtEntries;
    }

    public void setDebts(List<DebtEntry> debtEntries) {
        mDebtEntries = debtEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class DebtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView debtName;
        TextView debtUser;
        TextView debtQuantity;

        public DebtViewHolder(View itemView) {
            super(itemView);
            debtName = itemView.findViewById(R.id.tv_debt_name_onList);
            debtUser = itemView.findViewById(R.id.tv_debt_user_onList);
            debtQuantity = itemView.findViewById(R.id.tv_quantity_debt_onList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mDebtEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }


    }
}
