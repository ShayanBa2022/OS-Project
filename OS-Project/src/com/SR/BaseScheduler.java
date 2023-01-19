package com.SR;

public abstract class BaseScheduler {
    /*We use *protected* access modifier to make sure it's unreachable outside the package*/
    protected int totalTime;
    protected int idleTime;
    //-----------------------------------------------------------------------------------------
    protected Queue newQueue = new Queue<Process>(5);
    protected Queue readyQueue = new Queue<Process>(5);
    protected Queue runningQueue = new Queue<Process>(5);
    protected Queue ioQueue = new Queue<Process>(5);
    protected Queue zombieQueue = new Queue<Process>(5);
    //-----------------------------------------------------------------------------------------
    protected boolean allProcessesAreFinished(int counter){
        // checking queues to be empty
        boolean resault = readyQueue.isEmpty() && runningQueue.isEmpty() && ioQueue.isEmpty() && newQueue.isEmpty();
        if (resault){
            //counter has a 1 sec extra count, so for total time and idle time we have to remove this extra sec
            totalTime = --counter;
            idleTime = --counter;
        }
        return resault;
    }
    //-----------------------------------------------------------------------------------------

    // initializing getters
    public int getTotalTime() {
        return totalTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public Queue getNewQueue() {
        return newQueue;
    }
}
