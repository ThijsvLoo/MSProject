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
        Queue queue = new Queue();
        // A Corporate and a consumer source
        new ConsumerSource(queue,eventList,"Consumer source 1");
        new CorporateSource(queue,eventList,"Corporate source 1");
        // A sink
        Sink sink = new Sink("Sink 1");
        // A machine
        for(int i = 0; i < 2; i++){
            new ConsumerCSA(queue,sink,eventList,"Cons CSA " + (i + 1));
        }
        for(int i = 0; i < 2; i++){
            new CorporateCSA(queue,sink,eventList,"Corp CSA " + (i + 1));
        }



        // start the eventlist
        eventList.start(86400); // 86400 is one day in seconds and is the maximum time, time 0 is midnight, start of the first shift

        // Export to excel file
        Exporter.writeData();
    }
    
}
