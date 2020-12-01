package com.tarkaveda.serenity.utilities;

import org.apache.commons.lang3.StringUtils;

public class SerenityConfiguration {
	
	public static String browserName = "firefox";
	public static String excelDataPath = "src/main/resources/data/datasheet.xlsx";
	public static String scriptClass = "com.tarkaveda.serenity.scripts.BaseScript";
	
	public static void initializeConfigValues()
	{
		browserName = StringUtils.equals(System.getProperty("browsername"),null) ? browserName : System.getProperty("browsername") ;
		excelDataPath = StringUtils.equals(System.getProperty("exceldatapath"),null) ? excelDataPath : System.getProperty("exceldatapath");
		scriptClass = StringUtils.equals(System.getProperty("scriptclass"), null) ? scriptClass :  System.getProperty("scriptclass");
	}

}
