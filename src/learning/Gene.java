package learning;

public class Gene extends AbstructGene {
    /* 
     * 20
     * diff of stones
     * weight of diff
     * putable cells
     * weight of putable cells
     * 00010203
     * 08091011
     * 16171819
     * 24252627
     * 
     */
    
    public Gene(int n) {
        super(n);
    }
    public Gene(Gene g) {
        super(g);
    }
    
    @Override
    public Object clone() {
        return new Gene(this);
    }
    
    @Override
    public void calcFitness(Gene g) {
        // 自分以外の相手を選んでリーグ対戦(先攻後攻？)
        // 終了時、fitnessに(自分の石の数-相手の石の数)を加算
        
        
    }
}
