/**
 * This file is part of a project entitled Week9Samples which is provided
 * as sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011 Dominic Verity and Macquarie University.
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

import java.util.Random;

/**
 * A simple consumer class, waits for a random period of time and then
 * gets a value from the buffer and prints it to the terminal.
 * 
 * @author Dominic Verity
 *
 */
public class Consumer extends Thread {
	
	// Data members (instance variables)
	
	private BoundedBuffer mBuffer;
	
	// Constructors
	
	/**
	 * Construct a consumer which sources the values it consumes
	 * from a specified bounded buffer.
	 * 
	 * @param pBuffer the buffer to source values from.
	 */
	Consumer (BoundedBuffer pBuffer) {
		mBuffer = pBuffer;
	}

	// Methods
	
	/** 
	 * The run method for this thread.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// A random number generator for generating waiting periods
		Random vRandom = new Random();
		
		// Surround our code with a try catch to handle thread interruptions.
		try {
			while (!interrupted()) {
				// Generate a random period to wait for.
				long vWait = Math.abs(vRandom.nextInt()) % 1000;

				// Now sleep.
				sleep(vWait);
				
				// Finally get a value from the buffer
				int vValue = mBuffer.get();
				
				// And print it.
				System.out.println("Value consumed: " + vValue);
			}
		} catch (InterruptedException pExn) {
			// Nothing to do here, just exit.
		}
	}
}
