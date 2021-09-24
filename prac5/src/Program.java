import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static java.lang.String.format;

public class Program {
    public static void main(String[] args) {
        String[] totalPointsInput = new String[] {"trance", "recant"};
        int[] longestRunInput = new int[] {1, 2, 3, 5, 6, 7, 8, 9};
        int[] scores = new int[] {53, 79};

        System.out.println("FFFF " + "ABCD" + "\t" + sameLetterPattern("FFFF", "ABCD"));
        System.out.println(spiderToFly("A4", "C2"));
        System.out.println(digitsCount(12345));
        System.out.println(totalPoints(totalPointsInput, "recant"));
        System.out.println(longestRun(longestRunInput));
        System.out.println(takeDownAverage(scores));
        System.out.println(rearrange("Tesh3 th5e 1I lov2e way6 she7 j4ust i8s."));
        System.out.println(maxPossible(8732, 91255));
        System.out.println(timeDifference("America/New_York", "31-12-1970 01:40:00 PM", "Asia/Shanghai"));
        System.out.println(isNew(509));
    }

    /*
     * #1
     * Создайте функцию, которая возвращает true, если две строки имеют один и тот же
     * буквенный шаблон, и false в противном случае.
     */
    public static int getPattern(String input) {
        char[] inputArray = input.toLowerCase(Locale.ROOT).toCharArray();
        HashMap<Character, Integer> charMap = new HashMap<>();
        int patternCode = 0;

        for(int i = 0; i < input.length(); i++) {
            if(!charMap.containsKey(inputArray[i])) {
                charMap.put(inputArray[i], i);
            }
        }

        for(int i = 0; i < input.length(); ++i) {
            patternCode += (charMap.get(inputArray[i]) + 1) * pow(7, i);
        }

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
        char sr = spider.charAt(0), sl = spider.charAt(1), fr = fly.charAt(0), fl = fly.charAt(1);
        char midRing = abs(sr - fr) <= 2 || 6 <= abs(sr - fr) ? (char) min(sl,fl) : '0';

        StringBuilder answer = new StringBuilder();
        answer.append(sr).append(sl).append("-");

        while (sl > midRing) {
            answer.append(--sl == '0' ? "A0-" : "" + sr + sl + "-");
        }

        while (sr != fr) {
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
                if (sr < fr) {
                    sr++;
                } else {
                    sr--;
                }
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
            if (n < 10) {
                n = floorDiv(n, 10);
                return digitsCount(n);
            } else {
                n = floorDiv(n, 10);
                return 1 + digitsCount(n);
            }

        } else { return 1; }
    }

    /*
     * В игроки пытаются набрать очки, формируя слова, используя буквы из 6- буквенного
     * скремблированного слова. Они выигрывают раунд, если им удается успешно расшифровать слово из 6 букв.
     * Создайте функцию, которая принимает в массив уже угаданных слов расшифрованное 6- буквенное
     * слово и возвращает общее количество очков, набранных игроком в определенном раунде,
     * используя следующую рубрику:
     * 3-буквенные слова-это 1 очко
     * 4-буквенные слова-это 2 очка
     * 5-буквенные слова-это 3 очка
     * 6-буквенные слова-это 4 очка + 50 пт бонуса (за расшифровку слова)
     * Помните, что недопустимые слова (слова, которые не могут быть сформированы из 6- буквенных
     * расшифрованных слов) считаются 0 очками.
     */
    public static int totalPoints(String[] words, String answer) {
        int points = 0;

        Pattern regExPatternForThreeLetters = Pattern.compile(format("\\b([%s])(?!\\1)([%s])(?!\\1)(?!\\2)([%s])" +
                "(?!\\1)(?!\\2)(?!\\3)\\b", answer, answer, answer));
        Pattern regExPatternForFourLetters = Pattern.compile(format("\\b([%s])(?!\\1)([%s])(?!\\1)(?!\\2)([%s])" +
                "(?!\\1)(?!\\2)(?!\\3)([%s])(?!\1)(?!\2)(?!\3)(?!\4)\\b", answer, answer, answer, answer));
        Pattern regExPatternForFiveLetters = Pattern.compile(format("\\b([%s])(?!\\1)([%s])(?!\\1)(?!\\2)([%s])" +
                "(?!\\1)(?!\\2)(?!\\3)([%s])(?!\1)(?!\2)(?!\3)(?!\4)([%s])(?!\\1)(?!\\2)(?!\\3)(?!\\4)(?!\\5)\\b",
                answer, answer, answer, answer, answer));
        Pattern regExPatternForSixLetters = Pattern.compile(format("\\b([%s])(?!\\1)([%s])(?!\\1)(?!\\2)([%s])" +
                "(?!\\1)(?!\\2)(?!\\3)([%s])(?!\1)(?!\2)(?!\3)(?!\4)([%s])(?!\\1)(?!\\2)(?!\\3)(?!\\4)(?!\\5)" +
                "([%s])(?!\\1)(?!\\2)(?!\\3)(?!\\4)(?!\\5)(?!\\6)\\b", answer, answer, answer, answer, answer, answer));

        for (String word : words) {
            if (regExPatternForThreeLetters.matcher(word).find()) {
                points++;
            }
            else if (regExPatternForFourLetters.matcher(word).find()) {
                points += 2;
            }
            else if (regExPatternForFiveLetters.matcher(word).find()) {
                points += 3;
            }
            else if (regExPatternForSixLetters.matcher(word).find()) {
                points += 54;
            }

        }

        return points;
    }

