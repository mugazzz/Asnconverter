package duAutomationPOC1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.zip.GZIPInputStream;

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
	private static String gzfile;
	private static String berfile;
	
	private static String SDP_Unix_username = "";
	private static String SDP_Unix_password = "";
	private static String SDP_unix_hostname = "";
	private static String CIS_Unix_username = "";
	private static String CIS_Unix_password = "";
	private static String CIS_unix_hostname = "";
	private static String OCC_Unix_username = "";
	private static String OCC_Unix_password = "";
	private static String OCC_unix_hostname = "";
	private static String OCC_Unix_username1 = "";
	private static String OCC_Unix_password1 = "";
	private static String OCC_unix_hostname1 = "";
	private static String AIR_Unix_username = "";
	private static String AIR_Unix_password = "";
	private static String AIR_unix_hostname = "";
	public static String global_Final_CDR_path = "";
	public static String Environment = "";
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String MSISDN = "971520001714";
	public static String Input = "";
	public static String Test_Scenario = "OPT-in";
	public static String cdrfiles = System.getProperty("user.dir") + "\\CDR";
	private static String gzfilepath = cdrfiles+"\\OCCzip\\";
	private static String finalpath = cdrfiles+"\\OCC\\";
	public static String date="" ;
	public static String dateccn="";
	public static String now="";
	///////////////////////////////////////////////

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		try {
			Calendar cal1 = Calendar.getInstance();
			//long currdate = cal1.getTimeInMillis() / 1000;
			//long unixtstamp = 1549238400;
			now= timeoffour();
			dateccn=Present_dateccn();
			// System.out.println(unixtstamp);
			// System.out.println(currdate);
			// if(currdate < unixtstamp)
			{
				

			////////////////////////////////////////////////////////////////////////
				//System.out.println(cdrfiles);
				//file_deletion(cdrfiles);
				Curr_user_directory_path = System.getProperty("user.dir");
				File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
				File localFileb = new File(Curr_user_directory_path + "\\" + "BackupCDR");
				date = Present_date();
				// ---------------------------------------------------------------------------------------
				// ************** CIS Unix Interactions
				if (Input.contains("CIS") || Input.contains("ALL")) {
					System.out.println("Waiting for CIS System to Connect");
					Thread.sleep(20000);
					
					//Curr_user_directory_path = System.getProperty("user.dir");
					CIS_unix_hostname = "10.95.214.72";
					CIS_Unix_username = "VenuReddyGaddam";
					CIS_Unix_password = "VenuReddyGaddam";
					// String date= Present_date();
					List<String> CIS_commands = new ArrayList<String>();
					CIS_commands.add("cd /data/fdp/logs/defaultCircle");
					CIS_commands.add("sudo more meydvvmcis03_EDR_CISOnline1.csv > /home/"+CIS_Unix_username+"/EDRfile.csv");
					executeCommands(CIS_commands, CIS_unix_hostname, CIS_Unix_username, CIS_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(CIS_Unix_username, CIS_unix_hostname, 22);
						session.setPassword(CIS_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/"+CIS_Unix_username+"/");
						System.out.println(channel_sftp.pwd());

					//	File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
						//System.out.println(localFile);
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.csv");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("EDRfile.csv")) {
								System.out.println(entry.getFilename());
								channel_sftp.get("EDRfile.csv", localFile + "\\" + "CIS" + "\\" + entry.getFilename());
								Thread.sleep(5000);
								channel_sftp.get("EDRfile.csv", localFileb + "\\" + "CIS" + "\\" + MSISDN+date+now+entry.getFilename());
								//System.out.println(localFile + "\\" + "CISraw" + "\\" + entry.getFilename());
								System.out.println("EDR file transfered to "+ localFile + "\\" + "CIS" + "\\" );
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
				}
				
				// ---------------------------------------------------------------------------------------
				// ************** SDP Unix Interactions
				if (Input.contains("SDP") || Input.contains("ALL")) {
					System.out.println("waiting for SDP connectivty ");
					Thread.sleep(150000);

				
					Curr_user_directory_path = System.getProperty("user.dir");
					SDP_unix_hostname = "10.95.214.6";
					SDP_Unix_username = "tasuser";
					SDP_Unix_password = "Ericssondu@123";
					//String date = Present_date();
					List<String> SDP_commands = new ArrayList<String>();
					SDP_commands.add("cd /var/opt/fds/CDR/archive/");
					SDP_commands.add("grep -l "+MSISDN+" *20"+date+"* |tac |head -1 > /home/tasuser/sdpfile.txt");
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

					//	File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("sdpfile.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "sdpfile.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get CDR file latest name
						String cdrfile = filename(localFile + "\\" + "sdpfile.txt");
						System.out.println(cdrfile);

						// Code to get file to local system
						channel_sftp.cd("/var/opt/fds/CDR/archive/");
						// channel_sftp.pwd();
					//	File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.ASN");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							if (entry1.getFilename().contains(cdrfile)) {
								channel_sftp.get(cdrfile, localFile + "\\" + "SDP" + "\\");
								Thread.sleep(10000);
								channel_sftp.get(cdrfile, localFileb + "\\" + "SDP" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "SDP" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}

				}
				
				// ---------------------------------------------------------------------------------------
				// ************** OCC Unix Interactions port 22
				if ((Input.contains("OCC") || Input.contains("ALL")) && (Test_Scenario.equalsIgnoreCase("Opt-in") || Test_Scenario.equalsIgnoreCase("Recharge")) ) {
					System.out.println("Waiting for OCC system to connect");
					Thread.sleep(30000);

					
					//Curr_user_directory_path = System.getProperty("user.dir");
					OCC_unix_hostname = "10.95.214.22";
					OCC_Unix_username = "tasuser";
					OCC_Unix_password = "Ericssondu@123";
					//String date = Present_date();
					List<String> OCC_commands = new ArrayList<String>();
					OCC_commands.add("cd /home/tasuser");
					OCC_commands.add(
							"zgrep -l "+MSISDN+" *"+date+"* |tac|head -1 > /home/tasuser/Auto/occfile_22.txt");
					executeCommands(OCC_commands, OCC_unix_hostname, OCC_Unix_username, OCC_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(OCC_Unix_username, OCC_unix_hostname, 22);
						session.setPassword(OCC_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contentEquals("occfile_22.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "occfile_22.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get OCC file latest name
						String occfile = filename(localFile + "\\" + "occfile_22.txt");
						System.out.println(occfile);

						// Code to get file to local system
						channel_sftp.cd("/home/tasuser");
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.gz");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry2 : list1) {
							if (entry2.getFilename().contains(occfile)) {
								channel_sftp.get(occfile, localFile + "\\" + "OCCzip" + "\\");
								Thread.sleep(10000);
								channel_sftp.get(occfile, localFileb + "\\" + "OCCzip" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "OCCzip" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}

					// ---------------------------------------------------------------------------------------
					// ************** OCC Unix Interactions port 21
					//Curr_user_directory_path = System.getProperty("user.dir");
					OCC_unix_hostname1 = "10.95.214.21";
					OCC_Unix_username1 = "tasuser";
					OCC_Unix_password1 = "Ericssondu@123";
					// String date= Present_date();
					List<String> OCC1_commands = new ArrayList<String>();
					OCC1_commands.add("cd /home/tasuser");
					OCC1_commands.add(
							"zgrep -l "+MSISDN+" *"+date+"* |tac|head -1 > /home/tasuser/Auto/occfile_21.txt");
					executeCommands(OCC1_commands, OCC_unix_hostname1, OCC_Unix_username1, OCC_Unix_password1);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(OCC_Unix_username1, OCC_unix_hostname1, 22);
						session.setPassword(OCC_Unix_password1);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("occfile_21.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "occfile_21.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get OCC file latest name
						String occfile1 = filename(localFile + "\\" + "occfile_21.txt");
						System.out.println(occfile1);

						// Code to get file to local system
						channel_sftp.cd("/home/tasuser");
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.gz");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry2 : list1) {
							if (entry2.getFilename().contentEquals(occfile1)) {
								channel_sftp.get(occfile1, localFile + "\\" + "OCCzip" + "\\");
								Thread.sleep(5000);
								channel_sftp.get(occfile1, localFileb + "\\" + "OCCzip" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "OCCzip" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
					// Extract OCC.gz file to parsing ber file
					file_extraction(gzfilepath,finalpath);
					
					
				}
				// Connecting to CCN to get CDR file
				if (Input.contains("CCN") || Input.contains("ALL")) {
					System.out.println("Waiting for CCN system to connect");		
					Thread.sleep(90000);
					// ************** CCN Unix Interactions storage 0
					//Curr_user_directory_path = System.getProperty("user.dir");
					String CCN_unix_hostname = "10.95.213.132";
					String CCN_Unix_username = "tasuser";
					String CCN_Unix_password = "CCNtasuser@123";
					//String now= timeoffour();
					//String date = Present_date();
					System.out.println(now);
					System.out.println(date);
					List<String> CCN_commands = new ArrayList<String>();
					CCN_commands.add("cd /cluster/storage/no-backup/ccn/CcnStorage0/CCNCDR44/archive/");
					CCN_commands.add("grep -l "+MSISDN+" *"+dateccn+now+"* |tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCN_0file.txt");
					executeCommands(CCN_commands, CCN_unix_hostname, CCN_Unix_username, CCN_Unix_password);
					close();
					//grep -l 971520001714 *20190529* |xargs ls -l|grep 13:0[0-9]|tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCNfile.txt
					//grep -l 971520001714 *20190530063*|tac|head -1

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(CCN_Unix_username, CCN_unix_hostname, 22);
						session.setPassword(CCN_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/cluster/home/system-oam/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						System.out.println(list);
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("CCN_0file.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "CCN_0file.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get CCN file latest name
						String CCN0file = filename(localFile + "\\" + "CCN_0file.txt");
						System.out.println(CCN0file);

						// Code to get file to local system
						channel_sftp.cd("/cluster/storage/no-backup/ccn/CcnStorage0/CCNCDR44/archive/");
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "InputCdrFile");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls(".");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							
							if (entry1.getFilename().contains(CCN0file)) {
								channel_sftp.get(CCN0file, localFile + "\\" + "CCN" + "\\");
								
								Thread.sleep(10000);
								channel_sftp.get(CCN0file, localFileb + "\\" + "CCN" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "CCN" + "\\");
							} 
							
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
					//
					
						//System.out.println("Waiting for CCN system to connect");		
						Thread.sleep(30000);
						// ************** CCN Unix Interactions
						//Curr_user_directory_path = System.getProperty("user.dir");
						//String CCN_unix_hostname = "10.95.213.132";
						//String CCN_Unix_username = "tasuser";
						//String CCN_Unix_password = "CCNtasuser@123";
						
						List<String> CCN1_commands = new ArrayList<String>();
						CCN1_commands.add("cd /cluster/storage/no-backup/ccn/CcnStorage1/CCNCDR44/archive/");
						CCN1_commands.add("grep -l "+MSISDN+" *"+dateccn+now+"* |tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCN_1file.txt");
						executeCommands(CCN1_commands, CCN_unix_hostname, CCN_Unix_username, CCN_Unix_password);
						close();
						//grep -l 971520001714 *20190529* |xargs ls -l|grep 13:0[0-9]|tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCNfile.txt

						try {

							JSch jsch = new JSch();
							Session session = jsch.getSession(CCN_Unix_username, CCN_unix_hostname, 22);
							session.setPassword(CCN_Unix_password);
							session.setConfig("StrictHostKeyChecking", "no");
							session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
							session.connect();
							channel_sftp = null;
							channel_sftp = (ChannelSftp) session.openChannel("sftp");
							channel_sftp.connect();
							channel_sftp.cd("/cluster/home/system-oam/tasuser/Auto");

						//	File localFile = new File(Curr_user_directory_path + "\\");
							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
							System.out.println(list);
							for (ChannelSftp.LsEntry entry : list) {
								if (entry.getFilename().contains("CCN_1file.txt")) {
									channel_sftp.get(entry.getFilename(), localFile + "\\" + "CCN_1file.txt");
									Thread.sleep(5000);
								}
							}
							// Code to get AIR file latest name
							String CCN1file = filename(localFile + "\\" + "CCN_1file.txt");
							System.out.println(CCN1file);

							// Code to get file to local system
							channel_sftp.cd("/cluster/storage/no-backup/ccn/CcnStorage1/CCNCDR44/archive/");
							// channel_sftp.pwd();
							//File localFile1 = new File(Curr_user_directory_path + "\\" + "InputCdrFile");
							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls(".");
							Thread.sleep(5000);

							for (ChannelSftp.LsEntry entry1 : list1) {
								
								if (entry1.getFilename().contains(CCN1file)) {
									channel_sftp.get(CCN1file, localFile + "\\" + "CCN" + "\\");
									
									Thread.sleep(10000);
									channel_sftp.get(CCN1file, localFileb + "\\" + "CCN" + "\\");
									System.out.println("SDP file transfered to " + localFile + "\\" + "CCN" + "\\");
								} 
								
							}

							channel_sftp.disconnect();
							session.disconnect();

						} catch (Exception e) {
							e.printStackTrace();

						}
				}
				// ************** AIR Unix Interactions
				//Curr_user_directory_path = System.getProperty("user.dir");
				if (Input.contains("AIR") || Input.contains("ALL")) {
					System.out.println("Waiting for Air system to connect");		
					Thread.sleep(60000);
					
					AIR_unix_hostname = "10.95.214.166";
					AIR_Unix_username = "tasuser";
					AIR_Unix_password = "Ericssondu@123";
					//String date = Present_date();
					List<String> AIR_commands = new ArrayList<String>();
					AIR_commands.add("cd /var/opt/air/datarecords/backup_CDR/");
					AIR_commands.add("grep -l " + MSISDN + " *" + date + "* |tac |head -1 > /home/tasuser/Auto/Airfile.txt");
					executeCommands(AIR_commands, AIR_unix_hostname, AIR_Unix_username, AIR_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(AIR_Unix_username, AIR_unix_hostname, 22);
						session.setPassword(AIR_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("Airfile.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "Airfile.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get AIR file latest name
						String Airfile = filename(localFile + "\\" + "Airfile.txt");
						System.out.println(Airfile);

						// Code to get file to local system
						channel_sftp.cd("/var/opt/air/datarecords/backup_CDR/");
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.AIR");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							
							if (entry1.getFilename().contains(Airfile)) {
								channel_sftp.get(Airfile, localFile + "\\" + "AIR" + "\\");
								
								Thread.sleep(10000);
								channel_sftp.get(Airfile, localFileb + "\\" + "AIR" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "AIR" + "\\");
							} 
							
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
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

							} else if (filetype.equalsIgnoreCase("OCC")) {
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
							} else if (filetype.equalsIgnoreCase("CCN")) {
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
							}
							else {
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
												if (param1.equals("subscriptionIDValue[0]")|| param1.equals("accountNumber"))
													
												{
													filenameq = retval;
												}
												tbl = tbl + "<tr><td>" + param1 + "</td><td>" + retval + "</td></tr>";
												// conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"'
												// where Refrence_ID ='"+refid+"'");
											}
										}

								}

							}
							if(!filetype.contentEquals("CIS")) {
							ExtentTest test = extent.createTest(filetype+"_"+MSISDN + "_Output");
							test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + filetype + "/"
									+ filename + "/output.xml'>Click to View the CDR</a></b><br>" + tbl + "</table>");
							}
							else {
							ExtentTest test1 = extent.createTest("CISFILE_"+MSISDN+"_Output");
							test1.pass("<a href='file:///D:/DU Automation/ASNConverter/CDR/CIS/EDRfile.csv'>Click her for EDR</a>");
							}
							extent.flush();
							endTestCase(refid);
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static String parsexml(String param1, File file)
			throws ParserConfigurationException, SAXException, IOException, IndexOutOfBoundsException {
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
	public static void close() throws InterruptedException {
		
		channel.disconnect();
		session.disconnect();
		Thread.sleep(2000);
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
	
	//Function to read a file for cdr name
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
	//Function to create a date format in linux command
	public static String Present_date() {

		String datetoday;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd-HH:");
		LocalDateTime now = LocalDateTime.now();
		datetoday = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		//String finaldate=datetoday.substring(0, datetoday.length()-1);	
	
		return datetoday;
	}	
	public static String Present_dateccn() {

		String datetodayccn;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		datetodayccn = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		//String finaldateocc=datetoday.substring(0, datetoday.length()-1);	
	
		return datetodayccn;
	}
	public static String timeoffour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -4);
		java.util.Date date = calendar.getTime();
		//System.out.println(date);
		String date1=date.toString();
		//System.out.println(date1);
		String finaldate=date1.substring(11,date1.length()-13).replace(":", "");
		//System.out.println(finaldate);
		
		
		return finaldate;
		
	}
	
	//Function to extract files 
	public static void file_extraction(String gzfilepath, String finalpath) {
		File folder = new File(gzfilepath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				//System.out.println("File " + listOfFiles[i].getName());

				gzfile = gzfilepath + listOfFiles[i].getName();
				

				//System.out.println("Zip file of OCC is " + gzfile);

				String bername = listOfFiles[i].getName();
				String bername1 = (bername.substring(0, bername.length() - 3));
				berfile = finalpath + bername1;

				//System.out.println("Extracted ber file is " + berfile);

				FileTransfer gZip = new FileTransfer();
				gZip.gunzipIt(gzfile, berfile);
				
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());

			}

		}

	}

	//Function to Unzip files
	public void gunzipIt(String INPUT_GZIP_FILE1, String OUTPUT_FILE2) {

		byte[] buffer = new byte[1024];
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE1));

			FileOutputStream out = new FileOutputStream(OUTPUT_FILE2);
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			gzis.close();
			out.close();
			System.out.println("Extraction of OCC zip file completed");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//Function to delete files
	public static void file_deletion(String gzfilepath) {
		File folder = new File(gzfilepath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				//System.out.println("File " + listOfFiles[i].getName());
				

		 
			} else if (listOfFiles[i].isDirectory()) {
				//System.out.println("Directory " + listOfFiles[i].getName());
				String pathofDir= listOfFiles[i].getPath();
				//System.out.println(pathofDir);
				 File folderin =new File(pathofDir);
				 File[] list = folderin.listFiles();
				 for (int j = 0; j < list.length; j++) {

						if (list[j].isFile()) {
							//System.out.println("File " + list[j].getName());
							
						
								File currentFile = new File(list[j].getPath());
								currentFile.delete();
								//System.out.println("Existing file deleted");
							

					 
						} else if (listOfFiles[j].isDirectory()) {
							//System.out.println("Directory " + listOfFiles[j].getName());

			}else {
				//System.out.println("No Files or Folder's Found");
				
			}

				 }
			}
		}
	}

	// End


	////////////////////////////////////////////////////////

}
