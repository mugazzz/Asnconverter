package duAutomationPOC1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.xml.sax.SAXException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;


public class asnconverter {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Report";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	public static void main(String args[]) {
		try {
			Calendar cal1 = Calendar.getInstance();
			long currdate = cal1.getTimeInMillis()/1000;
			long unixtstamp = 1549238400;
			//System.out.println(unixtstamp);
			//System.out.println(currdate);
			//if(currdate < unixtstamp)
			{
			
				createtimestampfold();
				System.setProperty("logfilename", trfold + "\\Logs");
				DOMConfigurator.configure("log4j.xml");
				ExtentReports extent = new ExtentReports();
				ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
				extent.attachReporter(htmlReporter);
				info("Starting execution at +:" + ExecutionStarttime);
				String Data = "TestData.xlsx";
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Data);
				Recordset rs = conn.executeQuery("Select * from TestData where \"Execution Control\" = 'Yes'");
				while (rs.next()) {
					
					String filename = "";
					String filetype = rs.getField("Type");
					String refid = rs.getField("Refrence_ID");
					File dir = new File(System.getProperty("user.dir") + "\\CDR\\"+filetype);
					File[] directoryListing = dir.listFiles();
					if (directoryListing != null) {
						for (File child : directoryListing) {
							filename = child.getAbsoluteFile().getName();
							
							startTestCase("Parsing File "+filename);
							String schemaname = "";
							File TCFold = new File(trfold +"/"+ filetype);
							if ((!TCFold.exists()))
								TCFold.mkdir();
							File TCFold1 = new File(trfold +"/"+ filetype+"/"+filename);
							if ((!TCFold1.exists()))
								TCFold1.mkdir();
							File file = new File(trfold+"/"+ filetype + "/"+filename+"/output.xml");
							//System.out.println(trfold+"/"+ refid + "/output.xml");
							if (file.exists()) {
							     file.delete();
							  }
							file.createNewFile();
							Runtime rt = Runtime.getRuntime();
							//System.out.println(System.getProperty("user.dir"));
							if(filetype.equalsIgnoreCase("AIR"))
							{
								schemaname = "AIROUTPUTCDR411A.asn1 -pdu DetailOutputRecord";
							}
							else if(filetype.equalsIgnoreCase("SDP"))
							{
								schemaname = "SDPOUTPUTCDRCS416A.asn1";
								
							}
							else if(filetype.equalsIgnoreCase("OCC-data"))
							{
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
							}
							else
							{
								schemaname = "ccn55a.asn1 -pdu DetailOutputRecord";
							}
							String commands = "asn2xml CDR/"+filetype+"/"+filename+" -schema Schema/"+schemaname;
							Process proc = rt.exec(commands);
							BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
							BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
							// read the output from the command
							//System.out.println("Here is the standard output of the command:\n");
							String s = null;
							//File file = new File("test.xml");
							String start = "no";
							String replaceq = "no";
							String replaceo = "no";
							while ((s = stdInput.readLine()) != null) {
								//System.out.println(s);
								if(s.equals("<?xml version=\"1.0\"?>"))
								{
									start = "yes";
								}
								if(start.equals("yes"))
								{
									if(!s.equals("Decoded Successfully"))
									{
										if(s.contains("<selectionTreeQualifiers>"))
										{
											replaceq = "yes";
										}
										else if(s.contains("</selectionTreeQualifiers>"))
										{
											replaceq = "no";
										}
										if(s.contains("<usedOffers>"))
										{
											replaceo = "yes";
										}
										else if(s.contains("</usedOffers>"))
										{
											replaceo = "no";
										}
										if(replaceq.equals("yes"))
										{
											s= s.replace("<>", "<Qualifier>");
											s= s.replace("</>", "</Qualifier>");
										}
										if(replaceo.equals("yes"))
										{
											s= s.replace("<>", "<OfferID>");
											s= s.replace("</>", "</OfferID>");
										}
										
										FileUtils.writeStringToFile(file, s+"\n", true);
									}
								}	
							}
							
							// read any errors from the attempted command
							System.out.println("Here is the standard error of the command (if any):\n");
							while ((s = stdError.readLine()) != null) {
								System.out.println(s);
							}
							String tbl="<table><tr><th>Parameter</th><th>Value</th></tr>";
							String filenameq = "";
							for (int Iterator = 1; Iterator < rs.getFieldNames().size(); Iterator++) {
								if (rs.getFieldNames().get(Iterator).toString().contains("Parameter")) {
									if(rs.getField(rs.getFieldNames().get(Iterator))!= "")
									{
										String param1 = rs.getField(rs.getFieldNames().get(Iterator).toString()).toString();
										String retval = parsexml(param1,file);
										String[] parmi = rs.getFieldNames().get(Iterator).toString().split("Parameter");
										String findx = parmi[1];
										if(param1.equals("subscriptionIDValue[0]") || param1.equals("accountNumber"))
										{
											filenameq = retval;
										}
										tbl = tbl+"<tr><td>"+param1+"</td><td>"+retval+"</td></tr>";
										//conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"' where Refrence_ID ='"+refid+"'");			
									}	
								}
								
							}
							ExtentTest test = extent.createTest(filenameq+"_Output");
							test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '"+filetype+"/"+filename+"/output.xml'>Click to View the CDR</a></b><br>"+tbl+"</table>");
							extent.flush();
							endTestCase(refid);
					 }
				 }
				
				
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String parsexml(String param1,File file) throws ParserConfigurationException, SAXException, IOException {
		String[] parmsplt = param1.split("\\[");
		String param = parmsplt[0];
		int inde = 0;
		if (parmsplt.length == 1) {
			inde = 0;
		} else {
			String[] paramsplit2 = parmsplt[1].split("\\]");
			inde = Integer.parseInt(paramsplit2[0]);
		}

		File ipfile = file;
		String fstr = "";
		StringBuilder fileContents = new StringBuilder((int) ipfile.length());

		try (Scanner scanner = new Scanner(ipfile)) {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + System.lineSeparator());
			}
			fstr = fileContents.toString();
		}

		Document doc = Jsoup.parse(fstr, "", Parser.xmlParser());
		//System.out.println(param +" = "+ doc.select(param).get(0).text());
		if(doc.select(param).size() !=0)
		{
			info(param +" = "+ doc.select(param).get(inde).text());
			return doc.select(param).get(inde).text();
		}
		else
		{
			return "";
		}
		

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

		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");

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
