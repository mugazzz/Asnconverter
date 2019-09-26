package duAutomationPOC1;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Nodenew {
	public static String file = "C:\\Users\\venureddyg\\Desktop\\parallel_ussd-Regression_CIS_DB-API\\Result\\22-Sep-2019\\22-Sep-2019_16-54-39\\TC-1092__API_GetOffers\\Response\\response.xml";

	public static void main(String[] args) throws Exception {

		WebService(file);

	}

	public static void WebService(String XMLResponse_Path) throws Exception {
		String OfferId = "58";
		

		String Nodetag = "member";
		String sub = null;
		

		String nametag = "name";
	
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

					sub = eElement.getElementsByTagName("name").item(0).getTextContent();

					if (sub.contains("offerInformation")) {
						System.out.println(sub);
						Node nvalue = eElement.getElementsByTagName(nametag).item(0).getNextSibling();
						if (nvalue.getNodeType() == Node.ELEMENT_NODE) {
							Element vElement = (Element) nvalue;

							NodeList offerdata = vElement.getElementsByTagName("struct");

							int offerDataLength = offerdata.getLength();
							
							for (int i = 0; i < offerDataLength; i++) {

								Node oNode = offerdata.item(i);

								if (oNode.getNodeType() == Node.ELEMENT_NODE) {
									Element offElement = (Element) oNode;
									
									NodeList memberdata= offElement.getElementsByTagName("member");
									
									for (int j = 0; j < memberdata.getLength(); j++) {
										String datavalue = offElement.getElementsByTagName("value").item(1)
												.getTextContent();
										
										if(OfferId.equalsIgnoreCase(datavalue)) {
											String dataname = offElement.getElementsByTagName("name").item(j)
													.getTextContent();
										 datavalue = offElement.getElementsByTagName("value").item(j)
													.getTextContent();

											System.out.println(dataname + "==" + datavalue);
											
										}else {
											
											break;
											
										}
										
									}
								}

							}

						} 

					} 

				} 
			}

		} catch (Throwable e) {

		}

	}

}
