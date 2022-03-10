// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.RoboRio;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {

  private final double kScaleFactor = 0.5;

  private final CANSparkMax m_motor = new CANSparkMax(RoboRio.CanId.kArm, MotorType.kBrushless);

  // private final DigitalInput m_topLimit = new DigitalInput(RoboRio.DioPort.kArmTopLimit);
  // private final DigitalInput m_bottomLimit = new DigitalInput(RoboRio.DioPort.kArmBottomLimit);
  
  /** Creates a new IntakeSubsystem. */
  public ArmSubsystem() { 
    
    // SmartDashboard.putData("Arm Top Limit", m_topLimit);
    // SmartDashboard.putData("Arm Bottom Limit", m_bottomLimit);  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Arm Motor Speed", m_motor.get());
  }

  public void stop() {
    m_motor.set(0);
  }

  /***
   * Move the shooter arm up or down.
   * @param speed A positive value moves the shooter arm up.
   */
  public void go(double speed){ 
    if (speed > 0.0) {
      if (isAtTop()) {
        speed = 0.0;
      }
      if (speed > 1.0) {
        speed = 1.0;
      }
    }
    if (speed < 0.0) {
      if (isAtBottom()) {
        speed = 0.0;
      }
      if(speed < -1.0) {
        speed = -1.0;
      }
    }
  
    m_motor.set(speed * kScaleFactor);
  }

  public boolean isAtTop() {
    return false;
    //return m_topLimit.get();
  }

  public boolean isAtBottom() {
    return false;
    //return m_bottomLimit.get();
  }
}
