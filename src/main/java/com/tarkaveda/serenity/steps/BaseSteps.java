package com.tarkaveda.serenity.steps;

import static com.tarkaveda.serenity.utilities.Verify.verifyThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import com.tarkaveda.serenity.pages.BasePage;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class BaseSteps extends ScenarioSteps {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BasePage basePage;
	public LinkedHashMap<String, WebElementFacade> WebElementMap;

	public void getCurrentScreenInstance(final String screenName)
	{
		try {	
			final ClassLoader classLoader = BasePage.class.getClassLoader();
			String packageName = this.getClass().getPackage().getName().substring(0,this.getClass().getPackage().getName().lastIndexOf("."))+".pages.";
			basePage = (BasePage)classLoader.loadClass(packageName+screenName).newInstance();
			basePage = getPages().get(basePage.getClass());
			getWebElements();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getWebElements()
	{
		WebElementMap = new LinkedHashMap<String, WebElementFacade>();
		for(final Field field : basePage.getClass().getFields())
		{
			final String fieldName = field.getName();
			try{
				if(field.getType().getCanonicalName().contains("WebElementFacade"))
				{
					field.setAccessible(true);
					WebElementFacade element;
					element = (WebElementFacade) field.get(basePage);
					WebElementMap.put(fieldName, element);

				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}

	}
	
	
	@Step("clicking button {1} in {3} screen")
	public void clickElement(WebElementFacade element, final String fieldName, final String fieldValue, final String screenName)
	{
		if(fieldValue.equalsIgnoreCase("enter"))
			basePage.sendEnter(element);
		if(fieldValue.equalsIgnoreCase("click"))
			basePage.regularClick(element);
		if(fieldValue.equalsIgnoreCase("JSClick"))
			basePage.javaScriptClick(element);	
	}

	@Step("clicking radio button {1} in {3} screen")
	public void clickRadioButton(WebElementFacade element, final String fieldName, final String fieldValue, final String screenName)
	{
		if(fieldValue.equalsIgnoreCase("enter"))
			basePage.sendEnter(element);
		if(fieldValue.equalsIgnoreCase("click"))
			basePage.regularClick(element);
		if(fieldValue.equalsIgnoreCase("JSClick"))
			basePage.javaScriptClick(element);
	}
	
	@Step("Entering text{2} in text field {1} in {3} screen")
	public void enterText(WebElementFacade element, final String fieldName, final String fieldValue, final String screenName)
	{
		basePage.enterText(element, fieldValue);
	}
	
	@Step("Selecting option '{2}' for dropdown {1} in {3} screen")
	public void populateDropdown(WebElementFacade element, final String fieldName, final String fieldValue, final String screenName)
	{
		if(fieldValue.matches("\\([0-9][0-9]*\\)"))
			basePage.populateDropdownByIndex(element, fieldValue);
		else
			basePage.populateDropdownByVisibleText(element, fieldValue);
	}
	
	@Step("The Application is launched")
	public void launchApp()
	{
		clearCache();
		basePage.sleep(2);
		basePage.open();
	}
	
	public void switchPage(double duration){
		while(duration > 0 && !this.activate())
		{
			basePage.sleep(0.01);
			duration -= 0.01;
		}
		if(duration <=0)
		{
			
					
		}
	}
	
	private boolean activate(){
		try{
		
			for(final String handle : getDriver().getWindowHandles())
			{
				getDriver().switchTo().window(handle);
				if(getDriver().getTitle().contains(basePage.getExpectedScreenTitle()))
					return true;
			}
		
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
	public void clearCache(){
		Runtime cmd = Runtime.getRuntime();
		try{
			cmd.exec("cmd.exe /c start RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Step("Waiting for {0} screen to load")
	public void waitForPageToLoad(final String screenName){
		basePage.waitForPageToLoad();
	}
	
	@Step("Waiting for widget {1} to be visible in  {0} screen")
	public void waitForWidgetVisible(String expectedScreenTitle, String fieldValue, WebElementFacade element){
		basePage.waitForWidgetVisible(element);
	}
	
	@Step("Waiting for widget {1} to be enabled in {0} screen")
	public void waitForWidgetEnabled(String expectedScreenTitle, String fieldValue, WebElementFacade element){
		basePage.waitForWidgetEnabled(element);
	}
	
	@Step("Waiting for widget {1} to be invisible in {0} screen")
	public void waitForWidgetInVisible(String expectedScreenTitle, String fieldValue, WebElementFacade element){
		basePage.waitForWidgetNotVisible(element);
	}
	
	@Step("Waiting for widget {1} to be Disabled in {0} screen")
	public void waitForWidgetDisabled(String expectedScreenTitle, String fieldValue, WebElementFacade element){
		basePage.waitForWidgetDisabled(element);
	}
	
	@Step("Marking Checkbox {1} as {2} in {3} screen")
	public void populateCheckBox(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle){
		basePage.populateCheckBox(element, fieldValue);
	}
	
	@Step("Verifying that the option selected for dropdown {1} should be {2} in screen{3}")
	public void verifyDropdown(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle)
	{
		if(fieldValue.contains("\\$"))
			fieldValue = fieldValue.replace("\\", "");
		assertTrue(fieldValue.equals(basePage.getSelectedLabel(element)));
	}
	
	@Step("Verifying that the text entered in the textfield {1} should be {2} in screen {3}")
	public void verifyTextField(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle) 
	{
		assertTrue(fieldValue.equals(basePage.getText(element)));
	}
	
	@Step("Verifying that the display text of the link {1} should be {2} in screen {3}")
	public void verifyLink(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle) 
	{
		assertTrue(fieldValue.matches(basePage.getText(element)));
	}
	
	@Step("Verifying that the display text of the label {1} should be {2} in screen {3}")
	public void verifyText(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle) 
	{
		assertTrue(fieldValue.equals(basePage.getText(element)));
	}
	
	@Step("Verifying that the display text of the header {1} should be {2} in screen {3}")
	public void verifyHeader(WebElementFacade element, String fieldName, String fieldValue, String expectedScreenTitle) 
	{
		assertTrue(fieldValue.equals(basePage.getText(element)));
	}
	
	@Step("Verifying that the widget {0} in screen {2} should be {1}")
	public void verifyVisibility(String fieldName, String expectedValue, String expectedScreenTitle){
		boolean expected = (expectedValue.equalsIgnoreCase("visible")) ? true : false;
		assertEquals(expected, basePage.verifyVisible(WebElementMap.get(fieldName)));
	}
	
	@Step("Verifying that the widget {0} in screen {2} should be {1}")
	public void verifyEnability(String fieldName, String expectedValue, String expectedScreenTitle){
		boolean expected = (expectedValue.equalsIgnoreCase("enabled")) ? true : false;
		assertEquals(expected, basePage.verifyEnabled(WebElementMap.get(fieldName)));
	}
	
	@Step("Verifying that the checkbox {0} in screen {2} should be marked {1}")
	public void verifyCheckBox(String fieldName, String expectedValue, String expectedScreenTitle)
	{
		boolean expected = (expectedValue.equalsIgnoreCase("checked"))? true : false;
//		assertEquals(expected, basePage.verifyChecked(WebElementMap.get(fieldName)));
		verifyThat("", expected, equalTo(basePage.verifyChecked(WebElementMap.get(fieldName))));
	}
	
	@Step("Verifying that the checkbox {0} in screen {2} should be marked {1}")
	public void verifyRadioButton(String fieldName, String expectedValue, String expectedScreenTitle)
	{
		boolean expected = (expectedValue.equalsIgnoreCase("true"))? true : false;
		assertEquals(expected, basePage.verifyChecked(WebElementMap.get(fieldName)));
	}
	
	@Step("Verifying that the checkbox {0} in screen {2} should be {1}")
	public void verifyFocus(String fieldName, String expectedValue, String expectedScreenTitle)
	{
		boolean expected = (expectedValue.equalsIgnoreCase("focussed"))? true : false;
		assertEquals(expected, basePage.verifyFocus(WebElementMap.get(fieldName)));
	}
}