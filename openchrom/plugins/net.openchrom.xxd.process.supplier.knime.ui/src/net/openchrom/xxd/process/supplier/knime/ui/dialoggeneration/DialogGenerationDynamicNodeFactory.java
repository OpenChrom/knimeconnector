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
package net.openchrom.xxd.process.supplier.knime.ui.dialoggeneration;

import java.util.Map;

import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.NodeDialogPane;

import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsDialogFactory;
import net.openchrom.xxd.process.supplier.knime.ui.dialogfactory.SettingsObjectWrapper;

/**
 * Node factory that creates it's dialog from a provided {@link SettingsDialogFactory}.
 *
 * @author Martin Horn, University of Konstanz
 *
 * @param <NM>
 *            the node model, needs to implement {@link DialogGenerationNodeModel}
 * @param <SO>
 *            see {@link SettingsDialogFactory}
 */
public abstract class DialogGenerationDynamicNodeFactory<NM extends DialogGenerationNodeModel<SO>, SO> extends DynamicNodeFactory<NM> {

	private SettingsDialogFactory<SO> dialogFactory;

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return getSettingsDialogFactory().createDialog();
	}

	@Override
	public NM createNodeModel() {

		return createNodeModel(getSettingsDialogFactory().createSettingsObjectWrapper());
	}

	public abstract NM createNodeModel(SettingsObjectWrapper<SO> settingsObjectWrapper);

	/**
	 *
	 * @return <code>null</code> if there is no dialog
	 */
	protected abstract SettingsDialogFactory<SO> createSettingsDialogFactory();

	/**
	 * TODO
	 *
	 * To be added to the node description.
	 *
	 * @return
	 */
	protected Map<String, String> getDialogOptionsDescriptions() {

		return getSettingsDialogFactory().createDialogOptionDescriptions();
	}

	private SettingsDialogFactory<SO> getSettingsDialogFactory() {

		if(dialogFactory == null) {
			dialogFactory = createSettingsDialogFactory();
		}
		return dialogFactory;
	}

	@Override
	protected boolean hasDialog() {

		return getSettingsDialogFactory() != null;
	}
}
