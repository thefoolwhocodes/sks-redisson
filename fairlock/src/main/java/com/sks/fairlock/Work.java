/**
 * Work class that denotes a task.
 * Before updating the state of the object, a Redis distributed  lock needs to be taken by the application.
 * At any point of time _incrementOperationsCounter - _decrementOperationsCounter = _workValue.
 */
package com.sks.fairlock;

public class Work {
    private int _workId;
    private int _workValue;
    private int _incrementOperationsCounter;
    private int _decrementOperationsCounter;

    Work() {}

    Work(int id) {
        _workId = id;
        _workValue = 0;
        _incrementOperationsCounter = 0;
        _decrementOperationsCounter = 0;
    }

    public void incrementResource()
    {
        ++_workValue;
        ++_incrementOperationsCounter;
    }

    public void decrementResource()
    {
        --_workValue;
        ++_decrementOperationsCounter;
    }

    public int getId()
    {
        return _workId;
    }

    public void displayState(String threadName)
    {
        System.out.println(String.format("    %s: Work Id is: %d ",threadName,_workId));
        System.out.println(String.format("    %s: Total increment operations: %d ",threadName,_incrementOperationsCounter));
        System.out.println(String.format("    %s: Total decrement operations: %d",threadName,_decrementOperationsCounter));
        System.out.println(String.format("    %s: Resource values: %d",threadName,_workValue));
        System.out.println(String.format("    %s: Operations differences values: %d",threadName,(_incrementOperationsCounter - _decrementOperationsCounter) ));
    }
}
