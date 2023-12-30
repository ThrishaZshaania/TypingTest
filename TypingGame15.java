/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication66;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 *
 * @author farisyaalyssa
 */
public class TypingGame15 extends JFrame{
    
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
    private Timer timer;
    private int givenTime = 15;
    public static List<String> commonlyMisspelled;
    public static List<String> distinctMisspelled;
    public static String[] arrayDistinctMisspelled;
    
    public TypingGame15() {
        super("Type-A-Thon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setFocusable(true);

        timerLabel = new JLabel("Given time: 15s");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(timerLabel, BorderLayout.NORTH);

        add(panel, BorderLayout.CENTER);

        initializeGame();

        JButton repeatButton = new JButton("Repeat Level"); //repeat with same generated text
        repeatButton.addActionListener((ActionEvent e) -> {
            resetGame();
            resetTimer(); // Reset the timer when repeating the level
            startGameWithText(targetText);
        });

        JButton newPromptButton = new JButton("New Prompt");//repeat with another generated text
        newPromptButton.addActionListener((ActionEvent e) -> {
            resetGame();
            resetTimer(); // Reset the timer when generating a new prompt
            targetText = null;
            for (JLabel label : labels) {
                panel.remove(label);
            }
            panel.revalidate();
            panel.repaint();
            initializeGame();
            startGameWithText(targetText);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(repeatButton);
        buttonPanel.add(newPromptButton);

        add(buttonPanel, BorderLayout.SOUTH);

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (currentIndex == 0 && timer == null) {
                    // start timer when the user types the first character
                    startTimer();
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
                    stopTimer();
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

    private void initializeGame() {
        generateRandomPrompt();
        labels = new JLabel[targetText.length()];
        for (int i = 0; i < targetText.length(); i++) {
            labels[i] = new JLabel(String.valueOf(targetText.charAt(i)));
            labels[i].setFont(new Font("SansSerif", Font.BOLD, 20));
            panel.add(labels[i]);
        }
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

    private void resetGame() {
        currentIndex = 0;
        errorCount = 0;
        previousSpaceIndex = 0;
        wrongWordFirstChar = 0;
        correctlyTyped = 0;
        nextSpaceIndex = 0;
        missed = 0;
        stopTimer();
    }

    private void resetTimer() {
        updateTimerLabel(15);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int seconds = 15;

            @Override
            public void run() {
                if (seconds > 0) {
                    seconds--;
                    updateTimerLabel(seconds);
                } else {
                    stopTimer();
                    calculateScore();
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void calculateScore() {
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
            }
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
                correctlyTyped -= countGreen[k]; //to minua the green characters in incorrectly typed words that are not corrected by backspace or skipped by space
            }
        }

        int charactersTyped = 0;
        for(int wti = 0; wti<wordsTyped.length; wti++){ //wti: Words Typed Index
            charactersTyped += totalCharTyped[wti]; //to calculate total characters typed
        }
        
        double minutes = givenTime / 60.0; //can't use timeTaken=endTime-startTime because startTime is the time when the game is initiated, not when first character is typed
        int wpm = (int) Math.round(((double) correctlyTyped/5) / minutes);

        int accuracy = (int) Math.round(((double) (charactersTyped - errorCount) / charactersTyped) * 100);

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

    private void updateTimerLabel(int seconds) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Time left: "+ seconds + "s");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingGame typingGame = new TypingGame();
            typingGame.setVisible(true);
        });
    }

    //passage that will display in our test
    public static String getPassage(){
         // Generating random text
        Random r = new Random();
        StringBuilder textToType = new StringBuilder();
        for (int i = 0; i < 75; i++) {
            int index = r.nextInt(WORD_POOL.length);
            textToType.append(WORD_POOL[index]).append(" ");
        }
        return textToType.toString().trim();
        
    }
    
    public static String getPassageCF(){
        // Generating random text
        Random r = new Random();
        StringBuilder textToType = new StringBuilder();
        for (int i = 0; i < 75; i++) {
            int index = r.nextInt(arrayDistinctMisspelled.length);
            textToType.append(arrayDistinctMisspelled[index]).append(" ");
        }
        return textToType.toString().trim();
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
    

