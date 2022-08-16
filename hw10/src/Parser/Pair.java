package Parser;

public class Pair<Key, Value> {
    private Key k;
    private Value v;
    public Pair(Key k, Value v){
        this.k = k;
        this.v = v;
    }
    public Key getKey(){ return k; }
    public Value getValue(){ return v; }
    public void setKey(Key k){ this.k = k; }
    public void setValue(Value v){ this.v = v; }
}