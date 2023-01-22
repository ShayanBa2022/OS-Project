package com.SR;

import com.SR.Queue.Queue;
import java.util.List;

public class RoundRobin extends BaseScheduler{
	private Queue transferToNextQueue = new Queue<Process>(5);
	
	public Queue getTransferToNextQueue(){
        	return transferToNextQueue;
    	}

    	private void addingNewProcessesToReadyQueue(int counter){
       		while (true){
            		if (!newQueue.isEmpty() && ((Process) newQueue.first()).getArrivalTime() == counter){
                		var process = (Process) newQueue.dequeue();
                		process.setRemainingWorkTime(process.getFirstBurstTime()-1);
                		readyQueue.enqueue(process);
            	} 
		else
                	break;
        	}
    	}

    	private void stopProcessOrEnterIo(int counter, Algorithms algorithm, int timeQuantum){
        	if (!runningQueue.isEmpty()) {
            		if (((Process) runningQueue.first()).getRemainingWorkTime() == 0){
                		var process = (Process) runningQueue.dequeue();

                		switch (process.getRunningProcessStatus()) {
							case CPU1 :{
                        		if (process.getIOTime() == 0){
                            			process.getRunningProcessStatus(ProcessStatus.CPU2);
                           			process.setRemainingWorkTime(process.getSecondBurstTime()-1);
                           			process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                            			process.setCurrentQuantum(1); // because we count from 0 in our main loop
                            			runningQueue.enqueue(process);
                        		} 
					else{
                        			process.setRunningProcessStatus(ProcessStatus.IO);
                        			process.setFinishTime(counter + process.getIOTime());
                            			process.setLastUsed(counter + process.getIOTime());
                            			ioQueue.enqueue(process);
                        		}
                        		break;
                    		}
							case CPU2 :{
                        		process.setRunningProcessStatus(ProcessStatus.FINISHED);
                        		process.setEndTime(counter);
                        		zombieQueue.enqueue(process);
                        		break;
                    		}
                    		default -> throw new IllegalArgumentException("process is not in not running in burst mode");
                		}

            		}

            		else {
                		if (((Process) runningQueue.first()).getCurrentQuantum() == timeQuantum) {
                    			var process = (Process) runningQueue.dequeue();
                    			process.setRemainingWorkTime(process.getRemainingWorkTime() - 1);
                    			process.setLastUsed(counter);
                    			process.setCurrentQuantum(1);

                    			if (algorithm.equals(Algorithms.RR)) readyQueue.enqueue(process);
                    			else if (algorithm.equals(Algorithms.MLFQ)) transferToNextQueue.enqueue(process);
                		} 
				else{
                    			var process = (Process) runningQueue.first();
                   			process.setCurrentQuantum(process.getCurrentQuantum()+1);
                    			process.setRemainingWorkTime(process.getRemainingWorkTime() - 1);
                		}
            		}
        	}
    	}

	private void checkIfIoIsFinished(int counter) {
        	while (true){
            		if (!ioQueue.isEmpty() && ((Process) ioQueue.first()).getFinishTime() == counter){
                		var process = (Process) ioQueue.dequeue();

                		if (process.getRunningProcessStatus().equals(ProcessStatus.IO)){
                    			process.setRemainingWorkTime(process.getSecondBurstTime()-1);
                    			readyQueue.enqueue(process);
                		}
            		}
            		else
                		break;
        	}
    	}

	private void checkWhichCpuBurstTimeToRun(int counter) {

        	if (runningQueue.isEmpty() && !readyQueue.isEmpty()) {
            		var process = (Process) readyQueue.dequeue();

            		switch (process.getRunningProcessStatus()) {
						case NEW: {
                    			if (process.getFirstBurstTime() == 0) {
                        			process.setRunningProcessStatus(ProcessStatus.IO);
                        			process.setFinishTime(counter + process.getIOTime());
                        			process.setLastUsed(counter + process.getIOTime());
                        			process.setStartTime(counter);
                        			process.setResponseTime(counter - process.getArrivalTime());
                        			process.setWaitingTime(counter - process.getArrivalTime());
                        			ioQueue.enqueue(process);
                    			} 
					else {
                        			process.setRunningProcessStatus(ProcessStatus.CPU1);
                        			process.setStartTime(counter);
                        			process.setRemainingWorkTime(process.getFirstBurstTime()-1);
                        			process.setResponseTime(counter - process.getArrivalTime());
                        			process.setWaitingTime(counter - process.getArrivalTime());
                        			process.setCurrentQuantum(1); // because we count from 0 in our main loop
                        			runningQueue.enqueue(process);
                    			}
                    		break;
                		}

						case IO: {
                    			if (process.getSecondBurstTime() == 0){
                        			process.setRunningProcessStatus(ProcessStatus.FINISHED);
                        			process.setEndTime(counter);
                        			zombieQueue.enqueue(process);
                    			} 
					else {
                        			process.setRunningProcessStatus(ProcessStatus.CPU2);
                        			process.setRemainingWorkTime(process.getSecondBurstTime()-1);
                        			process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                        			process.setCurrentQuantum(1); // because we count from 0 in our main loop
                        			runningQueue.enqueue(process);
                    			}
                    			break;
                		}

						case CPU1,CPU2: {
                    			process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                    			process.setCurrentQuantum(1); // because we count from 0 in our main loop
                    			runningQueue.enqueue(process);
                    			break;
                		}

                		default -> throw new IllegalArgumentException("process not in the right situation to run");
            		}
        	}
    	}
	
	public Queue<Process> schedule(List<Process> processes, int timeQuantum, Algorithms algorithm, int counter) {
        	Process.sortByArrivalTime(processes);

        	for (Process process : processes) {
            		newQueue.enqueue(process);
        	}


        	while (true) {
            		// 1. adding to ready queue
            		addingNewProcessesToReadyQueue(counter);

            		// 2. stopping process or entering IO
            		stopProcessOrEnterIo(counter, algorithm, timeQuantum);

            		// 3. checking if we need to put it on ready queue
            		checkIfIoIsFinished(counter);

            		// 4. checking to run which CPU burst time
            		checkWhichCpuBurstTimeToRun(counter);

            		// 5. Double check for zero inputs in each iteration
            		checkIfIoIsFinished(counter);
            		checkWhichCpuBurstTimeToRun(counter);

            		// 6. check if we have a running process or not
            		if (runningQueue.isEmpty()) idleTime++;

         
   			counter++;

            		// check if all processes are finished
            		if (zombieQueue(counter))
                		break;
        	}

        	return finishedQueue;
    	}
}
