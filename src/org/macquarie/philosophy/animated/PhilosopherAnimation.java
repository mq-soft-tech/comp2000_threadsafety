/**
 * This file is part of a project entitled Week9Samples which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011 and 2012 Dominic Verity and Macquarie University.
 * Copyright (c) 2011 The COMP229 Class.
 * 
 * Week9Samples is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Week9Samples is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Week9Samples. (See files COPYING and COPYING.LESSER.) If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.macquarie.philosophy.animated;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;


/**
 * This class keeps track of an animation which demonstrates the 
 * dining philosophers.
 * 
 * @author Dominic Verity
 *
 */

public class PhilosopherAnimation implements Animatable {
	
	/*
	 * Class variables and constants
	 */
	
	/**
	 * Angle separating chopsticks held in a philosopher's hand and
	 * the philosopher himself.
	 */
	private static final double STICK_SEPARATION = 0.45;

	/**
	 * Diameter of the circle used to represent chopsticks in the animation.
	 */
	private static final int STICK_DIAMETER = 40;

	/**
	 * The radius from the centre of the table at which the diners are seated.
	 */
	private static final int SEATING_RADIUS = 250;

	/**
	 * The radius of our dining table.
	 */
	private static final int TABLE_RADIUS = 175;

	/**
	 * The width / height of the  square box in which the animated image is 
	 * confined, in pixels.
	 */
	private static final int CANVAS_SIZE = 600;
	
	/**
	 * The number of philosophers attending our party.
	 */
	private static final int PARTY_SIZE = 6;

	/*
	 * Instance variables.
	 */
	
	/**
	 * Contains references to all of the dining philosopher objects of our simulation.
	 */
	private Philosopher[] mDiners;

	/**
	 * Contains references to all of the chopstick objects of our simulation.
	 * Chopsticks are simply represented as reentrant lock objects.
	 */
	private ReentrantLock[] mSticks;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Default constructor, creates a random number generator and calls 
	 * <code>restart()</code> to randomly initialise the position and
	 * movement of the image.  
	 */
	PhilosopherAnimation() {
		mDiners = new Philosopher[PARTY_SIZE];
		mSticks = new ReentrantLock[PARTY_SIZE];
		
		for (int i = 0; i < PARTY_SIZE; i++)
			mSticks[i] = new ReentrantLock();
		
		for (int i = 0; i < PARTY_SIZE; i++) {
			mDiners[i] = new Philosopher(mSticks[i], mSticks[(i+1) % PARTY_SIZE]);
			mDiners[i].start();
		}
	}
	
	/**
	 * <p>Paint the current state of the dinner party.</p>
	 * 
	 * @see org.macquarie.philosophy.Animatable#paint(java.awt.Graphics)
	 */
	@Override
	public synchronized void paint(Graphics pGraphics) {
		pGraphics.setColor(Color.BLUE);
		pGraphics.fillOval(100, 100, 
				CANVAS_SIZE - 200, CANVAS_SIZE - 200);
		
		ImageIcon vHeadImage = HeadImage.getInstance();
		
		pGraphics.setColor(Color.RED);

		for (int i = 0; i < PARTY_SIZE; i++) {

			vHeadImage.paintIcon(null, pGraphics, 
					(int)(SEATING_RADIUS * Math.cos(2 * i * Math.PI / PARTY_SIZE) + 
							(CANVAS_SIZE - vHeadImage.getIconWidth()) / 2), 
					(int)(-SEATING_RADIUS * Math.sin(2 * i * Math.PI / PARTY_SIZE) + 
							(CANVAS_SIZE - vHeadImage.getIconHeight()) / 2));

			if (!mSticks[i].isLocked()) {
				pGraphics.fillOval(
						(int)(TABLE_RADIUS * Math.cos((2 * i - 1) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2), 
						(int)(-TABLE_RADIUS * Math.sin((2 * i - 1) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2), 
						STICK_DIAMETER, STICK_DIAMETER);
			}
			
			if (mDiners[i].isHoldingLeft()) {
				pGraphics.fillOval(
						(int)(SEATING_RADIUS * Math.cos((2 * i - STICK_SEPARATION) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2), 
						(int)(-SEATING_RADIUS * Math.sin((2 * i - STICK_SEPARATION) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2),
						STICK_DIAMETER, STICK_DIAMETER);
			}

			if (mDiners[i].isHoldingRight()) {
				pGraphics.fillOval(
						(int)(SEATING_RADIUS * Math.cos((2 * i + STICK_SEPARATION) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2), 
						(int)(-SEATING_RADIUS * Math.sin((2 * i + STICK_SEPARATION) * Math.PI / PARTY_SIZE) +
								(CANVAS_SIZE - STICK_DIAMETER) / 2),
						STICK_DIAMETER, STICK_DIAMETER);
			}
		}
	}
	
	/**
	 * @see org.macquarie.philosophy.Animatable#step()
	 */
	@Override
	public synchronized void step() {
		// Nothing to do here, since the philosopher threads handle
		// state updates for the animation.
	}
	
	/**
	 * Restart the philosophers, by sending the interrupt signal
	 * to each philosopher thread.
	 */
	public void restart() {
		for (int i = 0; i < PARTY_SIZE; i++) {
			mDiners[i].interrupt();
		}
	}
	
	/**
	 * @see org.macquarie.philosophy.Animatable#getWidth()
	 */
	@Override
	public int getWidth() {
		return CANVAS_SIZE;
	}

	/**
	 * @see org.macquarie.philosophy.Animatable#getHeight()
	 */
	@Override
	public int getHeight() {
		return CANVAS_SIZE;
	}
}
