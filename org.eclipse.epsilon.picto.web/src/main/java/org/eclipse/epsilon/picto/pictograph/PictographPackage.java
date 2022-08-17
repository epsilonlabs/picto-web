/**
 */
package org.eclipse.epsilon.picto.pictograph;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.picto.pictograph.PictographFactory
 * @model kind="package"
 * @generated
 */
public interface PictographPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "pictograph";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "pictograph";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PictographPackage eINSTANCE = org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.EntityMapImpl <em>Entity Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.EntityMapImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getEntityMap()
	 * @generated
	 */
	int ENTITY_MAP = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Entity Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl <em>Picto Graph</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPictoGraph()
	 * @generated
	 */
	int PICTO_GRAPH = 1;

	/**
	 * The feature id for the '<em><b>Paths</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__PATHS = 0;

	/**
	 * The feature id for the '<em><b>Modules</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__MODULES = 1;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__RULES = 2;

	/**
	 * The feature id for the '<em><b>Templates</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__TEMPLATES = 3;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__RESOURCES = 4;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__ELEMENTS = 5;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH__PROPERTIES = 6;

	/**
	 * The number of structural features of the '<em>Picto Graph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTO_GRAPH_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.EntityImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__NAME = 0;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__STATE = 1;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl <em>Input Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getInputEntity()
	 * @generated
	 */
	int INPUT_ENTITY = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_ENTITY__NAME = ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_ENTITY__STATE = ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_ENTITY__AFFECTS = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Input Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_ENTITY_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl <em>Path</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PathImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPath()
	 * @generated
	 */
	int PATH = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH__NAME = ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH__STATE = ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affected By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH__AFFECTED_BY = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl <em>Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getModule()
	 * @generated
	 */
	int MODULE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__RULES = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ResourceImpl <em>Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.ResourceImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getResource()
	 * @generated
	 */
	int RESOURCE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE__ELEMENTS = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__ELEMENT = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Previous Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__PREVIOUS_VALUE = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__VALUE = INPUT_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.ElementImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getElement()
	 * @generated
	 */
	int ELEMENT = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__RESOURCE = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__PROPERTIES = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl <em>Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.RuleImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getRule()
	 * @generated
	 */
	int RULE = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Module</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__MODULE = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Template</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__TEMPLATE = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Context Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__CONTEXT_ELEMENTS = INPUT_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__ELEMENTS = INPUT_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl <em>Template</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getTemplate()
	 * @generated
	 */
	int TEMPLATE = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__NAME = INPUT_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__STATE = INPUT_ENTITY__STATE;

	/**
	 * The feature id for the '<em><b>Affects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__AFFECTS = INPUT_ENTITY__AFFECTS;

	/**
	 * The feature id for the '<em><b>Modules</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__MODULES = INPUT_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__RULES = INPUT_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__ELEMENTS = INPUT_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Template</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_FEATURE_COUNT = INPUT_ENTITY_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.State <em>State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.picto.pictograph.State
	 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getState()
	 * @generated
	 */
	int STATE = 11;


	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Entity Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="pictograph.Entity"
	 * @generated
	 */
	EClass getEntityMap();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEntityMap()
	 * @generated
	 */
	EAttribute getEntityMap_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEntityMap()
	 * @generated
	 */
	EReference getEntityMap_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph <em>Picto Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Picto Graph</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph
	 * @generated
	 */
	EClass getPictoGraph();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getPaths <em>Paths</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Paths</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getPaths()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Paths();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Modules</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getModules()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Modules();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Rules</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getRules()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Rules();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getTemplates <em>Templates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Templates</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getTemplates()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Templates();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Resources</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getResources()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Resources();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Elements</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getElements()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Elements();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Properties</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.PictoGraph#getProperties()
	 * @see #getPictoGraph()
	 * @generated
	 */
	EReference getPictoGraph_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Entity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Entity#getName()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Entity#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Entity#getState()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_State();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.InputEntity <em>Input Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Entity</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.InputEntity
	 * @generated
	 */
	EClass getInputEntity();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.InputEntity#getAffects <em>Affects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Affects</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.InputEntity#getAffects()
	 * @see #getInputEntity()
	 * @generated
	 */
	EReference getInputEntity_Affects();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Path <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Path
	 * @generated
	 */
	EClass getPath();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Path#getAffectedBy <em>Affected By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Affected By</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Path#getAffectedBy()
	 * @see #getPath()
	 * @generated
	 */
	EReference getPath_AffectedBy();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Module
	 * @generated
	 */
	EClass getModule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.picto.pictograph.Module#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Module#getRules()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Rules();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Resource
	 * @generated
	 */
	EClass getResource();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.picto.pictograph.Resource#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Resource#getElements()
	 * @see #getResource()
	 * @generated
	 */
	EReference getResource_Elements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.epsilon.picto.pictograph.Property#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Element</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Property#getElement()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_Element();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Property#getPreviousValue <em>Previous Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Previous Value</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Property#getPreviousValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_PreviousValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Property#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Property#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Element
	 * @generated
	 */
	EClass getElement();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.epsilon.picto.pictograph.Element#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Resource</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Element#getResource()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Resource();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.picto.pictograph.Element#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Element#getProperties()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Rule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Rule
	 * @generated
	 */
	EClass getRule();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.epsilon.picto.pictograph.Rule#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Module</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Rule#getModule()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_Module();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.picto.pictograph.Rule#getTemplate <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Template</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Rule#getTemplate()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_Template();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Rule#getContextElements <em>Context Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Context Elements</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Rule#getContextElements()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_ContextElements();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Rule#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Elements</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Rule#getElements()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_Elements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.picto.pictograph.Template <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Template
	 * @generated
	 */
	EClass getTemplate();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Template#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Modules</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Template#getModules()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Modules();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Template#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rules</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Template#getRules()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Rules();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.picto.pictograph.Template#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Elements</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.Template#getElements()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Elements();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.epsilon.picto.pictograph.State <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>State</em>'.
	 * @see org.eclipse.epsilon.picto.pictograph.State
	 * @generated
	 */
	EEnum getState();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PictographFactory getPictographFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.EntityMapImpl <em>Entity Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.EntityMapImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getEntityMap()
		 * @generated
		 */
		EClass ENTITY_MAP = eINSTANCE.getEntityMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_MAP__KEY = eINSTANCE.getEntityMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_MAP__VALUE = eINSTANCE.getEntityMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl <em>Picto Graph</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPictoGraph()
		 * @generated
		 */
		EClass PICTO_GRAPH = eINSTANCE.getPictoGraph();

		/**
		 * The meta object literal for the '<em><b>Paths</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__PATHS = eINSTANCE.getPictoGraph_Paths();

		/**
		 * The meta object literal for the '<em><b>Modules</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__MODULES = eINSTANCE.getPictoGraph_Modules();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__RULES = eINSTANCE.getPictoGraph_Rules();

		/**
		 * The meta object literal for the '<em><b>Templates</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__TEMPLATES = eINSTANCE.getPictoGraph_Templates();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__RESOURCES = eINSTANCE.getPictoGraph_Resources();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__ELEMENTS = eINSTANCE.getPictoGraph_Elements();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTO_GRAPH__PROPERTIES = eINSTANCE.getPictoGraph_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.EntityImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__NAME = eINSTANCE.getEntity_Name();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__STATE = eINSTANCE.getEntity_State();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl <em>Input Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getInputEntity()
		 * @generated
		 */
		EClass INPUT_ENTITY = eINSTANCE.getInputEntity();

		/**
		 * The meta object literal for the '<em><b>Affects</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_ENTITY__AFFECTS = eINSTANCE.getInputEntity_Affects();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PathImpl <em>Path</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PathImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPath()
		 * @generated
		 */
		EClass PATH = eINSTANCE.getPath();

		/**
		 * The meta object literal for the '<em><b>Affected By</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATH__AFFECTED_BY = eINSTANCE.getPath_AffectedBy();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl <em>Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getModule()
		 * @generated
		 */
		EClass MODULE = eINSTANCE.getModule();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__RULES = eINSTANCE.getModule_Rules();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ResourceImpl <em>Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.ResourceImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getResource()
		 * @generated
		 */
		EClass RESOURCE = eINSTANCE.getResource();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE__ELEMENTS = eINSTANCE.getResource_Elements();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__ELEMENT = eINSTANCE.getProperty_Element();

		/**
		 * The meta object literal for the '<em><b>Previous Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__PREVIOUS_VALUE = eINSTANCE.getProperty_PreviousValue();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.ElementImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getElement()
		 * @generated
		 */
		EClass ELEMENT = eINSTANCE.getElement();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__RESOURCE = eINSTANCE.getElement_Resource();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__PROPERTIES = eINSTANCE.getElement_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl <em>Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.RuleImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getRule()
		 * @generated
		 */
		EClass RULE = eINSTANCE.getRule();

		/**
		 * The meta object literal for the '<em><b>Module</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__MODULE = eINSTANCE.getRule_Module();

		/**
		 * The meta object literal for the '<em><b>Template</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__TEMPLATE = eINSTANCE.getRule_Template();

		/**
		 * The meta object literal for the '<em><b>Context Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__CONTEXT_ELEMENTS = eINSTANCE.getRule_ContextElements();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__ELEMENTS = eINSTANCE.getRule_Elements();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl <em>Template</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getTemplate()
		 * @generated
		 */
		EClass TEMPLATE = eINSTANCE.getTemplate();

		/**
		 * The meta object literal for the '<em><b>Modules</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__MODULES = eINSTANCE.getTemplate_Modules();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__RULES = eINSTANCE.getTemplate_Rules();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__ELEMENTS = eINSTANCE.getTemplate_Elements();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.State <em>State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.picto.pictograph.State
		 * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getState()
		 * @generated
		 */
		EEnum STATE = eINSTANCE.getState();

	}

} //PictographPackage
