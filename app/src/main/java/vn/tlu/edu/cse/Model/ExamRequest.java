package vn.tlu.edu.cse.Model;

public class ExamRequest {
    private int requestId;
    private String studentId;
    private int examId;
    private String status;

    public ExamRequest(int requestId, String studentId, int examId, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.examId = examId;
        this.status = status;
    }

    public int getRequestId() { return requestId; }
    public String getStudentId() { return studentId; }
    public int getExamId() { return examId; }
    public String getStatus() { return status; }
}