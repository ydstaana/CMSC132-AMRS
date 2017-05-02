import java.util.Timer;
import java.util.TimerTask;

public class StartTimer{
	public AMRS computer;
	public Timer timer;
	public StartTimer(AMRS amrs){
		this.computer = amrs;
		this.timer = new Timer();
		timer.scheduleAtFixedRate(new TimeTask(),2000,1000);
	}

	class TimeTask extends TimerTask{
		int counter = 1;
		public void run(){
			computer.clock_cycle++;
			//System.out.println("COUNTING.." + counter++);
		}
	}

	public void stopTimer(){
		this.timer.cancel();
		System.out.println("TIMER STOPPED");
	}
}