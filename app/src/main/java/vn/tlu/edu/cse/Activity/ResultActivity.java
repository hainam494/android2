package vn.tlu.edu.cse.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import vn.tlu.edu.cse.R;

public class ResultActivity extends AppCompatActivity {
    private TextView scoreTextView, correctTextView, wrongTextView, unansweredTextView, timeTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        correctTextView = findViewById(R.id.correctTextView);
        wrongTextView = findViewById(R.id.wrongTextView);
        unansweredTextView = findViewById(R.id.unansweredTextView);
        timeTextView = findViewById(R.id.timeTextView);
        backButton = findViewById(R.id.backButton);

        int correct = getIntent().getIntExtra("CORRECT", 0);
        int wrong = getIntent().getIntExtra("WRONG", 0);
        int unanswered = getIntent().getIntExtra("UNANSWERED", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        long time = getIntent().getLongExtra("TIME", 0);

        // Tính điểm trên thang 10
        double score = (correct / (double) totalQuestions) * 10;
        scoreTextView.setText(String.format("Điểm: %.1f/10", score));
        correctTextView.setText("Số câu đúng: " + correct);
        wrongTextView.setText("Số câu sai: " + wrong);
        unansweredTextView.setText("Số câu chưa trả lời: " + unanswered);
        timeTextView.setText("Thời gian làm bài: " + time / 60 + " phút " + time % 60 + " giây");

        backButton.setOnClickListener(v -> finish());
    }
}