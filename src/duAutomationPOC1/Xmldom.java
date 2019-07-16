package duAutomationPOC1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.xml.sax.SAXException;

public class Xmldom {
	static File file = new File( "C:\\Users\\venureddyg\\Documents\\My Received Files\\TC_003.xml");
	public static String param1 = "name[0]";
	// public static String msisdn="971520002069";
	// public static String filetype="OCC";
	public static String nodetag = "params";
	// public static String idtag;

	public static void main(String argv[]) throws IndexOutOfBoundsException, ParserConfigurationException, SAXException, IOException {

		parsexml(param1, file);

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
		System.out.println(param + " = " + doc.select(param).get(0).text());
		if (doc.select(param).size() != 0) {
			// info(param + " = " + doc.select(param).get(inde).text());
			return doc.select(param).get(inde).text();
		} else {
			return "";
		}
	}
}