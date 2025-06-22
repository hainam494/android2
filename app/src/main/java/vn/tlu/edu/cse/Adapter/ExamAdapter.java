package vn.tlu.edu.cse.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.tlu.edu.cse.Model.Exam;
import vn.tlu.edu.cse.R;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Exam> examList;
    private OnExamDeleteListener deleteListener;

    public ExamAdapter(List<Exam> examList, OnExamDeleteListener deleteListener) {
        this.examList = examList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_manage, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        holder.tvExamId.setText("Mã đề: " + exam.getId());
        holder.tvExamName.setText(exam.getName());
        holder.tvExamTime.setText("Thời gian: " + exam.getTime() + " phút");
        holder.imgExam.setImageResource(R.drawable.avt); // Hình ảnh mặc định
        holder.itemView.setContentDescription("Bài thi: " + exam.getName());
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(exam.getId());
            }
        });
    }

    pub

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public void updateList(List<Exam> newList) {
        examList = newList;
        notifyDataSetChanged();
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        ImageView imgExam;
        TextView tvExamId;
        TextView tvExamName;
        TextView tvExamTime;
        ImageButton btnDelete;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExam = itemView.findViewById(R.id.imgExam);
            tvExamId = itemView.findViewById(R.id.tvExamId);
            tvExamName = itemView.findViewById(R.id.tvExamName);
            tvExamTime = itemView.findViewById(R.id.tvExamTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnExamDeleteListener {
        void onDelete(int examId);
    }
}