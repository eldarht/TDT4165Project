/** Provides solutions for task 2 a-d. */
object task2 extends App {
	

// Task 2: Concurrency in Scala

//a)

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

// b)
// Code snippet from task 2b, incrementing counter:

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
	def increaseCounter(): Unit = {
		counter += 1;
	}

    /** Function for printing out current counter
    *   What is this phenomenon called?
    *       There are two threads sharing the same resource - counter. Whenever a resource is
    *       accessed, it is not protected from other threads - this is called a race condition. The
    *       variable can then be "corrupted".
    *
    *   Give one example of a situation where it can be problematic.
    *       If one is checking for a value, i, to be false, and the race condition makes two threads
    *       increment the value, it may never become false.  
    */
    def printCounter() = {
        printf("The current value of counter is: %d\n", counter);
    }
    
    // Test 2b, printing counter
    // printCounter()
    
    // Creating 3 threads
    val thread1 = createThread(increaseCounter()); //initialize increaseCounter()
    val thread2 = createThread(increaseCounter()); //initialize increaseCounter()
    val thread3 = createThread(printCounter()); // Print function

    // Starting the initializing threads 
    thread1.start();
    thread2.start();
    // Join - wait for completion of initializing threads
    
    thread1.join();
    thread2.join();
    // Run print function 
    thread3.start();
    

// c)

    /** increaseCounter function where it is thread safe: Atomicity
    */
    def increaseCounterAtomicity(): Unit = this.synchronized {
      counter += 1
    }

    /** Function for printing out current counter, thread-safe.
     */
    def printCounterThreadSafe() = {
        printf("The current value of counter is: %d\n", this.counter);
    }

    // Test for 2c
    /*
    // Creating 3 threads
    val thread1 = createThread(increaseCounterAtomicity()); //initialize increaseCounter()
    val thread2 = createThread(increaseCounterAtomicity()); //initialize increaseCounter()
    val thread3 = createThread(printCounter()); // Print function

    // Starting the initializing threads 
    thread1.start();
    thread2.start();
    
    // Join - wait for completion of initializing threads
    thread1.join();
    thread2.join();
    
    // Run print function 
    thread3.start();
    */

// d) 

    /*What is deadlock, and what can be done to prevent it?
      A deadlock is when several processes/threads shares resources, but for some reason, a resource needed
      by one process is locked by another. This may lock the original process and the program
      is in stand-still, unable to proceed, stuck in an infinite wait. 

      Deadlock prevention is how to avoid deadlocks to occur in the system by preventing
      the locking of a resource that may cause deadlocks. 
      Several reasons that deadlocks occur - avoid them: 
      - Mutual exclusion: Avoid mutual exclusion where the resources are shared. 
      - Hold and wait: Prevent a process from keeping a resource when waiting, så that the process can wait but let the resource be used by other processes. Then a waiting process won’t lock resources for processes its waiting for. 
      - No preemption
      - Circular wait
      Avoiding deadlocks by being able to return to a safe state is also preventive. 
    */

    /** Example of deadlock using lazy val. Object A and B has lazy vals depending on each other.
    *  When thread A starts, it will wait for thread B to set the resource A needs. When thread B is created, it
    *  will wait for thread A and a deadlock will occur. 
    */
    def lazyDeadlock () = {
    
      object A {
        lazy val a = 0
        lazy val c = B.b
      }

      object B {
        lazy val b = A.a
      }

      val threadA = createThread(B.b);
      val threadB = createThread(A.c); 

      // Starting the threads
      threadA.start();
      threadB.start();

      // Wait for the threads to complete before printing.
      threadA.join();
      threadB.join();
      println("No deadlock.");
    }

    // Test 2d
    // lazyDeadlock();

}