package vn.tlu.edu.cse.Model;

public class User {
    private int id;
    private String email;
    private String studentId;
    private String role;

    public User(int id, String email, String studentId, String role) {
        this.id = id;
        this.email = email;
        this.studentId = studentId;
        this.role = role;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getStudentId() { return studentId; }
    public String getRole() { return role; }
}
