package com.staboss.convolutional_codes.plot;

import java.io.FileWriter;

class CsvWriter {

    //  Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ", ";
    private static final String NEW_LINE_SEPARATOR = "\n";

    static void writeCsvFile(String fileName, double[] p, double[] e) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            //  Write a new data to the CSV file
            for (int i = 0; i < p.length; i++) {
                fileWriter.append(String.valueOf(p[i]));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(e[i]));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
