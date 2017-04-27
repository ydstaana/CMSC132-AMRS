import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.*;
public class Instruction{
	ArrayList<String> listOfOperators = new ArrayList<>(Arrays.asList("LOAD", "CMP", "ADD", "SUB", "MUL", "DIV"));
	ArrayList<String> operands = new ArrayList<String>();
	String operator;
	public Instruction(String instr){
		instr = instr.replace(",", " ");
		String array[] = instr.split(" ");
		for(String a:array){
			if(listOfOperators.contains(a)){
				this.operator = a;
			}else{
				this.operands.add(a);
			}
		}
	}
	
	public void fetch(){}
	public void decode(){}
	public int execute(){
		//given na tama muna ang input
        switch (this.operator) {
            case "LOAD":
					for(String a:operands){
						
					}
                    break;
            case "ADD": operator = "ADD";
                    break;
            case "SUB": operator = "SUB";
                    break;
            case "MUL": operator = "MUL";
                    break;
			case "DIV": operator = "DIV";
                    break;
			case "CMP": operator = "CMP";
                    break;
			default:
                    break;
		}
		return 0;
	}


	public static void main(String[] args){
		String a  = "LOAD R1,3";
		Instruction in1 = new Instruction(a);
		System.out.println(in1.operands);
	}
}

