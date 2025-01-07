// BalanceInquiry.java
// Represents a balance inquiry ATM transaction
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BalanceInquiry extends Transaction
{
   private ATM theATM;
   private JPanel balanceInquiryPanel;
   
   // BalanceInquiry constructor
   public BalanceInquiry( int userAccountNumber, Screen atmScreen, 
      BankDatabase atmBankDatabase, ATM atm)
   {
      super( userAccountNumber, atmScreen, atmBankDatabase);
      
      BankDatabase bankDatabase = getBankDatabase();
      Screen screen = getScreen();
      theATM = atm;
      
      // get the available balance for the account involved
      double availableBalance = 
         bankDatabase.getAvailableBalance( getAccountNumber() );
      
      // get the total balance for the account involved
      double totalBalance = 
         bankDatabase.getTotalBalance( getAccountNumber() );
      
      // assign the BorderLayout manager to the class
      setLayout(new BorderLayout());
      
      // initialize and setup the layout of the balanceInquiryPanel
      balanceInquiryPanel = new JPanel();
      balanceInquiryPanel.setLayout(new GridLayout(4,1));
      balanceInquiryPanel.setBorder(BorderFactory.createEmptyBorder(150, 100, 150, 100));
      
      //create and initialize the elements in the balanceInquiryPanel
      // show the "Balance Information:" in the Panel
      JLabel title = new JLabel("Balance Information:",SwingConstants.CENTER);
      title.setFont(new Font("Serif", Font.PLAIN, 30));
      
      // show the available balance of an account
      JLabel available = new JLabel("-Available balance: HK$" +
            String.format("%.2f",availableBalance ),SwingConstants.CENTER);
      available.setFont(new Font("Serif", Font.PLAIN, 30));
      
      // show the total balance of an account
      JLabel total = new JLabel("-Total balance: HK$"+ 
            String.format("%.2f",totalBalance ),SwingConstants.CENTER);
      total.setFont(new Font("Serif", Font.PLAIN, 30));
      
      // uses to go back to the main menu
      JButton exitButton = new JButton("Back To Main Menu");
      exitButton.setFont(new Font("Serif", Font.PLAIN, 30));
      
      // the action listener of the exitbutton
      // when button is clicked, go back to the main menu of the ATM
      exitButton.addActionListener(
            new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        theATM.backToMenu();
                    }

                }
   
            );
      
      // add all the elements into the balanceInquiryPanel
      balanceInquiryPanel.add(title);
      balanceInquiryPanel.add(available);
      balanceInquiryPanel.add(total);
      balanceInquiryPanel.add(exitButton);
      
      // add the balanceInquiryPanel to the class JPanel
      add(balanceInquiryPanel, BorderLayout.CENTER);
   } // end BalanceInquiry constructor
} // end class BalanceInquiry



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