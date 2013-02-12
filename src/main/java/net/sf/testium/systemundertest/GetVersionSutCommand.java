/**
 * 
 */
package net.sf.testium.systemundertest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.sf.testium.configuration.SutControlConfiguration;
import net.sf.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for starting the System Under Test.
 */
public final class GetVersionSutCommand implements TestStepCommandExecutor
{
	private static final String ACTION = "getVersion";
	public static final String VERSION_PARAMETER = "version";
	public static final String VERSION_LOG_PARAMETER = "versionLog";

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

	public ArrayList<ParameterImpl> getParameters()
	{
		Trace.println( Trace.GETTER );
		ArrayList<ParameterImpl> params = new ArrayList<ParameterImpl>();
		ParameterImpl versionOutParameter = new ParameterImpl(VERSION_PARAMETER, String.class );
		ParameterImpl versionLogParameter = new ParameterImpl(VERSION_LOG_PARAMETER, File.class );
		params.add( versionOutParameter );
		params.add( versionLogParameter );

		return params;
	}

	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC, "execute( " + aStep.getDisplayName() + ", "
		               						   + aLogDir.getName() + " )", true );

		// verifyParameters( aStep.getParameters() ); // Not needed

		// TODO is this correct? Why not directly using the constants?
		ArrayList<ParameterImpl> params = getParameters();
		for (ParameterImpl param : params)
		{
			RunTimeVariable var = aVariables.get( param.getName() );
			if ( var == null ||
				 var.getType() != param.getValueType() )
			{
				throw new TestSuiteException( param.getName()
				                              + " is not set or is not of type "
				                              + param.getValueType().getName(),
				                              aStep );
			}
		}
		
		TestStepResult result = new TestStepResult( aStep );

		String commandName = aVariables.substituteVars( myConfig.getCommand().getPath() );
		File command = new File( commandName );

		String cmdParamTmp = myConfig.getVersionParameter();
		cmdParamTmp += " " + myConfig.getSettingsParameter();
		String cmdParam = aVariables.substituteVars( cmdParamTmp );

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		File versionLog = new File( aLogDir, "sutVersion.log" );
		result.addTestLog("sutVersion", "sutVersion.log");
		
		try
		{
			StandardSutControl.execute( command, cmdParam, output, versionLog );
			result.setResult(VERDICT.PASSED);
		}
		catch (Exception exc)
		{
        	Trace.print(Trace.EXEC_PLUS, exc );
    		result.setResult(VERDICT.FAILED);
    		result.setComment(exc.getMessage());
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

		RunTimeVariable versionVar = aVariables.get( VERSION_PARAMETER );
		versionVar.setValue( output.toString() );
		RunTimeVariable logVar = aVariables.get( VERSION_LOG_PARAMETER );
		logVar.setValue( versionLog );

		return result;
	}

	public String getCommand()
	{
		Trace.println( Trace.GETTER );
		return ACTION;
	}

	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;
	}
}
