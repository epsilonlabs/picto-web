/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

/*** PICTO ****/

var Picto = new Object();

Picto.treeContent = null;
Picto.pictoFile = null;
Picto.socket = null;
Picto.stompClient = null;
Picto.treeView = null;
Picto.selectedPathOld = null;
Picto.selectedPath = null;
Picto.viewContents = new Map();
Picto.tempSelectedNode = null; // temporary holder when refreshing jstree
Picto.tempSelectedPath = null; // temporary holder when refreshing jstree
Picto.views = new Map();
Picto.disableJsTreeOnSelectEvent = false;

/**
 * Covert a json object to string. The object contains the information of the file selected,
 * the type of the document, and the message.
 * @param {*} pictoFile 
 * @param {*} type 
 * @param {*} message 
 * @returns 
 */
Picto.convertToPictoRequest = function (pictoFile, type, message) {
  return JSON.stringify(
    {
      "pictoFile": pictoFile,
      "type": type,
      "code": message
    }
  );
}

/**
 * Convert the list of paths received from Picto Web server into JsTreeData 
 * for the view tree on the left panel.
 * @param {*} list 
 * @returns 
 */
Picto.listToJsTreeData = function (list) {
  var tree = [];
  var t = tree;
  for (key1 in list) {
    // console.log(list[key1]);
    var segments = list[key1].split("\/");
    for (key2 in segments) {
      var node = null;
      var i = t.findIndex(e => e.text === segments[key2]);
      if (i > -1) {
        node = t[i];
      } else {
        t.push({});
        node = t[t.length - 1];
        node["text"] = segments[key2];
        node["children"] = [];
      }
      if (key2 == segments.length - 1) {
        node["icon"] = "icons/document.gif";
      } else {
        node["icon"] = "icons/folder.gif";
      }
      t = node["children"];

    }
    t = tree;
  }
  // console.log(tree);
  return tree;
}

// /***
//  * Get the TreeView.
//  */
// Picto.executeCode = function () {
//   // console.log("Get TreeView ...");
//   this.stompClient.send("/app/treeview", {}, this.convertToPictoRequest(this.pictoFile, "TreeView", ""));
//   // console.log("Code sent.");
// }

// Picto.projectTree = function () {
//   // console.log("Get Project Tree ...");
//   this.stompClient.send("/app/projecttree", {}, this.convertToPictoRequest(this.pictoFile, "ProjectTree", ""));
//   // console.log("Code sent.");
// }

// Picto.getTreeView = function () {
//   // console.log("Get Treeview ...");
//   this.stompClient.send("/app/treeview", {}, this.convertToPictoRequest(this.pictoFile, "TreeView", ""));
//   // console.log("Code sent.");
// }

// Picto.openFile = function (filename) {
//   // console.log("Open File ...");
//   this.stompClient.send("/app/treeview", {}, this.convertToPictoRequest(this.pictoFile, "TreeView", filename));
//   //this.stompClient.send("/app/openfile", {}, this.convertToPictoRequest(this.pictoFile, "OpenFile", filename));
//   // console.log("Code sent.");
// }

// Picto.displayResult = function (message) {
//   $("#greetings").append("<tr><td>" + message + "</td></tr>");
// }

/**
 * Create the the tree on the left panel.
 * @param {*} treeView 
 * @returns 
 */
Picto.createTree = function (treeView) {
  var tree = [];
  path = '';
  tree = Picto.recursiveTree(tree, treeView, path);
  return tree;
}

/**
 * Recursively create the view tree on the left panel.
 * @param {*} tree 
 * @param {*} treeView 
 * @param {*} path 
 * @returns 
 */
Picto.recursiveTree = function (tree, treeView, path) {
  for (key in treeView.children) {
    var child = treeView.children[key];
    var object = {};
    tree.push(object);
    object['text'] = child['name'];
    object['state'] = { "opened": true };
    object['children'] = [];
    var p = path + "/" + object['text'];
    if (Picto.selectedPathOld == null)
      Picto.selectedPathOld = p;
    Picto.viewContents.set(p, child.content);
    Picto.recursiveTree(object['children'], child, p);
  }
  return tree;
}

/**
 * Get the view path of the currently selected node.
 * @param {*} data The data received from JsTree select_node event.
 * @returns 
 */
Picto.getSelectedViewPath = function (data) {
  var path = '';
  var node = data.instance.get_node(data.selected[0]);
  path = path + node.text;
  while (node.parent != '#') {
    path = '/' + path;
    var parent = data.instance.get_node(node.parent);
    path = parent.text + path;
    node = parent;
  }
  path = '/' + path;
  return path;
}

/**
 * Set and record the zoom level of an svg.
 * @param {*} view 
 * @param {*} svg 
 */
Picto.setZoom = function (view, svg) {
  var zoomedSvg = svgPanZoom(svg,
    {
      minZoom: 0.01, zoomEnabled: true, fit: false, center: true,
      onZoom: function (scale) {
        view.zoomScale = scale;
      },
      onPan: function (pan) {
        view.pan = pan;
      }
    }
  );
  var baseHeight = svg.height.baseVal.value;
  var baseWidth = svg.width.baseVal.value;
  var boundingClientRect = svg.getBoundingClientRect();
  var actualHeight = boundingClientRect.height;
  var actualWidth = boundingClientRect.width;
  var heightRatio = actualHeight / baseHeight;
  var widthRatio = actualWidth / baseWidth;
  var smallestRatio = (heightRatio > widthRatio) ? widthRatio : heightRatio;
  var ratio = (smallestRatio > 1) ? 1 / smallestRatio : 1;
  if (view.zoomScale != null) {
    ratio = view.zoomScale;
  } else {
    view.zoomScale = ratio;
  }
  zoomedSvg.zoom(ratio);
  if (view.pan != null) {
    zoomedSvg.pan(view.pan);
  }
}

