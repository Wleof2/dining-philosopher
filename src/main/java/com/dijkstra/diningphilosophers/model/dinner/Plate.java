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

package com.dijkstra.diningphilosophers.model.dinner;

import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;

import com.dijkstra.diningphilosophers.Resource;

import java.util.concurrent.atomic.AtomicBoolean;

public class Plate {
  // View Node
  private GridPane plateInfo;

  // Fork Status
  private AtomicBoolean left;
  private AtomicBoolean right;

  private Plate(GridPane plateInfo, int id) {
    this.plateInfo = plateInfo;
    this.setId(id);
    this.clear();

    this.left = new AtomicBoolean(false);
    this.right = new AtomicBoolean(false);
  }

  /**
   * sets the id of the philosopher's plate
   *
   * @param id
   */
  public void setId(int id) {
    Text text = (Text) plateInfo.lookup("#id");
    text.setText(String.valueOf(id));
  }

  /**
   * sets the state of the philosopher's plate
   *
   * @param status
   */
  public void setStatus(String status) {
    Text text = (Text) plateInfo.lookup("#status");
    text.setText(status);
  }

  /**
   * enables or disables a fork by id
   *
   * @param id
   * @param taked
   */
  public void setFork(int id, boolean taked) {
    SVGPath fork =
        (SVGPath) plateInfo.lookup("#fork_" + id);
    fork.fillProperty().set(Paint.valueOf(taked ?
        "#000000" : "#AAAAAA"));
  }

  /**
   * @return the node of the plate
   */
  public GridPane getNode() {
    return this.plateInfo;
  }

  /**
   * disables the two forks
   */
  public void clear() {
    this.setFork(0, false);
    this.setFork(1, false);
  }

  /**
   * enables the left fork
   */
  public void takeLeft() {
    this.left.set(true);
  }

  /**
   * enables the right fork
   */
  public void takeRight() {
    this.right.set(true);
  }

  /**
   * disables the left fork
   */
  public void putLeft() {
    this.left.set(false);
  }

  /**
   * disables the right fork
   */
  public void putRight() {
    this.right.set(false);
  }

  /**
   * updates the view nodes
   *
   * @param status
   */
  public void refresh(String status) {
    this.setFork(0, this.left.get());
    this.setFork(1, this.right.get());
    this.setStatus(status);
  }

  /**
   * @param id
   * @return a plate instance
   */
  public static Plate from(int id) {
    GridPane plateInfo =
        (GridPane) Resource.PLATE_INFO_FACTORY.getNode();

    return new Plate(plateInfo, id);
  }
}
