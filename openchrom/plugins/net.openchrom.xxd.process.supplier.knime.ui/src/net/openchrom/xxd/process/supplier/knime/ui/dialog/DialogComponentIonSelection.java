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
package net.openchrom.xxd.process.supplier.knime.ui.dialog;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.chemclipse.support.util.IonSettingUtil;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import net.openchrom.xxd.process.supplier.knime.ui.dialog.support.IonTable;

/**
 * A standard component allowing to choose a location(directory) and/or file
 * name.
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentIonSelection extends DialogComponent {

	private final TitledBorder m_border;
	private final IonTable ionTable;
	private IonSettingUtil ionSettingUtil;

	public DialogComponentIonSelection(SettingsModelString model, String title) {
		super(model);
		ionSettingUtil = new IonSettingUtil();
		m_border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		String value = model.getStringValue();
		ionTable = new IonTable(ionSettingUtil.deserialize(value));
		getComponentPanel().setBorder(m_border);
		getComponentPanel().setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		getComponentPanel().add(ionTable);
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
		ionTable.updateComponent(ionSettingUtil.deserialize(value));
	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

		updateModel();
	}

	private void updateModel() {

		((SettingsModelString)getModel()).setStringValue(ionSettingUtil.serialize(ionTable.getTableData()));
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {

	}

	@Override
	protected void setEnabledComponents(boolean enabled) {

		ionTable.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String text) {

		ionTable.setToolTipText(text);
	}
}
