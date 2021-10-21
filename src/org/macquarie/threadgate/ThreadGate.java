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


package org.macquarie.threadgate;

/**
 * <p>Thread gates allow us to pause a thread, or a group of threads, at a given point
 * and re-awaken it at a later time. This implementation of thread gates is taken
 * from the book "Java Concurrency in Practice" by Brian Goetz.</p>
 *
 * <p>This uses the {@link #wait()} and {@link #notifyAll()} mechanism to
 * queue the threads that are waiting on a closed gate and to notify / wake those
 * threads when the gate is reopened.</p>
 *
 * <p>Each thread gate counts the number of times it has been opened, by calling its
 * {@link #open()} method. When a thread waits for the gate to be opened, by calling its
 * {@link #await()} method, it makes a note of the current gate opening number. Then
 * when that thread is awoken it can check to see if the gate has been opened again since
 * it was suspended. It does this by comparing the note it made when {@link #await()} was
 * called with the current gate opening number.</p>
 *
 * @author Dominic Verity
 *
 */
public class ThreadGate {

	// Member variables (fields)

	/**
	 * Flag to indicate whether the gate is currently open (true) or closed (false).
	 */
	private boolean mIsOpen;

	/**
	 * This field is used to count the number of times the gate is opened. This allows
	 * us to keep track of when a thread starts waiting on the thread gate to open
	 * so that we can make sure that we don't miss the gate opening when it is opened
	 * and closed in quick succession.
	 */
	private int mGeneration;

	// Constructor

	/**
	 * Default constructor. Initialises the gate in the closed state.
	 */
	public ThreadGate() {
		mGeneration = 0;
		mIsOpen = false;
	}

	// Methods

	/**
	 * Method to close the gate. While the gate is closed any calls to the
	 * {@link ThreadGate#await()} method will wait, suspending the calling
	 * thread, until the gate is opened.
	 *
	 */
	public synchronized void close() {
		mIsOpen = false;
	}

	/**
	 * Method to open the gate. This is done by altering the value of the openness
	 * flag and then calling {@link Object#notifyAll()} to wake up all threads which
	 * are waiting at this gate. It also advances the generation counter, so that we
	 * can ensure that all threads waiting at the gate will pass through it even if it
	 * is slammed shut again immediately after.
	 *
	 */
	public synchronized void open() {
		mIsOpen = true;
		mGeneration++;
		notifyAll();
	}

	/**
	 * Open the gate, letting thorough all of the currently waiting threads, and then
	 * immediately close it again.
	 */
	public synchronized void openThenClose() {
		mIsOpen = false;
		mGeneration++;
		notifyAll();
	}

	/**
	 * Wait until the gate is opened. Because the gate might be closed by another thread
	 * before the thread which called {@link ThreadGate#await()} get a chance to be awoken,
	 * we record the number of the generation at which this method was called. Then when
	 * that thread is finally woken we allow it to proceed if the generation counter has been
	 * advanced.
	 *
	 * @throws InterruptedException
	 *
	 */
	public synchronized void await() throws InterruptedException {
		int vArrived = mGeneration;
		while (!(mIsOpen || vArrived < mGeneration))
			wait();
	}

	/**
	 * @return the <code>true</code> if the gate is currently open and
	 * <code>false</code> otherwise.
	 */
	public boolean isOpen() {
		return mIsOpen;
	}
}
