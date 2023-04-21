//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.Arrays;

class BoardGame {
    int h;
    int w;
    int[] sideCount;
    public char[] board;
    public WeightedQuickUnionUF wQUUF;

    public BoardGame(int h, int w) {
        this.h = h;  // self.h
        this.w = w;  // self.w
        board = new char[h * w];
        sideCount = new int[h * w];  // 連在一起的stone相加起來的空邊數
        wQUUF = new WeightedQuickUnionUF(h * w);
        Arrays.fill(sideCount, 4);  // 每個格子初始都有四個空邊
    } // create a board of size h*w

    public void putStone(int[] x, int[] y, char stoneType) {
        for (int i = 0; i < x.length; i++) {
            int index = w * x[i] + y[i];
            board[index] = stoneType;

            int up = w * (x[i] - 1) + y[i];
            int down = w * (x[i] + 1) + y[i];
            int left = w * x[i] + (y[i] - 1);
            int right = w * x[i] + (y[i] + 1);

            // 做四次一樣的事情，分別向上下左右找stone，處理boundary condition
            // int rootIndex = weightedQuickUnionUF.find(index);  // 放這邊會錯
            if (x[i] != 0) {
                int rootIndex = wQUUF.find(index);  // 找放stone位置的root（就是自己）
                int rootUp = wQUUF.find(up);  // 找上方stone的root
                sideCount[rootUp] -= 1;  // 每當放一個stone，上方stone的root空邊數-=1
                if (stoneType == board[up] && rootIndex != rootUp) {  // 若rootIndex==rootUp: 則是已經connected
                    wQUUF.union(rootIndex, rootUp);
                    int rootUpUpdate = wQUUF.find(up);
                    if (rootUpUpdate != rootUp) {  // size只有可能大於，root大小不一定也不重要，
                        sideCount[rootIndex] += sideCount[rootUp];  // up -> index
                    }
                    else {  // 若rootUp == rootUpUpdate，則必定是index指向up
                        sideCount[rootUp] += sideCount[rootIndex];  // index -> up
                    }
                }
            }
            if (x[i] != h - 1) {
                int rootIndex = wQUUF.find(index);
                int rootDown = wQUUF.find(down);
                sideCount[rootDown] -= 1;
                if (stoneType == board[down] && rootIndex != rootDown) {
                    wQUUF.union(rootIndex, rootDown);
                    int rootDownUpdate = wQUUF.find(down);
                    if (rootDownUpdate != rootDown) {
                        sideCount[rootIndex] += sideCount[rootDown];  // down -> index
                    }
                    else {
                        sideCount[rootDown] += sideCount[rootIndex];  // index -> down
                    }
                }
            }
            if (y[i] != 0) {
                int rootIndex = wQUUF.find(index);
                int rootLeft = wQUUF.find(left);
                sideCount[rootLeft] -= 1;
                if (stoneType == board[left] && rootIndex != rootLeft) {
                    wQUUF.union(rootIndex, rootLeft);
                    int rootLeftUpdate = wQUUF.find(left);
                    if (rootLeftUpdate != rootLeft) {
                        sideCount[rootIndex] += sideCount[rootLeft];  // left -> index
                    }
                    else {
                        sideCount[rootLeft] += sideCount[rootIndex];  // index -> left
                    }
                }
            }
            if (y[i] != w - 1) {
                int rootIndex = wQUUF.find(index);
                int rootRight = wQUUF.find(right);
                sideCount[rootRight] -= 1;
                if (stoneType == board[right] && rootIndex != rootRight) {
                    wQUUF.union(rootIndex, rootRight);
                    int rootRightUpdate = wQUUF.find(right);
                    if (rootRightUpdate != rootRight) {
                        sideCount[rootIndex] += sideCount[rootRight];  // right -> index
                    }
                    else {
                        sideCount[rootRight] += sideCount[rootIndex];  // index -> right
                    }
                }
            }
        }
    } // put stones of the specified type on the board according to the coordinates

