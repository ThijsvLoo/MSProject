package Simulation;

import java.util.ArrayList;

/**
 *	Queue that stores products until they can be handled on a machine machine
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Queue implements ProductAcceptor
{
	/** Lists in which the callers are kept */
	private ArrayList<Caller> ConsumerCallers;
	private ArrayList<Caller> CorporateCallers;
	/** Requests from machine that will be handling the products */
	private ArrayList<CSA> consumerCSARequests;
	private ArrayList<CSA> corporateCSARequests;

	/**
	*	Initializes the queue and introduces a dummy machine
	*	the machine has to be specified later
	*/
	public Queue()
	{
		ConsumerCallers = new ArrayList<>();
		CorporateCallers = new ArrayList<>();
		consumerCSARequests = new ArrayList<>();
		corporateCSARequests = new ArrayList<>();
	}
	
	/**
	*	Asks a queue to give a product to a machine
	*	True is returned if a product could be delivered; false if the request is queued
	* 	Policy is hardcoded to be: if corporate CSA requests a caller and there are no corporate callers, then hand them a consumer caller
	*/
	public boolean askCaller(CSA csa)
	{
		// This is only possible with a non-empty queue
		if(CorporateCallers.size()>0 && csa instanceof CorporateCSA)
		{
			// If the machine accepts the product
			if(csa.handoverCall(CorporateCallers.get(0)))
			{
				CorporateCallers.remove(0);// Remove it from the queue
				return true;
			}
			else
				return false; // Machine rejected; don't queue request
		}
		else if(ConsumerCallers.size()>0)
		{
			// If the machine accepts the product
			if(csa.handoverCall(ConsumerCallers.get(0)))
			{
				ConsumerCallers.remove(0);// Remove it from the queue
				return true;
			}
			else
				return false; // Machine rejected; don't queue request
		}
		else
		{
			if(csa instanceof CorporateCSA) {
				corporateCSARequests.add(csa);
			}
			else
			{
				consumerCSARequests.add(csa);
			}
			return false; // queue request
		}
	}
	
	/**
	*	Offer a caller to the queue
	*	It is investigated whether a CSA wants the product, otherwise it is stored
	*/
	public boolean handoverCall(Caller caller)
	{
		boolean isCorporateCaller = caller instanceof CorporateCaller;
		// Check if the machine accepts it
		if(isCorporateCaller)
		{
			if(corporateCSARequests.size() < 1)
			{
				CorporateCallers.add(caller);
			}
			else
			{
				boolean delivered = false;
				while(!delivered & (corporateCSARequests.size()>0))
				{
					delivered= corporateCSARequests.get(0).handoverCall(caller);
					// remove the request regardless of whether or not the call has been accepted
					corporateCSARequests.remove(0);
				}
				if(!delivered)
				{
					CorporateCallers.add(caller); // Otherwise store it
				}
			}
		}
		else
		{
			if(consumerCSARequests.size()<1 && corporateCSARequests.size()<1)
			{
				ConsumerCallers.add(caller);
			}
			else if(consumerCSARequests.size()>0)
			{
				boolean delivered = false;
				while(!delivered & (consumerCSARequests.size()>0))
				{
					delivered=consumerCSARequests.get(0).handoverCall(caller);
					// remove the request regardless of whether or not the product has been accepted
					consumerCSARequests.remove(0);
				}
				if(!delivered){
					ConsumerCallers.add(caller); // Otherwise store it
				}
			}
			else
			{
				boolean delivered = false;
				while(!delivered & (corporateCSARequests.size()>0))
				{
					delivered= corporateCSARequests.get(0).handoverCall(caller);
					// remove the request regardless of whether or not the call has been accepted
					corporateCSARequests.remove(0);
				}
				if(!delivered)
				{
					ConsumerCallers.add(caller); // Otherwise store it
				}
			}
		}
		return true;
	}

}