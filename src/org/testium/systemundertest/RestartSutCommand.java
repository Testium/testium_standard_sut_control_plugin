/**
 * 
 */
package org.testium.systemundertest;

import java.io.File;
import java.io.FileNotFoundException;

import org.testium.configuration.SutControlConfiguration;
import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestStep.StepType;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;


/**
 * @author arjan.kranenburg
 *
 * Simple class for restarting the System Under Test.
 */
public final class RestartSutCommand implements TestStepCommandExecutor
{
	private static final String myAction = "restart";
	private SutControlConfiguration myConfig;
	private TestStepCommandExecutor myStartCommand;
	private TestStepCommandExecutor myStopCommand;
	private StandardSutControl mySutControl;

	/**
	 * @param SutControlConfiguration
	 * @param TestStepCommandExecutor
	 * @param TestStepCommandExecutor
	 * @param standardSutControl 
	 */
	public RestartSutCommand( SutControlConfiguration aConfig,
	                          TestStepCommandExecutor aStartCommand,
	                          TestStepCommandExecutor aStopCommand,
	                          StandardSutControl aSutControl )
	{
		Trace.println( Trace.CONSTRUCTOR );
		myConfig = aConfig;
		myStartCommand = aStartCommand;
		myStopCommand = aStopCommand;
		mySutControl = aSutControl;
	}

	@Override
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC );
		TestStepResult result = new TestStepResult( aStep );

		String param = myConfig.getRestartParameter();
		if ( param.isEmpty() )
		{
			TestStepCommand stopStep = new TestStepCommand( StepType.action,
			                                                0,
			                                                "Stop Command",
			                                                "Stop",
			                                                mySutControl,
			                                                new ParameterArrayList() );
			TestStepResult stopResult = myStopCommand.execute(stopStep, aVariables, aLogDir);
			
			TestStepCommand startStep = new TestStepCommand( StepType.action,
			                                                0,
			                                                "Start Command",
			                                                "Start",
			                                                mySutControl,
			                                                new ParameterArrayList() );
			TestStepResult startResult = myStartCommand.execute(startStep, aVariables, aLogDir);

			// TODO Combine the 2 results as sub stepResult of the new result
			return startResult;
		}
		else
		{
			File command = myConfig.getCommand();

			File runLog = new File( aLogDir, "sutRestart.log" );
			result.addTestLog("sutRestart", "sutRestart.log");
			
			try
			{
				StandardSutControl.execute(command, param, runLog);
				result.setResult(VERDICT.PASSED);
			}
			catch (FileNotFoundException exc)
			{
	        	Trace.print(Trace.UTIL, exc );
	    		result.setResult(VERDICT.FAILED);
	    		result.setComment(exc.getMessage());
			}

        	return result;
		}
	}

	@Override
	public String getCommand()
	{
		Trace.println( Trace.GETTER );
		return myAction;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		Trace.println( Trace.EXEC_PLUS );
		return true;	
	}
}
