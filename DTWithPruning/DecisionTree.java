
/**
 * Do NOT edit implemented methods.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

/**
 * Class Decision Tree
 */
public class DecisionTree {
	/**
	 * Class Attribute
	 */
	public static class Attribute {
		/**
		 * Name of the attribute
		 */
		private final String name;

		/**
		 * Index of the column in the instances that corresponds to value of
		 * this attribute. Leftmost column has an index of zero.
		 */
		private final int columnIndex;

		/**
		 * All possible values that the attribute may take.
		 */
		private final String[] possibleAttrValues;

		/**
		 * Constructor.
		 *
		 * @param attributeInfo
		 *            String representation of all the fields of this class.
		 */
		public Attribute(final String attributeInfo) {
			final String[] fields = attributeInfo.split("\\s+");
			columnIndex = Integer.parseInt(fields[0]);
			name = fields[1];
			possibleAttrValues = Arrays.copyOfRange(fields, 2, fields.length);
		}
	}

	/**
	 * Class Instance
	 */
	private static class Instance {
		/**
		 * Label of an instance
		 */
		private final Label label;

		/**
		 * Features of an instance. These are values of all the attributes for
		 * this instance.
		 */
		private final String[] features;

		/**
		 * Constructor.
		 *
		 * @param featuresAndLabel
		 *            String representation of the fields of this class.
		 */
		public Instance(final String featuresAndLabel) {
			final String[] fields = featuresAndLabel.split("\\s+");
			features = Arrays.copyOfRange(fields, 0, fields.length - 1);
			label = Label.valueOf(fields[fields.length - 1]);
		}

		/**
		 * Get value of an attribute in this instance. May use while creating
		 * splits on an attribute-value pair.
		 *
		 * @param attr
		 *            Attribute
		 * @return Value
		 */
		public final String getValueForAttribute(final Attribute attr) {
			return features[attr.columnIndex];
		}
	}

	/**
	 * Possible Labels of an instance
	 */
	private static enum Label {
		YES, NO
	}

	/**
	 * @param args
	 *            Paths to input files.
	 */
	public static void main(final String[] args) {
		final List<Attribute> attributeInfo = readAttributes(args[0]);
		final List<Instance> trainingData = readInstances(args[1]);
		final DecisionTree tree = new DecisionTree(attributeInfo, trainingData);
		if(args.length > 3) {
			if(args[3].equalsIgnoreCase("prune")){
				// Only for bonus credit.
				System.out.println("Pruning implemented"); // Comment this out if you do implement!
				/*
				 * Add code to call your pruning method(s) here as appropriate.
				 * For example: tree.prune(arguments...);
				 */
				List<Instance> pruneData = readInstances(args[2]); //uses the test data to prune
				System.out.println("Initial Error: " + tree.computeError(pruneData));//calculates initial error
				tree.prune(pruneData);//call prune on the tree
			}
		}
		tree.print();
		System.out.println("\n");
		final List<Instance> testData = readInstances(args[2]);
		for (final Instance testInstance : testData) {
			System.out.println(tree.classify(testInstance));
		}
		System.out.println("\n");
		System.out.println("Training error = " + tree.computeError(trainingData) + " , Test error = "
				+ tree.computeError(testData));
	}

