// CashDispenser.java
// Represents the cash dispenser of the ATM
public class CashDispenser 
{
    // the default initial number of notes in the cash dispenser
    private final static int INITIAL_COUNT100 = 500, INITIAL_COUNT500 = 500,
    INITIAL_COUNT1000= 500;
    private int count100; // number of HK$100 notes remaining
    private int count500; // number of HK$500 notes remaining
    private int count1000; // number of HK$1000 notes remaining
    private int notes1000required, notes500required, notes100required;
   
   
    // no-argument CashDispenser constructor initializes count to default
    public CashDispenser()
    {
        count100 = INITIAL_COUNT100; // set count100 attribute to default
        count500 = INITIAL_COUNT500; // set count500 attribute to default
        count1000 = INITIAL_COUNT1000; // set count1000 attribute to default
    } // end CashDispenser constructor

    // simulates dispensing of specified amount of cash
    public void dispenseCash()
    {
        // update the count of notes1000
        count1000 -= notes1000required;
        // update the count of notes500
        count500 -= notes500required;
        // update the count of notes100
        count100 -= notes100required;
    } // end method dispenseCash

    // indicates whether cash dispenser can dispense desired amount
    public boolean isSufficientCashAvailable( int amount )
    {
        notes1000required = 0;
        notes500required = 0;
        notes100required = 0;
        while(amount >= 1000 && count1000 >0){
            amount-=1000;
            count1000--;
            notes1000required++;
        }
        // update the count of notes500
        while(amount >= 500 && count500 >0){
            amount-=500;
            count500--;
            notes500required++;
        }
        // update the count of notes100
        while(amount >= 100 && count100 >0){
            amount-=100;
            count100--;
            notes100required++;
        }
        count1000 += notes1000required;
        count500 += notes500required;
        count100 += notes100required;
        if ( amount == 0 )
            return true; // enough notes available
        else 
            return false; // not enough notes available
    } // end method isSufficientCashAvailable
} // end class CashDispenser


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