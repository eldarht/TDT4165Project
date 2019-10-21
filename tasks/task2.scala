/** Provides solutions for task 2 a-d. */
object task2 extends App {
	/** Creates a thread that can run a given function.
	 *
	 * This function is a rewrite of the one given in the project_task
	 * Main.scala file.
	 *
	 * @param      function  The function the thread can execute.
	 *
	 * @return     The runnable thread (not yet started).
	 */
	def createThread(function: => Unit): Thread = {      
		val t = new Thread {
            override def run() = function;
        }
        return t;
    }

    /** Counter to be manipulated by two threads
     * 
     * Without synchronization the result might be dependent on a race condition.
     * Meaning that the result varies depending on where the switching of the threads happens
     * or if they execute read/writes at the same time.
     * 
     * This can, as an example, be problematic your exit condition from a loop is i == 0.
     * The counter can be changed twice before the next itteratin, going from i==1 to i==-1,
     * creating an infinite loop.
     */
    private var counter: Int = 0;

    /** Increases the encapsulating objects counter variable.
     * 
     */
	def increaseCounter(): Unit = this.synchronized {
		counter += 1;
	}

	/** Reads a counter.
	 *
	 */
	def readCounter(): Unit = {
		printf("The current value of counter is: %d\n", this.counter);
	}

    val thread = createThread(printf("Thread has been run\n"));
    
    println(s"The created thread is: $thread");

    val increaseThread1 = createThread(increaseCounter());
    val increaseThread2 = createThread(increaseCounter());
    val readThread = createThread(readCounter());

    // Start the counter threads
   	increaseThread1.start();
   	increaseThread2.start();

   	// Wait for the thread to complete
   	increaseThread1.join();
   	increaseThread2.join();

   	// Print result now that both counters are done.
   	readThread.start();


}