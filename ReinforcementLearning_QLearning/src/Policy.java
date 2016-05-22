/* Author: Mingcheng Chen */

import java.io.PrintWriter;

public class Policy {
	/*
	 * parameterized constructor for the policy class
	 * @arg int[] actions, an integer array of actions
	 * creates a new policy object based on the input array of actions
	 */
  public Policy(int[] actions) {
    this.actions = new int[actions.length];
    for (int i = 0; i < actions.length; i++) {
      this.actions[i] = actions[i];
    }
  }
  /*
   * @param String filename, the name of the file that the policy should be saved to
   * saves the current policy to a text file
   */
  public void save(String filename) {
    try {
    PrintWriter writer = new PrintWriter(filename);
    for (int i = 0; i < this.actions.length; i++) {
      writer.println(this.actions[i]);
    }
    writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private int[] actions;
}
