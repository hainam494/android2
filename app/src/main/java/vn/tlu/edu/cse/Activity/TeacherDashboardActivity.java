package vn.tlu.edu.cse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.R;

public class TeacherDashboardActivity extends AppCompatActivity {
    private Button addQuestionButton, manageExamButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        addQuestionButton = findViewById(R.id.addQuestionButton);
        manageExamButton = findViewById(R.id.manageExamButton);
        logoutButton = findViewById(R.id.logoutButton);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, AddQuestionActivity.class));
            }
        });

        manageExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, ManageExamActivity.class));
            }
        });
        Button manageRequestsButton = findViewById(R.id.manageRequestsButton);
        manageRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageRequestsActivity.class));
        });
        public change

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}