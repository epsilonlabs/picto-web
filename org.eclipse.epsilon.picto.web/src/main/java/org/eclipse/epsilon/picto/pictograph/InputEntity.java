/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.InputEntity#getAffects <em>Affects</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getInputEntity()
 * @model abstract="true"
 * @generated
 */
public interface InputEntity extends Entity {
	/**
   * Returns the value of the '<em><b>Affects</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Path}.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Path#getAffectedBy <em>Affected By</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Affects</em>' reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getInputEntity_Affects()
   * @see org.eclipse.epsilon.picto.pictograph.Path#getAffectedBy
   * @model opposite="affectedBy"
   * @generated
   */
	EList<Path> getAffects();

} // InputEntity
