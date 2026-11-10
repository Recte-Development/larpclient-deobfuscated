package com.larpclient;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.ShaderSource.CachedIncludeSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.lwjgl.util.shaderc.ShadercIncludeResult;

public final class ob {
   private static final String R = "larp-client";
   private static final Pattern N = Pattern.compile("#moj_import\\s*<([^>]+)>");
   private static final Pattern W = Pattern.compile("(?m)^\\s*#version\\s+(\\d+)(\\s+core)?\\s*$");
   private static final Pattern X = Pattern.compile(
      "(?m)^([ \\t]*)((?:(?:flat|smooth|noperspective)\\s+)?)(in|out)\\s+(\\w+)\\s+(\\w+)\\s*(\\[\\s*(\\d+)\\s*\\])?\\s*;"
   );
   private static final Pattern f = Pattern.compile(
      "(?m)layout\\s*\\(\\s*location\\s*=\\s*(\\d+)\\s*\\)\\s*(?:(?:flat|smooth|noperspective)\\s+)?out\\s+\\w+\\s+(\\w+)"
   );

   public static ShaderSource e(RenderPipeline var0, ShaderSource var1) {
      Map var2 = var0.getShaders();
      return var2.values().stream().noneMatch(var0x -> "larp-client".equals(var0x.getNamespace()))
         ? var1
         : new ob.j(var1, (Identifier)var2.get(ShaderType.VERTEX));
   }

   private static String X(String var0, ShaderType var1, Map<String, Integer> var2, Identifier var3) {
      LinkedHashMap var4 = new LinkedHashMap<>(Map.of("in", 0, "out", 0));
      if (var1 == ShaderType.FRAGMENT && !var2.isEmpty()) {
         var4.put("in", var2.values().stream().mapToInt(Integer::intValue).max().orElse(-1) + 4);
      }

      Matcher var5 = X.matcher(var0);
      StringBuilder var6 = new StringBuilder();

      while (var5.find()) {
         String var7 = var5.group(3);
         String var8 = var5.group(4);
         String var9 = var5.group(5);
         int var10 = var5.group(7) == null ? 1 : Integer.parseInt(var5.group(7));
         int var11 = f(var8) * var10;
         int var12;
         if (var1 == ShaderType.FRAGMENT && var7.equals("in") && var2.containsKey(var9)) {
            var12 = (Integer)var2.get(var9);
         } else {
            var12 = (Integer)var4.get(var7);
            var4.put(var7, var12 + var11);
            if (var1 == ShaderType.FRAGMENT && var7.equals("in") && !var2.isEmpty()) {
               _q.R("Shader", var3 + " reads " + var9 + " which its vertex shader does not write");
            }
         }

         var5.appendReplacement(
            var6,
            Matcher.quoteReplacement(
               var5.group(1)
                  + "layout(location = "
                  + var12
                  + ") "
                  + var5.group(2)
                  + var7
                  + " "
                  + var8
                  + " "
                  + var9
                  + (var5.group(6) == null ? "" : var5.group(6))
                  + ";"
            )
         );
      }

      var5.appendTail(var6);
      return var6.toString();
   }

   private static int f(String var0) {
      return switch (var0) {
         case "mat2", "dmat2" -> 2;
         case "mat3", "dmat3" -> 3;
         case "mat4", "dmat4" -> 4;
         default -> 1;
      };
   }

   private ob() {
   }

   private record j(ShaderSource source, @Nullable Identifier vertex) implements ShaderSource {
      public @Nullable String getShader(Identifier var1, ShaderType var2) {
         String var3 = this.source.getShader(var1, var2);
         if (var3 != null && "larp-client".equals(var1.getNamespace())) {
            try {
               Map var4 = var2 == ShaderType.FRAGMENT && this.vertex != null ? this.vertexOutputs(this.vertex) : Map.of();
               return this.convert(var1, var3, var2, var4);
            } catch (RuntimeException var5) {
               _q.o("Shader", "26.3 shader conversion failed for " + var1 + "; compiling the original text", var5);
               return var3;
            }
         } else {
            return var3;
         }
      }

      private Map<String, Integer> vertexOutputs(Identifier var1) {
         String var2 = this.source.getShader(var1, ShaderType.VERTEX);
         if (var2 == null) {
            return Map.of();
         }

         if ("larp-client".equals(var1.getNamespace())) {
            var2 = this.convert(var1, var2, ShaderType.VERTEX, Map.of());
         }

         HashMap var3 = new HashMap();
         Matcher var4 = ob.f.matcher(var2);

         while (var4.find()) {
            var3.put(var4.group(2), Integer.parseInt(var4.group(1)));
         }

         return var3;
      }

      private @Nullable String includeText(Identifier var1) {
         CachedIncludeSource var2 = this.source.getInclude(var1);
         return var2 != null ? ShadercIncludeResult.create(var2.includeResultPtr()).contentString() : this.source.getShader(var1, ShaderType.FRAGMENT);
      }

      public @Nullable CachedIncludeSource getInclude(Identifier var1) {
         return this.source.getInclude(var1);
      }

      public void close() {
      }

      private String convert(Identifier var1, String var2, ShaderType var3, Map<String, Integer> var4) {
         String var5 = this.inline(var2, new HashSet<>());
         Matcher var6 = ob.W.matcher(var5);
         String var7 = "#version 330\n#extension GL_ARB_separate_shader_objects : require\n";
         var5 = var6.find() ? var5.substring(0, var6.start()) + var7 + var5.substring(var6.end()) : var7 + var5;
         var5 = var5.replaceAll("\\bgl_VertexID\\b", "gl_VertexIndex").replaceAll("\\bgl_InstanceID\\b", "gl_InstanceIndex");
         return ob.X(var5, var3, var4, var1);
      }

      private String inline(String var1, Set<Identifier> var2) {
         Matcher var3 = ob.N.matcher(var1);
         StringBuilder var4 = new StringBuilder();

         while (var3.find()) {
            Identifier var5 = Identifier.parse(var3.group(1));
            String var6;
            if (!"larp-client".equals(var5.getNamespace())) {
               var6 = "#include <" + var5 + ">";
            } else if (!var2.add(var5)) {
               var6 = "";
            } else {
               String var7 = this.includeText(var5);
               if (var7 == null) {
                  throw new IllegalStateException("Missing Larp include " + var5);
               }

               var6 = ob.W.matcher(this.inline(var7, var2)).replaceAll("");
            }

            var3.appendReplacement(var4, Matcher.quoteReplacement(var6));
         }

         var3.appendTail(var4);
         return var4.toString();
      }
   }
}
