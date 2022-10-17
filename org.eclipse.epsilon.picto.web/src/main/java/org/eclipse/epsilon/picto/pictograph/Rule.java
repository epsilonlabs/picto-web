/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Rule#getModule <em>Module</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Rule#getTemplate <em>Template</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Rule#getContextElements <em>Context Elements</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Rule#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getRule()
 * @model
 * @generated
 */
public interface Rule extends InputEntity {
	/**
	 * Returns the value of the '<em><b>Module</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Module#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' container reference.
	 * @see #setModule(org.eclipse.epsilon.picto.pictograph.Module)
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getRule_Module()
	 * @see org.eclipse.epsilon.picto.pictograph.Module#getRules
	 * @model opposite="rules" transient="false"
	 * @generated
	 */
	org.eclipse.epsilon.picto.pictograph.Module getModule();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Rule#getModule <em>Module</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' container reference.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(org.eclipse.epsilon.picto.pictograph.Module value);

	/**
	 * Returns the value of the '<em><b>Template</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Template</em>' reference.
	 * @see #setTemplate(Template)
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getRule_Template()
	 * @model
	 * @generated
	 */
	Template getTemplate();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Rule#getTemplate <em>Template</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template</em>' reference.
	 * @see #getTemplate()
	 * @generated
	 */
	void setTemplate(Template value);

	/**
	 * Returns the value of the '<em><b>Context Elements</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Element}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context Elements</em>' reference list.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getRule_ContextElements()
	 * @model
	 * @generated
	 */
	EList<Element> getContextElements();

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Element}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' reference list.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getRule_Elements()
	 * @model
	 * @generated
	 */
	EList<Element> getElements();

} // Rule
