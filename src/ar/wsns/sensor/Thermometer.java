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
public class Thermometer extends Sensor {

    private int averageTemp;

    public Thermometer() {
        averageTemp = (int) (Math.random() * 10 + 20);
    }

    /*
     * T = Tm * (.9 + .2 * exp(-.1 * t))
     *  t: hours (0-23)
     *  Tm: average temperature (20-30)
     */
    @Override
    public String readValue(int hour) {
        double value = averageTemp * (.9 + .2 * Math.exp(-.1 * hour));
        return String.valueOf(value);
    }

    @Override
    public String getType() {
        return "T";
    }

    @Override
    public String toString() {
        return "Thermometer";
    }

}
