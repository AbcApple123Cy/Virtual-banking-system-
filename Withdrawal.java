// Withdrawal.java
// Represents a withdrawal ATM transaction
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Withdrawal extends Transaction
{
    private int amount; // amount to withdraw
    private ATM theATM; // reference to atm
    private Keypad keypad; // reference to keypad
    private CashDispenser cashDispenser; // reference to cash dispenser
    private JPanel withdrawalPanel; // container for the panel in the withdrawal class
    private JPanel confirmPanel; // for showing the confirmation information
    private JTextArea confirmDisplay; // text element in the confirmPanel
    private JButton confirmButton; // button element in the confirmPanel
    

    // Withdrawal constructor
    public Withdrawal( int userAccountNumber, Screen atmScreen, 
    BankDatabase atmBankDatabase, Keypad atmKeypad, 
        CashDispenser atmCashDispenser, ATM atm )
    {
        
        // initialize superclass variables
        super( userAccountNumber, atmScreen, atmBankDatabase);
        cashDispenser = atmCashDispenser;
        theATM = atm;
        
        setLayout(new BorderLayout()); // assign layout manager to the Withdrawal class 
        withdrawalPanel = new JPanel(); // initialize the container
        withdrawalPanel.setLayout(new BorderLayout()); // assign layout manager to the container

        // initialize and construct the components in the confirmPanel
        // for showing the confirmation information
        confirmPanel = new JPanel();
        confirmPanel.setLayout(new BorderLayout());
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        confirmDisplay = new JTextArea(3,20);
        confirmDisplay.setFont(new Font("Serif", Font.PLAIN, 20));
        confirmDisplay.setEditable(false);
        confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Serif", Font.PLAIN, 20));
        confirmPanel.add(confirmDisplay, BorderLayout.CENTER);
        confirmPanel.add(confirmButton, BorderLayout.SOUTH);
        ConfirmButtonHandler confirm = new ConfirmButtonHandler();
        confirmButton.addActionListener(confirm);
        
        // initialize and construct the components in the controlPanel
        // for user to cancel the transaction or entering input
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2,1));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                    theATM.backToMenu();
                }
            }
        );
		
        JButton enterButton = new JButton("Enter");
		// add listner to the enterButton
        enterButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                    amount = keypad.getInput();
                    confirmDisplay.setText(
                        "Please confirm your withdrawal amount: HK$" + amount);
                    switchPanel(confirmPanel);
                    controlPanel.remove(enterButton);
                    withdrawalPanel.add(controlPanel,BorderLayout.EAST);
                }
            }
        );
		// add the cancelButton controlPanel by default
        controlPanel.add(cancelButton);
        
        // initialize and construct the components in the amountMenu
        // for displaying menu for user to choice an amount for withdrawal 
        JPanel amountMenu = new JPanel();
        amountMenu.setLayout(new BorderLayout());
        JLabel title = new JLabel("Please select an amount",SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        // initialize and construct the components in the amountButtonsMount
        // for arranging the buttons in amountMenu
        JPanel amountButtonsMount = new JPanel();        
        amountButtonsMount.setLayout(new GridLayout(3,2,30,30));
        amountButtonsMount.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JButton amountButtons[] = new JButton[6];
        amountButtons[0] = new JButton("HK$100");
        amountButtons[1] = new JButton("HK$200");
        amountButtons[2] = new JButton("HK$400");
        amountButtons[3] = new JButton("HK$500");
        amountButtons[4] = new JButton("HK$1000");
        amountButtons[5] = new JButton("Other Amounts");
        
        // add action listener to the buttons with fixed amount
		// add the fixed amount buttons to the amountButtonsMount
        for (int i = 0; i <= 4; i++)
        {
            amountButtons[i].addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        if (event.getSource() == amountButtons[0])
                        {
                            amount = 100;
                        }
                        if (event.getSource() == amountButtons[1])
                        {
                            amount = 200;
                        }
                        if (event.getSource() == amountButtons[2])
                        {
                            amount = 400;
                        }
                        if (event.getSource() == amountButtons[3])
                        {
                            amount = 500;
                        }
                        if (event.getSource() == amountButtons[4])
                        {
                            amount = 1000;
                        }
                        confirmDisplay.setText(
                                "Please confirm your withdrawal amount: HK$" + amount);
                        switchPanel(confirmPanel);
                        withdrawalPanel.add(controlPanel,BorderLayout.EAST);
                    }
                }   
                );
            amountButtonsMount.add(amountButtons[i]);
        }
        
		// initialize keypad for Other Amount input
        keypad = new Keypad();
        keypad.disableFloating();
        keypad.setTitle("Please enter an withdrawal amount\n(The minimum is HK$100\nand the maximum is HK$10000).");
		
        // add action listener to the button "Other Amount"
        amountButtons[5].addActionListener(
            new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                    switchPanel(keypad);
                    controlPanel.add(enterButton);
                    withdrawalPanel.add(controlPanel,BorderLayout.EAST);
                }
            }   
        );
		// add the other amount button to the amountButtonsMount
		amountButtonsMount.add(amountButtons[5]);
		
		// add the components to the amountMenu
        amountMenu.add(title, BorderLayout.NORTH);
        amountMenu.add(amountButtonsMount, BorderLayout.CENTER);

		// disable certain amount button when not enough notes in the cashDispenser
        for (int i = 0; i <= 4; i++)
        {
            int amountOption[] = {100, 200, 400, 500, 1000};
            if (!cashDispenser.isSufficientCashAvailable(amountOption[i]))
                amountButtons[i].setEnabled(false);
        }
		
		// add the components to the amountMenu
        withdrawalPanel.add(controlPanel,BorderLayout.EAST);
        withdrawalPanel.add(amountMenu, BorderLayout.CENTER);
		
		// add the withdrawalPanel to the class Panel
        add(withdrawalPanel);
        
    } // end Withdrawal constructor
    
    // listener for confirmButton
    private class ConfirmButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            Screen screen = getScreen();
            confirmButton.setText("Back to Menu"); 
            double availableBalance; // amount available for withdrawal
                        
            BankDatabase bankDatabase = getBankDatabase(); 
            availableBalance = 
                bankDatabase.getAvailableBalance( getAccountNumber() );
            
            // checking whether the amount is valid or not
            if (checkAmounts(amount)) 
            {
                // checking whether the balance has enough money
                if ( amount <= availableBalance )
                {   
                    // check whether the cash dispenser has enough money
                    if ( cashDispenser.isSufficientCashAvailable( amount ) )
                    {                
                        // update the account involved to reflect withdrawal
                        bankDatabase.debit( getAccountNumber(), amount );
                        cashDispenser.dispenseCash(); // dispense cash
                        
						// display success message and remainders
						// then back to welcome Panel
                        switchPanel(screen);
                        screen.withdrawalSuccess();
                        Timer timer = new Timer();
                            TimerTask timetask = new TimerTask()
                            {
                                @Override public void run() {
                                    screen.switchGreetingScreen();
                                    TimerTask timetask2 = new TimerTask()
                                    {
                                        @Override public void run() {
                                            theATM.backToWelcome();
                                        }  
                                    };
                                    timer.schedule(timetask2,3000); 
                                }
                            };
                            timer.schedule(timetask,6000);
                        
                        return;
                        
                    }
                    else // cash dispenser does not have enough cash
                    {
                        
                        confirmDisplay.setText( 
                            "Insufficient cash available in the ATM." +
                                "\nPlease choose a smaller amount." );
                        confirmButton.addActionListener(
                            new ActionListener()
                            {
                                public void actionPerformed( ActionEvent event )
                                {
                                    theATM.backToMenu();
                                }
                            }   
                        );        
                    }
                }
                else // account does not have enough available balance
                {
                   confirmButton.addActionListener(
                        new ActionListener()
                        {
                            public void actionPerformed( ActionEvent event )
                            {
                                theATM.backToMenu();
                            }
                        }   
                   );
                   confirmDisplay.setText("Insufficient funds in your account.");
                }
                
            }
            else // invalid user input
                confirmButton.addActionListener(
                        new ActionListener()
                        {
                            public void actionPerformed( ActionEvent event )
                            {
                                theATM.backToMenu();
                            }
                        }   
                   );
            
            switchPanel(confirmPanel);
        }
    }
    
    // method for validating the input of the other amount option.
    // return a boolean value
    private boolean checkAmounts(int amount)
    {
        int input = amount;
       
        if (input%100 == 0 && input <= 10000 && input >99)
        {
            return true;
        }
        if (input > 10000)
        {
            confirmDisplay.setText("The input amount is unacceptable." +
                "\nThe upper limit of each withdrawal is HK$10000.\nPlease try again.");
        }
		if (input < 100 ) 
        {
            confirmDisplay.setText("The input amount is unacceptable." +
                "\nThe minimum of withdrawal amount is HK$100.\nPlease try again.");
        }
        if (input%100 != 0) 
        {
            confirmDisplay.setText("The input amount is unacceptable." +
                "\nOnly the multiples of\nHK$100, HK$500, or HK$1000 is acceptable.\nPlease try again.");
        }
        return false;
    } // end checkOtherAmounts method
    
	// switch the certain Panel in the withdrawalPanel
    public void switchPanel(JPanel panel){
        withdrawalPanel.removeAll();
        withdrawalPanel.repaint();
        withdrawalPanel.revalidate();
        withdrawalPanel.add(panel,BorderLayout.CENTER);
        withdrawalPanel.repaint();
        withdrawalPanel.revalidate();
    } // end switchPanel method
} // end class Withdrawal



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