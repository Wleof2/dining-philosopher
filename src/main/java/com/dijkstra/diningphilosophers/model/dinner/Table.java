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

import com.dijkstra.diningphilosophers.model.worker.Status;
import com.dijkstra.diningphilosophers.model.worker.Philosopher;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;

public class Table {
  public static int N = 5;

  private static Table instance;

  private Pane root;
  private Fork[] forks;
  private Plate[] plates;
  private GridPane[] gridPanes;
  private Philosopher[] philosophers;

  public Table(Pane root) {
    this.root = root;

    this.forks = new Fork[Table.N];
    this.plates = new Plate[Table.N];
    this.gridPanes = new GridPane[Table.N];
    this.philosophers = new Philosopher[Table.N];

    for (int i = 0; i < Table.N; i++) {
      this.forks[i] = Fork.from(this.root, i);
      this.gridPanes[i] = (GridPane) root.lookup("#plate_" + i);
    }
  }

  /**
   * @param root
   * @return the table instance
   */
  public static Table getInstance(Pane root) {
    if (Table.instance == null) {
      Table.instance = new Table(root);
    }

    return Table.instance;
  }

  /**
   * refresh the view with the philosopher's information
   */
  public void refresh() {
    for (Philosopher philosopher : this.philosophers) {
      philosopher.refresh();
    }
  }

  /**
   * @param id
   * @return the left index
   */
  public static int getLeft(int id) {
    return (id + Table.N) % Table.N;
  }

  /**
   * @param id
   * @return the right index
   */
  public static int getRight(int id) {
    return (id + 1) % Table.N;
  }

  /**
   * @param id
   * @return the left fork
   */
  public Fork getLeftFork(int id) {
    return this.forks[Table.getLeft(id)];
  }

  /**
   * @param id
   * @return the right fork
   */
  public Fork getRightFork(int id) {
    return this.forks[Table.getRight(id)];
  }

  /**
   * checks if the two forks can be removed (Dijkstra's
   * solution)
   *
   * @param philosopher
   * @return true, if the two forks can be removed
   */
  public boolean canTake(Philosopher philosopher) {
    int id = philosopher.ID;

    Fork left = this.getLeftFork(id);
    Fork right = this.getRightFork(id);

    return left.canTake() && right.canTake();
  }

  /**
   * atomically takes the forks
   *
   * @param philosopher
   * @throws InterruptedException
   */
  public void takeForks(Philosopher philosopher) throws InterruptedException {
    int id = philosopher.ID;

    Fork left = this.getLeftFork(id);
    Fork right = this.getRightFork(id);

    Plate plate = philosopher.getNode();

    left.take();
    plate.takeLeft();

    // Hello, Deadlock
    philosopher.randomSleep();

    right.take();
    plate.takeRight();

    philosopher.setStatus(Status.EATING);
  }

  /**
   * atomically puts the forks
   *
   * @param philosopher
   */
  public void putForks(Philosopher philosopher) {
    int id = philosopher.ID;

    Fork left = this.getLeftFork(id);
    Fork right = this.getRightFork(id);
    Plate plate = philosopher.getNode();

    left.put();
    plate.putLeft();

    right.put();
    plate.putRight();

    philosopher.setStatus(Status.THINKING);
  }

  /**
   * adds a philosopher to the table
   *
   * @param philosopher
   */
  public void addPhilosopher(Philosopher philosopher) {
    Plate plate = philosopher.getNode();
    this.plates[philosopher.ID] = plate;
    this.gridPanes[philosopher.ID].add(plate.getNode(), 0, 0);
    this.philosophers[philosopher.ID] = philosopher;
  }

  /**
   * clears the table for a new dinner
   */
  public void clear() {
    for (int i = 0; i < Table.N; i++) {
      if (this.plates[i] != null) {
        Plate plate = this.plates[i];

        plate.putLeft();
        plate.putRight();

        if (this.gridPanes[i].getChildren().contains(plate.getNode())) {
          this.gridPanes[i].getChildren().remove(plate.getNode());
        }
      }

      this.forks[i].clear();
    }
  }
}
