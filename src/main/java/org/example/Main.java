package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Usage: java -jar example-jar.jar <file>");
        }

        if (args.length == 1 && !args[0].equals("-list")) {


            Extractor extractor = new Extractor();

            String s = extractor.readFileContent(args[0]);
            extractor.ExtractIngredieant(s);
            extractor.ExtractCookware(s);
            extractor.ExtractTime(s);
            extractor.print();



        } else if (args[0].equals("-list") && args.length >= 3) {

            Extractor extractor = new Extractor();

            for (String filepath : args) {
                File file = new File(filepath);

                if (filepath.equals("-list")) {
                    continue;
                }

                if (!file.exists() && !filepath.equals("-list")) {
                    System.out.println("File does not exist: " + file.getAbsolutePath());
                    return;
                }

                String s = extractor.readFileContent(filepath);
                extractor.ExtractIngredieant(s);

            }

            extractor.print();
        }

    }
}