package wordsmodegui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Range10 extends JFrame{
    private final JPanel panel;
    private JLabel[] labels;
    private final JLabel timerLabel; // New label to display the timer
    private String targetText;
    private int currentIndex = 0;
    private long startTime;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private int errorCount = 0; // New variable to count errors
    private Timer timer;

    
    public Range10(){
        super("Range10");
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
            targetText = randomTextToType(); // Generate new random text
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
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(repeatButton);
        buttonPanel.add(newRangeButton);

        add(buttonPanel, BorderLayout.SOUTH);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (currentIndex == 0 && timer == null) {
                    startStopWatch();
                }

                char typedChar = e.getKeyChar();

                if (typedChar == KeyEvent.VK_BACK_SPACE) {
                    currentIndex = Math.max(0, currentIndex - 1);
                    labels[currentIndex].setForeground(Color.BLACK);
                } else {
                    char targetChar = targetText.charAt(currentIndex);

                    if (typedChar == targetChar ) {
                        labels[currentIndex].setForeground(Color.GREEN);
                    } else {
                        labels[currentIndex].setForeground(Color.RED);
                        errorCount++; // Increment error count
                    }

                    currentIndex++;
                }

                if (currentIndex == targetText.length()) {
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
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        int totalWords = targetText.split("\\s+").length;
        double minutes = timeTaken / 60000.0;
        double wpm = (totalWords / 5.0) / minutes;

        int accuracy = (int) Math.round(((double) (currentIndex - errorCount) / currentIndex) * 100);

        JOptionPane.showMessageDialog(null, "Congratulations! You typed the sentence correctly.\nWPM: " + wpm + "\nErrors: " + errorCount + "\nAccuracy: " + accuracy + "%");
    }
    
    private void initializeWordsMode(){
        targetText = randomTextToType();
        labels = new JLabel[targetText.length()];
        for (int i=0; i<targetText.length(); i++) {
            labels[i] = new JLabel(String.valueOf(targetText.charAt(i)));
            labels[i].setFont(new Font("SansSerif", Font.BOLD, 20));
            labels[i].setFocusable(true);
            panel.add(labels[i]);
    }    
    }
    
    private static String randomTextToType(){
        //given text pool
        String[] words={"in", "a", "happy dance", "the", "brave", "elephant", "found", "maze", "of", "joyful", "pineapples", "and", "strong", "butterflies",
            "the", "spaceman", "went", "on", "great", "trip", "meeting", "musical", "breezes", "fizzy", "snow", "luck", "wish", "to", "explore", "brought", "unbeatable", "happiness", "connecting", "sweet", "chocolate", "changing", "colors", "in", "sky",
            "tough", "balance", "shared", "stories", "happy", "peace", "as", "the", "strange", "showed", "blue", "secrets", "quiet", "efforts", "this", "light", "flow", "words", "every", "tap", "makes", "nice", "song", "making", "great", "colorful"};
        
        //generating random text 
        Random rand = new Random();
        StringBuilder randomTextToType=new StringBuilder();
        for (int i=0; i<10; i++){
            int index = rand.nextInt(words.length);
            randomTextToType.append(words[index]).append(" ");
        }
        return randomTextToType.toString().trim(); 
    }
}

