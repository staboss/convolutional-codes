package com.staboss.coding.util;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Генератор случайной информационной последовательности из 0 и 1
 */
public class Generator {

    /**
     * С заданной длиной k возвращает последовательность из 0 и 1
     *
     * @param k длина информационной последовательности
     * @return информационная последовательность
     */
    public static int[] getVector(int k) {
        Random random = new Random();
        return IntStream.range(0, k)
                .map(i -> random.nextInt(2))
                .toArray();
    }

    /**
     * Инвертирует каждый бит в кодовом слове с заданной вероятностью
     *
     * @param vector кодовое слово
     * @param p      вероятность инвертировать бит
     */
    public static void invert(int[] vector, double p) {
        Random random = new Random();
        IntStream.range(0, vector.length)
                .filter(i -> random.nextDouble() < p)
                .forEach(i -> vector[i] ^= 1);
    }

    /**
     * Переводит кодовое слово из символьного представления в числовое
     *
     * @param vector кодовое слово
     * @return числовое представление
     */
    public static int[] makeIntVector(String vector) {
        return IntStream.range(0, vector.length())
                .map(i -> Integer.parseInt(String.valueOf(vector.charAt(i))))
                .toArray();
    }

    /**
     * Переводит кодовое слово из числового представления в символьное
     *
     * @param vector кодовое слово
     * @return символьное представление
     */
    public static String makeStringVector(int[] vector) {
        return Arrays.stream(vector)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
