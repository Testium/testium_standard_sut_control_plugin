package org.testium.systemundertest;

import java.io.File;

import org.testium.configuration.SutControlConfiguration;

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
