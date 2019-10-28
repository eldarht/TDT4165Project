/** Provides solutions for task 1 a-d. */

object task1 extends App {


//Task 1: Scala introduction
// a)

	/** Create array with elements 0-50.
     *  @return array - array consisting of Ints 0 to 50.
	 */
	def createArray(): Array[Int] = {
      // Array of ints that holds up to 51 elements
      var array = new Array[Int](51)
      for (x <- 0 to 50){
        array(x) = x
      }
      return array;
    }
    //Test 1a:
    printf("The array is: %s\n", createArray().mkString(" "));

// b)

	/** Uses a for loop to get the sum of an Int array.
	 *
	 * @param a Array to sum.
	 *
	 * @return Sum of array.
	 */
	def sumOfArrayElements(a: Array[Int]): Int = {
		var result : Int = 0;
		for(i <- 0 to a.length-1){
			result += a(i);
		}
		return result;
	}
    //Test 1b:
    printf("The sum of the array is: %d\n", sumOfArrayElements(createArray()));
	
// c) 

	/** Uses a for recursion to get the sum of an Int array.
	 *
	 * @param a Array to sum
	 *
	 * @return Sum of array
	 */
	def recursiveSumOfArray(a: Array[Int]): Int = {
		if(a.length > 1){
			a(0) + recursiveSumOfArray(a.drop(1));
		}else{
			a(0);
		}
	}
    //Test 1c:
    printf("The sum of the array is: %d\n", recursiveSumOfArray(createArray()));
	
// d) 

	/** Naive calculation of the nth fibonacci number without optimization.
	 *
	 * BigInt is different from Int in that it can take significantly larger numbers.
	 * 
	 * @param n The position of the number in the set of fibonacci numbers to be computed.
	 *
	 * @return The nth fibonacci number.
	 */
	def fibonacci(n: BigInt): BigInt = {
		if(n == 0){
			return 0;
		}else if(n == 1){
			return 1;
		}else{
			return fibonacci(n-1) + fibonacci(n-2);
		}
	}
    
    // Test 1d:
    printf("The 10th fibonacci number is: %d\n", fibonacci(10));
  
}

