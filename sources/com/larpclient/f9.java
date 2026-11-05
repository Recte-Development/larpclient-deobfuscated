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

public final class f9 {
   private static final f9 F = new f9();
   private final Map<String, f9.b> O = new LinkedHashMap<>();
   private final Map<String, Boolean> M = new HashMap<>();
   private String i;
   private int d = -1;
   private int W = -1;
   private int V;
   private int B = -1;
   private String J;
   private boolean I;
   private boolean g;
   private String l;

   private static Path n() {
      return ma.h("dispensers.properties");
   }

   private f9() {
      this.C();
   }

   public static f9 W() {
      return F;
   }

   public f9.b o() {
      return this.i == null ? null : this.O.get(this.i);
   }

   public f9.b A() {
      return this.I ? f9.b.F : this.o();
   }

   public boolean Y() {
      return this.g;
   }

   public String v() {
      return this.i;
   }

   public int i() {
      return this.d;
   }

   public boolean Y(Minecraft var1, f9.b var2) {
      if (var1 != null && var1.player != null && var1.level != null && var1.hitResult instanceof BlockHitResult var3) {
         BlockPos var7 = var3.getBlockPos().immutable();
         BlockState var5 = var1.level.getBlockState(var7);
         if (!(var5.getBlock() instanceof DispenserBlock)) {
            return false;
         }

         String var6 = this.x(var1, var7);
         this.J = var6;
         return this.Z(var6, var2).success();
      } else {
         return false;
      }
   }

   public f9.n Z(Minecraft var1) {
      return var1 != null
            && var1.player != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.x(this.x(var1, var2.getBlockPos()))
         : new f9.n(false, "Look at a dispenser first.");
   }

   public f9.b Z(Minecraft var1) {
      return var1 != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.O.get(this.x(var1, var2.getBlockPos()))
         : null;
   }

   public f9.n d(Minecraft var1, f9.b var2) {
      if (var1 != null
         && var1.player != null
         && var1.level != null
         && var1.hitResult instanceof BlockHitResult var3
         && var1.level.getBlockState(var3.getBlockPos()).getBlock() instanceof DispenserBlock) {
         String var5 = this.x(var1, var3.getBlockPos());
         this.J = var5;
         this.g = (boolean)0;
         return this.Z(var5, var2);
      } else {
         return new f9.n(false, "Look at a dispenser first.");
      }
   }

   public void r() {
      this.M.clear();
      this.i = null;
      this.d = -1;
      this.B = -1;
      this.V = 0;
      this.g = (boolean)0;
      this.I = (boolean)0;
      this.l = null;
   }

   public Map<String, f9.b> C() {
      return Collections.unmodifiableMap(this.O);
   }

