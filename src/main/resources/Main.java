import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static int[] quickSort(int[] arr) {

    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int times = s.nextInt();
        int[] arr = new int[times];
        for (int i = 1; i <= times; i++) {
            arr[i-1] = s.nextInt();
        }
        out = quickSort(arr);
        for (int i = 1; i <= times; i++) {
            System.out.println(out[i-1]);
        }
    }
}