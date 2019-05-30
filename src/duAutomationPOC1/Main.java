package duAutomationPOC1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

	
	public static void main(String[] args) {
		try {
			
			Random rand = new Random(); 
			String Filepath ="D:\\DU Automation\\ASNConverter\\CDR\\CIS\\meydvvmcis03_EDR_CISOnline20.csv";
			String MSISDN ="971520001714";
			String ClearData="Delete from public.EDR_CIS_DataSamp;";
			//tableCreation(sql);
			String loadCSV= "COPY public.EDR_CIS_DataSamp  (Transaction_Time  ,Client_Transaction_Id  ,Transaction_Id  ,IP_Address ," + 
					"Event_Type  ,A_Party_Msisdn  ,B_Party_Msisdn  ,input  ,Result_Code  ,Result_Description ," + 
					"Service_Class  ,Requested_Product_ID  ,Product_Name  ,Product_Type  ,Product_Cost  ,Applied_product_cost," + 
					"Product_Validity  ,Access_Channel  ,Access_Code  ,Charge_Indicator  ,Vat_Fee  ,Language_Id  ," + 
					"Iname  ,Circle_Code  ,Pay_Source  ,Send_sms  ,Skip_charging  ,Bill_Cycle_ID  ," + 
					"User_ID  ,Origin_Host  ,Faf_Indicator  ,Faf_MSISDN  ,Offer_ID  ,New_Imei  ,Old_Imei  ," + 
					"Dealer_ID  ,Transfer_Remark  ,DrCr  ,Subscription_Date  ,Expiry_Date  ,Last_Renewal_Date ," + 
					"Grace_Expiry_Date  ,Status  ,Subscription_Mode  ,Network_Status  ,Last_Status  ,Status_Change_time," + 
					"Command_Count  ,Charging_Session_Id  ,Notification_Message  ,Commission_Fee  ,Transfer_Fee ,GL_Code," + 
					"State  ,Subscriber_Type  ,OpParam1  ,OpParam2  ,OpParam3  ,OpParam4  ,OpParam5  ,OpParam6 ," + 
					"OpParam7  ,OpParam8  ,OpParam9  ,OpParam10  ,OpParam11  ,OpParam12  ,TDF_Event_Class  ," + 
					"TDF_Event_Name  ,TDF_Voucher_Type  ,TDF_Periodic_Charge  ,TDF_Usage  ,External_Data1  ,External_Data2  ," + 
					"External_Data3  ,External_Data4  ,Callback,ParentProductSPInfo) FROM '"+Filepath+"' DELIMITER '|' CSV HEADER";
			
			
			//FileOperation();
			ExecuteQuery(ClearData);
			ExecuteQuery(loadCSV);
			int num = rand.nextInt(1000);
			String Validation_Query ="CREATE VIEW public.CIS_EDR_Validation_"+num+" AS Select Product_Name, Event_Type,Access_Channel,Result_Description,Result_Code,Offer_ID,Service_Class,input,Requested_Product_ID," + 
					"Expiry_Date,Subscription_Mode,A_Party_Msisdn, Product_Validity, Vat_Fee, Iname,Network_Status FROM public.edr_cis_datasamp where A_Party_Msisdn='" +MSISDN +"'";
			ExecuteQuery(Validation_Query);
			String getValidationData ="Select Product_Name, Event_Type,Access_Channel,Result_Description,Result_Code,Offer_ID,Service_Class,input,Requested_Product_ID," + 
					"Expiry_Date,Subscription_Mode,A_Party_Msisdn, Product_Validity, Vat_Fee, Iname,Network_Status FROM public.edr_cis_datasamp where A_Party_Msisdn='" +MSISDN +"'";
		 ValidationQuery(getValidationData);
		 
		 // To export the required data to csv file
		 String Export_Data ="COPY (select * from cis_edr_validation) TO 'D:\\Du Automation Project\\EDR_Validation_db2.csv' DELIMITER '|' CSV HEADER" ;
		 ExecuteQuery(Export_Data);
		 
		 
		} catch (Exception e) {
			e.printStackTrace();
			
		}	
	}

	
	private static void FileOperation () throws IOException {
	 // PrintWriter object for output.txt 
    PrintWriter pw;
	try {
		pw = new PrintWriter("D:\\Du Automation Project\\Customer1.csv");
	 
      
    // BufferedReader object for input.txt 
    BufferedReader br1 = new BufferedReader(new FileReader("D:\\Du Automation Project\\Customer.csv")); 
      
    String line1 = br1.readLine(); 
    // loop for each line of input.txt 
    while(line1 != null) 
    { 
        boolean flag = false; 
          
                 
            if(line1.contains("First_Name")) 
            { 
                flag = true; 
                break; 
            } 
              
           // if flag = false 
        // write line of input.txt to output.txt 
        if(flag=true) 
            pw.println(line1); 
          
      //  line1 = br1.read(); 
          
    } 
      
    pw.flush(); 
      
    // closing resources 
    br1.close(); 
    pw.close(); 
      
    System.out.println("File operation performed successfully"); 
 
	}
	catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
}	
    
	private static Connection getCon() throws SQLException, ClassNotFoundException {
		  Connection c = null;
		   Statement stmt = null;
		   String dbURL = "jdbc:postgresql://localhost:5432/dU";
		      Properties parameters = new Properties();
		      parameters.put("user", "postgres");
		      parameters.put("password", "postgres");
		      Connection conn = DriverManager.getConnection(dbURL, parameters);
		      
		      conn.setAutoCommit(false);
   //   System.out.println("Opened database successfully");

		return c;
		
	}
	public static void ExecuteQuery(String sql) throws SQLException, ClassNotFoundException  {
		   
		   Connection c = null;
		   
	       Statement stmt = null;
		
		      String dbURL = "jdbc:postgresql://localhost:5432/dU";
		      Properties parameters = new Properties();
		      parameters.put("user", "postgres");
		      parameters.put("password", "postgres");
		      Connection conn = DriverManager.getConnection(dbURL, parameters);
		      
		      conn.setAutoCommit(false);
		   //   System.out.println("Opened database successfully");
		      
		 System.out.println("Opened database successfully");

        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
		 stmt.close();
		 conn.close();
	       System.out.println("Table created successfully");  
	}   
	
	public static void ValidationQuery(String Validatin_Query) throws SQLException {
		 String dbURL = "jdbc:postgresql://localhost:5432/dU";
	      Properties parameters = new Properties();
	      parameters.put("user", "postgres");
	      parameters.put("password", "postgres");
	     
	     
     Connection conn = DriverManager.getConnection(dbURL, parameters);
            Statement st = conn.createStatement();
    		ResultSet rs = st.executeQuery(Validatin_Query);
    	   		 
    		 System.out.println("Query Executed");
    		// display actor information
            displayActor(rs);

            st.close();
   		 conn.close();
    
}
	private static void displayActor(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("A_Party_Msisdn") + "\t"
                    + rs.getString("Product_Name") + "\t"
                    + rs.getString("Product_Validity"));
 
        }
    }
	
}

