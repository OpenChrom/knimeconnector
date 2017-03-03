package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class XYSeriesViewNodeModel extends NodeModel {
	
	static final SettingsModelString createXVectorColumnModel() {
		return new SettingsModelString("x_vector_column", "");
	}
	
	static final SettingsModelString createYVectorColumnModel() {
		return new SettingsModelString("y_vector_column", "");
	}
	
	private SettingsModelString m_xVectorColumn = createXVectorColumnModel();
	private SettingsModelString m_yvectorColumn = createYVectorColumnModel();
	

	protected XYSeriesViewNodeModel() {
		super(1, 0);
	}
	
	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return null;
	}
	
	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		return null;
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_xVectorColumn.saveSettingsTo(settings);
		m_yvectorColumn.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_xVectorColumn.validateSettings(settings);
		m_yvectorColumn.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_xVectorColumn.loadSettingsFrom(settings);
		m_yvectorColumn.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {
	}

}
