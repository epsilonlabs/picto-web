/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Resource#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getResource()
 * @model
 * @generated
 */
public interface Resource extends InputEntity {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Element}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Element#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getResource_Elements()
	 * @see org.eclipse.epsilon.picto.pictograph.Element#getResource
	 * @model opposite="resource" containment="true"
	 * @generated
	 */
	EList<Element> getElements();

} // Resource
