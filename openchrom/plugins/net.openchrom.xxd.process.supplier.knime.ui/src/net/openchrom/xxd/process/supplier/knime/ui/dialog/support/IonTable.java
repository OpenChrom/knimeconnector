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
package net.openchrom.xxd.process.supplier.knime.ui.dialog.support;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.eclipse.chemclipse.support.util.IonSettingUtil;

public class IonTable extends DialogTable<String> {

	private IonSettingUtil ionSettingUtils;

	public IonTable(List<String> ranges) {
		super("Ions", ranges, 200, JLabel.CENTER);
		ionSettingUtils = new IonSettingUtil();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6102406530574831646L;

	@Override
	protected Object getValue(String data, int columnIndex) {

		return data;
	}

	@Override
	protected int compare(Object o1, Object o2, int columnIndex) {

		return ionSettingUtils.compare((String)o1, (String)o2);
	}

	@Override
	protected List<String> add() {

		JFrame frame = new JFrame();
		String ranges = JOptionPane.showInputDialog(frame, "Set range or single integer value, or multiple value separeted by \";\" (5; 10; 15-17 )", //
				"Set Ranges Single Values ", //
				JOptionPane.DEFAULT_OPTION);
		if(ranges != null) {
			return ionSettingUtils.parseInput(ranges);
		}
		return new ArrayList<>();
	}
}
