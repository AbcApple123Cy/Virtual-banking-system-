// Screen.java
// Represents the screen of the ATM
import java.awt.*;
import javax.swing.*;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Screen extends JPanel
{
    private JPanel mainScreen; // container for the panel in the withdrawal class
    private JPanel waitingScreen; // show the waiting message when loading
    private JPanel takeCardScreen; // prmopt user to take card
	private JLabel takeCardText; // message label in take card screen
    private JPanel greetingScreen; // show the greeting message
    private JPanel messagePanel; // for displaying other information 
    private JLabel messageText; // message label in messagePanel
    
    public Screen()
    {
		// assign the BorderLayout manager to the class Panel
        setLayout(new BorderLayout());
		
		// initialize and setup the layout of the mainScreen
        mainScreen = new JPanel();
        mainScreen.setLayout(new BorderLayout());
		
		// initialize and setup the layout of the waitingScreen
        waitingScreen = new JPanel();
        waitingScreen.setLayout(new BorderLayout());
        JLabel waitingText = new JLabel(
            "Accessing please wait...", SwingConstants.CENTER);
        waitingText.setFont(new Font("Serif", Font.PLAIN, 30));
        waitingScreen.add(waitingText, BorderLayout.CENTER);
        
		// initialize and setup the layout of the takeCardScreen
        takeCardScreen = new JPanel();
        takeCardScreen.setLayout(new BorderLayout());
        takeCardText = new JLabel(
            "Please take your card", SwingConstants.CENTER);
        takeCardText.setFont(new Font("Serif", Font.PLAIN, 30));
        takeCardScreen.add(takeCardText, BorderLayout.CENTER);
        
		// initialize and setup the layout of the messagePanel
        messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messageText = new JLabel(
                "Accessing please wait...", SwingConstants.CENTER);
        messageText.setFont(new Font("Serif", Font.PLAIN, 30));
        messagePanel.add(messageText, BorderLayout.CENTER);
        
		// initialize and setup the layout of the greetingScreen
        greetingScreen = new JPanel();
        greetingScreen.setLayout(new BorderLayout());
        JLabel greetingText = new JLabel(
            "Thank you! Goodbye!", SwingConstants.CENTER);
        greetingText.setFont(new Font("Serif", Font.PLAIN, 30));
        greetingScreen.add(greetingText, BorderLayout.CENTER);
        
		// add the mainScreen container to the class Panel
        add(mainScreen, BorderLayout.CENTER);
    }
    
	// switch the mainScreen to waiting screen
    public void switchWaitingScreen(){
        mainScreen.removeAll();
        mainScreen.add(waitingScreen, BorderLayout.CENTER);
        mainScreen.repaint();
        mainScreen.revalidate();
        return;
    } // end method switchWaitingScreen
    
	// switch the mainScreen to take card screen
    public void switchTakeCardScreen(){
        mainScreen.removeAll();
        takeCardText.setText("Please take your card");
        mainScreen.add(takeCardScreen, BorderLayout.CENTER);
        mainScreen.repaint();
        mainScreen.revalidate();
        return;
    } // end method switchTakeCardScreen
    
	// switch the mainScreen to greeting screen
    public void switchGreetingScreen(){
        mainScreen.removeAll();
        mainScreen.add(greetingScreen, BorderLayout.CENTER);
        mainScreen.repaint();
        mainScreen.revalidate();
        return;
    } // end method switchGreetingScreen
    
	// switch the mainScreen to take card screen
	// display a serious of remainders for successful withdrawal
    public void withdrawalSuccess(){
        mainScreen.removeAll();
        takeCardText.setText("<html><center>Your withdrawal is accepted<br>Please take your card first</center></html>");
        mainScreen.add(takeCardScreen, BorderLayout.CENTER);
        mainScreen.repaint();
        mainScreen.revalidate();
        Timer timer = new Timer();
        TimerTask timetask1 = new TimerTask()
        {
            @Override public void run() {
                takeCardText.setText("Please take your cash");
            }
        };
        timer.schedule(timetask1,3000); 
        return;
    } // end method withdrawalSuccess
    
	// set the messageText displayed in the message screen
	// switch the mainScreen to message screen
    public void setMessage(String information){
        mainScreen.removeAll();
        messageText.setText(information);
        mainScreen.add(messagePanel, BorderLayout.CENTER);
        mainScreen.repaint();
        mainScreen.revalidate();
        Timer timer = new Timer();
        TimerTask timetask = new TimerTask()
        {
            @Override public void run() {
                messageText.setText(information);
            }
        };
        timer.schedule(timetask,3000);

        return;
    } // end method setMessage
    
} // end class Screen



/**************************************************************************
 * (C) Copyright 1992-2007 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/