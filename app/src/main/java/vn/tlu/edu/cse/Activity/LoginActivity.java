package vn.tlu.edu.cse.Activity;

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText emailEditText, passwordEditText;
//    private Button loginButton;
//    private TextView forgotPasswordText, registerText;
//    private DatabaseHelper db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        loginButton = findViewById(R.id.loginButton);
//        forgotPasswordText = findViewById(R.id.forgotPasswordText);
//        registerText = findViewById(R.id.registerText);
//        db = new DatabaseHelper(this);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString().trim();
//                String password = passwordEditText.getText().toString().trim();
//                String role = db.login(email, password);
//                if (role != null) {
//                    if (role.equals("student")) {
//                        Toast.makeText(LoginActivity.this, "Đăng nhập sinh viên thành công", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
//                        intent.putExtra("CURRENT_EMAIL", email);
//                        startActivity(intent);
//                        finish();
//                    } else if (role.equals("teacher")) {
//                        Toast.makeText(LoginActivity.this, "Đăng nhập giảng viên thành công", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity.this, TeacherDashboardActivity.class));
//                        finish();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        registerText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            }
//        });
//
//        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//            }
//        });
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registerText = findViewById(R.id.registerText);
        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String role = db.login(email, password);
                if (role != null) {
                    if (role.equals("student")) {
                        String studentId = db.getStudentIdByEmail(email);
                        if (studentId != null) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập sinh viên thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
                            intent.putExtra("CURRENT_EMAIL", email);
                            intent.putExtra("STUDENT_ID", studentId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Không tìm thấy mã sinh viên", Toast.LENGTH_SHORT).show();
                        }
                    } else if (role.equals("teacher")) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập giảng viên thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, TeacherDashboardActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
}