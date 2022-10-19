/*
 *    Copyright 2022 Wylton Leone<wleof2@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dijkstra.diningphilosophers.model;

import javafx.scene.Node;
import com.dijkstra.diningphilosophers.Resource;

/**
 * Provides a model for creating nodes
 */
public class NodeFactory {
  private String name;

  public NodeFactory(String name) {
    this.name = name;
  }

  /**
   * Load and instantiate a no from a fxml file
   *
   * @return a node
   */
  public Node getNode() {
    return Resource.loadNode(name);
  }
}
