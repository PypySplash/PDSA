//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import edu.princeton.cs.algs4.*;
import java.util.*;

class Percolation {
//    private static class Node {
//        private Point2D site;
//        private Node next;
//    }
    int N, count, percolatedRoot;
    boolean stop;
    public boolean[] grid;
    public WeightedQuickUnionUF UF2;  // 上下各開一個點，用來判斷percolate
    public WeightedQuickUnionUF UF1;  // 只開上面的點，用來判斷isFull
    public WeightedQuickUnionUF UF0;  // 上下不開點，用來記錄coordinate
    public List<Point2D> coordinate;

    public Percolation(int N) {
        this.N = N;
        this.grid = new boolean[N * N + 2];  // 多加格子外上下的點各一，用來連接最上排與最下排的格子
        Arrays.fill(grid, false);  // false: blocked
        UF2 = new WeightedQuickUnionUF(N * N + 2);  // 上下各開一個點
        UF1 = new WeightedQuickUnionUF(N * N + 1);  // 只開上面的點
        UF0 = new WeightedQuickUnionUF(N * N);  // 上下都不開點
    }// create N-by-N grid, with all sites blocked

    public void open(int i, int j) {
        count++;

        int topSite = N * N;  // index: N * N + 1 -1
        int bottomSite = N * N + 1;  // index: N * N + 2 - 1
        int up = N * (i - 1) + j;
        int down = N * (i + 1) + j;
        int left = N * i + (j - 1);
        int right = N * i + (j + 1);
        int index = N * i + j;
        grid[index] = true;  // 給定座標，就打開格子

        if (i != 0) {
            if (grid[up]) {  // grid[up] -> grid[up] = true
                UF2.union(index, up);
                UF1.union(index, up);
                UF0.union(index, up);
            }
        } else {  // if i == 0
            UF2.union(index, topSite);
            UF1.union(index, topSite);
        }

        if (i != N - 1) {
            if (grid[down]) {
                UF2.union(index, down);
                UF1.union(index, down);
                UF0.union(index, down);
            }
        } else UF2.union(index, bottomSite);  // if i == N - 1

        if (j != 0) {
            if (grid[left]) {
                UF2.union(index, left);
                UF1.union(index, left);
                UF0.union(index, left);
            }
        }
        if (j != N - 1) {
            if (grid[right]) {
                UF2.union(index, right);
                UF1.union(index, right);
                UF0.union(index, right);
            }
        }
        // 只需要第一次percolate的座標
        if (!stop && percolates()){  // if percolates() = true
            this.coordinate = new ArrayList<>();
            this.percolatedRoot = UF0.find(index);  // percolatedRegion用UF0判斷
            for (int k = 0; k < N * N; k++) {
                int findIndex = UF0.find(k);
                if (findIndex == percolatedRoot) {
                    int x = k / N;
                    int y = k % N;
                    Point2D location = new Point2D(x, y);
                    coordinate.add(location);
                }
            }
            stop = true;  // 第一次percolate即停止，避免找到第二次percolate之後的座標
        }
    }// open site (row i, column j) if it is not open already

    public boolean isOpen(int i, int j) {
        int index = N * i + j;
        return grid[index];
    }// is site (row i, column j) open?

    public boolean isFull(int i, int j) {  // iPad上看特例情況
        int index = N * i + j;
        int topSite = N * N;
        return UF1.find(index) == UF1.find(topSite);  // isFull用UF1判斷
    }// is site (row i, column j) full?

    public boolean percolates() {  // percolates用UF2判斷
        int topRoot = UF2.find(N * N);
        int bottomRoot = UF2.find(N * N + 1);
        return UF2.find(topRoot) == UF2.find(bottomRoot);
    }// does the system percolate?

    public Point2D[] PercolatedRegion() {
        Point2D[] end = coordinate.toArray(new Point2D[coordinate.size()]);
        Quick.sort(end);
        return end;
    }// print the sites of the percolated region in order

