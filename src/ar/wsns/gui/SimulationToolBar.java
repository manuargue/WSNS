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

import ar.wsns.core.Simulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * @author Manuel Argüelles
 */
public class SimulationToolBar extends JToolBar implements ActionListener {

    public final static String PLAY = "playSimulation";
    public final static String STOP = "stopSimulation";
    public final static String SPEED = "speedSimulation";

    private Simulator simulator;
    private JTextField simSpeed;
    private Canvas canvas;

    public SimulationToolBar(Simulator sim, Canvas canvas) {
        super("Simulation cotrol");
        simulator = sim;
        initComponents();
        setFloatable(false);
        this.canvas = canvas;
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton button = null;
        /* play button */
        button = makeEditButton("play", PLAY, "Start the simulation", "Play");
        add(button);

        /* stop button */
        button = makeEditButton("stop", STOP, "Stop the simulation", "Stop");
        add(button);

        /* separator */
        add(Box.createHorizontalStrut(15));

        /* speed text field */
        JLabel speedLabel = new JLabel("Delay (ms): ");
        add(speedLabel);
        simSpeed = new JTextField("1000");
        simSpeed.setActionCommand(SPEED);
        simSpeed.setHorizontalAlignment(JTextField.RIGHT);
        simSpeed.addActionListener(this);
        simSpeed.setMaximumSize(new Dimension(80, 25));
        add(simSpeed);

        add(Box.createHorizontalGlue());
    }

    private JButton makeEditButton(String imageName, String actionCommand,
                                     String toolTipText, String altText) {
        /* look for the image */
        String imgLocation = "icons/" + imageName + ".gif";
        URL imageURL = getClass().getResource(imgLocation);

        /* create and initialize the button */
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null)
            button.setIcon(new ImageIcon(imageURL, altText));
        else {
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(PLAY)) {
            try {
                simulator.run();
                simSpeed.setEnabled(false);
                canvas.setEditable(false);
            } catch (NullPointerException ex) {
                /* the network is not valid, show a message dialog */
                System.out.println("Aborting...");
                JOptionPane.showMessageDialog(this.getParent(),
                        "The network is not valid.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getActionCommand().equals(STOP)) {
            simulator.stop();
            simSpeed.setEnabled(true);
            canvas.setEditable(true);
        }
        else if (e.getActionCommand().equals(SPEED)) {
            try {
                simulator.setDelay(Integer.parseInt(simSpeed.getText()));
            } catch (NumberFormatException ex) {
                simSpeed.setText("1000");
            }
        }
    }
}
