#version 330
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
};


layout(std140) uniform DropletBatch { vec4 Drops[512]; vec4 CameraRight; vec4 CameraUp; vec4 SurfaceSize; };
flat in vec4 droplet;
out vec4 fragColor;
void main() {
    vec2 uv=gl_FragCoord.xy/SurfaceSize.xy;
    vec4 nearPoint=InverseViewProjection*vec4(uv*2.0-1.0,1,1);
    vec3 ray=normalize(nearPoint.xyz/nearPoint.w);
    float along=dot(ray,droplet.xyz);
    float discriminant=along*along-dot(droplet.xyz,droplet.xyz)+droplet.w*droplet.w;
    if (discriminant<=0.0) discard;
    float distance=along-sqrt(discriminant);
    if (distance<=.01) discard;
    vec3 position=ray*distance;
    vec3 normal=normalize(position-droplet.xyz);
    vec4 clip=ViewProjection*vec4(position,1);
    float z=clip.z/clip.w;
    gl_FragDepth=WaterFlags.w>.5?z:z*.5+.5;
    fragColor=vec4(normal*.5+.5,1);
}
