package com.larpclient;

public record l8(int legs, int hip, int tip, float glow, float length, float thickness) {
   public static final int RIGHT = 1;
   public static final int LEFT = 2;

   public boolean right() {
      return (this.legs & 1) != 0;
   }

   public boolean left() {
      return (this.legs & 2) != 0;
   }
}
