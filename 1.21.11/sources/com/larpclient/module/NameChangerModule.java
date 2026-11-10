package com.larpclient.module;

public final class NameChangerModule extends Module {
   private static NameChangerModule n;
   private final aj Q = this.addSetting(new aj("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      n = this;
      this.Q.A("Type any name");
   }

   public static NameChangerModule o() {
      return n;
   }

   public String u() {
      return this.isOperational() ? this.Q.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.Q.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
