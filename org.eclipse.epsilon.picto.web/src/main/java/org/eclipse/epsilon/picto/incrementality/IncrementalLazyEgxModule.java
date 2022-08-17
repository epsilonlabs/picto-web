package org.eclipse.epsilon.picto.incrementality;

import java.net.URI;
import java.util.Collection;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.egl.execute.context.IEgxContext;
import org.eclipse.epsilon.egl.spec.EglTemplateSpecification;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.picto.LazyEgxModule;

/***
 * Picto's EglModule to record the execution of an EGX transformation to record
 * model contextElement property accesses. This can be useful e.g. for coverage
 * analysis or incremental execution of EGX transformations. The same technique
 * can be used to record contextElement property accesses in other Epsilon
 * languages. too.
 */
public class IncrementalLazyEgxModule extends LazyEgxModule {

	protected AccessResource incrementalResource;
	protected AccessRecorder propertyAccessRecorder;

	public AccessRecorder getPropertyAccessRecorder() {
		return propertyAccessRecorder;
	}

	public AccessResource getIncrementalResource() {
		return incrementalResource;
	}

	public void startRecording() {
		propertyAccessRecorder.startRecording();
	}

	public void stopRecording() {
		propertyAccessRecorder.stopRecording();
	}

	public IncrementalLazyEgxModule(AccessResource incrementalResource) {

		this.incrementalResource = incrementalResource;

		this.getContext().setExecutorFactory(new IncrementalRuleExecutorFactory(incrementalResource));

		// Create the property access recorder that will record
		// all the property access events in the EGX transformation
		propertyAccessRecorder = new AccessRecorder();

		// Create a custom template factory so that we can monitor property
		// access events during template execution too
		this.setTemplateFactory(new EglFileGeneratingTemplateFactory() {
			@Override
			protected EglTemplate createTemplate(EglTemplateSpecification spec) throws Exception {
				EglTemplate template = super.createTemplate(spec);
				// Every time the factory creates a template, attach a listener to it
				// to record property accesses to our custom recorder
//				template.getModule().getContext().setExecutorFactory(new IncrementalTemplateExecutorFactory());
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
		String path = null;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public Object execute(IEolContext context_, Object element) throws EolRuntimeException {
			// Before executing a generation rule against an contextElement
			// update the rule and contextElement fields of the property change recorder

			propertyAccessRecorder.setContextElement(element);
			propertyAccessRecorder.setRule(this);
			// execute parent
			Object wrappedPromise = super.execute(context_, element);
			// after executing parent

			IEgxContext context = (IEgxContext) context_;
			URI templateUri = null;
			String templateName = (templateBlock == null) ? null : templateBlock.execute(context, false);
			if (templateName != null) {
				templateUri = context.getTemplateFactory().resolveTemplate(templateName);
			}
			EglTemplateFactory templateFactory = context.getTemplateFactory();

			propertyAccessRecorder.setTemplateUri(templateUri);
			this.path = (propertyAccessRecorder.getPath() == null) ? null
					: new StringBuffer(propertyAccessRecorder.getPath()).toString();
			
			propertyAccessRecorder.updateCurrentPropertyAccessesPath(this.path);
			propertyAccessRecorder.saveToAccessRecordResource();
//			PictoWeb.ACCESS_RECORD_RESOURCE.printIncrementalRecords();
			
			IncrementalLazyGenerationRuleContentPromise wrappingPromise = new IncrementalLazyGenerationRuleContentPromise(
					(LazyGenerationRuleContentPromise) wrappedPromise, element, this, this.path, templateFactory,
					templateUri);

			propertyAccessRecorder.setContextElement(null);
			propertyAccessRecorder.setRule(null);
			propertyAccessRecorder.setPath(null);
			propertyAccessRecorder.setTemplateUri(null);

			return wrappingPromise;
		}
	}

	public class IncrementalLazyGenerationRuleContentPromise extends LazyGenerationRuleContentPromise {

		LazyGenerationRuleContentPromise wrappedPromise = null;
		String path = null;
		GenerationRule generationRule = null;
		Object contextObject = null;

		public IncrementalLazyGenerationRuleContentPromise(LazyGenerationRuleContentPromise wrappedPromise,
				Object contextObject, GenerationRule generationRule, String path, EglTemplateFactory templateFactory,
				URI templateUri) {
			this.wrappedPromise = wrappedPromise;
			this.generationRule = generationRule;
			this.path = path;
			this.contextObject = contextObject;
			this.templateFactory = templateFactory;
			this.templateUri = templateUri;
		}

		public LazyGenerationRuleContentPromise getWrappedPromise() {
			return wrappedPromise;
		}

		public String getPath() {
			return path;
		}

		public GenerationRule getGenerationRule() {
			return generationRule;
		}

		public Object getContextObject() {
			return contextObject;
		}

		@Override
		public String getContent() throws Exception {
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setTemplateUri(templateUri);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setContextElement(contextObject);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setRule(generationRule);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setPath(path);
			String content = wrappedPromise.getContent();
			propertyAccessRecorder.updateCurrentPropertyAccessesPath(path);
			propertyAccessRecorder.saveToAccessRecordResource();
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setTemplateUri(null);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setContextElement(null);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setRule(null);
			IncrementalLazyEgxModule.this.getPropertyAccessRecorder().setPath(null);
			return content;
		}

		@Override
		public Collection<Variable> getVariables() {
			return wrappedPromise.getVariables();
		}

	}
}
