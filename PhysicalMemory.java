import java.util.ArrayList;
public class PhysicalMemory{
	private ArrayList<Instruction> instructions;
	private boolean isLocked;

	public PhysicalMemory(){
		this.instructions = new ArrayList<Instruction>();
		this.isLocked = false;
	}

	public ArrayList<Instruction> getInstructions(){
		return this.instructions;
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

}