package Simulation;

import java.sql.SQLOutput;

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class CorporateSource implements CProcess
{
	/** Eventlist that will be requested to construct events */
	private CEventList list;
	/** Queue that buffers products for the machine */
	private ProductAcceptor queue;
	/** Name of the source */
	private String name;
	/** Mean interarrival time */
	private double meanArrTime;
	/** Interarrival times (in case pre-specified) */
	private double[] interarrivalTimes;
	/** Interarrival time iterator */
	private int interArrCnt;

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public CorporateSource(ProductAcceptor q, CEventList l, String n)
	{
		list = l;
		queue = q;
		name = n;
		meanArrTime=33;
		// put first event in list for initialization
		list.add(this,0,drawRandomExponential(meanArrTime)); //target,type,time
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with specified mean
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param m	Mean arrival time
	*/
	public CorporateSource(ProductAcceptor q, CEventList l, String n, double m)
	{
		list = l;
		queue = q;
		name = n;
		meanArrTime=m;
		// put first event in list for initialization
		list.add(this,0,drawRandomExponential(meanArrTime)); //target,type,time
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are prespecified
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param ia	interarrival times
	*/
	public CorporateSource(ProductAcceptor q, CEventList l, String n, double[] ia)
	{
		list = l;
		queue = q;
		name = n;
		meanArrTime=-1;
		interarrivalTimes=ia;
		interArrCnt=0;
		// put first event in list for initialization
		list.add(this,0,interarrivalTimes[0]); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Call arrival at time = " + tme);
		// give arrived product to queue
		Caller p = new CorporateCaller();
		p.stamp(tme,"Creation",name);
		queue.handoverCall(p);
		// generate duration
		if(meanArrTime>0)
		{
			double duration = drawInterArrivalTime(tme);
			// Create a new event in the eventlist
			list.add(this,0,tme+duration); //target,type,time
		}
		else
		{
			interArrCnt++;
			if(interarrivalTimes.length>interArrCnt)
			{
				list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
			}
			else
			{
				list.stop();
			}
		}
	}
	
	public static double drawRandomExponential(double mean)
	{
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with mean 33
		double res = -mean*Math.log(u);
		return res;
	}

	private static double drawInterArrivalTime(double tme){
		double lambdaStar = 1.0/60;
		double iat = drawRandomExponential(1.0 / lambdaStar);
		double random = Math.random();
		while(random <= getlambda(tme + iat) / lambdaStar){

			iat += drawRandomExponential(1.0 / lambdaStar);
			random = Math.random();
		}
		return iat;
	}

	private static double getlambda(double t){
		double lambda = 0;

		if(t % 86400 < 3600 * 10) {
			lambda = 1.0 / 60;
		} else if(t % 86400 < 3600 * 14) {
			lambda = 0.4 / 60;
		} else if(t % 86400 < 3600 * 24) {
			lambda = 0.1 / 60;
		}

		return lambda;
	}
}