package com.example.myschedule30;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {

    private List<ScheduleItem> completedTasks;

    public ProgressAdapter(List<ScheduleItem> completedTasks) {
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleItem completedTask = completedTasks.get(position);
        holder.itemNameTextView.setText(completedTask.getItemName());
        holder.itemTypeTextView.setText(completedTask.getItemType());
        holder.itemDateTextView.setText(completedTask.getItemDate());
    }

    @Override
    public int getItemCount() {
        return completedTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemTypeTextView;
        TextView itemDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.completedItemNameTextView);
            itemTypeTextView = itemView.findViewById(R.id.completedItemTypeTextView);
            itemDateTextView = itemView.findViewById(R.id.completedItemDateTextView);
        }
    }
}
