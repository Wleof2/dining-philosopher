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

package com.dijkstra.diningphilosophers.model.worker;

import com.dijkstra.diningphilosophers.Controller;
import com.dijkstra.diningphilosophers.model.dinner.Plate;

public class Philosopher extends Worker {
  public final int ID;

  private Plate plate;
  private Status status;

  private Philosopher(Controller controller,
                      int id, Plate plate) {
    super(controller);

    this.ID = id;
    this.plate = plate;
    this.status = Status.THINKING;
  }

  /**
   * @param controller
   * @param id
   * @return a philosopher instance
   */
  public static Philosopher from(Controller controller,
                                 int id) {
    Plate plate = Plate.from(id);
    return new Philosopher(controller, id, plate);
  }

  @Override
  public void workerRun() throws InterruptedException {
    this.controller.work(this);
  }

  /**
   * @return the philosopher status
   */
  public Status getStatus() {
    return this.status;
  }

  public synchronized void setStatus(Status status) {
    this.status = status;
  }

  /**
   * @return the node of the plate
   */
  public Plate getNode() {
    return this.plate;
  }

  /**
   * updates the view nodes
   */
  public void refresh() {
    this.plate.refresh(this.status.toString());
  }
}
