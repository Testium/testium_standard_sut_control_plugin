package net.sf.testium.configuration;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class SutControlParametersConfigurationXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "parameters";

	private static final String	CFG_START		= "start";
	private static final String	CFG_STOP		= "stop";
	private static final String	CFG_RESTART		= "restart";
	private static final String	CFG_VERSION		= "version";
	private static final String	CFG_LONGVERSION	= "longVersion";
	private static final String	CFG_SETTINGS	= "settings";

	private String myStartParam;
	private String myStopParam;
	private String myRestartParam;
	private String myVersionParam;
	private String myLongVersionParam;
	private String mySettingsParam;

	public SutControlParametersConfigurationXmlHandler(XMLReader anXmlReader, RunTimeData anRtData)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);
	    reset();

	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_START));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_STOP));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_RESTART));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_VERSION));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_LONGVERSION));
	    this.addElementHandler(new GenericTagAndStringXmlHandler(anXmlReader, CFG_SETTINGS));
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

		if (aQualifiedName.equalsIgnoreCase(CFG_START))
    	{
			myStartParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_STOP))
    	{
			myStopParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_RESTART))
    	{
			myRestartParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_VERSION))
    	{
			myVersionParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_LONGVERSION))
    	{
			myLongVersionParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(CFG_SETTINGS))
    	{
			mySettingsParam = childXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
	}

	/**
	 * @return the myRestartParam
	 */
	public String getRestartParam()
	{
		return myRestartParam;
	}

	/**
	 * @return the myStartParam
	 */
	public String getStartParam()
	{
		return myStartParam;
	}

	/**
	 * @return the myStopParam
	 */
	public String getStopParam()
	{
		return myStopParam;
	}

	/**
	 * @return the myVersionParam
	 */
	public String getVersionParam()
	{
		return myVersionParam;
	}

	/**
	 * @return the myLongVersionParam
	 */
	public String getLongVersionParam()
	{
		return myLongVersionParam;
	}

	/**
	 * @return the mySettingsParam
	 */
	public String getSettingsParam()
	{
		return mySettingsParam;
	}

	public void reset()
	{
		myStartParam = "start";
		myStopParam = "stop";
		myRestartParam = "restart";
		myVersionParam = "version";
		myLongVersionParam = "version long";
		mySettingsParam = "";
	}
}
