/**
 * 
 */
package org.testium.systemundertest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.testium.configuration.SutControlConfiguration;
import org.testium.systemundertest.Parameter.DIRECTION;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for starting the System Under Test.
 */
public final class GetVersionSutCommand implements SutIfCommand
{
	private static final String myAction = "getVersion";
	public static final String myVersionParameter = "version";
	public static final String myVersionLogParameter = "versionLog";
	private SutControlConfiguration myConfig;

	/**
	 * Constructor
	 * 
	 * @param aConfig
	 */
	public GetVersionSutCommand( SutControlConfiguration aConfig )
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
	public boolean doAction(RunTimeData aRtData, File aLogDir)
	{
		Trace.println( Trace.EXEC, "doAction( " + aLogDir.getName() + " )", true );

		if ( ! this.verifyParameters(aRtData) ) { return false; }

		String commandName = aRtData.substituteVars( myConfig.getCommand().getPath() );
		File command = new File( commandName );

		String cmdParamTmp = myConfig.getVersionParameter();
		cmdParamTmp += " " + myConfig.getSettingsParameter();
		String cmdParam = aRtData.substituteVars( cmdParamTmp );

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		File versionLog = new File( aLogDir, "sutVersion.log" );
		
		try
		{
			StandardSutControl.execute( command, cmdParam, output, versionLog);
		}
		catch (Exception exc)
		{
        	Trace.print(Trace.EXEC_PLUS, exc );
			return false;
		}

		PrintWriter pw;
		try
		{
			pw = new PrintWriter(versionLog);
			pw.println(output.toString());
	        pw.flush();
		}
		catch (FileNotFoundException exc)
		{
        	Trace.print(Trace.UTIL, exc );
		}

		RunTimeVariable versionVar = aRtData.get( myVersionParameter );
		versionVar.setValue( output.toString() );
		RunTimeVariable logVar = aRtData.get( myVersionLogParameter );
		logVar.setValue( versionLog );
        return true;
	}

	public boolean verifyParameters(RunTimeData aVariables)
	{
		Trace.println( Trace.EXEC_PLUS );
		ArrayList<Parameter> params = getParameters();
		
		for (Parameter param : params)
		{
			RunTimeVariable var = aVariables.get( param.getName() );
			if ( var == null ) { return false; }
			if ( var.getType() != param.getType() ) { return false; }
		}
		return true;
	}

	public ArrayList<Parameter> getParameters()
	{
		Trace.println( Trace.GETTER );
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		Parameter versionOutParameter = new Parameter(myVersionParameter, DIRECTION.OUT, String.class );
		Parameter versionLogParameter = new Parameter(myVersionLogParameter, DIRECTION.OUT, File.class );
		params.add( versionOutParameter );
		params.add( versionLogParameter );

		return params;
	}
}
