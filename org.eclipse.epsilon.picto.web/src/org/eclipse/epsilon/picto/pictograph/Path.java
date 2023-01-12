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

} // Path
