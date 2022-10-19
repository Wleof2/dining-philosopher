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

import com.dijkstra.diningphilosophers.model.SimulationStatus;
import com.dijkstra.diningphilosophers.model.worker.Philosopher;
import com.dijkstra.diningphilosophers.model.worker.Status;
import com.dijkstra.diningphilosophers.model.dinner.Table;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.animation.AnimationTimer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.dijkstra.diningphilosophers.model.worker.Worker;

/**
 * provide a control structure for the view
 */
public class Controller {
  // View Injection
  @FXML
  private CheckBox enableMutex;

  @FXML
  private Button startButton;

  @FXML
  private Button stopButton;

  @FXML
  private Button pauseButton;

  @FXML
  private Pane root;

//    @FXML
//    private Pane warning;

  // Problem
  private Lock mutex;
  private Table table;
  private int bufferIndex;
  private Worker[] workers;

  // Simulation Status
  private SimulationStatus simulationStatus;

  // Draw Loop
  private AnimationTimer animationTimer;


  @FXML
  private void initialize() {
    this.mutex = new ReentrantLock(true);
    this.table = Table.getInstance(root);

    // Simulation

    this.simulationStatus = SimulationStatus.RESUMED;

    // Init Events

    this.startButton.setOnAction(event -> start());
    this.stopButton.setOnAction(event -> stop());
    this.pauseButton.setOnAction(event -> pause());

    // Init Draw Loop

    this.animationTimer = new AnimationTimer() {
      @Override
      public void handle(long l) {
        if (simulationStatus == SimulationStatus.PAUSED) {
          Thread.yield();
          return;
        }

        table.refresh();
      }
    };

    // Clear View

    this.clear();
  }

  /**
   * initializes the philosophers
   */
  private void startWorkers() {
    this.workers = new Worker[Table.N];
    for (int i = 0; i < Table.N; i++) {
      Philosopher p = Philosopher.from(this, i);
      workers[i] = p;
      this.table.addPhilosopher(p);
      p.start();
    }
  }

  /**
   * starts a philosopher's routine
   * @param philosopher
   * @throws InterruptedException
   */
  public void work(Philosopher philosopher) throws InterruptedException {
    if (philosopher.getStatus() != Status.EATING) {
      philosopher.setStatus(Status.HUNGRY);

      if (this.getEnableMutex()) {
        // take both forks atomically
        this.mutex.lock();
        if (this.table.canTake(philosopher)) {
          this.table.takeForks(philosopher);
          philosopher.randomSleep();
        }
        this.mutex.unlock();
      } else {
        this.table.takeForks(philosopher);
      }
    } else {
      this.table.putForks(philosopher);
    }
  }

  /**
   * @return the current value of the checkbox (EnableMonitors)
   */
  public synchronized boolean getEnableMutex() {
    return enableMutex.isSelected();
  }

  /**
   * set disable property of nodes
   *
   * @param value
   */
  private synchronized void setDisableProperty(boolean value) {
    this.enableMutex.disableProperty().set(value);
  }

  /**
   * displays the start button
   */
  public void showStartButton() {
    this.startButton.visibleProperty().set(true);
    this.pauseButton.visibleProperty().set(false);
    this.stopButton.visibleProperty().set(false);
  }

  /**
   * displays the stop button
   */
  public void showStopButtons() {
    this.startButton.visibleProperty().set(false);
    this.pauseButton.visibleProperty().set(true);
    this.stopButton.visibleProperty().set(true);
  }

  /**
   * initializes the execution
   */
  private synchronized void start() {
    this.clear();

    // Threads
    this.mutex = new ReentrantLock();
    this.startWorkers();
    this.animationTimer.start();

    // View
    this.showStopButtons();
    this.setDisableProperty(true);
  }

  /**
   * pause the execution
   */
  private synchronized void pause() {
    if (this.simulationStatus == SimulationStatus.RESUMED) {
      this.setPauseState();
    } else {
      this.setResumeStatus();
    }
  }

  /**
   * sets the simulation state to resumed
   */
  private void setResumeStatus() {
    this.pauseButton.textProperty().set("PAUSE");
    this.simulationStatus = SimulationStatus.RESUMED;
    if (this.workers != null) {
      for (Worker worker : this.workers) {
        if (worker != null) {
          worker.resume();
        }
      }
    }
  }

  /**
   * sets the simulation status to paused
   */
  private void setPauseState() {
    this.pauseButton.textProperty().set("RESUME");
    this.simulationStatus = SimulationStatus.PAUSED;
    if (this.workers != null) {
      for (Worker worker : this.workers) {
        if (worker != null) {
          worker.pause();
        }
      }
    }
  }

  /**
   * stops the execution
   */
  private synchronized void stop() {
    this.setPauseState();
    this.setDisableProperty(false);

    animationTimer.stop();

    if (this.workers != null) {
      for (Worker worker : workers) {
        if (worker != null) {
          worker.interrupt();
        }
      }

      this.workers = null;
    }

    this.clear();
    this.showStartButton();
  }

  /**
   * clear all view nodes
   */
  private void clear() {
    this.table.clear();
    this.setResumeStatus();
  }
}