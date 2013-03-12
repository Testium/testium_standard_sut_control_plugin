package net.sf.testium.systemundertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.testium.configuration.SutControlConfiguration;

import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
import org.testtoolinterfaces.utils.StreamGobbler;
import org.testtoolinterfaces.utils.Trace;



/**
 * @author Arjan Kranenburg
 *
 */
public class StandardSutControl extends SutControl
{
	private SutControlConfiguration myConfig;
	
	private GetVersionSutCommand myGetVersionCmd;
	private GetVersionLongSutCommand myGetVersionLongCmd;
	
	public StandardSutControl( SutControlConfiguration aConfig )
	{
		super();
		Trace.println(Trace.CONSTRUCTOR);

		myConfig = aConfig;
		
		StartSutCommand startCmd = new StartSutCommand( myConfig );
		StopSutCommand stopCmd = new StopSutCommand( myConfig );
		myGetVersionCmd = new GetVersionSutCommand( myConfig );
		myGetVersionLongCmd = new GetVersionLongSutCommand( myConfig );
		
		add( startCmd );
		add( stopCmd );
		add( new RestartSutCommand( myConfig, startCmd, stopCmd, this ) );
		add( myGetVersionCmd );
		add( myGetVersionLongCmd );
	}
	
//	public String getName()
//	{
//		Trace.println( Trace.GETTER );
//		return myConfig.getName();
//	}

	public SutInfo getSutInfo( File aLogDir, RunTimeData aParentRtData )
	{
		Trace.println( Trace.EXEC );
		
		// TODO Revise this function. What to do with it?
		// Should GetVersionSutCommand and GetVersionLongSutCommand even be a command?
		RunTimeData runtimeData = new RunTimeData( aParentRtData );
		RunTimeVariable versionVar = new RunTimeVariable( GetVersionSutCommand.VERSION_PARAMETER, String.class );
		RunTimeVariable versionLogVar = new RunTimeVariable( GetVersionSutCommand.VERSION_LOG_PARAMETER, File.class );
		RunTimeVariable versionLongLogVar = new RunTimeVariable( GetVersionLongSutCommand.myVersionLogParameter, File.class );
		runtimeData.add(versionVar);
		runtimeData.add(versionLogVar);
		runtimeData.add(versionLongLogVar);
		SutInfo sut = new SutInfo( this.getSutName() );

		TestStepCommand sutInfoStep = new TestStepCommand( 0,
		                                                "Get the Version of the Sut",
		                                                "GetVersionSut",
		                                                this,
		                                                new ParameterArrayList() );
		TestStepResult sutInfoResult;
		try
		{
			sutInfoResult = myGetVersionCmd.execute(sutInfoStep, runtimeData, aLogDir);
			if ( sutInfoResult.getResult().equals(VERDICT.PASSED) )
			{
				String[] versions = getVersions( (String) versionVar.getValue() );
				sut.setVersion(versions[0], versions[1], versions[2]);
	
				TestStepResult sutInfoLongResult = myGetVersionLongCmd.execute(sutInfoStep, runtimeData, aLogDir);
				if ( sutInfoLongResult.getResult().equals(VERDICT.PASSED) )
				{
					File versionLog = (File) versionLongLogVar.getValue();
					if ( versionLog != null )
					{
						sut.addSutLog("version", versionLog.getAbsolutePath());
					}
				}
			}
		}
		catch (TestSuiteException e)
		{
			sutInfoResult = new TestStepCommandResultImpl( sutInfoStep );
			sutInfoResult.setResult(VERDICT.FAILED);
		}

		return sut;
	}
	
    /**
     * See http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
     * 
	 * @param aScript the script to execute, without parameters
	 * @param aParameter the parameter to use
	 * @param aRunLog the file to store the output
	 * 
     * @throws FileNotFoundException when the script or run-log does not
     *         exist or are directories
     */
    public static boolean execute( File aScript, String aParameter, File aRunLog ) throws FileNotFoundException
    {
		Trace.println( Trace.EXEC_PLUS,
		               "execute( "
						+ aScript.getAbsolutePath() + ", "
						+ aParameter + ", "
						+ aRunLog.getAbsolutePath()
						+ " )",
					   true );

        FileOutputStream runLog = new FileOutputStream(aRunLog.getAbsolutePath());
		try
		{
			execute( aScript, aParameter, runLog, aRunLog);
		}
		catch (Exception exc)
		{
        	Trace.print(Trace.EXEC_PLUS, exc );
			return false;
		}

        return true;
    }

