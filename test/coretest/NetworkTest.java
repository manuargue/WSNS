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

package coretest;

import ar.wsns.core.*;
import ar.wsns.core.exception.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Manuel Argüelles
 */

public class NetworkTest {
    public static Network net;

    public static void main(String[] args) {
        net = new Network();

        Station n1 = new Station("n1", net);
        Station n2 = new Station("n2", net);
        Station n3 = new Station("n3", net);
        Station n4 = new Station("n4", net);
        Station n5 = new Station("n5", net);
        BaseStation base = new BaseStation("base", net);

        try {
            net.addNode(base);
            net.addNode(n1);
            net.addNode(n2);
            net.addNode(n3);
            net.addNode(n4);
            net.addNode(n5);
        } catch (DuplicatedNodeException ex) {
            print("nodo duplicado");
        }
        
        try {
            net.addLink(n1, n3, 10, 10);
            net.addLink(n1, n4, 10, 10);
            net.addLink(n1, n5, 10, 10);
            net.addLink(n1, base, 10, 10);
            net.addLink(n2, n3, 10, 10);
        } catch (NodeFullLinksException ex) {
            Logger.getLogger(NetworkTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DuplicatedLinkException ex) {
            print("link duplicado");
        }

        print("n1 a base: " + n1.isConnectedTo(base));
        print("n1 a n3: " + n1.isConnectedTo(n3));
        print("n1 a n4: " + n1.isConnectedTo(n4));
        print("n1 a n5: " + n1.isConnectedTo(n5));

        print("\nElimino n1 de la red");
        
        try {
            net.removeNode(n1);
        } catch (NodeNotFoundException ex) {
            print("no existe el nodo");
        }
        
        print("n1 a base: " + n1.isConnectedTo(base));
        print("n1 a n3: " + n1.isConnectedTo(n3));
        print("n1 a n4: " + n1.isConnectedTo(n4));
        print("n1 a n5: " + n1.isConnectedTo(n5));
        print("");

        for (Node n: net.getNodes())
            print(n.getName());

        print("\nn2 a n3: " + n2.isConnectedTo(n3));
        print("n3 a n2: " + n3.isConnectedTo(n2));

        print("\ndesconecto n2 y n3");
        net.removeLink(n2, n3);
        print("n2 a n3: " + n2.isConnectedTo(n3));
        print("n3 a n2: " + n3.isConnectedTo(n2));
    }

    public static void print(Object msg) {
        System.out.println(msg);
    }
}

