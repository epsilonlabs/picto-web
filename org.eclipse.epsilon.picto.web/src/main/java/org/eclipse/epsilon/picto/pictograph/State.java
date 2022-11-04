/**
 */
package org.eclipse.epsilon.picto.pictograph;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>State</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.picto.pictograph.PictographPackage#getState()
 * @model
 * @generated
 */
public enum State implements Enumerator {
	/**
   * The '<em><b>NEW</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #NEW_VALUE
   * @generated
   * @ordered
   */
	NEW(0, "NEW", "NEW"),

	/**
   * The '<em><b>UPDATED</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #UPDATED_VALUE
   * @generated
   * @ordered
   */
	UPDATED(1, "UPDATED", "UPDATED"),

	/**
   * The '<em><b>PROCESSED</b></em>' literal object.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #PROCESSED_VALUE
   * @generated
   * @ordered
   */
	PROCESSED(2, "PROCESSED", "PROCESSED");

	/**
   * The '<em><b>NEW</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #NEW
   * @model
   * @generated
   * @ordered
   */
	public static final int NEW_VALUE = 0;

	/**
   * The '<em><b>UPDATED</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #UPDATED
   * @model
   * @generated
   * @ordered
   */
	public static final int UPDATED_VALUE = 1;

	/**
   * The '<em><b>PROCESSED</b></em>' literal value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #PROCESSED
   * @model
   * @generated
   * @ordered
   */
	public static final int PROCESSED_VALUE = 2;

	/**
   * An array of all the '<em><b>State</b></em>' enumerators.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private static final State[] VALUES_ARRAY =
		new State[] {
      NEW,
      UPDATED,
      PROCESSED,
    };

	/**
   * A public read-only list of all the '<em><b>State</b></em>' enumerators.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public static final List<State> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
   * Returns the '<em><b>State</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static State get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      State result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

	/**
   * Returns the '<em><b>State</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static State getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      State result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

	/**
   * Returns the '<em><b>State</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
	public static State get(int value) {
    switch (value) {
      case NEW_VALUE: return NEW;
      case UPDATED_VALUE: return UPDATED;
      case PROCESSED_VALUE: return PROCESSED;
    }
    return null;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final int value;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final String name;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private final String literal;

	/**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private State(int value, String name, String literal) {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public int getValue() {
    return value;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String getName() {
    return name;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String getLiteral() {
    return literal;
  }

	/**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String toString() {
    return literal;
  }
	
} //State
