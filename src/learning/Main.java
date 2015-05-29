package learning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import util.Const;
import util.QSort;

public class Main {
    public static void main(String[] args) {
        int max_generation = 50; // 最大世代数
        int gene_num = Const.GENE_NUM; // 遺伝子集団の数
        int sel_num = (int) (max_generation * 0.2); // そのまま次世代へ残す優秀な遺伝子の数
        int chromo_num = Const.CHROMO_NUM; // chromosomeの数
        int crossover_type = 1; // 交叉の方法
        int[] mutate = {(int) (max_generation * 0.3), 6}; // 突然変異の方法
        byte[][] teachers = Const.GENE_TEACHER[3];
        
        Calendar now = Calendar.getInstance();
        String file_name = Const.GENE_DIR + 
                ((now.get(Calendar.HOUR_OF_DAY) < 10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : now.get(Calendar.HOUR_OF_DAY)) +"-"+ 
                ((now.get(Calendar.MINUTE) < 10) ? "0"+now.get(Calendar.MINUTE) : now.get(Calendar.MINUTE)) +"-"+
                ((now.get(Calendar.SECOND) < 10) ? "0"+now.get(Calendar.SECOND) : now.get(Calendar.SECOND)) + ".txt";
        
        PrintStream ps = null;
        try {
            FileOutputStream fos = new FileOutputStream(file_name, true);
            ps = new PrintStream(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ps.println("gen:" + max_generation + " genes:" + gene_num + " selNum:" + sel_num + 
                " chromoNum:" + chromo_num + " crossType:" + crossover_type + " mutate:" + mutate[0] + "," + mutate[1]);
        
        Gene[] g = new Gene[gene_num];
        for(int i=0; i<gene_num; i++) {
            g[i] = new Gene(chromo_num);
            g[i].initRandom();
        }
        
        // 教師遺伝子を作成
        
        Gene[] tg = new Gene[teachers.length];
        for(int i=0; i<teachers.length; i++) {
            tg[i] = new Gene(chromo_num);
            tg[i].setChromosome(teachers[i]);;
        }
        //*/
        
        (new File(file_name)).delete();
        GA ga = new GA(g);
        
        long start = System.currentTimeMillis();
        for(int i=0; i<max_generation; i++) {
            // i世代目
            System.out.printf("世代:%02d%n",i);
            
            // 対戦
            for(int j=0; j<gene_num; j++) {
                g[j].fitness = 0;
                
                // 固定された遺伝子と対戦する場合
                for(int k=0; k<teachers.length; k++) {
                    g[j].calcFitness(tg[k]);
                }
                /*/
                // 遺伝子総当りの場合
                for(int k=0; k<gene_num; k++) {
                    if(j == k) continue;
                    g[j].calcFitness(g[k]);
                }
                //*/
            }
            
            // 出力
            QSort.sort(ga.gene);
            System.out.println("優秀fitness:"+ga.getElite().getFitness());
            ps.printf("---------- %02d 世代 ----------%n", i);
            ga.out(ps);
            
            ga.gene = ga.select(sel_num);
            ga.crossover(crossover_type);
            ga.mutate(mutate[0], mutate[1]);
        }
        long end = System.currentTimeMillis();
        System.out.println("time: " + ((end - start) / 1000) + "s");
    }
}