// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase {
  private UsbCamera m_camera1;
  private UsbCamera m_camera2;
  
  // For SmartDashboard:
  //NetworkTableEntry m_cameraSelection;

  // For other dashboards:
  VideoSink server;

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
    m_camera1 = CameraServer.startAutomaticCapture(0);
    m_camera2 = CameraServer.startAutomaticCapture(1);

    // m_cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");

    server = CameraServer.getServer();
    
    m_camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    m_camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
  }
  
  @Override
  public void periodic() {
    
  }

  public void switchToCamera1() {
    //m_cameraSelection.setString(m_shooterCam.getName());
    System.out.println("Setting camera 1");
    server.setSource(m_camera1);
  }
  public void switchToCamera2() {
    // m_cameraSelection.setString(m_climberCam.getName());
    System.out.println("Setting camera 2");
        server.setSource(m_camera2);
  }
}
