public class Primes {
    public static void main(String[] args) {
        // System.out.println(isPrime(23) + "\t" + (23 / 2));
        checkPrimes(100);
    }

    public static boolean isPrime(int n) {
        // if n is 2
        if (n == 2)
            return true;
        // else check dividers from 2 to n / 2
        for (int i = 2; i < n / 2; ++i)
        {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static void checkPrimes(int n) {
        for (int i = 2; i <= n; ++i) {
            if (isPrime(i))
                System.out.println(i);
        }
    }
}

