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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public abstract class StringTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2494347070455690905L;
	private JTable table;
	private JButton addRangeButton;
	private JButton removeSelectedRangesButton;
	private JButton removeAllRangesButton;
	private DefaultTableModel model;

	public StringTable() {
		super();
		model = new DefaultTableModel();
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		addRangeButton = new JButton("Add");
		addRangeButton.addActionListener(e -> {
			Object[] data = add();
			if(data.length > 0) {
				model.addRow(data);
			}
		});
		removeSelectedRangesButton = new JButton("Remove Selection");
		removeSelectedRangesButton.addActionListener(e -> {
			removeSelecedRanges();
		});
		removeAllRangesButton = new JButton("Remove All");
		removeAllRangesButton.addActionListener(e -> {
			removeAllRanges();
		});
		//
		setMaximumSize(new Dimension(250, 130));
		setPreferredSize(new Dimension(250, 130));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane jScrollPane = new JScrollPane(table);
		add(jScrollPane, c);
		setBackground(Color.BLACK);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(addRangeButton, c);
		c.gridy++;
		add(removeSelectedRangesButton, c);
		c.gridy++;
		add(removeAllRangesButton, c);
		c.gridy++;
	}

	public StringTable(String[] init, String columnName) {
		this();
		model.addColumn(columnName);
		table.setModel(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		sorter.setComparator(0, (s1, s2) -> Comparator.comparing(String::toString).compare(s1.toString(), s2.toString()));
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		if(init.length > 0) {
			model.addRow(init);
		}
	}

	public List<String> getData() {

		List<String> numdata = new ArrayList<String>();
		for(int count = 0; count < model.getRowCount(); count++) {
			numdata.add(model.getValueAt(count, 0).toString());
		}
		return numdata;
	}

	protected abstract Object[] add();

	private void removeSelecedRanges() {

		int[] rows = table.getSelectedRows();
		for(int row : rows) {
			model.removeRow(row);
		}
		model.fireTableDataChanged();
	}

	private void removeAllRanges() {

		model.setRowCount(0);
		model.fireTableDataChanged();
	}
}
