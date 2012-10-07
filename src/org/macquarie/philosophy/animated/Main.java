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

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

/**
 * <p>The main class of this application, sets up the contents of the main
 * window and starts the application running.</p>
 * 
 * <p>This class allows this program to be run as a stand alone application,
 * started via the <code>main(String[])</code> method, or as an applet, embedded
 * in a web page. In either case, the actual hard work of constructing and laying 
 * out the GUI is delegated to the {@link MainPanel} class.</p>
 * 
 * @author Dominic Verity
 *
 */
@SuppressWarnings("serial")
public class Main extends JApplet {

	/*
	 * Static members - class variables and constants
	 */
	
	/**
	 * The top-level application object.
	 */
	private static Main mApplication;

	/*
	 * Instance variables (fields)
	 */

	/**
	 * The main application window.
	 */
	private RootPaneContainer mMainFrame;
	
	/**
	 * The main panel containing the animator and its control buttons.
	 */
	private MainPanel mMainPanel;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mApplication = new Main();
			
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mApplication.initApp();
			}
		});
	}

	/**
	 * Setup the GUI by calling {@link Main#init()} and call 
	 * {@link Main#start()} to set the application running.
	 */
	private void initApp() {
		// Make the main frame object
		JFrame vMainFrame = new JFrame("Bouncing Head");
		vMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vMainFrame.setResizable(false);
		mMainFrame = vMainFrame;

		// Setup.
		setup();
		
		// Pack the GUI and set it going.
		vMainFrame.pack();
		vMainFrame.setVisible(true);
		
		// Start the animation thread.
		start();
	}

	/**
	 * Set the applet running.
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setup();
			}
		});
	}

	/**
	 * Setup the GUI.
	 */
	public void setup() {
		// Set the main container to this object if we are running as an applet
		if (mMainFrame == null)
			mMainFrame = this;
		
		// Make a top level panel and make it the content pane.
		mMainPanel = new MainPanel();
		mMainFrame.setContentPane(mMainPanel);
	}

	/**
	 * Start the animation running.
	 * 
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		mMainPanel.getAnimator().start();
	}

	/**
	 * Stop the animation.
	 * 
	 * @see java.applet.Applet#stop()
	 */
	@Override
	public void stop() {
		mMainPanel.getAnimator().stop();
	}


}