/* ResourceManager.java		Defines the ResourceManager class which is 
 * responsible for throttling access to a group of IResource instances, 
 * allowing a resource to be used by at most one thread at a time.   
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/ResourceManager.java-arc   1.1   Feb 07 2007 08:03:04   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:04  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/ResourceManager.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:04   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:38   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 2     4/02/01 4:27p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration
package com.paymentech.eis.tools.resource;

// Standard Java imports

import com.paymentech.eis.tools.Debug;

import java.util.Hashtable;
import java.util.Stack;

// Eis Imports

/**
 * ResourceManager - Throttles access to a set of IResources, ensuring that
 * a thread can acquire at most one IResource instance.
 *
 * @author jpalmiero
 * @version $Revision: 1.1  $
 */
public class ResourceManager {
  int m_numResources;

  //-----------------------------------------------------
  // Instance altering methods
  //-----------------------------------------------------
  //--------------------------------------------------
  // Instance variables
  //--------------------------------------------------
  private IResource[] m_resources;  // The resources to manage
  private Stack m_availableResources;  // Container for available resources
  /* Maps the thread-id to the index into the resource array
   * of the resource in-use.
  */
  private Hashtable m_occupiedResources;
  /**
   * Constructor initializes the ResourceManager with an array of IResource
   * instances to manage.
   *
   * @params resourcesToManage    The array of IResource's to manage.
   */
  public ResourceManager(IResource[] resourcesToManage) {
    m_resources = resourcesToManage;    // These are our resources
    m_numResources = m_resources.length;  // This is how many we have
    m_availableResources = new Stack();  // Container for available ones

    // Container for mapping resource users to the resources
    m_occupiedResources = new Hashtable(m_numResources);

    // All of the resources are initailly available
    for (short i = 0; i < m_resources.length; i++)
      m_availableResources.push(new Short(i));
  }

  /**
   * acquire		Returns an available IResource instance, which is notified
   * of its impending acquisition via its resourceAcquired() method.
   * The method will block until an IResource becomes available. The
   * method contains synchronization to ensure serial access to the
   * resource manager internals.  A thread of execution is only allowed to
   * acquire one resource at a time.  If a thread attempts to acquire a
   * second resource, then a ResourceNotAvailable exception is thrown.
   *
   * @throws ResourceNotAvailable
   */
  public IResource acquire() throws ResourceNotAvailable {
    IResource resource = null;

    // Identify the current thread
    String threadId = Thread.currentThread().getName();

    // For debugging
    Debug.trace_debug(threadId, "inside acquire()");

    // Do we have any resources available?
    synchronized (this) {
      // Make sure this thread hasn't already acquired something
      if (m_occupiedResources.containsKey(threadId))
        throw new ResourceNotAvailable("Thread [" + threadId +
            "] has already acquired a resource from this manager.");

      // This blocks until something is available.
      while (m_availableResources.empty()) // We have to wait
        try {
          Debug.trace_debug(threadId, "zzzz");
          wait();
        } catch (Exception e) {
          throw new ResourceNotAvailable("Thread [" + threadId +
              "] could not obtain resource.");
        }

      Short availableIndex = (Short) m_availableResources.pop();
      resource = m_resources[availableIndex.intValue()];

      // Associate the threadId to the resource index.
      m_occupiedResources.put(threadId, availableIndex);
    }

		/* Tell the resource it's about to be acquired by someone.
    */
    resource.resourceAcquired();

    return resource;
  }

  /**
   * This method makes available the resource previously acquired by the
   * calling thread.
   */
  public void release() {
    // Identify the current thread
    String threadId = Thread.currentThread().getName();

		/* Acquire object lock only if the calling thread previously
		 * acquired something.
		*/
    if (m_occupiedResources.containsKey(threadId)) {
      synchronized (this) {
        Short resourceIndex = (Short) m_occupiedResources.remove
            (threadId);

        // Mark this resource as 'available'.
        m_availableResources.push(resourceIndex);

				/* Tell the resource that it's about to be released
				 * (not for long!! heh, heh...)
				*/
        m_resources[resourceIndex.intValue()].resourceReleased();

        // Tell the waiting threads that something is available
        notifyAll();
      }
    }
  }
};

