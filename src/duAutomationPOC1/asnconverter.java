package duAutomationPOC1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

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
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;



public class asnconverter {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Report";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	
	///////////////////////////////////////////
	public static String Curr_user_directory_path;
	private static Session session;
	private static ChannelShell channel;
	private static ChannelSftp channel_sftp;
	private static String SDP_Unix_username = "";
	private static String SDP_Unix_password = "";
	private static String SDP_unix_hostname = "";
	public static String global_Final_CDR_path = "";
	public static String Environment = "";
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String MSISDN ="971520001714"; 
	public static String sdp="SDP";
	///////////////////////////////////////////////

	public static void main(String args[]) {
		try {
			Calendar cal1 = Calendar.getInstance();
			long currdate = cal1.getTimeInMillis() / 1000;
			long unixtstamp = 1549238400;
			// System.out.println(unixtstamp);
			// System.out.println(currdate);
			// if(currdate < unixtstamp)
			{
				
				// ==To delete existing files in OCC-data folder===========
				File index = new File("D:\\DU Automation\\ASNConverter\\CDR\\SDP");
				String[] entries = index.list();
				for (String s : entries)
				{
					File currentFile = new File(index.getPath(), s);
					currentFile.delete();
					System.out.println("Existing file deleted");
				}
			////////////////////////////////////////////////////////////////////////
				
				if(sdp.contains("SDP") || sdp.contains("ALL")) {

					// ---------------------------------------------------------------------------------------
					// ************** SDP Unix Interactions
					Curr_user_directory_path = System.getProperty("user.dir");
					SDP_unix_hostname = "10.95.214.6";
					SDP_Unix_username = "tasuser";
					SDP_Unix_password = "Ericssondu@123";
					String date= Present_date();
					List<String> SDP_commands = new ArrayList<String>();
					SDP_commands.add("cd /var/opt/fds/CDR/archive/");
					SDP_commands.add("grep -l "+MSISDN+" *"+date+"* |tac |head -1 > /home/tasuser/sdpfile.txt");
					executeCommands(SDP_commands, SDP_unix_hostname, SDP_Unix_username, SDP_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(SDP_Unix_username, SDP_unix_hostname, 22);
						session.setPassword(SDP_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser");

						File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("sdpfile.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + entry.getFilename());
								Thread.sleep(5000);
							}
						}
						// Code to get CDR file latest name
						String cdrfile = filename(Curr_user_directory_path + "\\sdpfile.txt");
						System.out.println(cdrfile);
						
						//Code to get file to local system
						channel_sftp.cd("/var/opt/fds/CDR/archive/");
						//channel_sftp.pwd();
						File localFile1 = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.ASN");
						Thread.sleep(5000);
						
						for (ChannelSftp.LsEntry entry1 : list1) {
								if (entry1.getFilename().contains(cdrfile)) {
								channel_sftp.get(cdrfile, localFile1 + "\\" + "CDR" + "\\"+ "SDP"+ "\\");
								Thread.sleep(10000);
								System.out.println("SDP file transfered to "+localFile1 + "\\" + "CDR" + "\\"+ "SDP"+ "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
					
					System.out.println("Waiting to connect to other system");
					Thread.sleep(5000);

				}else {
					System.out.println("Choose the right system name");
				}
				/////////////////////////////////////////////////////////////////////////////

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
					File dir = new File(System.getProperty("user.dir") + "\\CDR\\" + filetype);
					File[] directoryListing = dir.listFiles();
					if (directoryListing != null) {
						for (File child : directoryListing) {
							filename = child.getAbsoluteFile().getName();

							startTestCase("Parsing File " + filename);
							String schemaname = "";
							File TCFold = new File(trfold + "/" + filetype);
							if ((!TCFold.exists()))
								TCFold.mkdir();
							File TCFold1 = new File(trfold + "/" + filetype + "/" + filename);
							if ((!TCFold1.exists()))
								TCFold1.mkdir();
							File file = new File(trfold + "/" + filetype + "/" + filename + "/output.xml");
							// System.out.println(trfold+"/"+ refid + "/output.xml");
							if (file.exists()) {
								file.delete();
							}
							file.createNewFile();
							Runtime rt = Runtime.getRuntime();
							// System.out.println(System.getProperty("user.dir"));
							if (filetype.equalsIgnoreCase("AIR")) {
								schemaname = "AIROUTPUTCDR411A.asn1 -pdu DetailOutputRecord";
							} else if (filetype.equalsIgnoreCase("SDP")) {
								schemaname = "SDPOUTPUTCDRCS416A.asn1";

							} else if (filetype.equalsIgnoreCase("OCC-data")) {
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
							} else {
								schemaname = "ccn55a.asn1 -pdu DetailOutputRecord";
							}
							String commands = "asn2xml CDR/" + filetype + "/" + filename + " -schema Schema/"
									+ schemaname;
							Process proc = rt.exec(commands);
							BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
							BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
							// read the output from the command
							// System.out.println("Here is the standard output of the command:\n");
							String s = null;
							// File file = new File("test.xml");
							String start = "no";
							String replaceq = "no";
							String replaceo = "no";
							while ((s = stdInput.readLine()) != null) {
								// System.out.println(s);
								if (s.equals("<?xml version=\"1.0\"?>")) {
									start = "yes";
								}
								if (start.equals("yes")) {
									if (!s.equals("Decoded Successfully")) {
										if (s.contains("<selectionTreeQualifiers>")) {
											replaceq = "yes";
										} else if (s.contains("</selectionTreeQualifiers>")) {
											replaceq = "no";
										}
										if (s.contains("<usedOffers>")) {
											replaceo = "yes";
										} else if (s.contains("</usedOffers>")) {
											replaceo = "no";
										}
										if (replaceq.equals("yes")) {
											s = s.replace("<>", "<Qualifier>");
											s = s.replace("</>", "</Qualifier>");
										}
										if (replaceo.equals("yes")) {
											s = s.replace("<>", "<OfferID>");
											s = s.replace("</>", "</OfferID>");
										}

										FileUtils.writeStringToFile(file, s + "\n", true);
									}
								}
							}

							// read any errors from the attempted command
							System.out.println("Here is the standard error of the command (if any):\n");
							while ((s = stdError.readLine()) != null) {
								System.out.println(s);
							}
							String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
							String filenameq = "";
							for (int Iterator = 1; Iterator < rs.getFieldNames().size(); Iterator++) {
								if (rs.getFieldNames().get(Iterator).toString().contains("Parameter")) {
									if (rs.getField(rs.getFieldNames().get(Iterator)) != "")
										if (rs.getFieldNames().get(Iterator + 1).toString().contains("value")) {
											if (rs.getField(rs.getFieldNames().get(Iterator + 1)).contains("YES")) {

												String param1 = rs.getField(rs.getFieldNames().get(Iterator).toString())
														.toString();
												String retval = parsexml(param1, file);
												String[] parmi = rs.getFieldNames().get(Iterator).toString()
														.split("Parameter");
												String findx = parmi[1];
												if (param1.equals("subscriptionIDValue[0]")
														|| param1.equals("accountNumber")) {
													filenameq = retval;
												}
												tbl = tbl + "<tr><td>" + param1 + "</td><td>" + retval + "</td></tr>";
												// conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"'
												// where Refrence_ID ='"+refid+"'");
											}
										}

								}

							}
							ExtentTest test = extent.createTest(filenameq + "_Output");
							test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + filetype + "/"
									+ filename + "/output.xml'>Click to View the CDR</a></b><br>" + tbl + "</table>");
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

	public static String parsexml(String param1, File file)
			throws ParserConfigurationException, SAXException, IOException {
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
		// System.out.println(param +" = "+ doc.select(param).get(0).text());
		if (doc.select(param).size() != 0) {
			info(param + " = " + doc.select(param).get(inde).text());
			return doc.select(param).get(inde).text();
		} else {
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
	
	///////////////////////////////////////////

	private static Session connect(String hostname, String username, String password) {

		JSch jSch = new JSch();

		try {

			session = jSch.getSession(username, hostname, 22);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.setPassword(password);

			System.out.println("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
			write_in_exiting_file_without_loosing_old_data("Connecting SSH to " +
			hostname + " - Please wait for few seconds... ",curr_log_file_path);
			session.connect();
			System.out.println("Successfully Connected to the Host " + hostname + "  !!!");
			write_in_exiting_file_without_loosing_old_data("Successfully Connected to the Host " + hostname + " !!!",curr_log_file_path);
		} catch (Exception e) {
			System.out.println("An error occurred while connecting to " + hostname + ": " + e);
			write_in_exiting_file_without_loosing_old_data("An error occurred while connecting to "+hostname+": "+e,curr_log_file_path);
		}

		return session;

	}

	// *****Function to get session
	private static Session getSession(String hostname, String username, String password) {
		if (session == null || !session.isConnected()) {
			session = connect(hostname, username, password);
		}
		return session;
	}

	// **Function to get Channel
	private static Channel getChannel(String hostname, String username, String password, String Channel_type) {
		if (channel == null || !channel.isConnected()) {
			try {
				channel = (ChannelShell) getSession(hostname, username, password).openChannel(Channel_type);
				channel.connect();

			} catch (Exception e) {
				System.out.println("Error while opening channel: " + e);
				write_in_exiting_file_without_loosing_old_data("Error while opening channel:"+ e,curr_log_file_path);
			}
		}
		return channel;
	}

	// *****Function to Execute Commands in the Unix-box
	private static void executeCommands(List<String> commands, String hostname, String username, String password) {

		try {
			Channel channel = getChannel(hostname, username, password, "shell");
			System.out.println("Sending commands...");

			sendCommands(channel, commands);

			readChannelOutput(channel);
			System.out.println("Finished sending commands!");

		} catch (Exception e) {
			System.out.println("An error ocurred during executeCommands: " + e);

		}
	}

	// **** Capture the Send-commands and fire
	private static void sendCommands(Channel channel, List<String> commands) {

		try {
			PrintStream out = new PrintStream(channel.getOutputStream());

			out.println("#!/bin/bash");
			for (String command : commands) {
					
				out.println(command);
			}
			out.println("exit");

			out.flush();
		} catch (Exception e) {
			System.out.println("Error while sending commands: " + e);
			write_in_exiting_file_without_loosing_old_data("Error while sending commands: " + e, curr_log_file_path);
		}

	}

	// ***** Function to Read the Channel Out-put and capture the same
	private static void readChannelOutput(Channel channel) {

		byte[] buffer = new byte[1024];

		try {
			InputStream in = channel.getInputStream();
			String line = "";
			global_Final_CDR_path = "";
			while (true) {
				while (in.available() > 0) {
					int i = in.read(buffer, 0, 1024);
					if (i < 0) {
						break;
					}
					line = new String(buffer, 0, i);
					System.out.println(line);
					write_in_exiting_file_without_loosing_old_data(line, curr_log_file_path);
					
					global_Final_CDR_path = global_Final_CDR_path + line + "\n";
				}

				if (line.contains("logout")) {
					break;
				}

				if (channel.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
		} catch (Exception e) {
			System.out.println("Error while reading channel output: " + e);
			write_in_exiting_file_without_loosing_old_data("Error while reading channel output: " + e,
					curr_log_file_path);
		}

	}

	// ***** Function to close the Channel and Session
	public static void close() {
		
		channel.disconnect();
		session.disconnect();
		System.out.println("Disconnected channel and session");
		write_in_exiting_file_without_loosing_old_data("Disconnected channel and session", curr_log_file_path);
	}

	// Function to create a text file in the specified directory with the current
	// time-stamp as file name
	public static void create_new_txt_file_in_specified_dir(String curr_log_file_path) {

		try {
			File file = new File(curr_log_file_path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Function to write in the existing text file with out loosing old data
	public static void write_in_exiting_file_without_loosing_old_data(String content_to_write,
			String curr_log_file_path) {

		try {

			File file = new File(curr_log_file_path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content_to_write);
			bw.newLine();
			bw.close();

			// System.out.println("Done");
			// "/users/mkyong/filename.txt"

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Function to generate Random digits based on the current time stamp
	public static String Random_Number_With_Required_Digits(int digits_Req) {

		String Rndm_Number;
		String Master_14_Digit_Number;
		Master_14_Digit_Number = "";
		Rndm_Number = "";

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		Master_14_Digit_Number = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":",
				"");
		System.out.println((Master_14_Digit_Number.substring(Master_14_Digit_Number.length() - digits_Req + 1,
				Master_14_Digit_Number.length())));
		Rndm_Number = (Master_14_Digit_Number.substring(Master_14_Digit_Number.length() - digits_Req,
				Master_14_Digit_Number.length()));
		System.out.println("Final Rndm_Number:-  " + Rndm_Number);
		return Rndm_Number;
	}

	public static String filename(String filepath) throws IOException {
		InputStream is = new FileInputStream(filepath);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line);
			line = buf.readLine();
		}
		String fileAsString = sb.toString().replace(" ", "");

		buf.close();
		return fileAsString;

	}
	public static String Present_date() {

		String datetoday;
		

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		datetoday = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		
		return datetoday;
	}	

	// End


	////////////////////////////////////////////////////////

}
