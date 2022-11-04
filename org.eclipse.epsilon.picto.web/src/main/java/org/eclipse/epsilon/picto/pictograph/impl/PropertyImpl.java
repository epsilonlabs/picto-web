/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.Property;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl#getPreviousValue <em>Previous Value</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PropertyImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyImpl extends InputEntityImpl implements Property {
	/**
   * The default value of the '{@link #getPreviousValue() <em>Previous Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getPreviousValue()
   * @generated
   * @ordered
   */
	protected static final String PREVIOUS_VALUE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getPreviousValue() <em>Previous Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getPreviousValue()
   * @generated
   * @ordered
   */
	protected String previousValue = PREVIOUS_VALUE_EDEFAULT;

	/**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected static final String VALUE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected String value = VALUE_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected PropertyImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return PictographPackage.Literals.PROPERTY;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Element getElement() {
    if (eContainerFeatureID() != PictographPackage.PROPERTY__ELEMENT) return null;
    return (Element)eInternalContainer();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetElement(Element newElement, NotificationChain msgs) {
    msgs = eBasicSetContainer((InternalEObject)newElement, PictographPackage.PROPERTY__ELEMENT, msgs);
    return msgs;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void setElement(Element newElement) {
    if (newElement != eInternalContainer() || (eContainerFeatureID() != PictographPackage.PROPERTY__ELEMENT && newElement != null)) {
      if (EcoreUtil.isAncestor(this, newElement))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newElement != null)
        msgs = ((InternalEObject)newElement).eInverseAdd(this, PictographPackage.ELEMENT__PROPERTIES, Element.class, msgs);
      msgs = basicSetElement(newElement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PROPERTY__ELEMENT, newElement, newElement));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String getPreviousValue() {
    return previousValue;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void setPreviousValue(String newPreviousValue) {
    String oldPreviousValue = previousValue;
    previousValue = newPreviousValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PROPERTY__PREVIOUS_VALUE, oldPreviousValue, previousValue));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String getValue() {
    return value;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void setValue(String newValue) {
    String oldValue = value;
    value = newValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.PROPERTY__VALUE, oldValue, value));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetElement((Element)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        return basicSetElement(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
    switch (eContainerFeatureID()) {
      case PictographPackage.PROPERTY__ELEMENT:
        return eInternalContainer().eInverseRemove(this, PictographPackage.ELEMENT__PROPERTIES, Element.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        return getElement();
      case PictographPackage.PROPERTY__PREVIOUS_VALUE:
        return getPreviousValue();
      case PictographPackage.PROPERTY__VALUE:
        return getValue();
    }
    return super.eGet(featureID, resolve, coreType);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        setElement((Element)newValue);
        return;
      case PictographPackage.PROPERTY__PREVIOUS_VALUE:
        setPreviousValue((String)newValue);
        return;
      case PictographPackage.PROPERTY__VALUE:
        setValue((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void eUnset(int featureID) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        setElement((Element)null);
        return;
      case PictographPackage.PROPERTY__PREVIOUS_VALUE:
        setPreviousValue(PREVIOUS_VALUE_EDEFAULT);
        return;
      case PictographPackage.PROPERTY__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public boolean eIsSet(int featureID) {
    switch (featureID) {
      case PictographPackage.PROPERTY__ELEMENT:
        return getElement() != null;
      case PictographPackage.PROPERTY__PREVIOUS_VALUE:
        return PREVIOUS_VALUE_EDEFAULT == null ? previousValue != null : !PREVIOUS_VALUE_EDEFAULT.equals(previousValue);
      case PictographPackage.PROPERTY__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
    }
    return super.eIsSet(featureID);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (previousValue: ");
    result.append(previousValue);
    result.append(", value: ");
    result.append(value);
    result.append(')');
    return result.toString();
  }

} //PropertyImpl
