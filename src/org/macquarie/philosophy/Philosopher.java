/**
 * This file is part of a project entitled Week9Samples which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2012 Dominic Verity and Macquarie University.
 * 
 * Week9Samples is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Week9Samples is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Week9Samples. (See files COPYING and COPYING.LESSER.) If not,
 * see <http://www.gnu.org/licenses/>.
 */

package org.macquarie.philosophy;
import java.util.Random;

/**
 * Simulate a single philosopher in the dining philosophers example.
 * Each philosopher is an autonomous individual so this class extends the
 * {@link Thread} class so that we can execute each {@link Philosopher} in
 * a thread of its own.
 * 
 * @author Dominic Verity
 *
 */
public class Philosopher extends Thread {

	/*
	 *  Class (static) variables.
	 */
	
	/**
	 * Random number generator for generating random pauses.
	 */
	private static Random mGenerator;
	
	/**
	 * Counter to record the number of philosopher objects constructed
	 * to date.
	 */
	private static int mNumPhilosophers = 0;
	
	/**
	 * Counter to record the number of chopsticks currently held in the left
	 * hands of philosophers. The party will be in deadlock when every 
	 * philosopher is holding a chopstick in his left hand 
	 * (<code>mNumPhilosophers == mLeftChopsticksHeld</code>).
	 */
	private static int mLeftChopsticksHeld = 0;
	
	/*
	 * Member variables (fields).
	 */

	/**
	 * Reference to the {@link Chopstick} object to the left of this
	 * philosopher.
	 */
	private Chopstick mLeft;

	/**
	 * Reference to the {@link Chopstick} object to the right of this
	 * philosopher.
	 */
	private Chopstick mRight;
	
	/**
	 * The unique identification number of this philosopher.
	 */
	private int mNum;

	/*
	 * Static methods
	 */
	
	/**
	 * Getter method.
	 * 
	 * @return the number of left hands which are currently holding chopsticks.
	 */
	public static int getLeftChopsticksHeld() {
		return mLeftChopsticksHeld;
	}
	
	/**
	 * Sleep for a randomly determined number of milliseconds. 
	 * 
	 * @param pMaxPause the maximum number of milliseconds to sleep for.
	 */
	public static void randomPause(long pMaxPause) {
		if (mGenerator == null)
			mGenerator = new Random();
		try {
			Thread.sleep(Math.abs(mGenerator.nextLong()) % pMaxPause);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct a philosopher object.
	 * 
	 * @param pLeft the chopstick to be placed on the left of the new philosopher. 
	 * @param pRight the chopstick to be placed on the right of the new philosopher.
	 */
	public Philosopher(Chopstick pLeft, Chopstick pRight) {
		mLeft = pLeft;
		mRight = pRight;
		mNum = mNumPhilosophers++;
	}
	
	/**
	 * <p>Implements the behaviour of a philosopher, that is:<p>
	 * 
	 * <ul>
	 * 	<li>Pause.</li>
	 * 	<li>Try to pick up chopstick in left hand.</li>
	 * 	<li>Once successful pause.</li>
	 * 	<li>Try to pick up second chopstick in right hand.</li>
	 *  <li>Once successful eat and drop both chopsticks.</li> 
	 * </ul>
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			randomPause(500);
			synchronized (mLeft) {
				mLeftChopsticksHeld++;
				System.out.println(toString() + " has lifted " + mLeft.toString() +".");
				randomPause(150);
				synchronized (mRight) {
					System.out.println(toString() + " has lifted " + mRight.toString() +".");
					System.out.println(toString() + " has eaten");
				}
				mLeftChopsticksHeld--;
			}
		}
	}
	
	/**
	 * Render this philosopher as a {@link String}.
	 * 
	 * @see java.lang.Thread#toString()
	 */
	public String toString() {
		return "Philosopher " + String.valueOf(mNum);
	}
}
