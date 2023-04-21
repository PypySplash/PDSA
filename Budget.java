//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;

import java.util.ArrayList;
import java.util.List;

class Budget {
    public Budget() {}
    public int plan(int island, List<int[]> bridge) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(island);
        for (int[] ints : bridge) G.addEdge(new Edge(ints[0], ints[1], ints[2]));
        PrimMST primMST = new PrimMST(G);
        return (int) primMST.weight();
    }

    public static void main(String[] args) {
        Budget solution = new Budget();
        System.out.println(solution.plan(4, new ArrayList<int[]>() {{
            add(new int[]{0, 1, 2});
            add(new int[]{0, 2, 4});
            add(new int[]{1, 3, 5});
            add(new int[]{2, 1, 1});
        }}));
        System.out.println(solution.plan(4, new ArrayList<int[]>() {{
            add(new int[]{0, 1, 0});
            add(new int[]{0, 2, 4});
            add(new int[]{0, 3, 4});
            add(new int[]{1, 2, 1});
            add(new int[]{1, 3, 4});
            add(new int[]{2, 3, 2});
        }}));
    }

//    public static void main(String[] args){
//        Budget sol = new Budget();
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for(Object CaseInList : all){
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray arg_hou = (JSONArray) person.get("distance");
//                    long l = (Long) person.get("answer");
//                    int Answer = (int) l;
//                    long ll = (Long) person.get("landmarks");
//                    int land = (int) ll;
//                    ArrayList<int[]> HOU = new ArrayList<int[]>();
//                    int Answer_W = 0;
//                    for(int i=0;i<arg_hou.size();i++){
//                        String spl = arg_hou.get(i).toString();
//                        String fir = "";
//                        String sec = "";
//                        String thi = "";
//                        String[] three;
//                        three = spl.split(",");
//                        fir = three[0].replace("[","");
//                        sec = three[1];
//                        thi = three[2].replace("]","");
//                        int[] hou = new int[3];
//                        hou[0] = Integer.parseInt(fir);
//                        hou[1] = Integer.parseInt(sec);
//                        hou[2] = Integer.parseInt(thi);
//                        HOU.add(hou);
//                    }
//                    Answer_W = sol.plan(land, HOU);
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
