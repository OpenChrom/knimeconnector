/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class XYSeriesPart {

	@Inject
	private Composite composite;
	private Button button;

	@PostConstruct
	public void postConstruct() {

		button = new Button(composite, SWT.PUSH);
		button.setText("Hello Martin");
	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void setFocus() {

		button.setFocus();
	}
}