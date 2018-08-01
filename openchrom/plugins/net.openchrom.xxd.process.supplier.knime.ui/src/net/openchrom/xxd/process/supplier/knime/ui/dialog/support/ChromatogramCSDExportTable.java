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

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramCSDExport;
import net.openchrom.xxd.process.supplier.knime.model.IChromatogramCSDExport;

public class ChromatogramCSDExportTable extends DialogTable<IChromatogramCSDExport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9141145691357278244L;
	private final static String[] columnsName = new String[]{"Name", "Prefix", "Postfix", "Directory path"};
	private final static int[] columnsWidth = new int[]{250, 100, 100, 350};

	public ChromatogramCSDExportTable(Collection<IChromatogramCSDExport> init) {
		super(columnsName, columnsWidth, init, 800, JLabel.LEFT);
	}

	@Override
	protected Object getValue(IChromatogramCSDExport data, int columnIndex) {

		switch(columnIndex) {
			case 0:
				return data.getName();
			case 1:
				return data.getPrefix();
			case 2:
				return data.getPostfix();
			case 3:
				return data.getDirectory().getAbsolutePath();
			default:
				break;
		}
		return null;
	}

	@Override
	protected int compare(Object o1, Object o2, int columnIndex) {

		return ((String)o1).compareTo((String)o2);
	}

	@Override
	protected List<IChromatogramCSDExport> add() {

		List<IChromatogramCSDExport> chromatogramExports = new ArrayList<>();
		List<ISupplier> suppliers = ChromatogramConverterCSD.getChromatogramConverterSupport().getExportSupplier();
		JComboBox<ISupplier> comboSupplier = new JComboBox<>(suppliers.toArray(new ISupplier[suppliers.size()]));
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JTextField prefix = new JTextField("");
		JTextField postfix = new JTextField("");
		JTextField folderPath = new JTextField("");
		comboSupplier.setRenderer(new DefaultListCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2822203045160348961L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

				if(value instanceof ISupplier) {
					value = ((ISupplier)value).getFilterName();
				}
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				return this;
			}
		});
		comboSupplier.setSelectedIndex(0);
		JButton selectFile = new JButton("Select Directory");
		selectFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int returnVal = fc.showOpenDialog(selectFile);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					folderPath.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		//
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Select Type"));
		panel.add(comboSupplier);
		//
		panel.add(new JLabel("Select output files."));
		panel.add(folderPath);
		panel.add(new JLabel(""));
		panel.add(selectFile);
		panel.add(new JLabel("Select prefix"));
		panel.add(prefix);
		panel.add(new JLabel("Select postfix"));
		panel.add(postfix);
		//
		int result = JOptionPane.showConfirmDialog(null, panel, "Export file", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.OK_OPTION) {
			ISupplier supplier = (ISupplier)comboSupplier.getSelectedItem();
			String id = supplier.getId();
			File file = new File(folderPath.getText());
			if(file.isDirectory()) {
				IChromatogramCSDExport chromatogramExport = new ChromatogramCSDExport(id, file);
				chromatogramExport.setPostfix(postfix.getText());
				chromatogramExport.setPrefix(prefix.getText());
				chromatogramExports.add(chromatogramExport);
			}
		}
		return chromatogramExports;
	}
}
