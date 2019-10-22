import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies

    /** Withdraws an amount from the accounts balance
     *
     *  The function assures that one can not withdraw more
     *  than what is available in the account and that the
     *  withdrawn amount is positive (not a deposit).
     *  
     * @param      amount  The amount
     * 
     * @return     Either right if success or left if not.
     */
    def withdraw(amount: Double): Either[String, String] = this.synchronized{ 
        if(this.balance.amount >= amount && amount > 0.0 ){
            this.balance.amount -= amount;
            return Left("Transaction Complete");
        }else{
            return Right("Invalid amount");
        }
    }

    /** Deposits an amount to the accounts balance
     *
     *  The function assures that one can not deposit
     *  a negative amount (not a withdrawal).
     *  
     * @param      amount  The amount
     * 
     * @return     Either right if success or left if not.
     */
    def deposit (amount: Double): Either[String, String] = this.synchronized{
        if(amount > 0){
            this.balance.amount += amount;
            return Left("Transaction complete")
        }else{
            return Right("Invalid amount");
        }
    } 

    /** Gets the balance amount.
     *
     * @return     The balance amount.
     */
    def getBalanceAmount: Double       = this.synchronized{
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
