/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web.component;

import java.io.IOException;

import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;

/***
 * This class mocks Picto Web server.
 * 
 * @author Alfa Yohannis
 *
 */
public class Server {

  private Thread pictoAppThread;

  public void start() throws InterruptedException {
    String[] args = new String[] {};
    pictoAppThread = new Thread() {
      @Override
      public void run() {
        try {
          PictoApplication.main(args);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    PictoApplication.setPictoWebOnLoadedListener(new PictoWebOnLoadedListener() {
      @Override
      public void onLoaded() {
        if (args != null) {
          synchronized (args) {
            args.notify();
          }
        }
      }
      // ---
    });
    synchronized (args) {
      pictoAppThread.start();
      args.wait();
    }
  }

  public void stop() throws IOException, InterruptedException {
    PictoApplication.exit();
  }
}