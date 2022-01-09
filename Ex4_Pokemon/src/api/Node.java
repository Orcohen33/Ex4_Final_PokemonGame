package api;

public class Node implements NodeData ,Comparable<Node>{
    private int key=0;
    private static int id=0;
    private GeoLocation location;
    private double weight;
    private String info;
    private int tag;



    public Node() {
        this.key = id++;// id
        this.tag = 0 ;
    }
    public Node(Point3D point3D) {
        this.key = id++;// id
        this.tag = 0 ;
        setLocation(point3D);
    }
    public Node(int key){
        this.key = key; // id
        this.tag = 0 ;
    }
    public Node(GeoLocation location ,int tag) {
        this.key = id++; // id
        this.location = location; // pos
        this.info = "key :"+ key+"\nlocation :" + location; //key + location
        this.tag = tag; // mark by value
    }
    public Node(GeoLocation location ,int tag,int key) {
        this.key = key; // id
        this.location = location; // pos
        this.info = "key :"+ key+"\nlocation :" + location; //key + location
        this.tag = tag; // mark by value
    }
    public Node(String s) {
        try {
            String[] a = s.split(",");
            double x = Double.parseDouble(a[0]);
            double y = Double.parseDouble(a[1]);
            double z = Double.parseDouble(a[2]);
            this.setLocation(new Point3D(x,y,z));
            this.key = id++;
        }
        catch(IllegalArgumentException e) {
            System.err.println("ERR: got wrong format string for POint3D init, got:"+s+"  should be of format: x,y,x");
            throw(e);
        }
    }


    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight=w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    public String toString(){
        return String.valueOf(this.getKey());
    }

    // ---------For Junit testing---------

    public boolean equals(Object d){
        if ( d==null ) return false;
        if ( d.getClass() != this.getClass() ) return false;
        NodeData temp = (NodeData) d;
        return ( this.info.equals(temp.getInfo()) && temp.getKey() == this.getKey() && this.location.equals(temp.getLocation()) && this.tag == temp.getTag() );
    }


    @Override
    public int compareTo(Node o) {
        if(o==null) return 1;
        if(this.weight > o.getWeight()) return 1;
        else if(this.getWeight() == o.getWeight()) return 0;
        return -1;
    }
}
