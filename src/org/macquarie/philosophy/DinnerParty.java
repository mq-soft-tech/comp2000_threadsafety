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

/**
 * An application to simulate a complete party consisting
 * of six philosophers and their chopsticks.
 * 
 * @author Dominic Verity
 *
 */
public class DinnerParty extends Thread {
	
	private static final int PARTY_SIZE = 6;

	private static Philosopher vDiners[];

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		vDiners = new Philosopher[PARTY_SIZE];
		Chopstick vSticks[] = new Chopstick[PARTY_SIZE];
		
		for (int i = 0; i < PARTY_SIZE; i++)
			vSticks[i] = new Chopstick();
		
		for (int i = 0; i < PARTY_SIZE; i++) {
			vDiners[i] = new Philosopher(vSticks[i], vSticks[(i+1) % PARTY_SIZE]);
			vDiners[i].start();
		}
		
		new DinnerParty().start();
	}
	
	public void run() {
		while (Philosopher.getLeftChopsticksHeld() < PARTY_SIZE) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Our dinner party is deadlocked!");
		Runtime.getRuntime().exit(0);
	}
}
