package org.eclipse.epsilon.picto.incrementality;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.IEolContext;

public class IncrementalTemplateExecutorFactory extends ExecutorFactory {

	@Override
	protected Object executeImpl(ModuleElement moduleElement, IEolContext context) throws Exception {
		return super.executeImpl(moduleElement, context);
	}
}
