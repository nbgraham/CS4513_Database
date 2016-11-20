package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import oracle.jdbc.OracleTypes;

public final class Database {
	private static Connection conn;
	private static Statement stmt;
	private static boolean connected = false;

	private final static String sourceURL = "jdbc:oracle:thin:@//oracle.cs.ou.edu:1521/pdborcl.cs.ou.edu";
	private final static String username = "grah8384";
	private final static String password = "TJig5Qk6";

	public static boolean connect() {
		boolean wasSuccessful = true;

		// Loading a database driver

		// Make sure driver class is in the project
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (Exception x) {
			System.out.println("Unable to load the driver class!");
			wasSuccessful = false;
		}

		// Attempt to connect to database
		try {
			conn = DriverManager.getConnection(sourceURL, username, password);
			stmt = conn.createStatement();
		} catch (SQLException ex) {
			// Print SQL error
			System.err.println("SQLException: " + ex.getMessage());
			wasSuccessful = false;
		}

		if (wasSuccessful) {
			connected = true;
		}

		return wasSuccessful;
	}

	public static boolean disconnect() {
		boolean disconnected = false;

		if (connected) {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnected = true;
		}

		if (disconnected) {
			connected = false;
		}

		return disconnected;
	}
	
	public static void clearData() throws SQLException {
		CallableStatement cs = conn.prepareCall("{call clear_data()}");
		cs.execute();
		cs.close();
	}

	public static String[] getAllWorkerNames() {
		String[] workerNames = {};

		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select name from worker");

				ArrayList<String> workers = new ArrayList<String>();
				while (rset.next()) {
					workers.add(rset.getString(1));
				}
				workerNames = workers.toArray(workerNames);

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return workerNames;
	}

	public static String[] getAllQualityControllerNames() {
		String[] qualityControllers = {};

		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select name from quality_controller");

				ArrayList<String> qcs = new ArrayList<String>();
				while (rset.next()) {
					qcs.add(rset.getString(1));
				}
				qualityControllers = qcs.toArray(qualityControllers);

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return qualityControllers;
	}

	public static String[] getAllTechnicalStaffNames() {
		String[] technicalStaff = {};

		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select name from technical_staff");

				ArrayList<String> qcs = new ArrayList<String>();
				while (rset.next()) {
					qcs.add(rset.getString(1));
				}
				technicalStaff = qcs.toArray(technicalStaff);

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return technicalStaff;
	}

	public static int getAccidentCount() {
		int accidentCount = -1;
		
		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select count(*) from accident");

				while (rset.next()) {
					accidentCount = rset.getInt(1);
				}

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return accidentCount;
	}
	
	public static ArrayList<Integer> getAllProductIDsArrayList() {
		ArrayList<Integer> productIDsArrayList = new ArrayList<Integer>();

		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select productID from product");

				// Build array list
				productIDsArrayList = new ArrayList<Integer>();
				while (rset.next()) {
					productIDsArrayList.add(new Integer(rset.getInt(1)));
				}

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return productIDsArrayList;
	}

	public static int[] getAllProductIDs() {
		ArrayList<Integer> productIDsArrayList = getAllProductIDsArrayList();

		// Move from ArrayList to int[]
		int resultCount = productIDsArrayList.size();
		int[] productIDs = new int[resultCount];
		for (int i = 0; i < resultCount; i++) {
			productIDs[i] = productIDsArrayList.get(i).intValue();
		}

		return productIDs;
	}

