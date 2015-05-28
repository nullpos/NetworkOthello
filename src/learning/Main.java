package learning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import util.Const;
import util.QSort;

public class Main {
    public static void main(String[] args) {
        int max_generation = 20; // 最大世代数
        int gene_num = Const.GENE_NUM; // 遺伝子集団の数
        int sel_num = 3; // そのまま次世代へ残す優秀な遺伝子の数
        int chromo_num = Const.CHROMO_NUM; // chromosomeの数
        int crossover_type = 1; // 交叉の方法
        Calendar now = Calendar.getInstance();
        String file_name = Const.GENE_DIR + ((now.get(Calendar.HOUR_OF_DAY) < 10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : now.get(Calendar.HOUR_OF_DAY)) +"-"+ 
                now.get(Calendar.MINUTE) +"-"+ now.get(Calendar.SECOND) + ".txt";
        
        PrintStream ps = null;
        try {
            FileOutputStream fos = new FileOutputStream(file_name, true);
            ps = new PrintStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ps.println("gen:" + max_generation + "genes:" + gene_num + "selNum:" + sel_num + "chromoNum:" + chromo_num + "crossType" + crossover_type);
        
        Gene[] g = new Gene[gene_num];
        for(int i=0; i<gene_num; i++) {
            g[i] = new Gene(chromo_num);
            g[i].initRandom();
        }
        
        (new File(file_name)).delete();
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
            System.out.println("  優秀fitness:"+g[0].getFitness());
            ps.printf("---------- %02d 世代 ----------%n", i);
            ga.out(ps);
            
            ga.select(sel_num);
            ga.crossover(crossover_type);
            ga.mutate(2, 2);
        }
    }
}