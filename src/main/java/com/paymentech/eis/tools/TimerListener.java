package com.paymentech.eis.tools;

import java.util.EventListener;

public interface TimerListener extends EventListener {
  public void actionPerformed(TimerEvent e);
}
