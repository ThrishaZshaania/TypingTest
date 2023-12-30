import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SuddenDeath extends JFrame {

    private static final String[] WORD_POOL = {"when", "there", "look", "where", "feel", "consider", "try", "action", "here", "type", 
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
    private Timer timer;
    private long startTime;
    private long endTime;

    public SuddenDeath() {
        super("Type-A-Thon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setFocusable(true);

        timerLabel = new JLabel("Given time: 30s");
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
                    startTime = System.currentTimeMillis();
                }

                char typedChar = e.getKeyChar();
                char targetChar = targetText.charAt(currentIndex);
                
                if (typedChar == targetChar && targetChar != KeyEvent.VK_SPACE) {
                    labels[currentIndex].setForeground(Color.GREEN);
                    currentIndex++;
                    correctlyTyped++;
                } else if (typedChar == targetChar && targetChar == KeyEvent.VK_SPACE) {
                    labels[currentIndex].setForeground(Color.GREEN);
                    currentIndex++; 
                } else {
                    labels[currentIndex].setForeground(Color.RED);
                    stopTimer();
                    endTime = System.currentTimeMillis();
                    calculateScore();
                }

                if (currentIndex == targetText.length()) { //if user finish typing
                    stopTimer();
                    endTime = System.currentTimeMillis();
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
        correctlyTyped = 0;
        stopTimer();
    }

    private void resetTimer() {
        updateTimerLabel(30);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int seconds = 30;

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
        double elapsedTime = endTime - startTime;
        double minutes = elapsedTime / 60000.0;
        int wpm = (int) Math.round(((double) correctlyTyped/5) / minutes);
        ProfileDisplay.SDscore.add(wpm);

        JOptionPane.showMessageDialog(null, "Time's up!\nWPM: " + wpm);
        resetGame();
    }

    private void updateTimerLabel(int seconds) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Time left: "+ seconds + "s");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SuddenDeath suddenDeath = new SuddenDeath();
            suddenDeath.setVisible(true);
        });
    }

    //passage that will display in our test
    public static String getPassage(){
        // Generating random text
        Random r = new Random();
        StringBuilder textToType = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            int index = r.nextInt(WORD_POOL.length);
            textToType.append(WORD_POOL[index]).append(" ");
        }
        return textToType.toString().trim();
    }
}