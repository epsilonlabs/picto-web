package org.eclipse.epsilon.picto.incrementality;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.WeakHashMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.OperationCallExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccessRecorder;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;

public class IncrementalPropertyAccessExecutionListener extends PropertyAccessExecutionListener {

	private final Collection<IPropertyAccessRecorder> recorders = new LinkedList<>();
	private final WeakHashMap<ModuleElement, Object> cache = new WeakHashMap<>();

	public IncrementalPropertyAccessExecutionListener(IPropertyAccessRecorder... recorders) {
//		super(recorders);
		this.recorders.addAll(Arrays.asList(recorders));
	}

	@Override
	public void finishedExecuting(ModuleElement ast, Object result, IEolContext context) {
		// When the left-hand side of a point expression has been executed, store the
		// object on
		// which the point expression was invoked, so that we can pass it to any
		// property access recorders
		if (isLeftHandSideOfPointExpression(ast)) {
			cache.put(ast, result);
		}

		// When a property access is executed, notify the recorders
		if (isPropertyAccessExpression(ast)) {

			PropertyCallExpression propertyCallExpression = (PropertyCallExpression) ast;

			final Object modelElement = cache.get(propertyCallExpression.getTargetExpression());
			final String propertyName = propertyCallExpression.getNameExpression().getName();

			if (isModelBasedProperty(modelElement, propertyName, context)) {
				for (IPropertyAccessRecorder recorder : this.recorders) {
					recorder.record(modelElement, propertyName);
				}
			}
		}
		// operation call expression
		else if (ast instanceof OperationCallExpression) {
			OperationCallExpression operationCallExpression = (OperationCallExpression) ast;

			final String propertyName = operationCallExpression.getNameExpression().getName();

			if (propertyName.equals("all")) {
				Collection<?> resultList = (Collection<?>) result;
				if (resultList.iterator().next() instanceof EObject) {
					for (IPropertyAccessRecorder recorder : this.recorders) {
						Iterator<?> iterator = resultList.iterator();
						while (iterator.hasNext()) {
							EObject eObject = (EObject) iterator.next();
							recorder.record(eObject, propertyName);
						}
					}
				}
			}
		}

	}

	@Override
	public void finishedExecutingWithException(ModuleElement ast, EolRuntimeException exception, IEolContext context) {
	}

	private static boolean isLeftHandSideOfPointExpression(ModuleElement ast) {
		return ast.getParent() instanceof PropertyCallExpression
				&& ((PropertyCallExpression) ast.getParent()).getTargetExpression() == ast;
	}

	private static boolean isPropertyAccessExpression(ModuleElement ast) {
		return ast instanceof PropertyCallExpression && // AST is a point expression
				!isAssignee(ast); // AST is not the left-hand side of an assignment
	}

	// Determines whether a property access is model-based (and not, for example,
	// for an extended property)
	private static boolean isModelBasedProperty(Object object, String property, IEolContext context) {
		return context.getIntrospectionManager().isModelBasedProperty(object, property, context);
	}

	// Determines whether the specified AST is the left-hand side of an assignment
	// expression
	private static boolean isAssignee(ModuleElement ast) {
		return ast.getParent() instanceof AssignmentStatement
				&& ((AssignmentStatement) ast.getParent()).getTargetExpression() == ast;
	}

}
