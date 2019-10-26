import exceptions._

/** Class Account
*/
class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    /** Withdrawing an amount from the current balance-objects balace as long as its a positive amount
    *   and it is enough balance on the account. 
    *   @param amount - amount to be withdrawn from account. 
    *   @return balance of the account after withdraw or fail (Right)
    */
    def withdraw(amount: Double):  Either[Double,String] = this.synchronized {
        if (this.balance.amount - amount >= 0.0 && amount >= 0.0) {
            // Return successfull withdrawal
            this.balance.amount -= amount
            Left(this.balance.amount);
        }
        else {
            // Return unsuccessfull withdrawal
            Right("Withdrawal failed because of negative amount or insufficient funds on account.");
         }
    }


    /** Deposit an amount to the current balance-objects balace as long as its a positive amount.
    *   @param amount - the amount to be deposited
    *   @return balance of the account after deposit or fail (Right)
    */
    def deposit (amount: Double): Either[Double,String]= this.synchronized {
        if (amount < 0.0) {
            // Return unsuccessfull deposit
            Right("Deposit failed due to invalid amount.")
        }
        else {
            // Return successfull deposit
            this.balance.amount += amount;
            Left(this.balance.amount);
        }
    } 
    

    /** Check current balance of the account (the amount in balance object).
    *   @return balance of the account.
    */
    def getBalanceAmount: Double = this.synchronized {
        this.balance.amount;
    }

    /** Transfer amount to account.
    *   @param account - account to transfer amount to
    *   @param amount - amount to transfer to account.
    *   @return bank 
    */
    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }

}
