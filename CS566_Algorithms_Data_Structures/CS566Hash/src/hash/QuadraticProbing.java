package hash;

public class QuadraticProbing extends MyHash {

	public QuadraticProbing(int size) {
		super(size);
	}

	public int hashFunc1(int key) {
		return key % arraySize;
	}

	public int hashFunc2(int key, int count) {
		return (key + count * count) % arraySize;
	}

	public void insert(int key, DataItem item) {
		int count = 0;
		int hashVal = hashFunc1(key); // hash the key
		
		// until empty cell or -1
		while (hashArray[hashVal] != null && hashArray[hashVal].getKey() != -1) {
			count++;
			int stepSize = hashFunc2(key, count); // get step size
			hashVal += stepSize; // add the step
			hashVal %= arraySize; // for wrap-around
			collisionCount++;
		}
		hashArray[hashVal] = item; // insert item
	}

	public void printReport() {
		System.out.println("Quadratic Probing");
		super.printReport();
	}

}
