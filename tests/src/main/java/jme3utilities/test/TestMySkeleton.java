/*
 Copyright (c) 2013-2017, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Stephen Gold's name may not be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL STEPHEN GOLD BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3utilities.test;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Misc;

/**
 * Test cases for the MySkeleton class.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestMySkeleton extends SimpleApplication {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger = Logger.getLogger(
            TestMySkeleton.class.getName());
    // *************************************************************************
    // new methods exposed

    /**
     * Simple application to test the MySkeleton class.
     *
     * @param ignored command-line arguments
     */
    public static void main(String[] ignored) {
        Misc.setLoggingLevels(Level.SEVERE);
        TestMySkeleton application = new TestMySkeleton();
        application.setShowSettings(false);
        application.start();
    }

    /**
     * Initialize the application and perform tests.
     */
    @Override
    public void simpleInitApp() {
        logger.setLevel(Level.INFO);
        System.out.print("Test results for class MySkeleton:\n\n");

        String modelPath = "Models/Oto/Oto.mesh.xml";
        Node node = (Node) assetManager.loadModel(modelPath);
        rootNode.attachChild(node);

        //String bone = "uparm.right";
        Quaternion orientation = new Quaternion();
        float[] angles = new float[3];
        for (int axis = 0; axis < 3; axis++) {
            angles[axis] = 0.4f;
        }
        for (int axis = 0; axis < 3; axis++) {
            float angle = 0.2f + 0.1f * axis;
            angles[axis] = angle;
            orientation.fromAngles(angles);
            //
        }

        stop();
    }
}