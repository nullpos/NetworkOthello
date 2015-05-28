package learning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import util.Const;
import util.QSort;

public class Main {
    public static void main(String[] args) {
        int max_generation = 20; // 最大世代数
        int gene_num = Const.GENE_NUM; // 遺伝子集団の数
        int sel_num = 3; // そのまま次世代へ残す優秀な遺伝子の数
        int chromo_num = Const.CHROMO_NUM; // chromosomeの数
        int crossover_type = 1; // 交叉の方法
        
        Gene[] g = new Gene[gene_num];
        for(int i=0; i<gene_num; i++) {
            g[i] = new Gene(chromo_num);
            g[i].initRandom();
        }
        
        (new File(Const.GENE_FILE)).delete();
        GA ga = new GA(g);
        
        for(int i=0; i<max_generation; i++) {
            // i世代目
            System.out.printf("世代:%02d%n",i);
            
            // 対戦
            for(int j=0; j<gene_num; j++) {
                for(int k=0; k<gene_num; k++) {
                    if(j == k) continue;
                    g[j].calcFitness(g[k]);
                }
            }
            
            // 出力
            QSort.sort(g);
            try {
                FileOutputStream fos = new FileOutputStream(Const.GENE_FILE, true);
                PrintStream ps = new PrintStream(fos);
                ps.printf("---------- %02d 世代 ----------%n", i);
                ga.print(ps);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            ga.select(sel_num);
            ga.crossover(crossover_type);
            ga.mutate(2, 2);
        }
    }
}