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
package net.openchrom.process.supplier.knime.ui.dialog;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import net.openchrom.nmr.process.supplier.knime.model.ScanNMRExport;
import net.openchrom.process.supplier.knime.ui.dialog.support.ScanNMRExportTable;

/**
 * A standard component allowing to choose a location(directory) and/or file
 * name.
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentScanNMRExport extends DialogComponent {

	private final TitledBorder m_border;
	private final ScanNMRExportTable scanNMRExportTable;

	public DialogComponentScanNMRExport(SettingsModelString model, String title) {

		super(model);
		m_border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		scanNMRExportTable = new ScanNMRExportTable();
		getComponentPanel().setBorder(m_border);
		getComponentPanel().setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		getComponentPanel().add(scanNMRExportTable);
		getComponentPanel().add(Box.createHorizontalGlue());
		getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {

				updateComponent();
			}
		});
		updateComponent();
	}

	@Override
	protected void updateComponent() {

		String value = ((SettingsModelString)getModel()).getStringValue();
		try {
			scanNMRExportTable.updateComponent((List)ScanNMRExport.readString(value));
		} catch(ClassNotFoundException | IOException e) {
		}
	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

		updateModel();
	}

	private void updateModel() {

		try {
			((SettingsModelString)getModel()).setStringValue(ScanNMRExport.writeToString(scanNMRExportTable.getTableData()));
		} catch(IOException e) {
		}
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {

	}

	@Override
	protected void setEnabledComponents(boolean enabled) {

		scanNMRExportTable.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String text) {

		scanNMRExportTable.setToolTipText(text);
	}
}