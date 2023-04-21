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

class Warriors {

    public int[] warriors(int[] strength, int[] range) {
        // Given the attributes of each warrior and output the minimal and maximum
        // index of warrior can be attacked by each warrior.

        // Method 1: 80/100
        /*
        int N = strength.length;
        int[] output = new int[2 * N];
        output[2 * N - 1] = N - 1;  // 最右邊的warrior不能往右打
        for (int i = N - 1; i > 0; i--) {  // 從最右邊開始往左找
            int j = i - 1;  // 每次往左找一個index
            output[2 * i] = i;  // output[2i]用來記錄往左找的index
            while (Math.abs(j - i) <= range[i] && strength[j] < strength[i]) {
                output[2 * i] = j;
                if (j > 0) {j = j - 1;}  // 往左找到0之前就停下來
                else break;
            }
        }
        for (int i = 0; i < N - 1; i++) {  // 從最左邊開始往右找
            int j = i + 1;
            output[2 * i + 1] = i;  // output[2i+1]用來記錄往右找的index
            while (Math.abs(j - i) <= range[i] && strength[j] < strength[i]) {
                output[2 * i + 1] = j;
                if (j < N - 1) {j = j + 1;}  // 往右找到N-1前停下來
                else break;
            }
        }
        if (N==1) {return new int[] {0, 0};}  // 若N==1, 則回傳{0, 0}
        else return output;
         */
        // Method 2: Stack 100/100
        int N = strength.length;

        int[] output = new int[2 * N];
        // 設定每個點的攻擊範圍最遠頂多到index[N-1]，避免超出邊界
        for(int i = 0; i < N; i++) {output[2 * i + 1] = N - 1;}

        Stack<Integer> rightStack = new Stack<>();
        Stack<Integer> leftStack = new Stack<>();
        // first compare the strength without considering the effective range (regardless of the range)
        for (int i = 0; i < N; i++){  // 從最左邊開始往右找
            while (!rightStack.isEmpty() && strength[i] >= strength[rightStack.peek()]){  // 和最上面的比較
                // stack.peek(): 看最上面的integer
                output[2 * rightStack.pop() + 1] = i - 1;  // pop出來的index，停止在i-1的地方
            }
            rightStack.push(i);
        }
        for (int i = N - 1; i >= 0; i--){  // 從最右邊開始往左找
            while (!leftStack.isEmpty() && strength[i] >= strength[leftStack.peek()]){
                output[2 * leftStack.pop()] = i + 1;
            }
            leftStack.push(i);
        }
        // consider the effective range
        for (int i = 0; i < N; i++){
            // virtual range = 在不考慮攻擊距離的情況下的攻擊到的index-原本的index
            // 若virtual range > range，則在原本的index回傳actual range = index + range
            if (Math.abs(output[2 * i + 1] - i) > range[i]) {output[2 * i + 1] = i + range[i];}
            if (Math.abs(output[2 * i] - i) > range[i]) {output[2 * i] = i - range[i];}  // 往左打則相反
        }
        if (N==1) {return new int[] {0, 0};}  // 若N==1, 則回傳{0, 0}
        else return output;
        // complete the code by returning an int[]
    }

    public static void main(String[] args) {
        Warriors sol = new Warriors();
        System.out.println(Arrays.toString(
                sol.warriors(new int[] {11, 13, 11, 7, 15},
                             new int[] { 1,  8,  1, 7,  2})));
        // Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
    }

//    public static void main(String[] args) {
//        Warriors sol = new Warriors();
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])) {
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            for (Object CaseInList : all) {
//                JSONArray a = (JSONArray) CaseInList;
//                int q_cnt = 0, wa = 0, ac = 0;
//                for (Object o : a) {
//                    q_cnt++;
//                    JSONObject person = (JSONObject) o;
//                    JSONArray arg_str = (JSONArray) person.get("strength");
//                    JSONArray arg_rng = (JSONArray) person.get("attack_range");
//                    JSONArray arg_ans = (JSONArray) person.get("answer");
//                    int STH[] = new int[arg_str.size()];
//                    int RNG[] = new int[arg_str.size()];
//                    int Answer[] = new int[arg_ans.size()];
//                    int Answer_W[] = new int[arg_ans.size()];
//                    for (int i = 0; i < arg_ans.size(); i++) {
//                        Answer[i] = (Integer.parseInt(arg_ans.get(i).toString()));
//                        if (i < arg_str.size()) {
//                            STH[i] = (Integer.parseInt(arg_str.get(i).toString()));
//                            RNG[i] = (Integer.parseInt(arg_rng.get(i).toString()));
//                        }
//                    }
//                    Answer_W = sol.warriors(STH, RNG);
//                    for (int i = 0; i < arg_ans.size(); i++) {
//                        if (Answer_W[i] == Answer[i]) {
//                            if (i == arg_ans.size() - 1) {
//                                System.out.println(q_cnt + ": AC");
//                            }
//                        } else {
//                            wa++;
//                            System.out.println(q_cnt + ": WA");
//                            break;
//                        }
//                    }
//
//                }
//                System.out.println("Score: " + (q_cnt - wa) + "/" + q_cnt);
//
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
