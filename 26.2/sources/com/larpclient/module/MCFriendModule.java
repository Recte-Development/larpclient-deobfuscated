package com.larpclient.module;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class MCFriendModule extends Module {
   private final Set<String> L = new HashSet<>();
   private boolean j = (boolean)0;

   public MCFriendModule() {
      super("MCFriend", "Add friends with middle click", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         long var1 = mc.getWindow().handle();
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
            byte var9 = this.j;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 == 0
               && mc.gui.screen() == null
               && mc.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof Player var15) {
               String var16 = var15.getName().getString();
               int var20 = this.L.contains(var16);
               boolean var12 = false;
               byte var11 = (byte)var20;
               var20 = Integer.signum(var11);
               if (((var20 | -var20) >>> 31 ^ 1) == 0) {
                  this.L.remove(var16);
                  mc.player.sendOverlayMessage(Component.literal("§8[§d§lLarp§8] §cRemoved friend: §f" + var16));
               } else {
                  this.L.add(var16);
                  mc.player.sendOverlayMessage(Component.literal("§8[§d§lLarp§8] §aAdded friend: §f" + var16));
               }
            }
         }

         this.j = (boolean)var3;
      }
   }

   public boolean S(String var1) {
      return this.L.contains(var1);
   }

   public Set<String> m() {
      return this.L;
   }
}
