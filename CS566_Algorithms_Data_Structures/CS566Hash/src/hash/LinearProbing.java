package hash;

public class LinearProbing extends MyHash {

	public LinearProbing(int size) {
		super(size);
	}

	public int hashFunc1(int key) {
		return key % arraySize;
	}

	public void insert(int key, DataItem item) {
		int hashVal = hashFunc1(key); // hash the key
		int stepSize = 1;
		
		// until empty cell or -1
		while (hashArray[hashVal] != null && hashArray[hashVal].getKey() != -1) {
			hashVal += stepSize; // add the step
			hashVal %= arraySize; // for wrap-around
			collisionCount++;
		}
		hashArray[hashVal] = item; // insert item
	}

	public void printReport() {
		System.out.println("Linear Probing");
		super.printReport();
	}

}
