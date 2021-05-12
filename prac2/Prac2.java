import java.util.*; 

class Prac2 {
    public static void main(String[] args) {
        System.out.println("5, 46 -> " + firstTask(5, 46));
        System.out.println("Seymour Butts -> " + secondTask("Seymour Butts"));
        System.out.println("100, 75 -> " + thirdTask(100, 75));
        int[] s = new int[]{44, 32, 86, 19};
        System.out.println("[44, 32, 86, 19] -> " + forthTask(s));
        System.out.println("3, 4, 1 -> " + fifthTask(3, 4, 1));
        System.out.println("Edabit is really helpful! -> " + sixthTask("Edabit is really helpful!"));
        System.out.println("1, 5, 9 -> " + seventhTask(1, 5, 9));
        System.out.println("zzoo -> " + eighthTask("zzoo"));
        System.out.print("This goes boom!!! -> ");
        ninethTask("This goes boom!!!");
        System.out.println("EdAbIt, EDABIT -> " + tenthTask("EdAbIt", "EDABIT"));

    }

    /**
     * Теша шел по прямой улице, по обеим сторонам которой стояло ровно n
     * одинаковых домов.Номера домов на улице выглядят так:
     * Она заметила, что четные дома увеличиваются справа, а нечетные уменьшаются слева.
     * Создайте функцию, которая принимает номер дома и длину улицы n и возвращает номер
     * дома на противоположной стороне.
     */
    public static int firstTask(int h, int s) {
        return h % 2 == 0 ? s * 2 - h + 1 : s * 2 - h / 2 * 2;
    }

    /**
     * Создайте метод, который принимает строку (имя и фамилию человека) и 
     * возвращает строку с заменой имени и фамилии.
     */
    public static String secondTask(String input)
    {
        String splitedInput[] = input.split(" ");
        return splitedInput[1] + " " + splitedInput[0];
        // return String.join(" ", Collections.reverse(input.split(" ")));
    }

    /**
     * Создайте функцию, которая принимает два аргумента: исходную цену и процент
     * скидки в виде целых чисел и возвращает конечную цену после скидки.
     */
    public static double thirdTask(int price, int discount)
    {
        return price * (100 - discount) / 100;
    }

    /**
     * Создайте функцию, которая принимает массив и возвращает разницу между
     * наибольшим и наименьшим числами.
     */
    public static int forthTask(int[] arr)
    {
        return Arrays.stream(arr).max().getAsInt() - Arrays.stream(arr).min().getAsInt();
    }

    /**
     * Создайте функцию, которая принимает три целочисленных аргумента (a, b, c) и
     * возвращает количество целых чисел, имеющих одинаковое значение.
     */
    public static int fifthTask(int a, int b, int c)
    {
        if (a == b && a == c)
            return 3;
        else if (a == b || a == c || c == b)
            return 2;
        else
            return 0;
    }

    /**
     * Создайте метод, который принимает строку в качестве аргумента и возвращает ее в
     * обратном порядке.
     */
    public static String sixthTask(String input)
    {
        StringBuilder output = new StringBuilder(input).reverse();
        return output.toString();
    }

    /**
     * Вы наняли трех программистов и (надеюсь) платите им. Создайте функцию,
     * которая принимает три числа (почасовая заработная плата каждого программиста)
     * и возвращает разницу между самым высокооплачиваемым программистом и самым
     * низкооплачиваемым.
     */
    public static int seventhTask(int a, int b, int c)
    {
        return Math.max(a, Math.max(b, c)) - Math.min(a, Math.min(b, c)); 
    }

    /**
     * Создайте функцию, которая принимает строку, проверяет, имеет ли она одинаковое
     * количество x и o и возвращает либо true, либо false.
     * Правила:
     * - Возвращает логическое значение (true или false).
     * - Верните true, если количество x и o одинаковы.
     * - Верните false, если они не одинаковы.
     * - Строка может содержать любой символ.
     * - Если "x" и "o" отсутствуют в строке, верните true
     */
    public static boolean eighthTask(String input)
    {
        input = input.toLowerCase();

        if (input.contains("x") == false && input.contains("o") == false) { return true; }

        long oCount = input.chars().filter(ch -> ch == 'o').count();
        long xCount = input.chars().filter(ch -> ch == 'x').count();

        return oCount == xCount;
    }

    /**
     * Напишите функцию, которая находит слово "бомба" в данной строке. Ответьте
     * "ПРИГНИСЬ!", если найдешь, в противном случае:"Расслабься, бомбы нет".
     */
    public static void ninethTask(String input)
    {
        if (input.toLowerCase().contains("bomb"))
            System.out.println("DUCK!");
        else
            System.out.println("Relax, there's no bomb.");
    }

    /**
     * Возвращает true, если сумма значений ASCII первой строки совпадает с суммой
     * значений ASCII второй строки, в противном случае возвращает false.
     */
    public static boolean tenthTask(String a, String b)
    {
        return a.chars().sum() == b.chars().sum();
    }

}
