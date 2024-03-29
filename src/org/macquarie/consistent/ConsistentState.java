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

package org.macquarie.consistent;
import java.util.Random;

/**
 * A simple class which uses synchronisation to ensure that the
 * state of an object remains consistent when it is updated and
 * read from different threads.
 * 
 * @author Dominic Verity
 *
 */
public class ConsistentState {

	static ConsistentState mInstance = null;
	static Thread mUpdateThread = null;
	static Thread mValidateThread = null;
	
	private long mValue=0;
	private long mValueTimesTwo=0;
	
	/**
	 * Sets the state of our object.
	 *  
	 * Now we've synchronised this method and the next, 
	 * which prevents other threads from checking the consistency of 
	 * an object while this method is actively updating that object.
	 * 
	 * @param pValue the value to update the current state with,
	 */
	public synchronized void setValues(long pValue) {
		mValue = pValue;
		doPause(3);
		mValueTimesTwo = pValue * 2;
	}
	
	/**
	 * Checks to see if the current state of our object is
	 * consistent.
	 * 
	 * @return true if it is.
	 */
	public synchronized boolean isConsistent() {
		return (mValue * 2 == mValueTimesTwo);
	}
	
	/**
	 * Utility routine - pauses our thread by calling
	 * sleep and supressing any InterruptedException.
	 */
	private static void doPause(long pPause) {
		try {
			Thread.sleep(pPause);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Main method, creates and starts two threads:
	 * 
	 * mUpdateThread which updates an InconsistentState object with a 
	 *               random value at regular intervals.
	 *               
	 * mValidateThread which checks repeatedly to see if the object is
	 *                 still in a consistent state.
	 *                 
	 * @param args
	 */
	public static void main(String[] args) {
		mInstance = new ConsistentState();
		
		mUpdateThread = new Thread(() -> {
			Random vGenerator = new Random();
			while (true) {
				long vNewValue = Math.abs(vGenerator.nextLong()) % 10000;

				System.out.print("Setting value to: ");
				System.out.println(vNewValue);
					
				mInstance.setValues(vNewValue);

				doPause(7);
			}
		});
		
		mValidateThread = new Thread(() -> {
			while (true) {
				System.out.print("State is currently ");
				if (mInstance.isConsistent())
					System.out.println("consistent.");
				else
					System.out.println("inconsistent!!!");

				doPause(11);
			}
		});
		
		mUpdateThread.start();
		mValidateThread.start();
	}
	


}
