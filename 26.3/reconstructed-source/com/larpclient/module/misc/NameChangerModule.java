package com.larpclient.module.misc;

public final class NameChangerModule extends Module {
   private static NameChangerModule instance;
   private final TextSetting nameSetting = this.addSetting(new TextSetting("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      instance = this;
      this.nameSetting.setTitle("Type any name");
   }

   public static NameChangerModule getInstance() {
      return instance;
   }

   public String getCustomName() {
      return this.isOperational() ? this.nameSetting.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.nameSetting.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
