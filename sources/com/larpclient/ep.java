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

public final class ep {
   private static final ep O = new ep();
   private final Map<String, ep.o> c = new LinkedHashMap<>();
   private final Map<String, Boolean> r = new HashMap<>();
   private String h;
   private int B = -1;
   private int H = -1;
   private int M;
   private int G = -1;
   private String l;
   private boolean w;
   private boolean Y;
   private String E;

   private static Path R() {
      return ko.m("dispensers.properties");
   }

   private ep() {
      this.U();
   }

   public static ep H() {
      return O;
   }

   public ep.o U() {
      return this.h == null ? null : this.c.get(this.h);
   }

   public ep.o y() {
      return this.w ? ep.o.x : this.U();
   }

   public boolean O() {
      return this.Y;
   }

   public String b() {
      return this.h;
   }

   public int V() {
      return this.B;
   }

   public boolean R(Minecraft var1, ep.o var2) {
      if (var1 != null && var1.player != null && var1.level != null && var1.hitResult instanceof BlockHitResult var3) {
         BlockPos var7 = var3.getBlockPos().immutable();
         BlockState var5 = var1.level.getBlockState(var7);
         if (!(var5.getBlock() instanceof DispenserBlock)) {
            return false;
         }

         String var6 = this.m(var1, var7);
         this.l = var6;
         return this.T(var6, var2).success();
      } else {
         return false;
      }
   }

   public ep.d t(Minecraft var1) {
      return var1 != null
            && var1.player != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.v(this.m(var1, var2.getBlockPos()))
         : new ep.d(false, "Look at a dispenser first.");
   }

   public ep.o U(Minecraft var1) {
      return var1 != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.c.get(this.m(var1, var2.getBlockPos()))
         : null;
   }

   public ep.d s(Minecraft var1, ep.o var2) {
      if (var1 != null
         && var1.player != null
         && var1.level != null
         && var1.hitResult instanceof BlockHitResult var3
         && var1.level.getBlockState(var3.getBlockPos()).getBlock() instanceof DispenserBlock) {
         String var5 = this.m(var1, var3.getBlockPos());
         this.l = var5;
         this.Y = (boolean)0;
         return this.T(var5, var2);
      } else {
         return new ep.d(false, "Look at a dispenser first.");
      }
   }

   public void m() {
      this.r.clear();
      this.h = null;
      this.B = -1;
      this.G = -1;
      this.M = 0;
      this.Y = (boolean)0;
      this.w = (boolean)0;
      this.E = null;
   }

   public Map<String, ep.o> i() {
      return Collections.unmodifiableMap(this.c);
   }

