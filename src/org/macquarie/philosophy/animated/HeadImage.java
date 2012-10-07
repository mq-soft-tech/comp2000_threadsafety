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

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * <p>A singleton class, which loads and manages a single {@link ImageIcon} object
 * containing the "Dom's head" image to be animated.</p>
 * 
 * <p>For more information about the singleton pattern consult chapter 5 
 * (pages 169 - 190) of the textbook <a href="http://headfirstlabs.com/books/hfdp/">
 * <em>"Head First Design Patterns"</em></a> by Freeman, Freeman, Sierra and Bates.<p>
 * 
 * @author Dominic Verity
 *
 */
public class HeadImage {

	/*
	 * Class variables ad constants
	 */

	/**
	 * Path to the image resource to load. 
	 */
	private static final String HEAD_IMAGE_NAME = 
			"/org/macquarie/images/mediumhead.png";

	/**
	 * Static variable in which to store the singleton head image object.
	 */
	private static ImageIcon mInstance = null;

	/*
	 * Constructors
	 */
	
	/**
	 * Make the default constructor private, to force clients to access the
	 * singleton instance of this class via the <code>getInstance()</code> method.
	 */
	private HeadImage() { }
	
	/*
	 * Methods
	 */
	
    /**
     * Return the singleton instance of this class. This instance is constructed
     * "lazily" the first time this method is called and is stored in the
     * static variable <code>mHead</code> for re-use each subsequent time this
     * method is called.
     * 
     * @return the unique singleton instance of this class.
     */
    public static ImageIcon getInstance() {
    	if (mInstance == null) {
    		URL vImageURL = HeadImage.class.getResource(HEAD_IMAGE_NAME);
    		mInstance = new ImageIcon(vImageURL);
    	}
    	return mInstance;
    }
}
