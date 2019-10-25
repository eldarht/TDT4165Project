import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO
    // project task 1.1
    // Add datastructure to contain the transactions

    /**A queue with transaction jobs
     * 
     */
    private val transactionQueue = new mutable.Queue[Transaction];

    /** Remove and return the first element from the queue
     *
     * @return     Unhandled transaction
     */
    def pop: Transaction = this.synchronized{this.transactionQueue.dequeue()};


    /** Return whether the queue is empty
     *
     * @return     True if empty, False otherwise.
     */
    def isEmpty: Boolean = this.synchronized{this.transactionQueue.isEmpty};

    /** Add new element to the back of the queue
     *
     * @param      t     A transaction job
     */
    def push(t: Transaction): Unit = this.synchronized{this.transactionQueue.enqueue(t)};

    /** Return the first element from the queue without removing it
     *
     * @return     A transaction job
     */
    def peek: Transaction = this.synchronized{this.transactionQueue.head};

    /** Return an iterator to allow you to iterate over the queue
     *
     * @return     An itterator
     */
    def iterator: Iterator[Transaction] = this.synchronized{this.transactionQueue.iterator}
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {

      /** Executes a transaction. Rolling back changes if action can not be completed.
       * 
       */
      def doTransaction() = {

          val errWithdrawn =  from.withdraw(amount);

          if(!errWithdrawn.isRight){
            val errDeposit = to.deposit(amount);
            
            if(errDeposit.isRight){
              from.deposit(amount);
            }
          }
      }

      // TODO - project task 3
      // make the code below thread safe
      if (status == TransactionStatus.PENDING) {
          doTransaction
          Thread.sleep(50) // you might want this to make more room for
                           // new transactions to be added to the queue
      }


    }
}
