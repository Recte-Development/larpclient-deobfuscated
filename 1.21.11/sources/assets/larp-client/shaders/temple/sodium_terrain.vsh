// Wet Ground and Snow Ground for Sodium 0.8 chunk terrain. SodiumTerrain resolves its Larp import
// and inserts this ahead of Sodium's own vertex main(), renamed larp_sodium_main(); it is never
// compiled on its own.
layout(std140) uniform LarpMaterial {
    vec4 WindTime;
    vec4 Water;
    vec4 WaterFlags;
    vec4 Wet;
    vec4 Modes;
    vec4 Sun;
    vec4 Sky;
    vec4 Camera;
    vec4 Snow;
    mat4 ViewProjection;
    mat4 InverseViewProjection;
    vec4 SnowTrailInfo;
    vec4 SnowTrailBounds;
    vec4 SnowTracks[48];
    vec4 CameraWorld;
};

#moj_import <larp-client:snow.glsl>

out vec3 larp_position;
out vec3 larp_base;
out float larp_sky;
out vec3 larp_light;
out float larp_snow_cap;
out float larp_snow_eligible;

void larp_sodium_main();

void main() {
    larp_sodium_main();
    // Sodium keeps the decoded vertex in globals. Rebuild its camera-relative position.
    vec3 position = _vert_position + u_RegionOffset + _get_draw_translation(_draw_id);
    larp_base = position;
    // Sodium stores sky light as the vanilla 0-240 lightmap value plus 8.
    larp_sky = max(_vert_tex_light_coord.y * 256.0 - 8.0, 0.0) / 240.0;
    vec4 lightmap = texture(u_LightTex, _vert_tex_light_coord);
    larp_light = lightmap.rgb;
    // Snow Ground's Sodium mesh hook writes the vanilla snow codes into vertex alpha.
    int code = int(round(_vert_color.a * 255.0));
    bool snow = code == 249 || code == 251 || code == 252;
    larp_snow_cap = code == 252 || code == 249 ? 1.0 : 0.0;
    larp_snow_eligible = snow ? 1.0 : 0.0;
    if (snow) v_Color.a = lightmap.a;
    if (code == 252 && Snow.x > 0.0) {
        float exposed = smoothstep(.92, .99, larp_sky);
        position.y += snowDisplacement(position + CameraWorld.xyz, larp_base, exposed, true);
        gl_Position = u_ProjectionMatrix * u_ModelViewMatrix * vec4(position, 1.0);
    }
    larp_position = position;
}
