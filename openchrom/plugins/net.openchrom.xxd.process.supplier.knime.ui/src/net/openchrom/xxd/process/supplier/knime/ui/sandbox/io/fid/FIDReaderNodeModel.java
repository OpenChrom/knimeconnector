package net.openchrom.xxd.process.supplier.knime.ui.sandbox.io.fid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.EndianUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.vector.doublevector.DoubleVectorCellFactory;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.streamable.simple.SimpleStreamableFunctionNodeModel;

public class FIDReaderNodeModel extends SimpleStreamableFunctionNodeModel {

	static final SettingsModelString createURLColumnModel() {
		return new SettingsModelString("url_column", "");
	}

	private SettingsModelString m_urlColumn = createURLColumnModel();

	@Override
	protected ColumnRearranger createColumnRearranger(DataTableSpec spec) throws InvalidSettingsException {
		ColumnRearranger colre = new ColumnRearranger(spec);
		colre.append(new CellFactory() {

			@Override
			public void setProgress(int curRowNr, int rowCount, RowKey lastKey, ExecutionMonitor exec) {
				exec.setProgress(curRowNr / (double) rowCount);
			}

			@Override
			public DataColumnSpec[] getColumnSpecs() {
				return new DataColumnSpec[] {
						new DataColumnSpecCreator("fid", DoubleVectorCellFactory.TYPE).createSpec() };
			}

			@Override
			public DataCell[] getCells(DataRow row) {
				String url = ((StringValue) row.getCell(spec.findColumnIndex(m_urlColumn.getStringValue())))
						.getStringValue();
				//read fid file and return as DoubleVector cell
				File file;
				try {
					file = new File(new URL(url).toURI());
				} catch (MalformedURLException | URISyntaxException e1) {
					throw new RuntimeException(e1);
				}
				double[] fid;
				InputStream inputStream;
				try {
					inputStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				int points = (int) (file.length() / 4);
				fid = new double[points];
				//
				for (int i = 0; i < points; i++) {
					try {
						fid[i] = EndianUtils.readSwappedInteger(inputStream);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				
				return new DataCell[] {DoubleVectorCellFactory.createCell(fid)};
			}
		});
		return colre;
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
		m_urlColumn.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_urlColumn.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_urlColumn.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {
	}

}
