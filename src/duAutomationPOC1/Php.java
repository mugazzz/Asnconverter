package duAutomationPOC1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Php {
	
	public static void main(String args[]) {
		try {
		String dbURL = "jdbc:mysql://localhost:3306/mav_auto";
		String username = "root";
		String password = "";

		Connection dbCon = null;
		Statement stmt0 = null;
		Statement stmt01 = null;
		//Statement stmupt = null;
		ResultSet inputs = null;
		ResultSet inputs0 = null;
		//ResultSet update = null;
		
		// -----------Get input data------------//
		String usercase = "SELECT * FROM `mav_tc_execute` WHERE created_by='venureddy' AND NOT execution_status='Completed'";
		dbCon = DriverManager.getConnection(dbURL, username, password);
		stmt01 = dbCon.prepareStatement(usercase);
		inputs0 = stmt01.executeQuery(usercase);
		while (inputs0.next()) {
			String row_id=inputs0.getString("test_case_id");
			//String updateq="UPDATE mav_tc_execute SET execution_status='Completed' WHERE test_case_id='"+row_id+"'";
			System.out.println("id  "+row_id);
			String inputQuery = "SELECT * FROM `mav_test_case` WHERE created_by='venureddy' AND row_id='"+row_id+"'";
			System.out.println(inputQuery);
			
			stmt0 = dbCon.prepareStatement(inputQuery);
			inputs = stmt0.executeQuery(inputQuery);
			
			
			

			while (inputs.next()) {
				
				String device = inputs.getString("Test_Device");
				System.out.println(device);
//				String MSISDN = inputs.getString("MSISDN");
//				String Test_Scenario = inputs.getString("Test_Scenario");
//				String Test_Case = inputs.getString("Test_Case");
//				String Test_Case_ID = inputs.getString("Test_Case_ID");
	
				//stmupt = dbCon.prepareStatement(updateq);
				//stmupt.executeUpdate(updateq);          
			}
			stmt0.close();
			
		}
		stmt01.close();
		dbCon.close();
	
           
        } catch (SQLException ex) {
        	ex.printStackTrace();
          
        } finally{
           //close connection ,stmt0 and resultset here
        }
       
    }  
	
}