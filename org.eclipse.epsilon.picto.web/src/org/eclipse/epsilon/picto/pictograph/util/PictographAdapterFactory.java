/**
 */
package org.eclipse.epsilon.picto.pictograph.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.Entity;
import org.eclipse.epsilon.picto.pictograph.InputEntity;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.PictoGraph;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.Property;
import org.eclipse.epsilon.picto.pictograph.Resource;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.Template;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage
 * @generated
 */
public class PictographAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static PictographPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PictographAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = PictographPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object) {
    if (object == modelPackage) {
      return true;
    }
    if (object instanceof EObject) {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PictographSwitch<Adapter> modelSwitch =
    new PictographSwitch<Adapter>() {
      @Override
      public Adapter casePictoGraph(PictoGraph object) {
        return createPictoGraphAdapter();
      }
      @Override
      public Adapter caseEntity(Entity object) {
        return createEntityAdapter();
      }
      @Override
      public Adapter caseInputEntity(InputEntity object) {
        return createInputEntityAdapter();
      }
      @Override
      public Adapter casePath(Path object) {
        return createPathAdapter();
      }
      @Override
      public Adapter caseModule(org.eclipse.epsilon.picto.pictograph.Module object) {
        return createModuleAdapter();
      }
      @Override
      public Adapter caseResource(Resource object) {
        return createResourceAdapter();
      }
      @Override
      public Adapter caseProperty(Property object) {
        return createPropertyAdapter();
      }
      @Override
      public Adapter caseElement(Element object) {
        return createElementAdapter();
      }
      @Override
      public Adapter caseRule(Rule object) {
        return createRuleAdapter();
      }
      @Override
      public Adapter caseTemplate(Template object) {
        return createTemplateAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object) {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target) {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.PictoGraph <em>Picto Graph</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.PictoGraph
   * @generated
   */
  public Adapter createPictoGraphAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Entity <em>Entity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Entity
   * @generated
   */
  public Adapter createEntityAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.InputEntity <em>Input Entity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.InputEntity
   * @generated
   */
  public Adapter createInputEntityAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Path <em>Path</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Path
   * @generated
   */
  public Adapter createPathAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Module <em>Module</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Module
   * @generated
   */
  public Adapter createModuleAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Resource <em>Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Resource
   * @generated
   */
  public Adapter createResourceAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Property <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Property
   * @generated
   */
  public Adapter createPropertyAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Element <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Element
   * @generated
   */
  public Adapter createElementAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Rule <em>Rule</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Rule
   * @generated
   */
  public Adapter createRuleAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.epsilon.picto.pictograph.Template <em>Template</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.epsilon.picto.pictograph.Template
   * @generated
   */
  public Adapter createTemplateAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter() {
    return null;
  }

} //PictographAdapterFactory
