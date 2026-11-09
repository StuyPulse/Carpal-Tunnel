/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.stuypulse.robot.util.talonfx.config.TalonFXConfig;
import edu.wpi.first.units.measure.*;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

interface WristConstants {
    public interface WristSettings {
        // TODO: FIGURE OUT NECESSARY CONSTANTS
        Angle WRIST_STOW_ANGLE = Degrees.of(0.0);
        Angle WRIST_DEPLOY_ANGLE = Degrees.of(90.0);

        Angle WRIST_NEUTRAL_ROLL = Degrees.of(0.0);
        Angle WRIST_ROTATE_ANGLE = Degrees.of(90.0);

        Angle ROLL_MIN = Degrees.of(-180.0);
        Angle ROLL_MAX = Degrees.of(180.0);

        // SIM (placeholder values, replace with real numbers from CAD)
        Angle WRIST_PITCH_MIN_ANGLE = Degrees.of(0.0);
        Angle WRIST_ROLL_MAX_ANGLE = Degrees.of(90.0);
        Angle WRIST_PITCH_MAX_ANGLE = Degrees.of(90.0);
        Distance WRIST_ARM_LENGTH = Meters.of(0.2);
        Mass WRIST_MASS = Kilograms.of(1.0);
        MomentOfInertia WRIST_MOI =
                KilogramSquareMeters.of(
                        SingleJointedArmSim.estimateMOI(
                                WRIST_ARM_LENGTH.in(Meters), WRIST_MASS.in(Kilograms)));

        double WRIST_GEAR_RATIO = 1.0;
    }

    public interface DeviceIDs {
        int LEFT_MOTOR = 21;
        int RIGHT_MOTOR = 30;
    }

    public interface WristGains {
        public interface Left {
            LoggedNetworkNumber kP = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kP", 0.0);
            LoggedNetworkNumber kI = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kI", 0.0);
            LoggedNetworkNumber kD = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kD", 0.0);

            LoggedNetworkNumber kS = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kS", 0.0);
            LoggedNetworkNumber kV = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kV", 0.0);
            LoggedNetworkNumber kA = new LoggedNetworkNumber("/Tuning/Wrist/Left/Gains/kA", 0.0);

            double kG = 0.5;
        }

        public interface Right {
            LoggedNetworkNumber kP = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kP", 0.0);
            LoggedNetworkNumber kI = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kI", 0.0);
            LoggedNetworkNumber kD = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kD", 0.0);

            LoggedNetworkNumber kS = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kS", 0.0);
            LoggedNetworkNumber kV = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kV", 0.0);
            LoggedNetworkNumber kA = new LoggedNetworkNumber("/Tuning/Wrist/Right/Gains/kA", 0.0);

            double kG = 0.5;
        }
    }

    public interface WristMotorConfigs {
        static TalonFXConfig motorConfig(InvertedValue inversion) {
            return new TalonFXConfig()
                    .withInvertedValue(inversion)
                    .withNeutralMode(NeutralModeValue.Brake)
                    .withSupplyCurrentLimit(Amps.of(30.0)) // TODO: ASK MEC
                    .withStatorCurrentLimitEnabled(false)
                    .withRampRate(0.25)
                    .withPIDConstants(
                            WristGains.Left.kP.get(),
                            WristGains.Left.kI.get(),
                            WristGains.Left.kD.get(),
                            0)
                    .withFFConstants(
                            WristGains.Left.kS.get(),
                            WristGains.Left.kV.get(),
                            WristGains.Left.kA.get(),
                            0.0,
                            0)
                    .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
                    .withGravityType(GravityTypeValue.Arm_Cosine)
                    .withSensorToMechanismRatio(WristSettings.WRIST_GEAR_RATIO);
        }

        TalonFXConfig LEFT_MOTOR_CONFIG = motorConfig(InvertedValue.Clockwise_Positive);
        TalonFXConfig RIGHT_MOTOR_CONFIG = motorConfig(InvertedValue.CounterClockwise_Positive);
    }

    //     public interface WristMotorConfigs {
    //         TalonFXConfig LEFT_MOTOR_CONFIG =
    //                 new TalonFXConfig()
    //                         .withInvertedValue(InvertedValue.Clockwise_Positive)
    //                         .withNeutralMode(NeutralModeValue.Brake)
    //                         .withSupplyCurrentLimit(Amps.of(30.0)) // TODO: ASK MEC
    //                         .withStatorCurrentLimitEnabled(false)
    //                         .withRampRate(0.25)
    //                         .withPIDConstants(
    //                                 WristGains.Left.kP.get(),
    //                                 WristGains.Left.kI.get(),
    //                                 WristGains.Left.kD.get(),
    //                                 0)
    //                         .withFFConstants(
    //                                 WristGains.Left.kS.get(),
    //                                 WristGains.Left.kV.get(),
    //                                 WristGains.Left.kA.get(),
    //                                 0.0,
    //                                 0)
    //
    // .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
    //                         .withGravityType(GravityTypeValue.Arm_Cosine)
    //                         .withSensorToMechanismRatio(WristSettings.WRIST_GEAR_RATIO);

    //         TalonFXConfig RIGHT_MOTOR_CONFIG =
    //                 new TalonFXConfig()
    //                         .withInvertedValue(InvertedValue.CounterClockwise_Positive)
    //                         .withNeutralMode(NeutralModeValue.Brake)
    //                         .withSupplyCurrentLimit(Amps.of(30.0)) // TODO: ASK MEC
    //                         .withStatorCurrentLimitEnabled(false)
    //                         .withRampRate(0.25)
    //                         .withPIDConstants(
    //                                 WristGains.Right.kP.get(),
    //                                 WristGains.Right.kI.get(),
    //                                 WristGains.Right.kD.get(),
    //                                 0)
    //                         .withFFConstants(
    //                                 WristGains.Right.kS.get(),
    //                                 WristGains.Right.kV.get(),
    //                                 WristGains.Right.kA.get(),
    //                                 0.0,
    //                                 0)
    //
    // .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
    //                         .withGravityType(GravityTypeValue.Arm_Cosine)
    //                         .withSensorToMechanismRatio(WristSettings.WRIST_GEAR_RATIO);
    //     }
}
