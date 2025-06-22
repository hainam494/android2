package vn.tlu.edu.cse.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.R;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText questionTextEditText, optionAEditText, optionBEditText, optionCEditText, optionDEditText, correctAnswerEditText;
    private Button addQuestionButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        questionTextEditText = findViewById(R.id.questionTextEditText);
        optionAEditText = findViewById(R.id.optionAEditText);
        optionBEditText = findViewById(R.id.optionBEditText);
        optionCEditText = findViewById(R.id.optionCEditText);
        optionDEditText = findViewById(R.id.optionDEditText);
        correctAnswerEditText = findViewById(R.id.correctAnswerEditText);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        db = new DatabaseHelper(this);

        addQuestionButton.setOnClickListener(v -> {
            String questionText = questionTextEditText.getText().toString().trim();
            String optionA = optionAEditText.getText().toString().trim();
            String optionB = optionBEditText.getText().toString().trim();
            String optionC = optionCEditText.getText().toString().trim();
            String optionD = optionDEditText.getText().toString().trim();
            String correctAnswer = correctAnswerEditText.getText().toString().trim().toUpperCase();

            if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || correctAnswer.isEmpty()) {
                Toast.makeText(AddQuestionActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C") && !correctAnswer.equals("D")) {
                Toast.makeText(AddQuestionActivity.this, "Đáp án đúng phải là A, B, C hoặc D", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.addQuestion(questionText, optionA, optionB, optionC, optionD, correctAnswer);
                if (success) {
                    Toast.makeText(AddQuestionActivity.this, "Thêm câu hỏi thành công", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(AddQuestionActivity.this, "Thêm câu hỏi thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearFields() {
        questionTextEditText.setText("");
        optionAEditText.setText("");
        optionBEditText.setText("");
        optionCEditText.setText("");
        optionDEditText.setText("");
        correctAnswerEditText.setText("");
    }
}