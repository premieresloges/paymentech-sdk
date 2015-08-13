package com.paymentech.eis.tools.concurrency;

/**
 * A PCQNode. Contains an object and a next pointer.
 */
final class PCQNode { // local node class for queue
  Object value;
  PCQNode next;

  PCQNode(Object x, PCQNode n) {
    value = x;
    next = n;
  }
}

/**
 * Sets up a queue
 */
public class PCQueue {
  /**
   * pointer to dummy header node
   */
  private PCQNode head_;
  /**
   * pointer to last node
   */
  private PCQNode last_;
  /**
   * protect access to last
   */
  private Object lastLock_;
  /**
   * number of nodes in the queue
   */
  private int m_count;
  /**
   * processing flag
   */
  private boolean m_bStopProcessing = false;


  /**
   * Constructor to create an empty queue
   */
  public PCQueue() {
    head_ = last_ = new PCQNode(null, null);
    lastLock_ = new Object();
    m_count = 0;
  }

  /**
   * Pushes an object into a node and inserts it at the end of the queue.
   *
   * @param x The object to insert into the queue.
   */
  public synchronized void push(Object x) {
    PCQNode node = new PCQNode(x, null);

    // insert at end of list
    last_.next = node;
    last_ = node;
    m_count++;

    notify();
  }

  /**
   * Pulls a node off of the queue
   *
   * @returns the first node in the queue
   */
  public synchronized Object pull() {
    // If queue is empty, waits until it is not empty

    Object x = null; // return value

    while (!m_bStopProcessing && size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    } // wait for the queue to get at least one element

    PCQNode first = head_.next; // first real node is after head

    // Another thread might have shut us down while waiting for Q to fill up
    if (first != null) {
      x = first.value;
      head_ = first; // old first becomes new head
      m_count--;
    }
    return x;
  }

  /**
   * return the number of elements in this queue
   */
  public int size() {
    return m_count;
  }

  /**
   * Mutator to allow lineHandler to interrupt the wait
   */
  public void stopProcessing() {
    m_bStopProcessing = true;
  }
}

