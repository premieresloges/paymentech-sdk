package com.paymentech.orbital.sdk.engine.pool;

import com.paymentech.orbital.sdk.engine.EngineIF;

/**
 * <p><b>Title:</b> EnginePoolIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Interface contract implemented by the engine pool. </p>
 */
public interface EnginePoolIF {
    EngineIF acquire() throws EngineNotAvailableException;

    void release();
}
