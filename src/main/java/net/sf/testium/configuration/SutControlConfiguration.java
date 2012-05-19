package net.sf.testium.configuration;
/**
 * 
 */

import java.io.File;

import org.testtoolinterfaces.utils.Trace;


/**
 * @author Arjan Kranenburg
 *
 */
public class SutControlConfiguration
{
	private String myName;
	private File myCommand;
	private String myStartParameter;
	private String myStopParameter;
	private String myRestartParameter;
	private String myVersionParameter;
	private String myVersionFormat;
	private String myLongVersionParameter;
	private String mySettingsParameter;

	/**
	 * @param aName
	 * @param aCommand
	 * @param aStartParameter
	 * @param aStopParameter
	 * @param aRestartParameter
	 * @param aVersionParameter
	 * @param aVersionFormat
	 * @param aLongVersionParameter
	 * @param aSettingsParameter
	 */
	public SutControlConfiguration( String aName,
	                                File aCommand,
	                                String aStartParameter,
	                                String aStopParameter,
	                                String aRestartParameter,
	                                String aVersionParameter,
	                                String aVersionFormat,
	                                String aLongVersionParameter,
	                                String aSettingsParameter )
	{
	    Trace.println( Trace.CONSTRUCTOR,
	                   "SutControlConfiguration( "
                  			+ aName != null ? aName + ", " : ""
                   			+ aCommand != null ? aCommand.getName() + ", " : ""
                  			+ aStartParameter != null ? aStartParameter + ", " : ""
                  			+ aStopParameter != null ? aStopParameter + ", " : ""
                  			+ aRestartParameter != null ? aRestartParameter + ", " : ""
                  			+ aVersionParameter != null ? aVersionParameter + ", " : ""
                  			+ aVersionFormat != null ? aVersionFormat + ", " : ""
                  			+ aLongVersionParameter != null ? aLongVersionParameter + ", " : ""
                  			+ aSettingsParameter != null ? aSettingsParameter : ""
                  			+ " )",
	                   	true );

	    myName = aName;
	    myCommand = aCommand;
	    myStartParameter = aStartParameter;
	    myStopParameter = aStopParameter;
	    myRestartParameter = aRestartParameter;
	    myVersionParameter = aVersionParameter;
	    myVersionFormat = aVersionFormat;
	    myLongVersionParameter = aLongVersionParameter;
	    mySettingsParameter = aSettingsParameter;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return myName;
	}

	/**
	 * @return the command
	 */
	public File getCommand()
	{
		return myCommand;
	}

	/**
	 * @return the restartParameter
	 */
	public String getRestartParameter()
	{
		return myRestartParameter;
	}

	/**
	 * @return the startParameter
	 */
	public String getStartParameter()
	{
		return myStartParameter;
	}

	/**
	 * @return the stopParameter
	 */
	public String getStopParameter()
	{
		return myStopParameter;
	}

	/**
	 * @return the versionFormat
	 */
	public String getVersionFormat()
	{
		return myVersionFormat;
	}

	/**
	 * @return the versionParameter
	 */
	public String getVersionParameter()
	{
		return myVersionParameter;
	}

	/**
	 * @return the longVersionParameter
	 */
	public String getLongVersionParameter()
	{
		return myLongVersionParameter;
	}

	/**
	 * @return the settingsParameter
	 */
	public String getSettingsParameter()
	{
		return mySettingsParameter;
	}
}
