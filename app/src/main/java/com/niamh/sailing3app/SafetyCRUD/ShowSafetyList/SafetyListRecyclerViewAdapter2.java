package com.niamh.sailing3app.SafetyCRUD.ShowSafetyList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.niamh.sailing3app.Database.DatabaseQueryClass;
import com.niamh.sailing3app.R;
import com.niamh.sailing3app.SafetyCRUD.CreateSafety.Safety;
import com.niamh.sailing3app.SafetyCRUD.UpdateSafety.SafetyUpdateDialogFragment2;
import com.niamh.sailing3app.SafetyCRUD.UpdateSafety.SafetyUpdateListener;
import com.niamh.sailing3app.Utils.Config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SafetyListRecyclerViewAdapter2 extends RecyclerView.Adapter<CustomViewHolder>{


    //declaring and assigning values
    private final Context context;
    private final List<Safety> safetyList2;
    private List<Safety> safetyFilteredData2;

    String available = "Available:";
    String fault = "Fault:";


    private boolean expanded = true;

    public SafetyListRecyclerViewAdapter2(Context context, List<Safety> safetyList) {
        this.context = context;
        this.safetyList2 = safetyList;
        this.safetyFilteredData2 =safetyList;

    }

    @Override
    public @NotNull CustomViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_safety, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Safety safety = safetyFilteredData2.get(position);

        holder.typeTextView.setText(safety.getType());
        holder.availableTextView.setText(safety.getAvailable());
        holder.faultTextView.setText(safety.getFault());
        holder.itemImageView.setImageBitmap(safety.getImage());
        holder.availableTitleTextView.setText(available);
        holder.faultTitleTextView.setText(fault);

        //clicking the holder will bring you to the update fragment used to be image of the pencil
        holder.safetyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyUpdateDialogFragment2 safetyUpdateDialogFragment2 = SafetyUpdateDialogFragment2.newInstance(safety.getId(), itemPosition, new SafetyUpdateListener() {
                    @Override
                    public void onSafetyInfoUpdate(Safety safety, int position) {
                        safetyList2.set(position, safety);
                        notifyDataSetChanged();
                    }
                });
                safetyUpdateDialogFragment2.show(((SafetyListActivity2) context).getSupportFragmentManager(), Config.UPDATE_SAFETY);
            }
        });



    }

    //count how many
    @Override
    public int getItemCount() {
        return safetyFilteredData2.size();
    }

    //RecyclerView(AndroidX 2020): Part 5 | Search bar with RecyclerView | Android Studio Tutorial https://www.youtube.com/watch?v=ILYfvCrpsj8
    //Getting a filter setup for search bar
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    safetyFilteredData2 = safetyList2;
                } else {
                    List<Safety> lstFiltered = new ArrayList<>();
                    for (Safety row : safetyFilteredData2) {
                        if (row.getType().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }
                    safetyFilteredData2 = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = safetyFilteredData2;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                safetyFilteredData2 = (List<Safety>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
