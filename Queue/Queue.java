package com.Queue;

import java.util.Arrays;
import java.util.Iterator;

public class Queue<E> implements InterfaceQueue<E>{
	public static final int CAPACITY = 1000; 
	// Array used for storage 
	private E[] = data;
	// Front element index
	private int front = 0;
	// Number of elements
	private int size = 0;
//------------------------------------------------
	public Queue(){this(CAPACITY);}
	
	public Queue(int capacity){
		data = (E[]) new Object[capacity];
	}
//------------------------------------------------
	@Override public int size(){return size;}
	
	@Override public boolean isEmpty(){
		return size == 0;
	}
	
	@Override public String toString(){
		return "{data=" + 
			Arrays.toString(data) + 
			"}"
	} 

    	@Override public E first(){
	        if (isEmpty()) return null;
       		return data[front];
	}

    	@Override public void enqueue(E e) throws IllegalStateException{
        	if (size == data.length)
            		throw new IllegalStateException("Queue is full");
        	int current = (front + size) % data.length; // use modular arithmetic
        	data[current] = e;
        	size++;
	}

    	@Override public E dequeue( ) {
        	if (isEmpty()) return null;

        	E answer = data[front];
        	data[front] = null; // dereference to help garbage collection
        	front = (front + 1) % data.length;
        	size--;
        	return answer;
    }
