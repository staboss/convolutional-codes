package com.staboss.convolutional_codes.plot;

import com.staboss.convolutional_codes.coder.Coder;
import com.staboss.convolutional_codes.coder.NonSystematicCoder;
import com.staboss.convolutional_codes.decoder.Decoder;
import com.staboss.convolutional_codes.model.State;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.staboss.convolutional_codes.util.Generator.*;
import static com.staboss.convolutional_codes.util.Util.getVectorValues;
import static java.lang.System.out;

public class DependencyGraph {

    private static volatile Map<String, State> states;
    private static volatile String firstState;

    private static Coder coder;

    private static int K;
    private static int I;
    private static int L;

    private static double STEP;

    private static volatile double[] genPRO;

    private static volatile double[] valPRO;
    private static volatile double[] valFER;
    private static volatile double[] valBER;

    static {
        //String g1 = "1101101";    //  1+x+x^3+x^4+x^6
        //String g2 = "1001111";    //  1+x^3+x^4+x^5+x^6

        //firstState = "000000";

        String g1 = "11001";    //  1+x+x^4
        String g2 = "10111";    //  1+x^2+x^3+x^4

        firstState = "0000";

        int size = 20;
        genPRO = new double[size];
        valPRO = new double[size];
        valFER = new double[size];
        valBER = new double[size];

        K = 256;
        I = 100000;
        L = (2 * (K + firstState.length())) * I;

        STEP = 0.005;

        try {
            coder = new NonSystematicCoder(g1, g2);
            states = coder.getStates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        double probability = 0.0;
        for (int i = 0; i < genPRO.length; i++) {
            genPRO[i] = probability;
            probability += STEP;
        }

        //Arrays.stream(genPRO).forEach(g -> out.print(g + " "));
        //out.println();

        //  создаем новый пул потоков, где одновременно может выполняться 4 потока
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        //  добавляем в пул потоков 4 новых потока
        for (int i = 0; i <= 15; i += 5) {
            executorService.submit(new Worker(states, firstState, i, i + 1, i + 2, i + 3, i + 4));
        }

        //  запускаем пул потоков
        long start = System.currentTimeMillis();
        executorService.shutdown();

        //  выделяем время на выполнение потоков
        executorService.awaitTermination(150, TimeUnit.MINUTES);
        long finish = System.currentTimeMillis();

        out.printf("\nTotal time: %d min\n", (int) ((double) (finish - start) / 60000.0));

        XYChart fer = QuickChart.getChart("", "P, %", "FER", "FER(P)", valPRO, valFER);
        XYChart ber = QuickChart.getChart("", "P, %", "BER", "BER(P)", valPRO, valBER);


        String plotFER, plotBER, valuesFER, valuesBER;

        if (firstState.length() == 6) {
            plotFER = "FER_6";
            plotBER = "BER_6";
            valuesFER = "FER_6.csv";
            valuesBER = "BER_6.csv";
        } else if (firstState.length() == 4) {
            plotFER = "FER_4";
            plotBER = "BER_4";
            valuesFER = "FER_4.csv";
            valuesBER = "BER_4.csv";
        } else {
            plotFER = "PlotFER";
            plotBER = "PlotBER";
            valuesFER = "ValuesFER.csv";
            valuesBER = "ValuesBER.csv";
        }

        BitmapEncoder.saveBitmap(fer, plotFER, BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(ber, plotBER, BitmapEncoder.BitmapFormat.PNG);

        CsvWriter.writeCsvFile(valuesFER, valPRO, valFER);
        CsvWriter.writeCsvFile(valuesBER, valPRO, valBER);
    }

    private static class Worker extends Thread {

        private static int id = 0;
        private int workerId;

        private Map<String, com.staboss.convolutional_codes.model.State> states;
        private String firstState;

        private int[] positions;

        Worker(Map<String, com.staboss.convolutional_codes.model.State> states, String firstState, int... positions) {
            this.states = states;
            this.firstState = firstState;
            this.positions = positions;
            this.workerId = ++id;
        }

        @Override
        public void run() {

            Decoder decoder;

            int[] iVector;
            int[] iCoded;

            String coded;
            String decoded;

            for (int i = 0; i <= positions.length - 1; i++) {

                int fErrors = 0;
                int bErrors = 0;

                for (int j = 1; j <= I; j++) {

                    iVector = getVector(K);                             //  сгенерировали вектор
                    coded = coder.encode(makeStringVector(iVector));    //  закодировали
                    iCoded = makeIntVector(coded);                      //  получили массив int
                    invert(iCoded, genPRO[positions[i]]);               //  инвертировали каждый бит с вероятностью P

                    try {
                        decoder = new Decoder(states, firstState, getVectorValues(makeStringVector(iCoded)));
                        decoded = decoder.getDecodedSequenceString();

                        if (!coded.equals(decoded)) {
                            fErrors++;
                            bErrors += decoder.getErrors();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                valPRO[positions[i]] = genPRO[positions[i]];
                valFER[positions[i]] = (double) fErrors / (double) I;
                valBER[positions[i]] = (double) bErrors / (double) L;
                out.printf("WORKER-%d : %2d completed\n", workerId, positions[i]);
            }
        }
    }
}
