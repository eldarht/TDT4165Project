class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    /** Adds a transaction to queue.
     *
     * @param      from    The account to take from
     * @param      to      The account to send to
     * @param      amount  The amount to send
     */
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
         val transaction: Transaction = new Transaction(transactionsQueue, null,
            from, to, amount, allowedAttempts
        );

        this.transactionsQueue.push(transaction);
        
        val t: Thread = new Thread { 
            override def run() = processTransactions;
        }

        t.start;
    }

    /** Processes a transaction from the queue and logs it in processedTransactions
     *
     */
    private def processTransactions: Unit = {

        val transaction: Transaction = this.transactionsQueue.pop;
        transaction.run();
        this.processedTransactions.push(transaction);
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
