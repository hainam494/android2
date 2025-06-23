package vn.tlu.edu.cse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.Model.Exam;
import vn.tlu.edu.cse.R;

//public class ExamListActivity extends AppCompatActivity {
//    private RecyclerView examListRecyclerView;
//    private ExamListAdapter examAdapter;
//    private DatabaseHelper db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_exam_list);
//
//        examListRecyclerView = findViewById(R.id.examListRecyclerView);
//        db = new DatabaseHelper(this);
//
//        examListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        loadExams();
//
//    }
//
//    private void loadExams() {
//        List<Exam> examList = db.getAllExams();
//        examAdapter = new ExamListAdapter(examList, examId -> {
//            Intent intent = new Intent(ExamListActivity.this, TakeExamActivity.class);
//            intent.putExtra("EXAM_ID", examId);
//            startActivity(intent);
//        });
//        examListRecyclerView.setAdapter(examAdapter);
//    }
//}

public class ExamListActivity extends AppCompatActivity {
    private RecyclerView examRecyclerView;
    private DatabaseHelper db;
    private String studentId;
    private List<Exam> exams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        examRecyclerView = findViewById(R.id.examRecyclerView);
        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        if (studentId == null || studentId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        exams = db.getAllExams();
        examRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        examRecyclerView.setAdapter(new ExamAdapter(exams, studentId));
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
        private List<Exam> examList;
        private String studentId;

        public String getStudentId() {
            return studentId;
        }

        public ExamAdapter(List<Exam> examList, String studentId) {
            this.examList = examList;
            this.studentId = studentId;
        }

        @NonNull
        @Override
        public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
            return new ExamViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
            Exam exam = examList.get(position);
            holder.examNameTextView.setText(exam.getName());

            if (db.hasPermission(studentId, exam.getId())) {
                holder.actionButton.setText("Làm bài");
                holder.actionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(ExamListActivity.this, TakeExamActivity.class);
                    intent.putExtra("STUDENT_ID", studentId);
                    intent.putExtra("EXAM_ID", exam.getId());
                    startActivity(intent);
                });
            } else if (db.hasSentRequest(studentId, exam.getId())) {
                holder.actionButton.setText("Đã gửi yêu cầu");
                holder.actionButton.setEnabled(false);
            } else {
                holder.actionButton.setText("Yêu cầu");
                holder.actionButton.setOnClickListener(v -> {
                    db.addExamRequest(studentId, exam.getId());
                    holder.actionButton.setText("Đã gửi yêu cầu");
                    holder.actionButton.setEnabled(false);
                    Toast.makeText(ExamListActivity.this, "Đã gửi yêu cầu", Toast.LENGTH_SHORT).show();
                });
            }
        }

        @Override
        public int getItemCount() {
            return examList.size();
        }

        class ExamViewHolder extends RecyclerView.ViewHolder {
            TextView examNameTextView;
            Button actionButton;

            public ExamViewHolder(@NonNull View itemView) {
                super(itemView);
                examNameTextView = itemView.findViewById(R.id.examNameTextView);
                actionButton = itemView.findViewById(R.id.actionButton);
            }
        }
    }
}