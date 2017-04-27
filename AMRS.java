import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AMRS{	
	private static final String inputFile = "inputfile.txt";
	public ArrayList<Instruction> physicalMemory = new ArrayList<Instruction>();
	public int pc; //points to the instructions in the physical memory ex. physicalMemory[pc] = "LOAD R1,5"

	public static void main(String[] args){
		BufferedReader br = null;
		FileReader fr = null;
		//---------------------_TO DO_-----------------------

		//Read instructions from file, append to instructions 
		try{
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		


		//for every instruction in physicalMemory, create an instance
		//for(String inst : physicalMemory)


	}
}