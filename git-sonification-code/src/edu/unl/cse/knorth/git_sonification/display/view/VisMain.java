package edu.unl.cse.knorth.git_sonification.display.view;

import processing.core.PApplet;
import processing.event.MouseEvent;

/**
 *
 * @author Shane
 */
public class VisMain extends PApplet {

    int num = 49;
    float mx[] = new float[num];
    float my[] = new float[num];

    float mouseStartX;
    float mouseStartY;

    float aspect;
    float s;

    float yPos;
    float playHead;
    float rotateAmt;

    int left;
    int right;
    int bottom;
    int top;

    public static void main(String args[]) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = VisMain.class.getCanonicalName();
        for (int i = 0; i < args.length; i++) {
            newArgs[i + 1] = args[i];
        }

        PApplet.main(newArgs);
    }

    @Override
    public void setup() {
        size(1200, 600, P3D);
        fill(200);
        aspect = (float) width / (float) height;
        s = aspect * 10;
        yPos = 0;
        playHead = 0;
        rotateAmt = 0;
        left = 0;
        right = width;
        bottom = 0;
        top = height;
        ortho(left, right, bottom, top, -10000, 10000);
    }

    @Override
    public void draw() {
        background(color(200, 50, 30));
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        translate(0, yPos, 0);
        strokeWeight(10);
        line(left, playHead, -1000, right, playHead, -1000);

        pushMatrix();
        translate(width / 2, 4 * s, 0);
        rotateY((rotateAmt / (float) width) * (2 * PI));
        for (int z = 0; z < 3; z++) {
            noStroke();
            for (int i = 0; i < 7; i++) {
                for (int w = 0; w < 7; w++) {
                    mx[w] = w * 4 * s;
                    my[i] = i * 4 * s;
                    pushMatrix();
                    fill(color(30 + z * 50, 30 + z * 50, 30 + z * 50));
                    translate(mx[w], my[i], z * 4 * s);
                    sphere(s);
                    popMatrix();
                }
            }
            strokeWeight(5);
            stroke(30 + z * 50, 30 + z * 50, 30 + z * 50);
            for (int i = 0; i < 6; i++) {
                for (int w = 0; w < 6; w++) {
                    line(mx[w], my[i], z * 4 * s, mx[w + 1], my[i + 1], z * 4 * s);
                }
            }
        }
        popMatrix();
    }

    @Override
    public void mousePressed() {
        mouseStartX = mouseX;
        mouseStartY = mouseY;
        if (mouseButton == RIGHT) {
            playHead = (float) (mouseY - yPos) ;//* ((float) (top - bottom) / (float) height);
        }
    }

    @Override
    public void mouseDragged() {
        if (mouseButton == LEFT) {
            yPos -= (float) (mouseStartY - mouseY) ;//* ((float) (top - bottom) / (float) height);
            rotateAmt += (mouseStartX - mouseX);
        }
        if (mouseButton == RIGHT) {
            playHead -= (float) (mouseStartY - mouseY);// * ((float) (top - bottom) / (float) height);
        }
        mouseStartX = mouseX;
        mouseStartY = mouseY;
    }
//
//    @Override
//    public void mouseWheel(MouseEvent event) {
//        float e = event.getCount();
//        bottom -= e;
//        top += e;
//        left = (int) (bottom * aspect);
//        right = (int) (top * aspect);
//        ortho(left, right, bottom, top, -10000, 10000);
//    }

}