   public List<String> v(ep.o var1) {
      ArrayList var2 = new ArrayList();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null) {
         String var4 = var3.level.dimension().identifier() + ";";

         for (Entry var6 : this.c.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public ep.d T(String var1, ep.o var2) {
      if (var1 != null && var2 != null) {
         ep.o var3 = this.c.get(var1);
         if (var3 == var2) {
            boolean var5 = this.X();
            return new ep.d(var5, var5 ? "This dispenser is set and saved as " + var2.N + "." : "This dispenser is set, but saving failed.");
         }

         this.c.put(var1, var2);
         this.l = var1;
         this.r.remove(var1);
         if (!this.X()) {
            return new ep.d(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null ? "Dispenser set to " + var2.N + "." : "Dispenser moved from " + var3.N + " to " + var2.N + ".";
         if (I(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new ep.d(true, var4);
      } else {
         return new ep.d(false, "Invalid dispenser assignment.");
      }
   }

   public ep.d v(String var1) {
      ep.o var2 = this.c.remove(var1);
      this.r.remove(var1);
      boolean var3 = this.X();
      return new ep.d(
         var3, !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.N + " dispenser.")
      );
   }

   public void d(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         if (!this.Y && !ze.c().isSwapActive()) {
            String var3 = this.J(var1);
            ep.o var4 = var3 == null ? null : this.c.get(var3);
            if (var4 != null) {
               if (var2.containerId != this.G || !var3.equals(this.h)) {
                  this.w = (boolean)0;
                  this.h = var3;
                  this.B = var2.containerId;
                  this.H = -1;
                  this.G = var2.containerId;
                  this.M = !I(var4) && var4 != ep.o.P && var4 != ep.o.l && _g.z(var4) == null ? 20 : 0;
                  switch (var4) {
                     case e:
                     case v:
                        ze.V().setContainerContext(var3);
                        break;
                     case K:
                        zu.v().setContainerContext(var3);
                        break;
                     case c:
                        z8.l().setContainerContext(var3);
                        break;
                     case m:
                     case p:
                        z6.u().setContainerContext(var3);
                        break;
                     case q:
                        zw.K().setContainerContext(var3);
                     case l:
                     case Q:
                     case U:
                     default:
                        break;
                     case x:
                        z.v().x(var1, var2);
                  }

                  this.f(var1, var4);
                  if (!I(var4) && var4 != ep.o.l && _g.z(var4) == null && this.i(var1, var2, var4)) {
                     this.M = 0;
                  }
               }
            }
         }
      }
   }

   public void u(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         this.U(var1);
         this.f(var1);
         if (!this.Y && !this.c.isEmpty()) {
            if (var1.player.containerMenu instanceof DispenserMenu var2) {
               if (var2.containerId != this.G) {
                  this.d(var1, var2);
               }
            } else {
               this.G = -1;
            }

            if (this.M > 0 && var1.player.containerMenu instanceof DispenserMenu var7 && var7.containerId == this.B) {
               ep.o var10 = this.c.get(this.h);
               if (var10 != null && !I(var10) && this.i(var1, var7, var10)) {
                  this.M = 0;
                  if (var10 == ep.o.e) {
                     xa.q("Blackjack", "Templates imported manually from " + this.h + " - registry now holds " + ze.V().countTemplates() + " cards");
                  }
               } else {
                  this.M--;
               }
            }

            String var8 = var1.level.dimension().identifier() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.c).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  BlockPos var6 = this.t(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == ep.o.c
                           || var4.getValue() == ep.o.e
                           || var4.getValue() == ep.o.v
                           || var4.getValue() == ep.o.m
                           || var4.getValue() == ep.o.p
                           || var4.getValue() == ep.o.q
                           || var4.getValue() == ep.o.K
                           || I((ep.o)var4.getValue())
                     )) {
                     this.u(var1, var6, var1.level.getBlockState(var6));
                  }
               }
            }
         }
      } else {
         this.Y = (boolean)0;
         this.w = (boolean)0;
      }
   }

   private void U(Minecraft var1) {
      String var2 = var1.level.dimension().identifier().toString();
      if (!var2.equals(this.E)) {
         this.r.clear();
         this.U();
         this.E = var2;
      }
   }

   public void u(Minecraft var1, BlockPos var2, BlockState var3) {
      if (var1 != null && var1.isSameThread() && var1.player != null && var1.level != null) {
         this.U(var1);
         String var4 = this.m(var1, var2);
         ep.o var5 = this.c.get(var4);
         if (var5 == ep.o.e || var5 == ep.o.v || var5 == ep.o.c || var5 == ep.o.m || var5 == ep.o.p || var5 == ep.o.q || var5 == ep.o.K || I(var5)) {
            int var6 = var3.getBlock() instanceof DispenserBlock && this.Z(var3) ? 1 : 0;
            if (this.s(var4, (boolean)var6)) {
               c3 var7 = c3.z();
               if (!this.Y && var7 != null) {
                  if (I(var5)) {
                     if (var7.W() && mi.L().p) {
                        int var12 = var5 == ep.o.k ? 1 : 0;
                        xa.q("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        mi.L().z(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == ep.o.e) {
                     ze var11 = ze.V();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     mj.D().I(var1, false);
                  } else if (var5 == ep.o.v) {
                     kq.I().I(var2, var3);
                     mj.D().I(var1, true);
                  } else if (var5 == ep.o.m || var5 == ep.o.p) {
                     z6 var10 = z6.u();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     tb.q().B(var1, var10, var5 == ep.o.m ? tb.u.n : tb.u.P);
                  } else if (var5 == ep.o.q) {
                     zw var9 = zw.K();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     g.s().M(var1, var9);
                  } else if (var5 == ep.o.K) {
                     zu.v().R();
                  } else if (var7.W() && _z.e().p) {
                     z8 var8 = z8.l();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.j(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean q(ep.o var1) {
      return !this.Y && !this.v(var1).isEmpty();
   }

   private boolean s(String var1, boolean var2) {
      Boolean var3 = this.r.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String u(ep.o var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var1 != null) {
         String var3 = var2.level.dimension().identifier() + ";";
         String var4 = null;
         if (var2.player != null && var2.player.containerMenu instanceof DispenserMenu var5 && var5.containerId == this.B && this.c.get(this.h) == var1) {
            var4 = this.h;
         } else if (var2.hitResult instanceof BlockHitResult var6 && var6.getType() == Type.BLOCK) {
            var4 = this.m(var2, var6.getBlockPos());
         }

         return this.S(var1, var3, var2.player == null ? null : new Vec3(var2.player.getX(), var2.player.getY(), var2.player.getZ()), var4);
      } else {
         return null;
      }
   }

   String S(ep.o var1, String var2, Vec3 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.c.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            BlockPos var10 = this.t((String)var9.getKey());
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

   public BlockPos x(String var1) {
      return var1 == null ? null : this.t(var1);
   }

   public boolean C(ep.o var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4 != null && var4.player != null && var4.level != null && var1 != null) {
         BlockPos var5 = this.n(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.player.distanceToSqr(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void U(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 == this.B && var2.player.containerMenu instanceof DispenserMenu var3 && var3.containerId == var1) {
         ep.o var6 = this.c.get(this.h);
         if (var6 == ep.o.M) {
            c6.K().T(var2, var3, this.x(this.h));
         }

         _g var5 = _g.z(var6);
         if (var5 != null) {
            var5.C(var2, var3, this.x(this.h));
         } else if (var6 == ep.o.P || var6 == ep.o.l) {
            this.i(var2, var3, var6);
         } else if (var6 == ep.o.x) {
            z.v().x(var2, var3);
         } else if (I(var6) && this.H != var1) {
            this.H = var1;
            mi.L().R(var2, var3);
            if (this.i(var2, var3, var6)) {
               xa.q("Paper", (var6 == ep.o.k ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.h);
            }
         }
      }
   }

   public void k(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null
         && var3.player != null
         && var1 == this.B
         && var2 >= 0
         && var2 < 9
         && var3.player.containerMenu instanceof DispenserMenu var4
         && var4.containerId == var1
         && I(this.U())) {
         ItemStack var7 = var4.getSlot(var2).getItem();
         if (!var7.isEmpty() && mi.L().I(this.U() == ep.o.k, var2, var7)) {
            c3 var6 = c3.z();
            if (var6 != null) {
               var6.w();
            }
         }
      }
   }

   private static boolean I(ep.o var0) {
      return var0 == ep.o.k || var0 == ep.o.h;
   }

   private void f(Minecraft var1, ep.o var2) {
      c3 var3 = c3.z();
      if (var3 != null && var2 != null) {
         if (var3.W()) {
            switch (var2) {
               case k:
               case h:
                  var3.S(c3.a.e);
                  break;
               case P:
                  var3.S(c3.a.I);
                  break;
               case M:
                  var3.S(c3.a.R);
                  break;
               case e:
                  var3.S(c3.a.E);
                  break;
               case v:
                  var3.S(c3.a.r);
                  break;
               case K:
                  var3.S(c3.a.z);
                  break;
               case c:
                  var3.S(c3.a.s);
                  break;
               case m:
               case p:
                  var3.S(c3.a.i);
                  break;
               case q:
                  var3.S(c3.a.M);
                  break;
               case l:
                  var3.S(c3.a.w);
                  break;
               case Q:
                  var3.S(c3.a.l);
                  break;
               case U:
                  var3.S(c3.a.Z);
                  break;
               case x:
                  var3.S(c3.a.K);
            }
         }
      }
   }

   private String J(Minecraft var1) {
      if (var1.hitResult instanceof BlockHitResult var2) {
         String var13 = this.m(var1, var2.getBlockPos());
         if (var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock) {
            return this.c.containsKey(var13) ? var13 : null;
         }
      }

      if (this.l != null && this.c.containsKey(this.l)) {
         BlockPos var11 = this.t(this.l);
         String var14 = var1.level.dimension().identifier() + ";";
         if (var11 != null && this.l.startsWith(var14) && var1.player.distanceToSqr(var11.getX() + 0.5, var11.getY() + 0.5, var11.getZ() + 0.5) <= 64.0) {
            return this.l;
         }
      }

      String var12 = var1.level.dimension().identifier() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.c.keySet()) {
         if (var7.startsWith(var12)) {
            BlockPos var8 = this.t(var7);
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

   private void f(Minecraft var1) {
      if (var1.gui.screen() == null) {
         if (var1.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            Block var7 = var1.level.getBlockState(var2.getBlockPos()).getBlock();
            if (var7 instanceof NoteBlock) {
               this.w = (boolean)1;
               this.Y = (boolean)0;
            } else if (!(var7 instanceof DispenserBlock)) {
               this.Y = (boolean)0;
            } else {
               this.w = (boolean)0;
               String var4 = this.m(var1, var2.getBlockPos());
               kq var5 = kq.I();
               int var6 = var5.Z() && var5.k() && this.U() == ep.o.v ? 1 : 0;
               this.Y = (boolean)(!this.c.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.c.containsKey(var4)) {
                  this.l = var4;
                  if (!var4.equals(this.h)) {
                     this.h = var4;
                     this.B = -1;
                     this.M = 0;
                  }
               }
            }
         } else {
            this.Y = (boolean)0;
         }
      }
   }

   private boolean i(Minecraft var1, DispenserMenu var2, ep.o var3) {
      int var4 = switch (var3) {
         case k -> this.D(var2, true);
         case h -> this.D(var2, false);
         case P -> this.U(var2);
         case M, Q, U, x -> 0;
         case e -> this.F(var2, ze.V(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case v -> kq.I().f() ? 0 : this.F(var2, ze.V(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case K -> this.S(var2);
         case c -> this.z(var2, z8.l(), 9);
         case m, p -> this.z(var2, z6.u(), 9);
         case q -> this.F(var2, zw.K(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case l -> this.H(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      c3 var5 = c3.z();
      if (var5 != null) {
         var5.w();
      }

      if (var3 == ep.o.l) {
         _u var6 = _u.O();
         xa.q(
            "Russian Roulette",
            "Loaded dispenser: normal=" + new ItemStack(var6.l).getHoverName().getString() + ", trigger=" + new ItemStack(var6.z).getHoverName().getString()
         );
      }

      return true;
   }

   private int D(DispenserMenu var1, boolean var2) {
      ItemStack[] var3 = new ItemStack[9];
      Arrays.fill(var3, ItemStack.EMPTY);
      int var4 = 0;
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (R(var7) && var7.getContainerSlot() >= 0 && var7.getContainerSlot() <= 8) {
            ItemStack var8 = var7.hasItem() ? var7.getItem().copyWithCount(1) : ItemStack.EMPTY;
            var3[var7.getContainerSlot()] = var8;
            var4++;
            if (!var8.isEmpty()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         mi var9 = mi.L();
         byte var10 = var9.W(false);

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].isEmpty() && var9.I(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int U(DispenserMenu var1) {
      return zv.x().Q(Minecraft.getInstance(), this.x(this.h), var1) ? 9 : 0;
   }

   private int z(DispenserMenu var1, ze var2, int var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (R(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            ItemStack var7 = var6.getItem();
            MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
            int var9 = m(var7.getHoverName().getString(), var3);
            if (var9 < 1 && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < var3) {
               var9 = var6.getContainerSlot() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof z8) {
                  String var10 = var7.getHoverName().getString().replaceAll("[\\p{Cntrl}]", " ");
                  xa.i(
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

   private int S(DispenserMenu var1) {
      zu var2 = zu.v();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            Slot var5 = null;

            for (Slot var7 : var1.slots) {
               if (R(var7) && var7.getContainerSlot() == var4) {
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
      var2.U();
      return var3;
   }

   private int F(DispenserMenu var1, ze var2, int[] var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (R(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
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

   private int H(DispenserMenu var1) {
      return _u.O().w(Minecraft.getInstance(), this.x(this.h), var1) ? 9 : 0;
   }

   private static boolean R(Slot var0) {
      return var0 != null && !(var0.container instanceof Inventory);
   }

   private static int m(String var0, int var1) {
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

   private boolean Z(BlockState var1) {
      return var1 != null && var1.hasProperty(BlockStateProperties.TRIGGERED) && (Boolean)var1.getValue(BlockStateProperties.TRIGGERED);
   }

   public String m(Minecraft var1, BlockPos var2) {
      return var1.level.dimension().identifier() + ";" + var2.getX() + ";" + var2.getY() + ";" + var2.getZ();
   }

   private BlockPos t(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new BlockPos(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public BlockPos n(ep.o var1) {
      return this.x(this.u(var1));
   }

   private void U() {
      if (R() != null && Files.exists(R())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(R())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               ep.o var6 = ep.o.a(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.c.clear();
            this.c.putAll(var3);
         } catch (Exception var9) {
            mt.T("Rig", "Could not load dispenser assignments from {}: {}", R(), var9.getMessage());
         }
      }
   }

   private boolean X() {
      if (R() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.c.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((ep.o)var3.getValue()).g);
      }

      try {
         Files.createDirectories(R().getParent());
         Path var18 = R().resolveSibling(R().getFileName() + ".tmp");

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
            Files.move(var18, R(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, R(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(R())) {
            var20.load(var21);
         }

         for (Entry var23 : this.c.entrySet()) {
            if (!((ep.o)var23.getValue()).g.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         mt.T("Rig", "Could not save dispenser assignments to {}: {}", R(), var17.getMessage());
         return false;
      }
   }

   public record d(boolean success, String message) {
   }

   public enum o {
      k("paper_host", "Paper host"),
      h("paper_viewer", "Paper viewer"),
      P("forty_five_ten", "45/45/10"),
      M("fifty_fifty", "50/50"),
      e("blackjack", "Blackjack"),
      v("blackjack_fo", "Blackjack Force Only"),
      K("odd_even", "Even or Odd"),
      c("higher_lower", "Higher or Lower"),
      m("baccarat_player", "Baccarat Player"),
      p("baccarat_banker", "Baccarat Banker"),
      q("mexican_jack", "Mexican Jack"),
      l("russian", "Russian Roulette"),
      Q("horse_racing", "Horse Racing"),
      U("armor_game", "Armor Game"),
      x("arrow", "Arrow Game");

      public final String g;
      public final String N;

      o(String var3, String var4) {
         this.g = var3;
         this.N = var4;
      }

      private static ep.o a(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return k;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return m;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (ep.o var5 : values()) {
               if (var5.g.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return e;
         }
      }
   }
}
