import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Turtle {
	  public static void turtle(String input, String output) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(input));
//	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));
	    String line;
	    while ((line = br.readLine()) != null) {
	    	System.out.println(line);
//			bw.write(line);
//			bw.newLine();
	    }
	    br.close();
//	    bw.close();
	  }
	  
	  public static void main (String[] args) throws IOException{
		  Turtle.turtle("artemisBookstoreData-v1.csv", "");
	  }
	
}
