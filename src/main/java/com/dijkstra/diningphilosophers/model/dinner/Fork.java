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

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
  private Lock mutex;
  private Rectangle rectangle;

  private Fork(Rectangle rectangle) {
    this.rectangle = rectangle;
    this.mutex = new ReentrantLock();
    this.setVisibility(true);
  }

  /**
   * checks if the fork can be taken off the table
   * @return true, if the fork can be taken off the table
   */
  public boolean canTake() {
    if(this.mutex.tryLock()) {
      this.mutex.unlock();
      return true;
    }

    return false;
  }

  /**
   * takes the fork from the table
   */
  public void take() {
    this.mutex.lock();
    this.setVisibility(false);
  }

  /**
   * puts the fork back on the table
   */
  public void put() {
    this.mutex.unlock();
    this.setVisibility(true);
  }

  /**
   * sets the visibility of the table fork
   * @param v
   */
  private void setVisibility(boolean v) {
    this.rectangle.visibleProperty().set(v);
  }

  /**
   * disables the table fork
   */
  public void clear() {
    this.mutex = new ReentrantLock();
    this.setVisibility(true);
  }

  /**
   * @param root
   * @param id
   * @return a fork instance
   */
  public static Fork from(Pane root, int id) {
    Node rectangle = root.lookup(
        "#fork_" + id);
    return new Fork((Rectangle) rectangle);
  }
}
