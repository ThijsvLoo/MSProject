package Simulation;

import java.util.ArrayList;
/**
 *	Product that is send trough the system
 *	@author Joel Karel
 *	@version %I%, %G%
 */
abstract class Caller
{
	/** Stamps for the products */
	protected ArrayList<Double> times;
	protected ArrayList<String> events;
	protected ArrayList<String> CSAs;

	/** 
	*	Constructor for the Consumer
	*	Mark the time at which it is created
	//*	@param create The current time
	*/
	public Caller()
	{
		times = new ArrayList<>();
		events = new ArrayList<>();
		CSAs = new ArrayList<>();
	}
	
	
	public void stamp(double time,String type, String event,String CSA)
	{
		times.add(time);
		events.add(event);
		CSAs.add(CSA);
		System.out.println(type + " " + event + " at time = " + time + " by: " + CSA);
	}
	
	public ArrayList<Double> getTimes()
	{
		return times;
	}

	public ArrayList<String> getEvents()
	{
		return events;
	}

	public ArrayList<String> getCSAs()
	{
		return CSAs;
	}
	
	public double[] getTimesAsArray()
	{
		times.trimToSize();
		double[] tmp = new double[times.size()];
		for (int i=0; i < times.size(); i++)
		{
			tmp[i] = times.get(i);
		}
		return tmp;
	}

	public String[] getEventsAsArray()
	{
		String[] tmp = new String[events.size()];
		tmp = events.toArray(tmp);
		return tmp;
	}

	public String[] getStationsAsArray()
	{
		String[] tmp = new String[CSAs.size()];
		tmp = CSAs.toArray(tmp);
		return tmp;
	}
}
