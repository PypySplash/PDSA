import java.lang.Math;
        import edu.princeton.cs.algs4.MinPQ;
        import java.util.Comparator;
        import java.util.ArrayList;
        import java.util.List;


class kdtree {
    public class stacks{
        int num;
        stacks next;
    }

    //kd-tree的node，紀錄比較值num(可能是x可能是y，依據height決定)，order表示那個點的原號碼，height=0是偶數層，height=1是奇數層
    public class Node{
        double num;
        int order;
        int height;
        Node right;
        Node left;
        Node(double n, int o , int h){
            num = n;
            order = o;
            right = null;
            left = null;
            height = h;
        }

    }

    //紀錄每一點的x,y值,離此點最近的點c, 被融合的次數times
    //同時c也用來記錄此點是否還存在，若c<0，表示此點被用過
    public class po{
        double xx;
        double yy;
        int c;
        int times;
        po(double x, double y, int t){
            xx = x;
            yy = y;
            c = 10000;
            times = t;
        }
    }
    //紀錄最近的距離, 點n1和n2
    public class dis{
        double distance;
        int n1;
        int n2;

        dis(double d, int nn, int oo){
            distance = d;
            n1 = nn;
            n2 = oo;
        }
    }

    //stack功用：
    //若點1,點2融合成新的點，除了要更新新點的最近距離，也要更新與點1,點2有關的點
    //ex:點3和點5最近的點是2，點3,5也要更新他們各自最近距離的點
    //因此stack存放與此點有關的點
    //ex:first[2]這個stack存放點1,點3,點5

    stacks[] first;
    dis[] dd;
    po[] p;
    Node[] node;
    Node root;
    int h = 0;
    double neardis;
    int nearorder;
    double cn_x;
    double cn_y;
    int cn;
    public void push(int a, int b){
        stacks oldfirst = first[a];
        first[a] = new stacks();
        first[a].num = b;
        first[a].next = oldfirst;
    }

    //recursive function 用來計算每一點離他最近的點和距離
    //cn表示當前點的號碼
    public void nearest(Node x){
        if(x!=null){
            if(x.order!=cn && p[x.order].c>0){ //找最近的點的時候，不用比較自己，以及被用過(不存在)的點
                int ni= x.order;
                //比較距離
                double e = Math.sqrt(Math.pow(cn_x-p[ni].xx,2)+ Math.pow(cn_y-p[ni].yy,2));
                if (e<neardis){
                    neardis = e;
                    nearorder = ni;
                }

            }
            //偶數層 比x
            if(x.height==0){
                if (cn_x > x.num){
                    nearest(x.right);
                    if(neardis > Math.abs(cn_x-x.num)){
                        nearest(x.left);
                    }
                }
                else{
                    nearest(x.left);
                    if(neardis > Math.abs(cn_x-x.num)){
                        nearest(x.right);
                    }
                }
            }
            //奇數層 比y
            else{
                if (cn_y > x.num){
                    nearest(x.right);
                    if(neardis > Math.abs(cn_y-x.num)){
                        nearest(x.left);
                    }
                }
                else{
                    nearest(x.left);
                    if(neardis > Math.abs(cn_y-x.num)){
                        nearest(x.right);
                    }
                }
            }
        }
    }

