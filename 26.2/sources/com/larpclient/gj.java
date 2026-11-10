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

public final class gj {
   private static final gj f = new gj();
   private final Map<String, gj.d> W = new LinkedHashMap<>();
   private final Map<String, Boolean> L = new HashMap<>();
   private String u;
   private int P = -1;
   private int a = -1;
   private int X;
   private int F = -1;
   private String p;
   private boolean e;
   private boolean i;
   private String H;

   private static Path I() {
      return h5.G("dispensers.properties");
   }

   private gj() {
      this.P();
   }

   public static gj c() {
      return f;
   }

   public gj.d E() {
      return this.u == null ? null : this.W.get(this.u);
   }

   public gj.d J() {
      return this.e ? gj.d.g : this.E();
   }

   public boolean F() {
      return this.i;
   }

   public String C() {
      return this.u;
   }

   public int r() {
      return this.P;
   }

   public boolean I(Minecraft var1, gj.d var2) {
      if (var1 != null && var1.player != null && var1.level != null && var1.hitResult instanceof BlockHitResult var3) {
         BlockPos var7 = var3.getBlockPos().immutable();
         BlockState var5 = var1.level.getBlockState(var7);
         if (!(var5.getBlock() instanceof DispenserBlock)) {
            return false;
         }

         String var6 = this.w(var1, var7);
         this.p = var6;
         return this.R(var6, var2).success();
      } else {
         return false;
      }
   }

   public gj.a b(Minecraft var1) {
      return var1 != null
            && var1.player != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.x(this.w(var1, var2.getBlockPos()))
         : new gj.a(false, "Look at a dispenser first.");
   }

   public gj.d s(Minecraft var1) {
      return var1 != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.W.get(this.w(var1, var2.getBlockPos()))
         : null;
   }

   public gj.a a(Minecraft var1, gj.d var2) {
      if (var1 != null
         && var1.player != null
         && var1.level != null
         && var1.hitResult instanceof BlockHitResult var3
         && var1.level.getBlockState(var3.getBlockPos()).getBlock() instanceof DispenserBlock) {
         String var5 = this.w(var1, var3.getBlockPos());
         this.p = var5;
         this.i = (boolean)0;
         return this.R(var5, var2);
      } else {
         return new gj.a(false, "Look at a dispenser first.");
      }
   }

   public void a() {
      this.L.clear();
      this.u = null;
      this.P = -1;
      this.F = -1;
      this.X = 0;
      this.i = (boolean)0;
      this.e = (boolean)0;
      this.H = null;
   }

   public Map<String, gj.d> J() {
      return Collections.unmodifiableMap(this.W);
   }

