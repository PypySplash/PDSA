//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.Stack;

class Kings {  // 寫一個compareTo來做到排序的效果
    // class實作一個Comparable，裡面要有compareTo
    public static class King implements Comparable<King> {
        private final int strength, location;
        public King(int STR, int index) {
            strength = STR;
            location = index;
        }
        public int compareTo(King that) {  // compareTo使得king可以根據str來排順序
            if (this.strength > that.strength ) return -1;
            if (this.strength < that.strength ) return +1;
            if (this.location < that.location ) return -1;
            if (this.location > that.location ) return +1;
            return 0;
        }
    }
    public King[] kingsArray;
    public int kingStackSize;
    public Kings(int[] strength, int[] range){
        // Given the attributes of each warrior
        int N = strength.length;
        int[] leftAttack = new int[N];   // 來自左邊的攻擊
        int[] rightAttack = new int[N];  // 來自右邊的攻擊

        for(int i = 0; i < N; i++) leftAttack[i] = rightAttack[i] = -Integer.MIN_VALUE;  // 設一個不會被變更的隨意數字

        Stack<Integer> rightStack = new Stack<>();  // 由左往右找
        Stack<Integer> leftStack = new Stack<>();   // 由右往左找
        Stack<King> kingStack = new Stack<>();
        for (int i = 0; i < N; i++){  // 從最左邊開始往右找
            // while loop: 條件1.Stack不是空的； 條件2.str[i] > str[stack最上面的index]; 條件3.range[i] >= i-stack最上面的index
            while (!rightStack.isEmpty() && strength[i]>strength[rightStack.peek()] && range[i] >= i-rightStack.peek()){
                // stack.peek(): 看最上面的integer
                rightAttack[rightStack.pop()] = i;
            }
            rightStack.push(i);
        }
        for (int i = N - 1; i >= 0; i--){  // 從最右邊開始往左找
            while (!leftStack.isEmpty() && strength[i] > strength[leftStack.peek()] && range[i] >= leftStack.peek()-i){
                leftAttack[leftStack.pop()] = i;
            }
            leftStack.push(i);
        }
        for (int i = 0; i < N; i++){  // 接下來找index沒有被更新的，即為king
            if (rightAttack[i] == -Integer.MIN_VALUE && leftAttack[i] == -Integer.MIN_VALUE){
                kingStack.push(new King(strength[i], i));
            }
        }
        kingStackSize = kingStack.size();  // 創一個新變數是為了topKKings，local變數不能跨越function
        kingsArray = new King[kingStackSize];  // 開一個array存放kings
        for (int i = 0; i < kingStackSize; i++) kingsArray[i] = kingStack.pop();
        Arrays.sort(kingsArray);  // 整理好kings
    }
    public int[] topKKings(int k) {
        int[] topKArray = new int[Math.min(kingStackSize, k)];  // king的數量有可能比k還小
        // 只要kings的location不用strength
        for (int i = 0; i < Math.min(kingStackSize, k); i++) topKArray[i] = kingsArray[i].location;
        return topKArray;
        // complete the code by returning an int[]
        // remember to return the array of indexes in the descending order of strength
    }

    public static void main(String[] args) {

        Kings sol = new Kings(new int[]{15, 3, 26, 2, 5, 19, 12, 8}
                , new int[]{1, 6, 1, 3, 2, 0, 1, 5});
        System.out.println(Arrays.toString(sol.topKKings(3)));
        // In this case, the kings are [0, 2, 4, 5, 6] (without sorting, only by the order of ascending indices)
        // Output: [2, 5, 0]
    }

//        Kings sol = new Kings(new int[] {1}
//                , new int[] {0});
//        System.out.println(Arrays.toString(sol.topKKings(1)));
//        // Output: [0]
//    }

//        Kings sol = new Kings(new int[] {1,2,1}
//                , new int[] {1,1,1});
//        System.out.println(Arrays.toString(sol.topKKings(1)));
//        // Output: [1]
//    }

//        Kings sol = new Kings(new int[] {1,2,2,1}
//                , new int[] {0,0,0,0});
//        System.out.println(Arrays.toString(sol.topKKings(3)));
//        // Output: [1,2,0]
//    }

//        Kings sol = new Kings(new int[]{1, 2, 6, 1}
//                , new int[]{1, 5, 0, 1});
//        System.out.println(Arrays.toString(sol.topKKings(10)));
//        // Output: [2,1,3]
//    }

//        Kings sol = new Kings(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}
//                , new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0});
//        System.out.println(Arrays.toString(sol.topKKings(10)));
//        // Output: [8,7]
//    }

//        Kings sol = new Kings(new int[] {0,1,2,3,4,3,2,1,0}
//                , new int[] {8,7,6,5,4,3,2,1,0});
//        System.out.println(Arrays.toString(sol.topKKings(5)));
//        // Output: [4]
//    }

//    public static void main(String[] args){
//        Kings sol;
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])){
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for(Object CaseInList : all){
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0,ac = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray arg_str = (JSONArray) person.get("strength");
//                    JSONArray arg_rng = (JSONArray) person.get("attack_range");
//                    Long arg_k = (Long) person.get("k");
//                    JSONArray arg_ans = (JSONArray) person.get("answer");
//                    int STH[] = new int[arg_str.size()];
//                    int RNG[] = new int[arg_str.size()];
//                    int k = Integer.parseInt(arg_k.toString());
//
//                    int Answer[] = new int[arg_ans.size()];
//                    int Answer_W[] = new int[arg_ans.size()];
//                    for(int i=0;i<arg_ans.size();i++){
//                        Answer[i]=(Integer.parseInt(arg_ans.get(i).toString()));
//                    }
//                    for(int i=0;i<arg_str.size();i++){
//                        STH[i]=(Integer.parseInt(arg_str.get(i).toString()));
//                        RNG[i]=(Integer.parseInt(arg_rng.get(i).toString()));
//                    }
//                    sol = new Kings(STH,RNG);
//                    Answer_W = sol.topKKings(k);
//                    for(int i=0;i<arg_ans.size();i++){
//                        if(Answer_W[i]==Answer[i]){
//                            if(i==arg_ans.size()-1){
//                                System.out.println(q_cnt+": AC");
//                            }
//                        }else {
//                            wa++;
//                            System.out.println(q_cnt+": WA");
//                            break;
//                        }
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
