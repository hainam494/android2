package vn.tlu.edu.cse.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.tlu.edu.cse.Model.Exam;
import vn.tlu.edu.cse.R;

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ExamViewHolder> {
    private List<Exam> examList;
    private OnExamSelectListener selectListener;

    public ExamListAdapter(List<Exam> examList, OnExamSelectListener selectListener) {
        this.examList = examList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_select, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        holder.examNameTextView.setText(exam.getName());
        holder.startButton.setOnClickListener(v -> selectListener.onSelect(exam.getId()));
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examNameTextView;
        Button startButton;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examNameTextView = itemView.findViewById(R.id.examNameTextView);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }

    interface OnExamSelectListener {
        void onSelect(int examId);
    }
}