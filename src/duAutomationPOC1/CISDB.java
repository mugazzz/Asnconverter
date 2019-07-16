package duAutomationPOC1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CISDB {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		String validate= "select msisdn, product_id, status,start_date, expiry_date,product_cost,srcchannel,network_status from rs_adhoc_products where msisdn=971520001714 order by last_action_date desc";
		
		ExecuteQuery(validate);
		
		
	}
		public static void ExecuteQuery(String sql) throws SQLException, ClassNotFoundException  {
			   
			  // Connection conn = null;
			   
		       Statement stmt = null;
			
			      String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
			      Properties parameters = new Properties();
			      parameters.put("user", "mugazmaveric");
			      parameters.put("password", "mugazmaveric");
			      Connection conn = DriverManager.getConnection(dbURL, parameters);
			      
			     // conn.setAutoCommit(false);
			   //   System.out.println("Opened database successfully");
			      
			 System.out.println("Opened database successfully");

	        stmt = conn.createStatement();
	        stmt.executeUpdate(sql);
			 stmt.close();
			 conn.close();
		       System.out.println("Table created successfully");  
		}   
		
		public static void ValidationQuery(String Validatin_Query) throws SQLException {
			 String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
		      Properties parameters = new Properties();
		      parameters.put("user", "mugazmaveric");
		      parameters.put("password", "mugazmaveric");
		     
		     
	     Connection conn = DriverManager.getConnection(dbURL, parameters);
	            Statement st = conn.createStatement();
	    		ResultSet rs = st.executeQuery(Validatin_Query);
	    	   		 
	    		 System.out.println("Query Executed");
	    		// display actor information
	            displayActor(rs);

	            st.close();
	   		 conn.close();
	    
	}
		public static String displayActor(ResultSet rs) throws SQLException {
			String tbl = "<table>" + "<tr>" + "<th>msisdn</th>" + "<th>last_renewal_date</th>"
					+ "<th>renewal_date</th>" + "<th>status</th>" + "<th>activation_date</th>"
					+ "<th>product_id</th>" + "<th>product_description</th>" + "<th>product_type</th>"
					+ "<th>srcchannel</th>" + "<th>product_category</th>" + "<th>product_purchase_type</th>" + "<th>language_id</th>"
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
								+ rs.getString("network_status"))+ "</td></tr style= 'min-width: 162px'>";

			}
			return tbl;
		
		
	 
	        
	    }

	}

