/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.picto;

import java.util.Iterator;

import org.eclipse.epsilon.picto.dummy.ViewPart;
import org.eclipse.swt.widgets.Composite;

public class PictoView extends ViewPart {

	public static final String ID = "org.eclipse.epsilon.picto.PictoView";

	protected ViewRenderer viewRenderer = new ViewRenderer(null);
	protected ViewTree rootViewTree = new ViewTree();

	public ViewRenderer getViewRenderer() {
		return viewRenderer;
	}

	public ViewTree getViewTree() {
		return (ViewTree) rootViewTree;
	}

	@Override
	public void createPartControl(Composite arg0) {

	}

	public void renderView(ViewTree view) throws Exception {

		// Check if one of the source contents of the view is active
		ViewContent content = null;
		for (Iterator<ViewContent> contentIterator = view.getContents(this).iterator(); contentIterator.hasNext()
				&& content == null;) {
			ViewContent next = contentIterator.next();
			if (next.isActive()) {
				content = next.getSourceContent(this);
			}
		}

		// ... if not, show the final rendered result
		if (content == null)
			content = view.getContent().getFinal(this);
//		viewRenderer.display(content.getText());

	}

	@Override
	public void setFocus() {

	}

}
