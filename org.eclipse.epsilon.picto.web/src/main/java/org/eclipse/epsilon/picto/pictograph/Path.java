/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

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

} // Path
