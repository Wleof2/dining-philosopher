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

/**
 * describes the state of a philosopher
 */
public enum Status {
  THINKING(0),
  HUNGRY(1),
  EATING(2);

  private int i;

  Status(int i) {
    this.i = i;
  }

  /**
   * @return a string representing the state
   */
  public String toString() {
    switch (i) {
      case 1:
        return "HUNGRY";
      case 2:
        return "EATING";
      default:
        return "THINKING";
    }
  }
}
