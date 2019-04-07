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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.knime.core.util.FileUtil;
import org.knime.core.util.SimpleFileFilter;

public class FileTable extends DialogTable<File> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1843133714073084163L;
	/**
	 * 
	 */
	private String[] suffixes;

	public FileTable(String[] suffixes, List<File> files) {
		super(new String[]{"File name", "File path"}, new int[]{200, 300}, files, 400, JLabel.LEFT);
		this.suffixes = suffixes;
	}

	@Override
	protected Object getValue(File data, int columnIndex) {

		if(columnIndex == 0) {
			return data.getName();
		}
		return data.getAbsolutePath();
	}

	@Override
	protected int compare(Object o1, Object o2, int columnIndex) {

		return Comparator.comparing(String::toString).compare(o1.toString(), o2.toString());
	}

	@Override
	protected List<File> add() {

		List<File> files = new ArrayList<>();
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(true);
		List<SimpleFileFilter> filters = createFiltersFromSuffixes(suffixes);
		for(SimpleFileFilter filter : filters) {
			fileChooser.addChoosableFileFilter(filter);
		}
		if(filters.size() > 0) {
			fileChooser.setFileFilter(filters.get(0));
		}
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent e) {

				fileChooser.grabFocus();
			}
		});
		try {
			if(!getTableData().isEmpty()) {
				String lastSelectedFile = getTableData().get(getTableData().size() - 1).getAbsolutePath();
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
			files.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
		}
		return files;
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
