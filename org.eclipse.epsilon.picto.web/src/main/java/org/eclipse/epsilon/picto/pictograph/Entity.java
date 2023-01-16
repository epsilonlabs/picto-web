/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Entity#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Entity#getState <em>State</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Entity#getHash <em>Hash</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.Entity#getAccessCount <em>Access Count</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getEntity()
 * @model abstract="true"
 * @generated
 */
public interface Entity extends EObject {
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getEntity_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Entity#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>State</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.epsilon.picto.pictograph.State}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>State</em>' attribute.
   * @see org.eclipse.epsilon.picto.pictograph.State
   * @see #setState(State)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getEntity_State()
   * @model
   * @generated
   */
  State getState();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Entity#getState <em>State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State</em>' attribute.
   * @see org.eclipse.epsilon.picto.pictograph.State
   * @see #getState()
   * @generated
   */
  void setState(State value);

  /**
   * Returns the value of the '<em><b>Hash</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hash</em>' attribute.
   * @see #setHash(String)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getEntity_Hash()
   * @model
   * @generated
   */
  String getHash();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Entity#getHash <em>Hash</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hash</em>' attribute.
   * @see #getHash()
   * @generated
   */
  void setHash(String value);

  /**
   * Returns the value of the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Access Count</em>' attribute.
   * @see #setAccessCount(int)
   * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getEntity_AccessCount()
   * @model
   * @generated
   */
  int getAccessCount();

  /**
   * Sets the value of the '{@link org.eclipse.epsilon.picto.pictograph.Entity#getAccessCount <em>Access Count</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Access Count</em>' attribute.
   * @see #getAccessCount()
   * @generated
   */
  void setAccessCount(int value);

} // Entity
