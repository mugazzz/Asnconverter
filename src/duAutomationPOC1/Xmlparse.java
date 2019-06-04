package duAutomationPOC1;

import java.io.File;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Xmlparse {
 public static String file="D:\\DU Automation\\ASNConverter\\Report\\04-Jun-2019\\04-Jun-2019_12-39-06\\AIR\\AIROUTPUTCDR_4011_3572_20190414-225920.AIR\\output.xml";
	public static String param1="accountNumber";
	public static String msisdn="971520000174";
	public static String filetype="AIR";
	public static String nodetag;
	public static String idtag;
	
	
	public static void main(String argv[]) {
	
		System.out.println(parsedata(filetype,file,param1,msisdn));

	}
		
		
		public static String parsedata(String filetype,String filepath ,String param1,String MSISDN) {
			String value="" ;
		try {
			
			if(filetype.equalsIgnoreCase("OCC")||filetype.equalsIgnoreCase("CCN")) {
				nodetag="onlineCreditControlRecord";
				idtag="subscriptionIDValue";
			}else {
				nodetag="refillRecordV2";
				idtag="subscriberNumber";
			}

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document docc = docBuilder.parse(new File(filepath));
			docc.getDocumentElement().normalize();
			
			NodeList data = docc.getElementsByTagName(nodetag);
		
			int totaldata = data.getLength();
			
			for (int temp = 0; temp < totaldata; temp++) {
	            Node nNode = data.item(temp);
			
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                Element eElement = (Element) nNode;
	                String[] parmsplt = param1.split("\\[");
	        		String param = parmsplt[0];
	        		int inde = 0;
	        		if (parmsplt.length == 1) {
	        			inde = 0;
	        		} else {
	        			String[] paramsplit2 = parmsplt[1].split("\\]");
	        			inde = Integer.parseInt(paramsplit2[0]);
	        		}
	        		

	                String sub=eElement.getElementsByTagName(idtag).item(0).getTextContent();
	                if (sub.contentEquals(MSISDN)){
	                value=(eElement.getElementsByTagName(param).item(inde).getTextContent());
	               
	                }
	                
	                 
	                
	            }
	            
			}

			

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		return value;

	}

}