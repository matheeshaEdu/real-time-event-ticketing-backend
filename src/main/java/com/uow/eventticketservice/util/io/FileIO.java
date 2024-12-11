package com.uow.eventticketservice.util.io;

import java.io.*;

public class FileIO {


    // Generalized method to write a string to a file
    public void writeToFile(String content, String filePath, boolean append) throws IOException {
        // Use try-with-resources to ensure BufferedWriter is closed after writing
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, append))) {
            bufferedWriter.write(content);
        }
    }

    // Generalized method to read any text file into a string
    public String readFromFile(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        // Try-with-resources to ensure file is closed after reading
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line and append it to the StringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator()); // Preserve line breaks
            }
        }

        return stringBuilder.toString();
    }
}

