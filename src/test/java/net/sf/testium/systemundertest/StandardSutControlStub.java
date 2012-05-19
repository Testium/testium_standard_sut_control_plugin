package net.sf.testium.systemundertest;

import java.io.File;

import net.sf.testium.configuration.SutControlConfiguration;
import net.sf.testium.systemundertest.StandardSutControl;

public class StandardSutControlStub extends StandardSutControl
{
	private String myVersionToReturn;
	
	public StandardSutControlStub( String aVersionToReturn, String aFormat )
	{
		super( new SutControlConfiguration( "tester",
		                                    new File( "nop" ),
                                            "nop",
                                            "nop",
                                            "nop",
                                            "nop",
                                            aFormat,
                                            "nop",
                                            "" ) );
		myVersionToReturn = aVersionToReturn;
	}
	
	public String getVersion()
    {
		return myVersionToReturn;
    }
}
