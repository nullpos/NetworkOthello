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
        int argv = 1;
        try {
            argv = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            argv = 1;
        }
        
        int max_generation = 50; // 最大世代数
        int gene_num = Const.GENE_NUM; // 遺伝子集団の数
        int chromo_num = Const.CHROMO_NUM; // chromosomeの数
        int crossover_type = 1; // 交叉の方法
        int sel_num = (int) (gene_num * 0.1); // ランダムでsel_num個の遺伝子を選び、複製した遺伝子列を作成する
        int[] mutate = {(int) (gene_num * 0.7), 6}; // 突然変異の方法
        byte[][] teachers = Const.GENE_TEACHER[1];
        
        Calendar now = Calendar.getInstance();
        String genes_file_name = Const.GENE_DIR + 
                ((now.get(Calendar.HOUR_OF_DAY) < 10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : now.get(Calendar.HOUR_OF_DAY)) +"-"+ 
                ((now.get(Calendar.MINUTE) < 10) ? "0"+now.get(Calendar.MINUTE) : now.get(Calendar.MINUTE)) +"-"+
                ((now.get(Calendar.SECOND) < 10) ? "0"+now.get(Calendar.SECOND) : now.get(Calendar.SECOND)) + ".txt";

        PrintStream ps = null;
        try {
            FileOutputStream fos = new FileOutputStream(genes_file_name, true);
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
        
        (new File(genes_file_name)).delete();
        GA ga = new GA(g);
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
            long t = System.currentTimeMillis();
            System.out.println("  time: " + ((t - start) / 1000) + "s");
            
            ps.printf("---------- %02d 世代 ----------%n", i);
            QSort.sort(ga.gene);
            ga.out(ps);
            
            ga.gene = ga.select(sel_num);
            ga.crossover(crossover_type);
            ga.mutate(mutate[0], mutate[1]);
        }
        
        ps.println("---------- 最終世代 ----------");
        Gene elite = ga.getElite();
        ps.println(elite.getFitness());
        ps.println(elite.toString());
        ps.println(elite.getFitness()+elite.toString2());
        
        long end = System.currentTimeMillis();
        System.out.println("time: " + ((end - start) / 1000) + "s");
        
        if (argv > 1) {
            args[0] = Integer.toString(argv-1);
            main(args);
        }
    }
}