package com.tarkaveda.serenity.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public static Map<String, Map<String, Map<String, String>>> readExcel(final String excelFilePath) throws IOException
	{
		Map<String, Map<String, Map<String, String>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> screenDataMap;
		Map<String, String> actionDataMap;
		String screenName=null, action=null, fieldName=null, fieldValue=null;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		int i;
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		while(iterator.hasNext())
		{
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.iterator();
			i=0;
			while(cellIterator.hasNext())
			{
				Cell cell = cellIterator.next();
				switch(i)
				{
				case 0 :
					screenName = getDataFromCell(cell);
				case 1 :
					action = getDataFromCell(cell);
				case 2 :
					fieldName = getDataFromCell(cell);
				case 3 :
					fieldValue = getDataFromCell(cell);
				}
				i++;
			}
			if(!dataMap.containsKey(screenName))
			{
				screenDataMap = new LinkedHashMap<String, Map<String, String>>();
				actionDataMap = new LinkedHashMap<String, String>();
				actionDataMap.put(fieldName, fieldValue);
				screenDataMap.put(action, actionDataMap);
				dataMap.put(screenName, screenDataMap);
				
			}
			else if(!dataMap.get(screenName).containsKey(action))
			{
				actionDataMap = new LinkedHashMap<String, String>();
				actionDataMap.put(fieldName, fieldValue);
				dataMap.get(screenName).put(action, actionDataMap);
			}
			else
				dataMap.get(screenName).get(action).put(fieldName, fieldValue);
		}
		workbook.close();
		inputStream.close();
		return dataMap;

	}

	private static String getDataFromCell(Cell cell) {
		String text = null;
		switch(cell.getCellType())
		{
		case Cell.CELL_TYPE_STRING :
			text = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN :
			Boolean flag = cell.getBooleanCellValue();
			text = "false";
			if(flag)
				text = "true";
			break;
		case Cell.CELL_TYPE_NUMERIC :
			text = ""+cell.getNumericCellValue();
			
		}
		return text;
	}

}
