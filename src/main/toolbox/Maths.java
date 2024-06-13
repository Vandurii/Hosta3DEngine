package main.toolbox;

import main.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static main.Configuration.*;

public class Maths {

    public static Matrix4f transform(Vector3f translation, Vector3f rotation, float scale){
        Matrix4f transform = new Matrix4f();
        transform.identity();
        transform.translate(translation);
        transform.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        transform.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        transform.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        transform.scale(scale);

        return transform;
    }

    public static Matrix4f createProjection(){
        float aspectRatio = (float) WINDOW_WIDTH / WINDOW_HEIGHT;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLAN - NEAR_PLAN;

        Matrix4f projection = new Matrix4f();
        projection.identity();
        projection.m00(xScale);
        projection.m11(yScale);
        projection.m22(-((FAR_PLAN + NEAR_PLAN) / frustumLength));
        projection.m23(-1);
        projection.m32(-((2 * NEAR_PLAN * FAR_PLAN) / frustumLength));
        projection.m33(0);

        return projection;
    }

    public static Matrix4f createView(Camera camera){
        Matrix4f view = new Matrix4f();
        view.identity();
        view.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        view.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        view.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z));

        return view;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

}