    public static void main(String[] args) {
        // test
        Percolation s = new Percolation(3);
        s.open(1,1);
        System.out.println(s.isFull(1, 1));
        System.out.println(s.percolates());
        s.open(0,1);
        s.open(2,0);
        System.out.println(s.isFull(1, 1));
        System.out.println(s.isFull(0, 1));
        System.out.println(s.isFull(2, 0));
        System.out.println(s.percolates());
        s.open(2,1);
        System.out.println(s.isFull(1, 1));
        System.out.println(s.isFull(0, 1));
        System.out.println(s.isFull(2, 0));
        System.out.println(s.isFull(2, 1));
        System.out.println(s.percolates());
        Point2D[] pr = s.PercolatedRegion();
        for (int i = 0; i < pr.length; i++) {
            System.out.println("("+(int)pr[i].x() + "," + (int)pr[i].y()+")");
        }
    }

//    public static void main(String[] args) {
//        Percolation g;
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])) {
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            int count = 0;
//            for (Object CaseInList : all) {
//                count++;
//                JSONArray a = (JSONArray) CaseInList;
//                int testSize = 0;
//                int waSize = 0;
//                System.out.print("Case ");
//                System.out.println(count);
//                //Board Setup
//                JSONObject argsSeting = (JSONObject) a.get(0);
//                a.remove(0);
//
//                JSONArray argSettingArr = (JSONArray) argsSeting.get("args");
//                g = new Percolation(
//                        Integer.parseInt(argSettingArr.get(0).toString()));
//
//                for (Object o : a) {
//                    JSONObject person = (JSONObject) o;
//
//                    String func = person.get("func").toString();
//                    JSONArray arg = (JSONArray) person.get("args");
//
//                    switch (func) {
//                        case "open":
//                            g.open(Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString()));
//                            break;
//                        case "isOpen":
//                            testSize++;
//                            String true_isop = (Boolean) person.get("answer") ? "1" : "0";
//                            String ans_isop = g.isOpen(Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString())) ? "1" : "0";
//                            if (true_isop.equals(ans_isop)) {
//                                System.out.println("isOpen : AC");
//                            } else {
//                                waSize++;
//                                System.out.println("isOpen : WA");
//                            }
//                            break;
//                        case "isFull":
//                            testSize++;
//                            String true_isfu = (Boolean) person.get("answer") ? "1" : "0";
//                            String ans_isfu = g.isFull(Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString())) ? "1" : "0";
//                            if (true_isfu.equals(ans_isfu)) {
//                                System.out.println("isFull : AC");
//                            } else {
//                                waSize++;
//                                System.out.println("isFull : WA");
//                            }
//                            break;
//                        case "percolates":
//                            testSize++;
//                            String true_per = (Boolean) person.get("answer") ? "1" : "0";
//                            String ans_per = g.percolates() ? "1" : "0";
//                            if (true_per.equals(ans_per)) {
//                                System.out.println("percolates : AC");
//                            } else {
//                                waSize++;
//                                System.out.println("percolates : WA");
//                            }
//                            break;
//                        case "PercolatedRegion":
//                            testSize++;
//                            String true_reg = person.get("args").toString();
//                            String reg = "[";
//                            Point2D[] pr = g.PercolatedRegion();
//                            for (int i = 0; i < pr.length; i++) {
//                                reg = reg + ((int) pr[i].x() + "," + (int) pr[i].y());
//                                if (i != pr.length - 1) {
//                                    reg = reg + ",";
//                                }
//                            }
//                            reg = reg + "]";
//                            if (true_reg.equals(reg)) {
//                                System.out.println("PercolatedRegion : AC");
//                            } else {
//                                waSize++;
//                                System.out.println("PercolatedRegion : WA");
//                            }
//                            break;
//                    }
//
//                }
//                System.out.println("Score: " + (testSize - waSize) + " / " + testSize + " ");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
