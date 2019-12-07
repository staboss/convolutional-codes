package com.staboss.convolutional_codes.plot;

import java.io.FileWriter;

public class CsvWriter {

    //  Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //  CSV file header
    private static final String FILE_HEADER_FER = "P,FER";
    private static final String FILE_HEADER_BER = "P,BER";

    public static void writeCsvFile(String fileName, String errorType, double[] p, double[] e) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {

            //  Write the CSV file header
            switch (errorType) {
                case "FER":
                    fileWriter.append(FILE_HEADER_FER);
                    break;
                case "BER":
                    fileWriter.append(FILE_HEADER_BER);
                    break;
                default:
                    throw new Exception();
            }

            //  Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

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
