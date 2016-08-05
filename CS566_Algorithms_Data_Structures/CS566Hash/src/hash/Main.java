package hash;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		
		
		int[] testArray = new int[] {5, 13, 8, 19, 20, 7};
		
		LinearProbing theLinearProbing = new LinearProbing(testArray.length);
		QuadraticProbing theQuadraticProbing = new QuadraticProbing(testArray.length);
		DoubleHashing theDoubleHashing = new DoubleHashing(testArray.length);
		
		for (int t: testArray) {
			theLinearProbing.insert(t, new DataItem(t));
			theQuadraticProbing.insert(t, new DataItem(t));
			theDoubleHashing.insert(t, new DataItem(t));
		}
		
		theLinearProbing.printReport();
		theQuadraticProbing.printReport();
		theDoubleHashing.printReport();
	}
}
