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
	protected final CEventList eventList;
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

	protected boolean stopFlag;

	/** stopFlag indicates CSA should stop because his shift is  over*/

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param sink	Where to send the completed products
	*	@param eventList	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public CSA(Queue q, ProductAcceptor sink, CEventList eventList, String n)
	{
		status = 'i';
		queue = q;
		this.sink = sink;
		this.eventList = eventList;
		name = n;
		//meanProcTime=30;
		queue.askCaller(this);
		stopFlag = false;
	}

	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme)
	{
		// show arrival
		//System.out.println("Call finished at time = " + tme + " by: " + name);
		// Remove product from system
		caller.stamp(tme, "Call", "finished",name);
		sink.handoverCall(caller);
		caller =null;

		if(stopFlag){
			status = 's';
		} else {
			// set machine status to idle
			status='i';
			// Ask the queue for products
			queue.askCaller(this);
		}
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean handoverCall(Caller p)
	{
		// Only accept something if the machine is idle
		if(status=='i')
		{
			// accept the product
			caller =p;
			// mark starting time
			caller.stamp(eventList.getTime(), "Call", "accepted",name);

			//send waiting time data to output for analysis
			int createdIndex = caller.getEvents().lastIndexOf("created");
			int acceptedIndex = caller.getEvents().lastIndexOf("accepted");
			if(caller instanceof ConsumerCaller){
				Exporter.addConsumer(caller.getTimes().get(acceptedIndex) - caller.getTimes().get(createdIndex));
			} else {
				Exporter.addCorporate(caller.getTimes().get(acceptedIndex) - caller.getTimes().get(createdIndex));
			}

			// start production
			HandleCall();
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
	protected void HandleCall()
	{
		// generate duration
		double duration = drawRandomNormal(meanCallTime, sdCallTime, minCallDuration);
		// Create a new event in the eventlist
		double tme = eventList.getTime();
		eventList.add(this,0,tme+duration); //target,type,time
		// set status to busy
		status='b';
//		if(meanCallTime >0)
//		{
//
//		}
//		else
//		{
//			if(processingTimes.length > callCnt)
//			{
//				eventList.add(this,0, eventList.getTime() + processingTimes[callCnt]); //target,type,time
//				// set status to busy
//				status='b';
//				callCnt++;
//			}
//			else
//			{
//				eventList.stop();
//			}
//		}
	}

	public static double drawRandomExponential(double mean)
	{
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with given mean
		return -mean*Math.log(u);
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

	public void stop(){ //make the csa not accept any more calls, but can finish their current call if still ongoing
		stopFlag = true;
		status = 's';
	}
}