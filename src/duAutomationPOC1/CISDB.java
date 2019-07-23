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

public class CISDB {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Report";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	public static String Curr_user_directory_path;
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String MSISDN = "971520001714";


	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Curr_user_directory_path = System.getProperty("user.dir");
		createtimestampfold();
		System.setProperty("logfilename", trfold + "\\Logs");
		DOMConfigurator.configure("log4j.xml");
		ExtentReports extent = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
		extent.attachReporter(htmlReporter);
		info("Starting execution at +:" + ExecutionStarttime);
		startTestCase("Connecting CIS_DB ");
		String table = "";
		String tbl =null;

		tbl=cis_db(table);
		
		ExtentTest test = extent.createTest("CIS_DB_" + MSISDN + "_Output");
		
		
		test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank'></a></b><br>" + tbl
				+ "</table>");
		extent.flush();
		endTestCase("Disconnected From CIS_DB");
	}

	public static String cis_db(String table) {
		String table_data ="";
		String table_data1 ="";
		try {
			String validate_adhoc = "select msisdn, product_id, status,start_date, expiry_date,product_cost,srcchannel,network_status from rs_adhoc_products where msisdn="+MSISDN+" order by last_action_date desc limit 2";

			String validate_renewal = "select msisdn,last_renewal_date,renewal_date,status,activation_date,product_id,product_description,product_type,srcchannel,product_category,product_purchase_type,language_id,network_status from renewal where msisdn="+MSISDN+" order by last_action_date desc limit 2";
			if (table.equalsIgnoreCase("adhoc")) {
				table_data=ValidationQuery(validate_adhoc, table);
			} else if (table.equalsIgnoreCase("renewal")) {
				table_data=ValidationQuery(validate_renewal, table);

			}else {
				table_data=ValidationQuery(validate_adhoc, "adhoc");
				table_data1=ValidationQuery(validate_renewal, "renewal");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return table_data + table_data1;
	}

	public static String ValidationQuery(String Validatin_Query, String Table) throws SQLException {
		String table_data = null;
		String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
		Properties parameters = new Properties();
		parameters.put("user", "mugazmaveric1");
		parameters.put("password", "maverick");

		Connection conn = DriverManager.getConnection(dbURL, parameters);
		System.out.println("Opened database successfully");
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(Validatin_Query);

		System.out.println("Query Executed");
		// display actor information
		if (Table.equalsIgnoreCase("adhoc")) {

			table_data = displayActorAdhoc(rs);

		} else if (Table.equalsIgnoreCase("renewal")) {

			table_data = displayActorRenew(rs);

		} else {

		}

		st.close();
		conn.close();
		return table_data;

	}

	public static String displayActorAdhoc(ResultSet rs) throws SQLException {
		String tbl ="<style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td ,caption{padding:5px;text-align:left;}</style>"
	+"<table>" + "<caption>Adhoc Table</caption>" + "<tr>" + "<th>msisdn</th>" + "<th>product_id</th>" + "<th>status</th>"
				+ "<th>start_date</th>" + "<th>expiry_date</th>" + "<th>product_cost</th>" + "<th>srcchannel</th>"
				+ "<th>network_status</th>" + "</tr>";
		while (rs.next()) {
			tbl = tbl + "<tr><td style= 'min-width: 162px'>"
					+ (rs.getString("msisdn") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("status") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("start_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("expiry_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_cost") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("srcchannel") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("network_status"))
					+ "</td></tr style= 'min-width: 162px'>";

		}
		// System.out.println(tbl);
		return tbl;

	}

	public static String displayActorRenew(ResultSet rs) throws SQLException {
		String tbl = "<style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td ,caption{padding:5px;text-align:left;}</style>"+
	"<table>" +"<caption>Renewal Table</caption>" +  "<tr>" + "<th>msisdn</th>" + "<th>last_renewal_date</th>" + "<th>renewal_date</th>"
				+ "<th>status</th>" + "<th>activation_date</th>" + "<th>product_id</th>"
				+ "<th>product_description</th>" + "<th>product_type</th>" + "<th>srcchannel</th>"
				+ "<th>product_category</th>" + "<th>product_purchase_type</th>" + "<th>language_id</th>"
				+ "<th>network_status</th>" + "</tr>";
		while (rs.next()) {
			tbl = tbl + "<tr><td style= 'min-width: 162px'>"
					+ (rs.getString("msisdn") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("last_renewal_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("renewal_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("status") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("activation_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_description") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_type") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("srcchannel") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_category") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_purchase_type") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("language_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("network_status"))
					+ "</td></tr style= 'min-width: 162px'>";

		}
		// System.out.println(tbl);
		return tbl;

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

		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " +sTestCaseName+ "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");

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
