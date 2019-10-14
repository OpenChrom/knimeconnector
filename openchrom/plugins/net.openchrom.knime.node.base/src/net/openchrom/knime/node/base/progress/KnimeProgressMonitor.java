/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.openchrom.knime.node.base.progress;

import org.eclipse.core.runtime.IProgressMonitor;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link IProgressMonitor} that delegates to an instance of
 * {@link ExecutionMonitor}.
 * 
 * @author Alexander Kerner
 *
 */
public class KnimeProgressMonitor implements IProgressMonitor {

	private final ExecutionMonitor delegate;

	private final static Logger logger = LoggerFactory.getLogger(KnimeProgressMonitor.class);

	private int totalWork;

	public KnimeProgressMonitor(final ExecutionMonitor delegate) {
		this.delegate = delegate;
	}

	@Override
	public void beginTask(final String name, final int totalWork) {
		this.totalWork = totalWork;
		setTaskName(name);
	}

	@Override
	public void done() {
		worked(totalWork);
	}

	@Override
	public void internalWorked(final double work) {
		if (logger.isDebugEnabled()) {
			logger.debug("'interal worked' at " + work + ", no way to propagate to delegate");
		}
	}

	@Override
	public boolean isCanceled() {
		try {
			delegate.getProgressMonitor().checkCanceled();
		} catch (final CanceledExecutionException e) {
			return true;
		}
		return false;
	}

	@Override
	public void setCanceled(final boolean value) {
		delegate.getProgressMonitor().setExecuteCanceled();

	}

	@Override
	public void setTaskName(final String name) {
		delegate.getProgressMonitor().setMessage(name);

	}

	@Override
	public void subTask(final String name) {
		delegate.getProgressMonitor().setMessage(name);

	}

	@Override
	public void worked(final int work) {
		delegate.getProgressMonitor().setProgress(work / totalWork);

	}

}
