import java.util.Date;

public class Student {
    private int studentId;
    private String course;
    private Date examDate;
    private Integer incourseMarks;
    private Integer finalMarks;
    private Integer totalMarks;

    // Constructors
    public Student() {
        // Default constructor
    }

    public Student(int studentId, String course, Date examDate, Integer incourseMarks, Integer finalMarks, Integer totalMarks) {
        this.studentId = studentId;
        this.course = course;
        this.examDate = examDate;
        this.incourseMarks = incourseMarks;
        this.finalMarks = finalMarks;
        this.totalMarks = totalMarks;
    }

    // Getter and Setter methods for each field
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Integer getIncourseMarks() {
        return incourseMarks;
    }

    public void setIncourseMarks(Integer incourseMarks) {
        this.incourseMarks = incourseMarks;
    }

    public Integer getFinalMarks() {
        return finalMarks;
    }

    public void setFinalMarks(Integer finalMarks) {
        this.finalMarks = finalMarks;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", course='" + course + '\'' +
                ", examDate=" + examDate +
                ", incourseMarks=" + incourseMarks +
                ", finalMarks=" + finalMarks +
                ", totalMarks=" + totalMarks +
                '}';
    }
}
