import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AMRS{	
	private static final String inputFile = "inputfile.txt";
	private static final PhysicalMemory physicalMemory = new PhysicalMemory();
	//public static ArrayList<Instruction> physicalMemory = new ArrayList<Instruction>();
	public static ArrayList<String> instructionSet = new ArrayList<String>();
	public static HashMap<String,Register> registers = new HashMap<String, Register>(); //set of registers
	public static int PC; //points to the instructions in the physical memory ex. physicalMemory[pc] = "LOAD R1,5"
	//public static int MAR;
	public static int OF = 0;
    public static int NF = 0;
    public static int ZF = 0;
    public static String operator;
    public static Register regOp1;
    public static Register regOp2;
    public static String op1;
    public static Integer op2;
    public static Integer answer;
    public static Integer a;
    public static Integer b;
    public static Integer clock_cycle = 1;
    public static Instruction instruction; 
    public static boolean cycleLock = false;
    public static StartTimer time;
	public static void main(String[] args){
		AMRS amrs = new AMRS();
		Register MBR =  new Register("MBR");
		Register MAR =  new Register("MAR");

		registers.put("MAR",MAR);
		registers.put("MBR",MBR);

		BufferedReader br = null;
		FileReader fr = null;
		
		for(int i = 1; i<=32; i++){
			String rname = "R"+Integer.toString(i);;
			registers.put(rname,new Register(rname));
		}

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

		boolean first = true;
		try{
			for (String inst : instructionSet){
				Instruction newInst = new Instruction(inst,amrs);
				physicalMemory.getInstructions().add(newInst);
				if(first){
					time = new StartTimer(amrs);
					first = false;
				}
				try{
					Thread.sleep(1000);
					Thread tr = new Thread(newInst);
					tr.setName(newInst.getName());
					tr.start();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
			}
 		}catch(Exception e){

		}
		//time.stopTimer();
		try{
			Thread.sleep(6000);
			for(String register:registers.keySet()) {
		    	System.out.println(register + " " + registers.get(register).getIntValue());
		 	}
		 }catch(Exception e){
		 	e.printStackTrace();
		 }
		
		
		/*execute();
		memory();
		writeBack();*/
			
	}


	public static void fetch(){
		//Get instruction address from PC, store in MAR
		//Access memory, get instruction with address MAR, store to MBR
		while(physicalMemory.tryLock() == true){ //check if physicalMemory is locked
			try{
				System.out.println("STALLING FETCH... AT CLOCK CYCLE " + clock_cycle);
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		if(PC < physicalMemory.getInstructions().size() + 1){
			physicalMemory.lock();
	       	registers.get("MAR").setIntValue(PC);
	        PC++;
	        //System.out.println(physicalMemory.getInstructions().get(0));
	        Instruction tempInst = physicalMemory.getInstructions().get(registers.get("MAR").getIntValue());
			System.out.println("FETCHING..." + tempInst.getName() + " AT CLOCK CYCLE " + clock_cycle);
	        registers.get("MBR").setInstObject(tempInst);
	        physicalMemory.unlock();
	    }

	}

	public static void decode(){
		//Get instruction from MBR, read operator and operands
		instruction = registers.get("MBR").getInstObject();

		operator = instruction.getOperator();
		regOp1 = instruction.getOp1();
		regOp2 = instruction.getOp2();

		if(operator.equals("LOAD")){
			while(regOp1.tryLock() != false){ //1st operand is being used
				try{
					System.out.println("STALLING DECODE..." + instruction.getName() + " AT CLOCK CYCLE " + clock_cycle);
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			System.out.println("DECODING..." + 	Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
			
			op2 = instruction.getIntOp2();
		}
		else{
			while(regOp1.tryLock() != false && regOp2.tryLock() != false){ //check if operands are being used by an instruction
				try{
					System.out.println("STALLING DECODE..." + instruction.getName() + " AT CLOCK CYCLE " + clock_cycle);
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			System.out.println("DECODING..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
			
		}
	}

	public static void execute(){
		String threadName = Thread.currentThread().getName();
		threadName = threadName.replace(",", "");
		String array[] = threadName.split(" ");
		String tempOperator = array[0];
		Register op1Reg = registers.get(array[1]);
		Register op2Reg = registers.get(array[2]);
		


		while(op1Reg.tryLock() == true || (op2Reg != null && op2Reg.tryLock() == true)){
			try{
				System.out.println("STALLING EXECUTE... AT CLOCK CYCLE " + clock_cycle);
				Thread.sleep(500);
			}catch(InterruptedException e){
					e.printStackTrace();
				}		
		}

		//given na tama muna ang input
		System.out.println("EXECUTING..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
		
		if(tempOperator.equals("LOAD")){
			op1Reg.lock();
		}
		else{
			op1Reg.lock();
			op2Reg.lock();
		}
        switch (tempOperator) {
            case "ADD":
				a = op1Reg.getIntValue();
				b = op2Reg.getIntValue();
	            answer = a + b;
	            	//registers.get(op1).setIntValue(answer);
                break;
            case "SUB":
            	a = op1Reg.getIntValue();
            	b = op2Reg.getIntValue();
            	answer = a - b;
            	//registers.get(op1).setIntValue(answer);
                break;
			case "CMP":
				a = op1Reg.getIntValue();
            	b = op2Reg.getIntValue();
            	answer = a - b;
            	if(answer == 0){
            		ZF = 1;
            	}else if(answer < 0){
            		NF = 1;
            	}
                break;
			default:
                    break;
		}
		if(tempOperator.equals("LOAD")){
			op1Reg.unlock();
		}
		else{
			op1Reg.unlock();
			op2Reg.unlock();
		}
	}
	public static void memory(){
		while(physicalMemory.tryLock() == true){
			try{
				System.out.println("STALLING...");
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		String threadName = Thread.currentThread().getName();
		threadName = threadName.replace(",", "");
		String array[] = threadName.split(" ");
		System.out.println("MEMORY....." + threadName + " AT CLOCK CYCLE " + clock_cycle);
		physicalMemory.lock();
		
		Register op1Reg = registers.get(array[1]);
		Register op2Reg = registers.get(array[2]);
		op1Reg.lock();
		System.out.println(op1Reg.getName() + " IS LOCKED");
		if(array[0].equals("LOAD")){
			op1Reg.setIntValue(Integer.parseInt(array[2]));
		}
		else{
			physicalMemory.unlock();
		}
		try{
			Thread.sleep(500);
			System.out.println(op1Reg.getName() + " IS UNLOCKED");
			op1Reg.unlock();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		physicalMemory.unlock();

		

	}

	public static void writeBack(){
		String threadName = Thread.currentThread().getName();
		threadName = threadName.replace(",", "");
		String array[] = threadName.split(" ");
		String tempOperator = array[0];
		Register op1Reg = registers.get(array[1]);
		Register op2Reg = registers.get(array[2]);

		while(op1Reg.tryLock() != false){ //check if operands are being used by an instruction
				try{
					System.out.println("STALLING... ");
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			System.out.println("WRITE BACK..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
			if(tempOperator.equals("LOAD")){
				op1Reg.lock();
			}
			else{
				op1Reg.lock();
				op2Reg.lock();
				op1Reg.setIntValue(answer);
			}

	}
}