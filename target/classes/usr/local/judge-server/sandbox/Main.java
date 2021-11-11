import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int len = s.nextInt();
        for (i = 1; i <= len; i++) {
            System.out.println(s.nextInt() + s.nextInt());
        }
    }
}