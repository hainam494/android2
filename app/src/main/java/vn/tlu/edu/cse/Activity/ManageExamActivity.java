package vn.tlu.edu.cse.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.tlu.edu.cse.Adapter.ExamAdapter;
import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.Model.Exam;
import vn.tlu.edu.cse.R;

public class ManageExamActivity extends AppCompatActivity {
    private EditText examNameEditText, questionCountEditText, timeEditText;
    private Button createExamButton, cancelButton;
    private RecyclerView examRecyclerView;
    private TextView emptyListTextView;
    private ExamAdapter examAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exam); // gán layout

        // ánh xạ
        examNameEditText = findViewById(R.id.examNameEditText);
        questionCountEditText = findViewById(R.id.questionCountEditText);
        timeEditText = findViewById(R.id.timeEditText);
        createExamButton = findViewById(R.id.createExamButton);
        cancelButton = findViewById(R.id.cancelButton);
        examRecyclerView = findViewById(R.id.examRecyclerView);
        emptyListTextView = findViewById(R.id.emptyListTextView);
        db = new DatabaseHelper(this);

        examRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadExams();

        createExamButton.setOnClickListener(v -> {
            String examName = examNameEditText.getText().toString().trim();
            String questionCountStr = questionCountEditText.getText().toString().trim();
            String timeStr = timeEditText.getText().toString().trim();

            if (examName.isEmpty() || questionCountStr.isEmpty() || timeStr.isEmpty()) {
                Toast.makeText(ManageExamActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // chuyển questionCount, timeStr thành int
                    int questionCount = Integer.parseInt(questionCountStr);
                    int time = Integer.parseInt(timeStr);
                    // gọi db.createExam()
                    boolean success = db.createExam(examName, questionCount, time);
                    // nếu thành công: xoá trắng các ô: resetFields(), tải lại ds: loadExams()
                    if (success) {
                        Toast.makeText(ManageExamActivity.this, "Tạo đề thi thành công", Toast.LENGTH_SHORT).show();
                        resetFields();
                        loadExams();
                    } else {
                        Toast.makeText(ManageExamActivity.this, "Không đủ câu hỏi hoặc lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(ManageExamActivity.this, "Số lượng câu và thời gian phải là số", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(v -> {
            resetFields();
            Toast.makeText(ManageExamActivity.this, "Đã hủy, nhập lại thông tin", Toast.LENGTH_SHORT).show();
        });
    }

    private void resetFields() {
        examNameEditText.setText("");
        questionCountEditText.setText("");
        timeEditText.setText("");
    }

    private void loadExams() {
        List<Exam> examList = db.getAllExams(); // lấy ds từ db.getAllExam()
        // nếu trống: ẩn RecyclerView, hênjn thông báo
        // neeus có: hiện recycerView, dùng ExamAdapter để hiện thị
        if (examList.isEmpty()) {
            examRecyclerView.setVisibility(View.GONE);
            emptyListTextView.setVisibility(View.VISIBLE);
            examNameEditText.setVisibility(View.VISIBLE);
            questionCountEditText.setVisibility(View.VISIBLE);
            timeEditText.setVisibility(View.VISIBLE);
            createExamButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        } else {
            examRecyclerView.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.GONE);
            if (examAdapter == null) {
                examAdapter = new ExamAdapter(examList, examId -> {
                    db.deleteExam(examId);
                    Toast.makeText(ManageExamActivity.this, "Xóa đề thi thành công", Toast.LENGTH_SHORT).show();
                    ManageExamActivity.this.loadExams();
                });
            } else {
                examAdapter.updateList(examList);
            }
            examRecyclerView.setAdapter(examAdapter);
        }
    }
}