/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cz.itnetwork.prvocisla;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Prvocisla {

    public static boolean[] sieveOfEratosthenes(int maxNumber) {
        boolean[] primes = new boolean[maxNumber + 1];
        primes[0] = false;
        primes[1] = false;

        for (int i = 2; i < maxNumber + 1; i++) { 
            primes[i] = true;
        }

        for (int p = 2; p * p < maxNumber + 1; p++) { 
            if (primes[p]) {
                for (int i = p * p; i < maxNumber + 1; i += p) { 
                    primes[i] = false;
                }
            }
        }

        return primes;
    }

    public static void main(String[] args) {
        
        String inputFile = "vzorek_dat.xlsx";

        try (InputStream is = Prvocisla.class.getClassLoader().getResourceAsStream(inputFile);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            List<Integer> numbers = new ArrayList<>();
            int maxNumber = 0;

            // Najdi největší číslo a přidej validní čísla do seznamu
            for (Row row : sheet) {
                Cell cell = row.getCell(1); // Sloupec B
                if (cell != null) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    try {
                        int number = Integer.parseInt(cellValue);
                        if (number >= 0) { // Ignoruj negativní čísla
                            numbers.add(number);
                            if (number > maxNumber) {
                                maxNumber = number;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignoruj nevalidní data
                    }
                }
            }

            boolean[] primes = sieveOfEratosthenes(maxNumber);

            // Vypiš prvočísla
            numbers.stream()
                    .filter(number -> number < primes.length && primes[number])
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
