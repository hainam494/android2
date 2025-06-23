package vn.tlu.edu.cse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.Model.Question;
import vn.tlu.edu.cse.R;

public class TakeExamActivity extends AppCompatActivity {
    private TextView timerTextView, questionTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton optionARadioButton, optionBRadioButton, optionCRadioButton, optionDRadioButton;
    private Button prevButton, nextButton, submitButton;
    private DatabaseHelper db;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private CountDownTimer timer;
    private long timeLeftInMillis = 600000; // 10 phút
    private String studentId;
    private int examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_exam);

        // Khởi tạo các thành phần giao diện
        timerTextView = findViewById(R.id.timerTextView);
        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        optionARadioButton = findViewById(R.id.optionARadioButton);
        optionBRadioButton = findViewById(R.id.optionBRadioButton);
        optionCRadioButton = findViewById(R.id.optionCRadioButton);
        optionDRadioButton = findViewById(R.id.optionDRadioButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        db = new DatabaseHelper(this);

        // Lấy dữ liệu từ Intent
        studentId = getIntent().getStringExtra("STUDENT_ID");
        examId = getIntent().getIntExtra("EXAM_ID", -1);

        // Kiểm tra quyền làm bài thi
        if (!db.hasPermission(studentId, examId)) {
            Toast.makeText(this, "Bạn không có quyền làm bài thi này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khôi phục trạng thái nếu có, nếu không thì lấy danh sách câu hỏi mới
        if (savedInstanceState != null) {
            questions = savedInstanceState.getParcelableArrayList("QUESTIONS");
            currentQuestionIndex = savedInstanceState.getInt("CURRENT_INDEX");
            timeLeftInMillis = savedInstanceState.getLong("TIME_LEFT");
        } else {
            questions = db.getRandomQuestionsForExam(10); // Giả định 10 câu hỏi
        }

        // Khởi động đồng hồ đếm ngược và hiển thị câu hỏi
        startTimer();
        displayQuestion();

        // Xử lý lựa chọn câu trả lời
        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String answer = null;
            if (checkedId == R.id.optionARadioButton) answer = "A";
            else if (checkedId == R.id.optionBRadioButton) answer = "B";
            else if (checkedId == R.id.optionCRadioButton) answer = "C";
            else if (checkedId == R.id.optionDRadioButton) answer = "D";
            questions.get(currentQuestionIndex).setUserAnswer(answer);
        });

        // Xử lý nút "Trước"
        prevButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        });

        // Xử lý nút "Tiếp"
        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            }
        });

        // Xử lý nút "Nộp bài"
        submitButton.setOnClickListener(v -> submitExam());
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                timerTextView.setText(String.format("Thời gian: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                submitExam();
            }
        }.start();
    }

    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestionText());
        optionARadioButton.setText(question.getOptionA());
        optionBRadioButton.setText(question.getOptionB());
        optionCRadioButton.setText(question.getOptionC());
        optionDRadioButton.setText(question.getOptionD());

        // Xóa tích trước khi khôi phục để tránh xung đột
        optionsRadioGroup.clearCheck();

        // Khôi phục trạng thái đáp án đã chọn
        if (question.getUserAnswer() != null) {
            switch (question.getUserAnswer()) {
                case "A":
                    optionARadioButton.setChecked(true);
                    break;
                case "B":
                    optionBRadioButton.setChecked(true);
                    break;
                case "C":
                    optionCRadioButton.setChecked(true);
                    break;
                case "D":
                    optionDRadioButton.setChecked(true);
                    break;
            }
        }

        // Đảm bảo giao diện cập nhật
        optionsRadioGroup.requestLayout();

        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
    }

    private void submitExam() {
        timer.cancel();
        int correct = 0, wrong = 0, unanswered = 0;
        for (Question q : questions) {
            if (q.getUserAnswer() == null) unanswered++;
            else if (q.getUserAnswer().equals(q.getCorrectAnswer())) correct++;
            else wrong++;
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("CORRECT", correct);
        intent.putExtra("WRONG", wrong);
        intent.putExtra("UNANSWERED", unanswered);
        intent.putExtra("TOTAL_QUESTIONS", questions.size());
        intent.putExtra("TIME", (600000 - timeLeftInMillis) / 1000);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("QUESTIONS", new ArrayList<>(questions));
        outState.putInt("CURRENT_INDEX", currentQuestionIndex);
        outState.putLong("TIME_LEFT", timeLeftInMillis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}