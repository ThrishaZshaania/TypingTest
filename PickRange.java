package wordsmodegui;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
public class PickRange extends JFrame{
    public PickRange(){
        setTitle("Words Game Mode");
        setLayout(new GridLayout(4, 1, 0, 10)); // 4 rows, 1 column, vertical gap of 10 pixels
        
        // Create buttons
        JButton button10 = new JButton("10 Words");
        JButton button25 = new JButton("25 Words");
        JButton button50 = new JButton("50 Words");
        JButton button100 = new JButton("100 Words");

        // Add action listeners to the buttons
        button10.addActionListener(new ButtonClickListener(10));
        button25.addActionListener(new ButtonClickListener(25));
        button50.addActionListener(new ButtonClickListener(50));
        button100.addActionListener(new ButtonClickListener(100));
        
        // Add buttons to the frame
        add(button10);
        add(button25);
        add(button50);
        add(button100);
        
        // Center the frame on the screen
        setLocationRelativeTo(null);

    }
    
    // Action listener for the buttons
    private static class ButtonClickListener implements ActionListener {
        private int range;

        public ButtonClickListener(int range) {
            this.range = range;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // You can perform actions when a button is clicked
            switch (range) {
                case 10 -> {
                    Range10 Range10 = new Range10();
                    Range10.setSize(500,500);
                    Range10.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Range10.setVisible(true);
                }
                case 25 -> {
                    Range25 Range25 = new Range25();
                    Range25.setSize(500,500);
                    Range25.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Range25.setVisible(true);
                }
                case 50 -> {
                    Range50 Range50 = new Range50();
                    Range50.setSize(500,500);
                    Range50.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Range50.setVisible(true);
                }
                case 100 -> {
                    Range100 Range100 = new Range100();
                    Range100.setSize(500,500);
                    Range100.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Range100.setVisible(true);
                }
            }
        }
    }
}
        
    


