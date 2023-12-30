/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication66;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author farisyaalyssa
 */
public class MyFrame extends JFrame{
    
    public MyFrame(){
        setTitle("Gamemode 2.2.1");
        setLayout(new GridLayout(4, 1, 0, 10)); // 4 rows, 1 column, vertical gap of 10 pixels
        
        // Create buttons
        JButton button15 = new JButton("15 seconds");
        JButton button30 = new JButton("30 seconds");
        JButton button45 = new JButton("45 seconds");
        JButton button60 = new JButton("60 seconds");

        // Add action listeners to the buttons
        button15.addActionListener(new ButtonClickListener(15));
        button30.addActionListener(new ButtonClickListener(30));
        button45.addActionListener(new ButtonClickListener(45));
        button60.addActionListener(new ButtonClickListener(60));
        
        /*button15.setSize(50, 20);
        button30.setSize(50, 20);
        button45.setSize(50, 20);
        button60.setSize(50, 20);
        */
        
        // Add buttons to the frame
        add(button15);
        add(button30);
        add(button45);
        add(button60);
        
        // Center the frame on the screen
        setLocationRelativeTo(null);

    }
    
    // Action listener for the buttons
    private static class ButtonClickListener implements ActionListener {
        private int seconds;

        public ButtonClickListener(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // You can perform actions when a button is clicked
            switch (seconds) {
                case 15 -> {
                    TypingGame15 TypingGame15 = new TypingGame15();
                    TypingGame15.setSize(900,600);
                    TypingGame15.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    TypingGame15.setVisible(true);
                }
                case 30 -> {
                    TypingGame TypingGame = new TypingGame();
                    TypingGame.setSize(900,600);
                    TypingGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    TypingGame.setVisible(true);
                }
                case 45 -> {
                    TypingGame45 TypingGame45 = new TypingGame45();
                    TypingGame45.setSize(900,600);
                    TypingGame45.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    TypingGame45.setVisible(true);
                }
                case 60 -> {
                    TypingGame60 TypingGame60 = new TypingGame60();
                    TypingGame60.setSize(900,600);
                    TypingGame60.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    TypingGame60.setVisible(true);
                }
                default -> {
                }
            }
                    }
    }
        
    
}
