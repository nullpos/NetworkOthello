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
        int max_generation = 30; // 最大世代数
        int gene_num = Const.GENE_NUM; // 遺伝子集団の数
        int chromo_num = Const.CHROMO_NUM; // chromosomeの数
        int crossover_type = 1; // 交叉の方法
        int sel_num = (int) (gene_num * 0.1); // ランダムでsel_num個の遺伝子を選び、複製した遺伝子列を作成する
        int[] mutate = {(int) (gene_num * 0.5), 6}; // 突然変異の方法
        byte[][] teachers = Const.GENE_TEACHER[4];
        
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
            tg[i].setChromosome(teachers[i]);
        }
        //*/
        
        (new File(file_name)).delete();
        GA ga = new GA(g);
        double tmp = Double.NEGATIVE_INFINITY;
        long start = System.currentTimeMillis();
        for(int i=0; i<max_generation; i++) {
            // i世代目
            System.out.printf("世代:%02d%n",i);
            
            // 対戦
            for(int j=0; j<gene_num; j++) {
                ga.gene[j].fitness = 0;
                // 固定された遺伝子と対戦する場合
                for(int k=0; k<teachers.length; k++) {
                    ga.gene[j].calcFitness(tg[k]);
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
            System.out.println("  最高適合度:"+ga.getElite().getFitness());
            ps.printf("---------- %02d 世代 ----------%n", i);
            QSort.sort(ga.gene);
            ga.out(ps);
            
            ga.gene = ga.select(sel_num);
            ga.crossover(crossover_type);
            ga.mutate(mutate[0], mutate[1]);
            
            if(tmp > ga.getElite().getFitness()) {
                System.err.println("Why smaaaaaaaal!!!!");
                System.exit(-1);
            }
            tmp = ga.getElite().getFitness();
        }
        
        ps.println("---------- 最終世代 ----------");
        Gene elite = ga.getElite();
        ps.println(elite.getFitness());
        ps.println(elite.toString());
        ps.print("{"+elite.chromosome[0]);
        for (int j = 1; j < elite.chromosome.length; j++) {
            ps.print(", " + elite.chromosome[j]);
        }
        ps.println("}");
        
        long end = System.currentTimeMillis();
        System.out.println("time: " + ((end - start) / 1000) + "s");
    }
}