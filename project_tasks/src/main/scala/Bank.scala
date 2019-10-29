/** This class describes a bank.
 * 
 * @constructor Sets the number of times a transaction might fail.
 * @param allowedAttempts The number of fails before the transaction is discarded.
 */
class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    /** Adds a transaction to queue and starts processing in other thread.
     *
     * @param      from    The account to take from
     * @param      to      The account to send to
     * @param      amount  The amount to send
     */
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        // Creating new transaction object.
        val transaction: Transaction = new Transaction(transactionsQueue, null,
            from, to, amount, allowedAttempts
        );

        // Put transaction object in queue
        this.transactionsQueue.push(transaction);
        
        // Spawning a thread that calls processTransactions
        val transactionThread: Thread = new Thread { 
            override def run() = processTransactions;
        }

        transactionThread.start;
    }

    /** Processes a transaction from the queue and logs it in processedTransactions
     *
     *  Assumes that the transactionsQueue is not empty. It will not be empty
     *  if used correctly as this function is only called by addTransactionToQueue after
     *  a transaction is added or by itself after readding the failed transaction.
     */
    private def processTransactions: Unit = {

        // Start the the next transaction
        val transaction: Transaction = this.transactionsQueue.pop;
        transaction.run();

        // Check if transaction is incomplete
        if(transaction.status == TransactionStatus.PENDING){
            //Schedule the transaction for later when the state might be sufficient.
            this.transactionsQueue.push(transaction);

            //Start the next scheduled transaction
            this.processTransactions;
        }else{

            this.processedTransactions.push(transaction);
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
