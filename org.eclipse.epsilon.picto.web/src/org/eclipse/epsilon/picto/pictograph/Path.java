/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Path</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getAffectedBy <em>Affected By</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationCount <em>Generation Count</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationTime <em>Generation Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgGenTime <em>Avg Gen Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckCount <em>Check Count</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckingTime <em>Checking Time</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgCheckTime <em>Avg Check Time</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath()
 * @model
 * @generated
 */
public interface Path extends Entity {
  /**
   * Returns the value of the '<em><b>Affected By</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.InputEntity}.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.InputEntity#getAffects <em>Affects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Affected By</em>' reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_AffectedBy()
   * @see org.eclipse.epsilon.picto.pictograph.InputEntity#getAffects
   * @model opposite="affects"
   * @generated
   */
  EList<InputEntity> getAffectedBy();

  /**
   * Returns the value of the '<em><b>Generation Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Generation Count</em>' attribute.
   * @see #setGenerationCount(int)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_GenerationCount()
   * @model
   * @generated
   */
  int getGenerationCount();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationCount <em>Generation Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Generation Count</em>' attribute.
   * @see #getGenerationCount()
   * @generated
   */
  void setGenerationCount(int value);

  /**
   * Returns the value of the '<em><b>Generation Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Generation Time</em>' attribute.
   * @see #setGenerationTime(long)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_GenerationTime()
   * @model
   * @generated
   */
  long getGenerationTime();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationTime <em>Generation Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Generation Time</em>' attribute.
   * @see #getGenerationTime()
   * @generated
   */
  void setGenerationTime(long value);

  /**
   * Returns the value of the '<em><b>Avg Gen Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Avg Gen Time</em>' attribute.
   * @see #setAvgGenTime(double)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_AvgGenTime()
   * @model
   * @generated
   */
  double getAvgGenTime();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgGenTime <em>Avg Gen Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Avg Gen Time</em>' attribute.
   * @see #getAvgGenTime()
   * @generated
   */
  void setAvgGenTime(double value);

  /**
   * Returns the value of the '<em><b>Check Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Check Count</em>' attribute.
   * @see #setCheckCount(int)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_CheckCount()
   * @model
   * @generated
   */
  int getCheckCount();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckCount <em>Check Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Check Count</em>' attribute.
   * @see #getCheckCount()
   * @generated
   */
  void setCheckCount(int value);

  /**
   * Returns the value of the '<em><b>Checking Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Checking Time</em>' attribute.
   * @see #setCheckingTime(long)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_CheckingTime()
   * @model
   * @generated
   */
  long getCheckingTime();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckingTime <em>Checking Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Checking Time</em>' attribute.
   * @see #getCheckingTime()
   * @generated
   */
  void setCheckingTime(long value);

  /**
   * Returns the value of the '<em><b>Avg Check Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Avg Check Time</em>' attribute.
   * @see #setAvgCheckTime(double)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPath_AvgCheckTime()
   * @model
   * @generated
   */
  double getAvgCheckTime();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgCheckTime <em>Avg Check Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Avg Check Time</em>' attribute.
   * @see #getAvgCheckTime()
   * @generated
   */
  void setAvgCheckTime(double value);

} // Path
