package com.paymentech.eis.tools;

import java.util.*;
import java.io.*;

public class Timer implements Runnable, Serializable{

    protected int timeOut;
    protected int noOfTimeouts = 1;
    protected int actualNumber = noOfTimeouts;
    protected transient Thread t = null;

    public Timer(){
        this(1000);
    }

    public Timer(int timeOut){
        this.timeOut = timeOut;
    }

    public int getTimeOut(){
           return timeOut;
    }

    public void setTimeOut(int t){
        timeOut = t;
    }

    public void setNoOfTimeouts(int x){
        if(x > 1){
            noOfTimeouts = x;
		actualNumber = x;
        }
        else{
            System.out.println("Invalid number of timeouts");
            noOfTimeouts = actualNumber = 0;
        }
    }

    public int getNoOfTimeouts(){
        return noOfTimeouts;
    }

    public synchronized void startTimer(){
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }

    public synchronized void stopTimer(){
        if(t != null){
            t.stop();
            t = null;
        }
    }

    public void run(){
        if(t != null){
            for(; actualNumber != 0; actualNumber--){
                try{
                    t.sleep(timeOut);
                }catch(Exception e){
                    System.out.println(e);
                }
                fireOff();
            }
        }

        t = null;
    }


    protected Vector listeners = new Vector();

    public synchronized void addTimerListener(TimerListener l){
        listeners.addElement(l);
    }

    public synchronized void removeTimerListener(TimerListener l){
        listeners.removeElement(l);
    }

    protected void fireOff(){
        TimerEvent te = new TimerEvent(this);
        te.setCount(noOfTimeouts);

		Vector listeners = (Vector) this.listeners.clone();

		for(int i = 0; i < listeners.size(); i++){
			((TimerListener) listeners.elementAt(i)).actionPerformed(te);
		}
    }

	public static void main (String[] args)
	{
		TestListener listener = new TestListener ();
		Timer a = new Timer (2 * 1000);
		Timer b = new Timer (6 * 1000);
		a.addTimerListener (listener);
		b.addTimerListener (listener);

		a.startTimer ();
		b.startTimer ();
		try {
			// Timer a should go off
			Thread.currentThread().sleep (3 * 1000);

			// cancel b timer
			b.stopTimer ();

			// see if timer b goes off
			Thread.currentThread().sleep (4 * 1000);
		}
		catch (InterruptedException e) {
			e.printStackTrace ();
			System.out.println (e.getMessage ());
		}
		
	}
}
