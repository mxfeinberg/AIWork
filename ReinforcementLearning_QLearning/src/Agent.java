/* Author: Mingcheng Chen */

public interface Agent {
	/*
	 * called only once at the beginning. It tells the agent the number of states and the number
	 * of actions in the world.  The states area identified by integers from 0 to (numOfStates - 1).
	 * Similarly, the actions are from 0 to (numOfActions-1). In all cases, numOfActions should be equal to 5.
	 */
  public void initialize(int numOfStates, int numOfActions);
  /*
   * Methods returns your selected action (an integer in {0, 1, 2, 3, 4}) given the state as an argument.
   */
  public int chooseAction(int state);
  /*
   * processes the reward recieved by executing an action which transitions the agent from oldState to newState.
   */
  public void updatePolicy(double reward, int action,
                           int oldState, int newState);
  /*
   * return a Policy Object, which speciifes the action for each state. More details can be found in Policy.java
   */
  public Policy getPolicy();
}
