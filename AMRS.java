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
		Register IR =  new Register("IR");


		registers.put("MAR",MAR);
		registers.put("MBR",MBR);
		registers.put("IR",IR);

		BufferedReader br = null;
		FileReader fr = null;
		
		registers.put("R1",new Register("R1"));
		registers.put("R2",new Register("R2"));
		registers.put("R3",new Register("R3"));
		registers.put("R4",new Register("R4"));
		

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
					Thread.sleep(1200);
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
			Thread.sleep(9000);
			for(String register:registers.keySet()) {
		    	System.out.println(register + " " + registers.get(register).getIntValue());
		 	}
		 }catch(Exception e){
		 	e.printStackTrace();
		 }
		


		
	
			
			

	}


	public static void fetch(){
		//Get instruction address from PC, store in MAR
		//Access memory, get instruction with address MAR, store to MBR
		Instruction tempInst =null;

		if(PC < physicalMemory.getInstructions().size() + 1){
			physicalMemory.lock();
	       	registers.get("MAR").setIntValue(PC);
	        PC++;
	        //System.out.println(physicalMemory.getInstructions().get(0));
	        tempInst = physicalMemory.getInstructions().get(registers.get("MAR").getIntValue());
			System.out.println("FETCHING..." + tempInst.getName() + " AT CLOCK CYCLE " + clock_cycle);
	        registers.get("MBR").setInstObject(tempInst);
	        physicalMemory.unlock();
	    }
	    try{
	    	Thread.sleep(900);
	        registers.get("IR").setInstObject(tempInst);	
	    	decode(registers.get("IR"));
	    }catch(InterruptedException e){
	    	e.printStackTrace();
	    }
	}

	public static void decode(Register CurrentInstructionRegister){
		//Get instruction from MBR, read operator and operands
		instruction = CurrentInstructionRegister.getInstObject();
		System.out.println("NAME : " + instruction.getName());
		ArrayList<Object> values = new ArrayList<Object>();
		operator = instruction.getOperator();
		regOp1 = instruction.getOp1();
		regOp2 = instruction.getOp2();
		values.add(operator);
		values.add(regOp1);

		
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
			values.add(op2);
		}
		else{
			values.add(regOp2);
			while(regOp1.tryLock() != false || regOp2.tryLock() != false){ //check if operands are being used by an instruction
				try{
					System.out.println("STALLING DECODE..." + instruction.getName() + " AT CLOCK CYCLE " + clock_cycle);
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			System.out.println("DECODING..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
			
		}
		try{
			Thread.sleep(900);
			if(operator.equals("LOAD")){
				execute(operator,regOp1,op2);
			}else{
				execute(operator,regOp1,regOp2);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public static void execute(String operator, Object operand1, Object operand2){
		Register executeOperand1 = null;
		Register executeOperand2 = null;
		Integer intOperand2 = null;
		if(operator.equals("LOAD")){
			executeOperand1 = (Register) operand1;
			intOperand2 = (Integer) operand2;
		}
		else{
			executeOperand1 = (Register) operand1;
			executeOperand2 = (Register) operand2;
		}
		//System.out.println(operator + " " + executeOperand1.getName() + " " + intOperand2);
		//System.out.println(results.get(0) + " " + results.get(1) + " "  + results.get(2));
		/*String tempOperator = (String) results.get(0);
		Register op1Reg =(Register) results.get(1);
		Register op2Reg =null;
		if(tempOperator.equals("LOAD")){
			Integer op2 = (Integer) results.get(2);	
		}
		else{
			op2Reg =(Register) results.get(2);
		}*/

		while(executeOperand1.tryLock() == true || (executeOperand2 != null && executeOperand2.tryLock() == true)){
			try{
				System.out.println("STALLING EXECUTE... AT CLOCK CYCLE " + clock_cycle);
				Thread.sleep(500);
			}catch(InterruptedException e){
					e.printStackTrace();
				}		
		}

		//given na tama muna ang input
		System.out.println("EXECUTING..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
		
		if(operator.equals("LOAD")){
			executeOperand1.lock();
			System.out.println(executeOperand1.getName() + " IS LOCKED");
		}
		else{
			executeOperand1.lock();
			executeOperand2.lock();
			System.out.println(executeOperand1.getName() + " IS LOCKED");
		}
        switch (operator) {
            case "ADD":
				a = executeOperand1.getIntValue();
				b = executeOperand2.getIntValue();
				System.out.println(a + " " + b);
	            answer = a + b;
	            	//registers.get(op1).setIntValue(answer);
                break;
            case "SUB":
            	a = executeOperand1.getIntValue();
            	b = executeOperand2.getIntValue();
            	answer = a - b;
            	//registers.get(op1).setIntValue(answer);
                break;
			case "CMP":
				a = executeOperand1.getIntValue();
            	b = executeOperand2.getIntValue();
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
		try{
			Thread.sleep(1000);
			if(operator.equals("LOAD")){
				System.out.println(executeOperand1.getName() + " IS UNLOCKED");
				executeOperand1.unlock();
				memory(operator,executeOperand1,intOperand2);
			}
			else{
				System.out.println(executeOperand1.getName() + " IS UNLOCKED");
				executeOperand1.unlock();
				executeOperand2.unlock();
				memory(operator,executeOperand1,executeOperand2);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		
		//results.add(answer);
		//System.out.println("EXECUTE " + results.get(2));
		//return results;
	}
	public static void memory(String operator, Object operand1, Object operand2){
		Register memoryOperand1 = null;
		Register memoryOperand2 = null;
		Integer intMemoryOperand2 = null;
		if(operator.equals("LOAD")){
			memoryOperand1 = (Register) operand1;
			intMemoryOperand2 = (Integer) operand2;
		}
		else{
			memoryOperand1 = (Register) operand1;
			memoryOperand2 = (Register) operand2;
		}
	
		while(physicalMemory.tryLock() == true){
			try{
				System.out.println("STALLING...");
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}

		System.out.println("MEMORY....." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
		physicalMemory.lock();
		
		memoryOperand1.lock();
		System.out.println(memoryOperand1.getName() + " IS LOCKED");
		
		try{
			if(operator.equals("LOAD")){
				memoryOperand1.setIntValue(intMemoryOperand2);
				Thread.sleep(1000);
				physicalMemory.unlock();
				memoryOperand1.unlock();
				System.out.println(memoryOperand1.getName() + " IS UNLOCKED");
				writeBack(operator,memoryOperand1, intMemoryOperand2);
			}
			else{
				Thread.sleep(1000);
				physicalMemory.unlock();
				memoryOperand1.unlock();
				System.out.println(memoryOperand1.getName() + " IS UNLOCKED");
				writeBack(operator,memoryOperand1, memoryOperand2);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public static void writeBack(String operator, Object operand1, Object operand2){
		Register wbReg1 = null;
		Register wbReg2 = null;
		Integer intWbReg2 = null;

		if(operator.equals("LOAD") == false){
			wbReg1 = (Register) operand1;
			wbReg2 = (Register) operand2;
		}
		else{
			wbReg1 = (Register) operand1;
			intWbReg2 = (Integer) operand2;	
		}


		while(wbReg1.tryLock() != false){ //check if operands are being used by an instruction
				try{
					System.out.println("STALLING...WRITEBACK AT CLOCK CYCLE " + clock_cycle);
					Thread.sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		System.out.println("WRITE BACK..." + Thread.currentThread().getName() + " AT CLOCK CYCLE " + clock_cycle);
		if(operator.equals("LOAD")){
			try{
				wbReg1.lock();
				Thread.sleep(900);
				wbReg1.unlock();
			}catch(InterruptedException e){
				e.printStackTrace();
			}

		}
		else{
			wbReg1.lock();
			wbReg2.lock();
			System.out.println("WRITEBACK " + answer);
			wbReg1.setIntValue(answer);
			wbReg1.unlock();
			wbReg2.unlock();
		}

	}
}