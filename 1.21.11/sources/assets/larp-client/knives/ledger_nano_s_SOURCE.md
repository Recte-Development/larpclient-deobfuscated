# Ledger Nano S attribution

Ledger Nano S by rtql8d (https://sketchfab.com/rtql8d)
Source: https://sketchfab.com/3d-models/ledger-nano-s-48460b4b82724e9b8d03c0c3574793a5
License: Creative Commons Attribution 4.0 International
https://creativecommons.org/licenses/by/4.0/

The user supplied the exact Sketchfab GLB with its embedded 2048px steel texture.
Original source: tools/assets/ledger/ledger_nano_s.glb.
Reproduce conversion with tools/import_ledger_model.py using NumPy and Pillow.

Changes: scene/axis conversion, uniform scale and grip translation, opaque
material atlas, charcoal adaptation of black PBR materials for vanilla lighting,
and explicit backfaces for the source's double-sided materials. Original device
shape, smooth normals and steel diffuse imagery are retained. Metallic/roughness
PBR reflections are not implemented by the existing Minecraft entity-solid pass.
Source and output hashes and material counts: ledger_nano_s_PROVENANCE.json.
The repository's license does not replace this asset's CC BY 4.0 license.
