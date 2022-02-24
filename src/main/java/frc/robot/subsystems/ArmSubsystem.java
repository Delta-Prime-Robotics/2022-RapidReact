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

  private final CANSparkMax m_motor = new CANSparkMax(RoboRio.CanId.kIntake, MotorType.kBrushless);

  private final DigitalInput m_topLimit = new DigitalInput(RoboRio.DioPort.kTopLimit);
  private final DigitalInput m_bottomLimit = new DigitalInput(RoboRio.DioPort.kBottomLimit);
  
  /** Creates a new IntakeSubsystem. */
  public ArmSubsystem() { 
        
    SmartDashboard.putData("Arm Top Limit Switch", m_topLimit);
    SmartDashboard.putData("Arm Bottom Limit Switch", m_bottomLimit);  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Arm Motor Speed", m_motor.get());
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
  
    m_motor.set(speed);
  }

  public boolean isAtTop() {
    return m_topLimit.get();
  }

  public boolean isAtBottom() {
    return m_bottomLimit.get();
  }
}
