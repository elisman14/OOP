import java.util.*; 

class Prac3 {
    public static void main(String[] args) {
        Object[][] cities = new Object[][]{{"Manila", 13923452}, {"Kuala Lumpur", 7996830}, {"Jakarta", 10770487}};
        cities = millionsRounding(cities);
        for (int i = 0; i < cities.length; ++i)
            System.out.println(cities[i][0] + " " + cities[i][1]);

        // System.out.println(otherSides(3));
        double[] toPrint = otherSides(3);
        for (double i : toPrint)
            System.out.println(i);
        System.out.println(rps("scissors", "paper"));
        int[] numbers = new int[]{5, 9, 45, 6, 2, 7, 34, 8, 6, 90, 5, 243};
        System.out.println(warOfNumbers(numbers));
        System.out.println(reverseCase("sPoNtAnEoUs"));
        System.out.println(inatorInator("EvilClone"));
        System.out.println(doesBrickFit(1, 2, 2, 1, 1));
        System.out.println(totalDistance(55.5, 5.5, 5, false));
        int[] forMean = new int[]{3, 3, 3, 3, 3};
        System.out.println(mean(forMean));
        System.out.println(parityAnalysis(3));
    }

    /**
     * Учитывая массив городов и населения, верните массив, в котором все население
     * округлено до ближайшего миллиона.
     */
    public static Object[][] millionsRounding(Object[][] cities)
    {
        for (int i = 0; i < cities.length; ++i)
            cities[i][1] = (int) cities[i][1] / 1000000 * 1000000 + 1000000;
        return cities;
    }

    /**
     * Учитывая самую короткую сторону треугольника 30° на 60° на 90°, вы должны
     * найти другие 2 стороны (верните самую длинную сторону, сторону средней
     * длины).
     * Примечание:
     * - треугольники 30° на 60° на 90° всегда следуют этому правилу, скажем, самая короткая
     * длина стороны равна x единицам, гипотенуза будет равна 2 единицам, а другая сторона
     * будет равна x * root3 единицам.
     * - Результаты тестов округляются до 2 знаков после запятой.
     * - Верните результат в виде массива.
     */
    public static double[] otherSides(double a)
    {
        double[] sides = new double[2];
        sides[0] = a * 2;
        sides[1] = Math.sqrt(a * a * 3);
        return sides;
    }

    /**
     * Создайте функцию, имитирующую игру "камень, ножницы, бумага". Функция
     * принимает входные данные обоих игроков (камень, ножницы или бумага), первый
     * параметр от первого игрока, второй от второго игрока. Функция возвращает
     * результат как таковой:
     * "Игрок 1 выигрывает"
     * "Игрок 2 выигрывает"
     * "НИЧЬЯ" (если оба входа одинаковы)
     * Правила игры камень, ножницы, бумага, если не известны:
     * Оба игрока должны сказать одновременно "камень", "бумага" или "ножницы".
     * Камень бьет ножницы, бумага бьет камень, ножницы бьют бумагу.
     */
    public static String rps(String player1, String player2)
    {
        char p1 = player1.charAt(0);
        char p2 = player2.charAt(0);

        if (p1 == p2) { return "TIE"; }

        if (p1 == 'r') {
            switch (p2) {
                case 'p':
                    return "Player 2 wins";
                case 's':
                    return "Player 1 wins";
            }
        }
        if (p1 == 'p')
        {
            switch (p2) {
                case 'r':
                    return "Player 1 wins";
                case 's':
                    return "Player 2 wins";
            }
        }
        else {
            switch (p2) {
                case 'p':
                    return "Player 1 wins";
                case 'r':
                    return "Player 2 wins";
            }
        }

        return "";
    }

    /**
     * Идет великая война между четными и нечетными числами. Многие уже погибли в
     * этой войне, и ваша задача-положить этому конец. Вы должны определить, какая
     * группа суммируется больше: четная или нечетная. Выигрывает большая группа.
     * Создайте функцию, которая берет массив целых чисел, суммирует четные и нечетные
     * числа отдельно, а затем возвращает разницу между суммой четных и нечетных чисел.
     */
    public static int warOfNumbers(int[] input)
    {
        int even = 0, odd = 0;
        for (int i = 0; i < input.length; ++i) {
            if (input[i] % 2 == 0)
                even += input[i];
            else
                odd += input[i];
        }
        return even - odd;
    }

    /**
     * Учитывая строку, создайте функцию для обратного обращения. Все буквы в
     * нижнем регистре должны быть прописными, и наоборот.
     */
    public static String reverseCase(String input)
    {
        char[] str = input.toCharArray();
        for (int i = 0; i < str.length; ++i)
            str[i] = Character.isUpperCase(str[i]) ? Character.toLowerCase(str[i]) : Character.toUpperCase(str[i]);
        return new String(str);
    }

    /**
     * Создайте функцию, которая принимает строку из одного слова и выполняет
     * следующие действия:
     * Конкатенирует inator до конца, если слово заканчивается согласным, в противном
     * случае вместо него конкатенирует -inator
     * Добавляет длину слова исходного слова в конец, снабженный '000'
     */
    public static String inatorInator(String input) {
        if (input.substring(input.length() - 1).matches("[aeoiu]")) 
            return input + "-inator " + input.length() + "000"; 
        return input + "inator " + input.length() + "000";
    }

    /**
     * Напишите функцию, которая принимает три измерения кирпича: высоту(a),
     * ширину(b) и глубину(c) и возвращает true, если этот кирпич может поместиться в
     * отверстие с шириной(w) и высотой(h)
     */
    public static boolean doesBrickFit(int a, int b, int c, int w, int h)
    {
        if (w >= a && (h >=b || h >= c)) 
            return true;
        if (w >= b && (h >= a || h >= c))
            return true;
        if (w >= c && (h >= a || h >= b))
            return true;
        return false;
    }

    /**
     * Напишите функцию, которая принимает топливо (литры), расход топлива
     * (литры/100 км), пассажиров, кондиционер (логическое значение) и возвращает
     * максимальное расстояние, которое может проехать автомобиль.
     * топливо-это количество литров топлива в топливном баке.
     * Расход топлива-это базовый расход топлива на 100 км (только с водителем внутри).
     * Каждый дополнительный пассажир увеличивает базовый расход топлива на 5%.
     * Если кондиционер включен, то его общий (не базовый) расход топлива увеличивается на
     * 10%.
     */
    public static double totalDistance(double fuel, double fuelConsumption, int passengers, boolean conditioner) {
        if (conditioner)
            return fuel / (fuelConsumption * (1 + 0.05 * passengers) * 1.1) * 100;
        return fuel / (fuelConsumption * (1 + 0.05 * passengers)) * 100;
    }

    /**
     * Создайте функцию, которая принимает массив чисел и возвращает среднее
     * значение (average) всех этих чисел.
     */
    public static double mean(int[] input) {
        double sum = 0;
        int count = input.length;
        for (int i = 0; i < count; ++i) {
            sum += input[i];
        }
        return sum / count;
    }

    /**
     * Создайте функцию, которая принимает число в качестве входных данных и
     * возвращает true, если сумма его цифр имеет ту же четность, что и все число. В
     * противном случае верните false.
     */
    public static boolean parityAnalysis(int input) {
        int sum = 0, i = input;
        while(i > 0){
            sum += i % 10;
            i /= 10;
        }
        // System.out.println(sum);
        return sum % 2 == input % 2;
    }
}
