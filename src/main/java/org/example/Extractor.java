package org.example;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    private String filePath;
    IngredientList list = new IngredientList(50);

    public Extractor () {
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

        int i = 0;

        // Filling The list with the matching items
        while (m1.find()) {

            // Print out the ingredient and quantity
            if (m1.group(1) != null) {
                Ingredient curr = new Ingredient();
                curr.setName(m1.group(1));
                curr.setQuantity(m1.group(2));
                curr.setUnit(m1.group(3));

                if (list.nameExists(curr.getName())) {
                    int j = list.getPosition(curr.getName());
                    int quan = Integer.parseInt(curr.getQuantity()) + Integer.parseInt(list.getIngredient(j).getQuantity());
                    String quantity = Integer.toString(quan);
                    list.getIngredient(j).setQuantity(quantity);
                } else {
                    list.addIngredient(curr, i);
                    i++;
                }
            }

            if (m1.group(4) != null) {
                Ingredient curr = new Ingredient();
                curr.setName(m1.group(4));

                if (!list.nameExists(curr.getName())) {
                    list.addIngredient(curr, i);
                    i++;
                }
            }

        }

    }

    public void ExtractCookware (String content) throws IOException {
        CookwareList list = new CookwareList(50);


        String pattern2 = "#([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{+\\}|\\#([α-ωΑ-Ωά-ώΆ-Ώ]{1,})+\\{?+\\}?";
        Pattern p2 = Pattern.compile(pattern2);
        Matcher m2 = p2.matcher(content);

        int j = 0;

        while (m2.find()) {
            if (m2.group(1) != null) {
                Cookware curr = new Cookware();
                curr.setName(m2.group(1));

                if (list.exists(curr.getName())) {
                    continue;
                } else {
                    list.add(curr, j);
                    j++;
                }
            }

            if (m2.group(2) != null) {
                Cookware curr = new Cookware();
                curr.setName(m2.group(2));

                if (list.exists(curr.getName())) {
                    continue;
                } else {
                    list.add(curr, j);
                    j++;
                }
            }
        }

        list.printList();

    }

    public void ExtractTime(String content) throws IOException {

        String pattern3 = "~\\{+([\\d]{1,})+\\%+([a-zA-Z]{1,})+\\}";
        Pattern p3 = Pattern.compile(pattern3);
        Matcher m3 = p3.matcher(content);

        Time timeCurr = new Time();

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

        System.out.println("\nΣυνολική Ώρα:\n" + timeCurr.getTime() + " " + timeCurr.getUnit());
    }

    public void print() {

        list.printList();
    }



}
