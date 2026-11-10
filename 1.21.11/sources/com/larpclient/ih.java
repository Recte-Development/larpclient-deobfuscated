package com.larpclient;

import com.mojang.blaze3d.shaders.ShaderType;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.lwjgl.opengl.GL31C;

public final class ih {
   static final class_2960 c = class_2960.method_60655("larp-client", "shaders/temple/sodium_terrain.vsh");
   static final class_2960 f = class_2960.method_60655("larp-client", "shaders/temple/sodium_terrain.fsh");
   static final String l = "blocks/block_layer_opaque";
   static final int B = 30;
   private static final Pattern a = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern X = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final Pattern h = Pattern.compile("(?m)^\\s*#moj_import\\s*<larp-client:([^>]+)>\\s*$");
   private static final List<String> g = List.of(
      "_vert_position",
      "u_RegionOffset",
      "_get_draw_translation",
      "_draw_id",
      "_vert_tex_light_coord",
      "_vert_color",
      "u_LightTex",
      "v_Color",
      "u_ProjectionMatrix",
      "u_ModelViewMatrix"
   );
   private static final List<String> Z = List.of("v_Color", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int R = 600;
   private static final IdentityHashMap<Object, Map<Object, Object>> D = new IdentityHashMap<>();
   private static final Object n = new Object();
   private static Method t;
   private static Method P;
   private static String y;
   private static String G;
   private static boolean z;
   private static boolean M;
   private static boolean Y;
   private static int j;
   private static int V;
   private static volatile boolean b;
   private static volatile boolean E;

   static boolean x() {
      return !FabricLoader.getInstance().isModLoaded("iris") && E().startsWith("0.8.");
   }

   static String E() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void G() {
      M = (boolean)1;
   }

   static boolean n() {
      return !M && j >= 600;
   }

   static boolean p() {
      return Y;
   }

   static boolean m() {
      return E;
   }

   static boolean e() {
      return !b && V >= 600;
   }

   static void i(boolean var0) {
      if (!M && ++j == 600) {
         q8.L("TerrainMaterials", "Sodium {} drew no terrain through the terrain material hook in {} frames; keeping Sodium output", E(), 600);
      }

      if (var0 && !b && ++V == 600) {
         q8.L("TerrainMaterials", "Sodium {} built no chunk mesh through the Snow Ground hook in {} frames; Snow Ground keeps Sodium output", E(), 600);
      }
   }

   public static Object z(Object var0, Object var1) {
      G();
      if (!Y && qs.A()) {
         try {
            Map var2 = D.computeIfAbsent(var0, var0x -> new HashMap<>());
            Object var3 = var2.get(var1);
            if (var3 == n) {
               return null;
            }

            if (var3 == null) {
               if (D(var1)) {
                  var2.put(var1, n);
                  return null;
               }

               var3 = a(var0, var1);
               var2.put(var1, var3);
               q8.G("TerrainMaterials", "Wet Ground and Snow Ground draw Sodium {} pass {} through their variant program", E(), var1);
            }

            GL31C.glBindBufferBase(35345, 30, qs.i());
            return var3;
         } catch (ReflectiveOperationException | IOException | RuntimeException var4) {
            Y = (boolean)1;
            q8.u("TerrainMaterials", "stage=sodium-variant; Wet Ground and Snow Ground keep Sodium output until resource reload", var4);
            qs.O();
            return null;
         }
      } else {
         return null;
      }
   }

   public static void k(Object var0) {
      if (!E && j5.K()) {
         try {
            for (Object var4 : (Object[])ih.a.b.get(var0)) {
               ih.a.f.setInt(var4, j5.y(ih.a.f.getInt(var4), ih.a.I.getFloat(var4), ih.a.P.getFloat(var4), ih.a.D.getFloat(var4)));
            }

            if (!b) {
               i();
            }
         } catch (ReflectiveOperationException | RuntimeException | LinkageError var5) {
            T(var5);
         }
      }
   }

   private static synchronized void i() {
      if (!b) {
         b = (boolean)1;
         q8.G("TerrainMaterials", "Snow Ground tags Sodium {} chunk meshes; relief follows block corners without subdivision", E());
      }
   }

   private static synchronized void T(Throwable var0) {
      if (!E) {
         E = (boolean)1;
         q8.u("TerrainMaterials", "stage=sodium-snow-mesh; Snow Ground keeps Sodium output until resource reload", var0);
         class_310 var1 = class_310.method_1551();
         if (var1 != null) {
            var1.execute(qs::O);
         }
      }
   }

   public static String K(class_2960 var0, String var1) {
      if (!z || var1 == null || !var0.method_12836().equals("sodium")) {
         return var1;
      } else if (var0.method_12832().equals("blocks/block_layer_opaque.vsh")) {
         return b(var1, y, ShaderType.VERTEX);
      } else {
         return var0.method_12832().equals("blocks/block_layer_opaque.fsh") ? b(var1, G, ShaderType.FRAGMENT) : var1;
      }
   }

   public static void d(Object var0) {
      Map var1 = D.remove(var0);
      if (var1 != null) {
         C(var1);
      }
   }

   static void p() {
      for (Map var1 : D.values()) {
         C(var1);
      }

      D.clear();
      G = null;
      y = null;
      E = (boolean)0;
      Y = (boolean)0;
   }

   public static String b(String var0, String var1, ShaderType var2) {
      return v(var0, var1, var2, "larp_terrain");
   }

   public static String v(String var0, String var1, ShaderType var2, String var3) {
      for (String var5 : var2 == ShaderType.VERTEX ? g : Z) {
         if (!var0.contains(var5)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var5);
         }
      }

      Matcher var9 = a.matcher(var0);
      if (!var9.find()) {
         throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has no main()");
      } else {
         int var10 = var9.start();
         int var6 = var9.end();
         if (var9.find()) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has more than one main()");
         } else {
            String var7 = var0.substring(0, var10) + var1 + "\nvoid larp_sodium_main()" + var0.substring(var6);
            if (var2 == ShaderType.VERTEX) {
               return var7;
            } else {
               Matcher var8 = X.matcher(var7);
               if (!var8.find()) {
                  throw new IllegalStateException("Sodium fragment chunk shader no longer fogs color through _linearFog");
               } else {
                  var10 = var8.start();
                  var6 = var8.end();
                  if (var8.find()) {
                     throw new IllegalStateException("Sodium fragment chunk shader fogs color more than once");
                  } else {
                     return var7.substring(0, var10) + "fragColor = _linearFog(" + var3 + "(color)," + var7.substring(var6);
                  }
               }
            }
         }
      }
   }

   public static String g(String var0, Function<String, String> var1) {
      Matcher var2 = h.matcher(var0);
      StringBuilder var3 = new StringBuilder();

      while (var2.find()) {
         String var4 = (String)var1.apply(var2.group(1));
         if (var4 == null) {
            throw new IllegalStateException("Missing Larp shader include " + var2.group(1));
         }

         if (h.matcher(var4).find()) {
            throw new IllegalStateException("Nested Larp shader include in " + var2.group(1));
         }

         var2.appendReplacement(var3, Matcher.quoteReplacement(var4));
      }

      var2.appendTail(var3);
      return var3.toString();
   }

   private static Object a(Object var0, Object var1) throws ReflectiveOperationException, IOException {
      if (y == null || G == null) {
         y = g(d(c), ih::d);
         G = g(d(f), ih::d);
      }

      Method var2 = b(var0.getClass(), "createShader", 2);
      z = (boolean)1;

      Object var3;
      try {
         var3 = var2.invoke(var0, "blocks/block_layer_opaque", var1);
      } catch (InvocationTargetException var10) {
         throw var10.getCause() instanceof RuntimeException var5 ? var5 : new IllegalStateException(var10.getCause());
      } finally {
         z = (boolean)0;
      }

      Object var4 = var3.getClass().getMethod("bindUniformBlockOptional", String.class, int.class).invoke(var3, "LarpMaterial", 30);
      if (var4 == null) {
         I(var3);
         throw new IllegalStateException("Sodium terrain material variant lost its LarpMaterial block");
      } else {
         return var3;
      }
   }

   private static boolean D(Object var0) throws ReflectiveOperationException {
      if (t == null) {
         t = var0.getClass().getMethod("pass");
      }

      Object var1 = t.invoke(var0);
      if (P == null) {
         P = var1.getClass().getMethod("isTranslucent");
      }

      return (Boolean)P.invoke(var1);
   }

   private static void C(Map<Object, Object> var0) {
      for (Object var2 : var0.values()) {
         if (var2 != n) {
            try {
               I(var2);
            } catch (ReflectiveOperationException | RuntimeException var4) {
               q8.L("TerrainMaterials", "Sodium terrain material variant release failed: {}", var4.toString());
            }
         }
      }

      var0.clear();
   }

   private static void I(Object var0) throws ReflectiveOperationException {
      var0.getClass().getMethod("delete").invoke(var0);
   }

   private static Method b(Class<?> var0, String var1, int var2) throws NoSuchMethodException {
      for (Class var3 = var0; var3 != null; var3 = var3.getSuperclass()) {
         for (Method var7 : var3.getDeclaredMethods()) {
            if (var7.getName().equals(var1) && var7.getParameterCount() == var2) {
               var7.setAccessible(true);
               return var7;
            }
         }
      }

      throw new NoSuchMethodException(var0.getName() + "." + var1);
   }

   private static String d(String var0) {
      try {
         return d(class_2960.method_60655("larp-client", "shaders/include/" + var0));
      } catch (IOException var2) {
         return null;
      }
   }

   private static String d(class_2960 var0) throws IOException {
      try (InputStream var1 = class_310.method_1551().method_1478().open(var0)) {
         return new String(var1.readAllBytes(), StandardCharsets.UTF_8);
      }
   }

   private ih() {
   }

   private static final class a {
      static final String X = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer";
      static final String j = "net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder$Vertex";
      static final Field b;
      static final Field I;
      static final Field P;
      static final Field D;
      static final Field f;

      private static Field e(Class<?> var0, String var1, Class<?> var2) throws NoSuchFieldException {
         Field var3 = var0.getField(var1);
         if (var3.getType() != var2) {
            throw new NoSuchFieldException(var0.getName() + "." + var1 + " is no longer " + var2);
         } else {
            return var3;
         }
      }

      static {
         try {
            ClassLoader var0 = ih.class.getClassLoader();
            Class var1 = Class.forName("net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", false, var0);
            Class var2 = Class.forName("net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder$Vertex", false, var0);
            b = var1.getDeclaredField("vertices");
            if (b.getType() != var2.arrayType()) {
               throw new NoSuchFieldException(
                  "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer.vertices is no longer a vertex array"
               );
            }

            b.setAccessible(true);
            I = e(var2, "x", float.class);
            P = e(var2, "y", float.class);
            D = e(var2, "z", float.class);
            f = e(var2, "color", int.class);
         } catch (ReflectiveOperationException var3) {
            throw new ExceptionInInitializerError(var3);
         }
      }
   }
}
