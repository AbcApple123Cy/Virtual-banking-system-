// ChequeAccount.java
// Represents a cheque account

public class ChequeAccount extends Account
{
    private final static double DEFAULT_LIMIT = 10000.0; // default cheque limit
    private double chequeLimit; // store maximum cheque limit of a account
    public ChequeAccount(int theAccountNumber, int thePIN, 
      double theAvailableBalance, double theTotalBalance)
    {
        super(theAccountNumber, thePIN, 
      theAvailableBalance, theTotalBalance);
      // explicit call to superclass Account constructor
	  
      chequeLimit = DEFAULT_LIMIT; // set the interest rate by default
    
	}
	
	public ChequeAccount(int theAccountNumber, int thePIN, 
      double theAvailableBalance, double theTotalBalance, double limit)
    {
        super(theAccountNumber, thePIN, 
      theAvailableBalance, theTotalBalance);
      // explicit call to superclass Account constructor
        
      chequeLimit = limit; // assgin limit to the chequeLimit
    
	}
    
	public void setChequeLimit(double input){
        
			chequeLimit = input;
        
    }//set the value for user cheque limit
             
    public double getChequeLimit()
    {
    
      return chequeLimit; 
        
    } // return the cheque limit value
} //end class ChequeAccount
 
