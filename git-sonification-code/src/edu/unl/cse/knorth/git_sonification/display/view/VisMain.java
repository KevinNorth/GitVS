package edu.unl.cse.knorth.git_sonification.display.view;

import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.RowType;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.RowDateComparator;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class VisMain extends PApplet {

    int num = 49;
    float mx[] = new float[num];
    float my[] = new float[num];

    float mouseStartX;
    float mouseStartY;

    float aspect;
    float fps;
    float s;
    float s4;
    
    float yPos;
    float playHead;
    float playSpeed;
    float rotateAmt;

    int left;
    int right;
    int bottom;
    int top;

    VisualizationData visDat;

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
        fps = 30;
        frameRate(fps);
        fill(200);
        aspect = (float) width / (float) height;
        s = 30;
        s4 = 4*s;
        playSpeed = (s4*25)/(60*fps);
        playHead = 0;
        yPos = 0;
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

        translate(0, yPos, 0); //sets everything to be relitive to yPos
        
        //this is the playhead for the music
        strokeWeight(10);
        if(keyPressed){
            if(key == 'w'){
                playHead -= playSpeed;
            }else if(key == 's'){
                playHead += playSpeed;
            }
        }
        line(left, playHead, -1000, right, playHead, -1000);

        pushMatrix();
        translate(width / 2, s4, 0); //centers the vis and starts it a unit down
        rotateY((rotateAmt / (float) width) * (2 * PI));

//        int numColors = visDat.getLayers().size();
//        int z = 0;
//        for (Layer layer : visDat.getLayers()) {
//            z++;
//            int y = 0;
//            for (Row row : layer.getRows()) {
//                y++;
//                strokeWeight(5);
//                stroke(z*255/numColors,z*255/numColors,z*255/numColors);
//                for (Line line : row.getOutgoingLines()) {
//                    if (line.isVisible) {
//                        line(line.fromBranch* s4, y* s4, z* s4, line.toBranch* s4, y* s4, z* s4);
//                    }
//                }
//                noStroke();
//                if (row.isVisable()) {
//                    pushMatrix();
//                    fill(color(z*255/numColors,z*255/numColors,z*255/numColors));
//                    translate(row.getBranchLocation() * s4, y * s4, z * s4);
//                    sphere(s);
//                    popMatrix();
//                }
//            }
//        }

        for (int z = 0; z < 3; z++) {
            noStroke();
            for (int i = 0; i < 7; i++) {
                for (int w = 0; w < 7; w++) {
                    mx[w] = w * s4;
                    my[i] = i * s4;
                    pushMatrix();
                    fill(color(30 + z * 50, 30 + z * 50, 30 + z * 50));
                    translate(mx[w], my[i], z * s4);
                    sphere(s);
                    popMatrix();
                }
            }
            strokeWeight(5);
            stroke(30 + z * 50, 30 + z * 50, 30 + z * 50);
            for (int i = 0; i < 6; i++) {
                for (int w = 0; w < 6; w++) {
                    line(mx[w], my[i], z * s4, mx[w + 1], my[i + 1], z * s4);
                }
            }
        }
        
        popMatrix();
        System.out.println("playHead = " + ((playHead/s4)+0.25));
    }

    @Override
    public void mousePressed() {
        mouseStartX = mouseX;
        mouseStartY = mouseY;
        if (mouseButton == RIGHT) {
            playHead = ((((float) mouseY * (float) (top - bottom)) / (float) height) + bottom - yPos);
        }
    }

    @Override
    public void mouseDragged() {
        if (mouseButton == LEFT) {
            yPos -= (float) (mouseStartY - mouseY) * ((float) (top - bottom) / (float) height);
            rotateAmt += (mouseStartX - mouseX);
        }
        if (mouseButton == RIGHT) {
            playHead -= (float) (mouseStartY - mouseY) * ((float) (top - bottom) / (float) height);
        }
        mouseStartX = mouseX;
        mouseStartY = mouseY;
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        bottom -= e * 5;
        top += e * 5;
        left = (int) (bottom * aspect);
        right = (int) (top * aspect);
        ortho(left, right, bottom, top, -10000, 10000);
    }

}
