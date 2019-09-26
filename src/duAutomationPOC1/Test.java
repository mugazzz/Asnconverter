package duAutomationPOC1;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class Test {
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String Curr_user_directory_path = System.getProperty("user.dir");
	static String Data = Curr_user_directory_path + "\\TestData.xlsx";

	public static void main(String[] args) throws FilloException {
		String tb1="Select ";
		String MSISDN="97152001714";
		int Iterator = 0;
		Fillo fillo = new Fillo();
		com.codoid.products.fillo.Connection conn1 = fillo.getConnection(Data);
		
		Recordset rs1 = conn1.executeQuery("Select * from DB_adhoc where \"Execution \" = 'Yes'");
		while (rs1.next()) {
			for (Iterator = 1; Iterator < rs1.getFieldNames().size(); Iterator++) {
			
			String data= rs1.getFieldNames().get(Iterator);
			tb1=tb1+data +",";
			
		}
			tb1=tb1+" from rs_adhoc_products where msisdn="+ MSISDN + " order by last_action_date desc limit 1";
			System.out.println(tb1);
			}
}

}
 