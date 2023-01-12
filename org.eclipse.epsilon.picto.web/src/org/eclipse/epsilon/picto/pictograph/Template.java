/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Template</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Template#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Template#getRules <em>Rules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Template#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getTemplate()
 * @model
 * @generated
 */
public interface Template extends InputEntity {
  /**
   * Returns the value of the '<em><b>Modules</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Module}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Modules</em>' reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getTemplate_Modules()
   * @model
   * @generated
   */
  EList<org.eclipse.epsilon.picto.pictograph.Module> getModules();

  /**
   * Returns the value of the '<em><b>Rules</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Rule}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rules</em>' reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getTemplate_Rules()
   * @model
   * @generated
   */
  EList<Rule> getRules();

  /**
   * Returns the value of the '<em><b>Elements</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Element}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getTemplate_Elements()
   * @model
   * @generated
   */
  EList<Element> getElements();

} // Template
