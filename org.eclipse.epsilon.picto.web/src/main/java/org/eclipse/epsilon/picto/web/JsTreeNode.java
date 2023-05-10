/*********************************************************************
* Copyright (c) 2022 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.picto.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsTreeNode {

	protected List<JsTreeNode> children = new ArrayList<>();
	protected String text;
	protected String path;
	protected String icon = "icons/diagram.gif";
	protected String url;

	/***
	 * Find a node with the specified path and remove it.
	 * 
	 * @param path
	 * @return
	 */
	public JsTreeNode removeNode(String path) {
		JsTreeNode result = null;
		Iterator<JsTreeNode> iterator = children.iterator();
		while (iterator.hasNext()) {
			JsTreeNode node = iterator.next();
			result = node.removeNode(path);
			if (result != null) {
				break;
			}
			if (path.equals(node.getPath())) {
				iterator.remove();
				result = node;
				break;
			}

		}

		return result;
	}

	public List<JsTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<JsTreeNode> children) {
		this.children = children;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
