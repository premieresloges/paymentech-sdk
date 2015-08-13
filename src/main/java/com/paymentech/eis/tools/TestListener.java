package com.paymentech.eis.tools;

class TestListener implements TimerListener {
  public void actionPerformed(TimerEvent e) {
    System.out.println(Thread.currentThread().getName() +
        ", in actionPerformed: " + e.getCount());
  }
}
