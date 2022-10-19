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

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker {
    protected Controller controller;

    private AtomicBoolean pauseSignal;
    private AtomicBoolean keepAliveSignal;

    private final Thread thread;

    public static final SecureRandom secureRandom =
        new SecureRandom();

    public Worker(Controller controller) {
        this.pauseSignal = new AtomicBoolean(false);
        this.keepAliveSignal = new AtomicBoolean(false);

        this.controller = controller;

        this.thread = new Thread(() -> {
            while (keepAliveSignal.get()) {
                if(this.pauseSignal.get()) {
                    Thread.yield();
                    continue;
                }

                try {
                    workerRun();
                    Worker.this.sleep(2000);
                } catch (Exception e) {}

                Thread.yield();
            }
        });
    }

    /**
     * interrupts the worker
     */
    public void interrupt() {
        this.keepAliveSignal.set(false);
        this.thread.interrupt();
    }

    /**
     * pauses the worker
     */
    public void pause() {
        this.pauseSignal.set(true);
    }

    /**
     * resume the worker
     */
    public void resume() {
        this.pauseSignal.set(false);
    }

    public void workerRun() throws InterruptedException {}

    /**
     * makes the worker sleep for a time
     * @param time
     * @throws InterruptedException
     */
    public void sleep(int time) throws InterruptedException {
        Thread.yield();
        Thread.sleep(time);
        Thread.yield();
    }

    /**
     * makes the worker sleep for a random time
     * @throws InterruptedException
     */
    public void randomSleep() throws InterruptedException {
        sleep(Worker.secureRandom.nextInt(1000));
    }

    /**
     * refresh the view node with the worker state
     */
    public void refresh() {
    }

    /**
     * initializes the worker
     */
    public void start()  {
        this.pauseSignal.set(false);
        this.keepAliveSignal.set(true);
        this.thread.start();
    }
}
