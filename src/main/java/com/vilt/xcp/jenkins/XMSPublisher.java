package com.vilt.xcp.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import hudson.util.Secret;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.vilt.xcp.jenkins.model.DataPolicy;
import com.vilt.xcp.jenkins.model.DeployEnvType;
import com.vilt.xcp.jenkins.model.DeployMethod;
import com.vilt.xcp.jenkins.model.XcpEnvironmentInstance;
import com.vilt.xcp.xms.xmsdeployer.IXMSPublishConfig;
import com.vilt.xcp.xms.xmsdeployer.XMSExecutionWrapper;

/**
 * Sample {@link Builder}.
 *
s * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link XMSPublisher} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author 
 */
public class XMSPublisher extends Publisher implements IXMSPublishConfig {

	/**
	 * Hold an instance of the Descriptor implementation of this publisher.
	 */
	@Extension
	public static final XMSPublisherDescriptor DESCRIPTOR = new XMSPublisherDescriptor();
	
	// Configs
	private final String xcpEnvironmentInstanceId;
	private final String xcpAppPackagePath;
	private final String xcpAppConfigPath;

	private final String environment;
	private final DataPolicy dataPolicy;
	private final DeployMethod deployMethod;
	private final DeployEnvType deployEnvType;

	private final boolean xploreIndexing;
	private final boolean validateOnly;

	private final String xmsUsername;
	private final Secret xmsPassword;
	private final String xmsServerHost;
	private final String xmsServerPort;
	private final String xmsServerSchema;
	private final String xmsServerContextPath;

	private final String workPath;
	private final String javaOpts;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
	@DataBoundConstructor public XMSPublisher(String xcpAppPackagePath, String xcpAppConfigPath,
			String environment, DataPolicy dataPolicy,
			DeployMethod deployMethod, DeployEnvType deployEnvType,
			boolean xploreIndexing, boolean validateOnly, String xmsUsername,
			Secret xmsPassword, String xmsServerHost, String xmsServerPort,
			String xmsServerSchema, String xmsServerContextPath, String xcpEnvironmentInstanceId,
			String workPath, String javaOpts) {
		this.xcpAppPackagePath = xcpAppPackagePath;
		this.xcpAppConfigPath = xcpAppConfigPath;
		this.environment = environment;
		this.dataPolicy = dataPolicy;
		this.deployMethod = deployMethod;
		this.deployEnvType = deployEnvType;
		this.xploreIndexing = xploreIndexing;
		this.validateOnly = validateOnly;
		this.xmsUsername = xmsUsername;
		this.xmsPassword = xmsPassword;
		this.xmsServerHost = xmsServerHost;
		this.xmsServerPort = xmsServerPort;
		this.xmsServerSchema = xmsServerSchema;
		this.xmsServerContextPath = xmsServerContextPath;
		this.xcpEnvironmentInstanceId = xcpEnvironmentInstanceId;
		this.workPath = workPath;
		this.javaOpts = javaOpts;
	}

	public String getXcpAppPackagePath() {
		return xcpAppPackagePath;
	}
	public String getXcpAppConfigPath() {
		return xcpAppConfigPath;
	}

	public String getEnvironment() {
		return environment;
	}

	public DeployMethod getDeployMethod() {
		return deployMethod;
	}
	public DataPolicy getDataPolicy() {
		return dataPolicy;
	}
	public DeployEnvType getDeployEnvType() {
		return deployEnvType;
	}

	public boolean isXploreIndexing() {
		return xploreIndexing;
	}
	public boolean isValidateOnly() {
		return validateOnly;
	}

	public String getXmsUsername() {
		return xmsUsername;
	}
	public String getXmsPassword() {
		return Secret.toString(xmsPassword);
	}
    public String getXmsServerHost() {
		return xmsServerHost;
	}
	public String getXmsServerPort() {
		return xmsServerPort;
	}
	public String getXmsServerSchema() {
		return xmsServerSchema;
	}
	public String getXmsServerContextPath() {
		return xmsServerContextPath;
	}

	public String getXmsToolsPath() {
		String xmsToolsPath = "";
        for(XcpEnvironmentInstance xcpEnvironmentInstance : DESCRIPTOR.getXcpEnvironmentInstances()) {
            if(xcpEnvironmentInstanceId.equals(xcpEnvironmentInstance.xcpEnvId)) {
            	xmsToolsPath = xcpEnvironmentInstance.xmsToolsPath;
            }
        }
		return xmsToolsPath;
	}
	public String getXmsToolsInstanceId() {
		return xcpEnvironmentInstanceId;
	}
	public String getWorkPath() {
		return workPath;
	}
	public String getJavaOpts() {
		return javaOpts;
	}

	@Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        // This is where you 'build' the project.
		XMSExecutionWrapper xmsExecution = new XMSExecutionWrapper();
		return xmsExecution.run(this, listener.getLogger());
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public XMSPublisherDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
}
