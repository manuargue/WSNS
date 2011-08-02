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

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import ar.wsns.sensor.Sensor;

/**
 * @brief Represents a base station that is part of a network
 * @author Manuel Argüelles
 */
public final class BaseStation extends Node {
    
    private ArrayList<Link> links;
    private HashMap<Station, String> packets;   // packet data in RAM
    private Connection connection;              // data base connection

    /**
     * @brief Constructs a base station
     * @param name Base station name
     * @param net Network to which this station belongs
     */
    public BaseStation(String name, Network net) {
        super(name, net);
        links = new ArrayList<Link>();
        packets = new HashMap<Station, String>();
        
        /* initialize data base */
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:res/statistics.db");
        } catch (ClassNotFoundException ex) {
            // logger
        } catch (SQLException ex) {
            // logger
        }       
    }

    /**
     * @brief Adds a link, if possible
     * @param link Link to add
     * @throws NodeFullLinksException 
     */
    @Override
    public void addLink(Link link) {
        links.add(link);
    }

    /**
     * @brief Removes a connection to the specified node, if it exists
     * @param nodeTo The other node of the link
     */
    @Override
    public void removeLinkTo(Node nodeTo) {
        for (Link l: links)
            if (l.getNodeTo() == nodeTo) {
                links.remove(l);
                break;
            }
    }

    /**
     * @brief Removes all the connections of this station
     */
    @Override
    public void removeAllLinks() {
        links.clear();
    }

    /**
     * @brief Determines whether this station is connected to a specified node
     * @param nodeTo The other node of the link
     * @return True, if this station is connected to the other node; otherwise
     *         false
     */
    @Override
    public boolean isConnectedTo(Node nodeTo) {
        for (Link l: links)
            if (l.getNodeTo() == nodeTo)
                return true;
        return false;
    }

    /**
     * @brief Gets all the links connected to this station 
     * @return All the links connected to this station
     */
    @Override
    public Link[] getLinks() {
        return links.toArray(new Link[1]);
    }

    /**
     * @brief Gets all the nodes connected to this station
     * @return All the nodes connected to this station, if they exists
     */
    @Override
    public Node[] getNodesConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Gets the link to the specified node
     * @param nodeTo The other node part of the link
     * @return The link to the specified node, if it exists
     */
    @Override
    public Link getLinkTo(Node nodeTo) {
        for (Link l: links)
            if (l.getNodeTo() == nodeTo)
                return l;
        return null;
    }

    /**
     * @brief Sends requests (empty packets) to all the station of the network
     * @param hour Transmission time
     */
    public void sendRequests(int hour) {
        Packet packet = null;
        String data = null;
        Station station = null;

        /* send request to all the stations */
        for (Node nodeTo: router.getRouteTable().keySet()) {
            System.out.println("\nBase sends request to " + nodeTo.getName() +
                    " at " + hour + "hs.");
            
            /* get the first station of the path */
            station = (Station) router.getPath(nodeTo);
            /* send an empty packet (request) */
            packet = station.handlePacket(new Packet(this, nodeTo, hour));

            try {
                /* print packet data */
                System.out.println("Base receives packet from " +
                        packet.getNodeFrom().getName() + " at " +
                        packet.getTime() + "hs. Data: " + packet.getData());

                /* -- save the packet data in RAM */
                /* create the pair key-value if not exists */
                Station sta = (Station) nodeTo;
                if (!packets.containsKey(sta))
                    packets.put(sta, "");

                /* append the data at the end of the string */
                data = packets.get(sta);
                packets.put(sta, data.concat(packet.getData()));               
            } catch (NullPointerException ex) {
                /* the station didn't reply */
                System.out.println("No reply!");
            }
        }
    }
    
    /**
     * @brief Creates a SQL table for each station in the network
     *
     * Creates a SQL table for each station in the network. The name of the
     * table is the concatenation of the word "station" and the station ID. If
     * the table already exists, it is discarded and recreated.
     * The the fields of the table are:
     *      * sensor: sensor type, String
     *      * date: date on which the data was stored, SQL Date
     *      * max, average and min: maximum, average and minimum daily
     */
    public void updateDataBase() {
        String tableName = null;
        
        try {
            Statement stat = connection.createStatement();

            /* create a sql table for each station in the network */
            System.out.println("Generating data base tables..");

            for (Node node: router.getRouteTable().keySet()) {
                if (node == this)
                    continue;
                tableName = "station" + String.valueOf(node.getID());
                stat.executeUpdate("drop table if exists " + tableName + ";");
                stat.executeUpdate("create table " + tableName +
                        " (sensor, date, max, average, min);");
            }
        } catch (SQLException ex) {
            // logger
        }
    }
    
    /**
     * @brief Stores the statistics of the stations in the database
     * @param calendar A calendar containing the simulation date
     */
    public void writeDataBase(GregorianCalendar calendar) {
        PreparedStatement sql = null;
        double[] results = new double[3];
        
        /* get the simulation date */
        Date date = new Date(calendar.getTimeInMillis());

        System.out.println();
        try {
            for (Station sta: packets.keySet()) {
                System.out.println("Saving data from " + sta.getName() + "...");

                /* sql parametric statement */
                sql = connection.prepareStatement("insert into station" +
                        String.valueOf(sta.getID()) +
                        " values (?, ?, ?, ?, ?);");
                
                for (Sensor sensor: sta.getSensors()) {
                    if (sensor != null) {
                        /* get the max, average and min value */
                        results = calculateStatistics(sensor.getType(), packets.get(sta));

                        /* fill the fields */
                        sql.setString(1, sensor.getType()); // sensor type
                        sql.setDate(2, date);               // date
                        sql.setDouble(3, results[0]);       // max
                        sql.setDouble(4, results[1]);       // average
                        sql.setDouble(5, results[2]);       // min
                        sql.addBatch();

                        /* save it in the data base */
                        //connection.setAutoCommit(false);
                        sql.executeBatch();
                        //connection.setAutoCommit(true);
                    }
                }
            }
        } catch (SQLException ex) {
            // logger
        }

        /* clear the packets in ram */
        packets.clear();
    }

    /**
     * @brief Calculates statistics for a particular type of sensor from a
     *        string containing data from several packages
     * 
     * @param type Sensor type
     * @param data A string containing data from several packages
     * @return An array of double containing the maximum, average and minimum
     *         value
     */
    private double[] calculateStatistics(String type, String data) {
        /* initial values */
        double sum = 0;
        double max = -1;
        double min = 99999;
        int cont = 0;

        /* parse the data */
        for (String token: data.split("/")) {           
            if (token.length() == 0)
                continue;

            /* check the sensor type */
            String dataType = String.valueOf(token.charAt(0));
            if (!type.equals(dataType))
                continue;

            /* add this value to previous values */
            double value = Double.parseDouble(token.substring(1));
            sum += value;

            /* check max and min */
            if (value > max)
                max = value;
            if (value < min)
                min = value;

            /* increment the counter values */
            cont++;
        }

        double[] stadistics = {max, sum/cont, min};
        return stadistics;
    }
}