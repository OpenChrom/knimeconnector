/*******************************************************************************
 * Copyright (c) 2017 Martin Horn.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.filter.dialoggeneration;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.filter.dialogfactory.SettingsObjectWrapper;

/**
 * Node model for the {@link DialogGenerationNodeFactory}. Essentially loads and saves the {@link SettingsObjectWrapper}.
 * 
 * @author Martin Horn, University of Konstanz
 *
 * @param <S>
 *            see {@link SettingsDialogFactory}
 */
public abstract class DialogGenerationNodeModel<S> extends NodeModel {

	private SettingsObjectWrapper<S> settingsObjectWrapper;

	protected DialogGenerationNodeModel(PortType[] inPortTypes, PortType[] outPortTypes, SettingsObjectWrapper<S> settingsObject) {
		super(inPortTypes, outPortTypes);
		this.settingsObjectWrapper = settingsObject;
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		settingsObjectWrapper.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsObjectWrapper.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		settingsObjectWrapper.loadValidatedSettingsFrom(settings);
	}

	protected S getSettingsObject() {

		return settingsObjectWrapper.getObject();
	}
}
