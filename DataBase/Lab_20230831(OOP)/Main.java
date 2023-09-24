// Md. Emon Khan
// Roll : 30
import java.sql.Date;
class Main {
    public static void main(String[] args) {
        try {
            mySQLDriver dv = new mySQLDriver();
            dv.connect();
            Student st = new Student(7, "d", Date.valueOf("2020-01-01"), 10, 50, 60);
            dv.save(st);
            dv.load();
            dv.query1(st);
            dv.query2(st);
            dv.query3();
        } catch (Exception e) {

        }
    }
}