	/**
	 * To parse the attribute info file.
	 *
	 * @param attrInfoPath
	 *            file path
	 * @return List of attributes (objects of Class Attribute)
	 */
	public static List<Attribute> readAttributes(final String attrInfoPath) {
		final List<Attribute> attributes = new ArrayList<Attribute>();
		BufferedReader br = null;

		try {
			String currentLine;

			br = new BufferedReader(new FileReader(attrInfoPath));

			while ((currentLine = br.readLine()) != null) {
				final Attribute attribute = new Attribute(currentLine);
				attributes.add(attribute);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
		return attributes;
	}

	/**
	 * To parse the training data (instances)
	 *
	 * @param trainDataPath
	 *            file path
	 * @return List of Instances.
	 */
	public static List<Instance> readInstances(final String trainDataPath) {
		final List<Instance> instances = new ArrayList<Instance>();
		BufferedReader br = null;

		try {
			String currentLine;

			br = new BufferedReader(new FileReader(trainDataPath));
			br.readLine();

			while ((currentLine = br.readLine()) != null) {
				final Instance instance = new Instance(currentLine);
				instances.add(instance);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
		return instances;
	}

	/**
	 * The attribute which is the root of this tree.
	 */
	private final Attribute rootAttribute;

	/**
	 * True if this tree is a leaf.
	 */
	private Boolean isLeaf;

	/**
	 * The label to be output if this tree is a leaf; Set to Null if the
	 * 'isleaf' flag is false.
	 */
	private Label leafVal;

	/**
	 * List of the children trees sorted in the same order as the corresponding
	 * values of the root attribute.
	 */
	private List<DecisionTree> children;

	/**
	 * Constructor. Builds the tree given the following parameters.
	 *
	 * @param attributeList
	 *            List of attributes
	 * @param instanceList
	 *            List of instances
	 */
	public DecisionTree(final List<Attribute> attributeList, final List<Instance> instanceList) {
		isLeaf = shouldThisBeLeaf(instanceList, attributeList);
		if (isLeaf) {
			leafVal = computeLeafLabel(instanceList);
			rootAttribute = null;
			children = null;
			return;
		}
		leafVal = null;
		rootAttribute = computeBestAttribute(attributeList, instanceList);
		final List<Attribute> remAttributeList = getRemainingAttributes(attributeList, rootAttribute);
		children = new ArrayList<DecisionTree>();
		for (final String possibleVal : rootAttribute.possibleAttrValues) {
			children.add(new DecisionTree(remAttributeList,
				generateSplitForAttrVal(instanceList, rootAttribute, possibleVal)));
		}
	}

	/**
	 * Classify an instance. May also be used when evaluating performance on test data.
	 *
	 * @param instance
	 *            Instance to be classified.
	 * @return Label output
	 */
	public Label classify(final Instance instance) {
		if(this.isLeaf) //we're at the end boys
			return leafVal; //give 'em what they want
		else
		{
			String val = instance.getValueForAttribute(rootAttribute); //get value of instance for rootAttribute
			int index = -1;
			for(int i = 0; i < rootAttribute.possibleAttrValues.length; i++)//loop through attribute value to find corresponding child index
			{
				if(rootAttribute.possibleAttrValues[i].equals(val))
				{
					index = i;//store index of child to progress to
					i = rootAttribute.possibleAttrValues.length;//end loop
				}
			}
			if(index != -1)
				return children.get(index).classify(instance);//recurse further down the tree
			else
				return null;//shouldn't reach here
		}
	}
	/*
	 * Prunes the given tree using reduced error pruning
	 *
	 * @param List<instance> data, a list of instances that contains the data to be used for pruning
	 *
	 */
	public void prune(List<Instance> data)
	{
		List<DecisionTree> leaves = getLeafParents(children); //Collect the parents of all of the leaves using a helper function
		//System.out.println(leaves.size());
		double startingError = computeError(data); //compute the initial error on the pruning data before any pruning
		DecisionTree root = this;
		for(DecisionTree DT : leaves)//iterate through all of the parents of the leaves
		{
			//System.out.println(DT.rootAttribute.name);
			if(DT.children != null && DT.children.size() != 0)//null checking
			{
				List<DecisionTree> temp = new ArrayList<DecisionTree>(DT.children.size()); //create a temporary array to store the original children
				for(int i = 0; i < DT.children.size(); i++)//store the node's children in temp
				{
					temp.add(DT.children.get(i));
				}
				int yCount = 0;
				for(DecisionTree child : temp)
				{
					if(child.isLeaf && child.leafVal == Label.YES)//count the number of YES labels of the node's children
						yCount++;
				}
				if(yCount == temp.size()-yCount) //If the YES's and NO's are split 50/50, check to see if either label yields a lower error
				{
					for(int k = 0; k < 2; k++)
					{
						if(k == 0)
							DT.leafVal = Label.YES;
						else if(k == 1)
							DT.leafVal = Label.NO;
						DT.children = null;
						DT.isLeaf = true;
						print();
						double error = computeError(data);
						System.out.println("New Error: " + error);
						if(error < startingError) //if new error is lower, keep the change!
						{
							startingError = error;
						}
						else //if new error is not lower, revert the changes
						{
							DT.isLeaf = false;
							DT.leafVal = null;
							DT.children = new ArrayList<DecisionTree>(temp.size());
							for(int i = 0; i < temp.size(); i++)
							{
								DT.children.add(temp.get(i));
							}
						}
					}
				}
				else //Case when YES's and NO's are not the same number
				{
					if(yCount >= (int)(0.5*temp.size())) //if more YES's, label YES
						DT.leafVal = Label.YES;
					else                                 //else label NO
						DT.leafVal = Label.NO;
					DT.children = null; //trim the leaves/substree
					DT.isLeaf = true;
					print();
					double error = computeError(data);
					System.out.println("New Error: " + error);
					if(error < startingError) //if error is lower, keep changes!
					{
						startingError = error;
					}
					else                      //if error is not lower, don't keep the changes!
					{
						DT.isLeaf = false;
						DT.leafVal = null;
						DT.children = new ArrayList<DecisionTree>(temp.size());
						for(int i = 0; i < temp.size(); i++)
						{
							DT.children.add(temp.get(i));
						}
					}
				}
			}
		}

	}
	/*
	 * retrieves the parent nodes of every leaf in the given tree
	 *
	 * This function is a simply a helper function for prune
	 * NOTE: this function will never return the root node even if it is the parent of a leaf as
	 *       we do not want to remove the root node.
	 *
	 * @param List<DecisionTree> nodes, the children list of the root node of the Decision Tree
	 */
	public List<DecisionTree> getLeafParents(List<DecisionTree> nodes)
	{
		ArrayList<DecisionTree> leaves = new ArrayList<DecisionTree>();
		if(nodes != null)
		{
			for(DecisionTree DT : nodes)
			{
				if(DT.children != null && DT.children.size() != 0)
				{
					for(int i = 0; i < DT.children.size(); i++)
					{
						if(DT.children.get(i).isLeaf)
						{
							leaves.add(DT);
							i = DT.children.size();
						}
					}
				}
				else
				{
					if(DT.children != null)
						leaves.addAll(getLeafParents(DT.children));
				}
			}
		}
		if(leaves.size() == 0)
			return null;
		else
			return leaves;
	}
	//public List<DecisionTree> getLeaves(List<DecisionTree> nodes)
	//{
	//	ArrayList<DecisionTree> leaves = new ArrayList<DecisionTree>();
	//	for(DecisionTree DT : nodes)
	//	{
	//		if(DT.isLeaf)
	//		{
	//			leaves.add(DT);
	//		}
	//		else
	//			leaves.addAll(getLeaves(DT.children));
	//	}
	//	if(leaves.size() == 0)
	//		return null;
	//	else
	//		return leaves;
	//}
	/**
	 * Computes the best attribute (least entropy)
	 *
	 * @param attributeList
	 *            List of attributes
	 * @param instanceList
	 *            List of instances
	 * @return The best attribute
	 */
	private Attribute computeBestAttribute(final List<Attribute> attributeList, final List<Instance> instanceList) {
		//each attribute has n possible values
		//let there be m attributes in the attribute list
		Attribute bestAttribute = null;
		double entropyMinimum = Double.POSITIVE_INFINITY;
		for(int i = 0; i < attributeList.size(); i++)
		{
			Attribute temp = attributeList.get(i); //store current attrib
			int[] counter = new int[temp.possibleAttrValues.length];
			int[] yesCount = new int[temp.possibleAttrValues.length];
			int[] noCount = new int[temp.possibleAttrValues.length];
			HashMap<String, Integer> map = new HashMap<String, Integer>(counter.length);//use this to map string values to their index
			double entropy = 0;
			for(int j = 0; j < counter.length; j++)//map string values to corresponding index
			{
				map.put(temp.possibleAttrValues[j], j);
				counter[j]= 0;
			}
			for(int k = 0; k < instanceList.size(); k++)//loop through instancelist counting occurence of each attr value
			{
				counter[map.get(instanceList.get(k).getValueForAttribute(temp))]++; //THAT ONE LINER THO
				if(instanceList.get(k).label == Label.YES)
				{
					yesCount[map.get(instanceList.get(k).getValueForAttribute(temp))]++;
				}
				else
				{
					noCount[map.get(instanceList.get(k).getValueForAttribute(temp))]++;
				}
			}
			for(int m = 0; m < counter.length; m++)//with known occurences, calculate entropy
			{
				if(yesCount[m]+noCount[m] == 0)
					continue;
				else
				{
				double eventProb = (1.0*counter[m]/instanceList.size());
				double prob1, prob2;
				double individualEntropy;
				prob1 = (1.0*yesCount[m]/(yesCount[m]+noCount[m]));
				prob2 = (1.0*noCount[m]/(yesCount[m]+noCount[m]));
				//System.out.println("Prob1: " + prob1);
				//System.out.println("Prob2: " + prob2);
				if(prob1 == 0)
					individualEntropy = (-1.0*prob2*(Math.log(prob2)/Math.log(2)));
				else if(prob2 == 0)
					individualEntropy = (-1.0*prob1*(Math.log(prob1)/Math.log(2)));
				else
					individualEntropy = (-1.0*prob1*(Math.log(prob1)/Math.log(2))) + (-1.0*prob2*(Math.log(prob2)/Math.log(2)));
				//System.out.println("Individual Entropy: " + individualEntropy);
				entropy+=(individualEntropy * eventProb);
				//System.out.println("Entropy: " + entropy);
				}
				//double prob = (1.0*counter[m]/instanceList.size());
				//entropy+=(-1.0*prob*(Math.log(prob)/Math.log(2)));
			}
			//System.out.println(entropy);
			//System.out.println(temp.name);
			//System.out.println("\n" + temp.name);
			//System.out.println("Final Entropy: " + entropy);
			if(entropy < entropyMinimum)//if lower entropy attrib found, replace best
			{
				bestAttribute = temp;
				entropyMinimum = entropy;
			}
		}
		//System.out.println(bestAttribute.name);
		return bestAttribute;
	}
	/**
	 * Evaluate performance of this tree.
	 *
	 * @param trainingData
	 * @return
	 */
	private double computeError(final List<Instance> trainingData) {
		int count = 0;
		for(Instance m : trainingData)
		{
			if(m.label == classify(m))//count the number correctly classified
				count++;
		}
		return (1.0*trainingData.size()-count)/trainingData.size();//return the error
	}

	/**
	 * computes the label to be output at a leaf (which minimizes error on
	 * training data). If the given split is empty, you can assign any label for
	 * this leaf.
	 *
	 * @param instanceList
	 *            List of instances
	 * @return computed label
	 */
	private Label computeLeafLabel(final List<Instance> instanceList) {

		//if(instanceList.size() == 0)
		//	return null;
		int countY = 0;
		int countN = 0;
		for(Instance i: instanceList)//count the number of Yes's and No's
		{
			if(i.label == Label.YES)
				countY++;
			else
				countN++;
		}
		if(countY == instanceList.size())//if all yes, label YES
			return Label.YES;
		else if(countN == instanceList.size())//If all no, label NO
			return Label.NO;
		else if(countY == countN)//if same, return randomly
			return Math.random() < 0.5 ? Label.YES : Label.NO;
		else if(countY >= (int)(0.50*instanceList.size()))//if more than half yes, label YES
			return Label.YES;
		else if(countN >= (int)(0.50*instanceList.size()))//if more than half no, label NO
			return Label.NO;
		else //randomly assign, shouldn't get here
			return Math.random() < 0.5 ? Label.YES : Label.NO;
	}

	/**
	 * Split the data on an attribute-value pair.
	 *
	 * @param instanceList
	 *            List of instances
	 * @param splitAttribute
	 *            Attribute to split on
	 * @param splitVal
	 *            Value to split on
	 * @return List of instances that constitute the said split (i.e. have the
	 *         given value for the given attribute)
	 */
	private List<Instance> generateSplitForAttrVal(final List<Instance> instanceList, final Attribute splitAttribute, final String splitVal)
	{
		List<Instance> splitList = new ArrayList<Instance>();
		for(Instance m : instanceList)//loop and add if Instance has desired splitVal
		{
			if(m.getValueForAttribute(splitAttribute).equals(splitVal))
				splitList.add(m);
		}
		return splitList;
	}

	/**
	 * @param attributeList
	 *            List of candidate attributes at this subtree
	 * @param rootAttribute
	 *            Attribute chosen as the root
	 * @return List of remaining attributes
	 */
	private List<Attribute> getRemainingAttributes(final List<Attribute> attributeList, final Attribute rootAttribute) {
		List<Attribute> remainingAttributes = new ArrayList<Attribute>();
		for(Attribute a : attributeList)//loop and add all attributes that aren't the rootAttribute
		{
			if(!a.equals(rootAttribute))
				remainingAttributes.add(a);
		}
		return remainingAttributes;
	}

	/**
	 * Print a representation of this tree.
	 */
	public void print() {
		print(0);
	}

	/**
	 * Print relative to a calling super-tree.
	 *
	 * @param rootDepth
	 *            Depth of the root of this tree in the super-tree.
	 */
	private void print(final int rootDepth) {
		if (!isLeaf) {
			final Iterator<DecisionTree> itr = children.iterator();
			for (final String possibleAttrVal : rootAttribute.possibleAttrValues) {
				printIndent(rootDepth);
				System.out.println(rootAttribute.name + " = " + possibleAttrVal + " :");
				itr.next().print(rootDepth + 1);
			}
		} else {
			printIndent(rootDepth);
			System.out.println(leafVal);
		}
	}

	/**
	 * For formatted printing.
	 *
	 * @param n
	 *            Indent
	 */
	private void printIndent(final int n) {
		for (int i = 0; i < n; i++)
			System.out.print("\t");
	}

	/**
	 * Determine if this is simply a leaf, as a function of the given
	 * parameters.
	 *
	 * @param instanceList
	 *            List of instances
	 * @param attributeList
	 *            List of attributes
	 * @return True iff this tree should be a leaf.
	 */
	private boolean shouldThisBeLeaf(final List<Instance> instanceList, final List<Attribute> attributeList) {
		//if(instanceList.size() == 0 || attributeList.size() == 0 ) //removed ||attributeList.size() == 1
		//	return true;
		if(instanceList.size() == 0)//no more instances, must be leaf
			return true;
		if(attributeList.size() == 0)//no more attributes, can't split anymore
		{
			return true;
		}
		Label first = instanceList.get(0).label;
		int counter = 1;
		for(int i = 1; i < instanceList.size(); i++)
		{
			if(first == instanceList.get(i).label)
			{
				counter++;
			}
		}
		//if(counter > (int)(0.50 * instanceList.size()))
		//	return true;
		if(counter == instanceList.size())//if all instances are the same, should be leaf
		{
			//System.out.println("$$$$$$$$$$$$$$$$WE HIT THIS$$$$$$$$$$$$$$");
			return true;
		}
		return false;
	}
}
