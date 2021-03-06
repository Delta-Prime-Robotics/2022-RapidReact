// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RoboRio;

public class ClimberSubsystem extends SubsystemBase {

  private final double kScaleFactor = 1.0;

  private final VictorSP m_motor = new VictorSP(RoboRio.PwmPort.kClimberMotor);
  // private final CANSparkMax m_motor = new CANSparkMax(RoboRio.CanId.kClimber, MotorType.kBrushless);

  // private final DigitalInput m_topLimit = new DigitalInput(RoboRio.DioPort.kClimberTopLimit);
  // private final DigitalInput m_bottomLimit = new DigitalInput(RoboRio.DioPort.kClimberBottomLimit);

  /** Creates a new ClimberSumsystem. */
  public ClimberSubsystem() {

    // SmartDashboard.putData("Climber Top Limit", m_topLimit);
    // SmartDashboard.putData("Climber Bottom Limit", m_bottomLimit);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climber Motor", m_motor.get());
  }

  public void stop() {
    m_motor.set(0);
  }

  /***
   * Move the climber up or down.
   * @param speed A positive value moves the climber up.
   */
  public void move(double speed) {
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
    // return m_topLimit.get();
   }
  
  public boolean isAtBottom() {
    return false;
    // return m_bottomLimit.get();
  }

}
