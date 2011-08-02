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
public class Anemometer extends Sensor {

    private int averageSpeed;
    private int variation;

    public Anemometer() {
        variation = (int) (Math.random() * 9 + 1);
        averageSpeed = (int) (Math.random() * 200 + 10);
    }

    /*
     * f = v + o * exp(-.5 * ((t - 12) / 8)^2) * sin(2 * t)
     *      t: hour (0-23)
     *      v: average wind speed (10-200)
     *      o: variation (1-10)
     */
    @Override
    public String readValue(int hour) {
        double value = averageSpeed + variation * Math.exp(-.5 *
                       Math.pow((hour - 12) / 8, 2)) * Math.sin(2 * hour);
        return String.valueOf(value);
    }

    @Override
    public String getType() {
        return "A";
    }

    @Override
    public String toString() {
        return "Anemometer";
    }

}
