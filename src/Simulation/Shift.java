package Simulation;

import java.util.ArrayList;

public class Shift implements CProcess {
    private static CEventList eventList;
    private static Queue queue;
    private static ProductAcceptor sink;
    private ArrayList<CSA> currentCSAs;

    public Shift(CEventList eventList, Queue queue, ProductAcceptor sink){
        this.eventList = eventList;
        this.queue = queue;
        this.sink = sink;
        eventList.add(this,0,0); //target,type,time
        currentCSAs = new ArrayList<CSA>();
    }

    public void execute(int type, double tme){
        queue.clearCSARequests();


        for(CSA csa : currentCSAs){
            csa.stop();
        }
        currentCSAs.clear();


        double timeOfDay = tme % 86400;
        System.out.println("New shift started at: " + timeOfDay);
        if(timeOfDay < 3600 * 6 || timeOfDay >= 3600 * 22) { // night shift times 10pm - 6am
            for(int i = 0; i < 5; i++){
                currentCSAs.add(new CorporateCSA(queue,sink,eventList,"Nightshift Corporate CSA " + (i + 1)));
            }
            for(int i = 0; i < 5; i++){
                currentCSAs.add(new ConsumerCSA(queue,sink,eventList,"Nightshift Consumer CSA " + (i + 1)));
            }

        } else if(timeOfDay < 3600 * 14) { // day shift 6am - 2pm
            for(int i = 0; i < 15; i++){
                currentCSAs.add(new CorporateCSA(queue,sink,eventList,"Dayshift Corporate CSA " + (i + 1)));
            }
            for(int i = 0; i < 15; i++){
                currentCSAs.add(new ConsumerCSA(queue,sink,eventList,"Dayshift Consumer CSA " + (i + 1)));
            }

        } else if(timeOfDay < 3600 * 22) { // afternoon and evening shift 2pm - 10 pm
            for(int i = 0; i < 15; i++){
                currentCSAs.add(new CorporateCSA(queue,sink,eventList,"EveShift Corporate CSA " + (i + 1)));
            }
            for(int i = 0; i < 20; i++){
                currentCSAs.add(new ConsumerCSA(queue,sink,eventList,"EveShift Consumer CSA " + (i + 1)));
            }

        }
        if(tme == 0){
            eventList.add(this,0,21600); //change shift after 6 hours / 21600 minutes, since the simulation starts during the nightshift
        } else {
            eventList.add(this,0,tme + 28800); //change shift again after another 8 hours / 28800 seconds
        }
    }
}
