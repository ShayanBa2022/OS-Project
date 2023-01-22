package com.SR;
import com.SR.Queue.Queue;

import java.util.List;

public class FCFS extends BaseScheduler {
    private void addNewProcessToReadyQueue(int counter, Algorithms algorithm) {
        while (true) {
            if (!newQueue.isEmpty() && ((Process) newQueue.first()).getArrivalTime() == counter) {
                var process = (Process) newQueue.dequeue();

                if (algorithm.equals(Algorithms.SJF)) // for SJF
                    process.setRemainingWorkTime(process.getFirstBurstTime());

                readyQueue.enqueue(process);
            } else
                break;
        }
    }
    private void stopProcessOrEnterIo(int counter) {
        if (!runningQueue.isEmpty()) {
            if (((Process) runningQueue.first()).getFinishTime() == counter) {
                var process = (Process) runningQueue.dequeue();

                switch (process.getRunningProcessStatus()) {
                    case CPU1: {
                        if (process.getIOTime() == 0){
                            process.setRunningProcessStatus(ProcessStatus.CPU2);
                            process.setFinishTime(counter + process.getSecondBurstTime());
                            process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                            runningQueue.enqueue(process);
                        } else {
                            process.setRunningProcessStatus(ProcessStatus.IO);
                            process.setFinishTime(counter + process.getIOTime());
                            process.setLastUsed(counter + process.getIOTime());
                            ioQueue.enqueue(process);
                        }
                        break;
                    }
                    case CPU2: {
                        process.setRunningProcessStatus(ProcessStatus.FINISHED);
                        process.setEndTime(counter);
                        zombieQueue.enqueue(process);
                        break;
                    }
                    default: throw new IllegalArgumentException("process is not in not running in burst mode");
                }
            }
        }
    }
    private void checkIfIoIsFinished(int counter, Algorithms algorithm) {
        while (true) {
            if (!ioQueue.isEmpty() && ((Process) ioQueue.first()).getFinishTime() == counter) {
                var process = (Process) ioQueue.dequeue();

                if (process.getRunningProcessStatus().equals(ProcessStatus.IO)) {

                    if (algorithm.equals(Algorithms.SJF)) // for SJF
                        process.setRemainingWorkTime(process.getSecondBurstTime());

                    readyQueue.enqueue(process);
                }
            } else
                break;
        }
    }
    private void checkWhichCpuBurstTimeToRun(int counter, Algorithms algorithm) {
        // if SJF -> we should order the queue based on minimum remaining time for each process
        if (readyQueue.size() > 1 && algorithm.equals(Algorithms.SJF))
            Process.sortByRemainingTime(readyQueue);

        if (runningQueue.isEmpty() && !readyQueue.isEmpty()) {
            var process = (Process) readyQueue.dequeue();

            switch (process.getRunningProcessStatus()) {
                case NEW: {
                    if (process.getFirstBurstTime() == 0){
                        process.setRunningProcessStatus(ProcessStatus.IO);
                        process.setStartTime(counter);
                        process.setFinishTime(counter + process.getIOTime());
                        process.setLastUsed(counter + process.getIOTime());
                        ioQueue.enqueue(process);
                    } else {
                        process.setRunningProcessStatus(ProcessStatus.CPU1);
                        process.setStartTime(counter);
                        process.setFinishTime(counter + process.getFirstBurstTime());
                        process.setResponseTime(counter - process.getArrivalTime());
                        process.setWaitingTime(counter - process.getArrivalTime());
                        runningQueue.enqueue(process);
                    }
                    break;
                }

                case IO:{
                    process.setRunningProcessStatus(ProcessStatus.CPU2);
                    process.setFinishTime(counter + process.getSecondBurstTime());
                    process.setWaitingTime(process.getWaitingTime() + counter - process.getLastUsed());
                    runningQueue.enqueue(process);
                    break;
                }

                default: throw new IllegalArgumentException("process not in a situation to be in CPU burst 1 or 2");
            }
        }
    }

    public Queue<Process> schedule(List<Process> processes, Algorithms algorithm, int counter) {
        Process.sortByRemainingTime((Queue<Process>) processes);

        for (Process process : processes) {
            newQueue.enqueue(process);
        }

        while (true) {
            // 1. adding to ready queue
            addNewProcessToReadyQueue(counter, algorithm);

            // 2. stopping process or entering IO
            stopProcessOrEnterIo(counter);

            // 3. checking if we need to put it on ready queue
            checkIfIoIsFinished(counter, algorithm);

            // 4. checking to run which CPU burst time
            checkWhichCpuBurstTimeToRun(counter, algorithm);

            // 5. Double check for zero inputs in each iteration
            checkIfIoIsFinished(counter, algorithm);
            stopProcessOrEnterIo(counter);

            // 6. check if we have a running process or not
            if (runningQueue.isEmpty())
                idleTime++;

            counter++;

            // check if all processes are finished
            if (allProcessesAreFinished(counter))
                break;
        }

        return zombieQueue;
    }
}
