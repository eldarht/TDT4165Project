// My version of project tasks

object Main extends App {

//Task 1: Scala introduction
// a)
    def createArray () = {
      // Array of ints that holds up to 51 elements
      var array = new Array[Int](51)
      for (x <- 0 to 50){
        array(x) = x
      }
      println(array.mkString(" "))
    }
    //Test 1a:
    //createArray()

// b)

    def sumOfArrayElements (list: Array[Int]) = {
      val arrayLength = list.length
      var sum = 0
      for (i <- 0 to arrayLength-1) {
        sum = sum + list(i)
      }
      println(sum)
    }
    //Test 1b:
    //sumOfArrayElements (Array(0,1,2,3,4))

// c)
    def recursiveSumOfArray(list: Array[Int]): Int = {
      if (list.isEmpty) 0
      else list.head + recursiveSumOfArray(list.tail)
    }

    //Test 1c:
    //println(recursiveSumOfArray(Array(0,1,2,3,4)))

// d)
    //Add the two numbers before to list. Starting with 0,1...
    def nthFibonacci(n: Int): BigInt = {
      if (n < 2)
        n
      else
        nthFibonacci(n-2)+nthFibonacci(n-1)
    }

    //Test 1d:
    //println(nthFibonacci(10))
    // Difference between Big Int and Int: BigInt has larger precision, can contain larger numbers
    // than an Int.

// Task 2: Concurrency in Scala

  //a)
    def createThread(function: => Unit): Thread = {
      val thread = new Thread {
        override def run = function
      }
      return thread
    }

    // b)
    // Code snippet from task 2b, incrementing counter:
    private var counter: Int = 0
    def increaseCounter(): Unit = {
      counter += 1
    }
  
    // Function for printing out current counter
    def printCounter() = {
        println(counter)
    }
    // Test 2b), printing counter
    // printCounter()
    /*
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
    */
    /* Running the program a few times, noticing print output: 
      1: 2
      2: 2
      3: 2
      4: 2
    
    What is this phenomenon called?
      There are two threads sharing the same resource - counter. Whenever a resource is
      accessed, it is not protected from other threads - this is called a race condition. The
      variable can then be "corrupted".

    Give one example of a situation where it can be problematic.
      If one is checking for a value, i, to be false, and the race condition makes two threads
      increment the value, it may never become false. 
    */
    
    // c)
    // increaseCounter function where it is thread safe: Atomicity
    def increaseCounterAtomicity(): Unit = this.synchronized {
      counter += 1
    }

    // Function for printing out current counter, thread-safe
    def printCounterThreadSafe() = {
        println(this.counter)
    }
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

    // Example of deadlock using lazy val

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
      println(A.c);
    }

    // Test 2d)
    //lazyDeadlock()

}
