package nachos.threads;

import nachos.machine.*;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 {
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    public Condition2(Lock conditionLock) {
	this.conditionLock = conditionLock;
 
    }
    
    

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */

    public void sleep() {
        Lib.assertTrue(conditionLock.isHeldByCurrentThread());
        conditionLock.release();// Release lock
        boolean tmp = Machine.interrupt().disable();// Disable interrupts 
	//waitQueue.waitForAccess(currentThread); //wait for thread access
        waitQueue.add(KThread.currentThread()); // Add currentThread to waitQueue
        KThread.currentThread().sleep(); // Put currentThread to sleep
        Machine.interrupt().restore(tmp);// Restore interrupts
        conditionLock.acquire();// Acquire lock
    }
    /**
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() {
        Lib.assertTrue(conditionLock.isHeldByCurrentThread());
        boolean tmp = Machine.interrupt().disable();/// Disable interrupts
	//if (waitQueue != null) //If waitQueue is not empty
        if(!waitQueue.isEmpty()){ // If waitQueue is not empty
            KThread x = waitQueue.removeFirst();// Get the first Thread in wait Queue
		//KThread a = waitQueue.nextThread(); //get next thread
            x.ready(); // Ready the thread
        }
        Machine.interrupt().restore(tmp);// Restore interrupts
    }

    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    
    public void wakeAll() {
        Lib.assertTrue(conditionLock.isHeldByCurrentThread());
	// While(!waitQueue.isEmpty()){ // If waitQueue is not empty
        while(waitQueue != null)// While waitQueue is not empty
		//KThread tmp = waitQueue.nextThread();
            wake(); // Wake all asleep threads
    }


    private LinkedList<KThread> waitQueue = new LinkedList<KThread>(); // A link-list to store all the thread that are asleep
    private Lock conditionLock;
}
