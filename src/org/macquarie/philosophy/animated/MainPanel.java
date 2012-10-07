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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <p>A panel of this type encapsulates the contents of the top level 
 * window of this application. This includes the animator panel itself
 * and a button for restarting the animation.</p>
 * 
 * <p>Such a panel may either be used as the content pane of a
 * {@link JFrame}, if our game is to run as an applications, or of a
 * {@link JApplet}, if our game is to run as an applet.</p>
 * 
 * @author Dominic Verity
 *
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	/*
	 * Instance variables (fields)
	 */

	/**
	 * A button to press to restart the animation.
	 */
	private JButton mButton;
	
	/**
	 * An object used to keep track of the current state of the
	 * animation and to step if from one frame to the next.
	 */
	private PhilosopherAnimation mAnimation;
	
	/**
	 * An animator object which is used to regularly update the state
	 * of the animation.
	 */
	private Animator mAnimator;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Build a main panel, containing an animator panel,
	 * and a control button. Delegates its work to the 
	 * <code>setup()</code> method.
	 * 
	 */
	MainPanel() {
		mAnimation = new PhilosopherAnimation();
		setup();
	}
	
	/*
	 * Methods
	 */

	/**
	 * Create the GUI components for this application
	 * and lay them out on this panel.
	 * 
	 */
    private void setup() {
    	// Make an animator object for the specified animation.
    	mAnimator = new Animator(mAnimation);
    	
    	// Create a button to restart the animation.
    	mButton = new JButton();
    	mButton.setText("Restart");
    	mButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mAnimation.restart();
				repaint();
			}
    		
    	});
    	
    	// Add these components to this panel.
    	setLayout(new BorderLayout());
    	add(mButton, BorderLayout.SOUTH);
    	add(mAnimator, BorderLayout.CENTER);
    }

	/**
     * Access the animator object of this panel.
     *
	 * @return the animator object aggregated into this object.
	 */
	public Animator getAnimator() {
		return mAnimator;
	}
}
