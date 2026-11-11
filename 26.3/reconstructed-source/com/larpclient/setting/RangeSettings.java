package com.larpclient.setting;

public final class RangeSettings {
   public int maxTargets = 32;
   public double distanceA = 4.0;
   public double distanceB = 14.0;
   public double heightA = -2.0;
   public double heightB = 7.0;
   public double speed = 1.8;
   public double smoothing = 1.0;
   public double randomness = 0.3;
   public double range = 5.0;
   public double minDelay = 0.1;
   public double scale = 1.0;
   public double radius = 3.0;
   public double horizontalFactor = 1.2;
   public double verticalFactor = 1.2;
   public double maxAngle = 24.0;
   public double fov = 18.0;
   public double maxRotation = 28.0;
   public double strength = 0.9;
   public double threshold = 0.35;
   public boolean enabled = (boolean)1;
   public boolean invert = (boolean)0;
   public boolean visibleOnly = (boolean)1;
   public int primaryColor = -2109441;
   public int secondaryColor = -7873035;
   public RangeSettings.YawMode mode = RangeSettings.YawMode.FIXED;
   public RangeSettings.Orientation targetPriority = RangeSettings.Orientation.CAMERA_FACING;
   public RangeSettings.Heading style = RangeSettings.Heading.STATIC;
   public RangeSettings.Movement filter = RangeSettings.Movement.SWIRL;
   public RangeSettings.DepthMode sortMode = RangeSettings.DepthMode.NORMAL;
   public RangeSettings.ColorMode behavior = RangeSettings.ColorMode.PULSE;

   public double minDistance() {
      return Math.min(this.distanceA, this.distanceB);
   }

   public double maxDistance() {
      return Math.max(this.distanceA, this.distanceB);
   }

   public double minHeight() {
      return Math.min(this.heightA, this.heightB);
   }

   public double maxHeight() {
      return Math.max(this.heightA, this.heightB);
   }

   public enum ColorMode {
      SOLID,
      PULSE,
      RAINBOW;
   }

   public enum DepthMode {
      NORMAL,
      SEE_THROUGH,
      NORMAL_WITH_GHOST;
   }

   public enum Heading {
      STATIC,
      VELOCITY,
      LOOK;
   }

   public enum Movement {
      SWIRL,
      DRIFT,
      FLOAT,
      FALL,
      UPRIGHT;
   }

   public enum Orientation {
      CAMERA_FACING,
      DRIFT,
      YAW_ONLY;
   }

   public enum YawMode {
      FIXED,
      FOLLOW_PLAYER;
   }
}
