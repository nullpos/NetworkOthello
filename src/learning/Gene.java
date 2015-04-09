package learning;

public class Gene extends AbstructGene {

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
    public void calcFitness() {
        
    }
}
