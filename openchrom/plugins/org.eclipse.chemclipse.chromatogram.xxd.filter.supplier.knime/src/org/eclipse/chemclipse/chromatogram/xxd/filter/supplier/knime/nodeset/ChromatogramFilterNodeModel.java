/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.nodeset;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialogfactory.SettingsObjectWrapper;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.knime.dialoggeneration.DialogGenerationNodeModel;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoViewSupport;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import net.openchrom.xxd.process.supplier.knime.model.ChromatogramFilterPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramFilterPortObjectSpec;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObject;
import net.openchrom.xxd.process.supplier.knime.model.ChromatogramSelectionMSDPortObjectSpec;

/**
 * Concatenates chromatogram filters (see {@link ChromatogramFilterPortObject}) and optionally executes them on a given chromatogram selection (see {@link ChromatogramSelectionMSDPortObject}).
 * 
 * @author Martin Horn, University of Konstanz
 *
 */
public class ChromatogramFilterNodeModel extends DialogGenerationNodeModel<IChromatogramFilterSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(ChromatogramFilterNodeModel.class);
	private String filterId;

	ChromatogramFilterNodeModel(String filterId, SettingsObjectWrapper<IChromatogramFilterSettings> so) {
		super(new PortType[]{ChromatogramSelectionMSDPortObject.TYPE_OPTIONAL, ChromatogramFilterPortObject.TYPE_OPTIONAL}, new PortType[]{ChromatogramSelectionMSDPortObject.TYPE, ChromatogramFilterPortObject.TYPE}, so);
		this.filterId = filterId;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		return new PortObjectSpec[]{new ChromatogramSelectionMSDPortObjectSpec(), new ChromatogramFilterPortObjectSpec()};
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

		ChromatogramSelectionMSDPortObject chromatogramSelectionPortObject;
		/*
		 * Apply the filter if a chromatogram selection is given at port 0.
		 */
		if(inObjects[0] != null) {
			chromatogramSelectionPortObject = (ChromatogramSelectionMSDPortObject)inObjects[0];
			if(chromatogramSelectionPortObject.getChromatogramSelectionMSD() != ChromatogramSelectionMSDPortObject.EMPTY_CHROMATOGRAM_SELECTION) {
				logger.info("Apply the filter");
				IChromatogramSelectionMSD chromatogramSelection = chromatogramSelectionPortObject.getChromatogramSelectionMSD();
				IProcessingInfo processingInfo = org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.ChromatogramFilter.applyFilter(chromatogramSelection, getSettingsObject(), filterId, new NullProgressMonitor());
				ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
			}
		} else {
			// otherwise pass an empty chromatogram selection port object
			chromatogramSelectionPortObject = new ChromatogramSelectionMSDPortObject();
		}
		/*
		 * Store applied chromatogram filter and it's settings
		 */
		ChromatogramFilterPortObject chromatogramFilterPortObject;
		if(inObjects[1] != null) {
			chromatogramFilterPortObject = (ChromatogramFilterPortObject)inObjects[1];
		} else {
			chromatogramFilterPortObject = new ChromatogramFilterPortObject();
		}
		chromatogramFilterPortObject.addChromatogramFilter(filterId, getSettingsObject());
		return new PortObject[]{chromatogramSelectionPortObject, chromatogramFilterPortObject};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}
}
