#version 400
// settings
float minBrightness = 0.3;

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
uniform sampler2D sam;
uniform vec3 lightColor;

uniform float shineDamper;
uniform float reflectivity;

uniform vec4 skyColor;

void main(){
    // Texture color.
    vec4 textureColor = texture(sam, fTexCords);

    // don't draw pixels when they have more then 0.5 alpha color
    if(textureColor.a < 0.5){
        discard;
    }

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
    outCol = vec4(diffuse, 0) * textureColor + vec4(finalSpecular, 1);
    outCol = mix(skyColor, outCol, visibility);
}