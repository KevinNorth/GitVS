package edu.unl.cse.knorth.git_sonification.display.view;

import edu.unl.cse.knorth.git_sonification.GitDataProcessor;
import edu.unl.cse.knorth.git_sonification.display.model.ViewModel;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Row;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Line;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.Layer;
import edu.unl.cse.knorth.git_sonification.display.model.visualization.VisualizationData;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.joda.time.DateTime;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class VisMain extends PApplet {

    float mouseStartX;
    float mouseStartY;

    float aspect;
    float s;
    float s4;

    float yPos;
    float playHead;
    float playSpeed;
    float rotateAmt;
    float startPos;

    int left;
    int right;
    int bottom;
    int top;

    double time;
    double oldTime;
    double delta;

    int oldCom;

    Clip clip;
    Clip[] dev = new Clip[14];
    Clip daySep;
    Clip conflict;
    Clip[] cd = new Clip[4];

    Map<String, Integer> map;
    int count;

//    List<Line> lines;
//    List<Row> rows;
//    List<Layer> layers;
    VisualizationData visDat;

    int numRows;
    int numLayers;
    float viewDist;

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

        DateTime since = new DateTime(2009, 11, 4, 0, 0);
        DateTime until = new DateTime(2009, 11, 9, 0, 0);
        try {
            visDat = new GitDataProcessor().processGitData("../../voldemort/.git", since, until).getVisualizationData();
        } catch (IOException ex) {
            Logger.getLogger(VisMain.class.getName()).log(Level.SEVERE, null, ex);
        }
//        layers = new ArrayList<>();
//        rows = new ArrayList<>();
//        lines = new ArrayList<>();
//        lines.add(new Line(1, 1, true));
//        lines.add(new Line(1, 2, true));
//        lines.add(new Line(2, 2, true));
//        for (int i = 0; i < 100; i++) {
//            rows.add(new Row("author0", new DateTime(), 1, true, 0, lines));
//            rows.add(new Row("author1", new DateTime(), 1, true, 0, lines));
//        }
//        rows.add(new Row("author2", new DateTime(), 1, true, 1, lines));
//        rows.add(new Row(new DateTime(), 1, lines));
//        rows.add(new Row("author3", new DateTime(), 1, true, 0, lines));
//        for (int i = 0; i < 50; i++) {
//            layers.add(new Layer(rows));
//        }
//        visDat = new VisualizationData(layers, 1, 1);

        numLayers = visDat.getLayers().size();
        numRows = visDat.getLayers().get(0).getRows().size();

        playSpeed = (s4 * 25) / (60);
        playHead = height;
        try {
            for (int i = 0; i < 14; i++) {
                dev[i] = AudioSystem.getClip();
                dev[i].open(AudioSystem.getAudioInputStream(new File("audio/dev" + (i + 1) + ".wav")));
            }
            daySep = AudioSystem.getClip();
            daySep.open(AudioSystem.getAudioInputStream(new File("audio/day_separator.wav")));
            for (int i = 0; i < 4; i++) {
                cd[i] = AudioSystem.getClip();
                cd[i].open(AudioSystem.getAudioInputStream(new File("audio/conflict_drums_" + (i + 1) + ".wav")));
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(VisMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        clip = dev[0];
        conflict = cd[0];

        yPos = 0;
        rotateAmt = 0;

        bottom = -20;
        top = height + 20;
        left = (int) (bottom * aspect);
        right = (int) (top * aspect);

        oldTime = time;
        time = System.currentTimeMillis();
        delta = 0;

        map = new HashMap<>();
        count = 0;

        startPos = (((int) (height / s4)) * s4) - (numRows * s4) - s4;
        viewDist = 20000;

        ortho(left, right, bottom, top, -viewDist, viewDist);
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

        background(color(100, 100, 250));
        //directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(240, 240, 240);
        pointLight(50, 50, 50, (left + right) / 2, (top + bottom), 200);

        translate(0, yPos, 0); //sets everything to be relitive to yPos

        //this is the playhead for the music
        strokeWeight(10);
        stroke(250, 250, 250);
        line(left, playHead, -(viewDist / 2), right, playHead, -(viewDist / 2));

        float playHeadValue = playHead - startPos - s4;

        if ((playHeadValue / s4) > -0.25 && (playHeadValue / s4) < (numRows - 0.75) && ((playHeadValue / s4) > ((round((playHeadValue / s4))) - 0.25)) && ((playHeadValue / s4) < ((round((playHeadValue / s4))) + 0.25))) {
            if (((int) ((playHeadValue / s4) + 0.25) != oldCom) || (clip.getFrameLength() <= clip.getFramePosition())) {
                clip.setFramePosition(0);
                clip.stop();
                conflict.setFramePosition(0);
                conflict.stop();
            }
            oldCom = (int) ((playHeadValue / s4) + 0.25);
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
                    if (n < 14) {
                        clip = dev[n];
                    }
                } else {
                    clip = daySep;
                }
                clip.start();
            }
            if ((conflict == null) || !conflict.isRunning()) {
                if (numCon > 0 && numCon < 5) {
                    conflict = cd[numCon - 1];
                    conflict.start();
                }
            }
        }

        pushMatrix();
        translate((left + right) / 2, startPos, ((left + right)) / 2); //centers the vis and starts it a unit up
        rotateY(((rotateAmt / (float) width) * (2 * PI)) - (PI / 100));

        int z = 0;
        for (Layer layer : visDat.getLayers()) {
            int y = 0;
            for (Row row : layer.getRows()) {
                if (y*s4+yPos+startPos<top+s4 && y*s4+yPos+startPos>bottom-s4) {
                    noStroke();
                    if (row.isVisable()) {
                        pushMatrix();
                        fill(color(Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(3)) * ((z * 200 / numLayers) + 50), Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(2)) * ((z * 200 / numLayers) + 50), Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(1)) * ((z * 200 / numLayers) + 50)));
                        translate(row.getBranchLocation() * s4, y * s4 + s4, s4 * (z - (numLayers / 2)));
                        sphere(s);
                        popMatrix();
                    }
                    strokeWeight(10 * height / (top - bottom));
                    stroke(Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(3)) * ((z * 200 / numLayers) + 50), Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(2)) * ((z * 200 / numLayers) + 50), Character.getNumericValue(Integer.toBinaryString((z % 8) + 8).charAt(1)) * ((z * 200 / numLayers) + 50));
                    for (Line line : row.getIncomingLines()) {
                        if (line.isVisible) {
                            line(line.fromBranch * s4, y * s4, s4 * (z - (numLayers / 2)), line.toBranch * s4, (y * s4) + s4, s4 * (z - (numLayers / 2)));
                        }
                    }
                }
                y++;
            }
            z++;
        }

        popMatrix();
//        System.out.println("playHead = " + (playHead / s4));
//        System.out.println("playHead- = " + (((int)((playHead / s4)+0.25)) - 0.25));
//        System.out.println("playHead+ = " + (((int)((playHead / s4)+0.25)) + 0.25));
//        System.out.println("framerate = " + frameRate);
//        System.out.println("delta time = " + delta);
//        System.out.println("lenght = " + dev1.getFrameLength());
//        System.out.println("pos = " + dev1.getFramePosition());
//        System.out.println("top = " + top);
//        System.out.println("bot = " + bottom);
//        System.out.println("yPos = " + yPos);
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
            rotateAmt += (mouseStartX - mouseX) / 5;
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
        ortho(left, right, bottom, top, -viewDist, viewDist);
    }

}
