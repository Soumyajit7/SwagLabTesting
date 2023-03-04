package base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.annotations.Test;

import testcases.ChildClass;
import utilities.ExcelDataReaderWriter;

public class TestController extends BaseTest {

	/*
	 * *****************************************************************************
	 * # Test Name : test # Description : To controll all the testcases # Developed
	 * by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	@Test
	public void test() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {

		// Read Keyword Data from new XLSX sheet to get regression
		regressionData_filepath = configProp.getProperty("regressionData");
		String filePath = System.getProperty("user.dir") + regressionData_filepath;
		regDataSet = ExcelDataReaderWriter.readDataFromExcel(filePath, "Regreesion");

		// Read Keyword Data from new XLSX sheet to get dataSet
		mainData_filepath = configProp.getProperty("mainData");
		filePath = System.getProperty("user.dir") + mainData_filepath;
		dataSet = ExcelDataReaderWriter.readDataFromExcel(filePath, "Keyword");

		// System.out.println(regDataSet);
		// System.out.println(dataSet);

		HashMap<String, Boolean> runData = new HashMap<String, Boolean>();

		for (LinkedHashMap<String, String> regData : regDataSet) {
			String testCase = regData.get("TestCase");
			String run = regData.get("Run");
			if (run.equalsIgnoreCase("Yes")) {
				runData.put(testCase, true);
			}
		}
		// System.out.println("runData->" + runData);

		for (LinkedHashMap<String, String> map : dataSet) {
			// System.out.println("Map->TestCase : " + map.get("TestCase"));
			// System.out.println("runData->TestCase : " +
			// runData.get(map.get("TestCase")));
			// System.out.print("{ ");
			if (runData.get(map.get("TestCase")) == null) {
				continue;
			} else if (runData.get(map.get("TestCase")) == true) {
				testCase = map.get("TestCase");
				int column = 0;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					// String key = entry.getKey();
					String value = entry.getValue();
					// System.out.print(key + "=" + value + ", ");
					if (value != "") {
						if (column == 1) {
							test = extent.createTest(value, "TestCase Name : " + value);
							controllerTestcase = value;
						} else if (column != 0 && column != 1) {
							// System.out.println(value + "()");
							String methodName = value;
							ChildClass obj = new ChildClass();
							Method method = obj.getClass().getMethod(methodName);
							method.invoke(obj);
						}
					}
					column += 1;
				}
			}
			// System.out.println("}");
		}
	}
}
