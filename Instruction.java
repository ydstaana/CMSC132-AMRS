import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.*;
public class Instruction{
	ArrayList<String> listOfOperators = new ArrayList<>(Arrays.asList("LOAD", "CMP", "ADD", "SUB"));
	ArrayList<String> operands = new ArrayList<String>();
	String operator;
	public Instruction(String instr){
		instr = instr.replace(",", "");
		String array[] = instr.split(" ");
		for(String a:array){
			if(listOfOperators.contains(a)){
				this.operator = a;
			}else{
				this.operands.add(a);
			}
		}
	}

	public String getOperator(){
		return this.operator;		//returns operator
	}

	public ArrayList<String> getOperands(){
		return this.operands;
	}
}

