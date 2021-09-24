import java.util.*;
import java.util.streams.stream;
import java.util.streams.Collectors;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        
        List<Integer> a = new ArrayList();

        for(int i = 0; i < 10; ++i) {
            a.add(i);
        }

        System.out.println(a.stream().filter(i -> i % 2 == 0).collect(Collectors.toList()));
    }

    private static void outList(int[] a) {
        System.out.println(a);
    }
}
