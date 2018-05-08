/*******************************************************************************
 * Copyright (c) 2018 Jan Holy.
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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IntegerRangesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1896011199305892966L;
	//
	private String columnName;
	private ObservableList<String> ranges;

	public IntegerRangesTableModel() {
		super();
		ranges = FXCollections.observableArrayList();
		columnName = "";
	}

	public IntegerRangesTableModel(String columnName) {
		this();
		this.columnName = columnName;
	}

	public List<String> getRanges() {

		return ranges;
	}

	@Override
	public int getRowCount() {

		return ranges.size();
	}

	@Override
	public int getColumnCount() {

		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return ranges.get(rowIndex);
	}

	@Override
	public String getColumnName(int column) {

		switch(column) {
			case 0:
				return columnName;
			default:
				return "";
		}
	}
}
