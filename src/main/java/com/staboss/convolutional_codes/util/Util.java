package com.staboss.convolutional_codes.util;

import java.util.Arrays;

import static java.lang.System.out;

public class Util {

    private static final String delimiter = "-------------------------------------------------------------------";

    /**
     * Помощь в использовании программы
     */
    public static void usage(boolean error) {
        if (error) {
            println((char) 27 + "[01;31m\n********************" +
                    "\n! INPUT DATA ERROR !\n********************\n\n"
                    + (char) 27 + "[00;00mPlease, check:\n" +
                    "1) the correctness of the entered polynomials\n" +
                    "2) the length of the information sequence\n" +
                    "3) the format of integers\n");
        }

        println((char) 27 + "[01;31m" + delimiter +
                "\nUsage : java -jar ConvolutionalCoding-1.0.jar [-u|-s] [-r|-g] G1 G2\n" + delimiter + "\n" +
                "\t-u : unsystematic coding\n" +
                "\t-s : systematic coding\n" +
                "\t-g : generating sequence\n" +
                "\t-r : read sequence from console\n" +
                "\t K : the length of information sequence [-g flag]\n" +
                "\t P : the probability to invert bit [-g flag]\n" +
                "\t I : the number of iterations [-g flag]\n" +
                "\t G : the polynomial (from 1 to x^6)\n" + delimiter);
    }

    /**
     * Переводит строковое представление полинома в двоичное
     *
     * @param g   полином
     * @param pow максимальная степень полинома
     * @return двоичное представление
     */
    public static String convertPolynomial(String g, int pow) {
        //  x+x^2+x^4 -> 01101
        StringBuilder gBit = new StringBuilder();

        //  убираем все ненужные символы
        String[] x = g.replaceAll("1", "x^0")
                .replaceAll("x\\+", "x^1+")
                .replaceAll("x\\^", "")
                .split("\\+");

        //  переводим оставшиеся степени в числа
        int[] xPow = Arrays.stream(x).mapToInt(Integer::parseInt).toArray();

        //  счетчик xPow
        int count = 0;

        //  проходимся по каждой степени и сравниваем с полиномом
        for (int i = 0; i <= pow; i++) {
            if (count == xPow.length) {
                gBit.append(0);
            } else if (xPow[count] == i) {
                gBit.append(1);
                count++;
            } else {
                gBit.append(0);
            }
        }

        return gBit.toString();
    }

    /**
     * Разбивает последовательность на массив элементов длинной в два символа
     *
     * @param vector информационная последовательность
     * @return массив последовательности
     */
    public static String[] getVectorValues(String vector) {
        String[] vectorValues = new String[(vector.length() >> 1)];

        //  счетчик для индексов массива
        int k = 0;

        //  проход по массиву через каждые 2 символа для нахождения новой подстроки
        for (int i = 0; i < vector.length() - 1; i = i + 2) {
            vectorValues[k++] = vector.substring(i, i + 2);
        }

        return vectorValues;
    }

    /**
     * Выводит строку в консоль
     *
     * @param str строка
     */
    public static void print(String str) {
        out.print(str);
    }

    /**
     * Выводит строку в консоль
     *
     * @param str строка
     */
    public static void println(String str) {
        out.println(str);
    }
}
