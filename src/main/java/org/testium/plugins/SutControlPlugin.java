package org.testium.plugins;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.testium.Testium;
import net.sf.testium.configuration.ConfigurationException;
import net.sf.testium.plugins.Plugin;
import net.sf.testium.plugins.PluginCollection;

import org.testium.configuration.SutControlConfiguration;
import org.testium.configuration.SutControlConfigurationXmlHandler;
import org.testium.systemundertest.StandardSutControl;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg
 *
 */
public final class SutControlPlugin implements Plugin
{
	public SutControlPlugin()
	{
		super();
		Trace.println(Trace.CONSTRUCTOR);
	}

	public void loadPlugIn(PluginCollection aPluginCollection,
			RunTimeData anRtData) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "loadPlugIn( " + aPluginCollection + " )", true );

		SutControlConfiguration config = readConfigFiles( anRtData );
		StandardSutControl sutControl = new StandardSutControl( config );
		
		aPluginCollection.setSutControl( sutControl );
	}

	public SutControlConfiguration readConfigFiles( RunTimeData anRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL);

		File configDir = (File) anRtData.getValue(Testium.CONFIGDIR);
		File configFile = new File( configDir, "sutControl.xml" );
		SutControlConfiguration globalConfig = readConfigFile( configFile, anRtData );
		
		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
		File userConfigFile = new File( userConfigDir, "sutControl.xml" );
		SutControlConfiguration userConfig = new SutControlConfiguration( null, null, null, null, null, null, null, null, null);
		if ( userConfigFile.exists() )
		{
			userConfig = readConfigFile( userConfigFile, anRtData );
		}

		String name = userConfig.getName();
		if (name == null)
		{
			name = globalConfig.getName();
			if ( name == null )
			{
				name = "Unknown";
			}
		}

		File command = userConfig.getCommand();
		if (command == null)
		{
			command = globalConfig.getCommand();
		    if ( command == null )
			{
				throw new ConfigurationException( "SUT control script is not set: " + SutControlConfigurationXmlHandler.CFG_COMMAND );
			}
		}
		
		String startParam = userConfig.getStartParameter();
		if (startParam == null)
		{
			startParam = globalConfig.getStartParameter();
		}

		String stopParam = userConfig.getStopParameter();
		if (stopParam == null)
		{
			stopParam = globalConfig.getStopParameter();
		}

		String restartParam = userConfig.getRestartParameter();
		if (restartParam == null)
		{
			restartParam = globalConfig.getRestartParameter();
		}

		String versionParam = userConfig.getVersionParameter();
		if (versionParam == null)
		{
			versionParam = globalConfig.getVersionParameter();
		}

		String longVersionParam = userConfig.getLongVersionParameter();
		if (longVersionParam == null)
		{
			longVersionParam = globalConfig.getLongVersionParameter();
		}

		String versionFormat = userConfig.getVersionFormat();
		if (versionFormat == null)
		{
			versionFormat = globalConfig.getVersionFormat();
			if ( versionFormat == null )
			{
				versionFormat = "%M-%S-%P";
			}
		}

		String settingsParam = userConfig.getSettingsParameter();
		if (settingsParam == null)
		{
			settingsParam = globalConfig.getSettingsParameter();
		}
		
		SutControlConfiguration config = new SutControlConfiguration( name,
		                                                              command,
		                                                              startParam,
		                                                              stopParam,
		                                                              restartParam,
		                                                              versionParam,
		                                                              versionFormat,
		                                                              longVersionParam,
		                                                              settingsParam );

		return config;
	}

	public SutControlConfiguration readConfigFile( File aConfigFile,
			RunTimeData aRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
        // create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        SutControlConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new SutControlConfigurationXmlHandler(xmlReader, aRtData);

	        // assign the handler to the parser
	        xmlReader.setContentHandler(handler);

	        // parse the document
	        xmlReader.parse( aConfigFile.getAbsolutePath() );
		}
		catch (ParserConfigurationException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (SAXException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (IOException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		
		SutControlConfiguration configuration = handler.getConfiguration();
		
		return configuration;
	}
}
