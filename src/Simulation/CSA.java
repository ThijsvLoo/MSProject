package Simulation;


import java.util.Random;

/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public abstract class CSA implements CProcess,ProductAcceptor
{
	/** Product that is being handled  */
	protected Caller caller;
	/** Eventlist that will manage events */
	protected final CEventList eventlist;
	/** Queue from which the machine has to take products */
	protected Queue queue;
	/** Sink to dump products */
	protected ProductAcceptor sink;
	/** Status of the machine (b=busy, i=idle) */
	protected char status;
	/** Machine name */
	protected final String name;
	/** Mean call time */
	protected double meanCallTime;
	/** Standard deviation of call time */
	protected double sdCallTime;
	/** minimum duration of call time */
	protected double minCallDuration;
	/** call times (in case pre-specified) */
	protected double[] processingTimes;
	/** call time iterator */
	protected int callCnt;

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public CSA(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		//meanProcTime=30;
		queue.askProduct(this);
	}

//	/**
//	*	Constructor
//	*        Service times are exponentially distributed with specified mean
//	*	@param q	Queue from which the machine has to take products
//	*	@param s	Where to send the completed products
//	*	@param e	Eventlist that will manage events
//	*	@param n	The name of the machine
//	*        @param m	Mean call time
//	*/
//	public CSA(Queue q, ProductAcceptor s, CEventList e, String n, double m)
//	{
//		status='i';
//		queue=q;
//		sink=s;
//		eventlist=e;
//		name=n;
//		meanProcTime=m;
//		queue.askProduct(this);
//	}
//
//	/**
//	*	Constructor
//	*        Service times are pre-specified
//	*	@param q	Queue from which the machine has to take products
//	*	@param s	Where to send the completed products
//	*	@param e	Eventlist that will manage events
//	*	@param n	The name of the machine
//	*        @param st	service times
//	*/
//	public CSA(Queue q, ProductAcceptor s, CEventList e, String n, double[] st)
//	{
//		status='i';
//		queue=q;
//		sink=s;
//		eventlist=e;
//		name=n;
//		meanProcTime=-1;
//		processingTimes=st;
//		procCnt=0;
//		queue.askProduct(this);
//	}

	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Call finished at time = " + tme);
		// Remove product from system
		caller.stamp(tme,"Production complete",name);
		sink.giveProduct(caller);
		caller =null;
		// set machine status to idle
		status='i';
		// Ask the queue for products
		queue.askProduct(this);
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean giveProduct(Caller p)
	{
		// Only accept something if the machine is idle
		if(status=='i')
		{
			// accept the product
			caller =p;
			// mark starting time
			caller.stamp(eventlist.getTime(),"Production started",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else return false;
	}
	
	/**
	*	Starting routine for the production
	*	Start the handling of the current product with an exponentially distributed call time with average 30
	*	This time is placed in the eventlist
	*/
	protected void startProduction()
	{
		// generate duration
		if(meanCallTime >0)
		{
			double duration = drawRandomNormal(meanCallTime, sdCallTime, minCallDuration);
			// Create a new event in the eventlist
			double tme = eventlist.getTime();
			eventlist.add(this,0,tme+duration); //target,type,time
			// set status to busy
			status='b';
		}
		else
		{
			if(processingTimes.length> callCnt)
			{
				eventlist.add(this,0,eventlist.getTime()+processingTimes[callCnt]); //target,type,time
				// set status to busy
				status='b';
				callCnt++;
			}
			else
			{
				eventlist.stop();
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

	public static double drawRandomNormal(double mean, double sd,double minCallTime)
	{
		// draw a [0,1] uniform distributed number
		Random r = new Random();
		// convert it into a normally distributed random variate
		double u = r.nextGaussian();
		double res = u * sd + mean;
		// truncation
		if(res < minCallTime)
		{
			res = minCallTime;
		}
		return res;
	}
}