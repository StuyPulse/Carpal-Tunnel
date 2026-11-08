/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.constants;

import com.pathplanner.lib.util.FlippingUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import java.util.List;

public interface FieldConstants {
    Field2d FIELD_2D = new Field2d();

    public static Pose2d transformToOppositeAlliance(Pose2d pose) {
        return FlippingUtil.flipFieldPose(pose);
    }

    public static List<Pose2d> transformToOppositeAlliance(List<Pose2d> poses) {
        return poses.stream().map(FieldConstants::transformToOppositeAlliance).toList();
    }
}
