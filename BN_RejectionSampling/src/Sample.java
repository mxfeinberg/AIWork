import java.util.ArrayList;
public class Sample {
	ArrayList<Boolean> samples;
	public Sample()
	{
		samples = new ArrayList<Boolean>();
	}
	public Sample(int i)
	{
		samples = new ArrayList<Boolean>(i);
	}
	public void add(boolean b)
	{
		samples.add(new Boolean(b));
	}
	public Boolean get(int i)
	{
		return samples.get(i);
	}
	public void set(int i, Boolean b)
	{
		samples.set(i, b);
	}

}
