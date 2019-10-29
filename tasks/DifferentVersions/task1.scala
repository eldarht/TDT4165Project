/** Provides solutions for task 1 a-d. 
 * @author Eldar Hauge Torkelsen
 */
object task1 extends App {

	/** Uses a for loop to get the sum of an Int array.
	 *
	 * @param a Array to sum
	 *
	 * @return Sum of array
	 */
	def sumFor(a: Array[Int]): Int = {
		var result : Int = 0;

		for(i <- 0 to a.length-1){
			result += a(i);
		}
		return result;
	}

	/** Uses a for recursion to get the sum of an Int array.
	 *
	 * @param a Array to sum
	 *
	 * @return Sum of array
	 */
	def sumRecursive(a: Array[Int]): Int = {
		if(a.length > 1){
			a(0) + sumRecursive(a.drop(1));
		}else{
			a(0);
		}
	}

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

	val a = new Array[Int](50);

	for(i <- 1 to 50){
		a(i-1)= i;
	}

	val result: String = a.mkString(", ");

	println(s"The array is: [$result]");
	printf("The sum of the array is: %d\n", sumFor(a));
	printf("The sum of the array is: %d\n", sumRecursive(a));
	printf("The 10th fibonacci number is: %d\n", fibonacci(10));
  
}

