package org.eclipse.epsilon.picto.web.test;

import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

public class Dummy {

  public static void main(String[] args) {
    EObjectImpl eObject = new DynamicEObjectImpl();
    if (args.length > 0) {
      System.out.println("HAHAHAHAH " + args[0]);
    }
    else {
      System.out.println("No parameters " + "HAHAHAHAH");
    }
  }

}