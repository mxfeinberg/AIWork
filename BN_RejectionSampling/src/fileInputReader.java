import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
public class fileInputReader {
	public static void main(String[] args)
	{
		Scanner s;
		ArrayList<String> numbers = new ArrayList<String>();
		try
		{
			s = new Scanner(new File("network1.txt"));
			System.out.println("Success");
			int n = Integer.parseInt(s.nextLine());
			System.out.println(n);
			for(int i = 0; i < n; i++)
			{
				System.out.println(s.nextLine());
			}
			while(s.hasNextLine())
			{
				numbers.add(s.nextLine());
			}
		}
		catch(Exception e){}
	}	

}
