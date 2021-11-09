
import java.io.IOException;

public class TestBench1 {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(Runtime.getRuntime().exec("ls"));
        System.out.println(Runtime.getRuntime().exec("ulimit").waitFor());
    }
}
