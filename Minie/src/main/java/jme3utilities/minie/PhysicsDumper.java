/*
 Copyright (c) 2013-2018, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its contributors
 may be used to endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3utilities.minie;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.joints.PhysicsJoint;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.objects.PhysicsVehicle;
import com.jme3.math.Vector3f;
import java.io.PrintStream;
import java.util.Collection;
import java.util.logging.Logger;
import jme3utilities.debug.Describer;
import jme3utilities.debug.Dumper;

/**
 * Dump portions of a jME3 scene graph for debugging.
 * <p>
 * {@link #dump(com.jme3.scene.Spatial)} is the usual interface to this class.
 * The level of detail can be configured dynamically.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class PhysicsDumper extends Dumper {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(PhysicsDumper.class.getName());
    // *************************************************************************
    // constructors

    /**
     * Instantiate a dumper that will use System.out for output.
     */
    public PhysicsDumper() {
        super();
        PhysicsDescriber newDescriber = new PhysicsDescriber();
        setDescriber(newDescriber);
    }

    /**
     * Instantiate a dumper that will use the specified output stream.
     *
     * @param printStream output stream (not null)
     */
    public PhysicsDumper(PrintStream printStream) {
        super(printStream);
        PhysicsDescriber newDescriber = new PhysicsDescriber();
        setDescriber(newDescriber);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Dump the specified physics character.
     *
     * @param character the character to dump (not null)
     */
    public void dump(PhysicsCharacter character) {
        long objectId = character.getObjectId();
        stream.printf("  character #%s ", Long.toHexString(objectId));

        Vector3f location = character.getPhysicsLocation();
        stream.printf("loc=[%.3f, %.3f, %.3f]",
                location.x, location.y, location.z);

        stream.println();

    }

    /**
     * Dump the specified ghost object.
     *
     * @param ghost the ghost object to dump (not null)
     */
    public void dump(PhysicsGhostObject ghost) {
        long objectId = ghost.getObjectId();
        stream.printf("  ghost #%s ", Long.toHexString(objectId));

        Vector3f location = ghost.getPhysicsLocation();
        stream.printf("loc=[%.3f, %.3f, %.3f]",
                location.x, location.y, location.z);

        stream.println();
    }

    /**
     * Dump the specified joint.
     *
     * @param joint the joint to dump (not null)
     */
    public void dump(PhysicsJoint joint) {
        long objectId = joint.getObjectId();
        long aId = joint.getBodyA().getObjectId();
        long bId = joint.getBodyB().getObjectId();
        stream.printf("  joint #%s a=%s,b=%s", Long.toHexString(objectId),
                Long.toHexString(aId), Long.toHexString(bId));

        stream.println();
    }

    /**
     * Dump the specified rigid body.
     *
     * @param body the rigid body to dump (not null)
     */
    public void dump(PhysicsRigidBody body) {
        long objectId = body.getObjectId();
        float mass = body.getMass();
        stream.printf("  rigid body #%s mass=%f",
                Long.toHexString(objectId), mass);

        Vector3f location = body.getPhysicsLocation();
        stream.printf(" loc=[%.3f, %.3f, %.3f]",
                location.x, location.y, location.z);

        CollisionShape shape = body.getCollisionShape();
        PhysicsDescriber physicsDescriber = getDescriber();
        String desc = physicsDescriber.describe(shape);
        stream.printf(" shape=%s", desc);

        Vector3f scale = shape.getScale();
        if (scale.x != 1f || scale.y != 1f || scale.z != 1f) {
            stream.printf(" sca=[%.3f, %.3f, %.3f]", scale.x, scale.y, scale.z);
        }

        stream.println();
    }

    /**
     * Dump the specified physics space.
     *
     * @param space the physics space to dump (not null)
     */
    public void dump(PhysicsSpace space) {
        Collection<PhysicsCharacter> characters = space.getCharacterList();
        Collection<PhysicsGhostObject> ghosts = space.getGhostObjectList();
        Collection<PhysicsJoint> joints = space.getJointList();
        Collection<PhysicsRigidBody> rigidBodies = space.getRigidBodyList();
        Collection<PhysicsVehicle> vehicles = space.getVehicleList();

        int numCharacters = characters.size();
        int numGhosts = ghosts.size();
        int numJoints = joints.size();
        int numBodies = rigidBodies.size();
        int numVehicles = vehicles.size();

        stream.printf("%nphysics space with %d character%s, %d ghost%s, ",
                numCharacters, (numCharacters == 1) ? "" : "s",
                numGhosts, (numGhosts == 1) ? "" : "s");
        stream.printf("%d joint%s, %d rigid bod%s, and %d vehicle%s%n",
                numJoints, (numJoints == 1) ? "" : "s",
                numBodies, (numBodies == 1) ? "y" : "ies",
                numJoints, (numVehicles == 1) ? "" : "s");

        for (PhysicsCharacter character : characters) {
            dump(character);
        }
        for (PhysicsGhostObject ghost : ghosts) {
            dump(ghost);
        }
        for (PhysicsJoint joint : joints) {
            dump(joint);
        }
        for (PhysicsRigidBody rigid : rigidBodies) {
            dump(rigid);
        }
        for (PhysicsVehicle vehicle : vehicles) {
            dump(vehicle);
        }
    }

    /**
     * Dump the specified vehicle.
     *
     * @param vehicle the vehicle to dump (not null)
     */
    public void dump(PhysicsVehicle vehicle) {
        long objectId = vehicle.getObjectId();
        float mass = vehicle.getMass();
        stream.printf("  vehicle #%s mass=%f", Long.toHexString(objectId),
                mass);

        Vector3f location = vehicle.getPhysicsLocation();
        stream.printf(" loc=[%.3f, %.3f, %.3f]",
                location.x, location.y, location.z);

        stream.println();
    }
    // *************************************************************************
    // Dumper methods

    /**
     * Access the describer used by this dumper.
     *
     * @return the pre-existing instance (not null)
     */
    @Override
    public PhysicsDescriber getDescriber() {
        Describer describer = super.getDescriber();
        PhysicsDescriber result = (PhysicsDescriber) describer;

        return result;
    }
}
