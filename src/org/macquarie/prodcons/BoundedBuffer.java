/**
 * This file is part of a project entitled ThreadSafety which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP2000 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2021 Dominic Verity and Macquarie University.
 * 
 * ThreadSafety is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * ThreadSafety is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ThreadSafety. (See files COPYING and COPYING.LESSER.) If not,
 * see <http://www.gnu.org/licenses/>.
 */

package org.macquarie.prodcons;

/**
 * A thread safe bounded buffer class - which uses a very simple
 * implementation which treats a fixed size array of values as
 * a circular buffer.
 * 
 * Demonstrates the use of synchronisation to ensure consistency 
 * and {@link #wait()} and {@link #notifyAll()} methods to handle 
 * overflow and underflow situations.
 * 
 * Buffers of this type are generic, so can hold values of any
 * (non-primitive) type.
 * 
 * See lecture notes for more information. Also it is worth looking 
 * up Producer-Consumer on Wikipedia.
 * 
 * @author Dominic Verity
 *
 */
public class BoundedBuffer<T> {

	// Static data members (constants)
	
	/**
	 * Maximum capacity of our buffer, this is intentionally quite small so
	 * to ensure that underflows and overflows occur relatively frequently.
	 */
	private final static int CAPACITY = 3;
	
	// Data members (instance variables)

	/**
	 * The array of values stored in this buffer.
	 */
	private T[] mValues;
	
	/**
	 * Index of the first free location in the buffer.
	 */
	private int mNext;
	
	/**
	 * Index of value at head of the queue.
	 */
	private int mHead;
	
	// Constructors
	
	/**
	 * Default constructor - makes an empty buffer.
	 */
	@SuppressWarnings("unchecked")
	public BoundedBuffer () {
		mValues = (T[]) new Object[CAPACITY];
		mNext = 0;
		mHead = 0;
	}
	
	// Public methods
	
	/**
	 * Add a new value to the queue.
	 */
	public synchronized void put(T pValue) throws InterruptedException {
		// First check to see if there is space in the buffer.
		while (mNext - mHead >= CAPACITY) {
			System.out.println("Waiting for some buffer space!");
			wait();		// If there is no space then wait in the wait
						// queue of this buffer object.
		}
		
		// Now we know there is space so add the new value.
		mValues[mNext % CAPACITY] = pValue;
		
		// And update next pointer.
		mNext++;
		
		// Finally notify all waiting threads. This will wake each thread in turn,
		// and if one of those is waiting for a value to be placed into the buffer
		// it will proceed.
		
		// We could optimise this by only calling notifyAll() when the buffer was
		// previously empty.
		notifyAll();
	}
	
	/**
	 * Remove a value from the queue and return it.
	 */
	public synchronized T get() throws InterruptedException {
		// First check to see if there is anything in the buffer.
		while (mNext - mHead <= 0) {
			System.out.println("Waiting for a value to become available!");
			wait();		// If there is nothing there then wait in the wait
						// queue of this buffer object
		}
		
		
		
		// Now we know that a value is present, so get the head value
		T vResult = mValues[mHead++];
		
		// Adjust if we've wrapped around in the buffer.
		if (mHead >= CAPACITY) {
			mHead -= CAPACITY;
			mNext -= CAPACITY;
		}
		
		// Finally notify all waiting threads. This will wake each thread in turn,
		// and if one of those is waiting for a space to become available in the buffer
		// it will proceed.
		
		// We could optimise this by only calling notifyAll() when the buffer was
		// previously full.
		notifyAll();
		
		
		// And return the retrieved value.
		return vResult;
	}
}
