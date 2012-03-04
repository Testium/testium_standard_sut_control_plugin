package org.testium.configuration;

import java.io.File;
import java.util.ArrayList;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class SutControlConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SutControlConfiguration";

	private static final String	CFG_NAME	= "name";
	public static final String	CFG_COMMAND	= "sutControl";
	private static final String	CFG_VERSION_FORMAT	= "versionFormat";

	private SutControlParametersConfigurationXmlHandler mySutControlParameterConfigurationXmlHandler;

	private String myTempName;
	private String myTempCommand;
	private String myTempVersionFormat;

	private String myStartParam;
	private String myStopParam;
	private String myRestartParam;
	private String myVersionParam;
	private String myLongVersionParam;
	private String mySettingsParam;

	private RunTimeData myRunTimeData;

	public SutControlConfigurationXmlHandler(XMLReader anXmlReader, RunTimeData anRtData)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myRunTimeData = anRtData;

		mySutControlParameterConfigurationXmlHandler = new SutControlParametersConfigurationXmlHandler(anXmlReader, anRtData);
		this.addElementHandler(SutControlParametersConfigurationXmlHandler.START_ELEMENT, mySutControlParameterConfigurationXmlHandler);

	    ArrayList<XmlHandler> xmlHandlers = new ArrayList<XmlHandler>();
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_NAME));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_COMMAND));
	    xmlHandlers.add(new GenericTagAndStringXmlHandler(anXmlReader, CFG_VERSION_FORMAT));

	    for (XmlHandler handler : xmlHandlers)
	    {
			this.addElementHandler(handler.getStartElement(), handler);
	    }
	}

	@Override
	public void handleStartElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleCharacters(String aValue)
	{
		// nop
	}

	@Override
	public void handleEndElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att)
	{
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
	    	      aQualifiedName + " )", true);
	    
	    if ( ! aChildXmlHandler.getClass().equals(GenericTagAndStringXmlHandler.class) )
		{
			throw new Error( "ChildXmlHandler (" + aChildXmlHandler.getClass().toString() + ") must be of type GenericTagAndStringXmlHandler" );
		}
		GenericTagAndStringXmlHandler childXmlHandler = (GenericTagAndStringXmlHandler) aChildXmlHandler;

		if (aQualifiedName.equalsIgnoreCase(CFG_NAME))
    	{
			myTempName = childXmlHandler.getValue();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_COMMAND))
    	{
			myTempCommand = myRunTimeData.substituteVars( childXmlHandler.getValue() );
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_VERSION_FORMAT))
    	{
			myTempVersionFormat = childXmlHandler.getValue();
    	}
		else if (aQualifiedName.equalsIgnoreCase(SutControlParametersConfigurationXmlHandler.START_ELEMENT))
    	{
			myStartParam = mySutControlParameterConfigurationXmlHandler.getStartParam();
			myStopParam = mySutControlParameterConfigurationXmlHandler.getStopParam();
			myRestartParam = mySutControlParameterConfigurationXmlHandler.getRestartParam();
			myVersionParam = mySutControlParameterConfigurationXmlHandler.getVersionParam();
			myLongVersionParam = mySutControlParameterConfigurationXmlHandler.getLongVersionParam();
			mySettingsParam = mySutControlParameterConfigurationXmlHandler.getSettingsParam();
    	}

   		aChildXmlHandler.reset();
	}
	
	public SutControlConfiguration getConfiguration() throws ConfigurationException
	{
	    Trace.println(Trace.UTIL);

		File command = null;
	    if ( myTempCommand != null )
		{
			command = new File ( myTempCommand );
		}
		
		return new SutControlConfiguration( myTempName,
		                                    command,
		                                    myStartParam,
		                                    myStopParam,
		                                    myRestartParam,
		                                    myVersionParam,
		                                    myTempVersionFormat,
		                                    myLongVersionParam,
		                                    mySettingsParam );
	}
}
