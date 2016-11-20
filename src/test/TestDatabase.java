package test;

import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.Driver;

import javax.swing.JFrame;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;

import junit.framework.TestCase;
import model.Database;
import model.DateProducedAndTimeToMake;

public class TestDatabase extends TestCase {
	@BeforeClass
	public static void setupDB() {
		Database.connect();

		try {
			Database.clearData();
		} catch (SQLException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public static void test01_addEmployees() throws NumberFormatException, IOException, SQLException {
		CSVReader reader = new CSVReader(new FileReader("employees.csv"), ',');

		String[] nextLine;
		String name, address, technicalPosition;
		int maxProducts, productType;
		while ((nextLine = reader.readNext()) != null) {

			name = nextLine[0];
			address = nextLine[1];

			if (nextLine[2].length() > 0) {
				maxProducts = new Integer(nextLine[2]).intValue();
				Database.addWorker(name, address, maxProducts);
			} else if (nextLine[3].length() > 0) {
				productType = new Integer(nextLine[3]).intValue();
				Database.addQualityController(name, address, productType);
			} else if (nextLine[4].length() > 0) {
				technicalPosition = nextLine[4];
				Database.addTechnicalStaff(name, address, technicalPosition);
			} else {
				assertTrue("Employee did not have a value determining his/her type", false);
			}
		}

		reader.close();
	}

	@Test
	public static void test02_addProducts() throws SQLException, IOException {
		CSVReader reader = new CSVReader(new FileReader("products.csv"), ',');

		String[] nextLine;
		String tester, producer, repairer, color, software;
		int productID, minutesToMake, length, height, width, productNum, weight;
		Date dateProduced, dateRepaired;

		weight = -1;
		color = null;
		software = null;

		while ((nextLine = reader.readNext()) != null) {

			productID = toInt(nextLine[0]);
			dateProduced = new Date(nextLine[1]);
			minutesToMake = toInt(nextLine[2]);
			length = toInt(nextLine[3]);
			width = toInt(nextLine[4]);
			height = toInt(nextLine[5]);
			productNum = toInt(nextLine[6]);
			tester = nextLine[7];
			producer = nextLine[8];

			if (productNum == 1) {
				software = nextLine[9];
			} else if (productNum == 2) {
				color = nextLine[10];
			} else if (productNum == 3) {
				weight = toInt(nextLine[11]);
			} else {
				assertTrue("Invalid product type num", false);
			}

			Database.addProduct(productID, dateProduced, minutesToMake, height, length, width, productNum, tester,
					producer, weight, color, software);

			repairer = nextLine[12];
			if (repairer.length() > 0) {
				dateRepaired = new Date(nextLine[13]);
				Database.addRepair(repairer, productID, dateRepaired);
			}
		}

		reader.close();
	}

	@Test
	public static void test03_addCustomers() throws IOException, SQLException {
		test18_import();
	}

	@Test
	public static void test04_addAccounts() throws NumberFormatException, IOException, SQLException {
		CSVReader reader = new CSVReader(new FileReader("accounts.csv"), ',');

		String[] nextLine;
		Date dateEstablished;
		int accountNum, productID;
		float productCost;
		while ((nextLine = reader.readNext()) != null) {

			accountNum = toInt(nextLine[0]);
			dateEstablished = new Date(nextLine[1]);
			productCost = new Float(nextLine[2]).floatValue();
			productID = toInt(nextLine[3]);

			Database.addAccount(accountNum, dateEstablished, productCost, productID);
		}
		reader.close();
	}

	@Test
	public static void test05_addComplaints() throws NumberFormatException, IOException, SQLException {
		CSVReader reader = new CSVReader(new FileReader("complaints.csv"), ',');

		String[] nextLine;
		Date dateOf;
		int complaintID, productID;
		String expectedTreatment, description;
		while ((nextLine = reader.readNext()) != null) {

			complaintID = toInt(nextLine[0]);
			dateOf = new Date(nextLine[1]);
			description = nextLine[2];
			expectedTreatment = nextLine[3];
			productID = toInt(nextLine[4]);

			Database.addComplaint(complaintID, dateOf, description, expectedTreatment, productID);
		}

		reader.close();
	}

	@Test
	public static void test06_addAccidents() throws NumberFormatException, IOException, SQLException {
		CSVReader reader = new CSVReader(new FileReader("accidents.csv"), ',');

		String[] nextLine;
		Date dateOf;
		int accidentNum, productID, daysLost, repairID;
		while ((nextLine = reader.readNext()) != null) {

			productID = -1;
			repairID = -1;

			accidentNum = toInt(nextLine[0]);
			dateOf = new Date(nextLine[1]);
			daysLost = toInt(nextLine[2]);

			if (nextLine[3].length() > 0) {
				repairID = toInt(nextLine[3]);
			} else if (nextLine[4].length() > 0) {
				productID = toInt(nextLine[4]);
			}

			Database.addAccident(accidentNum, dateOf, daysLost, repairID, productID);
		}
		reader.close();
	}

	@Test
	public static void test07_getDateAndTimeForProduct() throws SQLException {
		Map<Integer, DateProducedAndTimeToMake> qualityControllers = new HashMap<Integer, DateProducedAndTimeToMake>();

		qualityControllers.put(11, new DateProducedAndTimeToMake(new Date(2016, 5, 11), 1));
		qualityControllers.put(12, new DateProducedAndTimeToMake(new Date(2016, 6, 11), 2));
		qualityControllers.put(13, new DateProducedAndTimeToMake(new Date(2016, 7, 11), 3));

		for (Map.Entry<Integer, DateProducedAndTimeToMake> entry : qualityControllers.entrySet()) {
			Integer productID = entry.getKey();
			DateProducedAndTimeToMake expectedResult = entry.getValue();

			DateProducedAndTimeToMake actualResult = Database.getDateAndTimeToMakeFor(productID);

			assertEquals(actualResult.toString(), expectedResult.toString());
		}
	}

	@Test
	public static void test08_getProductsMadeByWorker() throws SQLException, IOException {
		CSVReader reader = new CSVReader(new FileReader("employees.csv"), ',');

		String[] nextLine;
		String name;
		while ((nextLine = reader.readNext()) != null) {

			name = nextLine[0];

			if (nextLine[2].length() > 0) {
				// Is worker
				ArrayList<Integer> DBproductsMadeBy = Database.getProductsMadeBy(name);
				ArrayList<Integer> CSVproductsMadeBy = getProductsMadeByFromCSV(name);

				assertTrue("DB and CSV give different products made by for " + name,
						equalLists(DBproductsMadeBy, CSVproductsMadeBy));
			}
		}

		reader.close();
	}

	@Test
	public static void test09_getErrorByQC() throws SQLException {
		Map<String, Integer> qualityControllers = new HashMap<String, Integer>();

		qualityControllers.put("Bob Board", 0);
		qualityControllers.put("Edgar Epitaph", 1);
		qualityControllers.put("Hitler Helm", 2);

		for (Map.Entry<String, Integer> entry : qualityControllers.entrySet()) {
			String name = entry.getKey();
			Integer errorsCorrect = entry.getValue();

			Integer errorsFromDB = new Integer(Database.getErrorsFrom(name));
			assertEquals("Errors not equal for " + name, errorsCorrect, errorsFromDB);
		}
	}

	@Test
	public static void test10_getCostProduct3RepairedBy() throws SQLException {
		Map<String, Float> qualityControllers = new HashMap<String, Float>();

		qualityControllers.put("Bob Board", 0f);
		qualityControllers.put("Edgar Epitaph", 0f);
		qualityControllers.put("Hitler Helm", 40.42f);

		for (Map.Entry<String, Float> entry : qualityControllers.entrySet()) {
			String name = entry.getKey();
			Float costCorrect = entry.getValue();

			Float costFromDB = new Float(Database.retrieveProduct3Cost(name));
			assertEquals("Product 3 cost repaired not equal for " + name, costCorrect, costFromDB);
		}
	}

	@Test
	public static void test11_getCustomerWhoPurchasedAllColor() throws SQLException {
		Map<String, ArrayList<String>> qualityControllers = new HashMap<String, ArrayList<String>>();

		ArrayList<String> blueEmployees = new ArrayList<String>();
		blueEmployees.add("Hitler Helm");
		qualityControllers.put("Blue", blueEmployees);

		ArrayList<String> greenEmployees = new ArrayList<String>();
		greenEmployees.add("Kaitlyn Kull");
		qualityControllers.put("Green", greenEmployees);

		for (Map.Entry<String, ArrayList<String>> entry : qualityControllers.entrySet()) {
			String color = entry.getKey();
			ArrayList<String> employeesCorrect = entry.getValue();

			ArrayList<String> employeesFromDB = Database.getCustomersWhoPurchased(color);

			assertTrue("Customer who purchased all different for " + color,
					equalLists(employeesCorrect, employeesFromDB));
		}
	}

	@Test
	public static void test12_getWorkDaysLostFromRepairs() throws SQLException {
		int expectedDaysLost = 13;
		int daysLost = Database.retrieveDaysLostFromRepairs();

		assertEquals(expectedDaysLost, daysLost);
	}

	@Test
	public static void test13_getAllCustomersWhoAreWorkers() throws SQLException, IOException {
		ArrayList<String> customerWorkersExpected = getCustomerWorkersCSV();
		ArrayList<String> customerWorkersDB = Database.getAllCustomerWhoAreWorkers();

		assertTrue(equalLists(customerWorkersExpected, customerWorkersDB));
	}

	@Test
	public static void test14_customersPurchasedOwn() throws SQLException {
		ArrayList<String> customersPurchasedOwnExpected = new ArrayList<String>();
		customersPurchasedOwnExpected.add("Kaitlyn Kull");

		ArrayList<String> customersPurchasedOwnDB = Database.getEmployeesWhoPurchasedOwnProduct();

		assertTrue(equalLists(customersPurchasedOwnExpected, customersPurchasedOwnDB));
	}

	@Test
	public static void test15_averageCostProductYear() throws SQLException {
		float averageCostDB = Database.getAverageCost(2016);
		float expectedAverageCost2016 = 34.048f;

		assertEquals(expectedAverageCost2016, averageCostDB);
	}

	@Test
	public static void test16_switchTSandQC() throws SQLException {
		String[] qcNames = Database.getAllQualityControllerNames();
		String[] tsNames = Database.getAllTechnicalStaffNames();

		assertTrue(arrayHas(qcNames, "Bob Board"));
		assertTrue(arrayHas(tsNames, "Charlie Cup"));

		Database.switchPositions("Charlie Cup", "Bob Board");

		qcNames = Database.getAllQualityControllerNames();
		tsNames = Database.getAllTechnicalStaffNames();

		assertTrue(arrayHas(qcNames, "Charlie Cup"));
		assertTrue(arrayHas(tsNames, "Bob Board"));
	}

	@Test
	public static void test17_deleteAccidentsInRange() throws SQLException {
		int accidentCount = Database.getAccidentCount();

		Calendar calendar = new GregorianCalendar(2016, Calendar.NOVEMBER, 5);
		Date startDate = calendar.getTime();

		calendar = new GregorianCalendar(2016, Calendar.NOVEMBER, 7);
		Date endDate = calendar.getTime();

		Database.deleteAccidentsInRange(startDate, endDate);

		accidentCount = Database.getAccidentCount();
		int expectedAccidentCount = 2;

		assertEquals(expectedAccidentCount, accidentCount);
	}

	public static void test18_import() throws IOException, SQLException {
		Database.importCustomers("customers.csv");

		int actualCustomerCount = Database.getCustomersCount();
		int expectedCustomerCount = 10;

		assertEquals(expectedCustomerCount, actualCustomerCount);
	}

	@Test
	public static void test19_export() throws IOException {
		Database.exportCustomers("testExport.csv");

		CSVReader reader = new CSVReader(new FileReader("testExport.csv"), ',');

		String[][] expectedResults = { { "Hitler Helm", "234 Eighth" }, { "Kaitlyn Kull", "567 Ninth" },
				{ "Luna Larn", "890 Tenth" }, { "Martha Mun", "543 Tree" }, { "Nick Nerth", "295 Rock" },
				{ "Oscar Obto", "626 Gym" }, { "Patrick Path", "326 Water" }, { "Ronald Ricky", "835 Cliff" },
				{ "Susan Seel", "294 Rose" }, { "Taylor Tawn", "275 Wood" } };

		String[] nextLine;
		int lineNumber = 0;
		while ((nextLine = reader.readNext()) != null) {
			assertTrue(Arrays.equals(expectedResults[lineNumber], nextLine));

			lineNumber++;
		}

		reader.close();
	}

	@Test
	public static void test20_Error() {
		try {
			Database.addCustomer("Oscar Obto", "564 Waffle");
			assertTrue(false);
		} catch (SQLException e) {
			String message = e.getMessage();
			assertTrue(message.toLowerCase().contains("unique"));
		}

		try {
			Database.performQuery("delete from product where productID = 15");
			assertTrue(false);
		} catch (SQLException e) {
			String message = e.getMessage();
			assertTrue(message.toLowerCase().contains("integrity"));
		}

		try {
			Database.addComplaint(-1, new Date(), "", "", 11);
			assertTrue(false);
		} catch (SQLException e) {
			String message = e.getMessage();
			assertTrue(message.toLowerCase().contains("null"));
		}
	}
	
	@Test
	public static void test21_quit() {
		JFrame mainFrame = Driver.createAndShowGUI(false);

		assertTrue(Database.connected);

		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));

		assertTrue(!Database.connected);
	}


