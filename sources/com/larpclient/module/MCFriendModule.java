package com.larpclient.module;

import com.larpclient.hy;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MCFriendModule extends Module {
   private final Set<String> Z = new HashSet<>();
   private boolean S = (boolean)0;

   public MCFriendModule() {
      super("MCFriend", "Add friends with middle click", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         long var1 = mc.getWindow().handle();
         int var10000 = hy.Y(var1, 2);
         byte var14 = 1;
         int var13 = var10000;
         var10000 = Integer.compare(var13, var14);
         int var3 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
         if (var3 != 0) {
            boolean var8 = false;
            byte var7 = this.S;
            var10000 = Integer.signum(var7);
            if ((var10000 | -var10000) >>> 31 == 0 && mc.gui.screen() == null) {
               HitResult var5 = mc.hitResult;
               int var19 = var5 instanceof EntityHitResult;
               boolean var10 = false;
               byte var9 = (byte)var19;
               var19 = Integer.signum(var9);
               if (((var19 | -var19) >>> 31 ^ 1) == 0) {
                  EntityHitResult var4 = (EntityHitResult)var5;
                  Entity var6 = var4.getEntity();
                  int var21 = var6 instanceof Player;
                  boolean var12 = false;
                  byte var11 = (byte)var21;
                  var21 = Integer.signum(var11);
                  if (((var21 | -var21) >>> 31 ^ 1) == 0) {
                     Player var15 = (Player)var6;
                     String var16 = var15.getName().getString();
                     if (this.Z.contains(var16)) {
                        this.Z.remove(var16);
                        mc.player.sendOverlayMessage(Component.literal("§8[§d§lLarp§8] §cRemoved friend: §f" + var16));
                     } else {
                        this.Z.add(var16);
                        mc.player.sendOverlayMessage(Component.literal("§8[§d§lLarp§8] §aAdded friend: §f" + var16));
                     }
                  }
               }
            }
         }

         this.S = (boolean)var3;
      }
   }

   public boolean V(String var1) {
      return this.Z.contains(var1);
   }

   public Set<String> k() {
      return this.Z;
   }
}
