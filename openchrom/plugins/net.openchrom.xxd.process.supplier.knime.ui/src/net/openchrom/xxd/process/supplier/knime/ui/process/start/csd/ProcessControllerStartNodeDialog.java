/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.process.start.csd;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;

import net.openchrom.xxd.process.supplier.knime.portobject.ChromatogramSelectionCSDPortObjectSpec;

public class ProcessControllerStartNodeDialog extends DefaultNodeSettingsPane {

	private final String IMMEDIATE_PROCESSING_LABEL = "Immediate Processing - process will be applaied in each node";
	private final String POSTPONED_PROCESSING_LABEL = "Posponed Processing - all process will by applied in node \"End Processing\"";

	public ProcessControllerStartNodeDialog() {

		super();
		addDialogComponent(new DialogComponentButtonGroup(ProcessControllerStartNodeModel.getProcessingType(), "Processing type", true, new String[]{POSTPONED_PROCESSING_LABEL, IMMEDIATE_PROCESSING_LABEL}, new String[]{ChromatogramSelectionCSDPortObjectSpec.MODE_POSTPONED_PROCESSING, ChromatogramSelectionCSDPortObjectSpec.MODE_IMMEDIATE_PROCESSING}));
	}
}
