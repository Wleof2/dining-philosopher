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

package com.dijkstra.diningphilosophers;

import java.net.URL;
import java.io.IOException;

import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import com.dijkstra.diningphilosophers.model.NodeFactory;

/**
 * provides functions to load resources
 */
public class Resource {
  // Dinner
  public static NodeFactory PLATE_INFO_FACTORY =
      loadNodeFactory("plate_info.fxml");

  /**
   * creates an instance of a node factory from a fxml
   *
   * @param name
   * @return a NodeFactory instance
   */
  public static NodeFactory loadNodeFactory(String name) {
    return new NodeFactory(name);
  }

  /**
   * creates an instance of a node from a fxml
   *
   * @param name
   * @return a Node instance
   */
  public static Node loadNode(String name) {
    URL url = com.dijkstra.diningphilosophers.View.class.getResource(name);

    try {
      FXMLLoader loader = new FXMLLoader(url);
      return loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