	public static ArrayList<Integer> getAllRepairIDsArrayList() {
		ArrayList<Integer> productIDsArrayList = new ArrayList<Integer>();

		if (connected) {
			try {
				// SQL call to get all workers
				ResultSet rset = stmt.executeQuery("select productID from repair");

				// Build array list
				productIDsArrayList = new ArrayList<Integer>();
				while (rset.next()) {
					productIDsArrayList.add(new Integer(rset.getInt(1)));
				}

				rset.close();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		return productIDsArrayList;
	}

	public static int[] getRepairIDs() {
		ArrayList<Integer> repairIDsArrayList = getAllRepairIDsArrayList();

		// Move from ArrayList to int[]
		int resultCount = repairIDsArrayList.size();
		int[] productIDs = new int[resultCount];
		for (int i = 0; i < resultCount; i++) {
			productIDs[i] = repairIDsArrayList.get(i).intValue();
		}

		return productIDs;
	}

	public static ArrayList<Integer> getPurchaseIDs() {
		// Prepare call to PL/SQL
		ArrayList<Integer> purchaseIDs = new ArrayList<Integer>();
	
		try {
			CallableStatement cs = conn.prepareCall("{? = call get_purchase_ids()}");
	
			cs.registerOutParameter(1, OracleTypes.CURSOR);
	
			// Execute call
			cs.execute();
	
			ResultSet rs = (ResultSet) cs.getObject(1);
	
			while (rs.next()) {
				purchaseIDs.add(rs.getInt(1));
			}
	
			cs.close();
			rs.close();
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return purchaseIDs;
	}

	public static int getErrorsFrom(String selectedQC) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call get_errors_from(?,?)}");
	
		// Set data in call
		cs.setString(1, selectedQC);
		cs.registerOutParameter(2, Types.INTEGER);
	
		// Execute call
		cs.execute();
	
		// Get results of function
		int errors = cs.getInt(2);
	
		cs.close();
	
		return errors;
	}

	public static ArrayList<Integer> getProductsMadeBy(String selectedWorker) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{? = call get_product_by(?)}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.setString(2, selectedWorker);
	
		// Execute call
		cs.execute();
	
		ResultSet rs = (ResultSet) cs.getObject(1);
		ArrayList<Integer> productIDs = new ArrayList<Integer>();
	
		while (rs.next()) {
			productIDs.add(rs.getInt(1));
		}
	
		cs.close();
		rs.close();
	
		return productIDs;
	}

