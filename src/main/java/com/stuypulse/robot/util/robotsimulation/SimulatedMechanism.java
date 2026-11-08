/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.util.robotsimulation;

import edu.wpi.first.math.geometry.Pose3d;

/**
 * A simulated mechanism that provides an externally usable pose for external simulation files, such
 * as a robot visualizer or 3D simulator.
 */
@FunctionalInterface
public interface SimulatedMechanism {
    Pose3d getSimulatedPose();
}
