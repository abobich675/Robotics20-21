package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;



@TeleOp(name="Deliver and Park Autonomous Mode", group="Tests")
public class DeliverParkAutonomousMode extends LinearOpMode {
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private ServoState servoOne;
    private ServoState servoTwo;
    private ServoState servoThree;
    private ElapsedTime runtime = new ElapsedTime();

    private static double DEFAULT_MOTOR_POWER = 0.7;
    private static double DEFAULT_LIFT_POS = 1.0;
    private static double DEFAULT_SCOOP_POS = 1.0;
    private static double DISTANCE_COEFFICIENT = 0.01;
    private static double TURN_TIME_90 = 1.15;
    private final double INCREMENT = 0.001;

    @Override
    public void runOpMode() {


        // go through and try to map various motors and servos based on some common names
        // if it fails (no config) it will throw an exception. we will catch and mark that
        // device as null so we can proceed but know later
        //
        // In a competition bot, you would probably want to fail, at least for critical
        // systems so you would know and be able to rectify the problem rather than having something
        // not work. But for testing, we want robustness and logging

        try {
            leftDrive = hardwareMap.get(DcMotor.class, "motor_left");
        } catch(Exception e) {
            leftDrive = null;
            telemetry.log().add("No motor_left configured");
        }
        try {
            rightDrive = hardwareMap.get(DcMotor.class, "motor_right");
        } catch(Exception e) {
            rightDrive = null;
            telemetry.log().add("No motor_right configured");
        }
        try {
            servoOne = new ServoState("servo_one", hardwareMap, telemetry, -.2);
        } catch(Exception e) {
            servoOne = null;
            telemetry.log().add("No servo_one configured");
        }
        try {
            servoTwo = new ServoState("servo_two", hardwareMap, telemetry, -.2);
        } catch(Exception e) {
            servoTwo = null;
            telemetry.log().add("No servo_two configured");
        }
        try {
            servoThree = new ServoState("servo_three", hardwareMap, telemetry, 1);
        } catch(Exception e) {
            servoThree = null;
            telemetry.log().add("No servo_three configured");
        }

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        if (leftDrive != null) {
            leftDrive.setDirection(DcMotor.Direction.REVERSE);
        }
        if (rightDrive != null) {
            rightDrive.setDirection(DcMotor.Direction.FORWARD);
        }
        if (servoTwo != null) {
            servoTwo.servo.setDirection(Servo.Direction.REVERSE);
//            servoTwo.servo.setPosition(DEFAULT_LIFT_POS);
        }
//        if (servoOne != null) {
//            servoOne.servo.setPosition(DEFAULT_LIFT_POS);
//        }
//        if (servoThree != null) {
//            servoThree.servo.setPosition(DEFAULT_SCOOP_POS);
//        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        sleep(100);
        runtime.reset();
        runtime.startTime();

        // run until the end of the match (driver presses STOP)
        //while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            /////////////////////////
            // Robot Movements
            ////////////////////////

        while(runtime.seconds() < 7) {
            leftDrive.setPower(DEFAULT_MOTOR_POWER);
            rightDrive.setPower(DEFAULT_MOTOR_POWER + 0.005);
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);
//        servoThree.servoPosition(0.7, INCREMENT);
//        wait(0.5);
//        servoThree.servoPosition(1.0, INCREMENT);
//        wait(0.5);
//        double time = runtime.seconds();
//        while(runtime.seconds() < runtime.seconds() + 3){
//            leftDrive.setPower(-DEFAULT_MOTOR_POWER);
//            rightDrive.setPower(-DEFAULT_MOTOR_POWER);
//        }


            //leftPower = Range.clip(drive + turn, -1.0, 1.0);
            //rightPower = Range.clip(drive - turn, -1.0, 1.0);
       // }
    }
    public void wait(int time) {
        double startTime = runtime.seconds();
        while (opModeIsActive() && runtime.seconds() < (startTime + time)) {
        }
    }
    public void wait(double time) {
        double startTime = runtime.seconds();
        while (opModeIsActive() && runtime.seconds() < (startTime + time)) {
        }
    }
}