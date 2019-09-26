package duAutomationPOC1;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmldom {
	
	public static String Root = System.getProperty("user.dir");
	public static String key1 = "";
	public static String value1 = "";
	public static String Value = "";
	public static String I4v = "";
	public static String sTringv = "";
	public static String DateTimev = "";
	public static String file="C:\\Users\\venureddyg\\Desktop\\TC_001_response.xml";
	
	 public static void main(String args[]) throws Throwable {
		 System.out.println(WebService(file));
	  }
	public static void WebService() throws Throwable 
	{
		String XMLResponse_Path = "C:\\Users\\venureddyg\\git\\parallel_ussd\\Result\\24-Jun-2019\\24-Jun-2019_16-30-49\\AddDifferentDA\\Response\\response.xml";
		
		System.out.println(XMLResponse_Path);

		key1="name";

		value1="value";	
		String i4 = "i4";
		String  dateTime = "dateTime.iso8601";
		String string3 = "string";
			@SuppressWarnings("rawtypes")
			
		    //Fetch Data from Soap Response
		    DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(new File(XMLResponse_Path));
			doc1.getDocumentElement().normalize();
			
 		    		 //String Output = getvalue(key1,value1,XMLResponse_Path);
 		    		 //String Output = getvalue(doc1, key1,value1);
			NodeList nList = doc1.getElementsByTagName(value1);
			NodeList nList1 = doc1.getElementsByTagName(key1);
			NodeList I4 = doc1.getElementsByTagName(i4);
			NodeList sTring = doc1.getElementsByTagName(string3);
			NodeList DateTime = doc1.getElementsByTagName(dateTime);

	           //  System.out.println("----------------------------");
	            	for(int k=0; k<=nList1.getLength();k++) {
	            		String Name = nList1.item(k).getTextContent();
	            		Value = nList.item(k).getTextContent();
//	            		if(nList.getLength() !=0) {
//	            			Value = nList.item(k).getTextContent();
//	            		}
//	            		if(I4.getLength() !=0){
//	            			I4v = I4.item(k).getTextContent();
//	            		}
////	            		if(sTring.getLength() !=0){
////	            			sTringv = sTring.item(k).getTextContent();
////	            		}
//	            		if(DateTime.getLength() !=0){
//	            			DateTimev = DateTime.item(k).getTextContent();
//	            		}
	            	//if (Name.equals("dedicatedAccountActiveValue1")) {
            	System.out.println("Output Value = "+":Name ="+Name+" :Value ="+Value);
	         //   }
	            	}
		}
	public static String WebService(String XMLResponse_Path) throws Exception {

		String Nodetag = "member";
		String sub = null;
		String value = null;
		String nametag = "name";
		String valuetag = "value";
		String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
		String sot = null;
		String values;
		try {

			DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(new File(XMLResponse_Path));
			doc1.getDocumentElement().normalize();

			NodeList data = doc1.getElementsByTagName(Nodetag);

			int totaldata = data.getLength();

			for (int temp = 0; temp < totaldata; temp++) {

				Node nNode = data.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					sub = eElement.getElementsByTagName(nametag).item(0).getTextContent();
					value = eElement.getElementsByTagName(valuetag).item(0).getTextContent();
					NodeList valuetags = eElement.getElementsByTagName(valuetag);

					int tagle = valuetags.getLength();

					if (sub.equalsIgnoreCase("accountFlagsAfter") || sub.equalsIgnoreCase("accountFlagsBefore")
							|| sub.equalsIgnoreCase("usageCounterUsageThresholdInformation")
							|| sub.equalsIgnoreCase("usageThresholdInformation")
							|| sub.equalsIgnoreCase("dedicatedAccountChangeInformation")
							|| sub.equalsIgnoreCase("accountFlags") || sub.equalsIgnoreCase("offerInformationList")
							|| sub.equalsIgnoreCase("dedicatedAccountInformation")
							|| sub.equalsIgnoreCase("serviceOfferings") || sub.equalsIgnoreCase("offerInformation")
							|| sub.equalsIgnoreCase("attributeInformationList")) {
						sot = sub;
						value = "";
						tbl = tbl + "<tr><td>" + sub + "</td><td>" + value + "</td></tr>";

					} else if (tagle != 1) {
						for (int i = 1; i < tagle; i++) {
							Node vNode = valuetags.item(i);
							// System.out.println("row "+i);
							Element eElementval = (Element) vNode;

							if (sub.contains("DateTime")) {
								values = eElementval.getElementsByTagName("dateTime.iso8601").item(0).getTextContent();
							} else if (sub.contains("Flags")) {
								values = eElementval.getElementsByTagName("boolean").item(0).getTextContent();
							} else if (sub.contains("Value") || sub.contains("masterAccountNumber")
									|| sub.contains("originTransactionID") || sub.contains("currency")) {
								values = eElementval.getElementsByTagName("string").item(0).getTextContent();
							} else {
								values = eElementval.getElementsByTagName("i4").item(0).getTextContent();
							}
							sot = sub + "==" + values;
							// System.out.println("hi--i4 tag " + sot);
							tbl = tbl + "<tr><td>" + sub + "</td><td>" + values + "</td></tr>";

						}
					} else {
						sot = sub + " == " + value;
						tbl = tbl + "<tr><td>" + sub + "</td><td>" + value + "</td></tr>";
					}
				}
				// tbl = tbl + "<tr><td>" + sot + "</td></tr>";

			}
		} catch (Throwable e) {

			System.setProperty("Order_Status", "FAIL");
		}
		return tbl;
	}
}
	    	
