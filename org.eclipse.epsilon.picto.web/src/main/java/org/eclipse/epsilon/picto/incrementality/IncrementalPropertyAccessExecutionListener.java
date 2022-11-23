package org.eclipse.epsilon.picto.incrementality;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.MapLiteralExpression;
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

  @SuppressWarnings("unchecked")
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
          if (recorder instanceof AccessRecordRecorder) {

            // skip if the property is transient or the element contained under a transient
            // property, including upper-level containers' transient properties
            if (modelElement instanceof EObject) {
              EObject element = (EObject) modelElement;
              if ((element.eClass()).getEStructuralFeature(propertyName).isTransient()) {
                continue;
              }

              // check also if container's feature is transient
              EStructuralFeature containingFeature = element.eContainingFeature();
              if (containingFeature != null && containingFeature.isTransient()) {
                continue;
              }

              // check also if super containers' features are also transient
              boolean isTransient = false;
              Set<EStructuralFeature> visitedFeatures = new HashSet<>();
              while (containingFeature != null) {
                if (visitedFeatures.contains(containingFeature)) {
                  break;
                } else {
                  visitedFeatures.add(containingFeature);
                }
                if (containingFeature.isTransient()) {
                  isTransient = true;
                  break;
                }
                if (element.eContainer() == null)
                  break;
                containingFeature = element.eContainer().eContainingFeature();
              }
              if (isTransient)
                continue;
            }

            // record access
            ((AccessRecordRecorder) recorder).record(modelElement, propertyName, result);

          }
        }
      }
    }
    // operation call expression
    else if (ast instanceof OperationCallExpression) {
      OperationCallExpression operationCallExpression = (OperationCallExpression) ast;

      final Object modelElement = operationCallExpression.getTargetExpression();
      final String operationName = operationCallExpression.getNameExpression().getName();

      if (operationName.equals("add")) {

      } //
      else if (operationName.equals("all")) {
        Collection<?> resultList = (Collection<?>) result;
        if (resultList.iterator().hasNext() && resultList.iterator().next() instanceof EObject) {
          for (IPropertyAccessRecorder recorder : this.recorders) {
            if (recorder instanceof AccessRecordRecorder) {
              Iterator<EObject> iterator = (Iterator<EObject>) resultList.iterator();
              while (iterator.hasNext()) {
                EObject member = (EObject) iterator.next();
                ((AccessRecordRecorder) recorder).record(member, null, null);
              }
            }
          }
        }
      }
    }
    // path variable, set access recorder path
    else if (ast instanceof MapLiteralExpression<?, ?>) {
      MapLiteralExpression<?, ?> mapLiteral = (MapLiteralExpression<?, ?>) ast;

      if (mapLiteral.getMapName().equals("Map") && mapLiteral.getParent() instanceof ExecutableBlock<?>
          && ((ExecutableBlock<?>) mapLiteral.getParent()).getText().equals("parameters")) {
        Map<String, ?> map = (Map<String, ?>) result;
        if (map.containsKey("path")) {
          Collection<String> segments = (Collection<String>) map.entrySet().stream()
              .filter(e -> e.getKey().equals("path")).findFirst().map(e -> e.getValue()).orElse(null);
          String path = IncrementalityUtil.getPath(segments);
          for (IPropertyAccessRecorder recorder : this.recorders) {
            if (recorder instanceof AccessRecordRecorder) {
              ((AccessRecordRecorder) recorder).setPath(path);
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