    public List<double[]> cluster(List<int[]> points, int cluster_num) {

        int ps = points.size();
        //j是所有點的數量總和(包含原本和最後新增的)
        int j = ps+ps-cluster_num+1;

        first = new stacks[j];
        node = new Node[j];
        p = new po[j]; //按照順序存放每一個點
        dd = new dis[100000];
        Node w;

        int n = 1;
        for(int[] i: points) {

            w = root;
            int x = i[0];
            int y = i[1];
            //將點放進kd-tree
            if (n==1){
                root = new Node(i[0],n,0);
            }
            else{
                while (true){
                    if(h%2==0){
                        if (x > w.num){
                            if (w.right!=null){
                                w = w.right;
                            }
                            else{
                                node[n] = new Node(y,n,1);
                                w.right = node[n];
                                break;
                            }
                        }
                        else{
                            if (w.left!=null){
                                w = w.left;
                            }
                            else{
                                node[n] = new Node(y,n,1);
                                w.left = node[n];
                                break;
                            }
                        }
                    }
                    else{
                        if (y > w.num){
                            if (w.right!=null){
                                w = w.right;
                            }
                            else{
                                node[n] = new Node(x,n,0);
                                w.right = node[n];
                                break;
                            }
                        }
                        else{
                            if (w.left!=null){
                                w = w.left;
                            }
                            else{

                                node[n] = new Node(x,n,0);
                                w.left = node[n];
                                break;
                            }
                        }
                    }
                    h++;
                }

            }

            p[n] = new po(x,y,1);
            n++;
            h = 0;
        }

        class cp implements Comparator<dis> {
            public int compare(dis a,dis b){
                if (a.distance>b.distance){
                    return 1;
                }
                return -1;
            }
        }


        MinPQ<dis> pq = new MinPQ<>(new cp());
        //計算每個點的nearest point和distance
        for(int i = 1; i<= ps;i++){
            neardis = 100000000;
            cn = i;
            cn_x = p[i].xx;
            cn_y = p[i].yy;

            nearest(root);

            //尚未被檢查
            if (nearorder > i){
                dd[i] = new dis(neardis, i, nearorder);
                pq.insert(dd[i]);

                p[i].c = nearorder;
                push(i,nearorder);
                push(nearorder, i);
            }
            //已經被檢查(與此點最近的點已經把要比較的distance放進pq就不用再放一次)
            else{
                if(p[nearorder].c==i){ //彼此都是最近的距離
                    dd[i] = new dis(neardis, i, nearorder);
                    p[i].c = nearorder;
                }
                else{
                    dd[i] = new dis(neardis, i, nearorder);
                    p[i].c = nearorder;
                    push(nearorder, i);
                }
            }


        }


        //開始融合新的點
        int u = ps-cluster_num; //新增的點的數量
        int t;
        int z;
        double t1;
        double t2;
        int tis;

        for (int i = 0; i < u; i++){

            while(true){
                double rr = pq.min().distance;
                t = pq.min().n2;
                z = pq.delMin().n1;

                if(p[t].c>0 && p[z].c>0){ //大於0表示這兩個點都存在
                    //calculate centroid
                    tis = p[z].times+p[t].times;
                    t1 = (p[z].xx*p[z].times+p[t].xx*p[t].times)/tis;
                    t2 = (p[z].yy*p[z].times+p[t].yy*p[t].times)/tis;

                    //將新點加入tree
                    h = 0;
                    w = root;
                    while (true){
                        if(h%2==0){
                            if (t1 > w.num){
                                if (w.right!=null){
                                    w = w.right;
                                }
                                else{
                                    node[n] = new Node(t2,n,1);
                                    w.right = node[n];
                                    break;
                                }
                            }
                            else{
                                if (w.left!=null){
                                    w = w.left;
                                }
                                else{
                                    node[n] = new Node(t2,n,1);
                                    w.left = node[n];
                                    break;
                                }
                            }
                        }
                        else{
                            if (t2 > w.num){
                                if (w.right!=null){
                                    w = w.right;
                                }
                                else{
                                    node[n] = new Node(t1,n,0);
                                    w.right = node[n];
                                    break;
                                }
                            }
                            else{
                                if (w.left!=null){
                                    w = w.left;
                                }
                                else{

                                    node[n] = new Node(t1,n,0);
                                    w.left = node[n];
                                    break;
                                }
                            }
                        }
                        h++;
                    }

                    //建立新點
                    p[n] = new po(t1,t2,tis);
                    //被融合的這兩點之後不能再用
                    p[t].c = -1;
                    p[z].c = -1;

                    stacks f1 = first[t];
                    stacks f2 = first[z];

                    //把跟t有關聯的點點都更新
                    while(f1!=null){

                        neardis = 100000000;
                        cn = f1.num;
                        if(p[cn].c>0){
                            cn_x = p[cn].xx;
                            cn_y = p[cn].yy;
                            nearest(root);
                            if (dd[cn].distance!=neardis){

                                if(p[nearorder].c>0){
                                    dd[cn] = new dis(neardis, cn, nearorder);

                                    p[cn].c = nearorder;
                                    pq.insert(dd[cn]);

                                    push(cn,nearorder);
                                    push(nearorder, cn);
                                }
                            }
                        }
                        f1 = f1.next;
                    }
                    //把跟z有關聯的點點都更新
                    while(f2!=null){
                        neardis = 100000000;
                        cn = f2.num;
                        if(p[cn].c>0){
                            cn_x = p[cn].xx;
                            cn_y = p[cn].yy;
                            nearest(root);
                            if (dd[cn].distance!=neardis){

                                dd[cn] = new dis(neardis, cn, nearorder);
                                p[cn].c = nearorder;
                                pq.insert(dd[cn]);

                                push(cn,nearorder);
                                push(nearorder, cn);

                            }
                        }

                        f2 = f2.next;
                    }


                    //找出新點的neardis
                    neardis = 100000000;
                    cn = n;
                    cn_x = t1;
                    cn_y = t2;

                    //找到nearest point and distance
                    nearest(root);

                    dd[n] = new dis(neardis, n, nearorder);
                    push(n,nearorder);
                    push(nearorder, n);
                    p[n].c = nearorder;
                    pq.insert(dd[n]);

                    n++;


                    break;
                }
            }

        }

        j = ps+u+1;
        class cc implements Comparator<po> {
            public int compare(po a,po b){
                if (a.xx>b.xx){
                    return 1;
                }
                if(a.xx==b.xx){
                    if (a.yy>b.yy){
                        return 1;
                    }
                }
                return -1;
            }
        }
        MinPQ<po> pri = new MinPQ<>(new cc());

        ArrayList<double[]> ans = new ArrayList<double[]>();

        for(int m = 1; m < j; m++){
            if(p[m].c>0){//此點還存在才放進pq
                pri.insert(p[m]);
            }
        }
        while(!pri.isEmpty()){
            ans.add(new double[]{pri.min().xx, pri.min().yy});
            pri.delMin();
        }
        return ans;
    }

    public static void main(String[] args) {
    }
}