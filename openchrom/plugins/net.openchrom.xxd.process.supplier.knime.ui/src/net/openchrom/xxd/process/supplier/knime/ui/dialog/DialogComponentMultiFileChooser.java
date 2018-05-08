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

/*
 * ------------------------------------------------------------------------
 * Copyright by KNIME GmbH, Konstanz, Germany
 * Website: http://www.knime.org; Email: contact@knime.org
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, Version 3, as
 * published by the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 * Additional permission under GNU GPL version 3 section 7:
 * KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 * Hence, KNIME and ECLIPSE are both independent programs and are not
 * derived from each other. Should, however, the interpretation of the
 * GNU GPL Version 3 ("License") under any applicable laws result in
 * KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 * you the additional permission to use and propagate KNIME together with
 * ECLIPSE with only the license terms in place for ECLIPSE applying to
 * ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 * license terms of ECLIPSE themselves allow for the respective use and
 * propagation of ECLIPSE together with KNIME.
 * Additional permission relating to nodes for KNIME that extend the Node
 * Extension (and in particular that are based on subclasses of NodeModel,
 * NodeDialog, and NodeView) and that only interoperate with KNIME through
 * standard APIs ("Nodes"):
 * Nodes are deemed to be separate and independent programs and to not be
 * covered works. Notwithstanding anything to the contrary in the
 * License, the License does not apply to Nodes, you are not required to
 * license Nodes under the License, and you are granted a license to
 * prepare and propagate Nodes, in each case even if such Nodes are
 * propagated with or for interoperation with KNIME. The owner of a Node
 * may freely choose the license terms applicable to such Node, including
 * when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 * History
 * 29.10.2005 (mb): created
 * 2006-05-26 (tm): reviewed
 */
import java.awt.Dimension;

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

import net.openchrom.xxd.process.supplier.knime.ui.dialog.support.FileTable;

/**
 * A standard component allowing to choose a location(directory) and/or file
 * name.
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentMultiFileChooser extends DialogComponent {

	private final TitledBorder m_border;
	private final FileTable fileTable;

	public DialogComponentMultiFileChooser(SettingsModel model, String title, String[] suffixes) {
		super(model);
		String[] files = new String[0];
		m_border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		fileTable = new FileTable(files, suffixes);
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

	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

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
