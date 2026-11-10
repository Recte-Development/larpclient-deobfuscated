package com.larpclient.module;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_3966;
import org.lwjgl.glfw.GLFW;

public class MCFriendModule extends Module {
   private final Set<String> y = new HashSet<>();
   private boolean p = (boolean)0;

   public MCFriendModule() {
      super("MCFriend", "Add friends with middle click", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         long var1 = mc.method_22683().method_4490();
         int var10000 = GLFW.glfwGetMouseButton(var1, 2);
         byte var14 = 1;
         int var13 = var10000;
         var10000 = Integer.compare(var13, var14);
         int var3 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
         boolean var8 = false;
         int var7 = var3;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            boolean var10 = false;
            byte var9 = this.p;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 == 0
               && mc.field_1755 == null
               && mc.field_1765 instanceof class_3966 var4
               && var4.method_17782() instanceof class_1657 var15) {
               String var16 = var15.method_5477().getString();
               int var20 = this.y.contains(var16);
               boolean var12 = false;
               byte var11 = (byte)var20;
               var20 = Integer.signum(var11);
               if (((var20 | -var20) >>> 31 ^ 1) == 0) {
                  this.y.remove(var16);
                  mc.field_1724.method_7353(class_2561.method_43470("§8[§d§lLarp§8] §cRemoved friend: §f" + var16), true);
               } else {
                  this.y.add(var16);
                  mc.field_1724.method_7353(class_2561.method_43470("§8[§d§lLarp§8] §aAdded friend: §f" + var16), true);
               }
            }
         }

         this.p = (boolean)var3;
      }
   }

   public boolean M(String var1) {
      return this.y.contains(var1);
   }

   public Set<String> T() {
      return this.y;
   }
}
