// SavingAccount.java
// Represents a saving account

public class SavingAccount extends Account
{
	private final static double DEFAULT_INTEREST = 0.001; //default interest rate
	private double interestRate; // store maximum interset ratestore of an account
    public SavingAccount(int theAccountNumber, int thePIN, 
		double theAvailableBalance, double theTotalBalance)
    {
		super(theAccountNumber, thePIN, theAvailableBalance, theTotalBalance);
        // explicit call to superclass Account constructor
		interestRate = DEFAULT_INTEREST; // set the interest rate by default
    }
	
	public SavingAccount(int theAccountNumber, int thePIN, 
		double theAvailableBalance, double theTotalBalance, double interest)
    {
        super(theAccountNumber, thePIN, theAvailableBalance, theTotalBalance);
        // explicit call to superclass Account constructor
		interestRate = interest; // assign input to the interestRate
    }
    
    public void setInterestRate(double input){
			interestRate = input; 
    } // assign input to the interestRate
    
    public double getInterestRate(){
        return interestRate; 
    } // return the interest rate value
} // end class SavingAccount

