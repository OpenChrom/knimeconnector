/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.swt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.chemclipse.ui.service.swt.charts.IChartSettings;
import org.eclipse.chemclipse.ui.service.swt.charts.IPrimaryAxisSettings;
import org.eclipse.chemclipse.ui.service.swt.charts.ISeriesData;
import org.eclipse.chemclipse.ui.service.swt.charts.SeriesData;
import org.eclipse.chemclipse.ui.service.swt.charts.line.ILineSeriesData;
import org.eclipse.chemclipse.ui.service.swt.charts.line.ILineSeriesSettings;
import org.eclipse.chemclipse.ui.service.swt.charts.line.LineChart;
import org.eclipse.chemclipse.ui.service.swt.charts.line.LineSeriesData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class XYSeriesChart extends LineChart {

	private static Color COLOR_BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private static DecimalFormat decimalFormatScientific = new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH));
	private static DecimalFormat decimalFormatInteger = new DecimalFormat(("0"), new DecimalFormatSymbols(Locale.ENGLISH));

	public XYSeriesChart(Composite parent, int style) {
		super(parent, style);
		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(true);
		chartSettings.setUseZeroX(true);
		chartSettings.setUseZeroY(true);
		applySettings(chartSettings);
		//
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("f1 (ppm)");
		primaryAxisSettingsX.setDecimalFormat(decimalFormatInteger);
		primaryAxisSettingsX.setColor(COLOR_BLACK);
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
		primaryAxisSettingsY.setDecimalFormat(decimalFormatScientific);
		primaryAxisSettingsY.setColor(COLOR_BLACK);
		//
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		double[] ySeries = new double[]{20, 30, 40, 35, 20, 20, 20, 10, 5};
		ISeriesData seriesData = new SeriesData(ySeries, "FFT");
		//
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSerieSettings = lineSeriesData.getLineSeriesSettings();
		lineSerieSettings.setEnableArea(false);
		lineSeriesDataList.add(lineSeriesData);
		//
		addSeriesData(lineSeriesDataList);
	}
}
