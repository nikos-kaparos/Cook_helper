package org.example;
import javax.swing.*;
        import java.awt.*;
        import java.io.File;

public class AppGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Βοηθός Μάγειρας");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel panel = new JPanel();
            JButton showRecipesButton = new JButton("Εμφάνιση Συνταγών");
            JButton shoppingListButton = new JButton("Λίστα Αγορών");

            // Προσθήκη listeners στα κουμπιά
            showRecipesButton.addActionListener(e -> showRecipes());
            shoppingListButton.addActionListener(e -> showShoppingList());

            panel.add(showRecipesButton);
            panel.add(shoppingListButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    private static void showRecipes() {
        // Χρησιμοποιούμε JFileChooser για επιλογή αρχείου
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Επιλέξτε Αρχείο Συνταγών");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Επιλεγμένο αρχείο: " + filePath);

            // Κώδικας για την εμφάνιση συνταγών
            try {
                Extractor extractor = new Extractor();
                String content = extractor.readFileContent(filePath);
                //PASS this test print all content from file
                System.out.println(content);
                extractor.ExtractIngredieant(content);
                extractor.ExtractCookware(content);
                extractor.ExtractTime(content);
//                extractor.ExtractStep();

                JTextArea textArea = new JTextArea(extractor.getFormattedOutput());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Συνταγές", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Σφάλμα κατά την επεξεργασία αρχείου: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Δεν επιλέχθηκε αρχείο.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void showShoppingList() {
        // Κώδικας για τη λίστα αγορών
        JOptionPane.showMessageDialog(null, "Λειτουργία Λίστας Αγορών");
    }
}
