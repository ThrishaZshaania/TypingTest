/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication66;

/**
 *
 * @author farisyaalyssa
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TypingGame45 extends JFrame{

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
    private int givenTime = 45;

    public TypingGame45() {
        super("Type-A-Thon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setFocusable(true);

        timerLabel = new JLabel("Given time: 45s");
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
        updateTimerLabel(45);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int seconds = 45;

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
        for (JLabel label : labels) {
            char currentChar = label.getText().charAt(0);
            if (currentChar == ' ') {
                insideWord = false;  // Set insideWord to false when encountering a space
            } else {
                if (!insideWord) { //when insideWord is false (previous character is space)
                    currentWordIndex++;  // Entering a new word
                    insideWord = true;
                }
                if (label.getForeground() == Color.GREEN) {
                    countGreen[currentWordIndex]++;
                } else if (label.getForeground() == Color.RED) {
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
        java.util.List<String> wordsTypedList = new ArrayList<>();
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
        ArrayList<String> Passages=new ArrayList<>();
        String pas1="If you're not doing something with your life, then it doesn’t matter how long you live. If you're doing something with your life, then it doesn't matter how short your life may be. A life is not measured by years lived, but by its usefulness. If you are giving, loving, serving, helping, encouraging, and adding value to others, then you're living a life that counts!";
        String pas2="An application programming interface(API) is a way for two or more computer programs to communicate with each other. It is a type of software interface, offering a service to other pieces of software. A document or standard that describes how to build or use such a connection or interface is called an API specification.";
        String pas3="Virtual reality is the computer-generated simulation of a three-dimensional image or environment that can be interacted with in a seemingly real or physical way by a person using special electronic equipment, such as a helmet with a screen inside or gloves fitted with sensors.";
        String pas4="A technological revolution is a period in which one or more technologies is replaced by another novel technology in a short amount of time. It is a time of accelerated technological progress characterized by innovations whose rapid application and diffusion typically cause an abrupt change in society.";
        String pas5="Augmented reality (AR) is the real-time use of information in the form of text, graphics, audio and other virtual enhancements integrated with real-world objects. It is this real world element that differentiates AR from virtual reality.";
        String pas6="Multimedia content helps to vary and enhance the learning process, and leads to better knowledge retention. Educational video can provide more opportunities for students to engage with the content. Students around the world can learn from course content made available through video.";
        String pas7="Some periods of our growth are so confusing that we don't even recognize that growth is happening...Those long periods when something inside ourselves seems to be waiting, holding its breath, unsure about what the next step should be, eventually become the periods we wait for, for it is in those periods that we realize that we are being prepared for the next phase of our life and that, in all probability, a new level of the personality is about to be revealed.";
        String pas8="Human life without technology is like birds without feathers. We cannot imagine to survive without technology in today’s fast-moving world. The Corona virus pandemic has moreover proven, the importance of technology in our daily lives. ";
        String pas9="Globalization wouldn’t have been possible without internet. The fact that we can connect and work from any part of the world is because we have internet. We can have client meets and requirements from all over the globe and we can assimilate information and process delivery because we have internet.";
        String pas10="The foremost purpose of technology is communication. Social media and other technological applications have brought families together. Today we can find long lost school mates over Facebook, twitter, Instagram and we can reinstate our communication. In fact, we can even communicate with world leaders, prominent figures over these platforms. Communication is required in professional fields as well, and technology ensures that we can communicate with the world from wherever we are.";
        
        Passages.add(pas1);
        Passages.add(pas2);
        Passages.add(pas3);
        Passages.add(pas4);
        Passages.add(pas5);
        Passages.add(pas6);
        Passages.add(pas7);
        Passages.add(pas8);
        Passages.add(pas9);
        Passages.add(pas10);
        
        Random r=new Random();
        //Getting a random position from 0-9
        int place=(r.nextInt(10)); 
        
        //to use 200 characters in our typing test so I am taking a substring of that passage from 0 to 200
        String toReturn=Passages.get(place).substring(0,200); 
        if (toReturn.charAt(199)==32){
            toReturn=toReturn.strip(); //removing the blank spaces before the after substring we have taken
            toReturn=toReturn+"."; //Adding a full stop at the last instead of a space
        }
        return(toReturn); //We have got our Passage
        
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

