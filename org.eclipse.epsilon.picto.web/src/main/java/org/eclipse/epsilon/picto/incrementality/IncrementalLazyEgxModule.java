package org.eclipse.epsilon.picto.incrementality;

import java.net.URI;
import java.util.Collection;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.egl.execute.context.EgxContext;
import org.eclipse.epsilon.egl.execute.context.concurrent.IEgxContextParallel;
import org.eclipse.epsilon.egl.spec.EglTemplateSpecification;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;
import org.eclipse.epsilon.erl.execute.context.concurrent.ErlContextParallel;
import org.eclipse.epsilon.picto.LazyEgxModule;

/***
 * Picto's EglModule to record the execution of an EGX transformation to record
 * model contextElement property accesses. This can be useful e.g. for coverage
 * analysis or incremental execution of EGX transformations. The same technique
 * can be used to record contextElement property accesses in other Epsilon
 * languages. too.
 */
public class IncrementalLazyEgxModule extends LazyEgxModule {

  protected AccessRecordResource accessRecordResource;
//  protected AccessRecordRecorder propertyAccessRecorder;

//  public AccessRecordRecorder getPropertyAccessRecorder() {
//    return propertyAccessRecorder;
//  }

  public AccessRecordResource getIncrementalResource() {
    return accessRecordResource;
  }

//  public void startRecording() {
//    propertyAccessRecorder.startRecording();
//  }

//  public void stopRecording() {
//    propertyAccessRecorder.stopRecording();
//  }

  public IncrementalLazyEgxModule(AccessRecordResource accessRecordResource) {

    this.accessRecordResource = accessRecordResource;

//    // Create the property access recorder that will record
//    // all the property access events in the EGX transformation
//    propertyAccessRecorder = new AccessRecordRecorder(accessRecordResource);
//
//    // Create a custom template factory so that we can monitor property
//    // access events during template execution too
//    this.setTemplateFactory(new EglFileGeneratingTemplateFactory() {
//      @Override
//      protected EglTemplate createTemplate(EglTemplateSpecification spec) throws Exception {
//        EglTemplate template = super.createTemplate(spec);
//        // Every time the factory creates a template, attach a listener to it
//        // to record property accesses to our custom recorder
////				template.getModule().getContext().setExecutorFactory(new IncrementalTemplateExecutorFactory());
//        if (!template.getModule().getContext().getExecutorFactory().getExecutionListeners().stream()
//            .anyMatch(e -> e.getClass().equals(IncrementalPropertyAccessExecutionListener.class))) {
//          template.getModule().getContext().getExecutorFactory()
//              .addExecutionListener(new IncrementalPropertyAccessExecutionListener(propertyAccessRecorder));
//        }
//        return template;
//      }
//    });
//
//    // Add a listener to record property access events during
//    // the execution of the EGX program
//    if (!this.getContext().getExecutorFactory().getExecutionListeners().stream()
//        .anyMatch(e -> e.getClass().equals(IncrementalPropertyAccessExecutionListener.class))) {
//      this.getContext().getExecutorFactory()
//          .addExecutionListener(new IncrementalPropertyAccessExecutionListener(propertyAccessRecorder));
//    }
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

  /**
   * 
   * @author Alfa Yohannis
   *
   */
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

      AccessRecordRecorder accessRecorder = new AccessRecordRecorder(accessRecordResource);
      IncrementalPropertyAccessExecutionListener listener = new IncrementalPropertyAccessExecutionListener(
          accessRecorder);
      IncrementalLazyEgxModule.this.getContext().getExecutorFactory().addExecutionListener(listener);
      accessRecorder.startRecording();

      // Before executing a generation rule against an contextElement
      // update the rule and contextElement fields of the property change recorder
      accessRecorder.setContextElement(element);
      accessRecorder.setRule(this);
      // execute parent class
      LazyGenerationRuleContentPromise wrappedPromise = (LazyGenerationRuleContentPromise) super.execute(context_,
          element);
      // after executing parent class

      EglTemplateFactory templateFactory = null;
      URI templateUri = null;
      if (context_ instanceof EgxContext) {
//      IEgxContextParallel  context = (IEgxContextParallel) context_;
        EgxContext serialContext = (EgxContext) context_;
        String templateName = (templateBlock == null) ? null : templateBlock.execute(serialContext, false);
        if (templateName != null) {
          templateUri = serialContext.getTemplateFactory().resolveTemplate(templateName);
        }
        templateFactory = serialContext.getTemplateFactory();
      } else if (context_ instanceof IEgxContextParallel) {
        IEgxContextParallel parallelContext = (IEgxContextParallel) context_;
        String templateName = (templateBlock == null) ? null : templateBlock.execute(parallelContext, false);
        if (templateName != null) {
          templateUri = parallelContext.getTemplateFactory().resolveTemplate(templateName);
        }
        templateFactory = parallelContext.getTemplateFactory();
      }

      accessRecorder.setTemplateUri(templateUri);
      this.path = (accessRecorder.getPath() == null) ? null
          : new StringBuffer(accessRecorder.getPath()).toString();

      accessRecorder.updateCurrentPropertyAccessesPath(this.path);
      accessRecorder.saveToAccessRecordResource();
//			PictoWeb.ACCESS_RECORD_RESOURCE.printIncrementalRecords();

      IncrementalLazyGenerationRuleContentPromise wrappingPromise = new IncrementalLazyGenerationRuleContentPromise(
          wrappedPromise, element, this, this.path, templateFactory, templateUri);

      accessRecorder.setContextElement(null);
      accessRecorder.setRule(null);
      accessRecorder.setPath(null);
      accessRecorder.setTemplateUri(null);

      accessRecorder.stopRecording();
      listener.removeRecorder(accessRecorder);
      IncrementalLazyEgxModule.this.getContext().getExecutorFactory().removeExecutionListener(listener);
      
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

//      if (!this.getContext().getExecutorFactory().getExecutionListeners().stream()
//        .anyMatch(e -> e.getClass().equals(IncrementalPropertyAccessExecutionListener.class))) {
//      this.getContext().getExecutorFactory()
//          .addExecutionListener(new IncrementalPropertyAccessExecutionListener(propertyAccessRecorder));
//    }

      AccessRecordRecorder accessRecorder = new AccessRecordRecorder(accessRecordResource);
      IncrementalPropertyAccessExecutionListener listener = new IncrementalPropertyAccessExecutionListener(
          accessRecorder);
      EglTemplateFactory tf = (EglTemplateFactory) IncrementalLazyEgxModule.this
          .getTemplateFactory();
      tf.getContext().getExecutorFactory().addExecutionListener(listener);
      accessRecorder.startRecording();

      // init
      accessRecorder.setTemplateUri(templateUri);
      accessRecorder.setContextElement(contextObject);
      accessRecorder.setRule(generationRule);
      accessRecorder.setPath(path);

      // gen and save
      String content = wrappedPromise.getContent();
      accessRecorder.updateCurrentPropertyAccessesPath(path);
      accessRecorder.saveToAccessRecordResource();

      // clean
      accessRecorder.setTemplateUri(null);
      accessRecorder.setContextElement(null);
      accessRecorder.setRule(null);
      accessRecorder.setPath(null);

      accessRecorder.stopRecording();
      listener.removeRecorder(accessRecorder);
      tf.getContext().getExecutorFactory().removeExecutionListener(listener);

      return content;
    }

    @Override
    public Collection<Variable> getVariables() {
      return wrappedPromise.getVariables();
    }
  }

}
