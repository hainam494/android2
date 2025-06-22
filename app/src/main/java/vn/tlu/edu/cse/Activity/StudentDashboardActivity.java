package vn.tlu.edu.cse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.R;

//public class StudentDashboardActivity extends AppCompatActivity {
//    private Button takeExamButton, editProfileButton, logoutButton;
//    private String currentEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_dashboard);
//
//        takeExamButton = findViewById(R.id.takeExamButton);
//        editProfileButton = findViewById(R.id.editProfileButton);
//        logoutButton = findViewById(R.id.logoutButton);
//        currentEmail = getIntent().getStringExtra("CURRENT_EMAIL");
//
//        takeExamButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(StudentDashboardActivity.this, ExamListActivity.class));
//            }
//        });
//
//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StudentDashboardActivity.this, EditProfileActivity.class);
//                intent.putExtra("CURRENT_EMAIL", currentEmail);
//                startActivity(intent);
//            }
//        });
//
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(StudentDashboardActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
//    }
//}

public class StudentDashboardActivity extends AppCompatActivity {
    private Button takeExamButton, editProfileButton, logoutButton;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        takeExamButton = findViewById(R.id.takeExamButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        takeExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, ExamListActivity.class);
                intent.putExtra("STUDENT_ID", studentId);
                intent.putExtra("EXAM_ID", 1); // Giả định examId là 1
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, EditProfileActivity.class);
                intent.putExtra("CURRENT_EMAIL", getIntent().getStringExtra("CURRENT_EMAIL"));
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}