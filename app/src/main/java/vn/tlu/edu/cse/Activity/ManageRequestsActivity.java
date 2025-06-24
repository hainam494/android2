package vn.tlu.edu.cse.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.tlu.edu.cse.DatabaseHelper;
import vn.tlu.edu.cse.Model.ExamRequest;
import vn.tlu.edu.cse.R;

public class ManageRequestsActivity extends AppCompatActivity {
    private RecyclerView requestRecyclerView;
    private DatabaseHelper db;
    private List<ExamRequest> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        requestRecyclerView = findViewById(R.id.requestRecyclerView);
        db = new DatabaseHelper(this);

        requests = db.getPendingRequests();
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestRecyclerView.setAdapter(new RequestAdapter(requests));
    }

    private class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
        private List<ExamRequest> requestList;

        public RequestAdapter(List<ExamRequest> requestList) {
            this.requestList = requestList;
        }

        @NonNull
        @Override
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
            return new RequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
            ExamRequest request = requestList.get(position);
            holder.studentNameTextView.setText("Tên sinh viên: " + db.getStudentNameById(request.getStudentId()));
            holder.studentIdTextView.setText("Mã sinh viên: " + request.getStudentId());
            holder.examNameTextView.setText("Tên đề thi: " + db.getExamNameById(request.getExamId()));

            holder.acceptButton.setOnClickListener(v -> {
                db.updateRequestStatus(request.getRequestId(), "accepted");
                db.addStudentToExam(request.getStudentId(), request.getExamId());
                requestList.remove(position);
                notifyItemRemoved(position);
            });

            holder.rejectButton.setOnClickListener(v -> {
                //db.updateRequestStatus(request.getRequestId(), "rejected");
                //requestList.remove(position);
                //notifyItemRemoved(position);
                // Xóa hoàn toàn request khỏi database khi từ chối
                db.deleteRequest(request.getRequestId());
                requestList.remove(position);
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return requestList.size();
        }

        class RequestViewHolder extends RecyclerView.ViewHolder {
            TextView studentNameTextView, studentIdTextView, examNameTextView;
            Button acceptButton, rejectButton;

            public RequestViewHolder(@NonNull View itemView) {
                super(itemView);
                studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
                studentIdTextView = itemView.findViewById(R.id.studentIdTextView);
                examNameTextView = itemView.findViewById(R.id.examNameTextView);
                acceptButton = itemView.findViewById(R.id.acceptButton);
                rejectButton = itemView.findViewById(R.id.rejectButton);
            }
        }
    }
}