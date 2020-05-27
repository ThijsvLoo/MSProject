package Simulation;

import java.lang.Math;

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class ConsumerSource implements CProcess
{
	/** Eventlist that will be requested to construct events */
	private final CEventList list;
	/** Queue that buffers products for the machine */
	private final ProductAcceptor queue;
	/** Name of the source */
	private final String name;


	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public ConsumerSource(ProductAcceptor q, CEventList l, String n)
	{
		list = l;
		queue = q;
		name = n;
		// put first event in list for initialization
		list.add(this,1,drawInterArrivalTime(0)); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme)
	{
		// show arrival
		//System.out.println("Consumer call arrival at time = " + tme);
		// give arrived product to queue
		Caller p = new ConsumerCaller();
		p.stamp(tme, "Consumer call" ,"created",name);
		queue.handoverCall(p);
		// generate duration

		// Create a new event in the eventlist
		list.add(this,1,tme+drawInterArrivalTime(tme)); //target,type,time
	}
	
	public static double drawRandomExponential(double mean)
	{
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with given mean
		return -mean*Math.log(u);
	}

	private static double drawInterArrivalTime(double tme){
		double lambdaStar = 3.8/60;
		double iat = drawRandomExponential(1.0 / lambdaStar);
		double random = Math.random();
		//System.out.println("lamda/l* " + getLambda(tme+iat)/lambdaStar + " iat " + iat + " rand " + random);
		while(random > getLambda(tme + iat) / lambdaStar){
			iat += drawRandomExponential(1.0 / lambdaStar);
			random = Math.random();
			//System.out.println("lamda/l* " + getLambda(tme+iat)/lambdaStar + " iat " + iat + " rand " + random);
		}
		return iat;
	}

	private static double getLambda(double t){
		return 0.03 * Math.sin(t * Math.PI / 43200 - 3 * Math.PI / 12)  + (1.0/30);
	}
}