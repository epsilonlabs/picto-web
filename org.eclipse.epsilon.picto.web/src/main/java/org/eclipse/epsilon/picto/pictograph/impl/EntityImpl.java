/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.epsilon.picto.pictograph.Entity;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.State;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl#getState <em>State</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl#getHash <em>Hash</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl#getAccessCount <em>Access Count</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class EntityImpl extends EObjectImpl implements Entity {
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getState() <em>State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getState()
   * @generated
   * @ordered
   */
  protected static final State STATE_EDEFAULT = State.NEW;

  /**
   * The cached value of the '{@link #getState() <em>State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getState()
   * @generated
   * @ordered
   */
  protected State state = STATE_EDEFAULT;

  /**
   * The default value of the '{@link #getHash() <em>Hash</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHash()
   * @generated
   * @ordered
   */
  protected static final String HASH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHash() <em>Hash</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHash()
   * @generated
   * @ordered
   */
  protected String hash = HASH_EDEFAULT;

  /**
   * The default value of the '{@link #getAccessCount() <em>Access Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAccessCount()
   * @generated
   * @ordered
   */
  protected static final int ACCESS_COUNT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getAccessCount() <em>Access Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAccessCount()
   * @generated
   * @ordered
   */
  protected int accessCount = ACCESS_COUNT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EntityImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return PictographPackage.Literals.ENTITY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName) {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.ENTITY__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public State getState() {
    return state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setState(State newState) {
    State oldState = state;
    state = newState == null ? STATE_EDEFAULT : newState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.ENTITY__STATE, oldState, state));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getHash() {
    return hash;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHash(String newHash) {
    String oldHash = hash;
    hash = newHash;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.ENTITY__HASH, oldHash, hash));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getAccessCount() {
    return accessCount;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAccessCount(int newAccessCount) {
    int oldAccessCount = accessCount;
    accessCount = newAccessCount;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.ENTITY__ACCESS_COUNT, oldAccessCount, accessCount));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case PictographPackage.ENTITY__NAME:
        return getName();
      case PictographPackage.ENTITY__STATE:
        return getState();
      case PictographPackage.ENTITY__HASH:
        return getHash();
      case PictographPackage.ENTITY__ACCESS_COUNT:
        return getAccessCount();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case PictographPackage.ENTITY__NAME:
        setName((String)newValue);
        return;
      case PictographPackage.ENTITY__STATE:
        setState((State)newValue);
        return;
      case PictographPackage.ENTITY__HASH:
        setHash((String)newValue);
        return;
      case PictographPackage.ENTITY__ACCESS_COUNT:
        setAccessCount((Integer)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
      case PictographPackage.ENTITY__NAME:
        setName(NAME_EDEFAULT);
        return;
      case PictographPackage.ENTITY__STATE:
        setState(STATE_EDEFAULT);
        return;
      case PictographPackage.ENTITY__HASH:
        setHash(HASH_EDEFAULT);
        return;
      case PictographPackage.ENTITY__ACCESS_COUNT:
        setAccessCount(ACCESS_COUNT_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
      case PictographPackage.ENTITY__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case PictographPackage.ENTITY__STATE:
        return state != STATE_EDEFAULT;
      case PictographPackage.ENTITY__HASH:
        return HASH_EDEFAULT == null ? hash != null : !HASH_EDEFAULT.equals(hash);
      case PictographPackage.ENTITY__ACCESS_COUNT:
        return accessCount != ACCESS_COUNT_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(", state: ");
    result.append(state);
    result.append(", hash: ");
    result.append(hash);
    result.append(", accessCount: ");
    result.append(accessCount);
    result.append(')');
    return result.toString();
  }

} //EntityImpl
