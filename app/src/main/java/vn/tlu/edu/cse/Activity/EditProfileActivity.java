package vn.tlu.edu.cse.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        db = new DatabaseHelper(this);
        currentEmail = getIntent().getStringExtra("CURRENT_EMAIL");

        loadCurrentProfile();

        updateButton.setOnClickListener(v -> handleUpdate());
        backText.setOnClickListener(v -> finish());
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        updateButton = findViewById(R.id.updateButton);
        backText = findViewById(R.id.backText);
    }

    private void handleUpdate() {
        String newEmail = emailEditText.getText().toString().trim();
        String newStudentId = studentIdEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newStudentId) || TextUtils.isEmpty(newPassword)) {
            showToast("Vui lòng điền đầy đủ thông tin");
            return;
        }

        boolean success = db.updateStudentProfile(currentEmail, newEmail, newStudentId, newPassword);
        if (success) {
            showToast("Cập nhật thông tin thành công");
            currentEmail = newEmail;
        } else {
            showToast("Cập nhật thất bại, email có thể đã tồn tại");
        }
    }

    private void loadCurrentProfile() {
        SQLiteDatabase dbReadable = db.getReadableDatabase();
        Cursor cursor = dbReadable.rawQuery(
                "SELECT " + DatabaseHelper.COLUMN_EMAIL + ", " +
                        DatabaseHelper.COLUMN_STUDENT_ID + ", " +
                        DatabaseHelper.COLUMN_PASSWORD +
                        " FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.COLUMN_EMAIL + "=?",
                new String[]{currentEmail});

        if (cursor.moveToFirst()) {
            emailEditText.setText(cursor.getString(0));
            studentIdEditText.setText(cursor.getString(1));
            passwordEditText.setText(cursor.getString(2));
        }
        public
        cursor.close();
        dbReadable.close();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
