package org.squadstack.pms.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InputProcessor {
    private InputProcessor() {
    }

    public static void process() {
        String filePath = System.getProperty("input.file.path");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> stringList = Arrays.asList(line.split(" "));
                ProcessorFactory.getProcessor(stringList.get(0)).process(line);
            }
        } catch (IOException e) {
            System.out.println("Error while reading the input file");
        }
    }
}
