package com.staboss.convolutional_codes;

import com.staboss.convolutional_codes.coder.Coder;
import com.staboss.convolutional_codes.coder.SystematicCoder;
import com.staboss.convolutional_codes.coder.NonSystematicCoder;
import com.staboss.convolutional_codes.decoder.Decoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.staboss.convolutional_codes.util.Generator.*;
import static com.staboss.convolutional_codes.util.Util.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * Клиент
 */
public class Main {

    private static String firstState;

    public static void main(String[] args) {

        if (args[0].equals("-h")) {
            usage(false);
            return;
        }

        if (args.length != 4) {
            usage(false);
            return;
        }

        //  систематический | несистематический кодер
        Coder coder;

        //  декодер Витерби
        Decoder decoder;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            int gPow1 = parseInt(args[2].substring(args[2].length() - 1));
            int gPow2 = parseInt(args[3].substring(args[3].length() - 1));

            int gPow = Math.max(gPow1, gPow2);

            String g1 = convertPolynomial(args[2], gPow);
            String g2 = convertPolynomial(args[3], gPow);

            setStateFormat(gPow);

            coder = getCoder(args[0], g1, g2);

            if (args[1].equals("-r")) {
                print("Enter the sequence: ");
                String vector = br.readLine();
                decoder = new Decoder(coder.getStates(), firstState, getVectorValues(vector));
                println(format((char) 27 + "[01;32mDECODE: %s", decoder.getDecodedSequenceString()));
                println(format("ERRORS: %d", decoder.getErrors()));
            } else if (args[1].equals("-g")) {
                //  количество итераций
                print("I = ");
                int I = parseInt(br.readLine());
                if (I <= 0) throw new Exception();

                //  длина информационного вектора
                print("K = ");
                int K = parseInt(br.readLine());

                //  вероятность инвертировать бит в кодовом слове
                print("P = ");
                double P = Double.parseDouble(br.readLine());

                int[] iVector;
                int[] iCoded;

                String encoded;
                String decoded;

                int errors = 0;

                long startTime = System.currentTimeMillis();
                for (int i = 1; i <= I; i++) {
                    //  сгенерировали вектор
                    iVector = getVector(K);

                    //  закодировали
                    encoded = coder.encode(makeStringVector(iVector));

                    //  получили массив int
                    iCoded = makeIntVector(encoded);

                    //  инвертировали каждый бит с вероятностью P
                    invert(iCoded, P);

                    decoder = new Decoder(coder.getStates(), firstState, getVectorValues(makeStringVector(iCoded)));
                    decoded = decoder.getDecodedSequenceString();

                    if (!encoded.equals(decoded)) errors++;
                }
                long finishTime = System.currentTimeMillis();

                println(format("\nFOR %d ITERATIONS : %d ERRORS", I, errors));
                println((char) 27 + "[01;32mFER = ERRORS / ITERATIONS = " + errors + " / " + I + " = " + ((double) errors / I));
                println((char) 27 + "[00;00m\nTotal time: " + ((finishTime - startTime) / 1000.0) + " s");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            usage(true);
        }
    }

    /**
     * Устанавливает первоначальное состояние регистра
     *
     * @param gPow максимальная степень полинома
     * @throws Exception если передан недопустимы формат
     */
    private static void setStateFormat(int gPow) throws Exception {
        String format = (gPow == 2) ? "%2s" : (gPow == 3) ? "%3s" : (gPow == 4) ? "%4s" : (gPow == 5) ? "%5s" : (gPow == 6) ? "%6s" : null;
        if (format == null) throw new Exception();
        firstState = format(format, Integer.toBinaryString(0)).replaceAll(" ", "0");
    }

    /**
     * Возвращает указанный кодер
     *
     * @param type тип кодера
     * @param g1   полином 1
     * @param g2   полином 2
     * @return нужный кодер
     * @throws Exception если ошибка входных данных
     */
    private static Coder getCoder(String type, String g1, String g2) throws Exception {
        return type.equals("-s") ? new SystematicCoder(g1, g2) : new NonSystematicCoder(g1, g2);
    }
}
