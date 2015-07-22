package edu.unl.cse.knorth.git_sonification.display.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.event.MouseEvent;
import javax.sound.sampled.*;
import org.joda.time.DateTime;

public class VisMain extends PApplet {

    int num = 49;
    float mx[] = new float[num];
    float my[] = new float[num];

    float mouseStartX;
    float mouseStartY;

    float aspect;
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

    double time;
    double oldTime;
    double delta;
    
    int oldCom;

    Clip clip;
    Clip dev1;
    Clip dev2;
    Clip dev3;
    Clip dev4;
    Clip dev5;
    Clip dev6;
    Clip dev7;
    Clip dev8;
    Clip dev9;
    Clip dev10;
    Clip dev11;
    Clip dev12;
    Clip dev13;
    Clip dev14;
    Clip daySep;
    Clip conflict;
    Clip cd1;
    Clip cd2;
    Clip cd3;
    Clip cd4;

    Map<String, Integer> map;
    int count;

    List<Line> lines;
    List<Row> rows;
    List<Layer> layers;
    VisualizationData visDat;

    int numRows;

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
        frameRate(30);
        fill(200);
        aspect = (float) width / (float) height;
        s = 30;
        s4 = 4 * s;

        layers = new ArrayList<>();
        rows = new ArrayList<>();
        lines = new ArrayList<>();
        lines.add(new Line(1, 1, true));
        rows.add(new Row("author0", new DateTime(), 1, true, 0, lines));
        rows.add(new Row("author1", new DateTime(), 1, true, 0, lines));
        rows.add(new Row("author2", new DateTime(), 1, true, 1, lines));
        rows.add(new Row(new DateTime(), 1, lines));
        rows.add(new Row("author3", new DateTime(), 1, true, 0, lines));
        layers.add(new Layer(rows));
        layers.add(new Layer(rows));
        visDat = new VisualizationData(layers, 1, 1);

        numRows = visDat.getLayers().get(0).getRows().size();

        playSpeed = (s4 * 25) / (60);
        playHead = height;
        try {
            dev1 = AudioSystem.getClip();
            dev1.open(AudioSystem.getAudioInputStream(new File("audio/dev1.wav")));
            dev2 = AudioSystem.getClip();
            dev2.open(AudioSystem.getAudioInputStream(new File("audio/dev2.wav")));
            dev3 = AudioSystem.getClip();
            dev3.open(AudioSystem.getAudioInputStream(new File("audio/dev3.wav")));
            dev4 = AudioSystem.getClip();
            dev4.open(AudioSystem.getAudioInputStream(new File("audio/dev4.wav")));
            dev5 = AudioSystem.getClip();
            dev5.open(AudioSystem.getAudioInputStream(new File("audio/dev5.wav")));
            dev6 = AudioSystem.getClip();
            dev6.open(AudioSystem.getAudioInputStream(new File("audio/dev6.wav")));
            daySep = AudioSystem.getClip();
            daySep.open(AudioSystem.getAudioInputStream(new File("audio/day_separator.wav")));
            cd1 = AudioSystem.getClip();
            cd1.open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_1.wav")));
            cd2 = AudioSystem.getClip();
            cd2.open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_2.wav")));
            cd3 = AudioSystem.getClip();
            cd3.open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_3.wav")));
            cd4 = AudioSystem.getClip();
            cd4.open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_4.wav")));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(VisMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        clip = dev1;
        conflict = cd1;

        yPos = 0;
        rotateAmt = 0;

        left = 0;
        right = width;
        bottom = 0;
        top = height;

        oldTime = time;
        time = System.currentTimeMillis();
        delta = 0;

        map = new HashMap<>();
        count = 1;

        ortho(left, right, bottom, top, -10000, 10000);
    }

