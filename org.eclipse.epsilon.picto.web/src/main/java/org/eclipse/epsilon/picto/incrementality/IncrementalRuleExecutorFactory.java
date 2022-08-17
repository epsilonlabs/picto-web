package org.eclipse.epsilon.picto.incrementality;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.dom.CollectionLiteralExpression;
import org.eclipse.epsilon.eol.dom.IExecutableModuleElementParameter;
import org.eclipse.epsilon.eol.dom.OperationCallExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.erl.execute.RuleExecutorFactory;

public class IncrementalRuleExecutorFactory extends RuleExecutorFactory {

	private AccessResource incrementalResource;

	public IncrementalRuleExecutorFactory(AccessResource incrementalResource) {
		super();
		this.incrementalResource = incrementalResource;
	}

	@Override
	protected Object executeImpl(IExecutableModuleElementParameter moduleElement, IEolContext context, Object parameter)
			throws EolRuntimeException {
		return super.executeImpl(moduleElement, context, parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void postExecuteSuccess(ModuleElement moduleElement, Object result, IEolContext context) {
		super.postExecuteSuccess(moduleElement, result, context);
		
		if (result instanceof Collection<?> && ((Collection<?>) result).size() > 0) {
				if (((Collection<?>) result).iterator().next() instanceof EObject){
					Iterator<?> iterator  = ((Collection<?>) result).iterator();
					while(iterator.hasNext()) {
						EObject eObject = (EObject) iterator.next();
					}
				}
		} else if (result instanceof EObject) {
			EObject eObject = (EObject) result;
			
		}
		
//		if (moduleElement instanceof OperationCallExpression) {
//			OperationCallExpression ope = (OperationCallExpression) moduleElement;
//			if (ope.getNameExpression().getName().equals("all")) {
//				System.console();
//			} else {
//				if (result instanceof Collection<?> && ((Collection<?>) result).size() > 0) {
//					System.console();
//				}
//			}
//		}
	}

}