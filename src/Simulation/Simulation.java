/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

public class Simulation {

    public CEventList list;
    public Queue queue;
    public ConsumerSource consumerSource;
    public Sink sink;
    public ConsumerCSA mach;
	

        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	// Create an eventlist
        CEventList eventList = new CEventList();
        // A queue for the machine
        Queue q = new Queue();
        // A source
        CorporateSource s = new CorporateSource(q,eventList,"Consumer source 1");
        // A sink
        Sink si = new Sink("Sink 1");
        // A machine
        ConsumerCSA m = new ConsumerCSA(q,si,eventList,"Machine 1");
        // start the eventlist
        eventList.start(86400); // 2000 is maximum time
    }
    
}
