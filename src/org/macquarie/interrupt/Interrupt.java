/**
 * This file is part of a project entitled IntroToConcurrency which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2017 Dominic Verity and Macquarie University.
 * 
 * IntroToConcurrency is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * IntroToConcurrency is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with IntroToConcurrency. (See files COPYING and COPYING.LESSER.) If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.macquarie.interrupt;
import java.util.Random;

/**
 * An application which shows how one thread may request for the execution of
 * another thread to be interrupted.
 * 
 * @author Dominic Verity
 *
 */

public class Interrupt {
	
	static Thread mCounter = null;
	static Thread mInterrupter = null;

	/**
	 * Create a Counter and an Interrupter thread and start
	 * the Counter thread. This in turn will start the Interrupter.
	 */
	public static void main(String[] args) {
		mCounter = new Counter();
		mInterrupter = new Interrupter();
		
		mCounter.start();
	}
	
	/**
	 * This class implements a Thread which counts backwards
	 * from 100 and restarts its count whenever it is interrupted
	 * 
	 * When this thread completes its countdown to 1 it terminates.
	 * 
	 * @author Dominic Verity
	 *
	 */
	private static class Counter extends Thread {
		private static final int STARTING_COUNT = 100;
		private int mCount = STARTING_COUNT;		
		
		public void run() {
			mInterrupter.start();
			
			while (mCount > 0) {
				System.out.print("Count: ");
				System.out.println(mCount--);

				if (interrupted())
					mCount = STARTING_COUNT;

				try {
					sleep(50);
				} catch (InterruptedException e) {
					mCount = STARTING_COUNT;
				}
			}
		}
	}
	
	/**
	 * This thread interrupts the Counter thread at random intervals.
	 * It checks to make sure that the corresponding Counter is still
	 * alive, and terminates as soon as it detects that that thread
	 * has died.
	 * 
	 * @author Dominic Verity
	 *
	 */
	private static class Interrupter extends Thread {
		private Random mGenerator = new Random();
		
		public void run() {
			while (mCounter.isAlive()) {
				try {
					sleep(Math.abs(mGenerator.nextLong()) % 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mCounter.interrupt();
			} 
		}
	}
}
