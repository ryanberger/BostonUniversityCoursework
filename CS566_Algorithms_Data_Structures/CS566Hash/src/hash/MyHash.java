package hash;

public abstract class MyHash {
	protected DataItem[] hashArray;
	protected int origArraySize;
	protected int arraySize;
	protected int collisionCount = 0;
	
	public MyHash(int size) {
		origArraySize = size;
		arraySize = (int) Math.round(1.2 * size);
		hashArray = new DataItem[arraySize];
	}
	
	public void printReport() {
		System.out.println("---------------------");
		System.out.println("# of collisions: " + collisionCount);
		System.out.println("Average # of collisions: " + (double)collisionCount/origArraySize);
		System.out.println();
	}

	public abstract void insert(int key, DataItem item);
}
