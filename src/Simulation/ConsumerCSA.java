package Simulation;



/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class ConsumerCSA extends CSA implements CProcess,ProductAcceptor
{
		/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public ConsumerCSA(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		super(q,s,e,n);
		meanCallTime = 72;
		minCallDuration = 25;
		sdCallTime = 35;
	}


}