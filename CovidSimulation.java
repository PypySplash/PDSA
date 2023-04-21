//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import edu.princeton.cs.algs4.MinPQ;
class CovidSimulation {
    private static class Event implements Comparable<Event>{  // Event必須實作Comparable，class裏面要有compareTo
        public int targetCity, happenDate, NumberOfTraveller, FromCity, ToCity, travellerRecoveryDate;
        public Event(int targetCity, int happenDate, int NumberOfTraveller, int FromCity, int ToCity, int travellerRecoveryDate){
            this.targetCity = targetCity;
            this.happenDate = happenDate;
            this.NumberOfTraveller = NumberOfTraveller;
            this.FromCity = FromCity;
            this.ToCity = ToCity;
            this.travellerRecoveryDate = travellerRecoveryDate;
        }
        public int compareTo(Event that){
            if (this.happenDate < that.happenDate) return -1;
            if (this.happenDate > that.happenDate) return +1;
            // 同一天如果同時有病毒和旅人，是病毒先入侵
            if (this.targetCity >= 0 && that.targetCity < 0) return -1;
            if (this.targetCity < 0 && that.targetCity >=0) return +1;
            return 0;
        }
    }
    public City[] cities;  // 存每個City的資訊
    public static class City{
        public int citizen, recoverDate, maxDate;
        public City(int citizen, int recoverDate, int maxDate){
            this.citizen = citizen;
            this.recoverDate = recoverDate;
            this.maxDate = maxDate;
        }
    }
    private final MinPQ<Event> pq = new MinPQ<>();  // Priority Queue (Min)
    public int N;
    public CovidSimulation(int[] Num_Of_Citizen) {  // 讀入每個City的居民人數
        //The initial number of people in each city is defined here.
        N = Num_Of_Citizen.length;
        // new一個array
        cities = new City[N];  // cities的資訊有：居民人數，恢復日期，最大恢復日期（7 days at max)
        for (int i = 0; i < N; i++) cities[i] = new City(Num_Of_Citizen[i], -1, -1);
    }
    public void virusAttackPlan(int city, int date) {  // 病毒攻擊事件
        //Covid is a highly intelligent being, they plan their attacks carefully.
        //The date on which Covid attacks a specific city would be defined here
        pq.insert(new Event(city, date, -1, -1,-1, -1));  // Priority Queue放入病毒攻擊事件
    }  // Event(city, date, 其他沒有的先預設-1，確保後面呼叫到也不影響
    public void TravelPlan(int NumberOfTraveller, int FromCity, int ToCity, int DateOfDeparture, int DateOfArrival) {  //旅行事件
        //The information of travellers' plan would be written here.
        //Since everyone travel with different methods, the duration to travel from City A to B would not be constant
        // (we tried our best to simplify the problem instead of giving an array of data!)
        pq.insert(new Event(-DateOfArrival, DateOfDeparture, NumberOfTraveller, FromCity, ToCity, -1));  // Priority Queue放入旅行事件
    }  // Event(後面用+-來判斷旅人出發event和旅人抵達event, date = 離家日, 旅行人數, 從哪個城市出發, 到哪個城市, 旅人恢復日（先預設-1)
    public int CityWithTheMostPatient(int date) {  // 讀入日期
        //return the index of city which has the most patients
        //if there are more than two cities with the same amount of patients, return the largest index value.
        int output = -1;

        while (!pq.isEmpty()){  // 當pq裏面還有東西，就不跳出while迴圈
            Event event = pq.delMin();  // Priority Queue把順序前面的pop出去

            // 若event發生日 < 讀入的日期，則開始處理事件
            if (event.happenDate <= date) {
                // virus入侵event && 此時城市未感染
                if (event.targetCity >= 0 && cities[event.targetCity].recoverDate <= event.happenDate) {  // 若已感染，則病毒攻擊無效
                    cities[event.targetCity].recoverDate = event.happenDate + 4;  // recover after 4 days
                    cities[event.targetCity].maxDate = event.happenDate + 7;  // recovery 7 days at max (***注意若恢復日那天又確診，則開始一個新的循環最多又可再7天: 7+7
                }
                // 旅人出發event
                if (event.targetCity < -1) {  // event.targetCity = -DateOfArrival --->>> 必<-1
                    cities[event.FromCity].citizen -= event.NumberOfTraveller;
                    // 因出發event和抵達event必不同日，故新增一個抵達event
                    pq.insert(new Event(-1, -event.targetCity, event.NumberOfTraveller, event.FromCity, event.ToCity, cities[event.FromCity].recoverDate));
                }  // Event(targetCity改為-1，用來判斷抵達事件, -event.targetCity = -(-DateOfArrival), ..., 旅人恢復日 = fromCity的恢復日)
                // 旅人抵達event
                if (event.targetCity == -1) {  // 在出發事件中，已將抵達事件之event.targetCity設為-1
                    cities[event.ToCity].citizen += event.NumberOfTraveller;
                    // 若旅人旅行到目的城鎮還沒恢復
                    if (event.happenDate < event.travellerRecoveryDate){  // event.happenDate = -event.targetCity = -(-event.DateOfArrival)
                        // 若旅人到達日期 < 城市恢復日，則恢復日延後
                        if (event.happenDate < cities[event.ToCity].recoverDate) {  // 恢復日設為min(目的城鎮最大恢復日, max(目的城鎮恢復日, 旅人康復日))
                            cities[event.ToCity].recoverDate = Math.min(cities[event.ToCity].maxDate, Math.max(cities[event.ToCity].recoverDate, event.travellerRecoveryDate));
                        } else {  // 若旅人到達日期 > 城市恢復日，則恢復日重置
                            cities[event.ToCity].recoverDate = event.happenDate + 4;
                            cities[event.ToCity].maxDate = event.happenDate + 7;
                        }
                    }
                }
            }
            else {  // 若event發生日 > 讀入的日期，則把事件塞回去
                pq.insert(event);
                int temp = 0;
                for (int i = 0; i < N; i++) {  // 若城鎮的恢復日 > 當日，且居民人數 > temp，則temp值被取代
                    if (cities[i].recoverDate > date && cities[i].citizen >= temp) {
                        temp = cities[i].citizen;
                        output = i;  // output為temp所在的城鎮
                    }
                }
                return output;  //if every city is clean, please return -1.
            }
        }
        // 若結束while迴圈，則跑一次for迴圈找有確診的最大居民城鎮
        int temp2 = 0;
        for (int i = 0; i < N; i++) {
            if (cities[i].recoverDate > date && cities[i].citizen >= temp2) {
                temp2 = cities[i].citizen;
                output = i;
            }
        }
        return output;  //if every city is clean, please return -1.
    }

