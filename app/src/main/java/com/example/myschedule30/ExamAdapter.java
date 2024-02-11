package com.example.myschedule30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private List<ExamItem> examList;

    public ExamAdapter(List<ExamItem> examList) {
        this.examList = examList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_exam_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamItem examItem = examList.get(position);
        holder.examNameTextView.setText(examItem.getExamName());
        holder.examGradeTextView.setText(String.valueOf(examItem.getExamGrade()));
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView examNameTextView;
        TextView examGradeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            examNameTextView = itemView.findViewById(R.id.examNameTextView);
            examGradeTextView = itemView.findViewById(R.id.examGradeTextView);
        }
    }
}
