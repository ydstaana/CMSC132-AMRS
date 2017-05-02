/*import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Computer{	
	private static final String inputFile = "inputfile.txt";
	public static ArrayList<Instruction> physicalMemory = new ArrayList<Instruction>();
	public static ArrayList<String> instructionSet = new ArrayList<String>();
	public static HashMap<String,Register> registers = new HashMap<String, Register>(); //set of registers
	public static int PC; //points to the instructions in the physical memory ex. physicalMemory[pc] = "LOAD R1,5"
	public static int MAR;
	public static int OF = 0;
    public static int NF = 0;
    public static int ZF = 0;
    public static String operator;
    public static String op1;
    public static String op2;
    private final ReentrantLock lock = new ReentrantLock();



	public static void main(String[] args){
		Computer comp = new Computer();
		Instruction inst1 = new Instruction("LOAD R1, R2");
		Instruction inst2 = new Instruction("ADD");
		inst1.setComputer(comp);
		inst2.setComputer(comp);
		new Thread(inst1).start();
		new Thread(inst2).start();
	}

	public void luh(String name){
		while(true){
			lock.lock();
			System.out.println("LUH " + name);
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){

			}
		}
	}

	public  void nuh(String name){
		while(true){
			System.out.println("NOT LUH " + name);
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){

			}
		}
	}





	
}*/