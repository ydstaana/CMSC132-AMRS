import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.*;
public class Instruction implements Runnable{
	ArrayList<String> listOfOperators = new ArrayList<>(Arrays.asList("LOAD", "CMP", "ADD", "SUB"));
	ArrayList<String> operands = new ArrayList<String>();
	String operator;
	private AMRS computer;
	private String name;
	private String instruction;
	private Register operand1;
	private Register operand2;
	private Integer intOperand2;

	public Instruction(String instr,AMRS amrs){
		this.name = instr;
		this.computer = amrs;
		instr = instr.replace(",", "");
		String array[] = instr.split(" ");
		for(String a:array){
			if(listOfOperators.contains(a)){
				this.operator = a;
			}else{
				this.operands.add(a);
			}
		}
		if(operator.equals("LOAD")){
			this.operand1 = amrs.registers.get(operands.get(0));
			this.intOperand2 = Integer.parseInt(this.operands.get(1));
		}
		else{
			this.operand1 = amrs.registers.get(operands.get(0));
			this.operand2 = amrs.registers.get(operands.get(1));
		}
	}

	public void run(){
		try{
			this.computer.fetch();
			Thread.sleep(0);
			/*ArrayList<Object> results = this.computer.decode();
			Thread.sleep(1000);
			results = this.computer.execute(results);
			Thread.sleep(800);
			results = this.computer.memory(results);
			Thread.sleep(1200);
			this.computer.writeBack(results);
			Thread.sleep(1200);*/
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		
	}

	public String getOperator(){
		return this.operator;		//returns operator
	}

	public ArrayList<String> getOperands(){
		return this.operands;
	}

	public void setOperand1(Register register){
		this.operand1 = register;
	}

	public void setOperand2(Register register){
		this.operand2 = register;
	}

	public Register getOp1(){
		return this.operand1;
	}

	public Register getOp2(){
		return this.operand2;
	}

	public Integer getIntOp2(){
		return this.intOperand2;
	}

	public void setIntOperand(Integer value){
		this.intOperand2 = value;
	}

	public String getName(){
		return this.name;
	}

	public void setComputer(AMRS computer){
		this.computer = computer;
	}
}

