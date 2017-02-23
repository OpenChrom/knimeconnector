/*******************************************************************************
 * Copyright (c) 2017 loge.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * loge - initial API and implementation
 *******************************************************************************/
package net.openchrom.xxd.process.supplier.knime.ui.io.msd;

public class Constants {

	// Java temporary directory path
	public static String JAVA_TMP = "java.io.tmpdir";
	// Parts per million
	public static final double PPM = Math.pow(10.0, 6.0);
	// Abundance values
	public static final double MAX_ABUNDANCE = 1000d;
	public static final double MIN_ABUNDANCE = 0d;
	// Separator
	public static final String SEPARATOR = " | ";
	public static final String DELIMITER = "~";
	// Number of threads used for web services
	public static final int NTHREADS = 10;
	// Number of data points for padded data sets
	public static final int PADDING = 3;

	/*
	 * MSn level.
	 */
	public enum MSN {
		MS1(1), MS2(2), MS3(3), MS4(4), MSn(5);

		private int ms;

		private MSN(int ms) {
			this.ms = ms;
		}

		public int getLvl() {

			return ms;
		}

		public MSN up() {

			return this == MS1 ? MS1 : get(ms - 1);
		}

		public MSN down() {

			return this == MSn ? MSn : get(ms + 1);
		}

		public static MSN get(int value) {

			return MSN.valueOf("MS" + value);
		}

		public static MSN get(String value) {

			return MSN.valueOf("MS" + value);
		}
	}

	/*
	 * Supported file formats.
	 */
	public enum FILE_FORMATS {
		RAW, CDF, MZML, CML
	}

	/*
	 * Values for ion mode.
	 */
	public enum ION_MODE {
		POSITIVE, NEGATIVE, UNKNOWN, NEUTRAL
	}

	/*
	 * Acquisition mode.
	 */
	public enum ACQUISITION_MODE {
		CENTROID, PROFILE
	}

	/**
	 * Normalization methods.
	 */
	public enum NORM_METHOD {
		TOTAL_SIGNAL
	}

	/*
	 * MSn mode.
	 */
	public enum MSN_MODE {
		PRECURSOR_ION_SCAN, PRODUCT_ION_SCAN, NEUTRAL_LOSS_SCAN
	}

	/*
	 * Frequent dimensions.
	 */
	public enum DOMAIN {
		MASS_CHARGE, INTENSITY, CHROMATOGRAPHIC
	}

	/*
	 * Subatomic particles.
	 */
	public enum PARTICLES {
		PROTON(1.007276467), ELECTRON(0.00054858);

		private double mass;

		private PARTICLES(double mass) {
			this.mass = mass;
		}

		public double getMass() {

			return mass;
		}
	}
}
