package duAutomationPOC1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CISDB {
	public static String table = "adhoc";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		String validate_adhoc = "select msisdn, product_id, status,start_date, expiry_date,product_cost,srcchannel,network_status from rs_adhoc_products where msisdn=971520001714 order by last_action_date desc";

		String validate_renewal = "select msisdn,last_renewal_date,renewal_date,status,activation_date,product_id,product_description,product_type,srcchannel,product_category,product_purchase_type,language_id,network_status from renewal where msisdn=971520001714 order by last_action_date desc";
		if (table.equalsIgnoreCase("adhoc")) {
			System.out.println(ValidationQuery(validate_adhoc, table));

		} else if (table.equalsIgnoreCase("renewal")) {
			System.out.println(ValidationQuery(validate_renewal, table));

		} else {
			System.out.println(ValidationQuery(validate_adhoc, "adhoc"));
			System.out.println(ValidationQuery(validate_renewal, "renewal"));

		}

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
			
			table_data=displayActorAdhoc(rs);

		} else if (Table.equalsIgnoreCase("renewal")) {
			
			table_data=displayActorRenew(rs);

		} else {

		}

		st.close();
		conn.close();
		return table_data;

	}

	public static String displayActorAdhoc(ResultSet rs) throws SQLException {
		String tbl = "<table>" + "<tr>" + "<th>msisdn</th>" + "<th>product_id</th>" + "<th>status</th>"
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
		String tbl = "<table>" + "<tr>" + "<th>msisdn</th>" + "<th>last_renewal_date</th>" + "<th>renewal_date</th>"
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

}
