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

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import ar.wsns.core.Network;
import ar.wsns.core.Simulator;

/**
 * @brief The main window that launchs the program
 * @author Manuel Argüelles
 */
public class MainWindow extends JFrame {

    private static Network network;
    private static Simulator simulator;
    private static JTextArea logText;

    public MainWindow() {
        super("WSNS - Weather Stations Network Simulator");
        network = new Network();
        simulator = new Simulator(network, 1000);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(840, 680);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        /* canvas */
        StationInfoPanel infoPanel = new StationInfoPanel();
        Canvas canvas = new Canvas(network, infoPanel);

        /* edit toolbar */
        EditToolBar editBar = new EditToolBar(canvas);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 0, 0);
        c.weightx = .1;
        c.gridx = 0;
        c.gridy = 0;
        add(editBar, c);

        /* simulation toolbar */
        SimulationToolBar simuBar = new SimulationToolBar(simulator, canvas);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 10);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        add(simuBar, c);

        /* split pane (canvas - info panel) */
        JScrollPane canvasScroll = new JScrollPane(canvas);
        canvasScroll.setMinimumSize(new Dimension(300, 50));   

        infoPanel.setMinimumSize(new Dimension(200, 50));
        JScrollPane infoPanelScroll = new JScrollPane(infoPanel);      
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              canvas, infoPanelScroll);
        splitPane.setDividerLocation(430);

        /* log text area */
        logText = new JTextArea();
        logText.setEditable(false);
        logText.setBackground(Color.WHITE);
        logText.setAutoscrolls(true);
        
        /* main split pane (splitPane - logText) */
        JSplitPane mainSplitPane = new JSplitPane(
                                    JSplitPane.VERTICAL_SPLIT,
                                    splitPane,
                                    new JScrollPane(logText));
        mainSplitPane.setDividerLocation(400);

        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 1;
        add(mainSplitPane, c);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        /* redirect the system output to the logText */
        System.setOut(new PrintStream(new FilteredStream(
                new ByteArrayOutputStream())));
        
        /* set the system look and feel 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
        
        /* run the aplication */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    
    private static class FilteredStream extends FilterOutputStream {
        
        public FilteredStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte b[]) throws IOException {
            logText.append(new String(b));
        }
        
        @Override
        public void write(int b) throws IOException {
            logText.append(String.valueOf((char) b));
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            String tmpString = new String(b , off , len);
            logText.append(tmpString);
        }
    }
}
