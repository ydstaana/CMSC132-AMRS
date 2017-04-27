public class Register{
	private String name;
	private int intValue; // register can contain a value --R1,R32-- --ZF,NF-- ex. R1 = 0
	private String instValue; //register can contain an address/instruction --MAR,PC-- ex. MAR = "LOAD R1, R2"

	public Register(String name){
		this.name = name;
		this.intValue = 0;
		this.instValue = "";
	}

	public void setIntValue(int value){
		this.intValue = value;
	}

	public void setInstValue(String instruction){
		this.instValue = instruction;
	}

	public int getIntValue(){
		return this.intValue;
	}

	public String getInstValue(){
		return this.instValue;
	}
}