/**
 * This file is part of a project entitled Week9Samples which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011 and 2012 Dominic Verity and Macquarie University.
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

package org.macquarie.philosophy.animated
;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Simulate a single philosopher in the dining philosophers example. Each
 * philosopher is a thread which executes the eating behaviour of that individual:</p>
 * 
 * <ul>
 * 		<li>Chat for a random period of time.</li>
 * 		<li>Pick up left chopstick when it becomes available.</li>
 * 		<li>Chat for a random period of time.</li>
 * 		<li>Pick up right chopstick when it becomes available.</li>
 * 		<li>Eat for a random period of time.</li>
 * 		<li>Put both chopsticks down on the table.</li>
 * 		<li>REPEAT.</li>
 * </ul>
 * 
 * <p>We use explicit {@link ReentrantLock} objects to simulate the chopsticks. To
 * pick up a chopstick a philosopher must <em>lock</em> the {@link ReentrantLock}
 * used to represent it. If that object is already locked then that philosopher thread
 * will block until the philosopher thread which originally locked that object 
 * returns that chopstick to the table by <em>unlocking</em> it again.</p>
 * 
 * <p>We use the {@link Thread#interrupt()} mechanism to handle resets of the simulation.
 * If a philosopher thread receives the interrupt signal it drops (unlocks) both of
 * its chopsticks and restarts the eating process. To make this work properly when
 * a philosopher is blocked waiting for a chopstick to become available, we use the
 * {@link ReentrantLock#lockInterruptibly()} method to lock our chopstick objects.</p>
 * 
 * @author Dominic Verity
 *
 */
public class Philosopher extends Thread {

	/*
	 * Constants and class varibles.
	 */
	
	/**
	 * The standard pause length. The random times which philosophers
	 * take to talk or eat food are scaled by this factor. So the bigger
	 * we make this the slower the simulation will run.
	 */
	private static final int PAUSE = 100;

	/**
	 * A counter used to assign a unique identification number to
	 * each philosopher.
	 */
	private static int sNextNum = 0;
	
	/**
	 * A random number generator used to generate randomly selected
	 * pausing periods for talking and eating.
	 */
	private Random mGenerator;

	/**
	 * References to the two {@link ReentrantLock} objects used to 
	 * represent the chopsticks to the left and the right of this
	 * philosopher.
	 */
	private ReentrantLock mLeft, mRight;
	
	/**
	 * The identification number of this philosopher.
	 */
	private int mNum;
	
	/**
	 * Flags which record whether the philosopher is currently holding 
	 * the left or right chopstick (respectively) in his hands. The value
	 * <code>true</code> tells us that a chopstick is currently being held
	 *in the given hand and <code>false</code> indicates otherwise.
	 */
	private volatile boolean mHoldingLeft, mHoldingRight;
	
	/*
	 * Constructors
	 */
	
	/**
	 * When we build a philosopher thread we pass this constructor a pair of
	 * references which point to the objects which represent the chopsticks
	 * to either side of this philosopher. The new philosopher itself is 
	 * initialised to a state in which it is not holding anything in either hand.
	 * 
	 * @param pLeft the lock object which represents the chopstick to the left 
	 * 				of the new philosopher. 
	 * @param pRight the lock object which represents the copstick to the right
	 * 				 of the new philosopher.
	 */
	public Philosopher(ReentrantLock pLeft, ReentrantLock pRight) {
		mGenerator = new Random();
		mLeft = pLeft;
		mHoldingLeft = false;
		mRight = pRight;
		mHoldingRight = false;
		mNum = sNextNum++;
	}
	
	/*
	 * Methods 
	 */
	
	/**
	 * This run method implements the philosopher behaviour described
	 * in the overall comment for this class.
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				while (!interrupted()) {

					sleep(mGenerator.nextInt(PAUSE * 3 / 2) + (PAUSE / 2));

					mLeft.lockInterruptibly();
					try {
						mHoldingLeft = true;

						sleep(mGenerator.nextInt(PAUSE));

						mRight.lockInterruptibly();
						try {
							mHoldingRight = true;

							sleep(mGenerator.nextInt(PAUSE));

							mHoldingLeft = false;
							mHoldingRight = false;
						} finally {
							mRight.unlock();
						}
					} finally {
						mLeft.unlock();
					}
				}
			} catch (InterruptedException eExn) {
				// Nothing to do here
			} finally {
				mHoldingLeft = false;
				mHoldingRight = false;
			}
		}
	}
	
	/**
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		return "Philosopher " + String.valueOf(mNum);
	}

	/**
	 * @return true if this philosopher is holding a chopstick in his left hand,
	 * 	       false otherwise
	 */
	public boolean isHoldingLeft() {
		return mHoldingLeft;
	}

	/**
	 * @return true if this philosopher is holding a chopstick in his right hand,
	 * 	       false otherwise
	 */
	public boolean isHoldingRight() {
		return mHoldingRight;
	}
}
