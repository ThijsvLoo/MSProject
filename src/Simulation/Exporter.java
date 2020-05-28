package Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Exporter {

    /** Lists of waiting time for data analysis */
    private static final ArrayList<Double> consumer = new ArrayList<>();
    private static final ArrayList<Double> corporate = new ArrayList<>();


    /**
     *	Method to append to append to list of waiting times
     *	@param c	The callers waiting time
     */
    public static void addConsumerData(double c) {
        consumer.add(c);
    }
    public static void addCorporateData(double c) {
        corporate.add(c);
    }


    /* Creates Excel file and adds the waiting times in the excel file */
    public static void writeData() {
        File output = new File("CorporateData.csv");
        try {
            StringBuilder sb = new StringBuilder();
            FileWriter fw = new FileWriter(output, true);
            corporate.stream().forEach(c -> sb.append(c + ", "));
            sb.append("\n");
            fw.write(sb.toString());
            fw.close();
            corporate.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File output2 = new File("ConsumerData.csv");
        try {
            StringBuilder sb = new StringBuilder();
            FileWriter fw = new FileWriter(output2, true);
            consumer.stream().forEach(c -> sb.append(c + ", "));
            sb.append("\n");
            fw.write(sb.toString());
            fw.close();
            consumer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
