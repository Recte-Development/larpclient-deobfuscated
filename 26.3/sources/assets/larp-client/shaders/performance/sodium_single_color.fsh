// Single Color Blocks for Sodium 0.9 chunk terrain. SingleColorTerrain inserts this ahead of Sodium's
// own fragment main(), renamed larp_sodium_main(), and routes Sodium's unfogged color through
// larp_single_block(). Sodium's alpha cutout has already run on the textured color.
in vec3 larp_single;

vec4 larp_single_block(vec4 color) {
    return vec4(larp_single, color.a);
}

void larp_sodium_main();

void main() {
    larp_sodium_main();
}
