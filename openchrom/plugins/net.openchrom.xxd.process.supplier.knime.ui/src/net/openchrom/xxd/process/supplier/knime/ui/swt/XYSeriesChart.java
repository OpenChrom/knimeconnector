/*******************************************************************************
 * Copyright (c) 2017 Lablicate GmbH.
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

import net.openchrom.xxd.process.supplier.knime.notifier.IXYSeriesListener;

public class XYSeriesChart extends LineChart implements IXYSeriesListener {

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
	}

	@Override
	public void update(double[] ySeries, String id) {

		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData = new SeriesData(ySeries, id);
		//
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSerieSettings = lineSeriesData.getLineSeriesSettings();
		lineSerieSettings.setEnableArea(false);
		lineSeriesDataList.add(lineSeriesData);
		//
		addSeriesData(lineSeriesDataList);
	}
}
