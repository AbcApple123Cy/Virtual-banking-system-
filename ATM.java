// ATM.java
// Represents an automated teller machine
import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ATM extends JFrame
{
    private boolean userAuthenticated; // whether user is authenticated
    private int currentAccountNumber; // current user's account number
    private Screen screen; // ATM's screen
    private Keypad keypad; // ATM's keypad
    private CashDispenser cashDispenser; // ATM's cash dispenser
    private BankDatabase bankDatabase; // account information database

    // constants corresponding to main menu options
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int TRANSFER = 3;
    private static final int EXIT = 4;
    
    private JPanel mainPanel; // container for the panel from all the class
    
    private JPanel welcomePanel; // for showing the welcomeMessage and start button
    private JPanel welcomePanelMount; // for constructing the welcomeMessage and start button
    private JLabel welcomeMessage; // text component for the welcome message
    private JButton startButton; // button for accessing the ATM system
    
    private JPanel loginPanel; // for showing the login components
    private boolean loginState; // indicating the state of the login process
    private JPanel invaildPanel; // for showing the login failed screen
    private JPanel menuPanel; // for showing the main menu of the ATM
    private JButton menuButtons[]; // buttons in the main menu of the ATM
    private int mainMenuSelection; // indicating what function is selected
    private JPanel transactionPanel; // for showing the transcation Screen
    private JPanel screenPanel; // for showing the template screen in the Screen Class
    
    // no-argument ATM constructor initializes instance variables
    public ATM() 
    {
        super("ATM");
        
        
        userAuthenticated = false; // user is not authenticated to start
        currentAccountNumber = 0; // no current account number to start
        screen = new Screen(); // create screen
        keypad = new Keypad(); // create keypad 
        cashDispenser = new CashDispenser(); // create cash dispenser
        bankDatabase = new BankDatabase(); // create acct info database
        
        
        setLayout(new BorderLayout()); // assign layout manager to the ATM class
        // setting of the attributes of the JFrame
        setPreferredSize(new Dimension(800, 800));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // initialize and construct the components in the welcomePanel        
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridBagLayout());
        
        // initialize and construct the components in the welcomePanelMount
        welcomePanelMount = new JPanel();
        welcomePanelMount.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        welcomeMessage = new JLabel(
            "Welcome! \nClick the button to use.", SwingConstants.CENTER);
        welcomeMessage.setFont(new Font("Serif", Font.PLAIN, 30));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 0;
        welcomePanelMount.add(welcomeMessage, c);
        startButton = new JButton("start");
        startButton.setFont(new Font("Serif", Font.PLAIN, 30));
        c.ipady = 40;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        welcomePanelMount.add(startButton, c);
        welcomePanel.add(welcomePanelMount);
        add(mainPanel, BorderLayout.CENTER);
        switchPanel(welcomePanel);
        // assign the StartButtonHandler to the startButton
        StartButtonHandler handler = new StartButtonHandler();
        startButton.addActionListener(handler);
        
        
        // initialize and construct the components in the invaildPanel
        invaildPanel = new JPanel();
        invaildPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        invaildPanel.setLayout(new GridLayout(2,1));
        JLabel invaildMessage = new JLabel(
            "Invalid PIN.", SwingConstants.CENTER);
        invaildMessage.setFont(new Font("Serif", Font.PLAIN, 30));
        JButton invaildButton = new JButton("Exit");
        invaildButton.setFont(new Font("Serif", Font.PLAIN, 30));
        invaildPanel.add(invaildMessage);
        invaildPanel.add(invaildButton);
        invaildButton.addActionListener(
            new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        backToWelcomeWithGreeting();
                    }
                }   
            );
        
        
        // initialize and construct the components in the menuPanel
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2,1));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        JPanel menuButtonMount = new JPanel();
        JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Serif", Font.PLAIN, 30));
        
        // initialize and construct the components in the menuButtonMount
        menuButtonMount.setLayout(new GridLayout(2,2,30,30));
        menuButtons = new JButton[5];
        menuButtons[1] = new JButton("View Balance");
        menuButtons[2] = new JButton("Withdrawal");
        menuButtons[3] = new JButton("Transfer");
        menuButtons[4] = new JButton("Logout");
        MenuButtonHandler menu  = new MenuButtonHandler();
        for ( int i = 1; i < 5; i++ )
        {
            menuButtons[ i ].setFont(new Font("Serif", Font.PLAIN, 20));
            menuButtons[ i ].addActionListener(menu);
        }
        for ( int i = 1; i < 5; i++ )
        {
            menuButtonMount.add(menuButtons[ i ]);
        }
        menuPanel.add(menuTitle);
        menuPanel.add(menuButtonMount);
        
        
    } // end no-argument ATM constructor
    
    // listener for menuButtons[]
    private class MenuButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            for ( int i = 1; i < 5; i++ )
            {
                if (event.getSource() == menuButtons[i])
                {
                    mainMenuSelection = i;
                    performTransactions();
                }
                    
            }
            if (event.getSource() == menuButtons[4])
            {
                backToWelcomeWithGreeting();
            }
                        
        }
    }
    
    // listener for startButton
    private class StartButtonHandler implements ActionListener
    {
        int accountNumber = 0, pin = 0;
        public void actionPerformed( ActionEvent event)
        {
            userAuthenticated = false; // user is not authenticated to start
            currentAccountNumber = 0; // no current account number to start
            screenPanel = new JPanel();
            screenPanel.setLayout(new BorderLayout());
            screenPanel.add(screen, BorderLayout.CENTER);
            loginState = false;
            // new loginPanel for key in the account number
            loginPanel = new JPanel();
            loginPanel.setLayout(new BorderLayout());
            
            // new keypad for key in the account number
            keypad = new Keypad();
            keypad.setTitle("Please insert your ATM card");
            keypad.disableFloating();
            
            // controlPanel for exit the login process or entering
            // initialize and construct the components in the controlPanel
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new GridLayout(2,1));
            JButton cancelButton = new JButton("Cancel");
            JButton enterButton = new JButton ("Enter");
            controlPanel.add(cancelButton);
            controlPanel.add(enterButton);
            loginPanel.add(controlPanel, BorderLayout.EAST);
            loginPanel.add(keypad, BorderLayout.CENTER);
            switchPanel(loginPanel);
            
            // listener for cancelButton
            cancelButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        if (!loginState){
                            backToWelcome();
                        }
                        else
                        {
                            backToWelcomeWithGreeting();
                        }
                    }
                }   
            );
            
            // listener for enterButton
            enterButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        if(!loginState) // if ATM card is inserted
                        {
                            accountNumber = keypad.getInput();
                            if (accountNumber != 0)
                            {
                                loginState = true;
                                keypad = new Keypad();
                                keypad.setTitle("Please enter your PIN");
                                keypad.hideInput();
                                keypad.disableFloating();
                                loginPanel.removeAll();
                                loginPanel.add(keypad, BorderLayout.CENTER);
                                loginPanel.add(controlPanel, BorderLayout.EAST);
                                loginPanel.repaint();
                                loginPanel.revalidate();
                                
                                switchPanel(screenPanel);
                                screen.switchWaitingScreen();
                                Timer timer = new Timer();
                                TimerTask timetask = new TimerTask()
                                {
                                    @Override public void run() {
                                        switchPanel(loginPanel);
                                    }
                                };
                                timer.schedule(timetask,2000);
                            }
                        }
                        else // if ATM card is not inserted
                        {
                            pin = keypad.getInput();
                            if (pin != 0)
                            {
                                loginState = false;
                                userAuthenticated = 
                                    bankDatabase.authenticateUser( accountNumber, pin );
                                if(userAuthenticated) // the account no. and the PIN is correct
                                {
                                    boolean dispenserEmpty = true;
                                    for (int i = 0; i <= 2; i++)
                                    {
                                        int amountOption[] = {100, 500, 1000};
                                        if (cashDispenser.isSufficientCashAvailable(amountOption[i]))
                                        dispenserEmpty = false;
                                    }
                                    if (dispenserEmpty)
                                    {
                                        menuButtons[2].setText("<html><center>Withdrawal is closed<br>Cash dispenser is empty</center></html>");
                                        menuButtons[2].setEnabled(false);
                                    }
                                    else
                                    {
                                        menuButtons[2].setEnabled(true);
                                    }
                                    switchPanel(menuPanel);
                                    currentAccountNumber = accountNumber;
                                }
                                else // either the account no. and the PIN is incorrect
                                {
                                    switchPanel(invaildPanel);
                                }
                            }
                        }
                        
                        
                    }
                }
            );
            
            

        }  
    }
        

    //perform transactions
    private void performTransactions() 
    {
        // local variable to store transaction currently being processed
        Transaction currentTransaction = null;
                    
            // decide how to proceed based on user's menu selection
            switch ( mainMenuSelection )
            {
                // user chose to perform one of three transaction types
                case BALANCE_INQUIRY: 
                case WITHDRAWAL:
                case TRANSFER:
                    // initialize as new object of chosen type
                    currentTransaction = 
                        createTransaction( mainMenuSelection );
                    
                    // initialize transactionPanel to put the currentTransaction in it
                    transactionPanel = new JPanel();
                    transactionPanel.setLayout(new BorderLayout());
                    transactionPanel.add(currentTransaction, BorderLayout.CENTER);
                    switchPanel(transactionPanel);

                    break; 

                default:
                    
                    break;
            } // end switch
        
    } // end method performTransactions
         
    // return object of specified Transaction subclass
    private Transaction createTransaction( int type )
    {
        Transaction temp = null; // temporary Transaction variable
      
        // determine which type of Transaction to create     
        switch ( type )
        {
            case BALANCE_INQUIRY: // create new BalanceInquiry transaction
                temp = new BalanceInquiry( 
                    currentAccountNumber, screen, bankDatabase, ATM.this);
                break;
            case WITHDRAWAL: // create new Withdrawal transaction
                temp = new Withdrawal( currentAccountNumber, screen, 
                    bankDatabase, keypad, cashDispenser, ATM.this);
            break;
            case TRANSFER: // create new Transfer transaction
                temp = new Transfer( currentAccountNumber, screen,
                    bankDatabase, keypad, ATM.this);
            break;
        } // end switch

        return temp; // return the newly created object
    } // end method createTransaction
        
    // switch the certain Panel in mainPanel
    public void switchPanel(JPanel panel){
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        mainPanel.add(panel,BorderLayout.CENTER);
        mainPanel.repaint();
        mainPanel.revalidate();
    } // end method switchPanel
    
    // switch the mainPanel back to menuPanel
    public void backToMenu(){
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        mainPanel.add(menuPanel,BorderLayout.CENTER);
        mainPanel.repaint();
        mainPanel.revalidate();
    } // end method backToMenu
    
    //switch the mainPanel back to welcomePanel
    public void backToWelcome(){
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        mainPanel.add(welcomePanel,BorderLayout.CENTER);
        mainPanel.repaint();
        mainPanel.revalidate();
    } // end method backToWelcome
    
    // switch the mainPanel back to welcomePanel
    // with take card reminder and greeting message
    // delay setting is included
    public void backToWelcomeWithGreeting(){
        screen.switchTakeCardScreen();
        switchPanel(screenPanel);
        Timer timer = new Timer();
        TimerTask timetask = new TimerTask()
        {
            @Override public void run() {
                screen.switchGreetingScreen();
                TimerTask timetask2 = new TimerTask()
                {
                    @Override public void run() {
                        mainPanel.removeAll();
                        mainPanel.repaint();
                        mainPanel.revalidate();
                        mainPanel.add(welcomePanel,BorderLayout.CENTER);
                        mainPanel.repaint();
                        mainPanel.revalidate();
                    }  
                };
                timer.schedule(timetask2,3000); 
            }
        };
        timer.schedule(timetask,3000);
        
    } // end method backToWelcomeWithGreeting
} // end class ATM



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