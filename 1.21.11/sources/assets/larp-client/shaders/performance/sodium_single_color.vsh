// Single Color Blocks for Sodium 0.9 chunk terrain. SingleColorTerrain inserts this ahead of Sodium's
// own vertex main(), renamed larp_sodium_main(); it is never compiled on its own.
#moj_import <larp-client:single_color.glsl>

out vec3 larp_single;

void larp_sodium_main();

void main() {
    larp_sodium_main();
    // Sodium keeps the decoded vertex in globals; its color already carries shade and occlusion.
    larp_single = larp_single_color(_vert_color.rgb) * texture(u_LightTex, _vert_tex_light_coord).rgb;
}
