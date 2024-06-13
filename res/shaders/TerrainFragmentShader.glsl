#version 400
// settings
float minBrightness = 0.2;

// from vertex
in vec3 fPos;
in vec2 fTexCords;

in vec3 fNormals;
in vec3 toLightVector;

in vec3 toCamVector;

in float visibility;

// out
out vec4 outCol;

// Uploaded values.
uniform vec3 lightColor;

uniform float shineDamper;
uniform float reflectivity;

uniform vec4 skyColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

void main(){
    // Texture color.
    vec4 blendMapColor = texture(blendMap, fTexCords);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tileCords = fTexCords * 40;

    vec4 backgroundTextureColor = texture(backgroundTexture, tileCords) * backTextureAmount;
    vec4 rTextureColor = texture(rTexture, tileCords) * blendMapColor.r;
    vec4 gTextureColor = texture(gTexture, tileCords) * blendMapColor.g;
    vec4 bTextureColor = texture(bTexture, tileCords) * blendMapColor.b;
    vec4 textureColor = backgroundTextureColor + rTextureColor + bTextureColor + gTextureColor;

    // Light difusion.
    vec3 unitNormal = normalize(fNormals);
    vec3 unitLightVector = normalize(toLightVector);
    float nDot = dot(unitNormal, unitLightVector);
    float brightness = max(nDot, minBrightness);
    vec3 diffuse = brightness * lightColor;

    // Light reflection.
    vec3 unitCamVector = normalize(toCamVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitNormal);
    specularFactor = max(specularFactor, 0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    // Out color.
    outCol = vec4(diffuse, 0) * textureColor ;//+ vec4(finalSpecular, 1);
    outCol = mix(skyColor, outCol, visibility);
}