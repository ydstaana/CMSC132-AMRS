public class Register{
	private String name;
	private int intValue; // register can contain a value --R1,R32-- --ZF,NF-- ex. R1 = 0
	private String instValue; //register can contain an address/instruction --MAR,PC-- ex. MAR = "LOAD R1, R2"
	private Instruction instObject;
	private boolean isLocked;

	public Register(String name){
		this.name = name;
		this.intValue = intValue;
		this.instValue = "";
		this.isLocked = false;
	}

	public boolean tryLock(){
		return this.isLocked;
	}
	public void lock(){
		this.isLocked = true;
	}

	public void unlock(){
		this.isLocked = false;
	}

	public String getName(){
		return this.name;
	}

	public void setIntValue(int value){
		this.intValue = value;
	}

	public void setInstValue(String instruction){
		this.instValue = instruction;
	}

	public void setInstObject(Instruction instruction){
		this.instObject = instruction;
	}

	public Instruction getInstObject(){
		return this.instObject;
	}

	public int getIntValue(){
		return this.intValue;
	}

	public String getInstValue(){
		return this.instValue;
	}
}