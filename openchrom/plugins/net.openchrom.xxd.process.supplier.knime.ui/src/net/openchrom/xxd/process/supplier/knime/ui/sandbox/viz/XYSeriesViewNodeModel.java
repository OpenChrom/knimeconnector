/*******************************************************************************
 * Copyright (c) 2017 Universität Konstanz.
 * 
 * This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contributors:
 * Dr. Martin Horn - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.sandbox.viz;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.BufferedDataTableHolder;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class XYSeriesViewNodeModel extends NodeModel implements BufferedDataTableHolder {

	static final SettingsModelString createXVectorColumnModel() {

		return new SettingsModelString("x_vector_column", "");
	}

	static final SettingsModelString createYVectorColumnModel() {

		return new SettingsModelString("y_vector_column", "");
	}

	private SettingsModelString m_xVectorColumn = createXVectorColumnModel();
	private SettingsModelString m_yVectorColumn = createYVectorColumnModel();
	private BufferedDataTable m_inTable;

	protected XYSeriesViewNodeModel() {
		super(1, 0);
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// TODO
		return null;
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {

		m_inTable = inData[0];
		return null;
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		m_xVectorColumn.saveSettingsTo(settings);
		m_yVectorColumn.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		m_xVectorColumn.validateSettings(settings);
		m_yVectorColumn.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		m_xVectorColumn.loadSettingsFrom(settings);
		m_yVectorColumn.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

	}

	/**
	 * @param idx
	 * @return <code>null</code> if no data is available
	 */
	public double[] getXSeries(int idx) {

		return getSeries(m_xVectorColumn.getStringValue(), idx);
	}

	/**
	 * @param idx
	 * @return <code>null</code> if no data is available
	 */
	public double[] getYSeries(int idx) {

		return getSeries(m_yVectorColumn.getStringValue(), idx);
	}

	private double[] getSeries(String columnName, int idx) {

		// TODO cache series
		if(m_inTable == null || columnName == null || columnName.length() == 0) {
			return null;
		}
		CloseableRowIterator iterator = m_inTable.iterator();
		DataRow row = null;
		for(int i = 0; i <= idx; i++) {
			row = iterator.next();
		}
		DoubleVectorValue vecVal = (DoubleVectorValue)row.getCell(m_inTable.getDataTableSpec().findColumnIndex(columnName));
		double[] vec = new double[vecVal.getLength()];
		for(int i = 0; i < vec.length; i++) {
			vec[i] = vecVal.getValue(i);
		}
		return vec;
	}

	public long getNumSeries() {

		return m_inTable.size();
	}

	@Override
	public BufferedDataTable[] getInternalTables() {

		return new BufferedDataTable[]{m_inTable};
	}

	@Override
	public void setInternalTables(BufferedDataTable[] tables) {

		m_inTable = tables[0];
	}
}
