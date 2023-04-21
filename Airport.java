//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

import edu.princeton.cs.algs4.Point2D;
import java.util.*;

class Airport {  // Output's smallest average distance with optimal selection of airport location.
    public static Stack<Point2D> newCCW(Point2D start, Point2D[] array) {
        int N = array.length;

        Stack<Point2D> ccwStack = new Stack<>();
        ccwStack.push(start);  // 原點不存在整理過後的array裏面
        if (N == 0) return ccwStack;
        ccwStack.push(array[0]);  // Stack放整理過後的array第一點
        if (N == 1) return ccwStack;

        for (int i = 1; i < N + 1; i++) {  // index到N是因為ccw最後還要判斷回原點
            if (i == N) ccwStack.push(start);  // 當i=N的時候，再把原點push進來
            else ccwStack.push(array[i]);
            while (true) {
                Point2D tempThird = ccwStack.pop();  // first pop = third push
                Point2D tempSecond = ccwStack.pop();
                Point2D tempFirst = ccwStack.pop();  // third pop = first push
                int ccw = Point2D.ccw(tempFirst, tempSecond, tempThird);  // 用Point2D裡的ccw
                if (ccw == -1 || ccw == 0) {  // 若不是counterclockwise則去除中間點，再繼續往下做
                    ccwStack.push(tempFirst);
                    ccwStack.push(tempThird);
                } else {  // 若為counterclockwise則break
                    ccwStack.push(tempFirst);
                    ccwStack.push(tempSecond);
                    ccwStack.push(tempThird);
                    break;
                }
                if (ccwStack.size() == 2) break;  // 若STACK只剩下兩個點，則跳出迴圈
            }
        }
        return ccwStack;
    }
    public double airport(List<int[]> houses) {
        int N = houses.size();
        Point2D[] houses2D = new Point2D[N];  // 準備把houses放進point2D
        for (int i = 0; i < N; i++) {  // 放houses進Point 2D
            int[] temp = houses.get(i);
            Point2D temp2 = new Point2D(temp[0], temp[1]);
            houses2D[i] = temp2;
        }
//        System.out.println("houses2D: " + Arrays.toString(houses2D));
        Point2D origin = houses2D[0];  // 記錄原點
        int minY = 0;
        for (int i = 1; i < N; i++) {  // 原點已紀錄，故從i = 1開始
            if (houses2D[i].compareTo(houses2D[minY]) < 0) {  // compareTo < 0: this比that小，改寫
                minY = i;
                origin = houses2D[i];
            }
        }
//        System.out.println("origin: " + origin);
        Point2D[] houses_1 = new Point2D[N - 1];  // 存一個扣掉原點的array: houses - 1 house
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (houses2D[i] != origin) {
                houses_1[count] = houses2D[i];
                count += 1;
            }
        }
//        System.out.println("houses_1: " + Arrays.toString(houses_1));
        Comparator<Point2D> polarOrder = origin.polarOrder();
        Comparator<Point2D> distanceOrder = origin.distanceToOrder();
        Arrays.sort(houses_1, distanceOrder);  // ArrayList是houses_1.sort(distanceOrder());
        Arrays.sort(houses_1, polarOrder);  // ArrayList是houses_1.sort(polarOrder());
//        Arrays.sort(houses_1, distanceOrder);
//        System.out.println("houses_1: " + Arrays.toString(houses_1));
        Stack<Point2D> ccwStack = Airport.newCCW(origin, houses_1);
//        System.out.println("ccwStack: " + ccwStack);

        // ------------------------------接下來計算哪一條線當跑道會有最短距離---------------------------
        double minDistance = Double.MAX_VALUE;

