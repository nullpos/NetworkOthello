package learning;

import application.*;

public class Main {
    public static void main(String[] args) {
        int max_generation = 100; // 最大世代数
        int gene_num = 20; // 遺伝子集団の数
        int sel_num = 5; // そのまま次世代へ残す優秀な遺伝子の数
        int chromo_num = 20; // chromosomeの数
        int crossover_type = 0; // 交叉の方法
        
        Othello othello;
        
        Gene[] g = new Gene[gene_num];
        for(int i=0; i<gene_num; i++) {
            g[i] = new Gene(chromo_num);
        }
        
        GA ga = new GA(g);
        
        for(int i=0; i<max_generation; i++) {
            
            ga.select(5);
            ga.crossover(1);
            ga.mutate(n, m);
        }
    }
}