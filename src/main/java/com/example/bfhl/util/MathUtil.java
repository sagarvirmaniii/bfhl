package com.example.bfhl.util;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    // Fibonacci series
    public static List<Integer> fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid fibonacci input");
        }

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (i == 0) {
                result.add(0);
            } else if (i == 1) {
                result.add(1);
            } else {
                result.add(result.get(i - 1) + result.get(i - 2));
            }
        }
        return result;
    }

    // Prime check
    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    // GCD / HCF
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // LCM
    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }
}
