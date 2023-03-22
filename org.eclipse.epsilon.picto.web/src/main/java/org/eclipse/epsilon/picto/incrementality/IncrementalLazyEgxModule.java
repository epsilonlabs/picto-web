package org.eclipse.epsilon.picto.incrementality;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.egl.EglTemplate;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.concurrent.EgxModuleParallelGenerationRuleAtoms;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.egl.execute.context.IEgxContext;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolAnyType;
import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.epsilon.erl.execute.context.concurrent.ErlContextParallel;
import org.eclipse.epsilon.picto.ContentPromise;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.PictoOperationContributor;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.test.PerformanceRecorder;
import org.eclipse.epsilon.pinset.DatasetRule;
import org.eclipse.epsilon.pinset.PinsetModule;

/***
 * Picto's EglModule to record the execution of an EGX transformation to record
 * model contextElement property accesses. This can be useful e.g. for coverage
 * analysis or incremental execution of EGX transformations. The same technique
 * can be used to record contextElement property accesses in other Epsilon
 * languages. too.
 */
public class IncrementalLazyEgxModule extends EgxModuleParallelGenerationRuleAtoms {

  protected AccessRecordResource accessRecordResource;
  protected List<String> paths = new ArrayList<>();

  protected long promiseDetectionTime;

  public AccessRecordResource getIncrementalResource() {
    return accessRecordResource;
  }

