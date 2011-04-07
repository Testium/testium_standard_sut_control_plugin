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
 * Simple class for starting the System Under Test.
 */
public final class StartSutCommand implements SutIfCommand
{
	private static final String myAction = "start";
	private SutControlConfiguration myConfig;

	/**
	 * @param sutControl
	 */
	public StartSutCommand( SutControlConfiguration aConfig )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
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
		File command = myConfig.getCommand();
		String cmdParam = myConfig.getStartParameter();
		cmdParam += " " + myConfig.getSettingsParameter();
		File runLog = new File( aLogDir, "sutStart.log" );
		try
		{
			StandardSutControl.execute(command, cmdParam, runLog);
		}
		catch (FileNotFoundException exc)
		{
        	Trace.print(Trace.UTIL, exc );
        	return false;
		}
    	return true;
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
