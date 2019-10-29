/** Class Bank.
*   @param allowedAttempts - How many attempts allowed for failure.
*/
class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()


    /** Being able add a new transaction to queue and call processTransactions.
    *   @param from - from account
    *   @param to - to account
    *   @param amount - what amount to transact
    *   @return processTransactions - Process transactions concurrently
    */
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        // Creating new transaction object.
        val transaction: Transaction = new Transaction(this.transactionsQueue, this.processedTransactions, from, to, amount, allowedAttempts);

        // Put transaction object in queue
        this.transactionsQueue.push(transaction);

        // Spawning a thread that calls processTransactions
        val processedTransactions: Thread = new Thread {
            override def run = {
                processTransactions;
            }
        }
        processedTransactions.start()
    }

                                                
    /** Processing the transactions. 
    *   @return Unit
    */
    private def processTransactions: Unit = {
        // Return first transaction-element from transactionqueue and remove from queue
        val currentTransaction: Transaction = this.transactionsQueue.pop;

        // Run Transaction, executing the transaction, and check if succeeded.
        currentTransaction.run();

        // If transaction is pending, try to process it again (x3) recursively
        if (currentTransaction.status == TransactionStatus.PENDING) {
            this.transactionsQueue.push(currentTransaction);
            processTransactions;
        // Else if transaction succeed, push processed transaction to processed transaction-queue.
        } else {
            this.processedTransactions.push(currentTransaction);
        }
        }

    /** Add a new account.
    *   @param initialBalance - balance on the new account
    */
    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    /** Create a list from the queue of processed transactions.
    *   @return processedTransactions as list. 
    */
    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
