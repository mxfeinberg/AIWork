import java.util.Random;
import java.util.ArrayList;
public class main {
	public static void main(String[] args)
	{
		Random rand = new Random();
		//ArrayList<Sample> samples = new ArrayList<Sample>();
		int count = 0;
		int GCount = 0;
		int EGCount = 0;
		int ABCount = 0;
		int FABCount = 0;
		//int[] test = {1, 2, 3, 4 ,5 ,6, 7, 8, 9, 10};
		//System.out.println(average(test));
		//System.out.println(stddev(test));
		double[] AoneHundredTrial = new double[10];
		double[] AoneThousandTrial = new double[10];
		double[] AtenThousandTrial = new double[10];
		double[] AoneHundredThousandTrial = new double[10];
		double[] BoneHundredTrial = new double[10];
		double[] BoneThousandTrial = new double[10];
		double[] BtenThousandTrial = new double[10];
		double[] BoneHundredThousandTrial = new double[10];
		double[] ConeHundredTrial = new double[10];
		double[] ConeThousandTrial = new double[10];
		double[] CtenThousandTrial = new double[10];
		double[] ConeHundredThousandTrial = new double[10];
		for(int k = 0; k < 4; k++)
		{
			
			for(int j = 0; j < 10; j++)
			{
				ArrayList<Sample> samples = new ArrayList<Sample>();
				for(int i = 0; i < 100 * Math.pow(10, k); i++)
				{
					samples.add(new Sample());
					//double a = rand.nextDouble();
					//samples.get(i).add(a < 0.6);
					//a
					reject(rand, samples, i, 0.6);
					//b
					reject(rand, samples, i, 0.3);
					
					//c
					if(samples.get(i).get(0))
						reject(rand, samples, i, 0.8);
					else
						reject(rand, samples, i, 0.7);
					
					//d
					if(samples.get(i).get(0) && samples.get(i).get(1)) //a = T and b = T
						reject(rand, samples, i, 0.4);
					else if(samples.get(i).get(0) && !samples.get(i).get(1)) //a = T and b = F
						reject(rand, samples, i, 0.2);
					else if(!samples.get(i).get(0) && samples.get(i).get(1)) //a = F and b = T
						reject(rand, samples, i, 0.1);
					else //a = F and b = F
					reject(rand, samples, i, 0.8);
					
					//e
					if(samples.get(i).get(2))
						reject(rand, samples, i, 0.8);
					else
						reject(rand, samples, i, 0.5);
					
					//f
					if(samples.get(i).get(2) && samples.get(i).get(3)) //c = T and d = T
						reject(rand, samples, i, 0.7);
					else if(samples.get(i).get(2) && !samples.get(i).get(3)) //c = T and d = F
						reject(rand, samples, i, 0.6);
					else if(!samples.get(i).get(2) && samples.get(i).get(3)) //c = F and d = T
						reject(rand, samples, i, 0.3);
					else //a = F and b = F
						reject(rand, samples, i, 0.4);
					
					//g
					if(samples.get(i).get(3))
						reject(rand, samples, i, 0.3);
					else
						reject(rand, samples, i, 0.2);
					if(samples.get(i).get(0) && !samples.get(i).get(1) && samples.get(i).get(2) 
					   && !samples.get(i).get(3) && samples.get(i).get(4) && samples.get(i).get(5)	
				       && !samples.get(i).get(6) )
					{
						count++;
					}
					if(!samples.get(i).get(6))
						GCount++;
					if(samples.get(i).get(4) && !samples.get(i).get(6))
						EGCount++;
					if(!samples.get(i).get(0) && samples.get(i).get(1))
						ABCount++;
					if(samples.get(i).get(5) && !samples.get(i).get(0) && samples.get(i).get(1))
						FABCount++;
				
				}
				//System.out.println(EGCount);
				//System.out.println(EGCount);
				if(k == 0)
				{
					AoneHundredTrial[j] = count / (100 * Math.pow(10, k));
					BoneHundredTrial[j] = 1.0 * EGCount / GCount;
					ConeHundredTrial[j] = 1.0 * FABCount / ABCount;
				}
				else if(k == 1)
				{
					AoneThousandTrial[j] = count / (100 * Math.pow(10, k));
					BoneThousandTrial[j] = 1.0 * EGCount / GCount;
					ConeThousandTrial[j] = 1.0 * FABCount / ABCount;
				}
				else if(k == 2)
				{
					AtenThousandTrial[j] = count / (100 * Math.pow(10, k));
					BtenThousandTrial[j] = 1.0 * EGCount / GCount;
					CtenThousandTrial[j] = 1.0 * FABCount / ABCount;
				}
				else
				{
					AoneHundredThousandTrial[j] = count / (100 * Math.pow(10, k));
					BoneHundredThousandTrial[j] = 1.0 * EGCount / GCount;
					ConeHundredThousandTrial[j] = 1.0 * FABCount / ABCount;
				}
				count = 0;
				EGCount = 0;
				GCount = 0;
				FABCount = 0;
				ABCount = 0;
			}
		}
		System.out.println("a) \n");
		System.out.format("100 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(AoneHundredTrial), stddev(AoneHundredTrial));
		System.out.format("1000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(AoneThousandTrial), stddev(AoneThousandTrial));
		System.out.format("10,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(AtenThousandTrial), stddev(AtenThousandTrial));
		System.out.format("100,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(AoneHundredThousandTrial), stddev(AoneHundredThousandTrial));
		System.out.println("b) \n");
		System.out.format("100 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(BoneHundredTrial), stddev(BoneHundredTrial));
		System.out.format("1000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(BoneThousandTrial), stddev(BoneThousandTrial));
		System.out.format("10,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(BtenThousandTrial), stddev(BtenThousandTrial));
		System.out.format("100,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(BoneHundredThousandTrial), stddev(BoneHundredThousandTrial));
		System.out.println("c) \n");
		System.out.format("100 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(ConeHundredTrial), stddev(ConeHundredTrial));
		System.out.format("1000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(ConeThousandTrial), stddev(ConeThousandTrial));
		System.out.format("10,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(CtenThousandTrial), stddev(CtenThousandTrial));
		System.out.format("100,000 Sample trial:\nAverage: %f\nStandard Deviation: %f\n", average(ConeHundredThousandTrial), stddev(ConeHundredThousandTrial));
	}
	public static void reject(Random r, ArrayList<Sample> s,int index, double probability)
	{
		double n = r.nextDouble();
		s.get(index).add(n < probability);
	}
	public static double average(int[] n)
	{
		int sum = 0;
		for(int k : n)
			sum+=k;
		return 1.0 * sum / n.length;
	}
	public static double average(double[] n)
	{
		double sum = 0;
		for(double k : n)
			sum+=k;
		return 1.0 * sum / n.length;
	}
	public static double stddev(double[] n)
	{
		double[] m = new double[n.length];
		for(int i = 0; i < m.length; i++)
			m[i]=Math.pow(n[i], 2);
		double expectationOne = average(m);
		double expectationTwo = Math.pow(average(n), 2);
		return Math.sqrt(expectationOne - expectationTwo);
	}

}
