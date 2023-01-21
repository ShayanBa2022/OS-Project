package com.Queue

public interface InterfaceQueue<E>{
	
	// Returns the size of queue!
	int size();  

	boolean isEmpty();

	// Add element at the end of the queue.
	void enqueue(E element);

	// Remove and Returns first element. (Null is basecase)
	E dequeue();

	// Return first element. (Null is empty)
	E first();
}
