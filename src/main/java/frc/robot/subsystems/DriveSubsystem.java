// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RoboRio;

public class DriveSubsystem extends SubsystemBase {
  // Drive constants
  private static final double kRampPeriod = 0.3;
  /* The following is an estimate. We need to confirm with the actual robot and remove this comment. */
  private static final double kDistancePerRotation = 2 * Math.PI * 6.0;

  // Drive train components
  private CANSparkMax m_leftLeader;
  private CANSparkMax m_leftFollower;
  private CANSparkMax m_rightLeader;
  private CANSparkMax m_rightFollower;

  private DifferentialDrive m_diffDrive;

  private RelativeEncoder m_leftEncoder;
  private RelativeEncoder m_rightEncoder;

  
  /**  Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    m_leftLeader = new CANSparkMax(RoboRio.CanId.kLeftLeader, MotorType.kBrushless);
    m_leftFollower = new CANSparkMax(RoboRio.CanId.kLeftFollower, MotorType.kBrushless);
    m_rightLeader = new CANSparkMax(RoboRio.CanId.kRightLeader, MotorType.kBrushless);
    m_rightFollower = new CANSparkMax(RoboRio.CanId.kRightFollower, MotorType.kBrushless);

    m_leftFollower.follow(m_leftLeader);
    m_rightFollower.follow(m_rightLeader);

    m_rightLeader.setInverted(true);
    setRampRate(kRampPeriod);

    m_diffDrive = new DifferentialDrive(m_leftLeader, m_rightLeader);

    m_leftEncoder = m_leftLeader.getEncoder();
    m_leftEncoder.setPositionConversionFactor(kDistancePerRotation);
    m_rightEncoder = m_rightLeader.getEncoder();
    m_rightEncoder.setPositionConversionFactor(kDistancePerRotation);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Encoder Distance", m_leftEncoder.getPosition());
    SmartDashboard.putNumber("Right Encoder Distance", m_rightEncoder.getPosition());
    SmartDashboard.putNumber("Left Drive Motor Speed", m_leftLeader.get());
    SmartDashboard.putNumber("Right Drive Motor Speed", m_rightLeader.get());
  }

  public void setRampRate(double rate) {
    m_leftLeader.setClosedLoopRampRate(rate);
    m_rightLeader.setClosedLoopRampRate(rate);
  }

  public void setIdleMode(IdleMode mode) {
    m_leftLeader.setIdleMode(mode);
    m_leftFollower.setIdleMode(mode);
    m_rightLeader.setIdleMode(mode);
    m_rightFollower.setIdleMode(mode);
  }

  public void resetEncoders() {
    m_leftEncoder.setPosition(0.0);
    m_rightEncoder.setPosition(0.0);
  }

  public double GetLeftDistance() {
    return m_leftEncoder.getPosition();
  }

  public double GetRightDistance() {
    return m_rightEncoder.getPosition();
  }

  public void stop() {
    m_diffDrive.tankDrive(0, 0);
  }

  public void arcadeDrive(double forwardSpeed, double rotation) {
    m_diffDrive.arcadeDrive(forwardSpeed, rotation);
  }
}
