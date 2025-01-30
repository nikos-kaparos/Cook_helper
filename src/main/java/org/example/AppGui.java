package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import gr.hua.dit.oop2.countdown.Countdown;
import gr.hua.dit.oop2.countdown.CountdownFactory;
import gr.hua.dit.oop2.countdown.Notifier;


public class AppGui {

    private static DefaultListModel<File> recipeListModel = new DefaultListModel<>();
    private static JList<File> recipeList = new JList<>(recipeListModel);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Βοηθός Μάγειρας");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);

            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(recipeList);
            scrollPane.setPreferredSize(new Dimension(300, 500));

            // Ενεργοποίηση επιλογής πολλαπλών συνταγών στην λίστα
            recipeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // Δημιουργία buttons
            JButton loadRecipesButton = new JButton("Φόρτωση Συνταγών");
            JButton showRecipesButton = new JButton("Εμφάνιση Συνταγής");
            JButton shoppingListButton = new JButton("Λίστα Αγορών");
            JButton executeRecipeButton = new JButton("Εκτέλεση Συνταγής");


            // Προσθήκη listeners στα κουμπιά
            showRecipesButton.addActionListener(e -> showRecipes());
            shoppingListButton.addActionListener(e -> showShoppingList());
            executeRecipeButton.addActionListener(e -> execute());
            loadRecipesButton.addActionListener(e -> loadRecipes());

            // Προσθήκη κουμπιών στο panel
            panel.add(loadRecipesButton);
            panel.add(showRecipesButton);
            panel.add(shoppingListButton);
            panel.add(executeRecipeButton);
            panel.add(scrollPane, BorderLayout.EAST);

            // Προσθήκη του panel στο frame
            frame.add(panel);
            frame.setVisible(true);

        });
    }

    private static void loadRecipes() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                if (!recipeListModel.contains(file)) {
                    recipeListModel.addElement(file);
                }
            }
        }
    }


    private static void showRecipes() {
            File selectedFile = recipeList.getSelectedValue();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Επιλεγμένο αρχείο: " + filePath);

            // Κώδικας για την εμφάνιση συνταγών
            try {
                Extractor extractor = new Extractor();
                String content = extractor.readFileContent(filePath);
                extractor.setFilePath(filePath);
                //PASS this test print all content from file
                System.out.println(content);
                extractor.ExtractIngredieant(content);
                extractor.ExtractCookware(content);
                extractor.ExtractTime(content);
                extractor.ExtractStep();

                JTextArea textArea = new JTextArea(extractor.getFormattedOutput());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Συνταγές", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Σφάλμα κατά την επεξεργασία αρχείου: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
    }

    private static void showShoppingList() {
        List<File> selectedFiles = recipeList.getSelectedValuesList();

        Extractor extractor = new Extractor();

        try {
            for (File file : selectedFiles) {
                String filepath = file.getAbsolutePath();
                String content = extractor.readFileContent(filepath);
                extractor.ExtractIngredieant(content);
            }

            JTextArea textArea = new JTextArea(extractor.getListOutput());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Λίστα αγορών", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την επεξεργασία αρχείου: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }

    }

    private static void execute() {
        File selectedFile = recipeList.getSelectedValue();

        try {

            if (selectedFile != null) {

                Extractor extractor = new Extractor();
                extractor.setFilePath(selectedFile.getAbsolutePath());
                extractor.ExtractStep();
                List<Steps> steps = extractor.steps;
                Iterator<Steps> iterator = steps.iterator();

                JFrame frame = new JFrame("Εκτέλεση Συνταγής");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 400);

                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel();

                frame.add(panel);
                frame.setVisible(true);

                JButton button = new JButton("Έναρξη");
                JLabel timeLabel = new JLabel();
                panel.add(button, BorderLayout.SOUTH);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String step = iterator.next().getStep();

                        if (iterator.hasNext()) {

                            label.setText(step);
                            panel.add(label, BorderLayout.CENTER);
                            button.setText("Επόμενο βήμα");
                            try {
                                if (extractor.returnTime(step) != 0) {
                                    Countdown countdown = CountdownFactory.countdown(extractor.returnTime(step));
                                    Timer timer = new Timer(1000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            panel.add(timeLabel, BorderLayout.NORTH);
                                            timeLabel.setText("Υπολειπόμενος χρόνος: " + countdown.secondsRemaining() / 60 + " min " + countdown.secondsRemaining() % 60 + " sec");

                                            button.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent actionEvent) {
                                                    countdown.stop();
                                                    ((Timer) e.getSource()).stop();
                                                    panel.remove(timeLabel);
                                                }
                                            });
                                            if (countdown.secondsRemaining() == 0) {
                                                ((Timer) e.getSource()).stop();
                                                panel.remove(timeLabel);

                                            }
                                        }
                                    });
                                    timer.start();
                                    countdown.start();
                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown countdown) {
                                        }
                                    });
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            label.setText(step);
                            panel.add(label, BorderLayout.CENTER);
                            button.setText("Ολοκλήρωση");
                            try {
                                if (extractor.returnTime(step) != 0) {
                                    Countdown countdown = CountdownFactory.countdown(extractor.returnTime(step));
                                    Timer timer = new Timer(1000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            panel.add(timeLabel, BorderLayout.NORTH);
                                            timeLabel.setText("Υπολειπόμενος χρόνος: " + countdown.secondsRemaining() / 60 + " min " + countdown.secondsRemaining() % 60 + " sec");

                                            button.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent actionEvent) {
                                                    countdown.stop();
                                                    ((Timer) e.getSource()).stop();
                                                    JOptionPane.showMessageDialog(null, "Η συνταγή ολοκληρώθηκε!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                                                    frame.dispose();
                                                }
                                            });
                                            if (countdown.secondsRemaining() == 0) {
                                                ((Timer) e.getSource()).stop();
                                                panel.remove(timeLabel);
                                            }
                                        }
                                    });
                                    timer.start();
                                    countdown.start();
                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown countdown) {
                                            JOptionPane.showMessageDialog(null, "Η συνταγή ολοκληρώθηκε!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                                            frame.dispose();
                                        }
                                    });
                                } else {
                                    button.setEnabled(false);
                                    button.setText("Ολοκλήρωση");
                                    JOptionPane.showMessageDialog(null, "Η συνταγή ολοκληρώθηκε!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                                    frame.dispose();
                                }

                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }


                });
            } else {
                JOptionPane.showMessageDialog(null, "Παρακαλώ φορτώστε και επιλέξτε αρχείο για εκτέλεση " , "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την επεξεργασία αρχείου: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }



}
