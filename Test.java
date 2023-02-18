import java.util.Scanner;
public class Test {
    public static void bubbleSort(int[] arr, int n) {
        for(int i = 0; i < n - 1; i++) {
            for(int j = 0; j < n - 1 - i; j++) {
                if(arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return;
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int tt = input.nextInt();
        while(tt-- != 0) {
            int n = input.nextInt();
            int[] arr = new int[n];
            for(int i = 0; i < n; i++) {
                arr[i] = input.nextInt();
            }
            bubbleSort(arr, n);
            for(int i = 0; i < n; i++) {
                System.out.print(arr[i] + " ");
            }
        }
        input.close();
    }
}
