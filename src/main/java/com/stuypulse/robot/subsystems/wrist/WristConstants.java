package com.stuypulse.robot.subsystems.wrist;

import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.stuypulse.robot.util.talonfx.config.TalonFXConfig;

interface WristConstants {
  public interface WristSettings {
    //TODO: FIGURE OUT NECESSARY CONSTANTS
    double WRIST_GEAR_RATIO = 1.0;
    }
    public interface DeviceIDs {
      int LEFT_MOTOR = 1;
      int RIGHT_MOTOR = 2;
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
    TalonFXConfig MOTOR_CONFIG = 
      new TalonFXConfig()
        .withInvertedValue(InvertedValue.Clockwise_Positive)
        .withNeutralMode(NeutralModeValue.Brake)
        .withSupplyCurrentLimit(Amps.of(30.0)) //TODO: ASK MEC
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

    TalonFXConfig RIGHT_MOTOR_CONFIG = 
      new TalonFXConfig()
        .withInvertedValue(InvertedValue.CounterClockwise_Positive)
        .withNeutralMode(NeutralModeValue.Brake)
        .withSupplyCurrentLimit(Amps.of(30.0)) //TODO: ASK MEC
        .withStatorCurrentLimitEnabled(false)
        .withRampRate(0.25)
        .withPIDConstants(
            WristGains.Right.kP.get(),
            WristGains.Right.kI.get(),
            WristGains.Right.kD.get(),
            0)
        .withFFConstants(
            WristGains.Right.kS.get(),
            WristGains.Right.kV.get(),
            WristGains.Right.kA.get(),
            0.0,
            0)
        .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseVelocitySign, 0)
        .withGravityType(GravityTypeValue.Arm_Cosine)
        .withSensorToMechanismRatio(WristSettings.WRIST_GEAR_RATIO);

  }
}

