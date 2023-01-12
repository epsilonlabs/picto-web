/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Element#getResource <em>Resource</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Element#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getElement()
 * @model
 * @generated
 */
public interface Element extends InputEntity {
  /**
   * Returns the value of the '<em><b>Resource</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Resource#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resource</em>' container reference.
   * @see #setResource(Resource)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getElement_Resource()
   * @see org.eclipse.epsilon.picto.pictograph.Resource#getElements
   * @model opposite="elements" transient="false"
   * @generated
   */
  Resource getResource();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Element#getResource <em>Resource</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resource</em>' container reference.
   * @see #getResource()
   * @generated
   */
  void setResource(Resource value);

  /**
   * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.epsilon.picto.pictograph.Property}.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Property#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Properties</em>' containment reference list.
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getElement_Properties()
   * @see org.eclipse.epsilon.picto.pictograph.Property#getElement
   * @model opposite="element" containment="true"
   * @generated
   */
  EList<Property> getProperties();

} // Element
