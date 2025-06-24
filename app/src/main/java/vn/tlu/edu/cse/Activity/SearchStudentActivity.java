package vn.tlu.edu.cse.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.R;
import vn.tlu.edu.cse.Model.User;

public class SearchStudentActivity extends AppCompatActivity {
    private EditText studentIdEditText;
    private Button searchButton, addToExamButton;
    private TextView studentInfoTextView;
    private DatabaseHelper db;
    private User student;
    private int examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);

        studentIdEditText = findViewById(R.id.studentIdEditText);
        searchButton = findViewById(R.id.searchButton);
        addToExamButton = findViewById(R.id.addToExamButton);
        studentInfoTextView = findViewById(R.id.studentInfoTextView);
        db = new DatabaseHelper(this);

        examId = getIntent().getIntExtra("EXAM_ID", -1);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = studentIdEditText.getText().toString().trim();
                student = db.getStudentById(studentId);
                if (student != null) {
                    studentInfoTextView.setText("Sinh viên: " + student.getEmail());
                    addToExamButton.setVisibility(View.VISIBLE);
                } else {
                    studentInfoTextView.setText("Không tìm thấy sinh viên");
                    addToExamButton.setVisibility(View.GONE);
                }
            }
        });

        addToExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addStudentToExam(student.getStudentId(), examId);
                finish();
            }
        });
    }
}