        if (ccwStack.size() <= 3) minDistance = 0;
        Point2D candidateStart = ccwStack.pop();
        while (!ccwStack.isEmpty()) {
            Point2D candidatePoint1 = candidateStart;  // 第一點
            Point2D candidatePoint2 = ccwStack.pop();  // 第二點
            // 點到直線距離公式：Math.abs(ax0+by0+c) / Math.sqrt(a^2 + b^2)
            double xCoefficient;
            double yCoefficient;
            double constant;
            if (candidatePoint1.x() != candidatePoint2.x()) {  // 若不為垂直線
                // 斜率公式 = (y1-y0) / (x1-x0)
                double slope = (candidatePoint2.y() - candidatePoint1.y()) / (candidatePoint2.x() - candidatePoint1.x());
                // ax + by + c = 0
                xCoefficient = (-1) * slope;
                yCoefficient = 1;
                constant = slope * candidatePoint2.x() - candidatePoint2.y();
            } else {  // 若為垂直線
                xCoefficient = 1;
                yCoefficient = 0;
                constant = (-1) * candidatePoint2.x();
            }
            double totalDistance = 0;
            for (int i = 0; i < N; i++) {
                Point2D everyHouse = houses2D[i];
                double houseX = everyHouse.x();
                double houseY = everyHouse.y();
                // 點到直線的距離公式
                double distance = Math.abs(xCoefficient * houseX + yCoefficient * houseY + constant) / Math.sqrt(Math.pow(xCoefficient, 2) + Math.pow(yCoefficient, 2));
                totalDistance += distance;
            }
            double avgDistance = totalDistance / N;
            candidateStart = candidatePoint2;
            if (avgDistance < minDistance) minDistance = avgDistance;
        }
        return minDistance;
    }

//    public static void main(String[] args) {
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 0.0
//            add(new int[]{0,0});
//            add(new int[]{1,0});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 0.2357022603955159
//            add(new int[]{0,0});
//            add(new int[]{1,0});
//            add(new int[]{0,1});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 1.0
//            add(new int[]{0,0});
//            add(new int[]{2,0});
//            add(new int[]{0,2});
//            add(new int[]{1,1});
//            add(new int[]{2,2});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 1.2857142857142865
//            add(new int[]{9,9});
//            add(new int[]{8,9});
//            add(new int[]{7,9});
//            add(new int[]{11,12});  // 改{11,13} ans: 1.4326197465181143
//            add(new int[]{15,15});
//            add(new int[]{15,10});
//            add(new int[]{15,11});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 1.414213562373095
//            add(new int[]{3,3});
//            add(new int[]{3,2});
//            add(new int[]{3,1});
//            add(new int[]{1,3});
//            add(new int[]{2,3});
//            add(new int[]{3,4});
//            add(new int[]{3,5});
//            add(new int[]{4,3});
//            add(new int[]{5,3});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 0.5656854249492379
//            add(new int[]{3,3});
//            add(new int[]{3,4});
//            add(new int[]{3,5});
//            add(new int[]{4,3});
//            add(new int[]{5,3});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 0.0
//            add(new int[]{7,7});
//            add(new int[]{6,6});
//            add(new int[]{5,5});
//            add(new int[]{4,4});
//            add(new int[]{3,3});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 3.171859650365775
//            add(new int[]{17,1});
//            add(new int[]{18,5});
//            add(new int[]{19,3});
//            add(new int[]{2,15});
//            add(new int[]{7,7});
//            add(new int[]{16,0});
//            add(new int[]{1,11});
//            add(new int[]{2,16});
//            add(new int[]{5,19});
//            add(new int[]{8,10});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 3.0
//            add(new int[]{7,3});
//            add(new int[]{7,21});
//            add(new int[]{1,3});
//            add(new int[]{1,21});
//            add(new int[]{4,12});
//        }}));
//        System.out.println(new Airport().airport(new ArrayList<int[]>(){{  // 1.0733126291998991
//            add(new int[]{3,3});
//            add(new int[]{4,3});
//            add(new int[]{1,3});
//            add(new int[]{3,5});
//            add(new int[]{3,1});
//        }}));
//    }

//    public static void main(String[] args){
//        Airport sol = new Airport();
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for(Object CaseInList : all){
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0,ac = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray arg_hou = (JSONArray) person.get("houses");
//                    double Answer = (double) person.get("answer");
//                    ArrayList<int[]> HOU = new ArrayList<int[]>();
//                    double Answer_W = 0;
//                    for(int i=0;i<arg_hou.size();i++){
//                        String spl = arg_hou.get(i).toString();
//                        String fir = "";
//                        String sec = "";
//                        String[] two = new String[2];
//                        two = spl.split(",");
//                        fir = two[0].replace("[","");
//                        sec = two[1].replace("]","");
//                        int[] hou = new int[2];
//                        hou[0] = Integer.parseInt(fir);
//                        hou[1] = Integer.parseInt(sec);
//                        HOU.add(hou);
//                    }
//                    Answer_W = sol.airport(HOU);
//                    if(Math.abs(Answer_W-Answer)<1e-4){
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
