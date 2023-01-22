package com.SR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.SR.Queue.Queue;

public class Process {
    private int processID;
    private int arrivalTime;
    private int firstBurstTime;
    private int IOTime;
    private int secondBurstTime;
    //-------------------------------------------------------------------------
    private int waitingTime;
    private int responseTime;
    private int startTime;
    private int endTime;
    //-------------------------------------------------------------------------
    private ProcessStatus runningProcessStatus;
    private int remainingWorkTime;
    private int currentQuantum;
    private int finishTime;
    private int lastUsed;
    //-------------------------------------------------------------------------
    public Process(int processID, int arrivalTime, int firstBurstTime, int IOTime, int secondBurstTime, ProcessStatus runningProcessStatus){
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.firstBurstTime = firstBurstTime;
        this.IOTime = IOTime;
        this.secondBurstTime = secondBurstTime;
        this.runningProcessStatus = runningProcessStatus;
    }
    public static void sortByArrivalTime(List<Process> processes){
        processes.sort(new Comparator<Process>(){
            @Override
            public int compare(Process p1, Process p2){
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
    }
    public static void sortByRemainingTime(Queue<Process> readyQueue){
        var processes = new ArrayList<Process>();

        while(!readyQueue.isEmpty())
            processes.add(readyQueue.dequeue());
        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2){
                return Integer.compare(p1.getRemainingWorkTime(),p2.getRemainingWorkTime());
                }
            });
            while(!processes.isEmpty())
                readyQueue.enqueue(processes.remove(0));
        }
        @Override
        public String toString() {
            return "Process{" + "processID=" + processID + ", responseTime=" + responseTime + ", turnAroundTime=" + endTime + ", waitingTime=" + waitingTime + '}';
        }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getFirstBurstTime() {
        return firstBurstTime;
    }

    public void setFirstBurstTime(int firstBurstTime) {
        this.firstBurstTime = firstBurstTime;
    }

    public int getIOTime() {
        return IOTime;
    }

    public void setIOTime(int IOTime) {
        this.IOTime = IOTime;
    }

    public int getSecondBurstTime() {
        return secondBurstTime;
    }

    public void setSecondBurstTime(int secondBurstTime) {
        this.secondBurstTime = secondBurstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public ProcessStatus getRunningProcessStatus() {
        return runningProcessStatus;
    }

    public void setRunningProcessStatus(ProcessStatus runningProcessStatus) {
        this.runningProcessStatus = runningProcessStatus;
    }

    public int getRemainingWorkTime() {
        return remainingWorkTime;
    }

    public void setRemainingWorkTime(int remainingWorkTime) {
        this.remainingWorkTime = remainingWorkTime;
    }

    public int getCurrentQuantum() {
        return currentQuantum;
    }

    public void setCurrentQuantum(int currentQuantum) {
        this.currentQuantum = currentQuantum;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(int lastUsed) {
        this.lastUsed = lastUsed;
    }
}
