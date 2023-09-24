import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class mySQLDriver extends dbDriver {
    private Connection con;
    private SimpleDateFormat dateFormat; // Declare dateFormat

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://10.33.4.30/db2020015640", "s2020015640", "33013301");
//            con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/lab707","root","33013301");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Initialize dateFormat
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void save(Student st) {
        try {
            Map<CustomKey, CustomKeyValue> highestTotalMarksMap = new HashMap<>();
            getHighestTotalMarks(highestTotalMarksMap);
            int studentId = st.getStudentId();
            String course = st.getCourse();
            CustomKey lookupKey = new CustomKey(studentId, course);
            CustomKeyValue value = highestTotalMarksMap.get(lookupKey);

            if (value != null && value.getSecond() >= 80) {
                System.out.println("Student already has 80%");
                return;
            }

            Date date = st.getExamDate();
            String examDate = dateFormat.format(date);

            if (value != null && isEarlierDate(examDate, value.getDate())) {
                System.out.println("Invalid Exam Date\n");
                return;
            }

            int incourseMarks;
            if (value == null) {
                incourseMarks = st.getIncourseMarks();
            } else {
                incourseMarks = value.getFirst();
                System.out.println("This Student has already attended an in-course, so input only final marks!\n");
            }
            int finalMarks = st.getFinalMarks();

            String insertQuery = "INSERT INTO ExamMarks (StudentId, Course, ExamDate, IncourseMarks, FinalMarks) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setInt(1, studentId);
            ps.setString(2, course);
            ps.setString(3, examDate);
            ps.setInt(4, incourseMarks);
            ps.setInt(5, finalMarks);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                if (value == null) {
                    highestTotalMarksMap.put(lookupKey, new CustomKeyValue(incourseMarks, incourseMarks + finalMarks, examDate));
                } else {
                    int hi = Math.max(value.getSecond(), incourseMarks + finalMarks);
                    highestTotalMarksMap.put(lookupKey, new CustomKeyValue(incourseMarks, hi, examDate));
                }
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void load() {
        try {
            Statement stmt = con.createStatement();

            ResultSet rowCountResult = stmt.executeQuery("SELECT COUNT(*) FROM ExamMarks");
            rowCountResult.next();
            int rowCount = rowCountResult.getInt(1);
            System.out.println("--------------------------");
            System.out.println("Total Row Count: " + rowCount);

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM ExamMarks");
            while (resultSet.next()) {
                int studentId = resultSet.getInt("StudentId");
                String course = resultSet.getString("Course");
                String examDate = resultSet.getString("ExamDate");
                int incourseMarks = resultSet.getInt("IncourseMarks");
                int finalMarks = resultSet.getInt("FinalMarks");
                int totalMarks = resultSet.getInt("TotalMarks");

                System.out.println("StudentId: " + studentId);
                System.out.println("Course: " + course);
                System.out.println("ExamDate: " + examDate);
                System.out.println("IncourseMarks: " + incourseMarks);
                System.out.println("FinalMarks: " + finalMarks);
                System.out.println("TotalMarks: " + totalMarks);
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database connection: " + e.getMessage());
        }
    }

    public void getHighestTotalMarks(Map<CustomKey, CustomKeyValue> highestTotalMarksMap) {
        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM ExamMarks");
            while (resultSet.next()) {
                int studentId = resultSet.getInt("StudentId");
                String course = resultSet.getString("Course");
                int totalMarks = resultSet.getInt("TotalMarks");
                int incourseMarks = resultSet.getInt("IncourseMarks");
                String examDate = resultSet.getString("ExamDate");
                CustomKey key = new CustomKey(studentId, course);
                CustomKeyValue value = highestTotalMarksMap.get(key);

                // Check if the key is already in the map
                if (value == null) {
                    // If the key is not in the map, create a new CustomKeyValue with the totalMarks
                    highestTotalMarksMap.put(key, new CustomKeyValue(incourseMarks, totalMarks, examDate));
                } else {
                    // If the key is in the map, update the value with the maximum totalMarks
                    int hi = Math.max(value.getSecond(), totalMarks);
                    boolean ok = isEarlierDate(value.getDate(), examDate);
                    if (!ok) {
                        examDate = value.getDate();
                    }
                    highestTotalMarksMap.put(key, new CustomKeyValue(incourseMarks, hi, examDate));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean isEarlierDate(String dateString1, String dateString2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(dateString1);
            Date date2 = dateFormat.parse(dateString2);

            return date1.before(date2);
        } catch (ParseException e) {
            // Handle the exception if the date strings are not in the expected format
            e.printStackTrace();
            return false; // You can choose an appropriate error handling strategy here
        }
    }
    public void query1(Student st) {
        String query_select = "SELECT StudentID, Course, ExamDate, IncourseMarks, FinalMarks, TotalMarks\n" +
                "FROM ExamMarks\n" +
                "WHERE StudentID = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query_select)) {
            preparedStatement.setInt(1, st.getStudentId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String course = resultSet.getString("Course");
                String examDate = resultSet.getString("ExamDate");
                int incourseMarks = resultSet.getInt("IncourseMarks");
                int finalMarks = resultSet.getInt("FinalMarks");
                int totalMarks = resultSet.getInt("TotalMarks");

                System.out.println("StudentID: " + st.getStudentId());
                System.out.println("Course: " + course);
                System.out.println("ExamDate: " + examDate);
                System.out.println("IncourseMarks: " + incourseMarks);
                System.out.println("FinalMarks: " + finalMarks);
                System.out.println("TotalMarks: " + totalMarks);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    public void query2(Student st) {
        String query_select = "SELECT\n" +
                "    MAX(TotalMarks) AS MaximumMarks,\n" +
                "    AVG(TotalMarks) AS AverageMarks\n" +
                "FROM ExamMarks\n" +
                "WHERE Course = ? AND ExamDate = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query_select)) {
            preparedStatement.setString(1, st.getCourse());
            preparedStatement.setString(2, dateFormat.format(st.getExamDate()));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int maxMarks = resultSet.getInt("MaximumMarks");
                double avgMarks = resultSet.getDouble("AverageMarks");
                System.out.println("Maximum Marks: " + maxMarks);
                System.out.println("Average Marks: " + avgMarks);
            } else {
                System.out.println("No data found for the specified course and exam date.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    public void query3() {
        String query_select = "SELECT StudentID\n" +
                "FROM (\n" +
                "    SELECT\n" +
                "        StudentID,\n" +
                "        COUNT(*) AS AttemptCount\n" +
                "    FROM ExamMarks\n" +
                "    GROUP BY StudentID, Course\n" +
                ") AS AttemptCounts\n" +
                "GROUP BY StudentID\n" +
                "HAVING MAX(AttemptCount) = 1;";

        try (PreparedStatement preparedStatement = con.prepareStatement(query_select);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("StudentID");
                System.out.println(id);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
}
