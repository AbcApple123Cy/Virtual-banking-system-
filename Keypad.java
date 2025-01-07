// Keypad.java
// Represents the keypad of the ATM
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Keypad extends JPanel
{
    private String temp; // temporary variable for storing input
	private JPanel display; // containing the title and the input field
    private JTextArea title; // the title of the keypad
	private JTextField inputValue; // input field with no mask
    private JPasswordField hidedValue; // input field with mask
	private JPanel keysmount; // containing the keypad buttons
    private JButton keys[]; // the keypad buttons
	

    public Keypad()
    {
        temp = "";
		
		// assign the BorderLayout manager to the class
		setLayout(new BorderLayout());
		
		// Panel for containing all the components of the keypad
        JPanel keypad = new JPanel();
        
		// Create and initialize the elements in the keypad
		
		// Panel with GridLayout for containing the title and the input fields
		display = new JPanel();
        display.setLayout(new GridLayout(2 , 1, 0, 50));
        display.setBorder(BorderFactory.createEmptyBorder(30, 80, 20, 80));
		
		// Create and initialize the components in the display
		title = new JTextArea(3,20);
		title.setFont(new Font("Serif", Font.PLAIN, 30));
        title.setEditable(false);
        inputValue = new JTextField();
		inputValue.setFont(new Font("Serif", Font.PLAIN, 30));
		inputValue.setEditable(false);
        hidedValue = new JPasswordField();
        hidedValue.setFont(new Font("Serif", Font.PLAIN, 30));
        hidedValue.setEditable(false);
		
		// Add the default components to display
        display.add(title);
        display.add(inputValue);
		
		// Create and initialize the components in the keysmount
        keysmount = new JPanel();
        keysmount.setLayout(new GridLayout(4,3, 15, 15));
        keysmount.setBorder(BorderFactory.createEmptyBorder(50, 80, 30, 80));
        keys = new JButton[ 16 ];
        for ( int i = 0; i < 10; i++ )
        {
            keys[ i ] = new JButton(String.valueOf( i ));
        }
        keys[ 11 ] = new JButton("Clear");
        keys[ 10 ] = new JButton(".");
		
		// Add the components to keysmount
        for ( int i = 7; i <= 9; i++ )
        keysmount.add( keys[ i ] );
        for ( int i = 4; i <= 6; i++ )
        keysmount.add( keys[ i ] );
        for ( int i = 1; i <= 3; i++ )
        keysmount.add( keys[ i ] );
        keysmount.add( keys[ 0 ] );
        keysmount.add( keys[ 10 ] );
        keysmount.add( keys[ 11 ] );
        
		// Add the components to the class Panel
        add(display, BorderLayout.NORTH);
        add(keysmount, BorderLayout.CENTER);
        
		// Add listener for the number button
        for ( int i = 0; i <= 9; i++ )
        {
            keys[i].addActionListener(
                    new ActionListener()
                    {
                        public void actionPerformed( ActionEvent event )
                        {
                            temp += event.getActionCommand();
                            inputValue.setText(temp);
                            hidedValue.setText(temp);
                        }
                    }
            );
        }

        // Add listener for "." Floating Point Button
        keys[10].addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        // Ignore if current input is empty
                        if(temp.equals("")) return;

                        temp += event.getActionCommand();
                        inputValue.setText(temp);
                        hidedValue.setText(temp);
                        // Not allow duplicates of floating-point
                        disableFloating();
                    }
                }
        );
        
        // Add listener for Clear Button
        keys[11].addActionListener(
            new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        temp = "";
                        inputValue.setText(temp);
                        hidedValue.setText(temp);
                    }
                }
        );
        
        
    } // end no-argument Keypad constructor

    // return an integer value entered by user 
    public int getInput()
    {
        try{
            return Integer.parseInt(temp); 
        }catch (NumberFormatException numberFormatException)
        {
            return 0;
        }
    } // end method getInput

	// return an double value entered by user 
    public double getDouble()
    {
        try{
            return Double.parseDouble(temp); 
        }catch (NumberFormatException numberFormatException)
        {
            return 0;
        }
         
    } // end method getDouble
	
	// return an String value entered by user
	public String getString()
    {
        return temp;
    } // end method getString
	
	// set the title of the keypad
    public void setTitle(String t)
    {
        title.setText(t);
    }
    
	// disable the "." button of the keypad
    public void disableFloating()
    {
        keys[ 10 ].setEnabled(false);
    }// end method disableFloating
    
	// replace input field with the masked input field
    public void hideInput()
    {
        display.removeAll();
        display.repaint();
        display.revalidate();
        display.add(title);
        display.add(hidedValue);
        display.repaint();
        display.revalidate();
    } // end method hideInput
	
	// set the font of the title in the keypad
	public void setTitleFontSize(int size) {
        title.setFont(new Font("Serif", Font.PLAIN, size));
    } // end method setTitleFontSize
	
	// change the action listener of clear button
	// allow it to enable the the "." button when clear button clicked
	public void allowClearToResetFloaingPoint(){
        for(ActionListener listener : keys[11].getActionListeners())
        {
            keys[11].removeActionListener(listener);
        }

        keys[11].addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent event )
                    {
                        temp = "";
                        inputValue.setText(temp);
                        hidedValue.setText(temp);

                        // Enable Floating Button if it has been disabled
                        if(keys[10].isEnabled() == false) {
                            keys[10].setEnabled(true);
                        }
                    }
                }
        );
    }
} // end class Keypad  



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