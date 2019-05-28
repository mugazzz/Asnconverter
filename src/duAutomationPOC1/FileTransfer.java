package duAutomationPOC1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class FileTransfer {
	private static String gzfile;
	private static String berfile;
	private static String gzfilepath = "D:\\DU Automation\\ASNConverter\\CDR\\";
	private static String finalpath = "D:\\DU Automation\\du_unix-master\\InputCdrFile\\OCCunzip\\";

	public static void main(String[] args) {
		File_Deletion(gzfilepath);
		
	}
	
	public static void File_Deletion(String gzfilepath) {
		File folder = new File(gzfilepath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				

		 
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
				String pathofDir= listOfFiles[i].getPath();
				System.out.println(pathofDir);
				 File folderin =new File(pathofDir);
				 File[] list = folderin.listFiles();
				 for (int j = 0; j < list.length; j++) {

						if (list[j].isFile()) {
							System.out.println("File " + list[j].getName());
							
						
								File currentFile = new File(list[j].getPath());
								currentFile.delete();
								System.out.println("Existing file deleted");
							

					 
						} else if (listOfFiles[j].isDirectory()) {
							System.out.println("Directory " + listOfFiles[j].getName());

			}else {
				System.out.println("No Files or Folder's Found");
				
			}

				 }
			}
		}
	}
	

	
	public void gunzipIt(String INPUT_GZIP_FILE1, String OUTPUT_FILE2) {

		byte[] buffer = new byte[1024];
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE1));

			// ==To delete existing files in OCC-data folder===========
			//File index = new File("D:\\DU Automation\\ASNConverter\\CDR\\OCC-data");
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