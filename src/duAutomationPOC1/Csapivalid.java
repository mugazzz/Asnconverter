package duAutomationPOC1;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Csapivalid {
	public static String file = "C:\\Users\\venureddyg\\Documents\\My Received Files\\TC-0041_response.xml";

	public static void main(String[] args) throws Exception {
	
			WebService(file,"1","");
		
	}

	public static void WebService(String XMLResponse_Path,String a1, String a2 ) throws Exception {

		String AtrrID = a2;
		String givenoff = a1;
		String Information= "pamInformationList";
		String Idinfo="pamClassID";

		String Nodetag = "member";
		String sub = null;
		int attid=0;
		int off=0;

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

					if (sub.contains(Information)) {
						System.out.println(sub);
						Node nvalue = eElement.getElementsByTagName(nametag).item(0).getNextSibling();
						if (nvalue.getNodeType() == Node.ELEMENT_NODE) {
							Element vElement = (Element) nvalue;

							NodeList structList = vElement.getElementsByTagName("struct");

							int structDataLength = structList.getLength();

							for (int i = 0; i < structDataLength; i++) {

								Node sNode = structList.item(i);

								if (sNode.getNodeType() == Node.ELEMENT_NODE) {
									Element structElement = (Element) sNode;

									NodeList memberList = structElement.getChildNodes();

									for (int j = 0; j < memberList.getLength(); j++) {
										Node mNode = memberList.item(j);
										if (mNode.getNodeType() == Node.ELEMENT_NODE) {
											Element memElement = (Element) mNode;

											String dta = memElement.getElementsByTagName("name").item(0)
													.getTextContent();
											String val = memElement.getElementsByTagName("value").item(0)
													.getTextContent();

											if (dta.contains(Idinfo) && val.contains(givenoff)) {
												off=1;
												// memElement.getParentNode().getChildNodes().getLength();
												String att = memElement.getParentNode().getFirstChild()
														.getTextContent();

												if (att.contains("attributeInformationList")) {
													Node nvalu = memElement.getPreviousSibling().getPreviousSibling()
															.getFirstChild().getNextSibling();
													if (nvalu.getNodeType() == Node.ELEMENT_NODE) {
														Element vElemen = (Element) nvalu;
														NodeList structLis = vElemen.getElementsByTagName("struct");

														for (int i1 = 0; i1 < structLis.getLength(); i1++) {

															Node oNode = structLis.item(i1);
															if (oNode.getNodeType() == Node.ELEMENT_NODE) {
																Element offElement = (Element) oNode;
																NodeList memberdata = offElement
																		.getElementsByTagName("member");

																for (int j1 = 0; j1 < memberdata.getLength(); j1++) {
																	String datavalue = offElement
																			.getElementsByTagName("value").item(0)
																			.getTextContent();

																	if (AtrrID.equalsIgnoreCase(datavalue)) {
																		attid=1;
																		String dataname = offElement
																				.getElementsByTagName("name").item(j1)
																				.getTextContent();
																		datavalue = offElement
																				.getElementsByTagName("value").item(j1)
																				.getTextContent();

																		System.out.println(dataname + "==" + datavalue);

																	} else {
//																		System.out.println("Given AtrrID " + AtrrID
//																				+ " not found matching for given OfferId "
//																				+ givenoff);

																		break;

																	}

																}
																
															}
															
														}
													} else {

													}
												} else {
													attid=1;
													System.out
															.println("Offer ID is not having attributeInformationList");
												}

												for (int k = 0; k < memberList.getLength(); k++) {

													Node mNodedup = memberList.item(k);

													if (mNode.getNodeType() == Node.ELEMENT_NODE) {
														Element memElementdup = (Element) mNodedup;

														String offertag = memElementdup.getElementsByTagName("name")
																.item(0).getTextContent();
														String offertagvalue = memElementdup
																.getElementsByTagName("value").item(0).getTextContent();
														System.out.println(offertag + "==" + offertagvalue);
													}
												}
											} else {
											}

										}

									}

								} else {
									// continue;
								}

							}

						}

					}

				}
			}
			if(attid==0) {
				System.out.println("Attribute not found");
				}
			if(off==0) {
				System.out.println("offer not found");
			}

		} catch (Throwable e) {

		}

	}
}
