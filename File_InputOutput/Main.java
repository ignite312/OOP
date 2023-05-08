import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String names[] = {"Emon", "Demon", "Cemon", "Kemon"};
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write("Here we go.....\n");
            for(String name : names) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
