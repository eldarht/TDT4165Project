import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    /**A queue with transaction jobs
     * Implements project task 1.1 partially
     */
    private val transactionQueue = new mutable.Queue[Transaction];

    /** Remove and return the first element from the queue
     * Implements project task 1.1 partially
     * 
     * @return     Unhandled transaction
     */
    def pop: Transaction = this.synchronized{this.transactionQueue.dequeue()};


    /** Return whether the queue is empty
     * Implements project task 1.1 partially
     * 
     * @return     True if empty, False otherwise.
     */
    def isEmpty: Boolean = this.synchronized{this.transactionQueue.isEmpty};

    /** Add new element to the back of the queue
     * Implements project task 1.1 partially
     * 
     * @param      t     A transaction job
     */
    def push(t: Transaction): Unit = this.synchronized{this.transactionQueue.enqueue(t)};

    /** Return the first element from the queue without removing it
     * Implements project task 1.1 partially
     * 
     * @return     A transaction job
     */
    def peek: Transaction = this.synchronized{this.transactionQueue.head};

    /** Return an iterator to allow you to iterate over the queue
     * Implements project task 1.1 partially
     * 
     * @return     An itterator
     */
    def iterator: Iterator[Transaction] = this.synchronized{this.transactionQueue.iterator}
}

/**  This class describes a transaction.
 * 
 * @constructor Creates a transaction with its state and transfer info.
 * 
 * @param transactionsQueue The queue this transaction is part of before completion
 * @param processedTransactions The queue this transaction is part of after competion
 * @param from The account to withdraw money from
 * @param to The account to deposit money to
 * @param amount The amount to transfer 
 * @param allowedAttemps The number of times the transaction can fail before giving up.
 * 
 */
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
       * The synchronization should be unnecessary as it only locks on an instanse of itself
       * (not the class) and there should only be one transaction of this instance active.
       *
       * As the deposit part of the transaction is mostly independent of state there should not 
       * be any state where its withdrawal is successful, but it can not deposit, even if context
       * switching occures in between.
       * 
       */
      def doTransaction() = this.synchronized{

        // Withdraw from money from first account
        if(from.withdraw(amount).isLeft){
          
          if(to.deposit(amount).isLeft){
            // Indicate that transaction is completed.
            this.status = TransactionStatus.SUCCESS;
          }else{
            // Deposit withdrawn amount back if unsuccessful
            from.deposit(amount);
          }
        }
      }

      if(status == TransactionStatus.PENDING && this.attempt < this.allowedAttemps) {
          this.attempt +=1;

          doTransaction
          Thread.sleep(50) // you might want this to make more room for
                           // new transactions to be added to the queue
      }else{
        this.status = TransactionStatus.FAILED;
      }
    }
}
