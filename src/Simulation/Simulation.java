/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

public class Simulation {

    public static CEventList eventList;
    public static Queue queue;
    public static ConsumerSource consumerSource;
    public static Sink sink;
    public static ConsumerCSA mach;
	

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(int i = 0; i < 100; i++){
            // Create an eventlist
            eventList = new CEventList();
            // A queue for the machine
            queue = new Queue();
            // A Corporate and a consumer source
            new ConsumerSource(queue,eventList,"Consumer source 1");
            new CorporateSource(queue,eventList,"Corporate source 1");
            // A sink
            Sink sink = new Sink("Sink 1");

            new Shift(eventList, queue, sink);

            // start the eventlist
            eventList.start(86400); // 86400 is one day in seconds and is the maximum time, time 0 is midnight, which is in the night shift
        }
    }
    
}
