// Wet Ground for Sodium 0.9 chunk terrain. SodiumTerrain inserts this ahead of Sodium's own
// vertex main(), renamed larp_sodium_main(); it is never compiled on its own.
out vec3 larp_position;
out float larp_sky;

void larp_sodium_main();

void main() {
    larp_sodium_main();
    // Sodium keeps the decoded vertex in globals. Rebuild its camera-relative position.
    larp_position = _vert_position + u_RegionOffset + _get_draw_translation(_draw_id);
    // Sodium stores sky light as the vanilla 0-240 lightmap value plus 8.
    larp_sky = max(_vert_tex_light_coord.y * 256.0 - 8.0, 0.0) / 240.0;
}
