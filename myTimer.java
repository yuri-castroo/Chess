import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class myTimer implements ActionListener {
	// GUI-specific variables 
    private JFrame frame;
    private JPanel panel;
    private JLabel timeLabel = new JLabel();
    private String timeLabelName;
    
    // Timer-specific variables
    private static final int ONE_SECOND = 1000;
    private int count = 6000000;
    private boolean isTimerActive = false;
    public Timer tmr = new Timer(ONE_SECOND, this);
    
    // Constructors
    public myTimer(boolean Comp) {
    	count= 600;
    	timeLabel.setFont(new Font("Calibri", Font.PLAIN, 24));
    	timeLabel.setText(formatTime(count));
    	if (Comp)
    	{    		
    		timeLabelName = "Computer Time:     ";
    	}
    	else
    	{
    		timeLabelName = "User Time:     ";
    	}
    	timeLabel.setText(timeLabelName);
        GUI();
    }
    
    public myTimer(JPanel addPanel) {
    	count= 6000000;
    	panel = addPanel;
    	timeLabel.setFont(new Font("Serif", Font.PLAIN, 24));
    	timeLabel.setText(formatTime(count));
    	timeLabel.setText("         ");
        GUI();
    }
    
    // Return the panel so that we can add it to the layered pane
    public JPanel getPanel()
    {
    	return panel;
    }
    
    // Format time 
    private String formatTime(int count) {
        int hours = count / 3600;
        int minutes = (count-hours*3600)/60;
        int seconds = count-minutes*60;

        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    // GUI Function 
    private void GUI() {
        frame = new JFrame();
        panel = new JPanel();

//        timeLabel.setBorder(BorderFactory.createRaisedBevelBorder());

//        JPanel cmdPanel = new JPanel();
//        cmdPanel.setLayout(new GridLayout());
//        JPanel clrPanel = new JPanel();
//        clrPanel.setLayout(new GridLayout(0,1));

//        panel.setLayout(new BorderLayout());
        panel.add(timeLabel, BorderLayout.NORTH);
//        panel.add(cmdPanel, BorderLayout.SOUTH);
//        panel.add(clrPanel, BorderLayout.EAST);

        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        // Begin the timer 
        isTimerActive = true;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (isTimerActive) {
          count--;
          if (count == 0)
          {
        	  System.out.println("Game Over!");
        	  System.exit(0);
          }
          timeLabel.setText(timeLabelName + formatTime(count));
      }
    }

  
}