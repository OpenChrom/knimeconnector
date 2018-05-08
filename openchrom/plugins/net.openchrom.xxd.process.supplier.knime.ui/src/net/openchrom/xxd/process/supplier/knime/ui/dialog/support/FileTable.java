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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.knime.core.util.FileUtil;
import org.knime.core.util.SimpleFileFilter;

public class FileTable extends JPanel {

	/**
	 * 
	 */
	private String[] suffixes;
	private static final long serialVersionUID = 7443485979459757002L;
	private JTable table;
	private JButton addRangeButton;
	private JButton removeSelectedRangesButton;
	private JButton removeAllRangesButton;
	private DefaultTableModel model;

	public FileTable(String[] files, String[] suffixes) {
		this.suffixes = suffixes;
		createComposite();
		for(String filepath : files) {
			File file = new File(filepath);
			model.addRow(new Object[]{file.getName(), file.getAbsolutePath()});
		}
	}

	void createComposite() {

		/*
		 * create table model
		 */
		model = new DefaultTableModel();
		model.addColumn("File name");
		model.addColumn("File path");
		/*
		 * create table
		 */
		table = new JTable(model);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		/*
		 * set column alignment
		 */
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		/*
		 * create columns
		 */
		addRangeButton = new JButton("Add");
		addRangeButton.addActionListener(e -> {
			File file = getOutputFileName();
			if(file != null) {
				Object[] data = new Object[]{file.getName(), file.getAbsolutePath()};
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
		/*
		 * layout
		 */
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
		JScrollPane jScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		;
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

	public List<File> getData() {

		List<File> files = new ArrayList<>();
		for(int count = 0; count < model.getRowCount(); count++) {
			files.add(new File(model.getValueAt(count, 1).toString()));
		}
		return files;
	}

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

	private File getOutputFileName() {

		// file chooser triggered by choose button
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(true);
		List<SimpleFileFilter> filters = createFiltersFromSuffixes(suffixes);
		for(SimpleFileFilter filter : filters) {
			fileChooser.addChoosableFileFilter(filter);
		}
		if(filters.size() > 0) {
			fileChooser.setFileFilter(filters.get(0));
		}
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent e) {

				fileChooser.grabFocus();
			}
		});
		try {
			if(!getData().isEmpty()) {
				String lastSelectedFile = getData().get(getData().size() - 1).getAbsolutePath();
				URL url = FileUtil.toURL(lastSelectedFile);
				Path localPath = FileUtil.resolveToPath(url);
				if(localPath != null) {
					if(Files.isDirectory(localPath)) {
						fileChooser.setCurrentDirectory(localPath.toFile());
					} else {
						fileChooser.setSelectedFile(localPath.toFile());
					}
				}
			}
		} catch(IOException | URISyntaxException | InvalidPathException ex) {
			// ignore
		}
		int r;
		r = fileChooser.showOpenDialog(FileTable.this);
		if(r == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}

	private static List<SimpleFileFilter> createFiltersFromSuffixes(final String... extensions) {

		List<SimpleFileFilter> filters = new ArrayList<>(extensions.length);
		for(final String extension : extensions) {
			if(extension.indexOf('|') > 0) {
				filters.add(new SimpleFileFilter(extension.split("\\|")));
			} else {
				filters.add(new SimpleFileFilter(extension));
			}
		}
		return filters;
	}
}
