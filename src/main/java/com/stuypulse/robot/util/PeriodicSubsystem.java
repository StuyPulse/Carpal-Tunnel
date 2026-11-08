/************************* PROJECT HIGHGROUND *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.util;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.ArrayList;
import java.util.List;

public abstract class PeriodicSubsystem extends SubsystemBase {
    private static final List<PeriodicSubsystem> instances = new ArrayList<>();

    public PeriodicSubsystem() {
        instances.add(this);
    }

    public void periodicAfterScheduler() {}
    ;

    public static void runAllPeriodicAfterScheduler() {
        for (var instance : instances) {
            instance.periodicAfterScheduler();
        }
    }
}