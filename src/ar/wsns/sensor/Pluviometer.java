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

package ar.wsns.sensor;

/**
 * @author Manuel Argüelles
 */
public class Pluviometer extends Sensor {

    private int max;
    private int averageTime;

    public Pluviometer() {
        max = (int) (Math.random() * 60);
        averageTime = (int) (Math.random() * 10 + 8);
    }

    /*
     * f = mm * exp(-0.5 * ((t - u) / 5)^2)
     *     t: hours (0-23)
     *     mm: max value of mm/h (2-60)
     *     u: average time (8-18)
     */
    @Override
    public String readValue(int hour) {
        double value = max * Math.exp(-.5 * Math.pow((hour - averageTime) / 5.0, 2));
        return String.valueOf(value);
    }

    @Override
    public String getType() {
        return "P";
    }

    @Override
    public String toString() {
        return "Pluviometer";
    }

}
