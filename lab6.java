import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class lab6 {
    public static void main(String[] args) {
        cache cache1 = new cache(2048, 1, 1);
        cache cache2 = new cache(2048, 1, 2);
        cache cache3 = new cache(2048, 1, 4);
        cache cache4 = new cache(2048, 2, 1);
        cache cache5 = new cache(2048, 4, 1);
        cache cache6 = new cache(2048, 4, 4);
        cache cache7 = new cache(4096, 1, 1);

        Scanner sc = null;
        try {
            sc = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (sc.hasNextLine()) {
            int next = Integer.parseInt(sc.nextLine(),16);
            cache1.add(next);
            cache2.add(next);
            cache3.add(next);
            cache4.add(next);
            cache5.add(next);
            cache6.add(next);
            cache7.add(next);
        }

        cache1.print("Cache #1");
        cache2.print("Cache #2");
        cache3.print("Cache #3");
        cache4.print("Cache #4");
        cache5.print("Cache #5");
        cache6.print("Cache #6");
        cache7.print("Cache #7");
    }
}
