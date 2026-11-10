package com.larpclient.module;

import com.larpclient.b0;
import com.larpclient.b5;
import com.larpclient.cf;
import com.larpclient.hh;
import com.larpclient.ho;
import com.larpclient.j4;
import com.larpclient.ja;
import com.larpclient.k5;
import com.larpclient.kj;
import com.larpclient.ko;
import com.larpclient.kz;
import com.larpclient.ln;
import com.larpclient.pj;
import com.larpclient.po;
import com.larpclient.sh;
import com.larpclient.uy;
import com.larpclient.wa;
import com.larpclient.zj;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int k = 4;
   public static final double p = 0.45;
   private static KillFXModule a;
   private final l G = this.addSetting(
      new l(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss",
         "Sakura",
         "Shatter",
         "Rug Pull"
      )
   );
   private final l v = this.addSetting(new l("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom"));
   private final i K = this.addSetting(new i("Primary Color", "Main energy color", -10101505));
   private final i i = this.addSetting(new i("Secondary Color", "Trail and impact color", -32547));
   private final s E = this.addSetting(new s("Bloom", "Use the shared world bloom pass", true));
   private final s m = this.addSetting(new s("Sound", "Local windup and finishing cues", true));
   private final com.larpclient.module.e b = this.addSetting(new com.larpclient.module.e("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final com.larpclient.module.e A = this.addSetting(new com.larpclient.module.e("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final ko V = new ko();
   private final po N = new po();
   private final ArrayList<KillFXModule.r> Z = new ArrayList<>();
   private List<KillFXModule.e> M = List.of();
   private Object h;
   private Object j;
   private int g = -1;
   private int Q = -1;
   private long L;
   private Vec3 W;
   private b0 F;
   private CameraRenderState S;
   private static final RenderStateDataKey<KillFXModule.p> D = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      a = this;
      this.K.P(() -> this.v.O("Custom"));
      this.i.P(() -> this.v.O("Custom"));
      this.b.P(() -> this.m.getValue());
      this.G.o(var1 -> {
         this.r();
         com.larpclient.h.A("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule t() {
      return a;
   }

   public List<q<?>> D() {
      return List.of(this.G, this.v, this.K, this.i);
   }

   public q<?> j() {
      return this.G;
   }

   public List<q<?>> t() {
      return List.of(this.E, this.m, this.b);
   }

   public List<q<?>> F() {
      return List.of(this.A);
   }

   public String Z() {
      return switch ((String)this.G.getValue()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         case "Rug Pull" -> "A trading chart pumps from $0 to $1,000 beside the victim, then a red candle crashes through the floor, the rug is yanked away and the profit drops to -$67,000 while a headline falls from the sky and floats in front of the chart.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.r();
      com.larpclient.h.A("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.G.getValue());
   }

   @Override
   public void onDisable() {
      this.r();
   }

   public void r() {
      this.V.R();
      this.Z.clear();
      this.M = List.of();
      this.N.d();
      if (this.F != null) {
         this.F.close();
         this.F = null;
      }

      this.S = null;
      this.h = this.j = null;
      this.W = null;
      this.g = -1;
   }

   private boolean v() {
      Minecraft var1 = getMc();
      if (this.isRuntimeActive() && var1.level != null && var1.player != null) {
         if (this.h != var1.level || this.j != var1.player || this.g != wa.s || this.W != null && this.W.distanceToSqr(var1.player.position()) > 256.0) {
            this.r();
            this.h = var1.level;
            this.j = var1.player;
            this.g = wa.s;
         }

         this.W = var1.player.position();
         return true;
      } else {
         this.r();
         return false;
      }
   }

   public void K(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.v()) {
            Minecraft var2 = getMc();
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.V.q(var3, var1.sourceCauseId() == var2.player.getId(), cf.I());
            }
         }
      }
   }

   public void H(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (this.v()) {
            Minecraft var2 = getMc();
            if (var1.getEntity(var2.level) instanceof AbstractClientPlayer var4 && var4 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.V.u(var4);
                  this.Z.removeIf(var1x -> var1x.l == var4);
                  this.M = this.M.stream().filter(var1x -> var1x.owner.l != var4).toList();
               } else if (var1.getEventId() == 3 && this.V.T(var4, cf.I())) {
                  if (!(var4.position().distanceToSqr(var2.player.position()) > this.A.getValue() * this.A.getValue())) {
                     double var5 = cf.I();
                     pj.f var7 = this.e();
                     int var8 = var7 == pj.f.h ? -19633 : (var7 == pj.f.X ? -5144321 : -10101505);
                     int var9 = var7 == pj.f.h ? -8915729 : (var7 == pj.f.X ? -8328705 : -32547);
                     if (var7 == pj.f.P) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == pj.f.Q || var7 == pj.f.E) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == pj.f.m) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == pj.f.C) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (var7 == pj.f.L) {
                        var8 = -13242230;
                        var9 = -50360;
                     }

                     if (this.v.O("Custom")) {
                        var8 = this.K.getValue();
                        var9 = this.i.getValue();
                     } else if (this.v.O("Theme")) {
                        var8 = ho.J;
                        var9 = ho.ds;
                     }

                     long var10 = var4.getUUID().getMostSignificantBits() ^ var4.getUUID().getLeastSignificantBits() ^ ++this.L;
                     double var12 = Math.toRadians(var4.yBodyRot);
                     pj var14 = new pj(var7, var4.position(), var12, var5, var10, var4.getSkin().model() == PlayerModelType.SLIM);
                     List var15 = this.Z.stream().filter(var0 -> !var0.F()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.r)var15.getFirst()).p(var5);
                        com.larpclient.h.U("KillFX", "Finisher limit reached; retiring the oldest ({}) over {}s", ((KillFXModule.r)var15.getFirst()).w.h, 0.45);
                     }

                     while (this.Z.size() >= 8) {
                        this.Z.removeFirst();
                     }

                     KillFXModule.r var16 = new KillFXModule.r(var4, var14, var8, var9, this.E.getValue(), this.m.getValue(), this.b.getValue());
                     this.Z.add(var16);
                     this.p(
                        var16,
                        var7 == pj.f.h
                           ? SoundEvents.FIRECHARGE_USE
                           : (
                              var7 == pj.f.m
                                 ? SoundEvents.CHERRY_LEAVES_BREAK
                                 : (
                                    var7 == pj.f.C
                                       ? SoundEvents.PLAYER_HURT_FREEZE
                                       : (
                                          var7 == pj.f.L
                                             ? (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value()
                                             : (
                                                var7 == pj.f.E
                                                   ? SoundEvents.PLAYER_ATTACK_SWEEP
                                                   : (var7 == pj.f.Q ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.AMETHYST_BLOCK_RESONATE)
                                             )
                                       )
                                 )
                           ),
                        0.8F
                     );
                     com.larpclient.h.U("KillFX", "Confirmed player death: id={}, style={}, source=recent observed server damage", var4.getId(), var7);
                  }
               }
            }
         }
      }
   }

   private pj.f e() {
      if (!this.G.O("Random")) {
         return switch ((String)this.G.getValue()) {
            case "Execution" -> pj.f.e;
            case "Singularity" -> pj.f.X;
            case "Star of David" -> pj.f.P;
            case "Tung Tung" -> pj.f.Q;
            case "Tung Tung Floss" -> pj.f.E;
            case "Sakura" -> pj.f.m;
            case "Shatter" -> pj.f.C;
            case "Rug Pull" -> pj.f.L;
            default -> pj.f.h;
         };
      } else {
         int var1 = pj.f.values().length;
         int var2 = Math.floorMod(this.L * 31L + Double.doubleToLongBits(cf.I()), var1);
         if (var2 == this.Q) {
            var2 = (var2 + 1) % var1;
         }

         this.Q = var2;
         return pj.f.values()[var2];
      }
   }

   public void i() {
      if (this.isEnabled()) {
         if (this.v()) {
            Minecraft var1 = getMc();
            double var2 = cf.I();
            this.V.P(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
            this.Z.removeIf(var2x -> var2x.w.n(var2) || var2x.F() && var2 - var2x.t >= 0.45);

            for (KillFXModule.r var5 : this.Z) {
               if (var5.g && !var5.l.isRemoved() && var5.l.isAlive() && !var5.F()) {
                  var5.p(var2);
                  com.larpclient.h.U("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.l.getId(), var5.w.h);
               }

               var5.g = (boolean)(var5.g | (!var5.l.isAlive() ? 1 : 0));
               var5.w.O(var2, (var1x, var2x) -> b5.r(var1.level, var1x, var2x));
               KillFXModule.y[] var6 = v(var5.w.h);

               while (var5.Q < var6.length && var2 - var5.w.U >= var6[var5.Q].time()) {
                  KillFXModule.y var7 = var6[var5.Q++];
                  var5.c = (boolean)1;
                  if (!var5.F()) {
                     this.p(var5, var7.event(), var7.pitch());
                  }
               }
            }
         }
      }
   }

   private static KillFXModule.y[] v(pj.f var0) {
      return switch (var0) {
         case e -> new KillFXModule.y[]{new KillFXModule.y(0.85, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 1.25F)};
         case X -> new KillFXModule.y[]{new KillFXModule.y(1.9, (SoundEvent)SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 1.25F)};
         case E -> new KillFXModule.y[]{new KillFXModule.y(1.62, SoundEvents.PLAYER_BIG_FALL, 1.25F)};
         case Q -> new KillFXModule.y[]{new KillFXModule.y(1.56, SoundEvents.ANVIL_LAND, 1.25F)};
         case P -> new KillFXModule.y[]{new KillFXModule.y(1.85, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.25F)};
         case h -> new KillFXModule.y[]{new KillFXModule.y(1.25, SoundEvents.BEACON_DEACTIVATE, 1.25F)};
         case m -> new KillFXModule.y[]{
            new KillFXModule.y(1.0, SoundEvents.BEACON_ACTIVATE, 1.4F),
            new KillFXModule.y(3.2, (SoundEvent)SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.1F),
            new KillFXModule.y(5.0, SoundEvents.AMETHYST_BLOCK_CHIME, 0.9F)
         };
         case C -> new KillFXModule.y[]{
            new KillFXModule.y(0.95, SoundEvents.AMETHYST_BLOCK_CHIME, 1.6F),
            new KillFXModule.y(1.32, SoundEvents.GLASS_BREAK, 0.75F),
            new KillFXModule.y(1.87, SoundEvents.AMETHYST_CLUSTER_FALL, 1.3F)
         };
         case L -> new KillFXModule.y[]{
            new KillFXModule.y(0.7, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.75F),
            new KillFXModule.y(1.1, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.9F),
            new KillFXModule.y(1.45, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.05F),
            new KillFXModule.y(1.75, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.25F),
            new KillFXModule.y(2.0, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.5F),
            new KillFXModule.y(2.3, SoundEvents.EXPERIENCE_ORB_PICKUP, 1.6F),
            new KillFXModule.y(2.6, (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.value(), 0.5F),
            new KillFXModule.y(2.64, (SoundEvent)SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 0.5F),
            new KillFXModule.y(2.82, SoundEvents.MACE_SMASH_GROUND_HEAVY, 0.8F),
            new KillFXModule.y(3.25, SoundEvents.VILLAGER_NO, 0.85F)
         };
      };
   }

   private void p(KillFXModule.r var1, SoundEvent var2, float var3) {
      if (var1.u && !(var1.i <= 0.0) && getMc().level != null) {
         Vec3 var4 = var1.w.v;
         float var10006 = (float)var1.i;
         getMc().level.playLocalSound(var4.x, var4.y, var4.z, var2, SoundSource.PLAYERS, var10006, var3, false);
      }
   }

   public void o(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3 var2 = var1.pos;
         if (this.F != null) {
            this.F.i();
         }

         this.S = null;
         this.M = List.of();
         if (this.v() && !this.Z.isEmpty()) {
            ArrayList var3 = new ArrayList();
            Vector3f var4 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var5 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3 var6 = new Vec3(var4.x, var4.y, var4.z);
            Vec3 var7 = new Vec3(var5.x, var5.y, var5.z);
            this.N.h();
            ArrayList var8 = new ArrayList();

            for (KillFXModule.r var10 : this.Z) {
               if (!(var10.w.v.distanceToSqr(var2) > this.A.getValue() * this.A.getValue())) {
                  if (var10.P == null) {
                     if (!(j4.d(var10.l, 1.0F) instanceof AvatarRenderState var12)) {
                        com.larpclient.h.w("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                        continue;
                     }

                     var10.P = var12;
                  }

                  double var20 = Math.max(0.0, cf.F() - var10.w.U);
                  uy.b var13 = var10.w.h == pj.f.P ? uy.Z(uy.B(var10.w, var20), var2, var6, var7) : (var10.w.s() != null ? var10.w.s().i(var2, cf.F()) : uy.V);
                  pj.n[] var14 = new pj.n[6];
                  double var15 = var10.w(cf.F());
                  if (var10.w.s() != null) {
                     if (this.F == null) {
                        this.F = new b0();
                     }

                     if (this.F.M()) {
                        try {
                           var10.w.s().Y(var13, var20, var15, this.F::B, var3);
                        } catch (RuntimeException var19) {
                           com.larpclient.h.w("KillFX", "Rug Pull chart labels failed; the chart continues without text", var19);
                        }
                     }
                  }

                  for (int var17 = 0; var17 < var14.length; var17++) {
                     pj.n var18 = var10.w.i(var17, var20, cf.l(), var13);
                     var14[var17] = var15 >= 1.0
                        ? var18
                        : new pj.n(
                           var18.position(),
                           var18.rx(),
                           var18.ry(),
                           var18.rz(),
                           var18.scale(),
                           var18.alpha() * var15,
                           var18.dissolve(),
                           var18.landed(),
                           var18.orientation(),
                           var18.stretch()
                        );
                  }

                  ln.v var21 = null;
                  ja.w var22 = null;
                  if (var10.w.h == pj.f.Q) {
                     k5.P();
                     if (var10.W == null) {
                        var10.W = new ln(k5.k());
                        com.larpclient.h.U("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                     }

                     var21 = var10.W.j(var10.w, var20, var2);
                  }

                  if (var10.w.h == pj.f.E) {
                     k5.P();
                     if (var10.r == null) {
                        var10.r = new ja(k5.k());
                        com.larpclient.h.U("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                     }

                     var22 = var10.r.V(var10.w, var20, var2);
                  }

                  var8.add(new KillFXModule.e(var10, var10.P, var14, var20, var2, var13, var21, var22, this.g, var15));
               }
            }

            this.M = List.copyOf(var8);
            if (this.F != null) {
               this.F.Q(var3, var1);
               this.S = var1;
            }
         }
      }
   }

   public boolean F() {
      return !this.Z.isEmpty();
   }

   public void I(Entity var1, EntityRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.level() == this.h) {
            for (KillFXModule.r var4 : this.Z) {
               if (var4.l == var1) {
                  var2.setData(D, new KillFXModule.p(var1, this.h, this.g));
                  return;
               }
            }
         }
      }
   }

   public boolean B(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && kz.x() && kz.u("KillFX") && !j4.X()) {
         KillFXModule.p var2 = (KillFXModule.p)var1.getData(D);
         if (var2 != null && var2.world == getMc().level && var2.generation == wa.s) {
            for (KillFXModule.e var4 : this.M) {
               if (var4.owner.l == var2.victim
                  && this.Z.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof LivingEntityRenderState var5 && var5.deathTime > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean t() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.M.stream().anyMatch(var0 -> var0.owner.V);
   }

   public void H(kj var1, PoseStack var2, hh var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            long var5 = zj.m();

            try {
               for (KillFXModule.e var8 : this.M) {
                  if (var8.generation == wa.s && (!var4 || var8.owner.V)) {
                     this.N.b(var8.skin, var8.owner.w, var8.parts, var8.camera, var2, var3, var8.owner.Y, var4);
                     if (!var4 && var8.strike != null) {
                        k5.U(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade);
                     }

                     if (!var4 && var8.floss != null) {
                        k5.U(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade);
                     }

                     sh.K(var1, var8.owner.w, var8.parts, var8.age, var8.owner.Y, var8.owner.I, var4, var8.fade, var8.starPlane, var8.strike, var8.floss);
                  }
               }

               if (!var4 && this.F != null && this.S != null) {
                  this.F.f(var2, this.S, false, false, 1.0);
               }
            } finally {
               zj.l("KillFX", var5);
            }
         }
      }
   }

   private record e(
      KillFXModule.r owner, AvatarRenderState skin, pj.n[] parts, double age, Vec3 camera, uy.b starPlane, ln.v strike, ja.w floss, int generation, double fade
   ) {
   }

   private record p(Object victim, Object world, int generation) {
   }

   private static final class r {
      final AbstractClientPlayer l;
      final pj w;
      final int Y;
      final int I;
      final boolean V;
      final boolean u;
      final double i;
      AvatarRenderState P;
      ln W;
      ja r;
      boolean c;
      boolean g;
      int Q;
      double t = Double.NaN;

      r(AbstractClientPlayer var1, pj var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.l = var1;
         this.w = var2;
         this.Y = var3;
         this.I = var4;
         this.V = var5;
         this.u = var6;
         this.i = var7;
      }

      boolean F() {
         return !Double.isNaN(this.t);
      }

      void p(double var1) {
         if (!this.F()) {
            this.t = var1;
         }
      }

      double w(double var1) {
         return this.F() ? 1.0 - pj.Z((var1 - this.t) / 0.45) : 1.0;
      }
   }

   private record y(double time, SoundEvent event, float pitch) {
   }
}
