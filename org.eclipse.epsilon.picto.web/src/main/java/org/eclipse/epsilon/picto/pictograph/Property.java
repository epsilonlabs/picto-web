/**
 */
package org.eclipse.epsilon.picto.pictograph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Property#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Property#getPreviousValue <em>Previous Value</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Property#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends InputEntity {
	/**
   * Returns the value of the '<em><b>Element</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.epsilon.picto.pictograph.Element#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' container reference.
   * @see #setElement(Element)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getProperty_Element()
   * @see org.eclipse.epsilon.picto.pictograph.Element#getProperties
   * @model opposite="properties" transient="false"
   * @generated
   */
	Element getElement();

	/**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Property#getElement <em>Element</em>}' container reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' container reference.
   * @see #getElement()
   * @generated
   */
	void setElement(Element value);

	/**
   * Returns the value of the '<em><b>Previous Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Previous Value</em>' attribute.
   * @see #setPreviousValue(String)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getProperty_PreviousValue()
   * @model
   * @generated
   */
	String getPreviousValue();

	/**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Property#getPreviousValue <em>Previous Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Previous Value</em>' attribute.
   * @see #getPreviousValue()
   * @generated
   */
	void setPreviousValue(String value);

	/**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getProperty_Value()
   * @model
   * @generated
   */
	String getValue();

	/**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Property#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
	void setValue(String value);

} // Property
