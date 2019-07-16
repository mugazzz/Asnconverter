package duAutomationPOC1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DBcis {
	
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Report";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	
	///////////////////////////////////////////
	public static String Curr_user_directory_path=System.getProperty("user.dir");;
	private static Session session;
	private static ChannelShell channel;
	private static String CIS_Unix_username = "VenuReddyGaddam";
	private static String CIS_Unix_password = "VenuReddyGaddam";
	private static String CIS_unix_hostname = "";
	public static String global_Final_CDR_path = "";
	public static String Environment = "";
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public static String MSISDN = "971520001714";
	public static String Input = "CIS";
	public static String Test_Scenario = "OPT-in";
	public static String cdrfiles = System.getProperty("user.dir") + "\\CDR";
	public static String date="" ;
	public static String dateccn="";
	public static String datecis;
	public static String now="";
	
	public  static String Cis_Filepath =cdrfiles+"\\CIS\\meydvvmcis03_EDR_CISOnline1.csv";
	public static String Cis_viewpath= "";
	public static String nodetag;
	public static String idtag;
	public static String cisnode="";
	//public  static String MSISDN ="971520001714";

	public static void main(String[] args) throws InterruptedException {
		
		
		// ---------------------------------------------------------------------------------------
		// ************** CIS Unix Interactions
		
			
			
			System.out.println("Waiting for CIS System to Connect");
			//Curr_user_directory_path = System.getProperty("user.dir");
			CIS_unix_hostname = "10.95.214.72";
			CIS_Unix_username = "VenuReddyGaddam";
			CIS_Unix_password = "VenuReddyGaddam";
			
			// String date= Present_date();
			List<String> CIS_commands = new ArrayList<String>();
			CIS_commands.add("psql -U testteam1 -d cis");
			CIS_commands.add("\\c scs");
			CIS_commands.add("select msisdn, product_id, status,start_date, expiry_date,product_cost,srcchannel,network_status from rs_adhoc_products where msisdn=971520001714 order by last_action_date desc;");
			CIS_commands.add("");
			executeCommands(CIS_commands, CIS_unix_hostname, CIS_Unix_username, CIS_Unix_password);
			channel.disconnect();
			session.disconnect();
			System.out.println("Disconnected channel and session");
			
			close();
		

		
				}

			

		
		
		
		
	
	
	//*****Function to connect to any Unix-box
		private static Session connect(String hostname, String username, String password){

		    JSch jSch = new JSch();

		    try {

		        session = jSch.getSession(username, hostname, 22);
		        java.util.Properties config = new java.util.Properties(); 
		        config.put("StrictHostKeyChecking", "no");
		        session.setConfig(config);
		        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		        session.setPassword(password);

		        System.out.println("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
		        //write_in_exiting_file_without_loosing_old_data("Connecting SSH to " + hostname + " - Please wait for few seconds... ",curr_log_file_path);
		        session.connect();
		        System.out.println("Successfully Connected to the Host " + hostname + "  !!!");
		       // write_in_exiting_file_without_loosing_old_data("Successfully Connected to the Host " + hostname + "  !!!",curr_log_file_path);
		    }catch(Exception e){
		        System.out.println("An error occurred while connecting to "+hostname+": "+e);
		        //write_in_exiting_file_without_loosing_old_data("An error occurred while connecting to "+hostname+": "+e,curr_log_file_path);
		    }

		    return session;

		}


		//*****Function to get session 
		private static Session getSession(String hostname, String username,String password){
		  if(session == null || !session.isConnected()){
		      session = connect(hostname,username,password);
		  }
		  return session;
		}


		//**Function to get Channel 
		private static Channel getChannel(String hostname, String username, String password, String Channel_type){
		    if(channel == null || !channel.isConnected()){
		        try{
		            channel = (ChannelShell)getSession(hostname,username,password).openChannel(Channel_type);
		            channel.connect();

		        }catch(Exception e){
		            System.out.println("Error while opening channel: "+ e);
		          //  write_in_exiting_file_without_loosing_old_data("Error while opening channel: "+ e,curr_log_file_path);
		        }
		    }
		    return channel;
		}


		//*****Function to Execute Commands in the Unix-box


		private static void executeCommands(List<String> commands , String hostname, String username, String password ){

		    try{
		        Channel channel=getChannel(hostname,username,password,"shell");

		        System.out.println("Sending commands...");
		       // write_in_exiting_file_without_loosing_old_data("Sending commands...",curr_log_file_path);
		        sendCommands(channel, commands);

		       readChannelOutput(channel);
		        System.out.println("Finished sending commands!");
		       // write_in_exiting_file_without_loosing_old_data("Finished sending commands!",curr_log_file_path);

		    }catch(Exception e){
		        System.out.println("An error ocurred during executeCommands: "+e);
		      //  write_in_exiting_file_without_loosing_old_data("An error ocurred during executeCommands: "+e,curr_log_file_path);
		    }
		}


		//**** Capture the Send-commands and fire 
		private static void sendCommands(Channel channel, List<String> commands){

		    try{
		        PrintStream out = new PrintStream(channel.getOutputStream());

		        out.println("#!/bin/bash");
		        for(String command : commands){
		        	//if (command.toLowerCase().contains("find /home/sssit/Out -name".toLowerCase())) {
		        		//System.out.println("Waiting for the Final Path of the Invoice PDF ...");
						//Thread.sleep(180000);
					//}
		            out.println(command);
		        }
		        out.println("exit");

		        out.flush();
		    }catch(Exception e){
		        System.out.println("Error while sending commands: "+ e);
		       // write_in_exiting_file_without_loosing_old_data("Error while sending commands: "+ e,curr_log_file_path);
		    }

		}


		//***** Function to Read the Channel Out-put and capture the same
		private static void readChannelOutput(Channel channel){

		    byte[] buffer = new byte[1024];

		    try{ 
		        InputStream in = channel.getInputStream();
		        String line = "";
		       
		        while (true){
		            while (in.available() > 0) {
		                int i = in.read(buffer, 0, 1024);
		                if (i < 0) {
		                    break;
		                }
		                line = new String(buffer, 0, i);
		                System.out.println(line);
		                write_in_exiting_file_without_loosing_old_data(line,curr_log_file_path);
		                            
		            }
		            
		            if(line.contains("logout")){
		                break;
		            }

		            if (channel.isClosed()){ 
		                break;
		            }
		            try {
		                Thread.sleep(1000);
		            } catch (Exception ee){}
		        }
		    }catch(Exception e){
		        System.out.println("Error while reading channel output: "+ e);
		        write_in_exiting_file_without_loosing_old_data("Error while reading channel output: "+ e,curr_log_file_path);
		    }

		}


		//***** Function to close the Channel and Session
			public static void close(){

				channel.disconnect();
				session.disconnect();
				System.out.println("Disconnected channel and session");
				//write_in_exiting_file_without_loosing_old_data("Disconnected channel and session",curr_log_file_path);
			}

			public static void write_in_exiting_file_without_loosing_old_data(String content_to_write, String curr_log_file_path ) {
				
				try {

			        File file = new File(curr_log_file_path);

			        // if file doesnt exists, then create it
			        if (!file.exists()) {
			            file.createNewFile();
			        }

			        FileWriter fw = new FileWriter(file,true);
			        BufferedWriter bw = new BufferedWriter(fw);
			        bw.write(content_to_write);
			        bw.newLine();
			        bw.close();

			        System.out.println("Done");
			        //"/users/mkyong/filename.txt"

			    } catch (IOException e) {
			        e.printStackTrace();
			    }		
			}	
			

}