   public List<String> T(f9.b var1) {
      ArrayList var2 = new ArrayList();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null) {
         String var4 = var3.level.dimension().identifier() + ";";

         for (Entry var6 : this.O.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public f9.n Z(String var1, f9.b var2) {
      if (var1 != null && var2 != null) {
         f9.b var3 = this.O.get(var1);
         if (var3 == var2) {
            boolean var5 = this.i();
            return new f9.n(var5, var5 ? "This dispenser is set and saved as " + var2.w + "." : "This dispenser is set, but saving failed.");
         }

         this.O.put(var1, var2);
         this.J = var1;
         this.M.remove(var1);
         if (!this.i()) {
            return new f9.n(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null ? "Dispenser set to " + var2.w + "." : "Dispenser moved from " + var3.w + " to " + var2.w + ".";
         if (K(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new f9.n(true, var4);
      } else {
         return new f9.n(false, "Invalid dispenser assignment.");
      }
   }

   public f9.n x(String var1) {
      f9.b var2 = this.O.remove(var1);
      this.M.remove(var1);
      boolean var3 = this.i();
      return new f9.n(
         var3, !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.w + " dispenser.")
      );
   }

   public void B(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         if (!this.g && !ug.x().isSwapActive()) {
            String var3 = this.t(var1);
            f9.b var4 = var3 == null ? null : this.O.get(var3);
            if (var4 != null) {
               if (var2.containerId != this.B || !var3.equals(this.i)) {
                  this.I = (boolean)0;
                  this.i = var3;
                  this.d = var2.containerId;
                  this.W = -1;
                  this.B = var2.containerId;
                  this.V = !K(var4) && var4 != f9.b.i && var4 != f9.b.u && hk.r(var4) == null ? 20 : 0;
                  switch (var4) {
                     case E:
                     case P:
                        ug.w().setContainerContext(var3);
                        break;
                     case T:
                        ue.V().setContainerContext(var3);
                        break;
                     case B:
                        um.T().setContainerContext(var3);
                        break;
                     case L:
                     case z:
                        u3.H().setContainerContext(var3);
                        break;
                     case c:
                        uu.M().setContainerContext(var3);
                     case u:
                     case g:
                     case X:
                     default:
                        break;
                     case F:
                        f2.f().w(var1, var2);
                  }

                  this.W(var1, var4);
                  if (!K(var4) && var4 != f9.b.u && hk.r(var4) == null && this.X(var1, var2, var4)) {
                     this.V = 0;
                  }
               }
            }
         }
      }
   }

   public void T(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         this.v(var1);
         this.c(var1);
         if (!this.g && !this.O.isEmpty()) {
            if (var1.player.containerMenu instanceof DispenserMenu var2) {
               if (var2.containerId != this.B) {
                  this.B(var1, var2);
               }
            } else {
               this.B = -1;
            }

            if (this.V > 0 && var1.player.containerMenu instanceof DispenserMenu var7 && var7.containerId == this.d) {
               f9.b var10 = this.O.get(this.i);
               if (var10 != null && !K(var10) && this.X(var1, var7, var10)) {
                  this.V = 0;
                  if (var10 == f9.b.E) {
                     hh.K("Blackjack", "Templates imported manually from " + this.i + " - registry now holds " + ug.w().countTemplates() + " cards");
                  }
               } else {
                  this.V--;
               }
            }

            String var8 = var1.level.dimension().identifier() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.O).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  BlockPos var6 = this.y(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == f9.b.B
                           || var4.getValue() == f9.b.E
                           || var4.getValue() == f9.b.P
                           || var4.getValue() == f9.b.L
                           || var4.getValue() == f9.b.z
                           || var4.getValue() == f9.b.c
                           || var4.getValue() == f9.b.T
                           || K((f9.b)var4.getValue())
                     )) {
                     this.b(var1, var6, var1.level.getBlockState(var6));
                  }
               }
            }
         }
      } else {
         this.g = (boolean)0;
         this.I = (boolean)0;
      }
   }

   private void v(Minecraft var1) {
      String var2 = var1.level.dimension().identifier().toString();
      if (!var2.equals(this.l)) {
         this.M.clear();
         this.C();
         this.l = var2;
      }
   }

   public void b(Minecraft var1, BlockPos var2, BlockState var3) {
      if (var1 != null && var1.isSameThread() && var1.player != null && var1.level != null) {
         this.v(var1);
         String var4 = this.x(var1, var2);
         f9.b var5 = this.O.get(var4);
         if (var5 == f9.b.E || var5 == f9.b.P || var5 == f9.b.B || var5 == f9.b.L || var5 == f9.b.z || var5 == f9.b.c || var5 == f9.b.T || K(var5)) {
            int var6 = var3.getBlock() instanceof DispenserBlock && this.R(var3) ? 1 : 0;
            if (this.v(var4, (boolean)var6)) {
               l6 var7 = l6.E();
               if (!this.g && var7 != null) {
                  if (K(var5)) {
                     if (var7.c() && oi.A().D) {
                        int var12 = var5 == f9.b.y ? 1 : 0;
                        hh.K("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        oi.A().L(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == f9.b.E) {
                     ug var11 = ug.w();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     mz.F().s(var1, false);
                  } else if (var5 == f9.b.P) {
                     mm.W().q(var2, var3);
                     mz.F().s(var1, true);
                  } else if (var5 == f9.b.L || var5 == f9.b.z) {
                     u3 var10 = u3.H();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     jf.u().f(var1, var10, var5 == f9.b.L ? jf.k.Z : jf.k.x);
                  } else if (var5 == f9.b.c) {
                     uu var9 = uu.M();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     li.D().X(var1, var9);
                  } else if (var5 == f9.b.T) {
                     ue.V().u();
                  } else if (var7.c() && ft.V().x) {
                     um var8 = um.T();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.z(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean f(f9.b var1) {
      return !this.g && !this.T(var1).isEmpty();
   }

   private boolean v(String var1, boolean var2) {
      Boolean var3 = this.M.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String u(f9.b var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var1 != null) {
         String var3 = var2.level.dimension().identifier() + ";";
         String var4 = null;
         if (var2.player != null && var2.player.containerMenu instanceof DispenserMenu var5 && var5.containerId == this.d && this.O.get(this.i) == var1) {
            var4 = this.i;
         } else if (var2.hitResult instanceof BlockHitResult var6 && var6.getType() == Type.BLOCK) {
            var4 = this.x(var2, var6.getBlockPos());
         }

         return this.p(var1, var3, var2.player == null ? null : new Vec3(var2.player.getX(), var2.player.getY(), var2.player.getZ()), var4);
      } else {
         return null;
      }
   }

   String p(f9.b var1, String var2, Vec3 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.O.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            BlockPos var10 = this.y((String)var9.getKey());
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

   public BlockPos j(String var1) {
      return var1 == null ? null : this.y(var1);
   }

   public boolean D(f9.b var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4 != null && var4.player != null && var4.level != null && var1 != null) {
         BlockPos var5 = this.R(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.player.distanceToSqr(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void x(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 == this.d && var2.player.containerMenu instanceof DispenserMenu var3 && var3.containerId == var1) {
         f9.b var6 = this.O.get(this.i);
         if (var6 == f9.b.j) {
            ro.H().m(var2, var3, this.j(this.i));
         }

         hk var5 = hk.r(var6);
         if (var5 != null) {
            var5.I(var2, var3, this.j(this.i));
         } else if (var6 == f9.b.i || var6 == f9.b.u) {
            this.X(var2, var3, var6);
         } else if (var6 == f9.b.F) {
            f2.f().w(var2, var3);
         } else if (K(var6) && this.W != var1) {
            this.W = var1;
            oi.A().w(var2, var3);
            if (this.X(var2, var3, var6)) {
               hh.K("Paper", (var6 == f9.b.y ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.i);
            }
         }
      }
   }

   public void z(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null
         && var3.player != null
         && var1 == this.d
         && var2 >= 0
         && var2 < 9
         && var3.player.containerMenu instanceof DispenserMenu var4
         && var4.containerId == var1
         && K(this.o())) {
         ItemStack var7 = var4.getSlot(var2).getItem();
         if (!var7.isEmpty() && oi.A().K(this.o() == f9.b.y, var2, var7)) {
            l6 var6 = l6.E();
            if (var6 != null) {
               var6.h();
            }
         }
      }
   }

   private static boolean K(f9.b var0) {
      return var0 == f9.b.y || var0 == f9.b.C;
   }

   private void W(Minecraft var1, f9.b var2) {
      l6 var3 = l6.E();
      if (var3 != null && var2 != null) {
         if (var3.c()) {
            switch (var2) {
               case y:
               case C:
                  var3.c(l6.e.H);
                  break;
               case i:
                  var3.c(l6.e.E);
                  break;
               case j:
                  var3.c(l6.e.k);
                  break;
               case E:
                  var3.c(l6.e.F);
                  break;
               case P:
                  var3.c(l6.e.L);
                  break;
               case T:
                  var3.c(l6.e.Q);
                  break;
               case B:
                  var3.c(l6.e.y);
                  break;
               case L:
               case z:
                  var3.c(l6.e.h);
                  break;
               case c:
                  var3.c(l6.e.W);
                  break;
               case u:
                  var3.c(l6.e.v);
                  break;
               case g:
                  var3.c(l6.e.s);
                  break;
               case X:
                  var3.c(l6.e.w);
                  break;
               case F:
                  var3.c(l6.e.e);
            }
         }
      }
   }

   private String t(Minecraft var1) {
      if (var1.hitResult instanceof BlockHitResult var2) {
         String var13 = this.x(var1, var2.getBlockPos());
         if (var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock) {
            return this.O.containsKey(var13) ? var13 : null;
         }
      }

      if (this.J != null && this.O.containsKey(this.J)) {
         BlockPos var11 = this.y(this.J);
         String var14 = var1.level.dimension().identifier() + ";";
         if (var11 != null && this.J.startsWith(var14) && var1.player.distanceToSqr(var11.getX() + 0.5, var11.getY() + 0.5, var11.getZ() + 0.5) <= 64.0) {
            return this.J;
         }
      }

      String var12 = var1.level.dimension().identifier() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.O.keySet()) {
         if (var7.startsWith(var12)) {
            BlockPos var8 = this.y(var7);
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

   private void c(Minecraft var1) {
      if (var1.gui.screen() == null) {
         if (var1.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            Block var7 = var1.level.getBlockState(var2.getBlockPos()).getBlock();
            if (var7 instanceof NoteBlock) {
               this.I = (boolean)1;
               this.g = (boolean)0;
            } else if (!(var7 instanceof DispenserBlock)) {
               this.g = (boolean)0;
            } else {
               this.I = (boolean)0;
               String var4 = this.x(var1, var2.getBlockPos());
               mm var5 = mm.W();
               int var6 = var5.I() && var5.x() && this.o() == f9.b.P ? 1 : 0;
               this.g = (boolean)(!this.O.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.O.containsKey(var4)) {
                  this.J = var4;
                  if (!var4.equals(this.i)) {
                     this.i = var4;
                     this.d = -1;
                     this.V = 0;
                  }
               }
            }
         } else {
            this.g = (boolean)0;
         }
      }
   }

   private boolean X(Minecraft var1, DispenserMenu var2, f9.b var3) {
      int var4 = switch (var3) {
         case y -> this.d(var2, true);
         case C -> this.d(var2, false);
         case i -> this.b(var2);
         case j, g, X, F -> 0;
         case E -> this.d(var2, ug.w(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case P -> mm.W().S() ? 0 : this.d(var2, ug.w(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case T -> this.d(var2);
         case B -> this.j(var2, um.T(), 9);
         case L, z -> this.j(var2, u3.H(), 9);
         case c -> this.d(var2, uu.M(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case u -> this.U(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      l6 var5 = l6.E();
      if (var5 != null) {
         var5.h();
      }

      if (var3 == f9.b.u) {
         js var6 = js.A();
         hh.K(
            "Russian Roulette",
            "Loaded dispenser: normal=" + new ItemStack(var6.Z).getHoverName().getString() + ", trigger=" + new ItemStack(var6.E).getHoverName().getString()
         );
      }

      return true;
   }

   private int d(DispenserMenu var1, boolean var2) {
      ItemStack[] var3 = new ItemStack[9];
      Arrays.fill(var3, ItemStack.EMPTY);
      int var4 = 0;
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (V(var7) && var7.getContainerSlot() >= 0 && var7.getContainerSlot() <= 8) {
            ItemStack var8 = var7.hasItem() ? var7.getItem().copyWithCount(1) : ItemStack.EMPTY;
            var3[var7.getContainerSlot()] = var8;
            var4++;
            if (!var8.isEmpty()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         oi var9 = oi.A();
         byte var10 = var9.e(false);

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].isEmpty() && var9.K(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int b(DispenserMenu var1) {
      return xa.r().U(Minecraft.getInstance(), this.j(this.i), var1) ? 9 : 0;
   }

   private int j(DispenserMenu var1, ug var2, int var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (V(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            ItemStack var7 = var6.getItem();
            MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
            int var9 = n(var7.getHoverName().getString(), var3);
            if (var9 < 1 && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < var3) {
               var9 = var6.getContainerSlot() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof um) {
                  String var10 = var7.getHoverName().getString().replaceAll("[\\p{Cntrl}]", " ");
                  hh.L(
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

   private int d(DispenserMenu var1) {
      ue var2 = ue.V();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            Slot var5 = null;

            for (Slot var7 : var1.slots) {
               if (V(var7) && var7.getContainerSlot() == var4) {
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
      var2.F();
      return var3;
   }

   private int d(DispenserMenu var1, ug var2, int[] var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (V(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
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

   private int U(DispenserMenu var1) {
      return js.A().y(Minecraft.getInstance(), this.j(this.i), var1) ? 9 : 0;
   }

   private static boolean V(Slot var0) {
      return var0 != null && !(var0.container instanceof Inventory);
   }

   private static int n(String var0, int var1) {
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

   private boolean R(BlockState var1) {
      return var1 != null && var1.hasProperty(BlockStateProperties.TRIGGERED) && (Boolean)var1.getValue(BlockStateProperties.TRIGGERED);
   }

   public String x(Minecraft var1, BlockPos var2) {
      return var1.level.dimension().identifier() + ";" + var2.getX() + ";" + var2.getY() + ";" + var2.getZ();
   }

   private BlockPos y(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new BlockPos(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public BlockPos R(f9.b var1) {
      return this.j(this.u(var1));
   }

   private void C() {
      if (n() != null && Files.exists(n())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(n())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               f9.b var6 = f9.b.C(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.O.clear();
            this.O.putAll(var3);
         } catch (Exception var9) {
            r4.m("Rig", "Could not load dispenser assignments from {}: {}", n(), var9.getMessage());
         }
      }
   }

   private boolean i() {
      if (n() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.O.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((f9.b)var3.getValue()).v);
      }

      try {
         Files.createDirectories(n().getParent());
         Path var18 = n().resolveSibling(n().getFileName() + ".tmp");

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
            Files.move(var18, n(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, n(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(n())) {
            var20.load(var21);
         }

         for (Entry var23 : this.O.entrySet()) {
            if (!((f9.b)var23.getValue()).v.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         r4.m("Rig", "Could not save dispenser assignments to {}: {}", n(), var17.getMessage());
         return false;
      }
   }

   public enum b {
      y("paper_host", "Paper host"),
      C("paper_viewer", "Paper viewer"),
      i("forty_five_ten", "45/45/10"),
      j("fifty_fifty", "50/50"),
      E("blackjack", "Blackjack"),
      P("blackjack_fo", "Blackjack Force Only"),
      T("odd_even", "Even or Odd"),
      B("higher_lower", "Higher or Lower"),
      L("baccarat_player", "Baccarat Player"),
      z("baccarat_banker", "Baccarat Banker"),
      c("mexican_jack", "Mexican Jack"),
      u("russian", "Russian Roulette"),
      g("horse_racing", "Horse Racing"),
      X("armor_game", "Armor Game"),
      F("arrow", "Arrow Game");

      public final String v;
      public final String w;

      b(String var3, String var4) {
         this.v = var3;
         this.w = var4;
      }

      private static f9.b C(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return y;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return L;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (f9.b var5 : values()) {
               if (var5.v.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return E;
         }
      }
   }

   public record n(boolean success, String message) {
   }
}