    public boolean surrounded(int x, int y) {
        int index = w * x + y;
        int root = wQUUF.find(index);
        return sideCount[root] == 0;
    } // Answer if the stone and its connected stones are surrounded by another type of stones

    public char getStoneType(int x, int y) {
        int index = w * x + y;
        return board[index];
    } // Get the type of the stone at (x,y)

    public int countConnectedRegions() {
        int emptyCount = 0;  // 計算空格的數量
        for (char type : board) {
            if (type != 'O' && type != 'X') {
                emptyCount += 1;
            }
        }
        return wQUUF.count() - emptyCount;
    } // Get the number of connected regions in the board, including both types of the stones

    public static void main(String args[]) {
        BoardGame g = new BoardGame(3, 3);
        g.putStone(new int[]{1}, new int[]{1}, 'O');
        System.out.println(g.surrounded(1, 1));
        System.out.println(g.countConnectedRegions());

        g.putStone(new int[]{0, 1, 1}, new int[]{1, 0, 2}, 'X');
        System.out.println(g.surrounded(1, 1));
        System.out.println(g.countConnectedRegions());

        g.putStone(new int[]{2}, new int[]{1}, 'X');
        System.out.println(g.surrounded(1, 1));
        System.out.println(g.surrounded(2, 1));
        System.out.println(g.countConnectedRegions());

        g.putStone(new int[]{2}, new int[]{0}, 'O');
        System.out.println(g.surrounded(2, 0));
        System.out.println(g.countConnectedRegions());
    }

//    public static void test(String[] args){
//        BoardGame g;
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            int count = 0;
//            for(Object CaseInList : all){
//                count++;
//                JSONArray a = (JSONArray) CaseInList;
//                int testSize = 0; int waSize = 0;
//                System.out.print("Case ");
//                System.out.println(count);
//                //Board Setup
//                JSONObject argsSeting = (JSONObject) a.get(0);
//                a.remove(0);
//
//                JSONArray argSettingArr = (JSONArray) argsSeting.get("args");
//                g = new BoardGame(
//                        Integer.parseInt(argSettingArr.get(0).toString())
//                        ,Integer.parseInt(argSettingArr.get(1).toString()));
//
//                for (Object o : a)
//                {
//                    JSONObject person = (JSONObject) o;
//
//                    String func =  person.get("func").toString();
//                    JSONArray arg = (JSONArray) person.get("args");
//
//                    switch(func){
//                        case "putStone":
//                            int xArray[] = JSONArraytoIntArray((JSONArray) arg.get(0));
//                            int yArray[] = JSONArraytoIntArray((JSONArray) arg.get(1));
//                            String stonetype =  (String) arg.get(2);
//
//                            g.putStone(xArray,yArray,stonetype.charAt(0));
//                            break;
//                        case "surrounded":
//                            Boolean answer = (Boolean) person.get("answer");
//                            testSize++;
//                            System.out.print(testSize + ": " + func + " / ");
//                            Boolean ans = g.surrounded(
//                                    Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString())
//                            );
//                            if(ans==answer){
//                                System.out.println("AC");
//                            }else{
//                                waSize++;
//                                System.out.println("WA");
//                            }
//                            break;
//                        case "countConnectedRegions":
//                            testSize++;
//                            int ans2 = Integer.parseInt(arg.get(0).toString());
//                            int ansCR = g.countConnectedRegions();
//                            System.out.print(testSize + ": " + func + " / ");
//                            if(ans2==ansCR){
//                                System.out.println("AC");
//                            }else{
//                                waSize++;
//                                System.out.println("WA");
//                            }
//                    }
//
//                }
//                System.out.println("Score: " + (testSize-waSize) + " / " + testSize + " ");
//            }
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static int[] JSONArraytoIntArray(JSONArray x){
//        int sizeLim = x.size();
//        int MyInt[] = new int[sizeLim];
//        for(int i=0;i<sizeLim;i++){
//            MyInt[i]= Integer.parseInt(x.get(i).toString());
//        }
//        return MyInt;
//    }
//
//    public static void main(String[] args) {//mian
//        test(args);
//    }

}
