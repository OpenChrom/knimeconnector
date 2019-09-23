package net.openchrom.knime.base;

import org.eclipse.chemclipse.processing.ProcessorFactory;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.openchrom.knime.base"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private static ServiceTracker<ProcessorFactory, ProcessTypeSupport> processTypeSupportTracker;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}


	public void start(BundleContext context) throws Exception {
		plugin = this;
		processTypeSupportTracker = new ServiceTracker<>(context, ProcessorFactory.class, new ServiceTrackerCustomizer<ProcessorFactory, ProcessTypeSupport>() {

			@Override
			public ProcessTypeSupport addingService(ServiceReference<ProcessorFactory> reference) {
				ProcessorFactory service = context.getService(reference);
				if (service != null) {
					return new ProcessTypeSupport(service);
				}
				return null;
			}

			@Override
			public void modifiedService(ServiceReference<ProcessorFactory> reference, ProcessTypeSupport service) {
			}

			@Override
			public void removedService(ServiceReference<ProcessorFactory> reference, ProcessTypeSupport service) {
				context.ungetService(reference);
				
			}
		});
		processTypeSupportTracker.open();
	}
	
	public static ProcessTypeSupport getProcessTypeSupport() {
		return processTypeSupportTracker.getService();
	}

	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		processTypeSupportTracker.close();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
