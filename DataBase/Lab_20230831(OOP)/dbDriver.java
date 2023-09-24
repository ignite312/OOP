import java.sql.Connection;

public abstract class dbDriver {
    protected Connection con;

    abstract public void connect();
    abstract public void load();
    abstract public void save(Student st);
    abstract public void query1(Student st);
    abstract public void query2(Student st);
    abstract public void query3();
}
