public class Palindrome {
    public static void main(String args[]) {
        try {
            for (int i = 0; i < args.length; ++i)
                System.out.println(args[i] + " isPalindrom -> " + isPalindrome(args[i]));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Введите строку аргументом");
        }
    }

    public static boolean isPalindrome(String s) {
        String reversedString;
        reversedString = "";
        for (int i = s.length() - 1; i >= 0; --i)
            reversedString += s.charAt(i);
        return s.equals(reversedString);
    }
}