/**
 * Render the received view on the visualisation panel, the right-side panel.
 * @param {*} view 
 */
Picto.render = function (view) {

  var localView = Picto.views.get(view.path);

  // old or updated, else is new
  if (localView != null) {
    // old
    if (view.timestamp <= localView.timestamp) {
      view = localView;
    } else { // updated
      // copy the old zoom and pan to the updated one
      view.zoomScale = localView.zoomScale;
      view.pan = localView.pan;
      Picto.views.delete(view.path);
    }
  }
  Picto.views.set(view.path, view);

  var container = document.getElementById("visualisation");
  container.innerHTML = '';
  if (view.type == 'svg') {
    var text = view.content;
    var parser = new DOMParser();
    var xmlDoc = parser.parseFromString(text, "text/xml");
    var svg = xmlDoc.getElementsByTagName("svg")[0];
    container.appendChild(svg);
    Picto.setZoom(view, svg);
  } else if (view.type == 'html') {
    var text = view.content;
    console.log(text);
    if (text.trim() == "") {
      text = "<body></body>";
      container.innerHTML = text;
    } else {
      var parser = new DOMParser();
      var xmlDoc = parser.parseFromString(text, "text/xml");
      var body = xmlDoc.getElementsByTagName("body")[0];

      container.innerHTML = body.innerHTML;

      var svgs = container.getElementsByTagName("svg");
      for (var i = 0; i < svgs.length; i++) {
        var svg = svgs[i];
        svg.setAttribute("style", null);
        Picto.setZoom(view, svg);
      }

    }
  } else if (view.type == 'markdown') {
    var text = view.content;
    var md = marked.parse(text);
    container.innerHTML = md;
  } else if (view.type == 'treeview') {
    Picto.treeContent = JSON.parse(view.content);
    var tree = $('#tree').jstree(true);
    tree.refresh(false, false);
  } else if (view.type = 'newviews') {
    var newViews = JSON.parse(view.content);
    // get the updated current view
    if (newViews.includes(Picto.selectedPath)) {
      var segments = Picto.selectedPath.split("/");
      var name = segments[segments.length - 1];
      var label = pictoName + '#' + Picto.selectedPath;
      var url = '/picto?file=' + pictoName + '&path=' + Picto.selectedPath
        + '&name=' + name;
      Picto.draw(label, url);
    }
    // get the updated treeview
    if (newViews.includes('/')) {
      var url = '/picto?file=' + pictoName + '&path=/';
      Picto.draw(label, url);
    }
  } else {
    console.log("Please check since this view type is not handled!");
  }
}

/**
 * Select the specific node of the view tree on the left-side panel.
 * @param {*} path 
 */
Picto.selectJsTreeNode = function (path) {
  var tree = $('#tree').jstree(true);
  var root = tree.get_node('#');
  for (key in root.children_d) {
    var id = root.children_d[key];
    var node = tree.get_node(id);
    var fullPath = "/" + tree.get_path(node, '/');
    if (fullPath == path) {
      tree.select_node(node, false, false);
      tree.open_node(node);
      break;
    }
  }
}

/**
 * Receive the view from Picto Web server.
 * @param {*} event 
 * @returns 
 */
Picto.getView = function (event) {
  if (event.target.readyState == 4 && event.target.status == 200) {
    if (event.target.responseText == null || event.target.responseText == "") {
      return;
    }
    var view = JSON.parse(event.target.responseText);
    Picto.render(view);
  }
}

/**
 * Reqeuest the view of the specified url and push the url state to the history 
 * so that it can also be handled when a user presses the back button. 
 * @param {*} label 
 * @param {*} url 
 * @returns 
 */
Picto.draw = function (label, url) {

  var localView = Picto.views.get(Picto.selectedPath);
  var urlWithTimestamp;
  if (localView != null && localView.timestamp != null) {
    urlWithTimestamp = url + '&timestamp=' + localView.timestamp;
  } else {
    urlWithTimestamp = url;
  }

  var request = new XMLHttpRequest();
  request.addEventListener("load", Picto.getView);
  request.open("GET", "/pictojson" + urlWithTimestamp);
  request.send();

  window.history.pushState(null, label, url);

  return false;
}

/**
 * Connect the client to Picto Web server.
 */
Picto.connectToServer = function (pictoFile) {
  this.pictoFile = pictoFile;
  this.socket = new SockJS('/picto-web');
  this.stompClient = Stomp.over(this.socket);
  this.stompClient.connect({}, function (frame) {
    Picto.stompClient.subscribe('/topic/picto' + Picto.pictoFile, function (message) {
      var view = JSON.parse(message.body);
      if (view == null || !'type' in view) {
        return;
      }
      if (!['treeview', 'newviews', Picto.selectedPath].includes(view.type)) {
        return;
      }
      Picto.render(view);

    });
  }
  );
}

/**
 * Add the show view function. It adds a link to another view in the SVG produced.
 */
var top = new Object();
top.showView = function (param) {
  path = "/" + param.join("/");
  Picto.selectJsTreeNode(path);
};


