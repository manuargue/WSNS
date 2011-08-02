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

package ar.wsns.gui;

import ar.wsns.core.Node;
import ar.wsns.core.Station;
import ar.wsns.core.exception.NodeFullSensorsException;
import ar.wsns.sensor.*;

import java.net.URL;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * @brief A panel that displays information related to a particular station
 * 
 * This class has a reference to the selected station, which can be changed by
 * the function setStation.
 * The panel has three sections: the title, which shows the station name, its 
 * ID and a picture of the station; the connection list, which shows all the 
 * nodes connected to the selected station; the sensor list, which shows the 
 * sensors connected to the station and allow you to add, remove or reset any
 * sensor of the list.
 *
 * @author Manuel Argüelles
 */
public class StationInfoPanel extends JPanel implements ActionListener {

    private Station station;                    // station selected
    private JList sensorList;
    private JList connectionList;
    private DefaultListModel sensorModel;
    private JLabel title;
    private JLabel state;

    /**
     * @brief StationInfoPanel constructor
     */
    public StationInfoPanel() {
        station = null;
        setVisible(false);
        initComponents();
    }

    /**
     * @brief Updates the data of the station
     */
    public void updateComponents() {
        /* set the new station name */
        title.setText(station.getName() + " (ID: " +
                String.valueOf(station.getID()) + ")");
        
        /* update the state*/
        if (station.isActive()) {
            state.setText("Enabled");
            state.setForeground(Color.GREEN);
        }
        else {
            state.setText("Disabled");
            state.setForeground(Color.RED);
        }
        
        /* update connection list */
        String[] nodeInfo = new String[4];
        int i = 0;
        for (Node n: station.getNodesConnected()) {
            if (n != null) {
                nodeInfo[i++] = n.getName() + "  /  Cost = " +
                                 station.getLinkTo(n).getCost();
            }
        }
        connectionList.setListData(nodeInfo);

        /* update sensor model */
        updateSensorModel();
    }

    /**
     * @brief Creates the GUI components
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = .8;

        /* title ---------------------------------------------------------- */
        title = new JLabel();
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        /* title icon */
        title.setIconTextGap(20);
        URL imageURL = getClass().getResource("icons/station_img.gif");
        title.setIcon(new ImageIcon(imageURL, "No picture available"));
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20, 20, 0, 20);
        add(title, c);
        
        /* station state -------------------------------------------------- */
        // title
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(10, 20, 0, 0);
        add(new JLabel("State: "), c);
        
        // state label
        c.gridx = 1;
        c.gridwidth = 1;
        c.insets = new Insets(8, -30, 0, 20);
        state = new JLabel();
        state.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        add(state, c);
        
        // state button
        c.gridx = 2;
        c.insets = new Insets(10, 0, 0, 20);
        JButton stateBtn = new JButton("Change state");
        stateBtn.setActionCommand("changeState");
        stateBtn.setToolTipText("Click to enable or disable the station");
        stateBtn.addActionListener(this);
        add(stateBtn, c);
        
        /* connections title ---------------------------------------------- */
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.insets = new Insets(20, 20, 0, 20);
        add(new JLabel("Nodes connected:"), c);

        /* connection list ------------------------------------------------ */
        // create the model containing the nodes connected to this station
        connectionList = new JList();
        connectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        connectionList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrollCon = new JScrollPane(connectionList);
        scrollCon.setPreferredSize(new Dimension(100,50));

        c.gridx = 0;
        c.gridy = 3;
        c.weighty = .1;
        c.insets = new Insets(10, 20, 0, 20);
        add(scrollCon, c);

        /* sensors title -------------------------------------------------- */
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0;
        c.insets = new Insets(20, 20, 0, 20);
        add(new JLabel("Sensors connected:"), c);

        /* sensor list ---------------------------------------------------- */
        /* create the model containing the sensors connected to the selected
         * station */
        sensorModel = new DefaultListModel();
        sensorList = new JList(sensorModel);
        sensorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sensorList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrollSensor = new JScrollPane(sensorList);
        scrollSensor.setPreferredSize(new Dimension(100, 50));
        
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = .1;
        c.insets = new Insets(10, 20, 10, 20);
        add(scrollSensor, c);

        /* add sensor button ---------------------------------------------- */
        JButton addBtn = new JButton("Add");
        addBtn.setActionCommand("addSensor");
        addBtn.setToolTipText("Add a sensor");
        addBtn.addActionListener(this);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 6;
        c.weighty = 0;
        c.insets = new Insets(0, 20, 10, 20);
        add(addBtn, c);

        /* reset sensor button -------------------------------------------- */
        JButton resetBtn = new JButton("Reset");
        resetBtn.setActionCommand("resetSensor");
        resetBtn.setToolTipText("Reset a sensor");
        resetBtn.addActionListener(this);

        c.gridx = 2;
        add(resetBtn, c);

        /* remove sensor button ------------------------------------------- */
        JButton removeBtn = new JButton("Remove");
        removeBtn.setActionCommand("removeSensor");
        removeBtn.setToolTipText("Remove a sensor");
        removeBtn.addActionListener(this);
        
        c.gridx = 1;
        c.insets = new Insets(0, 0, 10, 0);
        add(removeBtn, c);

        /* dummy label ---------------------------------------------------- */
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = .1;
        add(new JLabel(), c);
    }

    /**
     * @brief Handles the events raised by the buttons
     * @param e Action event
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("addSensor")) {
            /* show message dialog with sensor types */
            String[] types = {"Pluviometer", "Hygrometer", "Thermometer",
                              "Weather vane", "Anemometer"};
            String t = (String) JOptionPane.showInputDialog(
                    getParent(), "Choose the sensor:", "Sensor type",
                    JOptionPane.PLAIN_MESSAGE, null, types, "Pluviometer");
            
            if (t == null)      // cancel button
                return;

            /* try to add the sensor */
            try {
                if (t.equals("Pluviometer"))
                    station.addSensor(new Pluviometer());
                else if (t.equals("Hygrometer"))
                    station.addSensor(new Hygrometer());
                else if (t.equals("Thermometer"))
                    station.addSensor(new Thermometer());
                else if(t.equals("Weather vane"))
                    station.addSensor(new WeatherVane());
                else 
                    station.addSensor(new Anemometer());
            } catch (NodeFullSensorsException ex) {
                JOptionPane.showMessageDialog(getParent(),
                        "You can connect up to four sensors");
            }
        }
        else if (command.equals("removeSensor")) {
            station.removeSensor(sensorList.getSelectedIndex());
        }
        else if (command.equals("resetSensor")) {
            Sensor[] sensors = station.getSensors();
            int selected = sensorList.getSelectedIndex();
            if (selected > -1 && sensors[selected] != null)
                sensors[selected].reset();
        }
        else if (command.equals("changeState")) {
            if (station.isActive())
                station.setActive(false);
            else
                station.setActive(true);
            updateComponents();
        }
        updateSensorModel();
    }

    /**
     * @brief Updates the elements of the sensor model
     */
    private void updateSensorModel() {
        sensorModel.removeAllElements();
        for (Sensor sensor: station.getSensors()) {
            sensorModel.addElement(sensor);
        }
    }

    /**
     * @brief Set the selected station
     * @param station The station
     */
    public void setStation(Station station) {
        this.station = station;
        if (station != null) {
            setVisible(true);
            updateComponents();
        } else
            setVisible(false);
    }

    /**
     * @brief Returns the selected station
     * @return The selected station
     */
    public Station getStation() {
        return station;
    }
}
