import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Scanner;
public class csvCreator {
	public static boolean CSV(String info, String filename){
		File file = new File(filename);
		try {
			FileWriter writer = new FileWriter(filename);
			writer.append(info);
			writer.close();
		}catch (IOException error)
		  {
			  return false;
		  }
	  	return true;
	}
}
