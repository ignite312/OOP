// Md. Emon Khan
// Roll : 30
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;


class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://10.33.4.30/db2020015640","s2020015640","33013301");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/lab707","root","33013301");
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter\n1 for Create Table\n2 for Data Insert\n3 for Data Display\n4 for Query 1\n5 for Query 2\n6 for Query 3\n");
            while(true) {
                System.out.print("Enter Query Type: ");
                int type = sc.nextInt();
                if (type == 1) createTable(con);
                else if (type == 2) dataInput(con);
                else if (type == 3) display(con);
                else if(type == 4)query1(con);
                else if(type == 5)query2(con);
                else if(type == 6)query3(con);
                else System.out.println("Invalid Query Type");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void createTable(Connection con) {
        try {
            Statement stmt = con.createStatement();

            // Check if the table 'ExamMarks' exists
            ResultSet tables = con.getMetaData().getTables(null, null, "ExamMarks", null);

            if (tables.next()) {
                // Table already exists
                System.out.println("Table 'ExamMarks' already exists.");
            } else {
                // Table does not exist, so create it
                String createTableSQL = "CREATE TABLE ExamMarks (" + "StudentId INT," + "Course VARCHAR(255)," + "ExamDate DATE," + "IncourseMarks INT," + "FinalMarks INT," + "TotalMarks INT GENERATED ALWAYS AS (IncourseMarks + FinalMarks) STORED," + "PRIMARY KEY (StudentId, Course, ExamDate)," + "CHECK (IncourseMarks >= 0 AND IncourseMarks <= 30)," + "CHECK (FinalMarks >= 0 AND FinalMarks <= 70)" + ")";

                // Execute the SQL statement to create the table
                stmt.executeUpdate(createTableSQL);

                System.out.println("Table 'ExamMarks' created successfully.");
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void dataInput(Connection con) {
        try {
            ResultSet tables = con.getMetaData().getTables(null, null, "ExamMarks", null);
            if (!tables.next()) {
                System.out.println("Table 'ExamMarks' Doesn't exists");
                return;
            }
            Map<CustomKey, CustomKeyValue> highestTotalMarksMap = new HashMap<>();
            getHighestTotalMarks(highestTotalMarksMap, con);
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter total row: ");
            int tt;
            tt = sc.nextInt();
            for (int i = 0; i < tt; i++) {
                System.out.print("StudentId: ");
                int studentId = sc.nextInt();
                sc.nextLine();
                System.out.print("Course: ");
                String course = sc.nextLine();

                CustomKey lookupKey = new CustomKey(studentId, course);
                CustomKeyValue value = highestTotalMarksMap.get(lookupKey);

                if (value != null && value.getSecond() >= 80) {
                    System.out.println("Student already have 80%");
                    continue;
                }
                System.out.print("ExamDate (YYYY-MM-DD): ");
                String examDate = sc.nextLine();
                if(value != null && isEarlierDate(examDate, value.getDate())) {
                    System.out.println("Invalid Exam Date\n");
                    continue;
                }
                int incourseMarks;
                if (value == null) {
                    System.out.print("IncourseMarks: ");
                    incourseMarks = sc.nextInt();
                } else {
                    incourseMarks = value.getFirst();
                    System.out.print("This Student has already attend an in-course so input only final marks!\n");
                }
                System.out.print("FinalMarks: ");
                int finalMarks = sc.nextInt();

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
                        highestTotalMarksMap.put(lookupKey, new CustomKeyValue(incourseMarks, incourseMarks+finalMarks, examDate));
                    } else {
                        int hi = Math.max(value.getSecond(), incourseMarks+finalMarks);
                        highestTotalMarksMap.put(lookupKey, new CustomKeyValue(incourseMarks, hi, examDate));
                    }
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Failed to insert data.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getHighestTotalMarks(Map<CustomKey, CustomKeyValue> highestTotalMarksMap, Connection con) {
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
                    Boolean ok = isEarlierDate(value.getDate(), examDate);
                    if(!ok)examDate = value.getDate();
                    highestTotalMarksMap.put(key, new CustomKeyValue(incourseMarks, hi, examDate));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void display(Connection con) {
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

    public static void query1(Connection con) {
        String query_select = "SELECT StudentID, Course, ExamDate, IncourseMarks, FinalMarks, TotalMarks\n" +
                "FROM ExamMarks\n" +
                "WHERE (StudentID, Course, TotalMarks) IN (\n" +
                "    SELECT StudentID, Course, MAX(TotalMarks)\n" +
                "    FROM ExamMarks\n" +
                "    GROUP BY StudentID, Course\n" +
                ");";

        try (PreparedStatement preparedStatement = con.prepareStatement(query_select);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("StudentID");
                String cour = resultSet.getString("Course");
                String date = resultSet.getString("ExamDate");
                int inc = resultSet.getInt("IncourseMarks");
                int fin = resultSet.getInt("FinalMarks");
                int total = resultSet.getInt("TotalMarks");
                System.out.println(id + " " + cour + " " + date + " " + inc + " " + fin + " " + total);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    public static void query2(Connection con) {
        String query_select = "SELECT\n" +
                "    Course,\n" +
                "    ExamDate,\n" +
                "    MAX(TotalMarks) AS MaximumMarks,\n" +
                "    AVG(TotalMarks) AS AverageMarks\n" +
                "FROM ExamMarks\n" +
                "GROUP BY Course, ExamDate;";

        try (PreparedStatement preparedStatement = con.prepareStatement(query_select);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String cour = resultSet.getString("Course");
                String date = resultSet.getString("ExamDate");
                int maxMarks = resultSet.getInt("MaximumMarks");
                double avgMarks = resultSet.getDouble("AverageMarks");
                System.out.println(cour + " " + date + " " + maxMarks + " " + avgMarks);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
    public static void query3(Connection con) {
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
    public static boolean isEarlierDate(String dateString1, String dateString2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = (Date) dateFormat.parse(dateString1);
            Date date2 = (Date) dateFormat.parse(dateString2);

            return date1.before(date2);
        } catch (ParseException e) {
            // Handle the exception if the date strings are not in the expected format
            e.printStackTrace();
            return false; // You can choose an appropriate error handling strategy here
        }
    }

}
