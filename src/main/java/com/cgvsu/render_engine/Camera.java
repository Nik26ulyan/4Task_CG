package com.cgvsu.render_engine;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

public class Camera {

    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        this.distance = (float) Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.z, 2));
        rot = false;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void movePosition(final Vector3f translation) {
        this.position.add(translation);
    }

    public void movePositionTarget(final Vector3f translation) {
        this.position.add(translation);
        this.target.add(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.add(translation);
    }

    public void circleHorMovePosition(float translation) {
        var rad = (Math.PI / 180) * translation;

        position.x = (float) (position.x * Math.cos(rad) - position.z * Math.sin(rad));
        position.z = (float) (position.x * Math.sin(rad) + position.z * Math.cos(rad));
    }

    public void circleVerMovePosition(float angle) {
        if (Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.z, 2)) > 2) {
            rotateVerAroundTarget(angle);
        }
        else if ((angle > 0 && position.y >= 0) || (angle < 0 && position.y <= 0)) {
            rotateVerAroundTarget(angle);
        }
    }

    private void rotateVerAroundTarget(float angle) {
        Vector3f rotateVector = rotateVectorAroundVector(position, new Vector3f(0, 1, 0), 90);
        rotateVector.y = 0;
        position = rotateVectorAroundVector(position, rotateVector, angle);
    }

    private Vector3f rotateVectorAroundVector(Vector3f vector, Vector3f rotateVector, float angle) {
        var rad = (Math.PI / 180) * angle;
        Quat4f q = new Quat4f();
        rotateVector = normaliseVector(rotateVector);
        q.w = (float) Math.cos(rad / 2);
        q.x = (float) (rotateVector.x * Math.sin(rad / 2));
        q.y = (float) (rotateVector.y * Math.sin(rad / 2));
        q.z = (float) (rotateVector.z * Math.sin(rad / 2));

        Quat4f t = multiplyQV(q, vector);
        t = multiplyQQ(t, invertQuat(q));

        return new Vector3f(t.x, t.y, t.z);
    }

    private Quat4f invertQuat(Quat4f q) {
        Quat4f invQ = new Quat4f();

        invQ.w = q.w;
        invQ.x = -q.x;
        invQ.y = -q.y;
        invQ.z = -q.z;

        return normaliseQuat(invQ);
    }

    private Quat4f multiplyQQ(Quat4f a, Quat4f b) {
        Quat4f q = new Quat4f();
        q.w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
        q.x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
        q.y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x;
        q.z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w;

        return q;
    }

    private Quat4f multiplyQV(Quat4f a, Vector3f b) {
        Quat4f q = new Quat4f();

        q.w = -a.x * b.x - a.y * b.y - a.z * b.z;
        q.x = a.w * b.x + a.y * b.z - a.z * b.y;
        q.y = a.w * b.y - a.x * b.z + a.z * b.x;
        q.z = a.w * b.z + a.x * b.y - a.y * b.x;

        return q;
    }

    private Quat4f normaliseQuat(Quat4f q) {
        float n = (float) (1 / Math.sqrt(q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z));

        q.w = q.w * n;
        q.x = q.x * n;
        q.y = q.y * n;
        q.z = q.z * n;

        return q;
    }

    private Vector3f normaliseVector(Vector3f v) {
        float length = (float) Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
        Vector3f normal = new Vector3f();

        normal.x = v.x / length;
        normal.y = v.y / length;
        normal.z = v.z / length;

        return normal;
    }


    Matrix4f getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    private Vector3f position;
    private Vector3f target;
    private float distance;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
    private boolean rot;
}