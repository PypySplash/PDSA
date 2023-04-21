//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;

class LongJump {
    // Method 3: 手刻BST (100pts)
    public static class hwBST {  // 照顧泛型的function麻煩：把Key&Value拿掉
        private Node root;  // pointer，指向Node
        private static class Node {  // Node static?
            private final int key;  // key final?
            private int leftSum, rightSum;  // 紀錄Node的左邊children和＆右邊children和 => 左和＆右和
            private Node left, right;  // left & right subtrees
            public Node(int key, int leftSum, int rightSum) {  // val用不到
                this.key = key;
                this.leftSum = leftSum;
                this.rightSum = rightSum;
            }
        }
        public void put(int key) {root = put(root, key);}
        private Node put(Node x, int key) {  // put的時候同時判斷每個Node的左和＆右和
            if (x == null) return new Node(key, 0, 0);  // 找到最後x == null時，new一個Node，並將root指到這個Node
            int cmp = key - x.key;  // compareTo取代成key - x.key
            if (cmp < 0) {  // key < x.key，往左邊children找
                x.left = put(x.left, key);
                x.leftSum = x.left.key + x.left.leftSum + x.left.rightSum;  // Node的左和 ＝ 左邊孩子＋左邊孩子的左和＋左邊孩子的右和
            } else if (cmp > 0) {  // key > x.key，往右邊children找
                x.right = put(x.right, key);
                x.rightSum = x.right.key + x.right.leftSum + x.right.rightSum;  // Node的右和 ＝ 右邊孩子＋右邊孩子的左和＋右邊孩子的右和
            }
            return x;
        }
        public int sum(int key) {
            int[] tempSum = new int[1];  // 用array存是因為可以跨function儲存
            Node x = sum(root, key, tempSum);  // 從root開始找，最後回傳<=key的sum
            return tempSum[0];  // return儲存的sum值
        }
        private Node sum(Node x, int key, int[] tempSum) { //目前的Node.x.key vs. key，比較完後向下繼續判斷
            if (x == null) return null;
            int cmp = key - x.key;
            if (cmp == 0) {  // 若key == x.key，則紀錄這個Node的左和+這個key
                tempSum[0] += x.leftSum + x.key;
                return x;
            }
            if (cmp <  0) return sum(x.left, key, tempSum);  // 若key < x.key => 往左邊children找
            // 若key > x.key => 往右邊children找，同時紀錄x這個Node的左和
            tempSum[0] += x.leftSum + x.key;
            Node t = sum(x.right, key, tempSum);
            if (t != null) return t;
            else return x;  // 要往右邊children找，若已經找到t == null，則x就是floor
//            return Objects.requireNonNullElse(t, x);  // 這行和上面兩行一樣意思 （要import Objects)
        }
        // ceiling(public + private)照刻，把val拿掉
        public int ceiling(int key) {
            Node x = ceiling(root, key);
            if (x == null) return 0;
            else return x.key;
        }
        private Node ceiling(Node x, int key) {
            if (x == null) return null;
            int cmp = key - x.key;
            if (cmp == 0) return x;
            if (cmp <  0) {
                Node t = ceiling(x.left, key);
                if (t != null) return t;
                else return x;
            }
            return ceiling(x.right, key);
        }
    }
    hwBST hwBST = new hwBST();  // new一個手刻BST
    public LongJump(int[] playerList) {for (int j : playerList) hwBST.put(j);}  // Add new player in the competition with different distance
    public void addPlayer(int distance) {hwBST.put(distance);}  // return the winners total distance in range[from, to]
    public int winnerDistances(int from, int to) {
        if (from == hwBST.ceiling(from)) return hwBST.sum(to) - hwBST.sum(from) + from;  // boundary condition: 若from在邊界上會被扣掉，要加回去
        else return hwBST.sum(to) - hwBST.sum(from);
    }


////    Method 1: ArrayList (40pts)
//import java.util.ArrayList;
//import java.util.Comparator;
//    class LongJump {
//        ArrayList<Integer> playerRange = new ArrayList<>();
//        public LongJump(int[] playerList) {for (int i = 0; i < playerList.length; i++) playerRange.add(i, playerList[i]);}  // Add new player in the competition with different distance
//        public void addPlayer(int distance) {
//            playerRange.add(distance);
//            playerRange.sort(Comparator.naturalOrder());
//        }  // return the winners total distance in range[from, to]
//        public int winnerDistances(int from, int to) {
//            int sum = 0;
//            for (Integer integer : playerRange) if (integer >= from && integer <= to) sum += integer;
//            return sum;
//        }

////    Method 2: BST (40pts)
//import edu.princeton.cs.algs4.BST;
//    class LongJump {
//        BST<Integer, Integer> BST = new BST<>();
//        public LongJump(int[] playerList) {for (int j : playerList) BST.put(j, j);}
//        public void addPlayer(int distance) {BST.put(distance, distance);}
//        public int winnerDistances(int from, int to) {
//            int sum = 0;
//            Iterable<Integer> keys = BST.keys(from, to);
//            for (int key : keys) sum += key;
//            return sum;
//        }

    public static void main(String[] args) {
        LongJump solution = new LongJump(new int[]{2,5,6});
        System.out.println(solution.winnerDistances(3,10));
        solution.addPlayer(10);
        solution.addPlayer(8);
        System.out.println(solution.winnerDistances(3,10));
        solution.addPlayer(13);
        System.out.println(solution.winnerDistances(2,19));
    }

    // Expected output:
    // 11
    // 29
    // 44

//    public static void main(String[] args) {
//        LongJump g;
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
//                JSONObject argsSetting = (JSONObject) a.get(0);
//                a.remove(0);
//
//                JSONArray argSettingArr = (JSONArray) argsSetting.get("args");
//
//                int[] arr=new int[argSettingArr.size()];
//                for(int k=0;k<argSettingArr.size();k++) {
//                    arr[k] = (int)(long) argSettingArr.get(k);
//                }
//                g = new LongJump(arr);
//
//                for (Object o : a) {
//                    JSONObject person = (JSONObject) o;
//
//                    String func = person.get("func").toString();
//                    JSONArray arg = (JSONArray) person.get("args");
//
//                    switch (func) {
//                        case "addPlayer" -> g.addPlayer(Integer.parseInt(arg.get(0).toString()));
//                        case "winnerDistances" -> {
//                            testSize++;
//                            Integer t_ans = (int)(long)person.get("answer");
//                            Integer r_ans = g.winnerDistances(Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString()));
//                            if (t_ans.equals(r_ans)) {
//                                System.out.println("winnerDistances : AC");
//                            } else {
//                                waSize++;
//                                System.out.println("winnerDistances : WA");
//                                System.out.println("Your answer : "+r_ans);
//                                System.out.println("True answer : "+t_ans);
//                            }
//                        }
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
