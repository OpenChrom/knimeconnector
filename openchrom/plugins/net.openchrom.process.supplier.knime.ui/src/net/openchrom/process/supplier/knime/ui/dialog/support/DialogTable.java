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
package net.openchrom.process.supplier.knime.ui.dialog.support;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public abstract class DialogTable<Data> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2655941483606857740L;

	private class DataModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8147297920603841957L;

		@Override
		public String getColumnName(int column) {

			return columnNames[column];
		}

		@Override
		public int getRowCount() {

			return tableData.size();
		}

		@Override
		public int getColumnCount() {

			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {

			return getValue(tableData.get(rowIndex), columnIndex);
		}
	}

	/**
	 * 
	 */
	private JTable table;
	private JButton addRangeButton;
	private JButton removeSelectedRangesButton;
	private JButton removeAllRangesButton;
	private List<Data> tableData;
	private DataModel dataModel;
	private String[] columnNames;

	public DialogTable(String columnsName, Collection<Data> init, int width, int alignment) {

		this(new String[]{columnsName}, null, width, alignment, JTable.AUTO_RESIZE_ALL_COLUMNS);
		updateComponent(init);
	}

	public DialogTable(String[] columnsName, int[] columnsWidth, Collection<Data> init, int width, int alignment) {

		this(columnsName, columnsWidth, width, alignment, JTable.AUTO_RESIZE_OFF);
		updateComponent(init);
	}

	public DialogTable(String columnsName, int width, int alignment) {

		this(new String[]{columnsName}, null, width, alignment, JTable.AUTO_RESIZE_ALL_COLUMNS);
	}

	public DialogTable(String[] columnsName, int[] columnsWidth, int width, int alignment) {

		this(columnsName, columnsWidth, width, alignment, JTable.AUTO_RESIZE_OFF);
	}

	/**
	 * 
	 * @param columnsName
	 * @param columnsWidth
	 * @param init
	 * @param width
	 *            set width component
	 * @param aligment
	 *            set cell alignment JLabel.LEFT or JLabel.CENTER or JLabel.RIGHT
	 * @param tableAutoSize
	 *            set table JTable.AUTO_RESIZE_OFF - allow horizontal scrolling or JTable.AUTO_RESIZE_ALL_COLUMNS do not allow horizontal scrolling
	 */
	private DialogTable(String[] columnsName, int[] columnsWidth, int width, int alignment, int tableAutoSize) {

		super();
		tableData = new ArrayList<>();
		columnNames = columnsName;
		init(width, tableAutoSize);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(alignment);
		for(int i = 0; i < columnsName.length; i++) {
			final int j = i;
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			if(JTable.AUTO_RESIZE_OFF == tableAutoSize) {
				table.getColumnModel().getColumn(i).setMinWidth(columnsWidth[i]);
				table.getColumnModel().getColumn(i).setWidth(columnsWidth[i]);
			}
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
			sorter.setComparator(j, (o1, o2) -> compare(o1, o2, j));
			table.setRowSorter(sorter);
		}
		table.getRowSorter().setSortKeys(null);
	}

	private void init(int width, int tableAutoSize) {

		dataModel = new DataModel();
		table = new JTable(dataModel);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoResizeMode(tableAutoSize);
		addRangeButton = new JButton("Add");
		addRangeButton.addActionListener(e -> addData());
		removeSelectedRangesButton = new JButton("<html>Remove<br>Selection</html>");
		removeSelectedRangesButton.addActionListener(e -> {
			removeSelecedRanges();
		});
		removeAllRangesButton = new JButton("Remove All");
		removeAllRangesButton.addActionListener(e -> {
			removeAllRanges();
		});
		//
		setMaximumSize(new Dimension(width, 130));
		setPreferredSize(new Dimension(width, 130));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane jScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jScrollPane, c);
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

	protected abstract Object getValue(Data data, int columnIndex);

	protected abstract int compare(Object o1, Object o2, int columnIndex);

	protected abstract List<Data> add();

	public void updateComponent(Collection<Data> newData) {

		tableData.clear();
		for(Data data : newData) {
			if(!tableData.contains(data)) {
				tableData.add(data);
			}
		}
		dataModel.fireTableDataChanged();
	}

	private void addData() {

		List<Data> newData = add();
		for(Data data : newData) {
			if(!tableData.contains(data)) {
				tableData.add(data);
			}
		}
		dataModel.fireTableDataChanged();
	}

	public List<Data> getTableData() {

		return Collections.unmodifiableList(tableData);
	}

	private void removeSelecedRanges() {

		int[] rows = table.getSelectedRows();
		for(int row : rows) {
			tableData.remove(row);
		}
		dataModel.fireTableDataChanged();
	}

	private void removeAllRanges() {

		tableData.clear();
		dataModel.fireTableDataChanged();
	}
}
