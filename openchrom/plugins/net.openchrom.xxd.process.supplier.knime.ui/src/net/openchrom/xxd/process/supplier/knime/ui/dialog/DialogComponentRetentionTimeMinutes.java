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
 * 18.09.2005 (mb): created
 * 25.09.2006 (ohl): using SettingModel
 * 22.08.2018: (jan Holy) adjusting
 */
package net.openchrom.xxd.process.supplier.knime.ui.dialog;

import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObjectSpec;

/**
 * Provide a standard component for a dialog that allows to edit number value.
 * Provides label and spinner that checks ranges as well as functionality to
 * load/store into config object. The type of the number entered is determined
 * by the {@link SettingsModel} passed to the constructor (currently supported
 * are double and int).
 *
 * @author M. Berthold, University of Konstanz
 */
public class DialogComponentRetentionTimeMinutes extends DialogComponent {

	// the default width of the spinner if no width and no default value is set
	private final JSpinner m_spinner;
	private final JLabel m_label;
	private double MINUTE_CORRELATION_FACTOR = 60000.0d; // 1ms * 1000 = 1s; 1s * 60 = 1min

	/**
	 * Constructor puts label and spinner into panel and allows to specify
	 * width (in #characters) of component.
	 *
	 * @param numberModel
	 *            the SettingsModel determining the number type (double
	 *            or int)
	 * @param label
	 *            label for dialog in front of the spinner
	 * @param stepSize
	 *            step size for the spinner
	 * @param compWidth
	 *            the width (number of columns/characters) of the spinner
	 */
	public DialogComponentRetentionTimeMinutes(final SettingsModelIntegerBounded numberModel, final String label, final Number stepSize, final int compWidth) {
		super(numberModel);
		if(compWidth < 1) {
			throw new IllegalArgumentException("Width of component can't be " + "smaller than 1");
		}
		m_label = new JLabel(label);
		getComponentPanel().add(m_label);
		SpinnerNumberModel spinnerModel;
		double min;
		double max;
		double step;
		double value;
		min = numberModel.getLowerBound() / MINUTE_CORRELATION_FACTOR;
		max = numberModel.getUpperBound() / MINUTE_CORRELATION_FACTOR;
		value = numberModel.getIntValue() / MINUTE_CORRELATION_FACTOR;
		step = stepSize.doubleValue() / MINUTE_CORRELATION_FACTOR;
		spinnerModel = new SpinnerNumberModel(value, min, max, step);
		m_spinner = new JSpinner(spinnerModel);
		m_spinner.setEditor(new JSpinner.NumberEditor(m_spinner, "0.0##############"));
		final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)m_spinner.getEditor();
		editor.getTextField().setColumns(compWidth);
		editor.getTextField().setFocusLostBehavior(JFormattedTextField.COMMIT);
		m_spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {

				try {
					updateModel();
				} catch(final InvalidSettingsException ise) {
					// ignore it here.
				}
			}
		});
		// We are not updating the model immediately when the user changes
		// the value. We update the model right before save.
		// update the spinner, whenever the model changed
		getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {

				updateComponent();
			}
		});
		getComponentPanel().add(m_spinner);
		// add variable editor button if so desired
		// call this method to be in sync with the settings model
		updateComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateComponent() {

		final JComponent editor = m_spinner.getEditor();
		if(editor instanceof DefaultEditor) {
			clearError(((DefaultEditor)editor).getTextField());
		}
		// update the component only if it contains a different value than the
		// model
		try {
			m_spinner.commitEdit();
			final SettingsModelInteger model = (SettingsModelInteger)getModel();
			final double val = ((Double)m_spinner.getValue()).doubleValue();
			double newValue = model.getIntValue() / MINUTE_CORRELATION_FACTOR;
			if(val != newValue) {
				m_spinner.setValue(new Double(newValue));
			}
		} catch(final ParseException e) {
			// spinner contains invalid value - update component!
			final SettingsModelInteger model = (SettingsModelInteger)getModel();
			double oldValue = model.getIntValue() / MINUTE_CORRELATION_FACTOR;
			m_spinner.setValue(new Double(oldValue));
		}
		// also update the enable status of all components...
		setEnabledComponents(getModel().isEnabled());
	}

	/**
	 * Transfers the value from the spinner into the model. Colors the spinner
	 * red, if the number is not accepted by the settings model. And throws an
	 * exception then.
	 *
	 * @throws InvalidSettingsException
	 *             if the number was not accepted by the
	 *             model (reason could be an out of range, or just an invalid
	 *             input).
	 *
	 */
	private void updateModel() throws InvalidSettingsException {

		try {
			m_spinner.commitEdit();
			final SettingsModelInteger model = (SettingsModelInteger)getModel();
			int newValue = (int)(((Double)m_spinner.getValue()).doubleValue() * MINUTE_CORRELATION_FACTOR);
			model.setIntValue(newValue);
		} catch(final ParseException e) {
			final JComponent editor = m_spinner.getEditor();
			if(editor instanceof DefaultEditor) {
				showError(((DefaultEditor)editor).getTextField());
			}
			String errMsg = "Invalid number format. ";
			errMsg += "Please enter a valid floating point number.";
			throw new InvalidSettingsException(errMsg);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {

		updateModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkConfigurabilityBeforeLoad(final PortObjectSpec[] specs) throws NotConfigurableException {

		// we're always good - independent of the incoming spec
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setEnabledComponents(final boolean enabled) {

		boolean spinnerEnabled = enabled;
		// enable the spinner according to the variable model
		m_spinner.setEnabled(spinnerEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(final String text) {

		m_spinner.setToolTipText(text);
		m_label.setToolTipText(text);
	}
}
