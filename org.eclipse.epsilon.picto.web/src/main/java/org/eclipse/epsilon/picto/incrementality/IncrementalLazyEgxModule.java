package org.eclipse.epsilon.picto.incrementality;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.egl.spec.EglTemplateSpecification;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessExecutionListener;
import org.eclipse.epsilon.picto.LazyEgxModule;

/***
 * Picto's EglModule to record the execution of an EGX transformation to record
 * model contextElement property accesses. This can be useful e.g. for coverage
 * analysis or incremental execution of EGX transformations. The same technique
 * can be used to record contextElement property accesses in other Epsilon
 * languages. too.
 */
public class IncrementalLazyEgxModule extends LazyEgxModule {

	protected IncrementalResource incrementalResource;
	protected GenerationRulePropertyAccessRecorder propertyAccessRecorder;

	public GenerationRulePropertyAccessRecorder getPropertyAccessRecorder() {
		return propertyAccessRecorder;
	}

	
	public IncrementalResource getIncrementalResource() {
		return incrementalResource;
	}


	public void startRecording() {
		propertyAccessRecorder.startRecording();
	}

	public void stopRecording() {
		propertyAccessRecorder.stopRecording();
	}

	public IncrementalLazyEgxModule(IncrementalResource incrementalResource) {

		this.incrementalResource = incrementalResource;

		this.getContext().setExecutorFactory(new IncrementalRuleExecutorFactory(incrementalResource));

		// Create the property access recorder that will record
		// all the property access events in the EGX transformation
		propertyAccessRecorder = new GenerationRulePropertyAccessRecorder();

		// Create a custom template factory so that we can monitor property
		// access events during template execution too
		this.setTemplateFactory(new EglFileGeneratingTemplateFactory() {
			@Override
			protected EglTemplate createTemplate(EglTemplateSpecification spec) throws Exception {
				EglTemplate template = super.createTemplate(spec);
				// Every time the factory creates a template, attach a listener to it
				// to record property accesses to our custom recorder
				template.getModule().getContext().getExecutorFactory()
						.addExecutionListener(new IncrementalPropertyAccessExecutionListener(propertyAccessRecorder));
				return template;
			}
		});

		// Add a listener to record property access events during
		// the execution of the EGX program
		this.getContext().getExecutorFactory()
				.addExecutionListener(new IncrementalPropertyAccessExecutionListener(propertyAccessRecorder));
	}

	@Override
	public ModuleElement adapt(AST cst, ModuleElement parentAst) {
		ModuleElement ast = super.adapt(cst, parentAst);
		if (ast instanceof GenerationRule)
			return new IncrementalLazyGenerationRule();
		else
			return ast;
	}

	// Subclass the default EgxModule so that we can return custom generation rules
	@Override
	protected GenerationRule createGenerationRule(AST generationRuleAst) {
		return (GenerationRule) new IncrementalLazyGenerationRule();
	}

	public class IncrementalLazyGenerationRule extends LazyGenerationRule {

		@Override
		public Object execute(IEolContext context_, Object element) throws EolRuntimeException {
			// Before executing a generation rule against an contextElement
			// update the rule and contextElement fields of the property change recorder
			propertyAccessRecorder.setContextElement(element);
			propertyAccessRecorder.setRule(this);
			Object result = super.execute(context_, element);
			propertyAccessRecorder.setContextElement(null);
			propertyAccessRecorder.setRule(null);
			return result;
		}
		
		
	}
}
