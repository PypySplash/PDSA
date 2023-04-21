//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

class Flood {
    public Flood() {}
    //return which village is the latest one flooded
    public int village(int villages, List<int[]> road) {
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(villages);
        for (int[] roads : road) edgeWeightedDigraph.addEdge(new DirectedEdge(roads[0], roads[1], roads[2]));
        DijkstraSP dijkstraSP = new DijkstraSP(edgeWeightedDigraph, 0);
//        AcyclicSP acyclicSP = new AcyclicSP(edgeWeightedDigraph, 0);  // 沒有acyclic不能用
        int lastOne = 0;
        double maxDistance = 0;
        for (int i = 0; i < villages; i++) {  // 有些地方0走不到，初始值為無限大，故需多加個假設條件。
            if (dijkstraSP.distTo(i) != Double.POSITIVE_INFINITY && dijkstraSP.distTo(i) > maxDistance) {
                maxDistance = dijkstraSP.distTo(i);
                lastOne = i;
            }
        }
        return lastOne;
    }

//    public static void main(String[] args) {
//        //test t = new test(args);
//        Flood solution = new Flood();
//        System.out.println(solution.village(4, new ArrayList<int[]>(){{
//            add(new int[]{0,1,3});
//            add(new int[]{0,2,6});
//            add(new int[]{1,2,2});
//            add(new int[]{2,1,3});
//            add(new int[]{2,3,3});
//            add(new int[]{3,1,1});
//        }}));
//        System.out.println(solution.village(6, new ArrayList<int[]>(){{
//            add(new int[]{0,1,5});
//            add(new int[]{0,4,3});
//            add(new int[]{1,2,1});
//            add(new int[]{1,3,3});
//            add(new int[]{1,5,2});
//            add(new int[]{2,3,4});
//            add(new int[]{3,2,1});
//            add(new int[]{4,0,2});
//            add(new int[]{4,1,4});
//            add(new int[]{5,0,3});
//        }}));
//    }

//    public static void main(String[] args){
//        Flood sol = new Flood();
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for(Object CaseInList : all){
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray arg_hou = (JSONArray) person.get("road");
//                    long l = (Long) person.get("answer");
//                    int Answer = (int) l;
//                    long ll = (Long) person.get("villages");
//                    int land = (int) ll;
//                    ArrayList<int[]> HOU = new ArrayList<int[]>();
//                    int Answer_W = 0;
//                    for (Object value : arg_hou) {
//                        String spl = value.toString();
//                        String fir = "";
//                        String sec = "";
//                        String thi = "";
//                        String[] three;
//                        three = spl.split(",");
//                        fir = three[0].replace("[", "");
//                        sec = three[1];
//                        thi = three[2].replace("]", "");
//                        int[] hou = new int[3];
//                        hou[0] = Integer.parseInt(fir);
//                        hou[1] = Integer.parseInt(sec);
//                        hou[2] = Integer.parseInt(thi);
//                        HOU.add(hou);
//                    }
//                    Answer_W = sol.village(land, HOU);
//                    if(Answer_W == Answer){
//                        System.out.println(q_cnt+": AC");
//                    }
//                    else {
//                        wa++;
//                        System.out.println(q_cnt+": WA");
//                        System.out.println("your answer : "+Answer_W);
//                        System.out.println("true answer : "+Answer);
//                    }
//
//                }
//                System.out.println("Score: "+(q_cnt-wa)+"/"+q_cnt);
//
//            }
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