    public static void main(String[] args) {
        CovidSimulation sol = new CovidSimulation(new int[]{10, 100, 15, 25, 10, 13});

        sol.virusAttackPlan(0, 1);
        sol.virusAttackPlan(4, 3);
        sol.TravelPlan(3, 0, 3, 3, 4);
        sol.TravelPlan(3, 4, 0, 3, 4);

        System.out.println(sol.CityWithTheMostPatient(2));
        // output = 0

        sol.virusAttackPlan(5, 5);
        sol.TravelPlan(1, 5, 0, 5, 6);

        System.out.println(sol.CityWithTheMostPatient(4));
        // output = 3
        System.out.println(sol.CityWithTheMostPatient(8));
        // output = 5
    }

    //day 1:{10, 100, 15, 25, 10, 13}
    //infectedList:{1, 0, 0, 0, 0, 0}
    //day 2：{10, 100, 15, 25, 10, 13}
    //infectedList:{1, 0, 0, 0, 0, 0}
    //day 3：{7, 100, 15, 25, 7, 13}
    //infectedList:{1, 0, 0, 0, 1, 0}
    //day 4：{10, 100, 15, 28, 7, 13}
    //infectedList:{1, 0, 0, 1, 1, 0}
    //day 5：{10, 100, 15, 28, 7, 12}
    //infectedList:{1, 0, 0, 1, 1, 1}
    //day 6：{11, 100, 15, 28, 7, 12}
    //infectedList:{1, 0, 0, 1, 1, 1}
    //day 7：{11, 100, 15, 28, 7, 12}
    //infectedList:{1, 0, 0, 1, 0, 1}
    //day 8：{11, 100, 15, 28, 7, 12}
    //infectedList:{0, 0, 0, 0, 0, 1}

//    public static void main(String[] args) {
//        CovidSimulation g;
//        JSONParser jsonParser = new JSONParser();
//        try (FileReader reader = new FileReader(args[0])) {
//            JSONArray all = (JSONArray) jsonParser.parse(reader);
//            int waSize = 0;
//            int count = 0;
//            for (Object CaseInList : all) {
//                JSONArray a = (JSONArray) CaseInList;
//                //Board Setup
//                JSONObject argsSetting = (JSONObject) a.get(0);
//                a.remove(0);
//
//                JSONArray argSettingArr = (JSONArray) argsSetting.get("args");
//                int citySetting[] = new int[argSettingArr.size()];
//                for (int i = 0; i < argSettingArr.size(); i++) {
//                    citySetting[i] = (Integer.parseInt(argSettingArr.get(i).toString()));
//                }
//                g = new CovidSimulation(citySetting);
//
//                for (Object o : a) {
//                    JSONObject person = (JSONObject) o;
//                    String func = person.get("func").toString();
//                    JSONArray arg = (JSONArray) person.get("args");
//
//                    switch (func) {
//                        case "virusPlan":
//                            g.virusAttackPlan(Integer.parseInt(arg.get(0).toString()),
//                                    Integer.parseInt(arg.get(1).toString()));
//                            break;
//                        case "TravelPlan":
//                            g.TravelPlan(Integer.parseInt(arg.get(0).toString()), Integer.parseInt(arg.get(1).toString()), Integer.parseInt(arg.get(2).toString()),
//                                    Integer.parseInt(arg.get(3).toString()), Integer.parseInt(arg.get(4).toString()));
//                            break;
//
//                        case "CityMax":
//                            count++;
//                            int ans_sol = g.CityWithTheMostPatient(Integer.parseInt(arg.get(0).toString()));
//                            Long answer = (Long) person.get("answer");
//                            int ans = Integer.parseInt(answer.toString());
//                            if (ans_sol == ans) {
//                                System.out.println(count + ": AC");
//                            } else {
//                                waSize++;
//                                System.out.println(count + ": WA");
//                            }
//                    }
//
//                }
//            }
//            System.out.println("Score: " + (count - waSize) + " / " + count + " ");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
