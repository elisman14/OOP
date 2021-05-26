import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.Math.*;

public class Program {
    public static void main(String[] args) {
        // #1
        System.out.println("FFFF " + "ABCD" + "\t" + sameLetterPattern("FFFF", "ABCD"));
        System.out.println(spiderToFly("A4", "C2"));
        System.out.println(digitsCount(12345));
    }

    /*
     * #1
     * Создайте функцию, которая возвращает true, если две строки имеют один и тот же
     * буквенный шаблон, и false в противном случае.
     */
    public static int getPattern(String input) {
        input = input.toLowerCase(Locale.ROOT);
        char[] inputArray = input.toCharArray();
        int patternCode = 0;
        HashMap<Character, Integer> charMap = new HashMap<>();

        for(int i = 0; i < input.length(); i++) {
            if(!charMap.containsKey(inputArray[i])) {
                charMap.put(inputArray[i], i);
            }
        }

        for(int i = 0; i < input.length(); ++i) { patternCode += (charMap.get(inputArray[i]) + 1) * pow(10, i); }

        return patternCode;
    }

    public static boolean sameLetterPattern(String a, String b) {
        return getPattern(a) == getPattern(b);
    }

    /*
     * #2
     * Паутина определяется кольцами, пронумерованными от 0 до 4 от центра, и радиалами,
     * помеченными по часовой стрелке сверху как A-H.Создайте функцию, которая принимает
     * координаты паука и мухи и возвращает кратчайший путь для паука, чтобы добраться до мухи.
     * Стоит отметить, что кратчайший путь должен быть рассчитан "геометрически",
     * а не путем подсчета количества точек, через которые проходит этот путь. Мы могли бы это устроить:
     * Угол между каждой парой радиалов одинаков (45 градусов). Расстояние между каждой парой
     * колец всегда одинаково (скажем, "x").
     */
    public static String spiderToFly(final String spider, final String fly) {
        String radials = "ABCDEFGH";
        char sr = spider.charAt(0), sl = spider.charAt(1), fr = fly.charAt(0), fl = fly.charAt(1);
        char midRing = abs(sr - fr) <= 2 || 6 <= abs(sr - fr) ? (char) min(sl,fl) : '0';

        StringBuilder answer = new StringBuilder();
        answer.append(sr).append(sl).append("-");

        while (sl > midRing) {
            answer.append(--sl == '0' ? "A0-" : "" + sr + sl + "-");
        }

        while (true) {
            if (sr == fr) { break; }
            if (6 <= abs(sr - fr)) {
                if (sr > fr) {
                    sr++;
                    if (sr == 'I') {
                        sr = 'A';
                    }
                } else {
                    sr--;
                    if (sr == '@') {
                        sr = 'H';
                    }
                }
            } else {
                if (sr < fr) { sr++; }
                else { sr--; }
            }

            answer.append(sr).append(sl).append("-");
        }

        while (sl++ < fl) {
            answer.append(fr).append(sl).append("-");
        }

        return answer.substring(0, answer.length() -1);
    }

    /*
     * #3
     * Создайте функцию, которая будет рекурсивно подсчитывать количество цифр
     * числа. Преобразование числа в строку не допускается, поэтому подход является рекурсивным.
     */
    public static int digitsCount(long n) {
        if (n != 0) {
            n = floorDiv(n, 10);
            if (n < 10) { return digitsCount(n) ; }
            else { return 1 + digitsCount(n); }

        } else { return 0; }
    }
}
