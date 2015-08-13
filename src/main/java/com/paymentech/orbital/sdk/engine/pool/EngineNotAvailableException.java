package com.paymentech.orbital.sdk.engine.pool;

/**
 * <p><b>Title:</b> EngineNotAvailableException</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Indicates an engine is not available to the present thread,
 * possibly because the thread still has not released the previously allocated engine.</p>
 */
public class EngineNotAvailableException extends Exception {
  EngineNotAvailableException(String message) {
    super(message);
  }
}
