/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage
 * @generated
 */
public interface PictographFactory extends EFactory {
	/**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	PictographFactory eINSTANCE = org.eclipse.epsilon.picto.pictograph.impl.PictographFactoryImpl.init();

	/**
   * Returns a new object of class '<em>Picto Graph</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Picto Graph</em>'.
   * @generated
   */
	PictoGraph createPictoGraph();

	/**
   * Returns a new object of class '<em>Path</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Path</em>'.
   * @generated
   */
	Path createPath();

	/**
   * Returns a new object of class '<em>Module</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Module</em>'.
   * @generated
   */
	Module createModule();

	/**
   * Returns a new object of class '<em>Resource</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Resource</em>'.
   * @generated
   */
	Resource createResource();

	/**
   * Returns a new object of class '<em>Property</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Property</em>'.
   * @generated
   */
	Property createProperty();

	/**
   * Returns a new object of class '<em>Element</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Element</em>'.
   * @generated
   */
	Element createElement();

	/**
   * Returns a new object of class '<em>Rule</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Rule</em>'.
   * @generated
   */
	Rule createRule();

	/**
   * Returns a new object of class '<em>Template</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Template</em>'.
   * @generated
   */
	Template createTemplate();

	/**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
	PictographPackage getPictographPackage();

} //PictographFactory
