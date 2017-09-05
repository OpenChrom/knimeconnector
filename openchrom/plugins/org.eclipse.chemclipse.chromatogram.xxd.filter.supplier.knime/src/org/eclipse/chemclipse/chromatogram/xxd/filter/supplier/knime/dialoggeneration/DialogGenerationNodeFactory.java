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
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialoggeneration;

import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsDialogFactory;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsObjectWrapper;
import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.NodeDialogPane;

/**
 * Node factory that creates it's dialog from a provided {@link SettingsDialogFactory}.
 * 
 * @author Martin Horn, University of Konstanz
 *
 * @param <NM> the node model, needs to implement {@link DialogGenerationNodeModel}
 * @param <SO> see {@link SettingsDialogFactory}
 */
public abstract class DialogGenerationNodeFactory<NM extends DialogGenerationNodeModel<SO>, SO> extends DynamicNodeFactory<NM> {

	private SettingsDialogFactory<SO> dialogFactory;

	@Override
	protected boolean hasDialog() {

		return getSettingsDialogFactory() != null;
	}

	/**
	 * 
	 * @return <code>null</code> if there is no dialog
	 */
	protected abstract SettingsDialogFactory<SO> createSettingsDialogFactory();

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return getSettingsDialogFactory().createDialog();
	}
	
	@Override
	public NM createNodeModel() {
	
		return createNodeModel(getSettingsDialogFactory().createSettingsObjectWrapper());
	}
	
	public abstract NM createNodeModel(SettingsObjectWrapper<SO> settingsObjectWrapper);

	private SettingsDialogFactory<SO> getSettingsDialogFactory() {

		if(dialogFactory == null) {
			dialogFactory = createSettingsDialogFactory();
		}
		return dialogFactory;
	}

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
}
