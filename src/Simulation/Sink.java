package Simulation;

import java.util.ArrayList;
/**
 *	A sink
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Sink implements ProductAcceptor
{
	/** All products are kept */
	private final ArrayList<Caller> callers;
	/** All properties of products are kept */
	private final ArrayList<Integer> numbers;
	private final ArrayList<Double> times;
	private final ArrayList<String> events;
	private final ArrayList<String> stations;
	/** Counter to number products */
	private int number;
	/** Name of the sink */
	private final String name;
	
	/**
	*	Constructor, creates objects
	*/
	public Sink(String n)
	{
		name = n;
		callers = new ArrayList<>();
		numbers = new ArrayList<>();
		times = new ArrayList<>();
		events = new ArrayList<>();
		stations = new ArrayList<>();
		number = 0;
	}
	
        @Override
	public boolean handoverCall(Caller caller)
	{
		number++;
		callers.add(caller);
		// store stamps
		ArrayList<Double> t = caller.getTimes();
		ArrayList<String> e = caller.getEvents();
		ArrayList<String> s = caller.getCSAs();

		for(int i=0;i<t.size();i++)
		{
			numbers.add(number);
			times.add(t.get(i));
			events.add(e.get(i));
			stations.add(s.get(i));
		}
		return true;
	}
	
	public int[] getNumbers()
	{
		numbers.trimToSize();
		int[] tmp = new int[numbers.size()];
		for (int i=0; i < numbers.size(); i++)
		{
			tmp[i] = numbers.get(i);
		}
		return tmp;
	}

	public double[] getTimes()
	{
		times.trimToSize();
		double[] tmp = new double[times.size()];
		for (int i=0; i < times.size(); i++)
		{
			tmp[i] = times.get(i);
		}
		return tmp;
	}

	public String[] getEvents()
	{
		String[] tmp = new String[events.size()];
		tmp = events.toArray(tmp);
		return tmp;
	}

	public String[] getStations()
	{
		String[] tmp = new String[stations.size()];
		tmp = stations.toArray(tmp);
		return tmp;
	}
}