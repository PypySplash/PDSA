import java.util.*;
import edu.princeton.cs.algs4.ClosestPair;
import edu.princeton.cs.algs4.Point2D;

class Cluster {
    public List<double[]> cluster(List<int[]> points, int cluster_num) {
        // step 0: Treat each point as a cluster
        // step 1: find the nearest pair of clusters (c1, c2) among the remaining K clusters (initially K = N)
        // step 2: Create new cluster c3，其位置為c1&c2合併後之質心
        // Step 3: Delete the two old clusters: c1 and c2;
        // Step 4: K = K - 1
        // Step 5: Re-calculate the distance of the new cluster, c3, to the other remaining clusters;
        // Step 6 (while loop): go to Step 1 until K = cluster_num

        // Method 1: ClosestPair
        // 把點放進Point2D array，因為要用內建class：ClosestPair
        Point2D[] point2D_array = new Point2D[points.size()];
        for (int i = 0; i < points.size(); i++)
            point2D_array[i] = new Point2D(points.get(i)[0], points.get(i)[1]);

        // 每個點初始都是自己一個Cluster，用Map存Cluster及對應的合併數（詳見Notability)
        HashMap<Point2D, Integer> Cluster = new HashMap<>();
        int mergeNumber = 1;
        for (Point2D point : point2D_array) Cluster.put(point, mergeNumber);

        int K = point2D_array.length;
        while (K > cluster_num){  // 只要K還>所求最小cluster數，就繼續跑
            ClosestPair closestPair = new ClosestPair(point2D_array);  // ClosestPair

            // 移除最近的兩個點 & 加上質心：創一個新的Point2D array (array大小不能刪減）， 再使原本的point2D array指回去
            Point2D[] temp1 = new Point2D[point2D_array.length - 1];
            for (int i = 0, j = 0; i < point2D_array.length; i++)
                if (point2D_array[i] != closestPair.either() && point2D_array[i] != closestPair.other())
                    temp1[j++] = point2D_array[i];

            // 質心算法：either點的x權重 * x座標 + other點的x權重 * x座標
            double xSum = closestPair.either().x() * Cluster.get(closestPair.either()) + closestPair.other().x() * Cluster.get(closestPair.other());
            double ySum = closestPair.either().y() * Cluster.get(closestPair.either()) + closestPair.other().y() * Cluster.get(closestPair.other());
            int weight = Cluster.get(closestPair.either()) + Cluster.get(closestPair.other());
            double xCentroid = xSum / weight;
            double yCentroid = ySum / weight;
            temp1[point2D_array.length - 2] = new Point2D(xCentroid, yCentroid);  // 把質心加到Point2D array最後面
            point2D_array = temp1;

            // 把新的點（質心）加進Cluster，他Hashmap對應到的值為merging number(either的merging number + other的merging number），同時把either點跟other點移除
            int newMergeNumber = Cluster.get(closestPair.either()) + Cluster.get(closestPair.other());
            Cluster.remove(closestPair.either());
            Cluster.remove(closestPair.other());
            Cluster.put(new Point2D(xCentroid, yCentroid), newMergeNumber);
            K--;
        }
        Arrays.sort(point2D_array, Point2D.Y_ORDER);
        Arrays.sort(point2D_array, Point2D.X_ORDER);
        // 最後把Point2D array轉成ArrayList後輸出
        ArrayList<double[]> ans = new ArrayList<>();
        for (Point2D point : point2D_array){
            double[] temp2 = new double[2];
            temp2[0] = point.x();
            temp2[1] = point.y();
            ans.add(temp2);
        }
        return ans;
    }

//    public static void main(String[] args) {
//        List<double[]> out = new Cluster().cluster(new ArrayList<>(){{
//            add(new int[]{0,1});
//            add(new int[]{0,2});
//            add(new int[]{3,1});
//            add(new int[]{3,2});
//        }}, 2);
//        for (double[] o : out) System.out.println(Arrays.toString(o));
//    }

}
