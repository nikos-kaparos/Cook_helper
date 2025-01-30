package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    private String filePath;
    IngredientList IngrList = new IngredientList(50);
    CookwareList CookwareList = new CookwareList(50);
    Time timeCurr = new Time();
    ArrayList<Steps> steps = new ArrayList<>();
    int counter = 0;

    public Extractor () {
        this.steps = steps;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String readFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();

        return content.toString();
    }

    public void ExtractIngredieant(String content) throws IOException {


        String pattern1 = "@([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{([\\d]{1,})?+\\%?+([α-ωΑ-Ωά-ώΆ-Ώa-zA-z]{1,})?+\\}|\\@([α-ωΑ-Ωά-ώΆ-Ώ]{1,})";
        Pattern p1 = Pattern.compile(pattern1);
        Matcher m1 = p1.matcher(content);

        // Filling The list with the matching items
        while (m1.find()) {

            // Print out the ingredient and quantity
            if (m1.group(1) != null) {
                Ingredient curr = new Ingredient();
                curr.setName(m1.group(1));
                curr.setQuantity(m1.group(2));
                curr.setUnit(m1.group(3));
                if (IngrList.nameExists(curr.getName()) && m1.group(2) != null) {
                    int j = IngrList.getPosition(curr.getName());
                    int quan = Integer.parseInt(curr.getQuantity()) + Integer.parseInt(IngrList.getItem(j).getQuantity());
                    String quantity = Integer.toString(quan);
                    IngrList.getItem(j).setQuantity(quantity);
                } else if (IngrList.nameExists(curr.getName())) {
                    continue;
                } else {
                    IngrList.addItem(curr, counter);
                    counter++;
                }
            }

            if (m1.group(4) != null) {
                Ingredient curr = new Ingredient();
                curr.setName(m1.group(4));
                if (!IngrList.nameExists(curr.getName())) {
                    IngrList.addItem(curr, counter);
                    counter++;
                }
            }

        }

    }

    public void ExtractCookware (String content) throws IOException {

        String pattern2 = "#([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{+\\}|\\#([α-ωΑ-Ωά-ώΆ-Ώ]{1,})+\\{?+\\}?";
        Pattern p2 = Pattern.compile(pattern2);
        Matcher m2 = p2.matcher(content);

        int j = 0;

        while (m2.find()) {
            if (m2.group(1) != null) {
                Cookware curr = new Cookware();
                curr.setName(m2.group(1));

                if (CookwareList.exists(curr.getName())) {
                    continue;
                } else {
                    CookwareList.addItem(curr, j);
                    j++;
                }
            }

            if (m2.group(2) != null) {
                Cookware curr = new Cookware();
                curr.setName(m2.group(2));

                if (CookwareList.exists(curr.getName())) {
                    continue;
                } else {
                    CookwareList.addItem(curr, j);
                    j++;
                }
            }
        }


    }

    public void ExtractTime(String content) throws IOException {

        String pattern3 = "~\\{+([\\d]{1,})+\\%+([a-zA-Z]{1,})+\\}";
        Pattern p3 = Pattern.compile(pattern3);
        Matcher m3 = p3.matcher(content);

        while (m3.find()) {
            if (m3.group(1) != null) {
                if (timeCurr == null) {
                    timeCurr.setTime(Integer.parseInt(m3.group(1)));
                    timeCurr.setUnit(m3.group(2));
                } else {
                    timeCurr.setTime(timeCurr.getTime() + Integer.parseInt(m3.group(1)));
                    timeCurr.setUnit(m3.group(2));
                }
            }
        }

    }

    public void ExtractStep() throws IOException {

        File file = new File(getFilePath());
        BufferedReader br = new BufferedReader(new FileReader(file));

        while (br.read() != -1) {
            Steps curr = new Steps();
            curr.setStep(br.readLine());
            steps.add(curr);
        }

    }

    public long returnTime(String content) throws IOException {

        Time curr = new Time();
        curr.setTime(0);

        String pattern3 = "~\\{+([\\d]{1,})+\\%+([a-zA-Z]{1,})+\\}";
        Pattern p3 = Pattern.compile(pattern3);
        Matcher m3 = p3.matcher(content);

        while (m3.find()) {
            if (m3.group(1) != null) {
                curr.setTime(Integer.parseInt(m3.group(1)));
            }
        }

        return curr.getTime()*60;
    }

    public void print() {

        IngrList.printList();
        CookwareList.printList();
        System.out.println("\nΣυνολική Ώρα:\n" + timeCurr.getTime() + " " + timeCurr.getUnit()+"\n");

        int i = 1;
        System.out.println("Βήματα:");
        for(Steps s : steps) {
            System.out.println(i+". "+ s.getStep()+"\n");
            i++;
        }

    }

    public void printGroceries() {
        IngrList.printList();
    }


    public String getFormattedOutput() {
        StringBuilder output = new StringBuilder();

        // Ingredients
        output.append("Υλικά:")
                .append("\n--------------------\n");
        for (int i = 0; i < IngrList.getSize(); i++) {
            Ingredient ingredient = IngrList.getItem(i);
            if (ingredient != null) {
                output.append("- ")
                        .append(ingredient.getName());
                if (ingredient.getQuantity() != null) {
                    output.append(" (").append(ingredient.getQuantity());
                    if (ingredient.getUnit() != null) {
                        output.append(" ").append(ingredient.getUnit());
                    }
                    output.append(")");
                }
                output.append("\n");
            }
        }

        // Cookware
        output.append("\nΣκεύη:")
                .append("\n--------------------\n");
        for (int i = 0; i < CookwareList.getSize(); i++) {
            Cookware cookware = CookwareList.getItem(i);
            if (cookware != null) {
                output.append("- ").append(cookware.getName()).append("\n");
            }
        }

        // Total Time
        output.append("\nΣυνολικός Χρόνος:")
                .append("\n--------------------\n")
                .append(timeCurr.getTime()).append(" ").append(timeCurr.getUnit()).append("\n\n");

        // Steps
        output.append("Βήματα:")
                .append("\n--------------------\n");
        for (int i = 0; i < steps.size(); i++) {
            Steps step = steps.get(i);
            output.append((i + 1)).append(". ").append(step.getStep()).append("\n");
        }

        return output.toString();
    }

    public String getListOutput() {
        StringBuilder output = new StringBuilder();

        // Ingredients
        output.append("Υλικά:")
                .append("\n--------------------\n");
        for (int i = 0; i < IngrList.getSize(); i++) {
            Ingredient ingredient = IngrList.getItem(i);
            if (ingredient != null) {
                output.append("- ")
                        .append(ingredient.getName());
                if (ingredient.getQuantity() != null) {
                    output.append(" (").append(ingredient.getQuantity());
                    if (ingredient.getUnit() != null) {
                        output.append(" ").append(ingredient.getUnit());
                    }
                    output.append(")");
                }
                output.append("\n");
            }
        }

        return output.toString();
    }
}