	public static float getAverageCost(int year) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call avg_cost(?,?,?)}");
	
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		Date start = cal.getTime();
	
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11); // 11 = december
		cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
	
		Date end = cal.getTime();
	
		// Add parameters
		cs.setDate(1, sqlDate(start));
		cs.setDate(2, sqlDate(end));
		cs.registerOutParameter(3, Types.FLOAT);
	
		// Execute call
		cs.execute();
	
		float averageCost = cs.getFloat(3);
	
		cs.close();
	
		return averageCost;
	}

	public static DateProducedAndTimeToMake getDateAndTimeToMakeFor(int productID) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call get_date_time_product(?,?,?)}");
	
		// Set data in call
		cs.setInt(1, productID);
		cs.registerOutParameter(2, Types.DATE);
		cs.registerOutParameter(3, Types.INTEGER);
	
		// Execute call
		cs.execute();
	
		// Get results of function
		Date dateProduced = cs.getDate(2);
		int timeToMake = cs.getInt(3);
	
		DateProducedAndTimeToMake response = new DateProducedAndTimeToMake(dateProduced, timeToMake);
	
		cs.close();
	
		return response;
	}

	public static ArrayList<String> getAllCustomerWhoAreWorkers() throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{? = call get_customer_workers()}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
	
		// Execute call
		cs.execute();
	
		ResultSet rs = (ResultSet) cs.getObject(1);
		ArrayList<String> customerWorkers = new ArrayList<String>();
	
		while (rs.next()) {
			customerWorkers.add(rs.getString(1));
		}
	
		return customerWorkers;
	}

	public static ArrayList<String> getCustomersWhoPurchased(String color) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{? = call get_customers_color(?)}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.setString(2, color);
	
		// Execute call
		cs.execute();
	
		ResultSet rs = (ResultSet) cs.getObject(1);
		ArrayList<String> customerNames = new ArrayList<String>();
	
		while (rs.next()) {
			customerNames.add(rs.getString(1));
		}
	
		cs.close();
		rs.close();
	
		return customerNames;
	}

	public static ArrayList<String> getEmployeesWhoPurchasedOwnProduct() throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{? = call get_purchased_own()}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);
	
		// Execute call
		cs.execute();
	
		ResultSet rs = (ResultSet) cs.getObject(1);
		ArrayList<String> employeeNames = new ArrayList<String>();
	
		while (rs.next()) {
			employeeNames.add(rs.getString(1));
		}
	
		cs.close();
		rs.close();
	
		return employeeNames;
	}
	
	public static int getCustomersCount() throws SQLException {
		int customersCount = -1;
		
		ResultSet rs = stmt.executeQuery("select count(*) from customer");
		
		while(rs.next()) {
			customersCount = rs.getInt(1);
		}
		
		rs.close();
		
		return customersCount;
	}

	public static float retrieveProduct3Cost(String qualityControllerName) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call get_product3_cost(?,?)}");
	
		// Set data in call
		cs.setString(1, qualityControllerName);
		cs.registerOutParameter(2, Types.FLOAT);
	
		// Execute call
		cs.execute();
	
		// Get results of function
		float cost = cs.getFloat(2);
	
		cs.close();
	
		return cost;
	}

	public static int retrieveDaysLostFromRepairs() throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call get_days_lost_repairs(?)}");
	
		cs.registerOutParameter(1, Types.INTEGER);
	
		// Execute call
		cs.execute();
	
		// Get results of function
		int daysLost = cs.getInt(1);
	
		cs.close();
	
		return daysLost;
	}

	public static void addProduct(int productID, Date dateProduced, int minutesToMake, int height, int length,
			int width, int productNum, String testerName, String producerName, int weight, String color,
			String software) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newproduct(?,?,?,?,?,?,?,?,?,?,?,?)}");

		// Set data in call
		cs.setInt(1, productID);
		cs.setDate(2, sqlDate(dateProduced));

		cs.setInt(3, minutesToMake);
		cs.setInt(4, height);
		cs.setInt(5, length);
		cs.setInt(6, weight);
		cs.setInt(7, productNum);
		cs.setString(8, testerName);
		cs.setString(9, producerName);

		// Set unknown values to null (depending on product type)
		cs.setNull(10, Types.INTEGER);
		cs.setNull(11, Types.VARCHAR);
		cs.setNull(12, Types.VARCHAR);

		if (productNum == 1) {
			cs.setString(12, software);
		} else if (productNum == 2) {
			cs.setString(11, color);
		} else if (productNum == 3) {
			cs.setInt(10, weight);
		}

		// Execute call
		cs.executeUpdate();
		
		cs.close();
	}

	/**
	 * 
	 * @param accidentNumber
	 * @param dateOf
	 * @param daysLost
	 * @param repairID
	 *            -1 means null
	 * @param productionID
	 *            -1 means null
	 * @throws SQLException
	 */
	public static void addAccident(int accidentNumber, Date dateOf, int daysLost, int repairID, int productionID)
			throws SQLException {
		CallableStatement cs = conn.prepareCall("{call newaccident(?,?,?,?,?)}");

		// Set data in call
		cs.setInt(1, accidentNumber);
		cs.setDate(2, new java.sql.Date(dateOf.getTime()));
		cs.setInt(3, daysLost);

		// Set one to null depending on which is -1
		cs.setNull(4, Types.INTEGER);
		cs.setNull(5, Types.INTEGER);
		if (repairID != -1) {
			cs.setInt(4, repairID);
		} else if (productionID != -1) {
			cs.setInt(5, productionID);
		} else {
			throw new IllegalArgumentException("Either repairID or productionID must be not -1");
		}

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void addRepair(String technicalStaffName, int productID, Date dateRepaired) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newrepair(?,?,?)}");
	
		// Add parameters
		cs.setString(1, technicalStaffName);
		cs.setInt(2, productID);
		cs.setDate(3, sqlDate(dateRepaired));
	
		// Execute call
		cs.execute();
	
		cs.close();		
	}

	public static void addAccount(int accountNumber, Date dateEstablished, float productCost,
			int productID) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newaccount(?,?,?,?)}");
	
		// Add parameters
		cs.setInt(1, accountNumber);
		cs.setDate(2, new java.sql.Date(dateEstablished.getTime()));
		cs.setFloat(3, productCost);
		cs.setInt(4, productID);
	
		// Execute call
		cs.execute();
	
		cs.close();
	}

	public static void addWorker(String name, String address, int maxProductsDaily) throws SQLException {
		CallableStatement cs = conn.prepareCall("{call newworker(?,?,?)}");

		// Set data in call
		cs.setString(1, name);
		cs.setString(2, address);
		cs.setInt(3, maxProductsDaily);

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void addQualityController(String name, String address, int productType) throws SQLException {
		CallableStatement cs = conn.prepareCall("{call newqualitycontroller(?,?,?)}");

		// Set data in call
		cs.setString(1, name);
		cs.setString(2, address);
		cs.setInt(3, productType);

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void addTechnicalStaff(String name, String address, String technicalPosition) throws SQLException {
		CallableStatement cs = conn.prepareCall("{call newtechnicalstaff(?,?,?)}");

		// Set data in call
		cs.setString(1, name);
		cs.setString(2, address);
		cs.setString(3, technicalPosition);

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void addComplaint(int complaintID, Date dateOf, String description, String expectedTreatment,
			int purchaseID) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newcomplaint(?,?,?,?,?)}");
	
		// Add parameters
		
		if (complaintID == -1) {
			cs.setNull(1, Types.INTEGER);
		} else {
			cs.setInt(1, complaintID);
		}
		
		cs.setDate(2, sqlDate(dateOf));
		cs.setString(3, description);
		cs.setString(4, expectedTreatment);
		cs.setInt(5, purchaseID);
	
		// Execute call
		cs.execute();
	
		cs.close();
	}

	public static void addCustomer(String name, String address) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newcustomer(?,?)}");
	
		// Add parameters
		cs.setString(1, name);
		cs.setString(2, address);
	
		// Execute call
		cs.execute();
	
		cs.close();
	
	}

	public static void addCustomer(String name, String address, ArrayList<Integer> purchaseIDs) throws SQLException {
		addCustomer(name, address);
	
		for (int i = 0; i < purchaseIDs.size(); i++) {
			addPurchase(name, purchaseIDs.get(i).intValue());
		}
	}

	private static void addPurchase(String name, int intValue) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call newpurchase(?,?)}");
	
		// Add parameters
		cs.setString(1, name);
		cs.setInt(2, intValue);
	
		// Execute call
		cs.execute();
	
		cs.close();
	}

	public static void deleteAccidentsInRange(Date startDate, Date endDate) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call delete_accidents(?,?)}");

		cs.setDate(1, sqlDate(startDate));
		cs.setDate(2, sqlDate(endDate));

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void switchPositions(String technicalStaffName, String qualityControllerName) throws SQLException {
		// Prepare call to PL/SQL
		CallableStatement cs = conn.prepareCall("{call switch_positions(?,?)}");

		cs.setString(1, technicalStaffName);
		cs.setString(2, qualityControllerName);

		// Execute call
		cs.execute();

		cs.close();
	}

	public static void importCustomers(String csvFileName) throws IOException, SQLException {
		CSVReader reader = new CSVReader(new FileReader(csvFileName), ',');

		String[] nextLine;
		String name, address;
		while ((nextLine = reader.readNext()) != null) {
			
			name = nextLine[0];
			address = nextLine[1];

			if (nextLine.length == 3 && nextLine[2].length() > 0) {
				String[] purchaseIDs = nextLine[2].split(",");

				ArrayList<Integer> purchases = new ArrayList<Integer>(purchaseIDs.length);
				for (int i = 0; i < purchaseIDs.length; i++) {
					String st = purchaseIDs[i];
					purchases.add(new Integer(st));
				}
				addCustomer(name, address, purchases);
			} else {
				addCustomer(name, address);
			}
		}

		reader.close();
	}

	public static void exportCustomers(String path) throws IOException {
		// Prepare call to PL/SQL
		CSVWriter writer = new CSVWriter(new FileWriter(path), ',');

		try {
			CallableStatement cs = conn.prepareCall("{? = call get_customers()}");

			cs.registerOutParameter(1, OracleTypes.CURSOR);

			// Execute call
			cs.execute();

			ResultSet rs = (ResultSet) cs.getObject(1);

			while (rs.next()) {
				String name = rs.getString(1);
				String address = rs.getString(2);

				String[] entry = { name, address };
				writer.writeNext(entry);
			}

			cs.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		writer.close();
	}

	private static java.sql.Date sqlDate(java.util.Date date) {
		if (date == null) return null;
		
		return new java.sql.Date(date.getTime());
	}

	public static void performQuery(String query) throws SQLException {
		stmt.executeQuery(query);
	}
}
