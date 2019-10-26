import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

/** Class TransactionQueue being able to insert, remove, iterate and peek first element of queue.
*  @return TransactionQueue - an object, a queue of transactions.
*/
class TransactionQueue {
    
    // Adding datastructure to contain the transactions. A queue is FIFO datastructure.
    val TransactionQueue = new mutable.Queue[Transaction];

    // Remove and return the first element from the queue, thread safe
    def pop: Transaction = this.synchronized{this.TransactionQueue.dequeue};

    // Return whether the queue is empty, thread safe
    def isEmpty: Boolean = this.synchronized{this.TransactionQueue.isEmpty}

    // Add new element to the back of the queue, thread safe. Pushing a transaction.
    def push(transaction: Transaction): Unit = this.synchronized{this.TransactionQueue.enqueue(transaction)}

    // Return the first element from the queue without removing it, thread safe
    def peek: Transaction = this.synchronized{this.TransactionQueue.head}

    // Return an iterator to allow you to iterate over the queue, thread safe
    def iterator: Iterator[Transaction] = this.synchronized{this.TransactionQueue.iterator}
}

/** Class Transaction being able to actually do a transaction with specifications (to, from, amount, e.g.).
*  @return Transaction - an object, a transaction (to, from, amound, e.g.)
*/
class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

    var status: TransactionStatus.Value = TransactionStatus.PENDING
    var attempt = 0

    override def run = {


        /** Execute transaction with error handling. 
        */
        def doTransaction: Unit = this.synchronized {
            // Withdraw money and make sure process is valid (Left)
            if (from.withdraw(amount).isLeft)  {
                // Deposit money and make sure process is valid (Left)
                if (to.deposit(amount).isLeft) {
                    // Sucessful transaction
                    this.status = TransactionStatus.SUCCESS;
                } else {
                    from.deposit(amount)
                }
            } 
        }
    
        if (status == TransactionStatus.PENDING && this.attempt < allowedAttemps) {
            this.attempt += 1
            doTransaction
            Thread.sleep(50) // you might want this to make more room for
                            // new transactions to be added to the queue
        } else {
        this.status = TransactionStatus.FAILED;
        }
    }
}