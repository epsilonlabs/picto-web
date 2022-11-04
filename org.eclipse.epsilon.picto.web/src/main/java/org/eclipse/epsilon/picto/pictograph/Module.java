/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Module#getRules <em>Rules</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getModule()
 * @model
 * @generated
 */
public interface Module extends InputEntity {
	/**
   * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Rule}.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Rule#getModule <em>Module</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Rules</em>' containment reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getModule_Rules()
   * @see org.eclipse.epsilon.picto.pictograph.Rule#getModule
   * @model opposite="module" containment="true"
   * @generated
   */
	EList<Rule> getRules();

} // Module
