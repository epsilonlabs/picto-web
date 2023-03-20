/*********************************************************************
* Copyright (c) 2023 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* @author Alfa Yohannis
**********************************************************************/

/**
 * 
 */
package org.eclipse.epsilon.picto.pictograph;

/**
 * @author Alfa Yohannis
 *
 */
public abstract class Entity {
  protected String name;
  protected org.eclipse.epsilon.picto.pictograph.State state;
  protected byte[] hash;
  protected int accessCount;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public org.eclipse.epsilon.picto.pictograph.State getState() {
    return state;
  }

  public void setState(org.eclipse.epsilon.picto.pictograph.State state) {
    this.state = state;
  }

  public byte[] getHash() {
    return hash;
  }

  public void setHash(byte[] hash) {
    this.hash = hash;
  }

  public int getAccessCount() {
    return accessCount;
  }

  public void setAccessCount(int accessCount) {
    this.accessCount = accessCount;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "-" + this.getName();
  }
}
