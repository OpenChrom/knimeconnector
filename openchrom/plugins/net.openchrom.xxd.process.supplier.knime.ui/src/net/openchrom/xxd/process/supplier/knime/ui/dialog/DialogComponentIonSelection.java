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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;

import net.openchrom.xxd.process.supplier.knime.ui.dialog.support.IntegerRangesTable;

/**
 * A standard component allowing to choose a location(directory) and/or file
 * name.
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentIonSelection extends DialogComponent {

	private final TitledBorder m_border;
	private final IntegerRangesTable integerRangesTable;

	public DialogComponentIonSelection(SettingsModel model, String title) {
		super(model);
		m_border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		List<String> ranges = new ArrayList<>();
		integerRangesTable = new IntegerRangesTable(ranges, "Ion");
		getComponentPanel().setBorder(m_border);
		getComponentPanel().setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		getComponentPanel().add(integerRangesTable);
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

	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs) throws NotConfigurableException {

	}

	@Override
	protected void setEnabledComponents(boolean enabled) {

		integerRangesTable.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String text) {

		integerRangesTable.setToolTipText(text);
	}
}
