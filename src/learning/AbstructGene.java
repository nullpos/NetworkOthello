package learning;

import util.*;

public abstract class AbstructGene implements Sortable {
    protected byte[] chromosome; // byte -> -127~128
    protected double fitness;
    
    public AbstructGene(int n) {
        chromosome = new byte[n]; //
        fitness = 0.0;
    }
    
    public AbstructGene(Gene g) {
        chromosome = new byte[g.chromosome.length];
        System.arraycopy(g.chromosome,0,chromosome,0,chromosome.length);
        fitness=g.fitness;
    }
    
    public abstract Object clone();
    
    public abstract void calcFitness(Gene g);
    
    public double getFitness(){ return fitness; }
    
    public boolean isBefore(Sortable s){
        return fitness > ((Gene)s).fitness;
    }
    
    public boolean isAfter(Sortable s){
        return fitness < ((Gene)s).fitness;
    }
    
    public byte getRandomChromosome(){
        return (byte) (Math.random() * (Byte.MAX_VALUE - Byte.MIN_VALUE + 1));
    }
    
    public void initRandom(){
        for(int i = 0; i < chromosome.length; i++) {
            chromosome[i] = getRandomChromosome();
        }
    }
    
    /**
        一様交叉 遺伝子全体をランダムに交叉
    */
    public static void flatCrossover(Gene g1,Gene g2,double p){
        int len = g1.chromosome.length;
        if (len > g2.chromosome.length) len = g2.chromosome.length;
        
        for(int i = 0; i < len; i++) {
            if(Math.random() < p) {
                byte d = g1.chromosome[i];
                g1.chromosome[i] = g2.chromosome[i];
                g2.chromosome[i] = d;
            }
        }
    }
    
    /**
        1点交叉 ランダムな1点のみ交叉
    */
    public static void onePointCrossover(Gene g1,Gene g2){
        int len=g1.chromosome.length;
        if(len>g2.chromosome.length) len=g2.chromosome.length;
        int r=(int)(Math.random()*len);
        
        for(int i=r;i<len;i++){
            byte d=g1.chromosome[i];
            g1.chromosome[i]=g2.chromosome[i];
            g2.chromosome[i]=d;
        }
    }

    /**
        突然変異
        与えられた個数の染色体をランダムに変更します。
    */
    public void mutate(int n){
        for(int i=0;i<n;i++){
            int r=(int)(Math.random()*chromosome.length);
            chromosome[r]=getRandomChromosome();
        }
    }

    public String toString(){
        String str="";
        for(int i=0;i<chromosome.length;i++){
            String s="00"+Integer.toString((int)(chromosome[i]+Byte.MAX_VALUE),16);
            str=str+(s.substring(s.length()-2));
        }
    return str;
  }
}