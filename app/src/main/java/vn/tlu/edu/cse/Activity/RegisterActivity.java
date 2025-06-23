package vn.tlu.edu.cse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, studentIdEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginText;
    private ProgressBar progressBar;
    private DatabaseHelper db;
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{6,}$"; // Ít nhất 6 ký tự, chỉ chữ cái và số

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các thành phần giao diện ánh xạ View
        emailEditText = findViewById(R.id.emailEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progressBar);

        db = new DatabaseHelper(this);

        // Xử lý sự kiện nhấn nút đăng ký
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String studentId = studentIdEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Kiểm tra các trường nhập liệu
            if (email.isEmpty() || studentId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng mật khẩu
            if (!Pattern.matches(PASSWORD_PATTERN, password)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 ký tự, chỉ chứa chữ cái và số", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu trùng khớp
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hiển thị ProgressBar khi đăng ký
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);

            // Đăng ký sinh viên vào database
            boolean success = db.registerStudent(email, password, studentId);
            if (success) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
            }

            // Ẩn ProgressBar sau khi hoàn tất
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);
        });

        // Xử lý sự kiện nhấn "Đăng nhập"
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}