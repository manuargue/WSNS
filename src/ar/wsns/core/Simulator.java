/**
 * Copyright 2011, Manuel Argüelles
 * 
 * This file is part of WSNS
 * 
 * WSNS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WSNS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with WSNS.  If not, see <http://www.gnu.org/licenses/>.
 */

package ar.wsns.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import javax.swing.Timer;

/**
 * @brief Class used to simulate a network
 * @author Manuel Argüelles
 */
public final class Simulator {

    private Network net;
    private GregorianCalendar date;             // current date
    private Timer timer;

    /**
     * @brief Constructs a simulator
     * @param network Network to simulate
     * @param delay Delay time between steps, in millisenconds
     */
    public Simulator(Network network, int delay) {
        net = network;
        /* date 01/01/2011 at 00:00:00 hs */
        date = new GregorianCalendar(2011, GregorianCalendar.JANUARY, 28, 0, 0, 0);

        /* the timer fires <next> method */
        timer = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                next();
            }
        });
        setDelay(delay);
    }

    /**
     * @brief Constructs a simulator with default delay (1000)
     * @param network Network to simulate
     */
    public Simulator(Network network) {
        this(network, 1000);
    }

    /**
     * @brief Sets the delay time between steps, in millisenconds
     * @param delay The delay time between steps, in millisenconds
     */
    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    /**
     * @brief Performs the actions corresponding to a step and increases the simulator date
     */
    private void next() {
        System.out.println("\n---- Next step: " +
                date.get(GregorianCalendar.HOUR_OF_DAY) + "hs. --------------");

        /* send request to the stations */
        final BaseStation base = net.getBaseStation();
        base.sendRequests(date.get(GregorianCalendar.HOUR_OF_DAY));

        /* write on the data base */
        if (date.get(GregorianCalendar.HOUR_OF_DAY) == 23 && date.get(GregorianCalendar.MINUTE) == 30)
            base.writeDataBase(date);

        incrementTime();
    }

    /**
     * @brief Starts the simulation
     */
    public void run() {
        if (!timer.isRunning()) {          
            System.out.println("\nStarting the simulation...");
            initialize();
            System.gc();                    // call to garbage collector
            net.setModified(false);
            net.getBaseStation().updateDataBase();
            timer.start();
        }
    }

    /**
     * @brief Stops the simulation
     */
    public void stop() {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("Simulation stopped");
        }
    }

    /**
     * @brief Increases the date by half-hour
     */
    private void incrementTime() {
        date.roll(GregorianCalendar.MINUTE, 30);
        
        if (date.get(GregorianCalendar.MINUTE) == 0) {
            date.roll(GregorianCalendar.HOUR_OF_DAY, true);       
            if (date.get(GregorianCalendar.HOUR_OF_DAY) == 0) {
                if (date.get(GregorianCalendar.DAY_OF_MONTH) == 
                    date.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) {
                    
                    date.roll(GregorianCalendar.MONTH, true);
                }
                date.roll(GregorianCalendar.DAY_OF_MONTH, true);
            }
        }
    }

    /**
     * @brief Initializes the simulation and update the route table of the nodes 
     */
    private void initialize() {
        /* restart the calendar */
        date.set(2011, GregorianCalendar.JANUARY, 28, 0, 0, 0);

        /* update the route table of the nodes */
        for (Node node: net.getNodes()) {
            System.out.println("Updating the route table of " + node.getName());
            node.getRouter().updateRouteTable();
        }
    }

}
