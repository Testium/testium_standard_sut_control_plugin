/**
 * 
 */
package org.testium.systemundertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.testium.configuration.SutControlConfiguration;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for restarting the System Under Test.
 */
public final class RestartSutCommand implements SutIfCommand
{
	private static final String myAction = "restart";
	private SutControlConfiguration myConfig;
	private SutIfCommand myStartCommand;
	private SutIfCommand myStopCommand;

	/**
	 * @param sutControl
	 */
	public RestartSutCommand( SutControlConfiguration aConfig,
	                          SutIfCommand aStartCommand,
	                          SutIfCommand aStopCommand )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
		myStartCommand = aStartCommand;
		myStopCommand = aStopCommand;
	}

	public String getName()
	{
		Trace.println( Trace.GETTER );
		return myAction;
	}

	/* (non-Javadoc)
	 * @see org.TestToolInterfaces.systemUnderTest.AbstractSingleSutAction#doAction()
	 */
	public boolean doAction(RunTimeData aVariables, File aLogDir)
	{
		Trace.println( Trace.EXEC );
		String param = myConfig.getRestartParameter();
		if ( param.isEmpty() )
		{
			myStopCommand.doAction(aVariables, aLogDir);
			return myStartCommand.doAction(aVariables, aLogDir);
		}
		else
		{
			File command = myConfig.getCommand();
			File runLog = new File( aLogDir, "sutRestart.log" );
			try
			{
				StandardSutControl.execute(command, param, runLog);
			}
			catch (FileNotFoundException exc)
			{
	        	Trace.print(Trace.UTIL, exc );
	        	return false;
			}
        	return true;
		}
	}

	public boolean verifyParameters(RunTimeData aVariables)
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;
	}

	public ArrayList<Parameter> getParameters()
	{
		Trace.println( Trace.GETTER );
		return new ArrayList<Parameter>();
	}
}
