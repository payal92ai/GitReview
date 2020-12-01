package com.tarkaveda.serenity.utilities;

public class SerenitySettings {

	public static void setSerenitySettings()
	{
		if(SerenityConfiguration.browserName.equalsIgnoreCase("internetexplorer"))
			setInternetExplorer();
		else if(SerenityConfiguration.browserName.equalsIgnoreCase("firefox"))
			setFireFox();
		else if(SerenityConfiguration.browserName.equalsIgnoreCase("chrome"))
			setChrome();
		else if(SerenityConfiguration.browserName.equalsIgnoreCase("phantomjs"))
			setPhantomjs();
		else if(SerenityConfiguration.browserName.equalsIgnoreCase("provided"))
			setProvided();
	}

	private static void setProvided() {
		System.setProperty("webdriver.driver", "provided");
		System.setProperty("webdriver.provided.type", "mydriver");
		System.setProperty("webdriver.provided.mydriver", "");
		
	}

	private static void setPhantomjs() {
		System.setProperty("webdriver.driver", "phantomjs");
		
	}

	private static void setChrome() {
		System.setProperty("webdriver.driver", "chrome");
		System.setProperty("webdriver.chrome.driver", "src/main/resources/browserdrivers/chromeDriver.exe");
		System.setProperty("chrome.switches", "--no-sandbox,--ignore-certificate-errors,--homepage=about:blank,--no-first-run,--disable-extensions");
		
	}

	private static void setFireFox() {
		System.setProperty("webdriver.driver", "firefox");
		
	}

	private static void setInternetExplorer() {
		System.setProperty("webdriver.driver", "iexplorer");
		System.setProperty("webdriver.ie.driver", "src/main/resources/browserdrivers/IEDriverServer.exe");
		
	}
}
