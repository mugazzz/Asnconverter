package duAutomationPOC1;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DataValidDB {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Report";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	public static String Curr_user_directory_path = System.getProperty("user.dir");
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String MSISDN = "971520001714";
	public ExtentReports report;
	public ExtentTest test;
	static String Data = Curr_user_directory_path + "\\TestData.xlsx";
	

	public static void main(String[] args) {
		Curr_user_directory_path = System.getProperty("user.dir");
		createtimestampfold();
		System.setProperty("logfilename", trfold + "\\Logs");
		DOMConfigurator.configure("log4j.xml");
		ExtentReports extent = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
		extent.attachReporter(htmlReporter);
		info("Starting execution at +:" + ExecutionStarttime);

		String table = "renewal";
		String tbl = null;

		tbl = cis_db(table);

		//System.out.println(tbl);

		ExtentTest test = extent.createTest("CIS_DB_" + MSISDN + "_Output");

		test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank'></a></b><br>" + tbl + "</table>");
		extent.flush();
		endTestCase("Disconnected From CIS_DB");

	}

	public static String cis_db(String table) {
		String table_data = "";
		String table_data1 = "";
		
		
		try {
			
			if (table.equalsIgnoreCase("adhoc")) {
				table_data = ValidationQuery(table);
			} else if (table.equalsIgnoreCase("renewal")) {
				table_data = ValidationQuery(table);

			} else {
				table_data = ValidationQuery("adhoc");
				table_data1 = ValidationQuery("renewal");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return table_data + table_data1;
	}

	public static String ValidationQuery(String Table) throws SQLException, FilloException {
		String que = "select ";
		String data = null;
		String inputData;
		int Iterator = 0;
	 Recordset rs1 = null;
	 Statement st = null;
	 Connection conn = null;
		String table_data = "<style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td ,caption{padding:5px;text-align:left;}</style>"
				+ "<table>" + "<tr>" + "<th>Expected Data</th>" + "<th>DB Actual Data</th>" + "<th>Status</th>"
				+ "</tr>";

		String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
		Properties parameters = new Properties();
		parameters.put("user", "mugazmaveric1");
		parameters.put("password", "maverick");

		Fillo fillo = new Fillo();
		com.codoid.products.fillo.Connection conn1 = fillo.getConnection(Data);
		if (Table.equalsIgnoreCase("adhoc")) {

			rs1 = conn1.executeQuery("Select * from DB_adhoc where \"Execution \" = 'Yes'");

		} else if (Table.equalsIgnoreCase("renewal")) {

			rs1 = conn1.executeQuery("Select * from DB_renewal where \"Execution \" = 'Yes'");
		}
		try {
		while (rs1.next()) {
			for (Iterator = 1; Iterator < rs1.getFieldNames().size(); Iterator++) {

				inputData = rs1.getFieldNames().get(Iterator);
				que = que + inputData + ",";

			}
			if(Table.equalsIgnoreCase("adhoc")||Table.equalsIgnoreCase("")) {
				que = que.substring(0, que.length()-1) + " from rs_adhoc_products where msisdn=" + MSISDN + " order by last_action_date desc limit 1";
				System.out.println(que);
				
			}else if(Table.equalsIgnoreCase("renewal")||Table.equalsIgnoreCase("")){
				que = que.substring(0, que.length()-1) +" from renewal where msisdn=" + MSISDN + " order by last_action_date desc limit 1";
				System.out.println(que); 
			}
			
		

		conn = DriverManager.getConnection(dbURL, parameters);
		System.out.println("Opened database successfully");
		st = conn.createStatement();
		ResultSet rs = st.executeQuery(que);

		
		
				while (rs.next()) {
					for (Iterator = 1; Iterator < rs1.getFieldNames().size(); Iterator++) {
						String dbvalue = rs.getObject(Iterator).toString();
						String param = rs1.getField(rs1.getFieldNames().get(Iterator));
						System.out.println(rs.getObject(Iterator) + "==" + param);

						if (dbvalue.contains(param)) {
							System.out.println("data matched :" + rs1.getFieldNames().get(Iterator).toString());
							data = "pass";

						} else {
							System.out.println(
									"Data Failed to match at :" + rs1.getFieldNames().get(Iterator).toString());
							data = "Fail";
						}

						table_data = table_data + "<tr><td style= 'min-width: 162px'>"
								+ (rs1.getField(rs1.getFieldNames().get(Iterator)) + "\t" + "</td>"
										+ "<td style= 'min-width: 162px'>" + rs.getObject(Iterator).toString() + "\t"
										+ "</td>" + "<td style= 'min-width: 162px'>"

										+ data)
								+ "</td></tr style= 'min-width: 162px'>";

					}

				}
			}
		} catch (Exception e) {

			System.out.println(e);
			System.out.println("at field :" + rs1.getFieldNames().get(Iterator).toString());
		}

		System.out.println("Query Executed");

		st.close();
		conn.close();
		rs1.close();
		//System.out.println(table_data);
		return table_data;

	}

	public static void createtimestampfold() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar cal = Calendar.getInstance();

		try {

			resfold = new File(Result_FLD + "/" + dateFormat.format(cal.getTime()) + "/");
			if ((!resfold.exists()))
				resfold.mkdir();

			timefold = ExecutionStarttime.replace(":", "-").replace(" ", "_");
			File tresfold = new File(resfold + "/" + timefold + "/");
			if ((!tresfold.exists()))
				tresfold.mkdir();
			trfold = tresfold.toString();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private static Logger Log = Logger.getLogger(asnconverter.class.getName());//

	// This is to print log for the beginning of the test case, as we usually run so
	// many test cases as a test suite

	public static void startTestCase(String sTestCaseName) {

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

		Log.info("$$$$$$$$$$$$$$$$$$$$$ " + sTestCaseName + " $$$$$$$$$$$$$$$$$$$$$$$$$");

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

	}

	// This is to print log for the ending of the test case

	public static void endTestCase(String sTestCaseName) {

		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + sTestCaseName + "-E---N---D-"
				+ "             XXXXXXXXXXXXXXXXXXXXXX");

	}

	// Need to create these methods, so that they can be called

	public static void info(String message) {

		Log.info(message);

	}

	public void warn(String message) {

		Log.warn(message);

	}

	public static void error(String message) {

		Log.error(message);

	}

	public void fatal(String message) {

		Log.fatal(message);

	}

	public void debug(String message) {

		Log.debug(message);

	}

}
