package vn.tlu.edu.cse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.tlu.edu.cse.Model.Exam;
import vn.tlu.edu.cse.Model.ExamRequest;
import vn.tlu.edu.cse.Model.Question;
import vn.tlu.edu.cse.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizApp.db";
    private static final int DATABASE_VERSION = 2; // Tăng version để cập nhật bảng exams

    // Bảng users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_ROLE = "role";

    // Bảng questions
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_OPTION_A = "option_a";
    public static final String COLUMN_OPTION_B = "option_b";
    public static final String COLUMN_OPTION_C = "option_c";
    public static final String COLUMN_OPTION_D = "option_d";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    // Bảng exams
    public static final String TABLE_EXAMS = "exams";
    public static final String COLUMN_EXAM_ID = "exam_id";
    public static final String COLUMN_EXAM_NAME = "exam_name";
    public static final String COLUMN_QUESTION_COUNT = "question_count"; // Thêm trường
    public static final String COLUMN_TIME = "time"; // Thêm trường (phút)

    // Bảng exam_requests
    public static final String TABLE_EXAM_REQUESTS = "exam_requests";
    public static final String COLUMN_REQUEST_ID = "request_id";
    public static final String COLUMN_STUDENT_ID_FK = "student_id";
    public static final String COLUMN_EXAM_ID_FK = "exam_id";
    public static final String COLUMN_STATUS = "status"; // 'pending', 'accepted', 'rejected'

    // Bảng exam_permissions
    public static final String TABLE_EXAM_PERMISSIONS = "exam_permissions";
    public static final String COLUMN_PERMISSION_ID = "permission_id";
    public static final String COLUMN_STUDENT_ID_FK_PERM = "student_id";
    public static final String COLUMN_EXAM_ID_FK_PERM = "exam_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_STUDENT_ID + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createUsersTable);

        // Thêm tài khoản giảng viên mặc định
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + COLUMN_STUDENT_ID + ", " + COLUMN_ROLE + ") " +
                "VALUES ('teacher@example.com', 'password123', NULL, 'teacher')");

        // Tạo bảng questions
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION_TEXT + " TEXT, " +
                COLUMN_OPTION_A + " TEXT, " +
                COLUMN_OPTION_B + " TEXT, " +
                COLUMN_OPTION_C + " TEXT, " +
                COLUMN_OPTION_D + " TEXT, " +
                COLUMN_CORRECT_ANSWER + " TEXT)";
        db.execSQL(createQuestionsTable);

        // Tạo bảng exams
        String createExamsTable = "CREATE TABLE " + TABLE_EXAMS + " (" +
                COLUMN_EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXAM_NAME + " TEXT, " +
                COLUMN_QUESTION_COUNT + " INTEGER, " +
                COLUMN_TIME + " INTEGER)";
        db.execSQL(createExamsTable);

        // Tạo bảng exam_requests
        String createExamRequestsTable = "CREATE TABLE " + TABLE_EXAM_REQUESTS + " (" +
                COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_ID_FK + " TEXT, " +
                COLUMN_EXAM_ID_FK + " INTEGER, " +
                COLUMN_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_STUDENT_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_EXAM_ID_FK + ") REFERENCES " + TABLE_EXAMS + "(" + COLUMN_EXAM_ID + "))";
        db.execSQL(createExamRequestsTable);

        // Tạo bảng exam_permissions
        String createExamPermissionsTable = "CREATE TABLE " + TABLE_EXAM_PERMISSIONS + " (" +
                COLUMN_PERMISSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_ID_FK_PERM + " TEXT, " +
                COLUMN_EXAM_ID_FK_PERM + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_STUDENT_ID_FK_PERM + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_EXAM_ID_FK_PERM + ") REFERENCES " + TABLE_EXAMS + "(" + COLUMN_EXAM_ID + "))";
        db.execSQL(createExamPermissionsTable);

        insertSampleQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_PERMISSIONS);
        onCreate(db);
    }

    private void insertSampleQuestions(SQLiteDatabase db) {
        String[][] sampleQuestions = {
                {"Thành phần nào là cốt lõi của ứng dụng Android?", "A. Activity", "B. Service", "C. BroadcastReceiver", "D. ContentProvider", "A"},
                {"File nào định nghĩa giao diện trong Android?", "A. Java", "B. XML", "C. Kotlin", "D. Gradle", "B"},
                {"Phương thức nào được gọi khi Activity khởi tạo?", "A. onStart()", "B. onCreate()", "C. onResume()", "D. onPause()", "B"},
                {"Thư viện nào dùng để tạo giao diện Material Design?", "A. RecyclerView", "B. Material Components", "C. Volley", "D. Retrofit", "B"},
                {"AndroidManifest.xml có vai trò gì?", "A. Quản lý layout", "B. Cấu hình ứng dụng", "C. Xử lý dữ liệu", "D. Tạo database", "B"},
                {"Phương thức setContentView() dùng để làm gì?", "A. Kết nối database", "B. Gán layout cho Activity", "C. Xử lý sự kiện", "D. Tạo Intent", "B"},
                {"Thành phần nào chạy nền trong Android?", "A. Activity", "B. Service", "C. Fragment", "D. Intent", "B"},
                {"Đối tượng nào dùng để chuyển dữ liệu giữa các Activity?", "A. Bundle", "B. ViewModel", "C. SQLite", "D. SharedPreferences", "A"},
                {"Phương thức nào được gọi khi Activity hiển thị lại?", "A. onResume()", "B. onRestart()", "C. onCreate()", "D. onStop()", "A"},
                {"Dependency trong Gradle được khai báo ở đâu?", "A. AndroidManifest.xml", "B. build.gradle", "C. res/values", "D. src/main", "B"},
                {"Annotation nào đánh dấu Activity chính?", "A. @MainActivity", "B. @IntentFilter", "C. @MainLauncher", "D. Không có annotation", "D"},
                {"Phương thức nào xử lý sự kiện nhấn nút?", "A. onClick()", "B. onTouch()", "C. onKey()", "D. onLongClick()", "A"},
                {"Thư viện nào dùng để quản lý danh sách dữ liệu?", "A. RecyclerView", "B. ListView", "C. GridView", "D. Tất cả đều đúng", "A"},
                {"File nào chứa thông tin phiên bản ứng dụng?", "A. AndroidManifest.xml", "B. build.gradle", "C. strings.xml", "D. styles.xml", "B"},
                {"Fragment là gì trong Android?", "A. Một layout", "B. Một phần giao diện", "C. Một Activity", "D. Một Service", "B"},
                {"Phương thức nào được gọi khi Activity bị hủy?", "A. onPause()", "B. onStop()", "C. onDestroy()", "D. onFinish()", "C"},
                {"Thư viện nào dùng để gọi API?", "A. Retrofit", "B. Picasso", "C. Glide", "D. Room", "A"},
                {"SharedPreferences dùng để làm gì?", "A. Lưu dữ liệu lớn", "B. Lưu dữ liệu nhỏ dạng key-value", "C. Quản lý database", "D. Tạo giao diện", "B"},
                {"Phương thức nào khởi tạo Intent?", "A. new Intent()", "B. startActivity()", "C. createIntent()", "D. launchIntent()", "A"},
                {"Thư viện Room dùng để làm gì?", "A. Tạo giao diện", "B. Quản lý database", "C. Gọi API", "D. Tải ảnh", "B"},
                {"Lifecycle của Activity có bao nhiêu trạng thái chính?", "A. 5", "B. 6", "C. 7", "D. 8", "C"},
                {"Annotation nào dùng để inject view?", "A. @Inject", "B. @BindView", "C. @FindView", "D. Không cần annotation", "B"},
                {"BroadcastReceiver dùng để làm gì?", "A. Hiển thị giao diện", "B. Nhận thông báo hệ thống", "C. Chạy nền", "D. Quản lý dữ liệu", "B"},
                {"Phương thức nào được gọi khi xoay màn hình?", "A. onCreate()", "B. onConfigurationChanged()", "C. onResume()", "D. onRestart()", "B"},
                {"Thư viện Glide dùng để làm gì?", "A. Tải ảnh", "B. Gọi API", "C. Quản lý danh sách", "D. Tạo database", "A"},
                {"ConstraintLayout có ưu điểm gì?", "A. Dễ quản lý", "B. Linh hoạt vị trí", "C. Hiệu suất cao", "D. Tất cả đều đúng", "D"},
                {"Phương thức nào lưu trạng thái Activity?", "A. onSaveInstanceState()", "B. onRestoreInstanceState()", "C. onPause()", "D. onStop()", "A"},
                {"ViewModel thuộc kiến trúc nào?", "A. MVC", "B. MVP", "C. MVVM", "D. Singleton", "C"},
                {"Thư viện nào thay thế AsyncTask?", "A. Coroutines", "B. Retrofit", "C. Volley", "D. WorkManager", "A"},
                {"Phương thức nào xử lý sự kiện dài trên view?", "A. onClick()", "B. onLongClick()", "C. onTouch()", "D. onKey()", "B"},
                {"ContentProvider dùng để làm gì?", "A. Chia sẻ dữ liệu giữa ứng dụng", "B. Hiển thị giao diện", "C. Chạy nền", "D. Gọi API", "A"},
                {"File nào chứa tài nguyên màu sắc?", "A. colors.xml", "B. strings.xml", "C. styles.xml", "D. dimens.xml", "A"},
                {"Phương thức nào được gọi trước onStop()?", "A. onPause()", "B. onDestroy()", "C. onResume()", "D. onCreate()", "A"},
                {"Thư viện nào dùng để hiển thị thông báo?", "A. Toast", "B. Snackbar", "C. Notification", "D. Tất cả đều đúng", "D"},
                {"Activity có thể chạy mà không cần giao diện không?", "A. Có", "B. Không", "C. Chỉ trong Service", "D. Chỉ trong Fragment", "A"},
                {"Phương thức nào khởi động Service?", "A. startService()", "B. bindService()", "C. launchService()", "D. Cả A và B", "D"},
                {"LiveData thuộc kiến trúc nào?", "A. MVC", "B. MVP", "C. MVVM", "D. Singleton", "C"},
                {"File nào định nghĩa style cho ứng dụng?", "A. styles.xml", "B. themes.xml", "C. colors.xml", "D. Cả A và B", "D"},
                {"Phương thức nào xử lý sự kiện chạm màn hình?", "A. onClick()", "B. onTouch()", "C. onLongClick()", "D. onKey()", "B"},
                {"Thư viện nào dùng để quản lý công việc nền?", "A. WorkManager", "B. Retrofit", "C. Room", "D. Glide", "A"},
                {"Phương thức nào khôi phục trạng thái Activity?", "A. onRestoreInstanceState()", "B. onSaveInstanceState()", "C. onResume()", "D. onCreate()", "A"},
                {"LinearLayout sắp xếp view theo hướng nào?", "A. Dọc", "B. Ngang", "C. Cả hai", "D. Không cố định", "C"},
                {"Thư viện nào dùng để tải ảnh bất đồng bộ?", "A. Picasso", "B. Glide", "C. Cả A và B", "D. Retrofit", "C"},
                {"Phương thức nào được gọi khi Activity bị ẩn?", "A. onPause()", "B. onStop()", "C. onDestroy()", "D. onResume()", "B"},
                {"Dependency 'androidx.appcompat' dùng để làm gì?", "A. Hỗ trợ giao diện cũ", "B. Tạo database", "C. Gọi API", "D. Quản lý danh sách", "A"},
                {"Phương thức nào kiểm tra quyền trong Android?", "A. checkPermission()", "B. requestPermissions()", "C. hasPermission()", "D. grantPermission()", "A"},
                {"Thư viện nào dùng để tạo animation?", "A. Lottie", "B. Glide", "C. Retrofit", "D. Room", "A"},
                {"Phương thức nào yêu cầu quyền từ người dùng?", "A. checkPermission()", "B. requestPermissions()", "C. grantPermission()", "D. denyPermission()", "B"},
                {"RelativeLayout sắp xếp view dựa trên gì?", "A. Vị trí tuyệt đối", "B. Vị trí tương đối", "C. Kích thước", "D. Màu sắc", "B"},
                {"Phương thức nào được gọi khi Activity bắt đầu hiển thị?", "A. onStart()", "B. onResume()", "C. onCreate()", "D. onRestart()", "A"}
        };

        for (String[] q : sampleQuestions) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUESTION_TEXT, q[0]);
            values.put(COLUMN_OPTION_A, q[1]);
            values.put(COLUMN_OPTION_B, q[2]);
            values.put(COLUMN_OPTION_C, q[3]);
            values.put(COLUMN_OPTION_D, q[4]);
            values.put(COLUMN_CORRECT_ANSWER, q[5]);
            db.insert(TABLE_QUESTIONS, null, values);
        }
    }

    public boolean registerStudent(String email, String password, String studentId) {
        SQLiteDatabase db = this.getWritableDatabase(); // lấy đối tượng database để ghi dữ liệu
        // writable: thêm sửa xoá
        // reaable: đọc
        try {
            ContentValues values = new ContentValues(); //Tạo một "túi" chứa dữ liệu, giống như một hàng trong Excel.
            values.put(COLUMN_EMAIL, email); // đưa email vào cột email
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_STUDENT_ID, studentId);
            values.put(COLUMN_ROLE, "student"); // mặc định là student
            long result = db.insertOrThrow(TABLE_USERS, null, values); // gán result(ID của hàng mới thêm) = thêm túi DL vao bang user
            return result != -1; // nếu = -1 là thất bại, true : thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close(); // đóng kết nối
        }
    }

    public String login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public String getStudentIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public List<ExamRequest> getPendingRequests() {
        List<ExamRequest> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_REQUESTS +
                    " WHERE " + COLUMN_STATUS + " = 'pending'", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID));
                    String studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID_FK));
                    int examId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXAM_ID_FK));
                    requests.add(new ExamRequest(id, studentId, examId, "pending"));
                } while (cursor.moveToNext());
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
            return requests;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public String getStudentNameById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_EMAIL + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_STUDENT_ID + "=?", new String[]{studentId});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            }
            return "Unknown";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public String getExamNameById(int examId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_EXAM_NAME + " FROM " + TABLE_EXAMS +
                    " WHERE " + COLUMN_EXAM_ID + "=?", new String[]{String.valueOf(examId)});
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAM_NAME));
            }
            return "Unknown";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    //THÊM CÂU HỎI
    public boolean addQuestion(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase(); // lấy DB để ghi
        try {
            ContentValues values = new ContentValues(); // Tạo "túi" DL
            values.put(COLUMN_QUESTION_TEXT, questionText); // Đưa từng GT vào cột tương ứng: nội dung câu hỏi
            values.put(COLUMN_OPTION_A, optionA);
            values.put(COLUMN_OPTION_B, optionB);
            values.put(COLUMN_OPTION_C, optionC);
            values.put(COLUMN_OPTION_D, optionD);
            values.put(COLUMN_CORRECT_ANSWER, correctAnswer);
            // thêm vào bảng questions
            // result: id của câu hỏi mới thêm
            long result = db.insertOrThrow(TABLE_QUESTIONS, null, values);
            return result != -1; // thành công nếu id khác -1
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


    //TẠO ĐỀ THI
    public boolean createExam(String examName, int questionCount, int time) {
        SQLiteDatabase db = this.getReadableDatabase(); // lấy dl để đọc
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);// đếm tổng số câu hỏi trong bảng questions
            if (cursor.moveToFirst()) {
                int totalQuestions = cursor.getInt(0);
                if (totalQuestions < questionCount) {
                    return false; //(không đủ câu hỏi)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXAM_NAME, examName);
            values.put(COLUMN_QUESTION_COUNT, questionCount);
            values.put(COLUMN_TIME, time);
            long result = db.insertOrThrow(TABLE_EXAMS, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public List<Exam> getAllExams() {
        List<Exam> examList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAMS, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXAM_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAM_NAME));
                    int questionCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_COUNT));
                    int time = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                    examList.add(new Exam(id, name, questionCount, time));
                } while (cursor.moveToNext());
            }
            return examList;
        } catch (Exception e) {
            e.printStackTrace();
            return examList;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean deleteExam(int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_EXAMS, COLUMN_EXAM_ID + "=?", new String[]{String.valueOf(examId)});
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public List<Question> getRandomQuestionsForExam(int count) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " ORDER BY RANDOM() LIMIT " + count, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ID));
                    String text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT));
                    String optionA = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_A));
                    String optionB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_B));
                    String optionC = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_C));
                    String optionD = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_D));
                    String correct = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER));
                    questionList.add(new Question(id, text, optionA, optionB, optionC, optionD, correct));
                } while (cursor.moveToNext());
            }
            return questionList;
        } catch (Exception e) {
            e.printStackTrace();
            return questionList;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }
