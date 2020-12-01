package com.tarkaveda.serenity.pages;

import java.util.Set;

import org.openqa.selenium.WebElement;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;

@DefaultUrl("http://en.wiktionary.org/wiki/Wiktionary:Main_Page")
public class BasePage extends PageObject {
	@FindBy(name="search")
    private WebElementFacade searchTerms;
	
	
	public void sleep(double time)
    {
    	waitABit((long) (time*1000));
    }
	
	public void waitForPageToLoad()
	{
		double duration = 30;
		while(!((String)evaluateJavascript("return document.readyState")).equalsIgnoreCase("complete") && duration>0)
		{
			sleep(0.01);
			duration=-0.01;
		}
	}
	
	public void waitForPageToLoad(double duration)
	{
		while(!((String)evaluateJavascript("return document.readyState")).equalsIgnoreCase("complete") && duration>0)
		{
			sleep(0.01);
			duration=-0.01;
		}
	}
	
	public void waitForWidgetVisible(final WebElementFacade element)
    {
    	element.waitUntilVisible();
    }
    
    public void waitForWidgetNotVisible(final WebElementFacade element)
    {
    	element.waitUntilNotVisible();
    }
    
    public void waitForWidgetEnabled(final WebElementFacade element)
    {
    	element.waitUntilEnabled();
    }
    
    public void waitForWidgetDisabled(final WebElementFacade element)
    {
    	element.waitUntilDisabled();
    }
    
    public void waitForWidgetClickable(final WebElementFacade element)
    {
    	element.waitUntilClickable();
    }
    
    public void waitForWidgetPresent(final WebElementFacade element)
    {
    	element.waitUntilPresent();
    }
	
	public void enterText(final WebElementFacade element, final String fieldValue)
	{
		element.type(fieldValue);
	}
	
	public void regularClick(final WebElementFacade element)
	{
		element.click();
	}
	
	public void javaScriptClick(final WebElementFacade element)
	{
		javaScriptClick(element);
	}
	
	public void sendEnter(final WebElementFacade element)
	{
		element.typeAndEnter("");
	}
	
	public void populateDropdownByVisibleText(final WebElementFacade element,  String fieldValue)
	{
		if(fieldValue.contains("\\$"))
			fieldValue = fieldValue.replace("\\", "");
		element.selectByVisibleText(fieldValue);
	}

    public void populateDropdownByIndex(final WebElementFacade element, String fieldValue)
    {
    	element.selectByIndex(Integer.parseInt(fieldValue));
    }
    
    public void populateDropdownByVisibleValue(final WebElementFacade element,  String fieldValue)
	{
		if(fieldValue.contains("\\$"))
			fieldValue = fieldValue.replace("\\", "");
		element.selectByValue(fieldValue);
	}
    
    public void populateCheckBox(final WebElementFacade element, String fieldValue)
    {
    	if(fieldValue.equalsIgnoreCase("true"))
    		setCheckbox(element, true);
    	else if(fieldValue.equalsIgnoreCase("false"))
    		setCheckbox(element, false);
    	else
    		element.click();
    }
    
    public boolean verifyVisible(final WebElementFacade element)
    {
    	return element.isCurrentlyVisible();
    }
    
    public boolean verifyEnabled(final WebElementFacade element)
    {
    	return element.isCurrentlyEnabled();
    }
    
    public boolean verifyPresent(final WebElementFacade element)
    {
    	return element.isPresent();
    }
    
    public boolean verifyFocus(final WebElementFacade element)
    {
    	return element.hasFocus();
    }
    
    public boolean verifyChecked(final WebElementFacade element)
    {
    	return (((String)evaluateJavascript("return argument[0].checked", element)).equalsIgnoreCase("true"))?true:false;
    }
    
    public String getText(WebElementFacade element)
    {
    	return element.getText();
    }
    
    public String getSelectedLabel(WebElementFacade element)
    {
    	return getSelectedLabelFrom(element);
    }
    
    public String getSelectedValue(WebElementFacade element)
    {
    	return getSelectedValueFrom(element);
    }
    
    public Set<String> getSelectedLabels(WebElementFacade element)
    {
    	return getSelectedOptionLabelsFrom(element);
    }
    
    public Set<String> getSelectedValues(WebElementFacade element)
    {
    	return getSelectedOptionValuesFrom(element);
    }

	public String getExpectedScreenTitle() {
		return null;
	}

	public WebElementFacade getWebElementFacade(WebElement webElement) {
		// TODO Auto-generated method stub
		return element(webElement);
	}
}