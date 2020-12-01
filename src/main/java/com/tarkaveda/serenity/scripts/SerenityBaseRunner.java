package com.tarkaveda.serenity.scripts;

import org.junit.runner.JUnitCore;

import com.tarkaveda.serenity.utilities.SerenityConfiguration;
import com.tarkaveda.serenity.utilities.SerenitySettings;

public class SerenityBaseRunner {
	
	public static void main (String [] args)
	{
		SerenityConfiguration.initializeConfigValues();
		SerenitySettings.setSerenitySettings();
		JUnitCore junit = new JUnitCore();
		try {
//			JUnitCore.runClasses(Class.forName(SerenityConfiguration.scriptClass));
			junit.run(Class.forName(SerenityConfiguration.scriptClass));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
