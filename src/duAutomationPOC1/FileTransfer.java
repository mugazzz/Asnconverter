package duAutomationPOC1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class FileTransfer {
	private static String gzfile;
	private static String berfile;
	private static String gzfilepath = "D:\\DU Automation\\du_unix-master\\OCCGZ\\";
	private static String finalpath = "D:\\DU Automation\\ASNConverter\\CDR\\OCC-data\\";

	public static void main(String[] args) {
		File folder = new File("D:\\DU Automation\\du_unix-master\\OCCGZ");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());

				gzfile = gzfilepath + listOfFiles[i].getName();
				File f = new File(gzfile);

				System.out.println("Zip file of OCC is " + gzfile);

				String bername = listOfFiles[i].getName();
				String bername1 = (bername.substring(0, bername.length() - 3));
				berfile = finalpath + bername1;

				System.out.println("Extracted ber file is " + berfile);

				FileTransfer gZip = new FileTransfer();
				gZip.gunzipIt(gzfile, berfile);
				//f.delete();
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());

			}

		}

	}

	/**
	 * GunZip it
	 */
	public void gunzipIt(String INPUT_GZIP_FILE1, String OUTPUT_FILE2) {

		byte[] buffer = new byte[1024];
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE1));

			// ==To delete existing files in OCC-data folder===========
			File index = new File("D:\\DU Automation\\ASNConverter\\CDR\\OCC-data");
//			String[] entries = index.list();
//			for (String s : entries) {
//				File currentFile = new File(index.getPath(), s);
//				currentFile.delete();
//				System.out.println("Existing file deleted");
//			}
			// =====================================

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
}