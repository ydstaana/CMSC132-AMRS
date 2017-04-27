import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AMRS{	
	private static final String inputFile = "inputfile.txt";
	public static ArrayList<Instruction> physicalMemory = new ArrayList<Instruction>();
	public static ArrayList<String> instructionSet = new ArrayList<String>();
	public static HashMap<String,Register> registers = new HashMap<String, Register>(); //set of registers
	public static int PC; //points to the instructions in the physical memory ex. physicalMemory[pc] = "LOAD R1,5"
	public static int MAR;
	public int OF = 0;
    public int NF = 0;
    public int ZF = 0;
    public static String operator;
    public static String op1;
    public static String op2;


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
				instructionSet.add(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		
		
		decode(0, instructionSet);
		execute();
		//for every instruction in physicalMemory, create an instance
		//for(String inst : physicalMemory)

	}

	public static void fetch(){
		if(PC < physicalMemory.size() + 1){
	        MAR = PC;
	        PC++;
	    }
	}

	public static void decode(Integer i, ArrayList<String> instructionSet){
		Instruction instruction = new Instruction(instructionSet.get(i));
		operator = instruction.getOperator();
		op1 = instruction.getOperands().get(0);
		op2 = instruction.getOperands().get(1);
	}

	public static void execute(){
		//given na tama muna ang input
		int answer = 0;
		int a, b;
        switch (operator) {
            case "LOAD":		//Registername = value
            	/*checks if register already exist. If yes, it will replace the existing value.
            	  Else, create a new register.
            	*/
            	if(registers.containsKey(op1)){		
            		registers.get(op1).setIntValue(Integer.parseInt(op2));	
            	}else{
            		Register r= new Register(op1, Integer.parseInt(op2));
            		registers.put(op1, r);
            	}
                break;
            case "ADD": operator = "ADD";
            	if(registers.containsKey(op1) && registers.containsKey(op2) ){
	            	a = registers.get(op1).getIntValue();
	            	b = registers.get(op2).getIntValue();
	            	answer = a + b;
	            	registers.get(op1).setIntValue(answer);
	            }
                break;
            case "SUB": operator = "SUB";
            	if(registers.containsKey(op1) && registers.containsKey(op2) ){
	            	a = registers.get(op1).getIntValue();
	            	b = registers.get(op2).getIntValue();
	            	answer = a - b;
	            	registers.get(op1).setIntValue(answer);
	            }
                break;
			case "CMP": operator = "CMP";
				if(registers.containsKey(op1) && registers.containsKey(op2) ){
	            	a = registers.get(op1).getIntValue();
	            	b = registers.get(op2).getIntValue();
	            	answer = a - b;
	            	if(answer == 0){
	            		ZF = 1;
	            	}else if(answer < 0){
	            		NF = 1;
	            	}else{

	            	}
	            }
                break;
			default:
                    break;
		}
	}
}