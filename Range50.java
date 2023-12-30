/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wordsmodegui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class Range50 extends JFrame {
    public static final String[] WORD_POOL = {"when", "there", "look", "where", "feel", "consider", "try", "action", "here", "type", 
                                               "game", "and", "how", "word", "test", "love", "speed", "together", "tomorrow", "then",
                                               "after", "final", "study", "have", "from", "because", "photo", "bottle", "cap", "people",
                                               "thing", "laptop", "year", "know", "team", "answer", "question", "button", "attention", "use",
                                               "amount", "current", "first", "borrow", "zeal", "fashion", "short", "long", "good", "work"};
    private final JPanel panel;
    private JLabel[] labels;
    private final JLabel timerLabel; // New label to display the timer
    private String targetText;
    private int currentIndex = 0;
    private int correctlyTyped = 0;
    private int previousSpaceIndex = 0;
    private int nextSpaceIndex = 0;
    private int wrongWordFirstChar = 0;
    private int missed = 0;
    private int errorCount = 0; // New variable to count errors
    private long startTime;
    private Timer timer;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    public static List<String> commonlyMisspelled;
    public static List<String> distinctMisspelled;
    public static String[] arrayDistinctMisspelled;

    
    public Range50 (){
        super("Range50");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 1000);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        timerLabel = new JLabel("Time Taken: 0h : 0m : 0s");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(timerLabel, BorderLayout.NORTH);

        add(panel, BorderLayout.CENTER);
        
        initializeWordsMode();
        
        JButton repeatButton = new JButton("Repeat"); //repeat with same range of words
        repeatButton.addActionListener((ActionEvent e) -> {
            resetGame();
            resetStopWatch(); // Reset the timer when repeating the level
            targetText = getPassage(); // Generate new random text
            resetLabels(targetText); // Update labels with the new text
            updateTimerLabel(); // Update the timer label
            // Request focus on the panel after updating labels and starting the stopwatch
            panel.requestFocusInWindow();
        });

        JButton newRangeButton = new JButton("New Range");//repeat with another range of words
        newRangeButton.addActionListener((ActionEvent e) -> {
            // Hide the current Range10 frame
            setVisible(false);
        });
        
        JButton correctionFacility = new JButton("Practice Misspelled Words");//repeat with another generated text
        correctionFacility.addActionListener((ActionEvent e) -> {
            resetGame();
            resetStopWatch(); // Reset the timer when generating a new prompt
            targetText = null;
            for (JLabel label : labels) {
                panel.remove(label);
            }
            panel.revalidate();
            panel.repaint();
            initializeWordsMode();
            commonlyMisspelled = null;
            distinctMisspelled = null;
            arrayDistinctMisspelled = null;
            startGameWithTextCF(targetText);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(repeatButton);
        buttonPanel.add(newRangeButton);
        buttonPanel.add(correctionFacility);

        add(buttonPanel, BorderLayout.SOUTH);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (currentIndex == 0 && timer == null) {
                    startStopWatch();
                }

                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);
                
                if (typedChar == KeyEvent.VK_BACK_SPACE) {
                    // Handle backspace separately
                    labels[currentIndex].setForeground(Color.BLACK); //without this, the character will turn red since backspace is taking its place
                    currentIndex = Math.max(0, currentIndex - 1); //go back to the previous index, minimum 0
                    labels[currentIndex].setForeground(Color.BLACK); //turn the previous character to black again denoting we erased it
                } else if (typedChar == KeyEvent.VK_SPACE && targetChar != KeyEvent.VK_SPACE && currentIndex>0){
                    // Allow the user to move to the next word if they press space
                    previousSpaceIndex = findPreviousSpaceIndex(targetText, currentIndex-1);
                    wrongWordFirstChar = previousSpaceIndex + 1;
                    correctlyTyped -= Math.max(0, currentIndex - wrongWordFirstChar - 1); //to minus the already calculated correctly typed characters because the word is now incorrectly spelled
                    nextSpaceIndex = targetText.indexOf(" ", currentIndex);
                    missed = nextSpaceIndex - currentIndex;
                    errorCount += missed;
                    currentIndex = nextSpaceIndex + 1; //go to the next word's first character
                } else {
                    if (typedChar == targetChar && targetChar != KeyEvent.VK_SPACE) {
                        labels[currentIndex].setForeground(Color.GREEN);
                        currentIndex++;
                        correctlyTyped++;
                    } else if (typedChar == targetChar && targetChar == KeyEvent.VK_SPACE) {
                        labels[currentIndex].setForeground(Color.GREEN);
                        currentIndex++; 
                    } else {
                        labels[currentIndex].setForeground(Color.RED);
                        errorCount++; // Increment error count
                        currentIndex++;
                    }
                }

                if (currentIndex == targetText.length()) { //if user finish typing
                    stopStopWatch();
                    calculateScore();
                }
            }


            @Override
            public void keyPressed(KeyEvent e) {
                // Not needed for this example
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for this example
            }
        });

        
    }

    private void startStopWatch(){
        if (timer==null){
            startTime = System.currentTimeMillis();
            timer = new Timer(1000, new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    seconds++;
                    if(seconds==60){
                        minutes++;
                        seconds = 0;
                    }
                    if(minutes==60){
                        hours++;
                        minutes = 0;
                    }
                    updateTimerLabel();
                }
                
            });
            timer.start();
        }
        }
    
    private void updateTimerLabel(){
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Time Taken: "+hours+"h : "+minutes+"m : "+ seconds + "s");
        });
    }
    
    private void stopStopWatch(){
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }
    
    private void resetStopWatch(){
        if(timer!=null){
            timer.stop();
        }
            seconds = 0;
            minutes = 0;
            hours = 0;
            updateTimerLabel();
        
    }
    
    private void resetGame(){
        currentIndex = 0;
        errorCount = 0;   
    }
    
    private void resetLabels(String newText) {
        // Clear existing labels
        panel.removeAll();
        labels = new JLabel[newText.length()];

        // Add new labels
        for (int i = 0; i < newText.length(); i++) {
            labels[i] = new JLabel(String.valueOf(newText.charAt(i)));
            labels[i].setFont(new Font("SansSerif", Font.BOLD, 20));
            labels[i].setFocusable(true);
            panel.add(labels[i]);
        }

        // Request focus on the panel before updating labels
        panel.requestFocusInWindow();
    
        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    private void calculateScore(){
        commonlyMisspelled = new ArrayList<>();
        distinctMisspelled = new ArrayList<>();
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        
        String[] expectedWords = targetText.split("\\s+"); //array that stores the words of the text prompt
        int[] totalCharExpected = new int[expectedWords.length]; //array that stores the number of characters in each word of the text prompt
        for(int ewi=0; ewi<expectedWords.length; ewi++){ //ewi:Expected Words Index
            totalCharExpected[ewi] = expectedWords[ewi].length();
        }
        
        String[] wordsTyped = getTypedWordsFromLabels(); //array that stores the words typed by the user

        int[] countGreen = new int[wordsTyped.length]; //array that stores the number of green characters for each word typed 
        int[] countRed = new int[wordsTyped.length]; //array that stores the number of red characters for each word typed
        int[] totalCharTyped = new int[wordsTyped.length]; //array that stores the total characters typed in each word typed (excluding missed letters)

        int currentWordIndex = 0;  // Track the current word index
        boolean insideWord = true;  // Track whether the current character is inside a word

        //Checking correctness based on foreground color
        for (int i = 0; i < labels.length; i++) {
            char currentChar = labels[i].getText().charAt(0);

            if (currentChar == ' ') {
                insideWord = false;  // Set insideWord to false when encountering a space
            } else {
                if (!insideWord) { //when insideWord is false (previous character is space)
                    currentWordIndex++;  // Entering a new word
                    insideWord = true;
                }

                if (labels[i].getForeground() == Color.GREEN) {
                    countGreen[currentWordIndex]++;
                } else if (labels[i].getForeground() == Color.RED) {
                    countRed[currentWordIndex]++;
                }
                totalCharTyped[currentWordIndex] = countGreen[currentWordIndex] + countRed[currentWordIndex]; 
                
                if(countRed[currentWordIndex]>0)
                    commonlyMisspelled.add(wordsTyped[currentWordIndex]);
            }
        }
 
        distinctMisspelled = commonlyMisspelled.stream().distinct().collect(Collectors.toList());
        arrayDistinctMisspelled = new String[distinctMisspelled.size()];
        arrayDistinctMisspelled = distinctMisspelled.toArray(new String[0]);
        System.out.println("Commonly misspelled words: " + Arrays.toString(arrayDistinctMisspelled));
        for(String misspelled : arrayDistinctMisspelled){
            ProfileDisplay.allCommonlyMisspelled.add(misspelled);
        }
        
        /* if want to check the calculations
        for (int j = 0; j < wordsTyped.length; j++) {
            System.out.println("Word Typed: " + wordsTyped[j]);
            System.out.println("Number of characters typed correctly(green): " + countGreen[j]);
            System.out.println("Number of characters typed incorrectly(red): " + countRed[j]);
            System.out.println("Number of characters expected: " + totalCharExpected[j]);
            System.out.println("Number of characters typed: " + totalCharTyped[j]);
        }*/
        
        for (int k = 0; k < wordsTyped.length; k++) {
            if(countRed[k]>0 && countGreen[k]>0 && totalCharTyped[k]==totalCharExpected[k]){
                correctlyTyped -= countGreen[k]; //to minus the green characters in incorrectly typed words that are not corrected by backspace or skipped by space
            }
        }

        int charactersTyped = 0;
        for(int wti = 0; wti<wordsTyped.length; wti++){ //wti: Words Typed Index
            charactersTyped += totalCharTyped[wti]; //to calculate total characters typed
        }
        
        double minutes = timeTaken / 60.0; //can't use timeTaken=endTime-startTime because startTime is the time when the game is initiated, not when first character is typed
        int wpm = (int) Math.round(((double) correctlyTyped/5) / minutes);
        ProfileDisplay.allGamesWPM.add(wpm);

        int accuracy = (int) Math.round(((double) (charactersTyped - errorCount) / charactersTyped) * 100);
        ProfileDisplay.allGamesAccuracy.add(accuracy);

        JOptionPane.showMessageDialog(null, "Time's up!\nWPM: " + wpm
                + "\nErrors: " + errorCount + "\nAccuracy: " + accuracy + "%");
        resetGame();
    }
    
    private String[] getTypedWordsFromLabels() {
        List<String> wordsTypedList = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (JLabel label : labels) {
            String text = label.getText();
            if (!text.equals(" ")) {
                currentWord.append(text);
            } else if (currentWord.length() > 0) {
                wordsTypedList.add(currentWord.toString());
                currentWord.setLength(0); // Reset currentWord for the next word
            }
        }

        // Add the last word if the sentence doesn't end with a space
        if (currentWord.length() > 0) {
            wordsTypedList.add(currentWord.toString());
        }

        return wordsTypedList.toArray(String[]::new);
    }
    
    private void generateRandomPrompt() {
        targetText = getPassage();
    }
    
    private void startGameWithText(String text) {
        for (JLabel label : labels) {
            label.setForeground(Color.BLACK);
        }

        for (int i = 0; i < text.length(); i++) {
            labels[i].setText(String.valueOf(text.charAt(i)));
        }
        panel.requestFocusInWindow();
    }
    
    private void initializeWordsMode(){
        generateRandomPromptCF();
        labels = new JLabel[targetText.length()];
        for (int i=0; i<targetText.length(); i++) {
            labels[i] = new JLabel(String.valueOf(targetText.charAt(i)));
            labels[i].setFont(new Font("SansSerif", Font.BOLD, 20));
            labels[i].setFocusable(true);
            panel.add(labels[i]);
    }    
    }
    
    private void generateRandomPromptCF() {
        targetText = getPassageCF();
    }
    
    //passage that will display in our test
    public static String getPassage(){
        // Generating random text
        Random r = new Random();
        StringBuilder textToType = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            int index = r.nextInt(WORD_POOL.length);
            textToType.append(WORD_POOL[index]).append(" ");
        }
        return textToType.toString().trim();
    }
    
    public static String getPassageCF(){
        // Generating random text
        Random r = new Random();
        StringBuilder textToType = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            int index = r.nextInt(arrayDistinctMisspelled.length);
            textToType.append(arrayDistinctMisspelled[index]).append(" ");
        }
        return textToType.toString().trim();
    }
    
    private void startGameWithTextCF(String text) {
        for (JLabel label : labels) {
            label.setForeground(Color.BLACK);
        }

        for (int i = 0; i < text.length(); i++) {
            labels[i].setText(String.valueOf(text.charAt(i)));
        }
        panel.requestFocusInWindow();
    }

    
    private static int findPreviousSpaceIndex(String text, int currentIndex) {
        for (int i = currentIndex; i >= 0; i--) {
            if (text.charAt(i) == ' ') {
                return i;
            }
        }
        // If no space is found, return -1
        return -1;
    }

}