	@AfterClass
	public static void cleanupDB() {
		if (Database.connected) {
			Database.disconnect();
		}
	}

	private static <T> boolean arrayHas(T[] array, T value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			} else if (array[i].equals(value)) {
				return true;
			}
		}

		return false;
	}

	private static ArrayList<String> getCustomerWorkersCSV() throws IOException {
		// Get employees
		CSVReader reader = new CSVReader(new FileReader("employees.csv"), ',');

		String[] nextLine;
		String name;
		ArrayList<String> employeeNames = new ArrayList<String>();

		while ((nextLine = reader.readNext()) != null) {
			name = nextLine[0];
			employeeNames.add(name);
		}

		reader.close();

		// Check against customers
		reader = new CSVReader(new FileReader("customers.csv"), ',');

		ArrayList<String> matchingNames = new ArrayList<String>();

		while ((nextLine = reader.readNext()) != null) {
			name = nextLine[0];

			if (employeeNames.contains(name)) {
				matchingNames.add(name);
			}
		}

		reader.close();

		return matchingNames;
	}

	private static ArrayList<Integer> getProductsMadeByFromCSV(String name) throws IOException {
		CSVReader reader = new CSVReader(new FileReader("products.csv"), ',');

		String[] nextLine;
		int productID;
		String producer;

		ArrayList<Integer> productIDs = new ArrayList<Integer>();

		while ((nextLine = reader.readNext()) != null) {

			productID = toInt(nextLine[0]);
			producer = nextLine[8];

			if (producer.equals(name)) {
				productIDs.add(new Integer(productID));
			}
		}

		reader.close();

		return productIDs;
	}

	private static int toInt(String st) {
		return new Integer(st).intValue();
	}

	private static <T extends Comparable<? super T>> boolean equalLists(List<T> one, List<T> two) {
		if (one == null && two == null) {
			return true;
		}

		if ((one == null && two != null) || one != null && two == null || one.size() != two.size()) {
			return false;
		}

		// to avoid messing the order of the lists we will use a copy
		// as noted in comments by A. R. S.
		one = new ArrayList<T>(one);
		two = new ArrayList<T>(two);

		Collections.sort(one);
		Collections.sort(two);
		return one.equals(two);
	}

}