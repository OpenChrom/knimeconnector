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
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class IntegerRangesTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2494347070455690905L;
	private JTable table;
	private IntegerRangesTableModel model;
	private JButton addRangeButton;
	private JButton removeSelectedRangesButton;
	private JButton removeAllRangesButton;
	private final String SEPARATOR = ";";
	private final String RANGE_SEPARATOR = "-";

	public IntegerRangesTable() {
		super();
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		addRangeButton = new JButton("Add");
		addRangeButton.addActionListener(e -> {
			JFrame frame = new JFrame();
			String ranges = JOptionPane.showInputDialog(frame, "Set range or single integer value, or multiple value separeted by \";\" (5; 10; 15-17 )", //
					"Set Ranges Single Values ", //
					JOptionPane.DEFAULT_OPTION);
			if(ranges != null) {
				for(String range : checkRange(ranges)) {
					addRange(range);
				}
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

	public IntegerRangesTable(List<String> ranges, String columnName) {
		this();
		model = new IntegerRangesTableModel(columnName);
		model.getRanges().addAll(ranges);
		table.setModel(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		sorter.setComparator(0, (s1, s2) -> {
			String splitS1 = ((String)s1).split("-")[0].trim();
			int i1 = Integer.parseInt(splitS1);
			String splitS2 = ((String)s2).split("-")[0].trim();
			int i2 = Integer.parseInt(splitS2);
			return Integer.compare(i1, i2);
		});
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
	}

	private List<String> checkRange(String range) {

		List<String> newRanges = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(range, SEPARATOR);
		while(stringTokenizer.hasMoreElements()) {
			String s = (String)stringTokenizer.nextElement();
			String[] splitS = s.split(RANGE_SEPARATOR);
			if(splitS.length == 1) {
				try {
					String value = splitS[0].trim();
					Integer.parseInt(value);
					newRanges.add(value);
				} catch(NumberFormatException e) {
				}
			} else if(splitS.length == 2) {
				try {
					String value1 = splitS[0].trim();
					String value2 = splitS[1].trim();
					Integer.parseInt(value1);
					Integer.parseInt(value2);
					newRanges.add(value1 + " - " + value2);
				} catch(NumberFormatException e) {
				}
			}
		}
		return newRanges;
	}

	private void addRange(String range) {

		if(!model.getRanges().contains(range)) {
			model.getRanges().add(range);
		}
		model.fireTableDataChanged();
	}

	private void removeSelecedRanges() {

		int[] rows = table.getSelectedRows();
		for(int row : rows) {
			model.getRanges().remove(table.convertRowIndexToModel(row));
		}
		model.fireTableDataChanged();
	}

	private void removeAllRanges() {

		model.getRanges().clear();
		model.fireTableDataChanged();
	}
}
