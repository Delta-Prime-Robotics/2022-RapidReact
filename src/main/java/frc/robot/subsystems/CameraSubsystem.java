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
  private UsbCamera _shooterCam;
  private UsbCamera _climberCam;
  
  NetworkTableEntry _cameraSelection;

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
    _shooterCam = CameraServer.startAutomaticCapture(0);
    _climberCam = CameraServer.startAutomaticCapture(1);

    _cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
  }
  
  @Override
  public void periodic() {
    
  }

  public void switchToShooterCam() {
    _cameraSelection.setString(_shooterCam.getName());
  }
  public void switchToClimberCam() {
    _cameraSelection.setString(_climberCam.getName());
  }
}
