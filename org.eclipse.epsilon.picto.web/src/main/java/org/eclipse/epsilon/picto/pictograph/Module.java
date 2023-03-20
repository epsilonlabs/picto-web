package org.eclipse.epsilon.picto.pictograph;

import java.util.LinkedList;
import java.util.List;

public class Module extends InputEntity {

  protected List<Rule> rules = new LinkedList<>();

  public void addRule(Rule rule) {
    if (!this.rules.contains(rule)) {
      this.rules.add(rule);
    }
    rule.setModule(this);
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

}