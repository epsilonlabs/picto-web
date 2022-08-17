/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Picto Graph</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getPaths <em>Paths</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getRules <em>Rules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getTemplates <em>Templates</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getResources <em>Resources</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getElements <em>Elements</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph()
 * @model
 * @generated
 */
public interface PictoGraph extends EObject {
	/**
	 * Returns the value of the '<em><b>Paths</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paths</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Paths()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getPaths();

	/**
	 * Returns the value of the '<em><b>Modules</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Modules()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getModules();

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Rules()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getRules();

	/**
	 * Returns the value of the '<em><b>Templates</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Templates</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Templates()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getTemplates();

	/**
	 * Returns the value of the '<em><b>Resources</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resources</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Resources()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getResources();

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Elements()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getElements();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.epsilon.picto.pictograph.Entity},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' map.
	 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getPictoGraph_Properties()
	 * @model mapType="pictograph.EntityMap&lt;org.eclipse.emf.ecore.EString, pictograph.Entity&gt;"
	 * @generated
	 */
	EMap<String, Entity> getProperties();

} // PictoGraph
