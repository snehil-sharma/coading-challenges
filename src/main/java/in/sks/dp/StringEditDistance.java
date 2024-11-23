package in.sks.dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringEditDistance {
    public static void main(String[] args) {
        // Get the resource file using ClassLoader
        URL resource = StringEditDistance.class.getClassLoader()
                .getResource("string_edit_distance_test_cases.csv");
        if (resource == null) {
            System.err.println("File not found: string_edit_distance_test_cases.csv");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            String line;
            int testCaseNo = 1;

            System.out.printf("%-10s %-10s %-10s %-10s %-15s%n", "Test Case", "Expected", "Actual", "Pass?", "Duration");
            System.out.println("------------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    testCaseNo++;
                    continue;
                } // Skip invalid lines

                String s1 = parts[0].trim();
                String s2 = parts[1].trim();
                int expected = Integer.parseInt(parts[2].trim());

                // Measure time
                long startTime = System.nanoTime();
                int actual = self(s1, s2);
                long endTime = System.nanoTime();

                long duration = endTime - startTime;
                boolean pass = (actual == expected);
                String resultSymbol = pass ? "\u2714" : "\u2716";

                System.out.printf("%-10d %-10d %-10d %-10s %-15d%n",
                        testCaseNo, expected, actual, resultSymbol, duration);

                testCaseNo++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    static int self(String s1, String s2) {
        List<Integer> s1List = new ArrayList<>();
        List<Integer> s2List = new ArrayList<>();

        for (int i = 0; i < s1.length(); i++) s1List.add((int) s1.charAt(i));
        for (int i = 0; i < s2.length(); i++) s2List.add((int) s2.charAt(i));

        Collections.sort(s1List);
        Collections.sort(s2List);

        int add = 0;
        int remove = 0;
        int replace = 0;

        int s1i = 0, s2i = 0;
        while (s1i < s1List.size() && s2i < s2List.size()) {
            if (s1List.get(s1i) == s2List.get(s2i)) {
                s1i++;
                s2i++;
                continue;
            };

            if (s1List.get(s1i) > s2List.get(s2i)) {
                add++;
                s2i++;
                continue;
            }

            while (s1List.get(s1i) < s2List.get(s2i)) {
                remove++;
                s1i++;
            }
            if (s1List.get(s1i) == s2List.get(s2i)) {
                s1i++;
                s2i++;
                continue;
            }
            if (s1List.get(s1i) > s2List.get(s2i)) {
                remove--;
                replace++;
                s2i++;
            }
        }

        return add + remove + replace;
    }
}
