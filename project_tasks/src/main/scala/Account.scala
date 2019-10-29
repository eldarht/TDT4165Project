import exceptions._

/** This class describes a bank account.
 * @constructor sets the bank the account belongs to and the account balance
 * 
 * @param bank The bank the account belongs to
 * @param initialBalance The initial balance in the account
 */
class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    /** Withdraws an amount from the accounts balance
     *
     *  The function assures that one can not withdraw more
     *  than what is available in the account and that the
     *  withdrawn amount is positive (not a deposit).
     *  
     *  Implements task 1.2 and 1.3
     *  
     * @param      amount  The amount to be withdrawn from account
     * 
     * @return     Either left with remaining balance if success or right with message.
     */
    def withdraw(amount: Double): Either[Double, String] = this.synchronized{ 
        if(this.balance.amount >= amount && amount > 0.0 ){
            this.balance.amount -= amount;
            return Left(this.balance.amount);
        }else{
            return Right("Invalid amount");
        }
    }

    /** Deposits an amount to the accounts balance
     *
     *  The function assures that one can not deposit
     *  a negative amount (not a withdrawal).
     *  
     * @param      amount  The amount to be deposited
     * 
     * @return     Either left with new balance if success or right with message.
     */
    def deposit (amount: Double): Either[Double, String] = this.synchronized{
        if(amount > 0.0){
            this.balance.amount += amount;
            return Left(this.balance.amount);
        }else{
            return Right("Invalid amount");
        }
    } 

    /** Gets the current balance.
     *
     * @return     The balance.
     */
    def getBalanceAmount: Double = this.synchronized{
        return this.balance.amount;
    }

    /** Schedules a transfer from this account to an other
     *
     * @param      account  The other account
     * @param      amount   The amount to transfer
     */
    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }
}
