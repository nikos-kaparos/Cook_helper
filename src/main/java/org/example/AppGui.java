package org.example;
import javax.swing.*;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Βοηθός Μάγειρας");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            Image bgImage = Toolkit.getDefaultToolkit().getImage("/home/petros/Downloads/bg.jpeg");
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, null);
                }
            };

            JButton showRecipesButton = new JButton("Εμφάνιση Συνταγών");
            JButton shoppingListButton = new JButton("Λίστα Αγορών");
            JButton executeRecipeButton = new JButton("Εκτέλεση Συνταγής");

            // Προσθήκη listeners στα κουμπιά
            showRecipesButton.addActionListener(e -> showRecipes());
            shoppingListButton.addActionListener(e -> showShoppingList());
            executeRecipeButton.addActionListener(e -> execute());


            panel.add(showRecipesButton);
            panel.add(shoppingListButton);
            panel.add(executeRecipeButton);

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
        } else {
            JOptionPane.showMessageDialog(null, "Δεν επιλέχθηκε αρχείο.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void showShoppingList() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Επιλέξτε Αρχείο Συνταγών");
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            Extractor extractor = new Extractor();
            try {
                for (File file : files) {
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
        } else {
            JOptionPane.showMessageDialog(null, "Δεν επιλέχθηκε αρχείο.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
        }

    }

    private static void execute() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Επιλέξτε αρχείο συνταγής για εκτέλεση:");
        int result = fileChooser.showOpenDialog(null);

        try {

            if (result == JFileChooser.APPROVE_OPTION) {

                Extractor extractor = new Extractor();
                extractor.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
                extractor.ExtractStep();
                List<Steps> steps = extractor.steps;
                Iterator<Steps> iterator = steps.iterator();

                JFrame frame = new JFrame("Εκτέλεση Συνταγής");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 400);

                JPanel panel = new JPanel (new BorderLayout());
                JLabel label = new JLabel();

                frame.add(panel);
                frame.setVisible(true);

                JButton button = new JButton("Έναρξη");
                panel.add(button, BorderLayout.SOUTH);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String step = iterator.next().getStep();

                        if(iterator.hasNext()) {

                            label.setText(step);
                            panel.add(label, BorderLayout.CENTER);
                            button.setText("Επόμενο βήμα");
                            try {
                                if (extractor.returnTime(step) != 0) {
                                    button.setEnabled(false);
                                    Countdown countdown = CountdownFactory.countdown(extractor.returnTime(step));
                                    Timer timer = new Timer(1000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            button.setText("Υπολειπόμενος χρόνος: " + (countdown.secondsRemaining() + 1) + "s");
                                            if (countdown.secondsRemaining() == 0) {
                                                ((Timer) e.getSource()).stop();
                                                button.setText("Υπολειπόμενος χρόνος: 1s");
                                            }
                                        }
                                    });
                                    timer.start();
                                    countdown.start();
                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown countdown) {
                                            button.setEnabled(true);
                                            button.setText("Επόμενο βήμα");
                                        }
                                    });
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            label.setText(step);
                            panel.add(label, BorderLayout.CENTER);
                            try {
                                if (extractor.returnTime(step) != 0) {
                                    button.setEnabled(false);
                                    Countdown countdown = CountdownFactory.countdown(extractor.returnTime(step));
                                    Timer timer = new Timer(1000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            button.setText("Υπολειπόμενος χρόνος: "+countdown.secondsRemaining() + 1 + "s");
                                            if (countdown.secondsRemaining() == 0) {
                                                ((Timer) e.getSource()).stop();
                                                button.setText("Υπολειπόμενος χρόνος: 1s");
                                            }
                                        }
                                    });
                                    timer.start();
                                    countdown.start();
                                    countdown.addNotifier(new Notifier() {
                                        @Override
                                        public void finished(Countdown countdown) {
                                            button.setEnabled(false);
                                            button.setText("Ολοκλήρωση");
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
                JOptionPane.showMessageDialog(null, "Δεν επιλέχθηκε αρχείο.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Σφάλμα κατά την επεξεργασία αρχείου: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }



}
