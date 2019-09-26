package duAutomationPOC1;

import java.io.File;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Xmlparse {
 public static String file="C:\\Users\\venureddyg\\Desktop\\parallel_ussd-Regression_CIS_DB-API\\Result\\19-Sep-2019\\19-Sep-2019_16-39-16\\TC-1092__API_GetOffers\\Response\\response.xml";
	public static String param1="subscriptionIDValue[0]";
	public static String msisdn="971520002069";
	public static String filetype="";
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
			}else if(filetype.equalsIgnoreCase("AIR")) {
				nodetag="refillRecordV2";
				idtag="subscriberNumber";
			}else {
				nodetag="periodicAccountMgmt";
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
	                
	              System.out.println(param1+" == "+value);   
	                
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