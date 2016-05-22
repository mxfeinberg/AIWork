import java.util.ArrayList;
import java.util.Random;

public class QLearningAgent implements Agent {

  /*
   * Default constructor for the QLearningAgent Class
   * numOfActions defaults to 5
   * numOfstates defaults to 0 
   * policy defaults to null	
   */
  public QLearningAgent() 
  {
	  this.numOfActions = 5;
	  this.numOfStates = 0;
	  policy = null;
	  actingPolicy = new int[numOfStates];
	  Q = new double[numOfActions][numOfStates];
	  this.rand = new Random();
  }
  /*
	 * called only once at the beginning. It tells the agent the number of states and the number
	 * of actions in the world.  The states area identified by integers from 0 to (numOfStates - 1).
	 * Similarly, the actions are from 0 to (numOfActions-1). In all cases, numOfActions should be equal to 5.
	 * 
	 * @param int numOfStates, the number of states in the world
	 * @param int numOfActions, the number of possible actions
	 */
  public void initialize(int numOfStates, int numOfActions) {
    this.numOfStates = numOfStates;
    this.numOfActions = numOfActions;
    policy = new Policy(actingPolicy);
	actingPolicy = new int[numOfStates];
	Q = new double[numOfActions][numOfStates];
	//for(int i = 0; i < numOfActions; i++)
		//for(int j = 0; j < numOfStates; j++)
			//Q[i][j] = Math.max(Math.sin(i)*j+i*j, Math.cos(i)*j+i*j);
  }
  /*
   * Methods returns your selected action (an integer in {0, 1, 2, 3, 4}) given the state as an argument.
   */
  public int chooseAction(int state) {
    if(rand.nextDouble() <= EXPLORATION)
    {
    	return rand.nextInt(numOfActions);
    }
    else
    {
    	int bestAction = 0;
    	double bestVal = Double.NEGATIVE_INFINITY;
    	for(int i = 0; i < numOfActions; i++)
    	{
    		if(Q[i][state] > bestVal)
    		{
    			bestAction = i;
    			bestVal = Q[i][state];
    		}
    	}
    	ArrayList<Integer> best = new ArrayList<Integer>();
    	for(int j = 0; j < numOfActions; j++)
    	{
    		if(bestVal == Q[j][state])
    			best.add(j);
    	}
    	return best.get(rand.nextInt(best.size()));
    	//return actingPolicy[state];
    }
  }

  public void updatePolicy(double reward, int action, int oldState, int newState) {
    // What is reward?
    // What is action?
    // What is oldState?
    // What is newState?
	double maximumValue = Double.NEGATIVE_INFINITY;
	for(int i = 0; i < numOfActions; i++)
	{
		if(Q[i][newState] > maximumValue)
		{
			maximumValue = Q[i][newState];	
		}
	}
	Q[action][oldState] = Q[action][oldState] + LEARNING_RATE * (reward + (DISCOUNT_FACTOR*maximumValue) - Q[action][oldState]);
	int bestAction = 0;
	double bestVal = Double.NEGATIVE_INFINITY;
	for(int i = 0; i < numOfActions; i++)
	{
		if(Q[i][oldState] > bestVal)
		{
			bestVal = Q[i][oldState];
			bestAction = i;
		}
	}
	actingPolicy[oldState] = bestAction;
	policy = new Policy(actingPolicy);
	
    return;
  }

  public Policy getPolicy() {
    return policy;
  }
  private Random rand;
  private int numOfStates;
  private int numOfActions;
  private int[] actingPolicy;
  private double[][] Q;
  private Policy policy;
  private final double DISCOUNT_FACTOR = 0.95;
  private final double LEARNING_RATE = 0.1;
  private final double EXPLORATION = 0.1;
}
