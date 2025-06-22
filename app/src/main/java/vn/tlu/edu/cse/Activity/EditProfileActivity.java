package vn.tlu.edu.cse.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.R;

public class EditProfileActivity extends AppCompatActivity {
    private EditText emailEditText, studentIdEditText, passwordEditText;
    private Button updateButton;
    private TextView backText;
    private DatabaseHelper db;
    private String currentEmail; // Email hiện tại của sinh viên

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        emailEditText = findViewById(R.id.emailEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        updateButton = findViewById(R.id.updateButton);
        backText = findViewById(R.id.backText);
        db = new DatabaseHelper(this);

        // Giả định email hiện tại được truyền từ màn hình chính hoặc lưu trong SharedPreferences
        currentEmail = getIntent().getStringExtra("CURRENT_EMAIL");
        loadCurrentProfile();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = emailEditText.getText().toString().trim();
                String newStudentId = studentIdEditText.getText().toString().trim();
                String newPassword = passwordEditText.getText().toString().trim();

                if (newEmail.isEmpty() || newStudentId.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = db.updateStudentProfile(currentEmail, newEmail, newStudentId, newPassword);
                    if (success) {
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        currentEmail = newEmail; // Cập nhật email hiện tại
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại, email có thể đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadCurrentProfile() {
        SQLiteDatabase dbReadable = db.getReadableDatabase();
        Cursor cursor = dbReadable.rawQuery("SELECT " + DatabaseHelper.COLUMN_EMAIL + ", " + DatabaseHelper.COLUMN_STUDENT_ID + ", " + DatabaseHelper.COLUMN_PASSWORD +
                " FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COLUMN_EMAIL + "=?", new String[]{currentEmail});
        if (cursor.moveToFirst()) {
            emailEditText.setText(cursor.getString(0));
            studentIdEditText.setText(cursor.getString(1));
            passwordEditText.setText(cursor.getString(2));
        }
        cursor.close();
        dbReadable.close();
    }
}