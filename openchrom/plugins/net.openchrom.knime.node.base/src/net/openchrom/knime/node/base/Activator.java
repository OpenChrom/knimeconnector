/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base;

 import org.osgi.framework.BundleActivator;
 import org.osgi.framework.BundleContext;

 /**
  * The activator class controls the plug-in life cycle
  */
 public class Activator implements BundleActivator {

	 // The plug-in ID
	 public static final String PLUGIN_ID = "net.openchrom.knime.base"; //$NON-NLS-1$

	 // The shared instance
	 private static Activator plugin;

	 /**
	  * The constructor
	  */
	 public Activator() {
	 }

	 @Override
	 public void start(final BundleContext context) throws Exception {
		 plugin = this;
	 }

	 @Override
	 public void stop(final BundleContext context) throws Exception {
		 plugin = null;
	 }

	 /**
	  * Returns the shared instance
	  *
	  * @return the shared instance
	  */
	 public static Activator getDefault() {
		 return plugin;
	 }

 }
