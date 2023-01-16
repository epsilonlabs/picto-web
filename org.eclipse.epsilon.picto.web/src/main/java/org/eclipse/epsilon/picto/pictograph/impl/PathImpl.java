/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.epsilon.picto.pictograph.InputEntity;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Path</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getAffectedBy <em>Affected By</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getGenerationCount <em>Generation Count</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getGenerationTime <em>Generation Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getAvgGenTime <em>Avg Gen Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getCheckCount <em>Check Count</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getCheckingTime <em>Checking Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl#getAvgCheckTime <em>Avg Check Time</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PathImpl extends EntityImpl implements Path {
  /**
   * The cached value of the '{@link #getAffectedBy() <em>Affected By</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAffectedBy()
   * @generated
   * @ordered
   */
  protected EList<InputEntity> affectedBy;

  /**
   * The default value of the '{@link #getGenerationCount() <em>Generation Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenerationCount()
   * @generated
   * @ordered
   */
  protected static final int GENERATION_COUNT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getGenerationCount() <em>Generation Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenerationCount()
   * @generated
   * @ordered
   */
  protected int generationCount = GENERATION_COUNT_EDEFAULT;

  /**
   * The default value of the '{@link #getGenerationTime() <em>Generation Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenerationTime()
   * @generated
   * @ordered
   */
  protected static final long GENERATION_TIME_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getGenerationTime() <em>Generation Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenerationTime()
   * @generated
   * @ordered
   */
  protected long generationTime = GENERATION_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getAvgGenTime() <em>Avg Gen Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAvgGenTime()
   * @generated
   * @ordered
   */
  protected static final double AVG_GEN_TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getAvgGenTime() <em>Avg Gen Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAvgGenTime()
   * @generated
   * @ordered
   */
  protected double avgGenTime = AVG_GEN_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getCheckCount() <em>Check Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckCount()
   * @generated
   * @ordered
   */
  protected static final int CHECK_COUNT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getCheckCount() <em>Check Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckCount()
   * @generated
   * @ordered
   */
  protected int checkCount = CHECK_COUNT_EDEFAULT;

  /**
   * The default value of the '{@link #getCheckingTime() <em>Checking Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckingTime()
   * @generated
   * @ordered
   */
  protected static final long CHECKING_TIME_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getCheckingTime() <em>Checking Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckingTime()
   * @generated
   * @ordered
   */
  protected long checkingTime = CHECKING_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getAvgCheckTime() <em>Avg Check Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAvgCheckTime()
   * @generated
   * @ordered
   */
  protected static final double AVG_CHECK_TIME_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getAvgCheckTime() <em>Avg Check Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAvgCheckTime()
   * @generated
   * @ordered
   */
  protected double avgCheckTime = AVG_CHECK_TIME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PathImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return PictographPackage.Literals.PATH;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<InputEntity> getAffectedBy() {
    if (affectedBy == null) {
      affectedBy = new EObjectWithInverseResolvingEList.ManyInverse<InputEntity>(InputEntity.class, this, PictographPackage.PATH__AFFECTED_BY, PictographPackage.INPUT_ENTITY__AFFECTS);
    }
    return affectedBy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getGenerationCount() {
    return generationCount;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setGenerationCount(int newGenerationCount) {
    int oldGenerationCount = generationCount;
    generationCount = newGenerationCount;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__GENERATION_COUNT, oldGenerationCount, generationCount));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public long getGenerationTime() {
    return generationTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setGenerationTime(long newGenerationTime) {
    long oldGenerationTime = generationTime;
    generationTime = newGenerationTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__GENERATION_TIME, oldGenerationTime, generationTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getAvgGenTime() {
    return avgGenTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAvgGenTime(double newAvgGenTime) {
    double oldAvgGenTime = avgGenTime;
    avgGenTime = newAvgGenTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__AVG_GEN_TIME, oldAvgGenTime, avgGenTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getCheckCount() {
    return checkCount;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCheckCount(int newCheckCount) {
    int oldCheckCount = checkCount;
    checkCount = newCheckCount;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__CHECK_COUNT, oldCheckCount, checkCount));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public long getCheckingTime() {
    return checkingTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCheckingTime(long newCheckingTime) {
    long oldCheckingTime = checkingTime;
    checkingTime = newCheckingTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__CHECKING_TIME, oldCheckingTime, checkingTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getAvgCheckTime() {
    return avgCheckTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAvgCheckTime(double newAvgCheckTime) {
    double oldAvgCheckTime = avgCheckTime;
    avgCheckTime = newAvgCheckTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PATH__AVG_CHECK_TIME, oldAvgCheckTime, avgCheckTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case PictographPackage.PATH__AFFECTED_BY:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getAffectedBy()).basicAdd(otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case PictographPackage.PATH__AFFECTED_BY:
        return ((InternalEList<?>)getAffectedBy()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case PictographPackage.PATH__AFFECTED_BY:
        return getAffectedBy();
      case PictographPackage.PATH__GENERATION_COUNT:
        return getGenerationCount();
      case PictographPackage.PATH__GENERATION_TIME:
        return getGenerationTime();
      case PictographPackage.PATH__AVG_GEN_TIME:
        return getAvgGenTime();
      case PictographPackage.PATH__CHECK_COUNT:
        return getCheckCount();
      case PictographPackage.PATH__CHECKING_TIME:
        return getCheckingTime();
      case PictographPackage.PATH__AVG_CHECK_TIME:
        return getAvgCheckTime();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case PictographPackage.PATH__AFFECTED_BY:
        getAffectedBy().clear();
        getAffectedBy().addAll((Collection<? extends InputEntity>)newValue);
        return;
      case PictographPackage.PATH__GENERATION_COUNT:
        setGenerationCount((Integer)newValue);
        return;
      case PictographPackage.PATH__GENERATION_TIME:
        setGenerationTime((Long)newValue);
        return;
      case PictographPackage.PATH__AVG_GEN_TIME:
        setAvgGenTime((Double)newValue);
        return;
      case PictographPackage.PATH__CHECK_COUNT:
        setCheckCount((Integer)newValue);
        return;
      case PictographPackage.PATH__CHECKING_TIME:
        setCheckingTime((Long)newValue);
        return;
      case PictographPackage.PATH__AVG_CHECK_TIME:
        setAvgCheckTime((Double)newValue);
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
      case PictographPackage.PATH__AFFECTED_BY:
        getAffectedBy().clear();
        return;
      case PictographPackage.PATH__GENERATION_COUNT:
        setGenerationCount(GENERATION_COUNT_EDEFAULT);
        return;
      case PictographPackage.PATH__GENERATION_TIME:
        setGenerationTime(GENERATION_TIME_EDEFAULT);
        return;
      case PictographPackage.PATH__AVG_GEN_TIME:
        setAvgGenTime(AVG_GEN_TIME_EDEFAULT);
        return;
      case PictographPackage.PATH__CHECK_COUNT:
        setCheckCount(CHECK_COUNT_EDEFAULT);
        return;
      case PictographPackage.PATH__CHECKING_TIME:
        setCheckingTime(CHECKING_TIME_EDEFAULT);
        return;
      case PictographPackage.PATH__AVG_CHECK_TIME:
        setAvgCheckTime(AVG_CHECK_TIME_EDEFAULT);
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
      case PictographPackage.PATH__AFFECTED_BY:
        return affectedBy != null && !affectedBy.isEmpty();
      case PictographPackage.PATH__GENERATION_COUNT:
        return generationCount != GENERATION_COUNT_EDEFAULT;
      case PictographPackage.PATH__GENERATION_TIME:
        return generationTime != GENERATION_TIME_EDEFAULT;
      case PictographPackage.PATH__AVG_GEN_TIME:
        return avgGenTime != AVG_GEN_TIME_EDEFAULT;
      case PictographPackage.PATH__CHECK_COUNT:
        return checkCount != CHECK_COUNT_EDEFAULT;
      case PictographPackage.PATH__CHECKING_TIME:
        return checkingTime != CHECKING_TIME_EDEFAULT;
      case PictographPackage.PATH__AVG_CHECK_TIME:
        return avgCheckTime != AVG_CHECK_TIME_EDEFAULT;
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
    result.append(" (generationCount: ");
    result.append(generationCount);
    result.append(", generationTime: ");
    result.append(generationTime);
    result.append(", avgGenTime: ");
    result.append(avgGenTime);
    result.append(", checkCount: ");
    result.append(checkCount);
    result.append(", checkingTime: ");
    result.append(checkingTime);
    result.append(", avgCheckTime: ");
    result.append(avgCheckTime);
    result.append(')');
    return result.toString();
  }

} //PathImpl
