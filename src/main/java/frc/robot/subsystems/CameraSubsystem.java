// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase {
  private UsbCamera m_shooterCam;
  private UsbCamera m_climberCam;
  
  NetworkTableEntry m_cameraSelection;

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
    m_shooterCam = CameraServer.startAutomaticCapture(0);
    m_climberCam = CameraServer.startAutomaticCapture(1);

    m_cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
  }
  
  @Override
  public void periodic() {
    
  }

  public void switchToShooterCam() {
    m_cameraSelection.setString(m_shooterCam.getName());
  }
  public void switchToClimberCam() {
    m_cameraSelection.setString(m_climberCam.getName());
  }
}
