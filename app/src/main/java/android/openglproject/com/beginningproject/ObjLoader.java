package android.openglproject.com.beginningproject;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ObjLoader {
    /*
    public final Point3[] Vertex = null;
    public final Point2[] TextureCoor = null;
    public final Point3[] Normal = null;
    */

    public final float[] VertexCoordinate;


    public ObjLoader(Context context, String file) {

        ArrayList<Point3> vertex = new ArrayList<Point3>();
        ArrayList<Point3> textureCoor = new ArrayList<Point3>();
        ArrayList<Point3> normal = new ArrayList<Point3>();

        ArrayList<Face> listFace = new ArrayList<Face>();

        ArrayList<Float> listVertex = new ArrayList<Float>();

        BufferedReader reader = null;

        try{
            InputStreamReader in = new InputStreamReader(context.getAssets().open(file));
            reader = new BufferedReader(in);

            while (reader.ready()){
                String line = reader.readLine();
                if (line == null)
                    break;

                StringTokenizer tok = new StringTokenizer(line);
                String cmd = tok.nextToken();

                if (cmd.equals("v")){
                    vertex.add(readPointInLine(tok));
                }
                else if (cmd.equals("vt")){
                    textureCoor.add(readPointInLine(tok));
                }
                else if (cmd.equals("vn")){
                    normal.add(readPointInLine(tok));
                }
                else if (cmd.equals("f")){

                    if (tok.countTokens() > 3){
                        Face face1 = new Face(3);
                        Face face2 = new Face(3);
                        int i = 0;

                        while (tok.hasMoreTokens()) {
                            StringTokenizer face_tok = new StringTokenizer(tok.nextToken(), "/");

                            int v_idx = -1;
                            int vt_idx = -1;
                            int vn_idx = -1;
                            v_idx = Integer.parseInt(face_tok.nextToken());
                            if (face_tok.hasMoreTokens())
                                vt_idx = Integer.parseInt(face_tok.nextToken());
                            if (face_tok.hasMoreTokens())
                                vn_idx = Integer.parseInt(face_tok.nextToken());

                            //Log.v("objmodel", "face: "+v_idx+"/"+vt_idx+"/"+vn_idx);

                            face1.addVertex(
                                    vertex.get(v_idx - 1),
                                    vt_idx == -1 ? null : textureCoor.get(vt_idx - 1),
                                    vn_idx == -1 ? null : normal.get(vn_idx - 1)
                            );

                            if (i != 1){
                                face2.addVertex(
                                        vertex.get(v_idx - 1),
                                        vt_idx == -1 ? null : textureCoor.get(vt_idx - 1),
                                        vn_idx == -1 ? null : normal.get(vn_idx - 1)
                                );
                            }
                            i++;
                        }
                        listFace.add(face1);
                        listFace.add(face2);
                    }
                    else {
                        Face face = new Face(3);
                        while (tok.hasMoreTokens()) {
                            StringTokenizer face_tok = new StringTokenizer(tok.nextToken(), "/");

                            int v_idx = -1;
                            int vt_idx = -1;
                            int vn_idx = -1;
                            v_idx = Integer.parseInt(face_tok.nextToken());
                            if (face_tok.hasMoreTokens())
                                vt_idx = Integer.parseInt(face_tok.nextToken());
                            if (face_tok.hasMoreTokens())
                                vn_idx = Integer.parseInt(face_tok.nextToken());

                            //Log.v("objmodel", "face: "+v_idx+"/"+vt_idx+"/"+vn_idx);

                            face.addVertex(
                                    vertex.get(v_idx - 1),
                                    vt_idx == -1 ? null : textureCoor.get(vt_idx - 1),
                                    vn_idx == -1 ? null : normal.get(vn_idx - 1)
                            );
                        }

                        listFace.add(face);
                    }
                }
            }

        }catch (IOException ex) {
            Log.e("Load File : ", "FAILED." + ex);
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //log the exception
                }
            }
        }

        for (Face face : listFace){
            face.returnFloatVertex(listVertex);
        }

        Log.v("NUM OF TRIANGLE :", String.valueOf(listFace.size()));


        VertexCoordinate = new float[listVertex.size()];
        int i = 0;
        for (Float f : listVertex) {
            VertexCoordinate[i++] = (f != null ? f : Float.NaN);
        }

        Log.v("NUM OF VERTEX :", String.valueOf(VertexCoordinate.length));
    }


    private static class Face{
        Point3 v[];
        Point3 vt[];
        Point3 vn[];
        int size;
        int count;


        public Face(int size) {
            this.size = size;
            this.count = 0;

            this.v = new Point3[size];
            this.vt = new Point3[size];
            this.vn = new Point3[size];
        }


        public boolean addVertex(Point3 v, Point3 vt, Point3 vn) {
            if (count >= size)
                return false;
            this.v[count] = v;
            this.vt[count] = vt;
            this.vn[count] = vn;
            count++;
            return true;
        }

        public void returnFloatVertex(ArrayList<Float> vertex){
            for (int i = 0 ; i < size ; i++){

                // vertex coor
                vertex.add(this.v[i].x);
                vertex.add(this.v[i].y);
                vertex.add(this.v[i].z);

                // texture coor
                vertex.add(this.vt[i].x);
                vertex.add(this.vt[i].y);

                // normal coor
                vertex.add(this.vn[i].x);
                vertex.add(this.vn[i].y);
                vertex.add(this.vn[i].z);
            }
        }

        @Override
        public String toString() {
            return "Face{" +
                    "v=" + Arrays.toString(v) +
                    ", vt=" + Arrays.toString(vt) +
                    ", vn=" + Arrays.toString(vn) +
                    '}';
        }
    }

    private static Point3 readPointInLine(StringTokenizer tok){
        Point3 ret = new Point3();
        if (tok.hasMoreTokens()){
            ret.x = Float.parseFloat(tok.nextToken());

            if (tok.hasMoreTokens()){
                ret.y = Float.parseFloat(tok.nextToken());

                if (tok.hasMoreTokens()){
                    ret.z = Float.parseFloat(tok.nextToken());
                }
            }
        }
        return ret;
    }
}