    /*
     * Последовательный прогон-это список соседних последовательных целых чисел. Этот список может
     * быть как увеличивающимся, так и уменьшающимся. Создайте функцию, которая принимает массив
     * чисел и возвращает длину самого длинного последовательного запуска.
     */
    public static int longestRun(int[] array) {
        int start = 0, end = 0, maxLength = 0;
        boolean rise = array[0] < array[1];


        for (int i = 0; i < array.length - 1; ++i) {
            if ((array[i] + 1 == array[i+1] && rise)) {
                end++;
            }
            else if (array[i] - 1 == array[i+1] && !rise) {
                end++;
            }
            else {
                start = i;
                end = start;
            }
            maxLength = max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    /*
     * Какой процент вы можете набрать на тесте, который в одиночку снижает средний балл по классу на 5%?
     * Учитывая массив оценок ваших одноклассников, создайте функцию, которая возвращает ответ.
     * Округлите до ближайшего процента.
     */
    public static int takeDownAverage(int[] scores) {
        int sumOfScores = Arrays.stream(scores).sum();
        int mean = (sumOfScores / scores.length) - 5;
        return (mean * (scores.length + 1)) - sumOfScores;
    }

    /*
     *  Учитывая предложение с числами, представляющими расположение слова,
     *  встроенного в каждое слово, верните отсортированное предложение.
     */
    public static String rearrange(String input) {
        Map<Integer, String> wordNumbers = new TreeMap<>();
        StringBuilder out = new StringBuilder();
        Pattern pattern = Pattern.compile("\\b.*(\\d).*\\b");
        String index;

        for (String word : input.split(" ")) {

            Matcher matcher = pattern.matcher(word);

            if (matcher.find()) {
                index = matcher.group(1);
                wordNumbers.put(Integer.parseInt(index), word.replace(index, ""));
            }
        }

        wordNumbers.forEach((integer, s) -> out.append(s).append(" "));

       return out.toString();
    }

    /*
     * Напишите функцию, которая делает первое число как можно больше,
     * меняя его цифры на цифры во втором числе.
     */
    public static int maxPossible(int a, int b) {
        Queue<Short> numbersOfB = new PriorityQueue<>(Collections.reverseOrder());
        char[] aChars = String.valueOf(a).toCharArray();
        StringBuilder out = new StringBuilder();

        while(b != 0) {
            numbersOfB.add((short) (b % 10));
            b /= 10;
        }

        for (int i = 0; i < aChars.length; ++i) {
            if (Character.getNumericValue(aChars[i]) < numbersOfB.element()) {
                aChars[i] = numbersOfB.poll().toString().charAt(0);
            }
            out.append(aChars[i]);
        }

        return Integer.parseInt(out.toString());
    }

    /**
     * В этой задаче цель состоит в том, чтобы вычислить, сколько времени сейчас в двух разных городах.
     * Вам дается строка cityA и связанная с ней строка timestamp (time in cityA) с датой,
     * отформатированной в полной нотации США, как в этом примере:
     * "July 21, 1983 23:01"
     * Вы должны вернуть новую метку времени с датой и соответствующим временем в cityB,
     * отформатированную как в этом примере:
     * "1983-7-22 23:01"
     * Список данных городов и их смещения по Гринвичу (среднее время по Гринвичу) приведены в таблице ниже.
     */
    public static String timeDifference(String cityA, String timeA, String cityB) {
        String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";

        LocalDateTime ldt = LocalDateTime.parse(timeA, DateTimeFormatter.ofPattern(DATE_FORMAT));

        ZoneId singaporeZoneId = ZoneId.of(cityA);

        ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);

        ZoneId newYokZoneId = ZoneId.of(cityB);

        ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newYokZoneId);

        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);

        return format.format(nyDateTime);
    }

    /**
     * Новое число-это число, которое не является перестановкой любого меньшего числа.
     * 869-это не новое число, потому что это просто перестановка меньших чисел, 689 и 698.
     * 509-это новое число, потому что оно не может быть образовано перестановкой любого
     * меньшего числа (ведущие нули не допускаются).
     * Напишите функцию, которая принимает неотрицательное целое число и возвращает true,
     * если целое число является новым числом, и false, если это не так.
     */
    public static boolean isNew(int n) {
        char[] numbers = String.valueOf(n).toCharArray();
        short mainNumber = Short.parseShort(Character.toString(numbers[0]));
        short inLoop;

        for (int i = 1; i < numbers.length; ++i) {
            inLoop = Short.parseShort(Character.toString(numbers[i]));
            if (inLoop < mainNumber && inLoop != 0) {
                return false;
            }
        }

        return true;
    }
}
