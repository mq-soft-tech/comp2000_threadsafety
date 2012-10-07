/**
 * This file is part of a project entitled Week9Samples which is provided
 * as sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2012 Dominic Verity and Macquarie University.
 * 
 * Week9Samples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Week9Samples is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Week9Samples. (See files COPYING and COPYING.LESSER.) If
 * not, see <http://www.gnu.org/licenses/>.
 */

package org.macquarie.prodcons;

/**
 * A thread safe bounded buffer class - which uses a very simple
 * array based queue implementation. Demonstrates the use of
 * synchronisation to ensure consistency and wait() and notify() 
 * methods to handle overflow and underflow situations.
 * 
 * Buffers of this type contain integers.
 * 
 * See lecture notes and Jia chapter 11 for more information.
 * Also it is worth looking up Producer-Consumer on Wikipedia.
 * 
 * @author Dominic Verity
 *
 */
public class BoundedBuffer {

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
	private int[] mValues;
	
	/**
	 * Index of the first free location in the buffer.
	 */
	private int mNext;
	
	// Constructors
	
	/**
	 * Default constructor - makes an empty buffer.
	 */
	BoundedBuffer () {
		mValues = new int[CAPACITY];
		mNext = 0;
	}
	
	// Public methods
	
	/**
	 * Add a new value to the queue.
	 */
	public synchronized void put(int pValue) throws InterruptedException {
		// First check to see if there is space in the buffer.
		while (mNext >= CAPACITY) {
			System.out.println("Waiting for some buffer space!");
			wait();		// If there is no space then wait in the wait
						// queue of this buffer object.
		}
		
		// Now we know there is space so add the new value.
		mValues[mNext] = pValue;
		
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
	public synchronized int get() throws InterruptedException {
		// First check to see if there is anything in the buffer.
		while (mNext <= 0) {
			System.out.println("Waiting for a value to become available!");
			wait();		// If there is nothing there then wait in the wait
						// queue of this buffer object
		}
		
		// Now we know that a value is present, so update the next pointer
		mNext--;
		
		// Finally notify all waiting threads. This will wake each thread in turn,
		// and if one of those is waiting for a space to become available in the buffer
		// it will proceed.
		
		// We could optimise this by only calling notifyAll() when the buffer was
		// previously full.
		notifyAll();
		
		
		// And return the retrieved value.
		return (mValues[mNext]);
	}
}
