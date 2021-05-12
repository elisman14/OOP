import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3, 4, 5, 6, 9, 97};
        int[] b = new int[]{9, 4, 26, 26, 0, 0, 5, 20, 6, 25, 5};
        System.out.println(sevenBoomStream(a));
        System.out.println(cons(a));
        System.out.println(unmix("hTsii  s aimex dpus rtni.g"));
        System.out.println(noYellin("I just!!! can!!! not!!! believe!!! it!"));
        System.out.println(xPronounce("Inside the box was a xylophone"));
        System.out.println(largestGap(b));
        System.out.println(noName(7977));
        System.out.println(commonLastVowel("OOI UUI EEI AAI"));
        System.out.println(memeSum(1222, 30277));
        System.out.println(unrepeated("hello"));
    }

    /**
     * task #1
     * Создайте функцию, которая принимает массив чисел и возвращает "Бум!", если в
     * массиве появляется цифра 7. В противном случае верните "в массиве нет 7".
     */
    public static String sevenBoomStream(int[] a) {
        // boolean stream = Arrays.stream(a).anyMatch(i -> i == 7);
        for (int i : a) {
            while (i > 0) {
                if (i % 10 == 7) { return "BOOM"; }
                i = i / 10;
            }
        }
        return "there is no 7 in the array";
    }

    /**
     * task #2
     * Создайте функцию, которая определяет, могут ли элементы в массиве быть
     * переупорядочены, чтобы сформировать последовательный список чисел, где
     * каждое число появляется ровно один раз.
     */
    public static boolean cons(int[] a) {
        return Arrays.stream(a).allMatch(new HashSet<>()::add);
    }

    /**
     * task #3
     * Каким-то образом все строки перепутались, каждая пара символов поменялась местами.
     * Помоги отменить это, чтобы снова понять строки.
     */
    // Есть ли смысл сделать через stream ?
    public static String unmix(String s) {
        char[] a = s.toCharArray();
        char t;
        for (int i = 1; i < a.length; i += 2)
        {
                t = a[i];
                a[i] = a[i - 1];
                a[i - 1] = t;
        }
        return String.valueOf(a);
    }

    /**
     * task #4
     * Создать функцию, которая преобразует предложения, заканчивающиеся
     * несколькими вопросительными знаками ? или восклицательными знаками ! в
     * предложение, заканчивающееся только одним, без изменения пунктуации в
     * середине предложений.
     */
    public static String noYellin(String s) {
        Pattern pattern = Pattern.compile("^.+?[?!]$");
        Matcher matcher = pattern.matcher(s);
        return (matcher.find()) ? s.substring(matcher.start(), matcher.end()) : s;
    }

    /**
     * task #5
     * Создайте функцию, которая заменяет все x в строке следующими способами:
     * Замените все x на "cks", ЕСЛИ ТОЛЬКО:
     * Слово начинается с "x", поэтому замените его на "z".
     * Слово-это просто буква "х", поэтому замените ее на " cks ".
     */
    public static String xPronounce(String s) {
        String[] sArray = s.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : sArray) {
            if (i.startsWith("x") && i.length() > 1) i = "z" + i.substring(1);
            stringBuilder.append(i.replaceAll("x", "cks")).append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * task #6
     * Учитывая массив целых чисел, верните наибольший разрыв между
     * отсортированными элементами массива.
     */
    public static int largestGap(int[] a) {
        Arrays.sort(a);
        int max = 0;
        for (int i = 1; i < a.length; i++) {
            max = Math.max(max, Math.abs(a[i] - a[i - 1]));
        }
        return max;
    }

    // Считает максимальную разность между всеми эелементами массива,
    // а не соседними
    public static OptionalInt largestGapStream(int[] a) {
        OptionalInt max;
        Arrays.sort(a);
        max = Arrays.stream(a)
                .flatMap(n -> Arrays.stream(a)
                        .map(i -> i - n)).max();

        return max;
    }

    /**
     * task #7
     * Это вызов обратного кодирования. Обычно вам дают явные указания о том, как
     * создать функцию. Здесь вы должны сгенерировать свою собственную функцию,
     * чтобы удовлетворить соотношение между входами и выходами.
     * Ваша задача состоит в том, чтобы создать функцию, которая при подаче входных данных
     * ниже производит показанные примеры выходных данных.
     */
    public static int noName(int n) {
        ArrayList<Integer> arr = new ArrayList<>();
        int numCopy = n, result = 0;
        while (numCopy > 0) {
            arr.add(numCopy % 10);
            numCopy /= 10;
        }

        Collections.sort(arr);
        for (Integer k : arr) { result = result * 10 + k; }

        return Math.max(n - result, 0);
    }

    /**
     * task #8
     * Создайте функцию, которая принимает предложение в качестве входных данных и
     * возвращает наиболее распространенную последнюю гласную в предложении в
     * виде одной символьной строки.
     */
    public static String commonLastVowel(String s) {
        char[] lettersArray = new char[]{'a', 'e', 'y', 'u', 'i', 'o'};
        Set<Character> lettersSet = new HashSet<>();
        for (char i : lettersArray) lettersSet.add(i);
        Map<Character, Integer> lettersDict = new HashMap<>();
        String[] stringArray = s.toLowerCase().split(" ");

        for (String i : stringArray) {
            char lastLetter = i.charAt(i.length() - 1);
            if (lettersSet.contains(lastLetter)) {
                lettersDict.merge(lastLetter, 1, Integer::sum);
            }
        }
        return Collections.max(lettersDict.entrySet(), Map.Entry.comparingByValue()).getKey().toString();
    }

    /**
     * task #9
     * Для этой задачи забудьте, как сложить два числа вместе. Лучшее объяснение того,
     * что нужно сделать для этой функции, - это этот мем: 248 + 208 = 4416
     */
    public static int memeSum(int a, int b) {
        StringBuilder s = new StringBuilder();
        while (a > 0 || b > 0){
            s.insert(0, (a % 10 + b % 10));
            a /= 10;
            b /= 10;
        }
        return Integer.parseInt(s.toString());
    }

    /**
     * task #10
     * Создайте функцию, которая удалит все повторяющиеся символы в слове,
     * переданном этой функции. Не просто последовательные символы, а символы,
     * повторяющиеся в любом месте строки.
     */
    public static String unrepeated(String s) {
        StringBuilder builder = new StringBuilder();
        s.chars()
                .mapToObj(c -> (char) c)
                .filter(new HashSet<>()::add)
                .forEach(builder::append);
        return builder.toString();
    }
}
