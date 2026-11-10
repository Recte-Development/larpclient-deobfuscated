package com.larpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class hk {
   private static final hk v = new hk();
   private final Map<String, hk.k> T = new LinkedHashMap<>();
   private final Map<String, Boolean> d = new HashMap<>();
   private String N;
   private int H = -1;
   private int q = -1;
   private int J;
   private int C = -1;
   private String b;
   private boolean g;
   private boolean j;
   private String F;

   private static Path s() {
      return ht.U("dispensers.properties");
   }

   private hk() {
      this.l();
   }

   public static hk e() {
      return v;
   }

   public hk.k l() {
      return this.N == null ? null : this.T.get(this.N);
   }

   public hk.k j() {
      return this.g ? hk.k.N : this.l();
   }

   public boolean q() {
      return this.j;
   }

   public String v() {
      return this.N;
   }

   public int v() {
      return this.H;
   }

   public boolean A(Minecraft var1, hk.k var2) {
      if (var1 != null && var1.player != null && var1.level != null && var1.hitResult instanceof BlockHitResult var3) {
         BlockPos var7 = var3.getBlockPos().immutable();
         BlockState var5 = var1.level.getBlockState(var7);
         if (!(var5.getBlock() instanceof DispenserBlock)) {
            return false;
         }

         String var6 = this.v(var1, var7);
         this.b = var6;
         return this.R(var6, var2).success();
      } else {
         return false;
      }
   }

   public hk.o J(Minecraft var1) {
      return var1 != null
            && var1.player != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.y(this.v(var1, var2.getBlockPos()))
         : new hk.o(false, "Look at a dispenser first.");
   }

   public hk.k f(Minecraft var1) {
      return var1 != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.T.get(this.v(var1, var2.getBlockPos()))
         : null;
   }

   public hk.o O(Minecraft var1, hk.k var2) {
      if (var1 != null
         && var1.player != null
         && var1.level != null
         && var1.hitResult instanceof BlockHitResult var3
         && var1.level.getBlockState(var3.getBlockPos()).getBlock() instanceof DispenserBlock) {
         String var5 = this.v(var1, var3.getBlockPos());
         this.b = var5;
         this.j = (boolean)0;
         return this.R(var5, var2);
      } else {
         return new hk.o(false, "Look at a dispenser first.");
      }
   }

   public void L() {
      this.d.clear();
      this.N = null;
      this.H = -1;
      this.C = -1;
      this.J = 0;
      this.j = (boolean)0;
      this.g = (boolean)0;
      this.F = null;
   }

   public Map<String, hk.k> e() {
      return Collections.unmodifiableMap(this.T);
   }

   public List<String> A(hk.k var1) {
      ArrayList var2 = new ArrayList();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null) {
         String var4 = var3.level.dimension().identifier() + ";";

         for (Entry var6 : this.T.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public hk.o R(String var1, hk.k var2) {
      if (var1 != null && var2 != null) {
         hk.k var3 = this.T.get(var1);
         if (var3 == var2) {
            boolean var5 = this.L();
            return new hk.o(var5, var5 ? "This dispenser is set and saved as " + var2.o + "." : "This dispenser is set, but saving failed.");
         }

         this.T.put(var1, var2);
         this.b = var1;
         this.d.remove(var1);
         if (!this.L()) {
            return new hk.o(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null ? "Dispenser set to " + var2.o + "." : "Dispenser moved from " + var3.o + " to " + var2.o + ".";
         if (v(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new hk.o(true, var4);
      } else {
         return new hk.o(false, "Invalid dispenser assignment.");
      }
   }

   public hk.o y(String var1) {
      hk.k var2 = this.T.remove(var1);
      this.d.remove(var1);
      boolean var3 = this.L();
      return new hk.o(
         var3, !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.o + " dispenser.")
      );
   }

   public void g(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         if (!this.j && !vy.a().isSwapActive()) {
            String var3 = this.F(var1);
            hk.k var4 = var3 == null ? null : this.T.get(var3);
            if (var4 != null) {
               if (var2.containerId != this.C || !var3.equals(this.N)) {
                  this.g = (boolean)0;
                  this.N = var3;
                  this.H = var2.containerId;
                  this.q = -1;
                  this.C = var2.containerId;
                  this.J = !v(var4) && var4 != hk.k.j && var4 != hk.k.s && sl.E(var4) == null ? 20 : 0;
                  switch (var4) {
                     case x:
                     case q:
                        vy.w().setContainerContext(var3);
                        break;
                     case i:
                        vz.E().setContainerContext(var3);
                        break;
                     case b:
                        vh.R().setContainerContext(var3);
                        break;
                     case A:
                     case I:
                        vf.x().setContainerContext(var3);
                        break;
                     case Y:
                        v2.L().setContainerContext(var3);
                     case s:
                     case k:
                     case V:
                     default:
                        break;
                     case N:
                        ig.r().d(var1, var2);
                  }

                  this.H(var1, var4);
                  if (!v(var4) && var4 != hk.k.s && sl.E(var4) == null && this.t(var1, var2, var4)) {
                     this.J = 0;
                  }
               }
            }
         }
      }
   }

   public void u(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         this.s(var1);
         this.k(var1);
         if (!this.j && !this.T.isEmpty()) {
            if (var1.player.containerMenu instanceof DispenserMenu var2) {
               if (var2.containerId != this.C) {
                  this.g(var1, var2);
               }
            } else {
               this.C = -1;
            }

            if (this.J > 0 && var1.player.containerMenu instanceof DispenserMenu var7 && var7.containerId == this.H) {
               hk.k var10 = this.T.get(this.N);
               if (var10 != null && !v(var10) && this.t(var1, var7, var10)) {
                  this.J = 0;
                  if (var10 == hk.k.x) {
                     bn.j("Blackjack", "Templates imported manually from " + this.N + " - registry now holds " + vy.w().countTemplates() + " cards");
                  }
               } else {
                  this.J--;
               }
            }

            String var8 = var1.level.dimension().identifier() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.T).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  BlockPos var6 = this.C(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == hk.k.b
                           || var4.getValue() == hk.k.x
                           || var4.getValue() == hk.k.q
                           || var4.getValue() == hk.k.A
                           || var4.getValue() == hk.k.I
                           || var4.getValue() == hk.k.Y
                           || var4.getValue() == hk.k.i
                           || v((hk.k)var4.getValue())
                     )) {
                     this.f(var1, var6, var1.level.getBlockState(var6));
                  }
               }
            }
         }
      } else {
         this.j = (boolean)0;
         this.g = (boolean)0;
      }
   }

   private void s(Minecraft var1) {
      String var2 = var1.level.dimension().identifier().toString();
      if (!var2.equals(this.F)) {
         this.d.clear();
         this.l();
         this.F = var2;
      }
   }

   public void f(Minecraft var1, BlockPos var2, BlockState var3) {
      if (var1 != null && var1.isSameThread() && var1.player != null && var1.level != null) {
         this.s(var1);
         String var4 = this.v(var1, var2);
         hk.k var5 = this.T.get(var4);
         if (var5 == hk.k.x || var5 == hk.k.q || var5 == hk.k.b || var5 == hk.k.A || var5 == hk.k.I || var5 == hk.k.Y || var5 == hk.k.i || v(var5)) {
            int var6 = var3.getBlock() instanceof DispenserBlock && this.X(var3) ? 1 : 0;
            if (this.E(var4, (boolean)var6)) {
               a6 var7 = a6.F();
               if (!this.j && var7 != null) {
                  if (v(var5)) {
                     if (var7.j() && qo.Z().u) {
                        int var12 = var5 == hk.k.y ? 1 : 0;
                        bn.j("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        qo.Z().k(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == hk.k.x) {
                     vy var11 = vy.w();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     io.z().f(var1, false);
                  } else if (var5 == hk.k.q) {
                     q5.a().f(var2, var3);
                     io.z().f(var1, true);
                  } else if (var5 == hk.k.A || var5 == hk.k.I) {
                     vf var10 = vf.x();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     h6.l().q(var1, var10, var5 == hk.k.A ? h6.e.t : h6.e.x);
                  } else if (var5 == hk.k.Y) {
                     v2 var9 = v2.L();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     i6.k().O(var1, var9);
                  } else if (var5 == hk.k.i) {
                     vz.E().J();
                  } else if (var7.j() && com.larpclient.k.D().h) {
                     vh var8 = vh.R();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.d(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean L(hk.k var1) {
      return !this.j && !this.A(var1).isEmpty();
   }

   private boolean E(String var1, boolean var2) {
      Boolean var3 = this.d.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String I(hk.k var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var1 != null) {
         String var3 = var2.level.dimension().identifier() + ";";
         String var4 = null;
         if (var2.player != null && var2.player.containerMenu instanceof DispenserMenu var5 && var5.containerId == this.H && this.T.get(this.N) == var1) {
            var4 = this.N;
         } else if (var2.hitResult instanceof BlockHitResult var6 && var6.getType() == Type.BLOCK) {
            var4 = this.v(var2, var6.getBlockPos());
         }

         return this.h(var1, var3, var2.player == null ? null : new Vec3(var2.player.getX(), var2.player.getY(), var2.player.getZ()), var4);
      } else {
         return null;
      }
   }

   String h(hk.k var1, String var2, Vec3 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.T.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            BlockPos var10 = this.C((String)var9.getKey());
            if (var10 != null) {
               double var11 = var3 == null ? 0.0 : var3.distanceToSqr(Vec3.atCenterOf(var10));
               if (((String)var9.getKey()).equals(var4) && var11 <= 64.0) {
                  return var4;
               }

               if (var5 == null || var11 < var6) {
                  var6 = var11;
                  var5 = (String)var9.getKey();
               }
            }
         }
      }

      return var5;
   }

   public BlockPos Q(String var1) {
      return var1 == null ? null : this.C(var1);
   }

   public boolean m(hk.k var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4 != null && var4.player != null && var4.level != null && var1 != null) {
         BlockPos var5 = this.f(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.player.distanceToSqr(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void h(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 == this.H && var2.player.containerMenu instanceof DispenserMenu var3 && var3.containerId == var1) {
         hk.k var6 = this.T.get(this.N);
         if (var6 == hk.k.B) {
            wj.m().r(var2, var3, this.Q(this.N));
         }

         sl var5 = sl.E(var6);
         if (var5 != null) {
            var5.k(var2, var3, this.Q(this.N));
         } else if (var6 == hk.k.j || var6 == hk.k.s) {
            this.t(var2, var3, var6);
         } else if (var6 == hk.k.N) {
            ig.r().d(var2, var3);
         } else if (v(var6) && this.q != var1) {
            this.q = var1;
            qo.Z().R(var2, var3);
            if (this.t(var2, var3, var6)) {
               bn.j("Paper", (var6 == hk.k.y ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.N);
            }
         }
      }
   }

   public void o(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null
         && var3.player != null
         && var1 == this.H
         && var2 >= 0
         && var2 < 9
         && var3.player.containerMenu instanceof DispenserMenu var4
         && var4.containerId == var1
         && v(this.l())) {
         ItemStack var7 = var4.getSlot(var2).getItem();
         if (!var7.isEmpty() && qo.Z().C(this.l() == hk.k.y, var2, var7)) {
            a6 var6 = a6.F();
            if (var6 != null) {
               var6.A();
            }
         }
      }
   }

   private static boolean v(hk.k var0) {
      return var0 == hk.k.y || var0 == hk.k.w;
   }

   private void H(Minecraft var1, hk.k var2) {
      a6 var3 = a6.F();
      if (var3 != null && var2 != null) {
         if (var3.j()) {
            switch (var2) {
               case y:
               case w:
                  var3.S(a6.d.L);
                  break;
               case j:
                  var3.S(a6.d.k);
                  break;
               case B:
                  var3.S(a6.d.K);
                  break;
               case x:
                  var3.S(a6.d.d);
                  break;
               case q:
                  var3.S(a6.d.a);
                  break;
               case i:
                  var3.S(a6.d.g);
                  break;
               case b:
                  var3.S(a6.d.Y);
                  break;
               case A:
               case I:
                  var3.S(a6.d.B);
                  break;
               case Y:
                  var3.S(a6.d.j);
                  break;
               case s:
                  var3.S(a6.d.f);
                  break;
               case k:
                  var3.S(a6.d.H);
                  break;
               case V:
                  var3.S(a6.d.F);
                  break;
               case N:
                  var3.S(a6.d.N);
            }
         }
      }
   }

   private String F(Minecraft var1) {
      if (var1.hitResult instanceof BlockHitResult var2) {
         String var13 = this.v(var1, var2.getBlockPos());
         if (var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock) {
            return this.T.containsKey(var13) ? var13 : null;
         }
      }

      if (this.b != null && this.T.containsKey(this.b)) {
         BlockPos var11 = this.C(this.b);
         String var14 = var1.level.dimension().identifier() + ";";
         if (var11 != null && this.b.startsWith(var14) && var1.player.distanceToSqr(var11.getX() + 0.5, var11.getY() + 0.5, var11.getZ() + 0.5) <= 64.0) {
            return this.b;
         }
      }

      String var12 = var1.level.dimension().identifier() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.T.keySet()) {
         if (var7.startsWith(var12)) {
            BlockPos var8 = this.C(var7);
            if (var8 != null) {
               double var9 = var1.player.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5);
               if (var9 < var4) {
                  var4 = var9;
                  var15 = var7;
               }
            }
         }
      }

      return var15;
   }

   private void k(Minecraft var1) {
      if (var1.gui.screen() == null) {
         if (var1.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            Block var7 = var1.level.getBlockState(var2.getBlockPos()).getBlock();
            if (var7 instanceof NoteBlock) {
               this.g = (boolean)1;
               this.j = (boolean)0;
            } else if (!(var7 instanceof DispenserBlock)) {
               this.j = (boolean)0;
            } else {
               this.g = (boolean)0;
               String var4 = this.v(var1, var2.getBlockPos());
               q5 var5 = q5.a();
               int var6 = var5.N() && var5.L() && this.l() == hk.k.q ? 1 : 0;
               this.j = (boolean)(!this.T.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.T.containsKey(var4)) {
                  this.b = var4;
                  if (!var4.equals(this.N)) {
                     this.N = var4;
                     this.H = -1;
                     this.J = 0;
                  }
               }
            }
         } else {
            this.j = (boolean)0;
         }
      }
   }

   private boolean t(Minecraft var1, DispenserMenu var2, hk.k var3) {
      int var4 = switch (var3) {
         case y -> this.Q(var2, true);
         case w -> this.Q(var2, false);
         case j -> this.x(var2);
         case B, k, V, N -> 0;
         case x -> this.Z(var2, vy.w(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case q -> q5.a().G() ? 0 : this.Z(var2, vy.w(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case i -> this.T(var2);
         case b -> this.b(var2, vh.R(), 9);
         case A, I -> this.b(var2, vf.x(), 9);
         case Y -> this.Z(var2, v2.L(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case s -> this.g(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      a6 var5 = a6.F();
      if (var5 != null) {
         var5.A();
      }

      if (var3 == hk.k.s) {
         pp var6 = pp.A();
         bn.j(
            "Russian Roulette",
            "Loaded dispenser: normal=" + new ItemStack(var6.n).getHoverName().getString() + ", trigger=" + new ItemStack(var6.E).getHoverName().getString()
         );
      }

      return true;
   }

   private int Q(DispenserMenu var1, boolean var2) {
      ItemStack[] var3 = new ItemStack[9];
      Arrays.fill(var3, ItemStack.EMPTY);
      int var4 = 0;
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (v(var7) && var7.getContainerSlot() >= 0 && var7.getContainerSlot() <= 8) {
            ItemStack var8 = var7.hasItem() ? var7.getItem().copyWithCount(1) : ItemStack.EMPTY;
            var3[var7.getContainerSlot()] = var8;
            var4++;
            if (!var8.isEmpty()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         qo var9 = qo.Z();
         byte var10 = 0;

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].isEmpty() && var9.C(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         if (var10 != 0) {
            _q.z(
               "Rig", "Paper: learned {} items from the {} table ({})", var5, var2 ? "host" : "viewer", var9.a() ? "shared by both tables" : "this table only"
            );
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int x(DispenserMenu var1) {
      return p2.p().x(Minecraft.getInstance(), this.Q(this.N), var1) ? 9 : 0;
   }

   private int b(DispenserMenu var1, vy var2, int var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (v(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            ItemStack var7 = var6.getItem();
            MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
            int var9 = O(var7.getHoverName().getString(), var3);
            if (var9 < 1 && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < var3) {
               var9 = var6.getContainerSlot() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof vh) {
                  String var10 = var7.getHoverName().getString().replaceAll("[\\p{Cntrl}]", " ");
                  bn.b(
                     "HigherLower Template import slot="
                        + var6.getContainerSlot()
                        + " mapId="
                        + var8.id()
                        + " displayedName="
                        + var10.substring(0, Math.min(var10.length(), 120))
                        + " chosenValue="
                        + var9
                  );
               }

               var4++;
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      return var4;
   }

   private int T(DispenserMenu var1) {
      vz var2 = vz.E();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            Slot var5 = null;

            for (Slot var7 : var1.slots) {
               if (v(var7) && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.hasItem() && var5.getItem().is(Items.FILLED_MAP)) {
               MapId var8 = (MapId)var5.getItem().getComponents().get(DataComponents.MAP_ID);
               int var9 = var4 + 1;
               if (var8 != null) {
                  var2.setTemplateForValue(var9, var8);
                  var3++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      var2.clearQueue();
      var2.r();
      return var3;
   }

   private int Z(DispenserMenu var1, vy var2, int[] var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (v(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            int var7 = var6.getContainerSlot();
            if (var7 >= 0 && var7 < var3.length) {
               MapId var8 = (MapId)var6.getItem().getComponents().get(DataComponents.MAP_ID);
               if (var8 != null) {
                  var2.setTemplateForValue(var3[var7], var8);
                  var4++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      return var4;
   }

   private int g(DispenserMenu var1) {
      return pp.A().H(Minecraft.getInstance(), this.Q(this.N), var1) ? 9 : 0;
   }

   private static boolean v(Slot var0) {
      return var0 != null && !(var0.container instanceof Inventory);
   }

   private static int O(String var0, int var1) {
      if (var0 == null) {
         return -1;
      }

      String var2 = var0.toLowerCase(Locale.ROOT);
      if (var2.matches(".*\\b(ace|a)\\b.*")) {
         return 1;
      }

      if (var2.matches(".*\\b(jack|queen|king|j|q|k)\\b.*")) {
         return Math.min(10, var1);
      }

      Matcher var3 = Pattern.compile("(?<!\\d)(10|[1-9])(?!\\d)").matcher(var2);
      if (!var3.find()) {
         return -1;
      }

      try {
         int var4 = Integer.parseInt(var3.group(1));
         return var4 <= var1 ? var4 : -1;
      } catch (NumberFormatException var5) {
         return -1;
      }
   }

   private boolean X(BlockState var1) {
      return var1 != null && var1.hasProperty(BlockStateProperties.TRIGGERED) && (Boolean)var1.getValue(BlockStateProperties.TRIGGERED);
   }

   public String v(Minecraft var1, BlockPos var2) {
      return var1.level.dimension().identifier() + ";" + var2.getX() + ";" + var2.getY() + ";" + var2.getZ();
   }

   private BlockPos C(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new BlockPos(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public BlockPos f(hk.k var1) {
      return this.Q(this.I(var1));
   }

   private void l() {
      if (s() != null && Files.exists(s())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(s())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               hk.k var6 = hk.k.H(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.T.clear();
            this.T.putAll(var3);
         } catch (Exception var9) {
            _q.R("Rig", "Could not load dispenser assignments from {}: {}", s(), var9.getMessage());
         }
      }
   }

   private boolean L() {
      if (s() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.T.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((hk.k)var3.getValue()).P);
      }

      try {
         Files.createDirectories(s().getParent());
         Path var18 = s().resolveSibling(s().getFileName() + ".tmp");

         try (
            FileChannel var19 = FileChannel.open(var18, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            OutputStream var4 = Channels.newOutputStream(var19);
            OutputStreamWriter var5 = new OutputStreamWriter(var4, StandardCharsets.ISO_8859_1);
         ) {
            var1.store(var5, "Assigned casino game dispensers");
            var5.flush();
            var19.force(true);
         }

         try {
            Files.move(var18, s(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, s(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(s())) {
            var20.load(var21);
         }

         for (Entry var23 : this.T.entrySet()) {
            if (!((hk.k)var23.getValue()).P.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         _q.R("Rig", "Could not save dispenser assignments to {}: {}", s(), var17.getMessage());
         return false;
      }
   }

   public enum k {
      y("paper_host", "Paper host"),
      w("paper_viewer", "Paper viewer"),
      j("forty_five_ten", "45/45/10"),
      B("fifty_fifty", "50/50"),
      x("blackjack", "Blackjack"),
      q("blackjack_fo", "Blackjack Force Only"),
      i("odd_even", "Even or Odd"),
      b("higher_lower", "Higher or Lower"),
      A("baccarat_player", "Baccarat Player"),
      I("baccarat_banker", "Baccarat Banker"),
      Y("mexican_jack", "Mexican Jack"),
      s("russian", "Russian Roulette"),
      k("horse_racing", "Horse Racing"),
      V("armor_game", "Armor Game"),
      N("arrow", "Arrow Game");

      public final String P;
      public final String o;

      k(String var3, String var4) {
         this.P = var3;
         this.o = var4;
      }

      private static hk.k H(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return y;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return A;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (hk.k var5 : values()) {
               if (var5.P.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return x;
         }
      }
   }

   public record o(boolean success, String message) {
   }
}
