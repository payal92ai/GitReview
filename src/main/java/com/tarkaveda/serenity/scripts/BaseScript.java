package com.tarkaveda.serenity.scripts;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tarkaveda.serenity.steps.BaseSteps;
import com.tarkaveda.serenity.utilities.ExcelReader;
import com.tarkaveda.serenity.utilities.SerenityConfiguration;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class BaseScript {

	@Managed(uniqueSession = true)
	public WebDriver driver;

	@Steps
	public BaseSteps baseSteps;

	public static Map<String, Map<String, Map<String, String>>> dataMap;

	@Test
	public void mainTest() {

		readExcel(SerenityConfiguration.excelDataPath);
		navigateScreens();
	}

	private void readExcel(final String excelFilepath)
	{
		try {
			dataMap = ExcelReader.readExcel(excelFilepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void navigateScreens()
	{
		String screenName = null;
		for(String screenOrder : dataMap.get("otherSpec").get("ScreenOrder").keySet())
		{
			screenName = dataMap.get("otherSpec").get("ScreenOrder").get(screenOrder);
			baseSteps.getCurrentScreenInstance(screenName);
			baseSteps.switchPage(15);
			Serenity.takeScreenshot();
			for(String actionType : dataMap.get(screenName).keySet())
			{
				if(actionType.contains("populate"))
				{
					populateAll(dataMap.get(screenName).get(actionType));
					Serenity.takeScreenshot();
				}
				else if (actionType.contains("verify"))
				{
					verifyAll(dataMap.get(screenName).get(actionType));
					Serenity.takeScreenshot();
				}
			}
		}
	}

	private void populateAll(Map<String, String> data)
	{
		for(String fieldName : data.keySet())
		{
			String fieldValue = Matcher.quoteReplacement(data.get(fieldName));
			if(fieldName.toLowerCase().contains("waitfor"))
				waitForThings(fieldName, fieldValue);
			else
				populateWidget(fieldName, fieldValue);

		}
	}


	private void switchFrame(final WebElementFacade element, final String fieldValue)
	{
		if(fieldValue.equalsIgnoreCase("activate"))
			driver.switchTo().frame(element);
		else if(fieldValue.equalsIgnoreCase("activate"))
			driver.switchTo().defaultContent();
	}

	public void waitForThings(final String waitKind, final String waitValue)
	{
		final String screenTitle = baseSteps.basePage.getExpectedScreenTitle();
		if (waitKind.toLowerCase().contains("waitforpagetoload"))
			baseSteps.waitForPageToLoad(screenTitle);
		else
		{
			WebElementFacade element = baseSteps.WebElementMap.get(waitValue);
			if(waitKind.toLowerCase().contains("waitForwidgetvisible"))
				baseSteps.waitForWidgetVisible(screenTitle, waitValue, element);
			else if(waitKind.toLowerCase().contains("waitForwidgetinvisible"))
				baseSteps.waitForWidgetInVisible(screenTitle, waitValue, element);
			else if(waitKind.toLowerCase().contains("waitForwidgetenabled"))
				baseSteps.waitForWidgetEnabled(screenTitle, waitValue, element);
			else if(waitKind.toLowerCase().contains("waitForwidgetdisabled"))
				baseSteps.waitForWidgetDisabled(screenTitle, waitValue, element);
		}

	}

	private void populateWidget(final String fieldName, final String fieldValue)
	{
		WebElementFacade element = baseSteps.WebElementMap.get(fieldName);
		waitForWidgetReady(element);
		String tagName = element.getTagName();
		if(tagName.equalsIgnoreCase("div")||tagName.equalsIgnoreCase("td")||tagName.equalsIgnoreCase("tr")||tagName.equalsIgnoreCase("span")||tagName.equalsIgnoreCase("p"))
		{
			element = baseSteps.basePage.getWebElementFacade(element.findElements(By.xpath(".//a|.//button|.//select|.//textarea|.//textfield|.//input[@type!=hidden]")).get(0));
			tagName = element.getTagName();
		}
		if(tagName.equalsIgnoreCase("a")||tagName.equalsIgnoreCase("button"))
			baseSteps.clickElement(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("select"))
			baseSteps.populateDropdown(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("textarea")||tagName.equalsIgnoreCase("textfield"))
			baseSteps.enterText(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("input"))
			populateInput(element, fieldName, fieldValue);
		else if(tagName.equalsIgnoreCase("iframe")||tagName.equalsIgnoreCase("frame"))
			switchFrame(element, fieldValue);

	}

	private void populateInput(WebElementFacade element, String fieldName, String fieldValue) {
		final String type = element.getAttribute("type");
		if(type.equalsIgnoreCase("select"))
			baseSteps.populateDropdown(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(type.equalsIgnoreCase("textarea")||type.equalsIgnoreCase("textfield"))
			baseSteps.enterText(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(type.equalsIgnoreCase("submit")||type.equalsIgnoreCase("button"))
			baseSteps.clickElement(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(type.equalsIgnoreCase("checkbox"))
			baseSteps.populateCheckBox(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(type.equalsIgnoreCase("radio"))
			baseSteps.clickRadioButton(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());

	}

	private void waitForWidgetReady(WebElementFacade element)
	{
		double duration = 30;
		while(!baseSteps.basePage.verifyVisible(element) && duration>0)
		{
			baseSteps.basePage.sleep(0.01);
			duration =-0.01;
		}

		while(!baseSteps.basePage.verifyEnabled(element)&&duration>0)
		{
			baseSteps.basePage.sleep(0.01);
			duration =-0.01;
		}
	}

	private void verifyAll(Map<String, String> data)
	{
		for(String fieldName : data.keySet())
		{
			String fieldValue = Matcher.quoteReplacement(data.get(fieldName));
			verifyOneData(fieldName, fieldValue);

		}
	}

	private void verifyOneData(String fieldName, String fieldValue) {
		if(!nonContentVerification(fieldValue, fieldName))
			verifyWithData(fieldValue, fieldName);
	}

	private boolean nonContentVerification(String expectedValue, String fieldName) {
		if(expectedValue.equalsIgnoreCase("visible")||expectedValue.equalsIgnoreCase("invisible"))
			baseSteps.verifyVisibility(fieldName, expectedValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(expectedValue.equalsIgnoreCase("enabled")||expectedValue.equalsIgnoreCase("disabled"))
			baseSteps.verifyEnability(fieldName, expectedValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(expectedValue.equalsIgnoreCase("focussed")||expectedValue.equalsIgnoreCase("notfocussed"))
			baseSteps.verifyFocus(fieldName, expectedValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(expectedValue.equalsIgnoreCase("checked")||expectedValue.equalsIgnoreCase("unchecked"))
			baseSteps.verifyCheckBox(fieldName, expectedValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(expectedValue.equalsIgnoreCase("true")||expectedValue.equalsIgnoreCase("false"))
			baseSteps.verifyRadioButton(fieldName, expectedValue, baseSteps.basePage.getExpectedScreenTitle());
		else
			return false;
		return true;
	}

	private void verifyWithData(String fieldValue, String fieldName) {
		WebElementFacade element = baseSteps.WebElementMap.get(fieldName);
		WebElementFacade parentElement = element;
		waitForWidgetReady(element);
		String tagName = element.getTagName();
		if(tagName.equalsIgnoreCase("div")||tagName.equalsIgnoreCase("td")||tagName.equalsIgnoreCase("tr")||tagName.equalsIgnoreCase("span")||tagName.equalsIgnoreCase("p"))
		{
			try{
				element = baseSteps.basePage.getWebElementFacade(element.findElements(By.xpath(".//a|.//button|.//select|.//textarea|.//textfield|.//input[@type!=hidden]")).get(0));
				tagName = element.getTagName();}

			catch(NullPointerException e)
			{
				element = parentElement;
				tagName = element.getTagName();
			}
		}
		if(tagName.equalsIgnoreCase("a")||tagName.equalsIgnoreCase("button"))
			baseSteps.verifyLink(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("select"))
			baseSteps.verifyDropdown(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("textarea")||tagName.equalsIgnoreCase("textfield"))
			baseSteps.verifyTextField(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("h"))
			baseSteps.verifyHeader(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("label")||tagName.equalsIgnoreCase("div")||tagName.equalsIgnoreCase("td")||tagName.equalsIgnoreCase("tr")||tagName.equalsIgnoreCase("span")||tagName.equalsIgnoreCase("p"))
			baseSteps.verifyTextField(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(tagName.equalsIgnoreCase("input"))
			verifyInput(element, fieldName, fieldValue);

	}

	private void verifyInput(WebElementFacade element, String fieldName, String fieldValue) {
		final String type = element.getAttribute("type");
		if(type.equalsIgnoreCase("select"))
			baseSteps.verifyDropdown(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());
		else if(type.equalsIgnoreCase("textarea")||type.equalsIgnoreCase("textfield"))
			baseSteps.verifyTextField(element, fieldName, fieldValue, baseSteps.basePage.getExpectedScreenTitle());

	}



} 