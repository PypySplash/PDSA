//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

import java.util.*;
import edu.princeton.cs.algs4.ClosestPair;
import edu.princeton.cs.algs4.Point2D;

class ClusterTest {
    public List<double[]> cluster(List<int[]> points, int cluster_num) {

        // 把點放進Point2D array，因為要用內建class：ClosestPair
        Point2D[] point2D_array = new Point2D[points.size()];
        for (int i = 0; i < points.size(); i++)
            point2D_array[i] = new Point2D(points.get(i)[0], points.get(i)[1]);
        // step 0: Treat each point as a cluster
        // 每個點初始都是自己一個Cluster，用Map存Cluster及對應的合併的點（詳見Notability)
//        HashMap<Point2D, ArrayList<Point2D>> Cluster = new HashMap<>();
//        for (Point2D point : point2D_array)
//            Cluster.put(point, new ArrayList<>() {{
//                add(point);
//            }});
        HashMap<Point2D, Integer> Cluster = new HashMap<>();
        int index = 1;
        for (Point2D point : point2D_array)
            Cluster.put(point, index);

        int K = point2D_array.length;
        while (K > cluster_num) {
            // step 1: find the nearest pair of clusters (c1, c2) among the remaining K clusters (initially K = N)
            ClosestPair closestPair = new ClosestPair(point2D_array);
            // Step 5: Re-calculate the distance of the new cluster, c3, to the other remaining clusters;
            Point2D[] temp1 = new Point2D[point2D_array.length - 1];
            for (int i = 0, j = 0; i < point2D_array.length; i++)
                if (point2D_array[i] != closestPair.either() && point2D_array[i] != closestPair.other())
                    temp1[j++] = point2D_array[i];
            double xSum = closestPair.either().x() * Cluster.get(closestPair.either()) + closestPair.other().x() * Cluster.get(closestPair.other());
            double ySum = closestPair.either().y() * Cluster.get(closestPair.either()) + closestPair.other().y() * Cluster.get(closestPair.other());
            int weight = Cluster.get(closestPair.either()) + Cluster.get(closestPair.other());
            double xCentroid = xSum / weight;
            double yCentroid = ySum / weight;
            temp1[point2D_array.length-2] = new Point2D(xCentroid, yCentroid);
            point2D_array = temp1;


//            double xSum = 0;
//            double ySum = 0;

//            for (Point2D point : Cluster.get(closestPair.either())) {
//                xSum += point.x();
//                ySum += point.y();
//            }
//            for (Point2D point : Cluster.get(closestPair.other())) {
//                xSum += point.x();
//                ySum += point.y();
//            }

            // Step 3: Delete the two old clusters: c1 and c2;
//            Point2D[] temp2 = new Point2D[point2D_array.length + 1];
//            System.arraycopy(point2D_array, 0, temp2, 0, point2D_array.length);
//            temp2[point2D_array.length] = new Point2D(xCentroid, yCentroid);
//            point2D_array = temp2;
            // step 2: Create new cluster c3，其位置為c1&c2合併後之質心

//            ArrayList<Point2D> newCluster = new ArrayList<>();
//            for (Point2D point : Cluster.get(closestPair.either())) {
//                newCluster.add(point);
//                Cluster.remove(point);
//            }
//            for (Point2D point : Cluster.get(closestPair.other())) {
//                newCluster.add(point);
//                Cluster.remove(point);
//            }
            int new_index = Cluster.get(closestPair.either()) + Cluster.get(closestPair.other());
            Cluster.remove(closestPair.either());
            Cluster.remove(closestPair.other());
            Cluster.put(new Point2D(xCentroid, yCentroid), new_index);
            K -= 1;  // Step 4: K = K - 1
            // Step 6 (while loop): go to Step 1 until K = cluster_num
        }
        Arrays.sort(point2D_array, Point2D.Y_ORDER);
        Arrays.sort(point2D_array, Point2D.X_ORDER);
        ArrayList<double[]> ans = new ArrayList<>();
        for (Point2D point : point2D_array) {
            double[] temp3 = new double[2];
            temp3[0] = point.x();
            temp3[1] = point.y();
            ans.add(temp3);
        }
        return ans;
    }

    public static void main(String[] args) {
        List<double[]> out = new ClusterTest().cluster(new ArrayList<>() {{
            add(new int[]{0, 1});
            add(new int[]{0, 2});
            add(new int[]{3, 1});
            add(new int[]{3, 2});
        }}, 2);
        for (double[] o : out) System.out.println(Arrays.toString(o));
    }

//    public static void main(String[] args){
//        Cluster sol = new Cluster();
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for(Object CaseInList : all){
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0,ac = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray point = (JSONArray) person.get("points");
//                    Long clusterNumber = (Long) person.get("cluster_num");
//                    JSONArray arg_ans = (JSONArray) person.get("answer");
//                    int points_x[] = new int[point.size()];
//                    int points_y[] = new int[point.size()];
//                    double Answer_x[] = new double[arg_ans.size()];
//                    double Answer_y[] = new double[arg_ans.size()];
//                    List<double[]> ansClus = new ArrayList<double[]>();
//                    ArrayList<int[]> pointList = new ArrayList<int[]>();
//                    for(int i=0;i<clusterNumber;i++){
//                        String ansStr = arg_ans.get(i).toString();
//                        ansStr = ansStr.replace("[","");ansStr = ansStr.replace("]","");
//                        String[] parts = ansStr.split(",");
//                        Answer_x[i] = Double.parseDouble(parts[0]);
//                        Answer_y[i] = Double.parseDouble(parts[1]);
//                    }
//                    for(int i=0;i< point.size();i++){
//                        String ansStr = point.get(i).toString();
//                        ansStr = ansStr.replace("[","");ansStr = ansStr.replace("]","");
//                        String[] parts = ansStr.split(",");
//                        pointList.add(new int[]{Integer.parseInt(parts[0]),Integer.parseInt(parts[1])});
//                    }
//                    ansClus = sol.cluster(pointList,Integer.parseInt(clusterNumber.toString()));
//                    if(ansClus.size()!=clusterNumber){
//                        wa++;
//                        System.out.println(q_cnt+": WA");
//                        break;
//                    } else{
//                        for(int i=0;i<clusterNumber;i++){
//                            if(ansClus.get(i)[0]!=Answer_x[i] || ansClus.get(i)[1]!=Answer_y[i]){
//                                wa++;
//                                System.out.println(q_cnt+": WA");
//                                break;
//                            }
//                        }
//                        System.out.println(q_cnt+": AC");
//                    }
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
