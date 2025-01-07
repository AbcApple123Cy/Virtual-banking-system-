// Transfer.java
// Represents a transfer ATM transaction


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class Transfer extends Transaction {
    private double amount; // amount to transfer
    private int payeeAccountNumber; //transfer number to transfer
    private String payeeAccountNumberInText;
    private Keypad keypad; // reference to keypad
    private ATM theATM;
    private JPanel transferPanel;
    private JTextArea message;
    private JButton permissionButton;
    private JPanel amountPanel;
    private JPanel informationPanel;



    //Transfer constructor
    public Transfer(int userAccountNumber, Screen atmScreen,
                    BankDatabase atmBankDatabase, Keypad atmKeypad, ATM atm) {
        //initialize superclass variables
        super(userAccountNumber, atmScreen, atmBankDatabase);
        theATM = atm;
        //initialize superclass variables
        keypad = atmKeypad;

        //transfer panel
        setLayout(new BorderLayout());
        transferPanel = new JPanel();
        transferPanel.setLayout(new BorderLayout());


        //whether the is make a transfer
        JPanel permissionPanel = new JPanel();
        permissionPanel.setLayout(new BorderLayout());

        //Information Panel showing some information
        informationPanel = new JPanel();
        informationPanel.setLayout(new BorderLayout());
        informationPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JPanel permissionButtonPanel = new JPanel();
        permissionButtonPanel.setLayout(new GridLayout(1, 2));

        message = new JTextArea(3, 20);
        message.setFont(new Font("Serif", Font.PLAIN, 25));
        message.setEditable(false);

        permissionButton = new JButton("Confirm");
        JButton cancelTransferButton = new JButton("Cancel");
        permissionButton.setFont(new Font("Serif", Font.PLAIN, 30));
        cancelTransferButton.setFont(new Font("Serif", Font.PLAIN, 30));


        permissionButtonPanel.add(permissionButton);
        permissionButtonPanel.add(cancelTransferButton);

        informationPanel.add(message, BorderLayout.CENTER);
        permissionPanel.add(informationPanel, BorderLayout.CENTER);
        permissionPanel.add(permissionButtonPanel, BorderLayout.SOUTH);


        PermissionButtonHandler confirm = new PermissionButtonHandler();
        permissionButton.addActionListener(confirm);
        cancelTransferButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        theATM.backToMenu();
                    }
                }
        );


        //amount panel
        Keypad amountKeypad = new Keypad();
        amountKeypad.allowClearToResetFloaingPoint();

        amountPanel = new JPanel();
        amountPanel.setLayout(new BorderLayout());
        JPanel amountControlPanel = new JPanel();
        amountControlPanel.setLayout(new GridLayout(2, 1));
        JButton amountCancelButton = new JButton("Cancel");
        JButton amountEnterButton = new JButton("Enter");
        amountControlPanel.add(amountCancelButton);
        amountControlPanel.add(amountEnterButton);
        amountKeypad.setTitleFontSize(22);
        amountKeypad.setTitle("Notes:\n" +
                "The transfer amount will be rounded into\n" +
                "the nearest number with 2 decimal places\n" + "\nPlease enter your amount: ");
        amountPanel.add(amountKeypad, BorderLayout.CENTER);
        amountPanel.add(amountControlPanel, BorderLayout.EAST);
        amountEnterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        amount = amountKeypad.getDouble();// The amount is fixed to two decimal points

                        // Force input to be two decimal places (with rounding).
                        // e.g. 775.354777 to 775.35
                        amount = amount * 100;
                        amount = Math.round(amount);
                        amount = amount / 100;

                        message.setText(
                                "\nTransfer Information: \nThe transfer account is: " + payeeAccountNumberInText +
                                        "\nThe transfer amount  is: HK$" + amount);



                        switchPanel(permissionPanel);
                    }
                }
        );
        amountCancelButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        theATM.backToMenu();
                    }
                }
        );


        // transfer account panel
        Keypad transferKeypad = new Keypad();
        transferKeypad.disableFloating();
        transferKeypad.setTitle("Please enter the account number of the \npayee:");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));
        JButton accountEnterButton = new JButton("Enter");
        JButton accountCancelButton = new JButton("Cancel");
        accountEnterButton.addActionListener(

                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        payeeAccountNumberInText = transferKeypad.getString();
                        payeeAccountNumber = transferKeypad.getInput();

                        switchPanel(amountPanel);

                    }
                }
        );
        accountCancelButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        theATM.backToMenu();
                    }
                }
        );

        controlPanel.add(accountCancelButton);
        controlPanel.add(accountEnterButton);

        transferPanel.add(transferKeypad, BorderLayout.CENTER);
        transferPanel.add(controlPanel, BorderLayout.EAST);

        add(transferPanel,BorderLayout.CENTER);

    }// end Transfer constructor



    private class PermissionButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            BankDatabase bankDatabase = getBankDatabase();//get reference

            Screen screen = new Screen();
            screen.switchWaitingScreen();
            switchPanel(screen);


            JButton backMenuButton = new JButton("Back To Menu");
            backMenuButton.setFont(new Font("Serif", Font.PLAIN, 30));
            informationPanel.add(backMenuButton, BorderLayout.SOUTH);
            backMenuButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            theATM.backToMenu();
                        }
                    }
            );

            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override public void run() {
                    switchPanel(informationPanel);
                }
            },2000);


            boolean userExist = bankDatabase.isUserExist(payeeAccountNumber);

            // Transaction with these two cases is not allowed.
            // 1. The input account doesn't exist
            // 2. Transfer money to yourself
            // The transaction will be cancelled and return to the main menu.

            // Case 1
            if (!userExist)
                message.setText("\nSorry, we can not find the account.\nThe transaction is canceled.");
                //Case 2
            else if(payeeAccountNumber == getAccountNumber())
                message.setText("\nSorry, transfer money to yourself is not allowed.\nThe transaction is canceled.");
            else if (amount > bankDatabase.getAvailableBalance(getAccountNumber()))
                message.setText("\nSorry, your transaction is fail " +
                        "\nbecause the transfer amount is out of your \navailable balance");
            else if(amount == 0)
                message.setText("\nSorry,your transaction is fail " +
                            "\nbecause the transfer amount need to greater than 0");
            //enough
            else {
                // update the account involved
                bankDatabase.debit(getAccountNumber(), amount);
                bankDatabase.credit(payeeAccountNumber, amount);

                // Cancel the original timer that switch to the informationPanel after 2 seconds
                timer.cancel();

                // Accessing... waiting - Screen Panel
                // After 2 seconds, it will display "Your transfer is success.....take your card"
                Timer timerSetMessage = new Timer();
                timerSetMessage.schedule(new TimerTask()
                {
                    @Override public void run() {
                        screen.setMessage("<html><center>Your transfer was successful.<br>Please take your card</center></html>");
                    }
                },2000);

                Timer timer2 = new Timer();
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
                        timer2.schedule(timetask2,3000); 
                    }
                };
                timer2.schedule(timetask,6000);
            
            }
        }
    }


    public void switchPanel(JPanel panel) {
        transferPanel.removeAll();
        transferPanel.repaint();
        transferPanel.revalidate();
        transferPanel.add(panel, BorderLayout.CENTER);
        transferPanel.repaint();
        transferPanel.revalidate();
    }
}


