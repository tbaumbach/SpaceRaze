package sr.client.battlesim;

/**
 * This class is used to send information about an ongoing simulation to a listener of that simulation
 * @author WMPABOD
 *
 */
public class BattleSimResult {
	private double tf1wins;
	private double tf2wins;
	private int iterations;
	
	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public BattleSimResult(double tf1wins,double tf2wins){
		this.tf1wins = tf1wins;
		this.tf2wins = tf2wins;
	}
	
	public double getTf1wins() {
		return tf1wins;
	}
	
	public void setTf1wins(double tf1wins) {
		this.tf1wins = tf1wins;
	}
	
	public double getTf2wins() {
		return tf2wins;
	}
	
	public void setTf2wins(double tf2wins) {
		this.tf2wins = tf2wins;
	}
}
