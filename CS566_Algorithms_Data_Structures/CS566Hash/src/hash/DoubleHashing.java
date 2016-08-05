package hash;

public class DoubleHashing extends MyHash {

	public DoubleHashing(int size) {
		super(size);
	}

	public int hashFunc1(int key) {
		return key % arraySize;
	}

	public int hashFunc2(int key) {
		return 1 + (key % (arraySize - 1));
	}

	public void insert(int key, DataItem item) {
		int hashVal = hashFunc1(key); // hash the key
		int stepSize = hashFunc2(key); // get step size
		// until empty cell or -1
		while (hashArray[hashVal] != null && hashArray[hashVal].getKey() != -1) {
			hashVal += stepSize; // add the step
			hashVal %= arraySize; // for wrap-around
			collisionCount++;
		}
		hashArray[hashVal] = item; // insert item
	}

	public void printReport() {
		System.out.println("Double Hashing");
		super.printReport();
	}

}
