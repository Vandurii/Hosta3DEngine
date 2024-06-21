package main.toolbox;

import main.controllers.MouseListener;
import main.entities.Camera;
import main.terrains.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static main.Configuration.WINDOW_HEIGHT;
import static main.Configuration.WINDOW_WIDTH;

public class MousePicker {

    private int recursionCount = 200;
    private int rayRange = 600;

    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    private Vector3f currentRay;
    private Matrix4f projection;
    private Matrix4f view;
    private Camera camera;

    public MousePicker(Camera camera, Matrix4f projection, Terrain terrain){
        this.camera = camera;
        this.terrain = terrain;
        this.projection = projection;
        this.view = Maths.createView(camera);
    }

    public void update(){
        view = Maths.createView(camera);
        currentRay = calculateRay();

        if (intersectionInRange(0, rayRange, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, rayRange, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    public Vector3f calculateRay(){
        MouseListener mouse = MouseListener.getInstance();
        Vector2f normalizedCords = viewPortToNormalizedCords((float) mouse.getX(), (float) mouse.getY());
        Vector4f clipCords = new Vector4f(normalizedCords.x, normalizedCords.y, -1, 1);
        Vector4f eyeCords = clipToEyeCords(clipCords);
        Vector3f worldCords = eyeToWorldCords(eyeCords);

        return  worldCords;
    }

    public Vector2f viewPortToNormalizedCords(float mouseX, float mouseY){
        float x = ((mouseX * 2) / WINDOW_WIDTH) - 1;
        float y = ((mouseY * 2) / WINDOW_HEIGHT) - 1;

        return new Vector2f(x, -y);
    }

    public Vector4f clipToEyeCords(Vector4f clipCords){
        Matrix4f invertedProjection = new Matrix4f();
        projection.invert(invertedProjection);
        Vector4f eyeCords = invertedProjection.transform(clipCords);

        return new Vector4f(eyeCords.x, eyeCords.y, -1, 0);
    }

    public Vector3f eyeToWorldCords(Vector4f eyeCords){
        Matrix4f invertedView = new Matrix4f();
        view.invert(invertedView);
        Vector4f worldCords = invertedView.transform(eyeCords);
        worldCords.normalize();

        return new Vector3f(worldCords.x, worldCords.y, worldCords.z);
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }


    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return new Vector3f(start.x, start.y, start.z).add(scaledRay);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= recursionCount) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.x, endPoint.z);
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.x, testPoint.z);
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }

    public Vector3f getCurrentTerrainPoint(){
        return currentTerrainPoint;
    }
}
