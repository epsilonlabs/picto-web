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
 *        annotation="index name='path' by='Path.name' many='false'"
 *        annotation="index name='module' by='Module.name' many='false'"
 *        annotation="index name='rule' by='Module.name, rules.name' many='false'"
 *        annotation="index name='template' by='Module.name, rules.name, template.name' many='false'"
 *        annotation="index name='resource' by='Resource.name' many='false'"
 *        annotation="index name='element' by='Resource.name, elements.name' many='false'"
 *        annotation="index name='property' by='Resource.name, elements.name, properties.name' many='false'"
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
   * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl <em>Picto Graph</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl
   * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPictoGraph()
   * @generated
   */
  int PICTO_GRAPH = 0;

  /**
   * The number of structural features of the '<em>Picto Graph</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PICTO_GRAPH_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.EntityImpl <em>Entity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.epsilon.picto.pictograph.impl.EntityImpl
   * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getEntity()
   * @generated
   */
  int ENTITY = 1;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__HASH = 2;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__ACCESS_COUNT = 3;

  /**
   * The number of structural features of the '<em>Entity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl <em>Input Entity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl
   * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getInputEntity()
   * @generated
   */
  int INPUT_ENTITY = 2;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_ENTITY__HASH = ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_ENTITY__ACCESS_COUNT = ENTITY__ACCESS_COUNT;

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
  int PATH = 3;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__HASH = ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__ACCESS_COUNT = ENTITY__ACCESS_COUNT;

  /**
   * The feature id for the '<em><b>Affected By</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__AFFECTED_BY = ENTITY_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Generation Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__GENERATION_COUNT = ENTITY_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Generation Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__GENERATION_TIME = ENTITY_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Avg Gen Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__AVG_GEN_TIME = ENTITY_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Check Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__CHECK_COUNT = ENTITY_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Checking Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__CHECKING_TIME = ENTITY_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Avg Check Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH__AVG_CHECK_TIME = ENTITY_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Path</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PATH_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl <em>Module</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.epsilon.picto.pictograph.impl.ModuleImpl
   * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getModule()
   * @generated
   */
  int MODULE = 4;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODULE__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODULE__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int RESOURCE = 5;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int PROPERTY = 6;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int ELEMENT = 7;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENT__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENT__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int RULE = 8;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int TEMPLATE = 9;

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
   * The feature id for the '<em><b>Hash</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEMPLATE__HASH = INPUT_ENTITY__HASH;

  /**
   * The feature id for the '<em><b>Access Count</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEMPLATE__ACCESS_COUNT = INPUT_ENTITY__ACCESS_COUNT;

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
  int STATE = 10;


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
   * Returns the meta object for the attribute list '{@link org.eclipse.epsilon.picto.pictograph.Entity#getHash <em>Hash</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Hash</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Entity#getHash()
   * @see #getEntity()
   * @generated
   */
  EAttribute getEntity_Hash();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Entity#getAccessCount <em>Access Count</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Access Count</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Entity#getAccessCount()
   * @see #getEntity()
   * @generated
   */
  EAttribute getEntity_AccessCount();

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
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationCount <em>Generation Count</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Generation Count</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getGenerationCount()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_GenerationCount();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getGenerationTime <em>Generation Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Generation Time</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getGenerationTime()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_GenerationTime();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgGenTime <em>Avg Gen Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Avg Gen Time</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getAvgGenTime()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_AvgGenTime();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckCount <em>Check Count</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Check Count</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getCheckCount()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_CheckCount();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getCheckingTime <em>Checking Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Checking Time</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getCheckingTime()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_CheckingTime();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.epsilon.picto.pictograph.Path#getAvgCheckTime <em>Avg Check Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Avg Check Time</em>'.
   * @see org.eclipse.epsilon.picto.pictograph.Path#getAvgCheckTime()
   * @see #getPath()
   * @generated
   */
  EAttribute getPath_AvgCheckTime();

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
     * The meta object literal for the '{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl <em>Picto Graph</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl
     * @see org.eclipse.epsilon.picto.pictograph.impl.PictographPackageImpl#getPictoGraph()
     * @generated
     */
    EClass PICTO_GRAPH = eINSTANCE.getPictoGraph();

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
     * The meta object literal for the '<em><b>Hash</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITY__HASH = eINSTANCE.getEntity_Hash();

    /**
     * The meta object literal for the '<em><b>Access Count</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITY__ACCESS_COUNT = eINSTANCE.getEntity_AccessCount();

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
     * The meta object literal for the '<em><b>Generation Count</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__GENERATION_COUNT = eINSTANCE.getPath_GenerationCount();

    /**
     * The meta object literal for the '<em><b>Generation Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__GENERATION_TIME = eINSTANCE.getPath_GenerationTime();

    /**
     * The meta object literal for the '<em><b>Avg Gen Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__AVG_GEN_TIME = eINSTANCE.getPath_AvgGenTime();

    /**
     * The meta object literal for the '<em><b>Check Count</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__CHECK_COUNT = eINSTANCE.getPath_CheckCount();

    /**
     * The meta object literal for the '<em><b>Checking Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__CHECKING_TIME = eINSTANCE.getPath_CheckingTime();

    /**
     * The meta object literal for the '<em><b>Avg Check Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PATH__AVG_CHECK_TIME = eINSTANCE.getPath_AvgCheckTime();

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