    /**
     * See http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
     * 
	 * @param aScript the script to execute, without parameters
	 * @param aParameter the parameter to use
	 * @param anOutputStream the output stream for the return value
	 * @param aRunLog the file to store the output
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void execute( File aScript,
                                 String aParameter,
                                 OutputStream anOutputStream,
                                 File aRunLog ) throws IOException, InterruptedException
    {
		if ( aScript.isDirectory() )
		{
			throw new FileNotFoundException("Script cannot be a directory: " + aScript.getPath());
		}

		if ( aRunLog.isDirectory() )
		{
			throw new FileNotFoundException("Run log cannot be a Directory: " + aRunLog.getPath());
		}

		Trace.println( Trace.EXEC_PLUS,
		               "execute( "
						+ aScript.getAbsolutePath() + ", "
						+ aParameter + ", "
						+ aRunLog.getAbsolutePath()
						+ " )",
					   true );

        FileOutputStream runLog = new FileOutputStream(aRunLog.getAbsolutePath());
    	if ( !aScript.canExecute() )
    	{
			throw new IOException("Command cannot be executed: " + aScript.getAbsolutePath());
    	}

    	String commandString = aScript.getAbsolutePath() + " " + aParameter;
    	Trace.println(Trace.EXEC_PLUS, "Executing " + commandString);
    	Trace.println(Trace.EXEC_PLUS, "Writing log to " + aRunLog.getAbsolutePath());
 
        Runtime rt = Runtime.getRuntime();
        String[] envVars = {"TRACE=0"};
        Process proc = rt.exec(commandString, envVars);

        // any error message?
        StreamGobbler errorGobbler = new 
//            StreamGobbler(proc.getErrorStream(), runLog);            
        	StreamGobbler(proc.getErrorStream(), runLog);            
        
        // any output?
        StreamGobbler outputGobbler = new 
            StreamGobbler(proc.getInputStream(), anOutputStream);
            
        // kick them off
        errorGobbler.start();
        outputGobbler.start();
                                
        // Wait for threads to finish
        int exitVal = proc.waitFor();
		try
		{
			Thread.sleep( 20 );
		}
		catch (InterruptedException e)
		{
			throw new Error( e );
		}
        runLog.flush();
        runLog.close();

        anOutputStream.flush();
        anOutputStream.close();

		Trace.println(Trace.EXEC_PLUS, "Exit value is " + exitVal);
        if ( exitVal > 0 )
        {
			throw new IOException("Command returned non-zero value: " + exitVal);
        }
    }

	private String[] getVersions(String aVersionString)
	{
		Trace.print(Trace.UTIL, "getVersions( " + aVersionString + " )", true );
		String[] versions = { "", "", "" };
		try
		{
			String format = myConfig.getVersionFormat() + "end";
			String[] formatParts = format.split("%[MSP%]");
	
			String remainder = aVersionString.substring(formatParts[0].length()) + "end";
			String remainderFormat = format.substring(formatParts[0].length());
			for ( int i = 1; i<formatParts.length; i++ )
			{
				int index = remainder.indexOf(formatParts[i]);
				if ( index >= 0 )
				{
					String value = remainder.substring(0, index);
					remainder = remainder.substring(index + formatParts[i].length());
		
					int indexFormat = remainderFormat.indexOf(formatParts[i]);
					String valueType = remainderFormat.substring(0, indexFormat);
					remainderFormat = remainderFormat.substring(indexFormat + formatParts[i].length());
	
					if ( valueType.startsWith("%M") )
						versions[0] = value;
					else if ( valueType.startsWith("%S") )
						versions[1] = value;
					else if ( valueType.startsWith("%P") )
						versions[2] = value;
					else if ( valueType.startsWith("%%") )
					{	// NOP
					}
				}
			}
		}
		catch ( StringIndexOutOfBoundsException e )
		{
			// If any substring is not possible because the string has not the expected length,
			// we'll return what we have so far.
			Trace.print(Trace.UTIL, e);
		}
		return versions;
	}

	@Override
	public String getSutName()
	{
		Trace.println(Trace.GETTER);
		return myConfig.getName();
	}

	@Override
	public ParameterImpl createParameter(String aName, String aType, String aValue)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy()
	{
		// NOP
	}
}