    @Override
    public void draw() {
        oldTime = time;
        time = System.currentTimeMillis();
        delta = (time - oldTime) * 0.001f;
        if (keyPressed && key == CODED) {
            if (keyCode == UP) {
                playHead -= playSpeed * delta;
            }
            if (keyCode == DOWN) {
                playHead += playSpeed * delta;
            }
        }

        background(color(200, 50, 30));
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        translate(0, yPos, 0); //sets everything to be relitive to yPos

        //this is the playhead for the music
        strokeWeight(10);
        line(left, playHead, -1000, right, playHead, -1000);

        if ((playHead / s4) > -0.25 && (playHead / s4) < (numRows - 0.75) && ((playHead / s4) > ((round((playHead / s4))) - 0.25)) && ((playHead / s4) < ((round((playHead / s4))) + 0.25))) {
            if (((int) ((playHead / s4) + 0.25) != oldCom) || (clip.getFrameLength() <= clip.getFramePosition())) {
                clip.setFramePosition(0);
                clip.stop();
                conflict.setFramePosition(0);
                conflict.stop();
            }
            oldCom = (int) ((playHead / s4) + 0.25);
            String author = null;
            int numCon = 0;
            try {
                Row row = visDat.getLayers().get(0).getRows().get(oldCom);
                author = row.getAuthor();
                numCon = row.getNumConflicts();
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("index out of bounds exception");
            }
            if ((clip == null) || !clip.isRunning()) {
                if (author != null) {
                    Integer n = map.get(author);
                    if (n == null) {
                        map.put(author, count);
                        n = count;
                        count++;
                    }
                    switch (n) {
                        case 1:
                            clip = dev1;
                            break;
                        case 2:
                            clip = dev2;
                            break;
                        case 3:
                            clip = dev3;
                            break;
                        case 4:
                            clip = dev4;
                            break;
                        case 5:
                            clip = dev5;
                            break;
                        case 6:
                            clip = dev6;
                            break;
                        default:
                            clip = dev1;
                            break;
                    }
                } else {
                    clip = daySep;
                }
                clip.start();
            }
            if ((conflict == null) || !conflict.isRunning()) {
                if (numCon > 0) {
                    switch (numCon) {
                        case 1:
                            conflict = cd1;
                            break;
                        case 2:
                            conflict = cd2;
                            break;
                        case 3:
                            conflict = cd3;
                            break;
                        case 4:
                            conflict = cd4;
                            break;
                        default:
                            conflict = cd1;
                            break;
                    }
                    conflict.start();
                }
            }
        }

//        if((((int)((playHead / s4) + 0.25))&1)==0){
//            if (dev1.getFrameLength() == dev1.getFramePosition()){
//                dev1.setFramePosition(0);
//                dev1.stop();
//            }
//            dev1.start();
//        }
        pushMatrix();
        translate(width / 2, 0, 0); //centers the vis and starts it a unit down
        rotateY((rotateAmt / (float) width) * (2 * PI));

        int numColors = visDat.getLayers().size();
        int z = 0;
        for (Layer layer : visDat.getLayers()) {
            int y = 0;
            for (Row row : layer.getRows()) {
                noStroke();
                if (row.isVisable()) {
                    pushMatrix();
                    fill(color(z * 255 / numColors, z * 255 / numColors, z * 255 / numColors));
                    translate(row.getBranchLocation() * s4, y * s4, z * s4);
                    sphere(s);
                    popMatrix();
                }
                strokeWeight(5);
                stroke(z * 255 / numColors, z * 255 / numColors, z * 255 / numColors);
                for (Line line : row.getIncomingLines()) {
                    if (line.isVisible) {
                        line(line.fromBranch * s4, y * s4, z * s4, line.toBranch * s4, (y * s4) + s4, z * s4);
                    }
                }
                y++;
            }
            z++;
        }
//        for (int z = 0; z < 3; z++) {
//            noStroke();
//            for (int i = 0; i < 7; i++) {
//                for (int w = 0; w < 7; w++) {
//                    mx[w] = w * s4;
//                    my[i] = i * s4;
//                    pushMatrix();
//                    fill(color(30 + z * 50, 30 + z * 50, 30 + z * 50));
//                    translate(mx[w], my[i], z * s4);
//                    sphere(s);
//                    popMatrix();
//                }
//            }
//            strokeWeight(5);
//            stroke(30 + z * 50, 30 + z * 50, 30 + z * 50);
//            for (int i = 0; i < 6; i++) {
//                for (int w = 0; w < 6; w++) {
//                    line(mx[w], my[i], z * s4, mx[w + 1], my[i + 1], z * s4);
//                }
//            }
//        }

        popMatrix();
//        System.out.println("playHead = " + (playHead / s4));
//        System.out.println("playHead- = " + (((int)((playHead / s4)+0.25)) - 0.25));
//        System.out.println("playHead+ = " + (((int)((playHead / s4)+0.25)) + 0.25));
//        System.out.println("framerate = " + frameRate);
//        System.out.println("delta time = " + delta);
//        System.out.println("lenght = " + dev1.getFrameLength());
//        System.out.println("pos = " + dev1.getFramePosition());
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
