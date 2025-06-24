package vn.tlu.edu.cse.Model;

public class Exam {
    private int id;
    private String name;
    private int questionCount;
    private int time;

    public Exam(int id, String name, int questionCount, int time) {
        this.id = id;
        this.name = name;
        this.questionCount = questionCount;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getTime() {
        return time;
    }
}