   public List<String> S(gj.d var1) {
      ArrayList var2 = new ArrayList();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null) {
         String var4 = var3.level.dimension().identifier() + ";";

         for (Entry var6 : this.W.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public gj.a R(String var1, gj.d var2) {
      if (var1 != null && var2 != null) {
         gj.d var3 = this.W.get(var1);
         if (var3 == var2) {
            boolean var5 = this.Z();
            return new gj.a(var5, var5 ? "This dispenser is set and saved as " + var2.p + "." : "This dispenser is set, but saving failed.");
         }

         this.W.put(var1, var2);
         this.p = var1;
         this.L.remove(var1);
         if (!this.Z()) {
            return new gj.a(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null ? "Dispenser set to " + var2.p + "." : "Dispenser moved from " + var3.p + " to " + var2.p + ".";
         if (K(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new gj.a(true, var4);
      } else {
         return new gj.a(false, "Invalid dispenser assignment.");
      }
   }

   public gj.a x(String var1) {
      gj.d var2 = this.W.remove(var1);
      this.L.remove(var1);
      boolean var3 = this.Z();
      return new gj.a(
         var3, !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.p + " dispenser.")
      );
   }

   public void i(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         if (!this.i && !we.Y().isSwapActive()) {
            String var3 = this.D(var1);
            gj.d var4 = var3 == null ? null : this.W.get(var3);
            if (var4 != null) {
               if (var2.containerId != this.F || !var3.equals(this.u)) {
                  this.e = (boolean)0;
                  this.u = var3;
                  this.P = var2.containerId;
                  this.a = -1;
                  this.F = var2.containerId;
                  this.X = !K(var4) && var4 != gj.d.l && var4 != gj.d.s && cy.V(var4) == null ? 20 : 0;
                  switch (var4) {
                     case m:
                     case a:
                        we.u().setContainerContext(var3);
                        break;
                     case b:
                        w3.M().setContainerContext(var3);
                        break;
                     case Z:
                        wc.s().setContainerContext(var3);
                        break;
                     case x:
                     case c:
                        wk.m().setContainerContext(var3);
                        break;
                     case q:
                        w0.l().setContainerContext(var3);
                     case s:
                     case T:
                     case u:
                     default:
                        break;
                     case g:
                        bs.R().L(var1, var2);
                  }

                  this.K(var1, var4);
                  if (!K(var4) && var4 != gj.d.s && cy.V(var4) == null && this.a(var1, var2, var4)) {
                     this.X = 0;
                  }
               }
            }
         }
      }
   }

   public void L(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         this.U(var1);
         this.Q(var1);
         if (!this.i && !this.W.isEmpty()) {
            if (var1.player.containerMenu instanceof DispenserMenu var2) {
               if (var2.containerId != this.F) {
                  this.i(var1, var2);
               }
            } else {
               this.F = -1;
            }

            if (this.X > 0 && var1.player.containerMenu instanceof DispenserMenu var7 && var7.containerId == this.P) {
               gj.d var10 = this.W.get(this.u);
               if (var10 != null && !K(var10) && this.a(var1, var7, var10)) {
                  this.X = 0;
                  if (var10 == gj.d.m) {
                     te.Z("Blackjack", "Templates imported manually from " + this.u + " - registry now holds " + we.u().countTemplates() + " cards");
                  }
               } else {
                  this.X--;
               }
            }

            String var8 = var1.level.dimension().identifier() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.W).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  BlockPos var6 = this.h(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == gj.d.Z
                           || var4.getValue() == gj.d.m
                           || var4.getValue() == gj.d.a
                           || var4.getValue() == gj.d.x
                           || var4.getValue() == gj.d.c
                           || var4.getValue() == gj.d.q
                           || var4.getValue() == gj.d.b
                           || K((gj.d)var4.getValue())
                     )) {
                     this.h(var1, var6, var1.level.getBlockState(var6));
                  }
               }
            }
         }
      } else {
         this.i = (boolean)0;
         this.e = (boolean)0;
      }
   }

   private void U(Minecraft var1) {
      String var2 = var1.level.dimension().identifier().toString();
      if (!var2.equals(this.H)) {
         this.L.clear();
         this.P();
         this.H = var2;
      }
   }

   public void h(Minecraft var1, BlockPos var2, BlockState var3) {
      if (var1 != null && var1.isSameThread() && var1.player != null && var1.level != null) {
         this.U(var1);
         String var4 = this.w(var1, var2);
         gj.d var5 = this.W.get(var4);
         if (var5 == gj.d.m || var5 == gj.d.a || var5 == gj.d.Z || var5 == gj.d.x || var5 == gj.d.c || var5 == gj.d.q || var5 == gj.d.b || K(var5)) {
            int var6 = var3.getBlock() instanceof DispenserBlock && this.Q(var3) ? 1 : 0;
            if (this.F(var4, (boolean)var6)) {
               sg var7 = sg.G();
               if (!this.i && var7 != null) {
                  if (K(var5)) {
                     if (var7.J() && g5.a().Z) {
                        int var12 = var5 == gj.d.J ? 1 : 0;
                        te.Z("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        g5.a().d(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == gj.d.m) {
                     we var11 = we.u();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     s3.S().p(var1, false);
                  } else if (var5 == gj.d.a) {
                     lh.I().H(var2, var3);
                     s3.S().p(var1, true);
                  } else if (var5 == gj.d.x || var5 == gj.d.c) {
                     wk var10 = wk.m();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     m9.O().b(var1, var10, var5 == gj.d.x ? m9.x.x : m9.x.t);
                  } else if (var5 == gj.d.q) {
                     w0 var9 = w0.l();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     kr.N().a(var1, var9);
                  } else if (var5 == gj.d.b) {
                     w3.M().q();
                  } else if (var7.J() && k6.d().Q) {
                     wc var8 = wc.s();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.N(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean H(gj.d var1) {
      return !this.i && !this.S(var1).isEmpty();
   }

   private boolean F(String var1, boolean var2) {
      Boolean var3 = this.L.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String v(gj.d var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var1 != null) {
         String var3 = var2.level.dimension().identifier() + ";";
         String var4 = null;
         if (var2.player != null && var2.player.containerMenu instanceof DispenserMenu var5 && var5.containerId == this.P && this.W.get(this.u) == var1) {
            var4 = this.u;
         } else if (var2.hitResult instanceof BlockHitResult var6 && var6.getType() == Type.BLOCK) {
            var4 = this.w(var2, var6.getBlockPos());
         }

         return this.k(var1, var3, var2.player == null ? null : new Vec3(var2.player.getX(), var2.player.getY(), var2.player.getZ()), var4);
      } else {
         return null;
      }
   }

   String k(gj.d var1, String var2, Vec3 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.W.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            BlockPos var10 = this.h((String)var9.getKey());
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

   public BlockPos n(String var1) {
      return var1 == null ? null : this.h(var1);
   }

   public boolean n(gj.d var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4 != null && var4.player != null && var4.level != null && var1 != null) {
         BlockPos var5 = this.q(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.player.distanceToSqr(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void e(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 == this.P && var2.player.containerMenu instanceof DispenserMenu var3 && var3.containerId == var1) {
         gj.d var6 = this.W.get(this.u);
         if (var6 == gj.d.V) {
            pw.S().l(var2, var3, this.n(this.u));
         }

         cy var5 = cy.V(var6);
         if (var5 != null) {
            var5.W(var2, var3, this.n(this.u));
         } else if (var6 == gj.d.l || var6 == gj.d.s) {
            this.a(var2, var3, var6);
         } else if (var6 == gj.d.g) {
            bs.R().L(var2, var3);
         } else if (K(var6) && this.a != var1) {
            this.a = var1;
            g5.a().D(var2, var3);
            if (this.a(var2, var3, var6)) {
               te.Z("Paper", (var6 == gj.d.J ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.u);
            }
         }
      }
   }

   public void j(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null
         && var3.player != null
         && var1 == this.P
         && var2 >= 0
         && var2 < 9
         && var3.player.containerMenu instanceof DispenserMenu var4
         && var4.containerId == var1
         && K(this.E())) {
         ItemStack var7 = var4.getSlot(var2).getItem();
         if (!var7.isEmpty() && g5.a().E(this.E() == gj.d.J, var2, var7)) {
            sg var6 = sg.G();
            if (var6 != null) {
               var6.Q();
            }
         }
      }
   }

   private static boolean K(gj.d var0) {
      return var0 == gj.d.J || var0 == gj.d.R;
   }

   private void K(Minecraft var1, gj.d var2) {
      sg var3 = sg.G();
      if (var3 != null && var2 != null) {
         if (var3.J()) {
            switch (var2) {
               case J:
               case R:
                  var3.Q(sg.i.V);
                  break;
               case l:
                  var3.Q(sg.i.D);
                  break;
               case V:
                  var3.Q(sg.i.a);
                  break;
               case m:
                  var3.Q(sg.i.c);
                  break;
               case a:
                  var3.Q(sg.i.g);
                  break;
               case b:
                  var3.Q(sg.i.n);
                  break;
               case Z:
                  var3.Q(sg.i.h);
                  break;
               case x:
               case c:
                  var3.Q(sg.i.O);
                  break;
               case q:
                  var3.Q(sg.i.A);
                  break;
               case s:
                  var3.Q(sg.i.J);
                  break;
               case T:
                  var3.Q(sg.i.I);
                  break;
               case u:
                  var3.Q(sg.i.Q);
                  break;
               case g:
                  var3.Q(sg.i.L);
            }
         }
      }
   }

   private String D(Minecraft var1) {
      if (var1.hitResult instanceof BlockHitResult var2) {
         String var13 = this.w(var1, var2.getBlockPos());
         if (var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock) {
            return this.W.containsKey(var13) ? var13 : null;
         }
      }

      if (this.p != null && this.W.containsKey(this.p)) {
         BlockPos var11 = this.h(this.p);
         String var14 = var1.level.dimension().identifier() + ";";
         if (var11 != null && this.p.startsWith(var14) && var1.player.distanceToSqr(var11.getX() + 0.5, var11.getY() + 0.5, var11.getZ() + 0.5) <= 64.0) {
            return this.p;
         }
      }

      String var12 = var1.level.dimension().identifier() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.W.keySet()) {
         if (var7.startsWith(var12)) {
            BlockPos var8 = this.h(var7);
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

   private void Q(Minecraft var1) {
      if (var1.gui.screen() == null) {
         if (var1.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            Block var7 = var1.level.getBlockState(var2.getBlockPos()).getBlock();
            if (var7 instanceof NoteBlock) {
               this.e = (boolean)1;
               this.i = (boolean)0;
            } else if (!(var7 instanceof DispenserBlock)) {
               this.i = (boolean)0;
            } else {
               this.e = (boolean)0;
               String var4 = this.w(var1, var2.getBlockPos());
               lh var5 = lh.I();
               int var6 = var5.S() && var5.k() && this.E() == gj.d.a ? 1 : 0;
               this.i = (boolean)(!this.W.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.W.containsKey(var4)) {
                  this.p = var4;
                  if (!var4.equals(this.u)) {
                     this.u = var4;
                     this.P = -1;
                     this.X = 0;
                  }
               }
            }
         } else {
            this.i = (boolean)0;
         }
      }
   }

   private boolean a(Minecraft var1, DispenserMenu var2, gj.d var3) {
      int var4 = switch (var3) {
         case J -> this.M(var2, true);
         case R -> this.M(var2, false);
         case l -> this.w(var2);
         case V, T, u, g -> 0;
         case m -> this.k(var2, we.u(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case a -> lh.I().V() ? 0 : this.k(var2, we.u(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case b -> this.l(var2);
         case Z -> this.a(var2, wc.s(), 9);
         case x, c -> this.a(var2, wk.m(), 9);
         case q -> this.k(var2, w0.l(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case s -> this.d(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      sg var5 = sg.G();
      if (var5 != null) {
         var5.Q();
      }

      if (var3 == gj.d.s) {
         pq var6 = pq.Q();
         te.Z(
            "Russian Roulette",
            "Loaded dispenser: normal=" + new ItemStack(var6.c).getHoverName().getString() + ", trigger=" + new ItemStack(var6.Y).getHoverName().getString()
         );
      }

      return true;
   }

   private int M(DispenserMenu var1, boolean var2) {
      ItemStack[] var3 = new ItemStack[9];
      Arrays.fill(var3, ItemStack.EMPTY);
      int var4 = 0;
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (a(var7) && var7.getContainerSlot() >= 0 && var7.getContainerSlot() <= 8) {
            ItemStack var8 = var7.hasItem() ? var7.getItem().copyWithCount(1) : ItemStack.EMPTY;
            var3[var7.getContainerSlot()] = var8;
            var4++;
            if (!var8.isEmpty()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         g5 var9 = g5.a();
         byte var10 = 0;

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].isEmpty() && var9.E(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         if (var10 != 0) {
            h.A("Rig", "Paper: learned {} items from the {} table ({})", var5, var2 ? "host" : "viewer", var9.g() ? "shared by both tables" : "this table only");
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int w(DispenserMenu var1) {
      return t5.O().y(Minecraft.getInstance(), this.n(this.u), var1) ? 9 : 0;
   }

   private int a(DispenserMenu var1, we var2, int var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (a(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            ItemStack var7 = var6.getItem();
            MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
            int var9 = x(var7.getHoverName().getString(), var3);
            if (var9 < 1 && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < var3) {
               var9 = var6.getContainerSlot() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof wc) {
                  String var10 = var7.getHoverName().getString().replaceAll("[\\p{Cntrl}]", " ");
                  te.R(
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

   private int l(DispenserMenu var1) {
      w3 var2 = w3.M();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            Slot var5 = null;

            for (Slot var7 : var1.slots) {
               if (a(var7) && var7.getContainerSlot() == var4) {
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
      var2.j();
      return var3;
   }

   private int k(DispenserMenu var1, we var2, int[] var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (a(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
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

   private int d(DispenserMenu var1) {
      return pq.Q().s(Minecraft.getInstance(), this.n(this.u), var1) ? 9 : 0;
   }

   private static boolean a(Slot var0) {
      return var0 != null && !(var0.container instanceof Inventory);
   }

   private static int x(String var0, int var1) {
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

   private boolean Q(BlockState var1) {
      return var1 != null && var1.hasProperty(BlockStateProperties.TRIGGERED) && (Boolean)var1.getValue(BlockStateProperties.TRIGGERED);
   }

   public String w(Minecraft var1, BlockPos var2) {
      return var1.level.dimension().identifier() + ";" + var2.getX() + ";" + var2.getY() + ";" + var2.getZ();
   }

   private BlockPos h(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new BlockPos(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public BlockPos q(gj.d var1) {
      return this.n(this.v(var1));
   }

   private void P() {
      if (I() != null && Files.exists(I())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(I())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               gj.d var6 = gj.d.i(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.W.clear();
            this.W.putAll(var3);
         } catch (Exception var9) {
            h.g("Rig", "Could not load dispenser assignments from {}: {}", I(), var9.getMessage());
         }
      }
   }

   private boolean Z() {
      if (I() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.W.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((gj.d)var3.getValue()).P);
      }

      try {
         Files.createDirectories(I().getParent());
         Path var18 = I().resolveSibling(I().getFileName() + ".tmp");

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
            Files.move(var18, I(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, I(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(I())) {
            var20.load(var21);
         }

         for (Entry var23 : this.W.entrySet()) {
            if (!((gj.d)var23.getValue()).P.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         h.g("Rig", "Could not save dispenser assignments to {}: {}", I(), var17.getMessage());
         return false;
      }
   }

   public record a(boolean success, String message) {
   }

   public enum d {
      J("paper_host", "Paper host"),
      R("paper_viewer", "Paper viewer"),
      l("forty_five_ten", "45/45/10"),
      V("fifty_fifty", "50/50"),
      m("blackjack", "Blackjack"),
      a("blackjack_fo", "Blackjack Force Only"),
      b("odd_even", "Even or Odd"),
      Z("higher_lower", "Higher or Lower"),
      x("baccarat_player", "Baccarat Player"),
      c("baccarat_banker", "Baccarat Banker"),
      q("mexican_jack", "Mexican Jack"),
      s("russian", "Russian Roulette"),
      T("horse_racing", "Horse Racing"),
      u("armor_game", "Armor Game"),
      g("arrow", "Arrow Game");

      public final String P;
      public final String p;

      d(String var3, String var4) {
         this.P = var3;
         this.p = var4;
      }

      private static gj.d i(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return J;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return x;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (gj.d var5 : values()) {
               if (var5.P.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return m;
         }
      }
   }
}
