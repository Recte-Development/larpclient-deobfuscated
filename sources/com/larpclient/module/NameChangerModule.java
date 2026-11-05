package com.larpclient.module;

public final class NameChangerModule extends Module {
   private static NameChangerModule u;
   private final li Y = this.addSetting(new li("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      u = this;
      this.Y.R("Type any name");
   }

   public static NameChangerModule B() {
      return u;
   }

   public String T() {
      return this.isOperational() ? this.Y.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.Y.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
