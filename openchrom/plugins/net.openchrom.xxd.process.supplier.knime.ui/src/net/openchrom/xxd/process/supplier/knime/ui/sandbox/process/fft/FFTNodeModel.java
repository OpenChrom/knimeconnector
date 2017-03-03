package net.openchrom.xxd.process.supplier.knime.ui.sandbox.process.fft;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.vector.doublevector.DoubleVectorCellFactory;
import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.streamable.simple.SimpleStreamableFunctionNodeModel;

public class FFTNodeModel extends SimpleStreamableFunctionNodeModel {

	static final SettingsModelString createVectorColumnModel() {
		return new SettingsModelString("vector_column", "");
	}

	private SettingsModelString m_vectorColumn = createVectorColumnModel();

	@Override
	protected ColumnRearranger createColumnRearranger(DataTableSpec spec) throws InvalidSettingsException {
		ColumnRearranger colre = new ColumnRearranger(spec);
		colre.append(new CellFactory() {

			@Override
			public void setProgress(int curRowNr, int rowCount, RowKey lastKey, ExecutionMonitor exec) {
				exec.setProgress((double) curRowNr / rowCount);
			}

			@Override
			public DataColumnSpec[] getColumnSpecs() {
				return new DataColumnSpec[] {
						new DataColumnSpecCreator("fft", DoubleVectorCellFactory.TYPE).createSpec() };
			}

			@Override
			public DataCell[] getCells(DataRow row) {
				DoubleVectorValue val = ((DoubleVectorValue) row
						.getCell(spec.findColumnIndex(m_vectorColumn.getStringValue())));
				FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
				// pad with zeros if not power of two size
				double[] in = new double[getPowerOfTwoSize(val.getLength())];
				for (int i = 0; i < val.getLength(); i++) {
					in[i] = val.getValue(i);
				}
				Complex[] transform = transformer.transform(in, TransformType.FORWARD);
				double[] mag = new double[in.length];
				for (int i = 0; i < mag.length; i++) {
					mag[i] = Math.sqrt(transform[i].getReal() * transform[i].getReal()
							+ transform[i].getImaginary() * transform[i].getImaginary());
				}
				return new DataCell[] { DoubleVectorCellFactory.createCell(mag) };
			}
		});
		return colre;
	}

	/**
	 * Returns a number that is greater or equal to the given and is power of 2.
	 * 
	 * @param initialSize
	 *            the value which is smaller or equal to the returned value
	 * @return a number that is a power of two and is not less that initialSize
	 * 
	 *         Source(!!):
	 *         http://www.programcreek.com/java-api-examples/index.php?source_dir=svarog-master/svarog/src/main/java/org/signalml/math/fft/FourierTransform.java
	 */
	private static int getPowerOfTwoSize(int initialSize) {
		double log_of_initialSize_to_base_2 = Math.log(initialSize) / Math.log(2);
		return (int) Math.pow(2, Math.ceil(log_of_initialSize_to_base_2));
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
		m_vectorColumn.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_vectorColumn.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_vectorColumn.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {

	}
}