  public IncrementalLazyEgxModule(AccessRecordResource accessRecordResource) {

    this.accessRecordResource = accessRecordResource;

//    templateFactory = new EglFileGeneratingTemplateFactory() {
//      @Override
//      protected EglTemplate createTemplate(EglTemplateSpecification spec) throws Exception {
//        EglTemplate template = super.createTemplate(spec);
//        IncrementalLazyEgxModule.this.template = template;
//        return template;
//      }
//    };
//    this.setTemplateFactory(templateFactory);
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

  @SuppressWarnings("unchecked")
  @Override
  protected Object processRules() throws EolRuntimeException {

//    if (getContext() instanceof ErlContextParallel) {
//      Collection<IncrementalLazyGenerationRuleContentPromise> parallelPromises = (Collection<IncrementalLazyGenerationRuleContentPromise>) ((ErlContextParallel) getContext())
//          .executeJob(getAllJobs());
//      parallelPromises = parallelPromises.parallelStream().filter(p -> {
//        try {
//          return !p.getGenerationRule().isLazy(context);
//        } catch (EolRuntimeException e) {
//          e.printStackTrace();
//        }
//        return false;
//      }).collect(Collectors.toList());
//      return parallelPromises;
//    }

//    else return getContext().executeAll(this, getAllJobs());

    IEgxContext context = getContext();
    ExecutorFactory ef = context.getExecutorFactory();
    context.getOperationContributorRegistry().add(new PictoOperationContributor(this));
    Collection<? extends GenerationRule> rules = getGenerationRules();
//
//    Collection<LazyGenerationRuleContentPromise> promises = rules.parallelStream()
//        .filter(rule -> {
//          
//      boolean result = false;
//      try {
//        result = !rule.isLazy(context);
//      } catch (EolRuntimeException e) {
//        e.printStackTrace();
//      }
//      return result;
//    }).parallel()
//        .map(rule -> {
//      Collection<? extends LazyGenerationRuleContentPromise> result = null;
//      try {
//        result = (Collection<? extends LazyGenerationRuleContentPromise>) ef.execute(rule, context);
//      } catch (EolRuntimeException e) {
//        e.printStackTrace();
//      }
//      return result;
//    }).parallel().flatMap(l -> l.stream()).collect(Collectors.toList());

    Collection<LazyGenerationRuleContentPromise> promises = new ArrayList<>(rules.size());
    for (GenerationRule rule : rules) {
      if (!rule.isLazy(context)) {
        promises.addAll((Collection<? extends LazyGenerationRuleContentPromise>) ef.execute(rule, context));
      }
    }

    return promises;
  }

  /**
   * The generation rule of Picto's incremental approach.
   * 
   * @author Alfa Yohannis
   *
   */
  public class IncrementalLazyGenerationRule extends GenerationRule {

    @Override
    public Object execute(IEolContext context_, Object contextObject) throws EolRuntimeException {

      // initialise recording
      AccessRecordRecorder accessRecorder = new AccessRecordRecorder(accessRecordResource);
      accessRecorder.setContextElement(contextObject);
      accessRecorder.setRule(this);

      IncrementalPropertyAccessExecutionListener listener = new IncrementalPropertyAccessExecutionListener(
          accessRecorder);
      IncrementalLazyEgxModule.this.getContext().getExecutorFactory().addExecutionListener(listener);
      accessRecorder.startRecording();

      /** START: Execute what LazyEgxModule performs **/
      IEgxContext context = (IEgxContext) context_;
//      IEgxContextParallel context = (IEgxContextParallel) context_;
      FrameStack frameStack = context.getFrameStack();

      if (sourceParameter != null) {
        frameStack.enterLocal(FrameType.PROTECTED, this,
            Variable.createReadOnlyVariable(sourceParameter.getName(), contextObject));
      } else {
        frameStack.enterLocal(FrameType.PROTECTED, this);
      }

      if (guardBlock != null && !guardBlock.execute(context, false)) {
        frameStack.leaveLocal(this);
        return null;
      }

      if (preBlock != null) {
        preBlock.execute(context, false);
      }

      String templateName = (templateBlock == null) ? null : templateBlock.execute(context, false);
      EglTemplateFactory templateFactory = context.getTemplateFactory();
      Map<URI, EglTemplate> templateCache = context.getTemplateCache();
      List<Variable> variables = new ArrayList<>();
      URI templateUri = null;

      if (templateName != null) {
        templateUri = templateFactory.resolveTemplate(templateName);
        accessRecorder.setTemplateUri(templateUri);
      }

      if (sourceParameter != null) {
        variables.add(Variable.createReadOnlyVariable(sourceParameter.getName(), contextObject));
      }

      if (parametersBlock != null) {
        EolMap<String, ?> x = parametersBlock.execute(context, false);
        for (Map.Entry<String, ?> entry : x.entrySet()) {
          variables.add(new Variable(entry.getKey(), entry.getValue(), EolAnyType.Instance, false));
        }
      }

      frameStack.leaveLocal(this);
      /** END **/

      // -----
      String path = (accessRecorder.getPath() == null) ? null : new StringBuffer(accessRecorder.getPath()).toString();

      accessRecorder.updateCurrentPropertyAccessesPath(path);
      accessRecorder.saveToAccessRecordResource();

      accessRecorder.stopRecording();
      listener.removeRecorder(accessRecorder);
      IncrementalLazyEgxModule.this.getContext().getExecutorFactory().removeExecutionListener(listener);

      if (path != null)
        paths.add(path);

//      /*** START FILTER THE PROMISES ***/
//      if (!PictoApplication.isNonIncremental()) {
//        long startTime = System.currentTimeMillis();
//        Set<String> invalidatedViewPaths = new HashSet<String>();
//        ((AccessGraphResource) accessRecordResource).checkPath((EgxModule) context.getModule(), invalidatedViewPaths,
//            new HashSet<String>(), new HashSet<EObject>(), path);
//        promiseDetectionTime += (System.currentTimeMillis() - startTime);
//        // if invalidated views is empty (the path doesn't need regeneration) then there
//        // is no need to generate the promise, instead skip to the next element.
//        if (invalidatedViewPaths.size() == 0) {
//          return null;
//        }
//      }
//      /*** END FILTER THE PROMISES ***/

      IncrementalLazyGenerationRuleContentPromise promise = new IncrementalLazyGenerationRuleContentPromise(
          contextObject, this, path, templateFactory, templateUri, variables, templateCache);

      return promise;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(IEolContext context) throws EolRuntimeException {
      Collection<IncrementalLazyGenerationRuleContentPromise> promises = new ArrayList<>();
      ExecutorFactory ef = context.getExecutorFactory();
      Collection<?> elements = getAllElements(context);
      for (Object element : elements) {

//        if (!PictoApplication.isNonIncremental()) {
//          
//          long startTime = System.currentTimeMillis();
//          
//          /**
//           * Get the path, if the path is still valid then skip the generation of the
//           * promise
//           */
//          FrameStack frameStack = context.getFrameStack();
//          if (sourceParameter != null) {
//            frameStack.enterLocal(FrameType.PROTECTED, this,
//                Variable.createReadOnlyVariable(sourceParameter.getName(), element));
//          } else {
//            frameStack.enterLocal(FrameType.PROTECTED, this);
//          }
//          String path = null;
//          if (parametersBlock != null) {
//            EolMap<String, ?> parameters = parametersBlock.execute(context, false);
//            Entry<String, ?> entry = parameters.entrySet().stream().filter(e -> e.getKey().equals("path")).findFirst()
//                .orElse(null);
//            path = "/" + String.join("/", (Collection<String>) entry.getValue());
//          }
//          frameStack.leaveLocal(this);
//
//          
//          Set<String> invalidatedViewPaths = new HashSet<String>();
//          ((AccessGraphResource) accessRecordResource).checkPath((EgxModule) context.getModule(), invalidatedViewPaths,
//              new HashSet<String>(), new HashSet<EObject>(), path);
//          promiseDetectionTime += (System.currentTimeMillis() - startTime);
//
//          // if invalidated views is empty (the path doesn't need regeneration) then there
//          // is no need to generate the promise, instead skip to the next element.
//          if (invalidatedViewPaths.size() == 0) {
//            continue;
//          }
//          /***/
//        }

        Object result = ef.execute(this, context, element);
        if (result instanceof IncrementalLazyGenerationRuleContentPromise) {
          promises.add((IncrementalLazyGenerationRuleContentPromise) result);
        }
      }
      return promises;
    }

    public Parameter getSourceParameter() {
      return sourceParameter;
    }

  }

  /***
   * The Content Promise of the Picto's incremental approach.
   * 
   * @author Alfa Yohannis
   *
   */
  public class IncrementalLazyGenerationRuleContentPromise implements ContentPromise {

    protected EglTemplateFactory templateFactory;
    protected Map<URI, EglTemplate> templateCache;
    protected URI templateUri;
    protected Collection<Variable> variables;
    protected String path;
    protected GenerationRule generationRule;
    protected Object contextObject;

    public IncrementalLazyGenerationRuleContentPromise(Object contextObject, GenerationRule generationRule, String path,
        EglTemplateFactory templateFactory, URI templateUri, List<Variable> variables,
        Map<URI, EglTemplate> templateCache) {
      this.generationRule = generationRule;
      this.path = path;
      this.contextObject = contextObject;
      this.templateFactory = templateFactory;
      this.variables = variables;
      this.templateUri = templateUri;
      this.templateCache = templateCache;
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

      if (templateUri == null)
        return "";

      // handle pinset
      if (templateUri.toString().endsWith(".pinset")) {

        // start recording
        AccessRecordRecorder accessRecorder = new AccessRecordRecorder(accessRecordResource);
        accessRecorder.setContextElement(contextObject);
        accessRecorder.setRule(generationRule);
        accessRecorder.setTemplateUri(templateUri);

        IncrementalPropertyAccessExecutionListener listener = new IncrementalPropertyAccessExecutionListener(
            accessRecorder);
        IncrementalLazyEgxModule.this.getContext().getExecutorFactory().addExecutionListener(listener);
        accessRecorder.startRecording();
        // --

        PinsetModule module = new PinsetModule();
        module.setContext(context);
        module.persistDatasets(false);
        module.parse(templateUri);

        String ruleName = null;
        for (Variable variable : variables) {
          if (variable.getName().equalsIgnoreCase("pinsetrule")) {
            ruleName = "" + variable.getValue();
            break;
          }
        }

        DatasetRule rule = null;
        if (ruleName != null) {
          rule = module.getDatasetRule(ruleName);
          if (rule == null) {
            throw new RuntimeException("Pinset rule \"" + ruleName + "\" not found");
          }
        } else {
          rule = module.getDatasetRules().get(0);
        }

        context.getFrameStack().enterLocal(FrameType.PROTECTED, module,
            variables.toArray(new Variable[variables.size()]));

        // execute model
        module.preExecution();
        rule.execute(module.getContext());

        context.getFrameStack().leaveLocal(module);

        String content = rule.getDataset().toString();

        // save record
        accessRecorder.updateCurrentPropertyAccessesPath(path);
        accessRecorder.saveToAccessRecordResource();

        // stop recording
        accessRecorder.stopRecording();
        listener.removeRecorder(accessRecorder);
        IncrementalLazyEgxModule.this.getContext().getExecutorFactory().removeExecutionListener(listener);
        // --

        rule.dispose();

        return content;
      }

      // handle common template
      EglTemplate template = null;
      if (templateCache == null || (template = templateCache.get(templateUri)) == null) {
        template = templateFactory.load(templateUri);
        if (templateCache != null) {
          templateCache.put(templateUri, template);
        }
      }

      for (Variable variable : variables) {
        template.populate(variable.getName(), variable.getValue());
      }

      // initialise access recorder
      AccessRecordRecorder accessRecorder = new AccessRecordRecorder(accessRecordResource);
      accessRecorder.setTemplateUri(templateUri);
      accessRecorder.setContextElement(contextObject);
      accessRecorder.setRule(generationRule);
      accessRecorder.setPath(path);

      IncrementalPropertyAccessExecutionListener listener = new IncrementalPropertyAccessExecutionListener(
          accessRecorder);
      template.getModule().getContext().getExecutorFactory().addExecutionListener(listener);
      accessRecorder.startRecording();

      // process template
      String content = template.process();
      template.reset();

      // save records
      accessRecorder.updateCurrentPropertyAccessesPath(path);
      accessRecorder.saveToAccessRecordResource();

      // stop recording
      accessRecorder.stopRecording();
      listener.removeRecorder(accessRecorder);
      template.getModule().getContext().getExecutorFactory().addExecutionListener(listener);
      // ----

      return content;
    }

    public Collection<Variable> getVariables() {
      return variables;
//      return wrappedPromise.getVariables();
    }
  }

  public long getPromiseDetectionTime() {
    return promiseDetectionTime;
  }

  public List<String> getPaths() {
    return paths;
  }
}
