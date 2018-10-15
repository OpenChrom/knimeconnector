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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.chemclipse.support.util.FileSettingUtil;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import net.openchrom.process.supplier.knime.ui.dialog.support.FileTable;

/**
 * A standard component allowing to choose a location(directory) and/or file
 * name.
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentMultiFileChooser extends DialogComponent {

	private final TitledBorder m_border;
	private final FileTable fileTable;
	private FileSettingUtil fileSettingUtil;

	public DialogComponentMultiFileChooser(SettingsModelString model, String title, String[] suffixes) {
		super(model);
		fileSettingUtil = new FileSettingUtil();
		String[] files = new String[0];
		m_border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		fileTable = new FileTable(suffixes, fileSettingUtil.deserialize(model.getStringValue()));
		getComponentPanel().setBorder(m_border);
		getComponentPanel().setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		getComponentPanel().add(fileTable);
		getComponentPanel().add(Box.createHorizontalGlue());
		getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {

				updateComponent();
			}
		});
	}

	@Override
	protected void updateComponent() {

		String value = ((SettingsModelString)getModel()).getStringValue();
		fileTable.updateComponent(fileSettingUtil.deserialize(value));
	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

		updateModel();
	}

	private void updateModel() {

		((SettingsModelString)getModel()).setStringValue(fileSettingUtil.serialize(fileTable.getTableData()));
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {

	}

	@Override
	protected void setEnabledComponents(boolean enabled) {

		fileTable.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String text) {

		fileTable.setToolTipText(text);
	}
}
