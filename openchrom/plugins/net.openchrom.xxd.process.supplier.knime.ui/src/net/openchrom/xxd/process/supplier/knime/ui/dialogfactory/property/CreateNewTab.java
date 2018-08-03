/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.property;

class CreateNewTab {

	private String tabName;
	private boolean isDefault;

	public CreateNewTab(String tabName) {
		this(tabName, false);
	}

	public CreateNewTab(String tabName, boolean isDefault) {
		this.tabName = tabName;
		this.isDefault = isDefault;
	}

	public String getTabName() {

		return tabName;
	}

	public boolean isDefault() {

		return isDefault;
	}
}