//
    public boolean updateStudentProfile(String currentEmail, String newEmail, String newStudentId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, newEmail);
            values.put(COLUMN_STUDENT_ID, newStudentId);
            values.put(COLUMN_PASSWORD, newPassword);
            int rows = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{currentEmail});
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean addExamRequest(String studentId, int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STUDENT_ID_FK, studentId);
            values.put(COLUMN_EXAM_ID_FK, examId);
            values.put(COLUMN_STATUS, "pending");
            long result = db.insertOrThrow(TABLE_EXAM_REQUESTS, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean hasSentRequest(String studentId, int examId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_REQUESTS +
                    " WHERE " + COLUMN_STUDENT_ID_FK + "=? AND " + COLUMN_EXAM_ID_FK + "=?", new String[]{studentId, String.valueOf(examId)});
            return cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public List<ExamRequest> getExamRequestsByStatus(String status) {
        List<ExamRequest> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_REQUESTS +
                    " WHERE " + COLUMN_STATUS + "=?", new String[]{status});
            if (cursor.moveToFirst()) {
                do {
                    int requestId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID));
                    String studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID_FK));
                    int examId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXAM_ID_FK));
                    requests.add(new ExamRequest(requestId, studentId, examId, status));
                } while (cursor.moveToNext());
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
            return requests;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public boolean updateRequestStatus(int requestId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STATUS, newStatus);
            int rows = db.update(TABLE_EXAM_REQUESTS, values, COLUMN_REQUEST_ID + "=?", new String[]{String.valueOf(requestId)});
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean addStudentToExam(String studentId, int examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STUDENT_ID_FK_PERM, studentId);
            values.put(COLUMN_EXAM_ID_FK_PERM, examId);
            long result = db.insertOrThrow(TABLE_EXAM_PERMISSIONS, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean hasPermission(String studentId, int examId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_PERMISSIONS +
                    " WHERE " + COLUMN_STUDENT_ID_FK_PERM + "=? AND " + COLUMN_EXAM_ID_FK_PERM + "=?", new String[]{studentId, String.valueOf(examId)});
            return cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public User getStudentById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_STUDENT_ID + "=?", new String[]{studentId});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
                return new User(id, email, studentId, role);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }
    public boolean deleteRequest(int requestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_EXAM_REQUESTS,
                    COLUMN_REQUEST_ID + "=?",
                    new String[]{String.valueOf(requestId)});
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    public ExamRequest getExamRequestById(int requestId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_REQUESTS +
                    " WHERE " + COLUMN_REQUEST_ID + "=?", new String[]{String.valueOf(requestId)});
            if (cursor.moveToFirst()) {
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID_FK));
                int examId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXAM_ID_FK));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                return new ExamRequest(requestId, studentId, examId, status);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }
}