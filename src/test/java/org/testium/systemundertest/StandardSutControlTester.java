package org.testium.systemundertest;

import java.io.File;

import junit.framework.Assert;

import net.sf.testium.systemundertest.SutControl;

import org.junit.Before;
import org.junit.Test;
import org.testtoolinterfaces.testresult.SutInfo;
import org.testtoolinterfaces.utils.RunTimeData;


public class StandardSutControlTester extends junit.framework.TestCase
{
	@Before
	public void setUp()
	{
		System.out.println("==========================================================================");
		System.out.println(this.getName() + ":");
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version, as intended
	 */
	@Test
	public void testGetSutInfo_Normal()
	{
		SutControl sutControl = new StandardSutControlStub( "R7.6.20100111R", "R%M.%S.%PR" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "7", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "6", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20100111", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string trailing the patch
	 */
	@Test
	public void testGetSutInfo_noPostPatch()
	{
		SutControl sutControl = new StandardSutControlStub( "R7.6.20100111R", "R%M.%S.%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "7", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "6", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20100111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_noPreMain()
	{
		SutControl sutControl = new StandardSutControlStub( "7B.6.20100111R", "%M.%S.%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "7B", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "6", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20100111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_notSeparated()
	{
		SutControl sutControl = new StandardSutControlStub( "7B-6.20100111R", "%M%S.%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "7B-6", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20100111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_differentOrder()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4.20108111R", "%P.%M.%S" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "20108111R", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "7", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_charactersInFormat()
	{
		SutControl sutControl = new StandardSutControlStub( "7P.M4.PP111R", "%PP.M%M.P%S" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "P111R", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "7", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_doubleEntriesInFormat()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4.20108111R", "%P.%M.%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20108111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_endInFormat()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4end-20108111R", "%S.%Mend-%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "7", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20108111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_percentageInFormat()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4_%-20108111R", "%S.%M_%%-%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "7", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20108111R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_percentageInFormat2()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4_%M-20108112R", "%S.%M_%%M-%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "7", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20108112R", sutInfo.getVersionPatchLevel());
	}

	/**
	 * Test method for {@link org.testium.systemundertest.StandardSutControl#getSutInfo()}.
	 * Test Normal Version without any string before the main-level
	 */
	@Test
	public void testGetSutInfo_endInVersion()
	{
		SutControl sutControl = new StandardSutControlStub( "7.4end-20108111R", "%S.%M-%P" );
		SutInfo sutInfo = sutControl.getSutInfo( new File( "" ), new RunTimeData( null ) );
		
		Assert.assertEquals("Incorrect Main Version", "4end", sutInfo.getVersionMainLevel());
		Assert.assertEquals("Incorrect Sub Version", "7", sutInfo.getVersionSubLevel());
		Assert.assertEquals("Incorrect Patch Level", "20108111R", sutInfo.getVersionPatchLevel());
	}

}
