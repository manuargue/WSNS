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

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;

import ar.wsns.core.BaseStation;
import ar.wsns.core.Network;
import ar.wsns.core.Node;
import ar.wsns.core.Station;
import ar.wsns.core.exception.*;

/**
 * @brief A graphical interface to create a network
 *
 * The actions to perform are defined by the selected tool in the Edit Tool Bar.
 * The canvas has a reference to the network to manage their nodes and links.
 * It also has a reference to the information panel to set the selected node.
 *
 * @author Manuel Argüelles
 */
public class Canvas extends JPanel implements ActionListener,
                                              MouseMotionListener,
                                              MouseListener {

    private ArrayList<NodeItem> nodes;
    private ArrayList<LinkItem> links;
    private Network net;
    private StationInfoPanel infoPanel;
    private String toolSelected;
    private NodeItem firstNode;
    private LinkItem currentLink;
    private boolean nodeDrag;
    private boolean editable;

    /* link dialog */
    private int currentBandWidth;
    private int currentDistance;

    /**
     * @brief Canvas constructor
     * @param net Reference to the network
     * @param infoPanel Reference to the InfoPanel
     */
    public Canvas(Network net, StationInfoPanel infoPanel) {
        nodes = new ArrayList<NodeItem>();
        links = new ArrayList<LinkItem>();
        this.net = net;
        this.infoPanel = infoPanel;
        
        /* adds the base station */
        try {
            BaseStation base = new BaseStation("Base", net);
            net.addNode(base);
            nodes.add(new BaseNodeItem(new Point(150, 150), base.getID()));
        } catch (DuplicatedNodeException ex) {}

        toolSelected = EditToolBar.ADD_STATION;
        firstNode = null;
        currentLink = null;
        nodeDrag = false;
        editable = true;

        setBackground(Color.WHITE);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /**
     * @brief Enable or disable the canvas
     * @param edit Boolean to enable/disable the canvas
     */
    public void setEditable(boolean edit) {
        editable = edit;
        if (edit)
            setBackground(Color.WHITE);
        else
            setBackground(Color.decode("0xE8E8E8"));    // light gray
    }

    /**
     * @brief Set the selected tool
     * @param e Action event
     */
    public void actionPerformed(ActionEvent e) {
        toolSelected = e.getActionCommand();
    }

    /**
     * @brief Perform the action corresponding to the tool selected when mouse is pressed
     * @param e Mouse event
     */
    public void mousePressed(MouseEvent e) {
        /* get the mouse position relative to the canvas */
        Point mousePos = new Point(e.getX(), e.getY());

        /* right click on a node, show info panel; on a link, show link options;
         * otherwise show nothing */
        if (e.getButton() == MouseEvent.BUTTON3) {
            Station station;
            NodeItem node = getNode(mousePos);
            try {
                if (node.getID() > 0) {             // is not the base
                    station = (Station) net.getNodeFromID(node.getID());
                    infoPanel.setStation(station);
                }
            } catch (NodeNotFoundException ex) {
                // logger
            } catch (NullPointerException ex) {     // no selection
                infoPanel.setStation(null);
            }

            /* if there's a link below the pointer and the canvas is enabled,
             * show link properties dialog */
            LinkItem link = getLink(mousePos);
            try {
                if (editable) {
                    Node nFrom = net.getNodeFromID(link.getNodeFrom().getID());
                    Node nTo = net.getNodeFromID(link.getNodeTo().getID());
                    /* Show a message dialog with link options */
                    LinkDialog linkDialog = new LinkDialog(this);
                    /* set the new link properties */
                    net.modifyLink(nFrom, nTo, currentBandWidth, currentDistance);
                }
            } catch (NullPointerException ex) {
                /* there's not a link */
            } catch (NodeNotFoundException ex) {
                // logger
            } catch (LinkNotFoundException ex) {
                // logger
            }
        }

        /* if the canvas is not enable or is not left mouse button, return */
        if (!editable || e.getButton() != MouseEvent.BUTTON1)
            return;

        /* perform the action corresponding to the tool selected */
        
        if (toolSelected.equals(EditToolBar.ADD_STATION)) {
            /* if there's not a node under the pointer add a new station */
            if (getNode(mousePos) == null) {
                try {
                    Station newStation = new Station(net);
                    net.addNode(newStation);
                    NodeItem newNodeItem = new NodeItem(mousePos, newStation.getID());
                    nodes.add(newNodeItem);
                } catch (DuplicatedNodeException ex) {}
            }
            else
                /* start to dragging the node */
                nodeDrag = true;
        }
        else if (toolSelected.equals(EditToolBar.ADD_LINK)) {
            /* if the start node is not defined, set the node under the pointer
             * as the first node of the link */
            if (firstNode == null) {
                firstNode = getNode(mousePos);
                if (firstNode != null) {
                    currentLink = new LinkItem(firstNode, new NodeItem(mousePos, -1));
                }
            }
            else {
            /* create a link between the first node and the node under the
             * pointer. Show a message dialog with link options */
                NodeItem secondNode = getNode(mousePos);
                try {
                    if (secondNode != firstNode) {
                        Node nodeFrom = net.getNodeFromID(firstNode.getID());
                        Node nodeTo = net.getNodeFromID(secondNode.getID());
                        net.addLink(nodeFrom, nodeTo, LinkItem.BAND_WIDTH, LinkItem.DISTANCE);
                        currentLink.setNodeTo(secondNode);
                        /* add the link item */
                        links.add(currentLink);

                        /* Show a message dialog with link options */
                        LinkDialog linkDialog = new LinkDialog(this);
                        /* set the new link properties */
                        net.modifyLink(nodeFrom, nodeTo, currentBandWidth, currentDistance);
                    }
                } catch (NullPointerException ex) {
                    /* second node is null, cancel the link */
                } catch (NodeNotFoundException ex) {
                    // logger
                } catch (NodeFullLinksException ex) {
                    // logger
                } catch (DuplicatedLinkException ex) {
                    // logger
                } catch (LinkNotFoundException ex) {
                    // logger
                } finally {
                    /* clean */
                    firstNode = null;
                    currentLink = null;
                }
            }
        }
        /* delete the link or node under the pointer */
        else if (toolSelected.equals(EditToolBar.DELETE)) {
            /* try to delete a node */
            NodeItem nodeItem = getNode(mousePos);
            try {
                /* delete all the links connected to this node
                 * (quick and dirty! i must see synchronized lists) */
                Node node = net.getNodeFromID(nodeItem.getID());
                net.removeNode(node);
                ArrayList<LinkItem> linksToDelete = new ArrayList<LinkItem>();

                for (LinkItem link: links) {
                    if (link.getNodeFrom() == nodeItem || link.getNodeTo() == nodeItem)
                        linksToDelete.add(link);
                }
                links.removeAll(linksToDelete);
                linksToDelete = null;
                nodes.remove(nodeItem);

                /* if the node to delete is the same displayed on infoPanel,
                 * set to null InfoPanel station */
                if (node == infoPanel.getStation())
                    infoPanel.setStation(null);
                
            } catch (NullPointerException ex) {
                /* node is null, do nothing */
            } catch (ClassCastException ex) {
                JOptionPane.showMessageDialog(getParent(),
                    "The base station cannot be removed", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NodeNotFoundException ex) {
                // logger
            }

            /* try to delete a link */
            LinkItem link = getLink(mousePos);
            if (link != null) {
                try {
                    Node nodeFrom = net.getNodeFromID(link.getNodeFrom().getID());
                    Node nodeTo = net.getNodeFromID(link.getNodeTo().getID());
                    net.removeLink(nodeFrom, nodeTo);
                    links.remove(link);
                } catch (NodeNotFoundException ex) {}
            }
        }
        repaint();
        try {
            infoPanel.updateComponents();
        } catch (NullPointerException ex) {}
    }

    /**
     * @brief Create a temporal link or drag the node under the pointer
     * @param e Mouse event
     */
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     * @brief Create a temporal link or drag the node under the pointer
     *
     * If "add link" tool is selected, create a temporal link between the first
     * node and the mouse pointer position. If "add station" tool is selected
     * and nodeDrag is enabled, drag the node under the pointer.
     *
     * @param e Mouse event
     */
    public void mouseMoved(MouseEvent e) {
        /* get the mouse position relative to the canvas */
        Point mousePos = new Point(e.getX(), e.getY());

        if (toolSelected.equals(EditToolBar.ADD_LINK)) {
            /* create a temporal link between the first node and the current 
             * mouse pointer position */
            if (currentLink != null) {
                currentLink.setNodeTo(new NodeItem(mousePos, -1));
                repaint();
            }
        }
        else if (nodeDrag) {
            /* Drag the node under the pointer */
            NodeItem nodeItem = getNode(mousePos);
            if (nodeItem != null) {
                nodeItem.setPos(mousePos);
                repaint();
            }
        }
    }

    /**
     * @brief Stop to drag a node when the mouse is released
     * @param e Mouse event
     */
    public void mouseReleased(MouseEvent e) {
        nodeDrag = false;
    }

    /**
     * @brief Not implemented
     * @param e Mouse event
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * @brief Not implemented
     * @param e Mouse event
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * @brief Delete unfinished links when the mouse leaves the canvas
     * @param e Mouse event
     */
    public void mouseExited(MouseEvent e) {
        firstNode = null;
        currentLink = null;
        repaint();
    }

    /**
     * @brief Get the node item near a point, if it exists
     *
     * The proximity to the point is calculated with a threshold defined as
     * a half time the height (width) of a node
     *
     * @param pointRef Reference point
     * @return The node, if it exists, otherwise null
     */
    private NodeItem getNode(Point pointRef) {
        for (NodeItem n: nodes) {
            if (pointRef.contains(n.getPos(), (int) (n.getHeight() * 1.5)))
                return n;
        }
        return null;
    }

    /**
     * @brief Get the link item near a point, if it exists
     *
     * The proximity to the point is calculated with a threshold defined as 30
     * pixels from the center of the link
     *
     * @param pointRef Reference point
     * @return The link, if it exists, otherwise null
     */
    private LinkItem getLink(Point pointRef) {
        for (LinkItem link: links) {
            if (pointRef.contains(link.getPos(), 30))
                return link;
        }
        return null;
    }

    /**
     * @brief Paint the items on the canvas
     * @param g Graphic surface
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentLink != null)        // first the links
            currentLink.paint(g);
        for (LinkItem l: links)
            l.paint(g);
        for (NodeItem n: nodes)         // the nodes after
            n.paint(g);
    }

    /**
     * @brief Set the current link band width
     * @param bw New band width value
     */
    public void setCurrentBandWidth(int bw) {
        currentBandWidth = bw;
    }

    /**
     * @brief Set the current link distance
     * @param distance New distance value
     */
    public void settCurrentDistance(int distance) {
        currentDistance = distance;
    }
}