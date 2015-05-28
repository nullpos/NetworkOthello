package learning;

import java.io.*;
import java.util.Iterator;

import util.QSort;

public class GA {

    Gene[] gene;      // 遺伝子集団
    Gene[] gene_tmp;  // 遺伝子集団の予備

    /**
    生成
     */
    public GA(Gene[] gene){
        this.gene=gene;
        gene_tmp=new Gene[gene.length];
    }

    /**
    エリート
    適応度が最も大きい遺伝子を返します。
    線形探索を行います。
     */
    public Gene getElite(){
        Gene elite=gene[0];
        double max=elite.getFitness();
        for(int i=1;i<gene.length;i++)
            if(gene[i].getFitness()>max){
                elite=gene[i];
                max=elite.getFitness();
            }
        return elite;
    }

    /**
    N者トーナメント方式による選択
    ランダムにN個の遺伝子を選んで、その中で最も適応度の
    大きなものを複製して返します。
     */
    Gene doTournamentSelect(int n){
        Gene max_gene=null;
        double max=Double.NEGATIVE_INFINITY;
        for(int i=0;i<n;i++){
            int r=(int)(Math.random()*gene.length);
            if(gene[r].getFitness()>max){
                max_gene=gene[r];
                max=max_gene.getFitness();
            }
        }
        return (Gene)max_gene.clone();
    }

    /**
    選択
    エリート保存、N者トーナメント方式の選択を行います。
    選択処理を行うと、遺伝子集団のコピー操作が
    行われますが、コピーの処理を省くために、
    仮の遺伝子集団に対して選択を行い、
    最後に遺伝集団の入れ替えを行います。
    これにより、それまで遺伝子集団だった配列は
    仮の集団として使われることになりますから、
    元のプログラムでも遺伝子集団の入れ替えが
    必要になります。
    このメソッドは新しい遺伝子集団を返しますので、
    その集団に置き換えます。
     */
    public Gene[] select(int n){
        gene_tmp[0]=getElite();
        for(int i=1;i<gene.length;i++)
            gene_tmp[i]=doTournamentSelect(n);
        Gene[] d=gene;
        gene=gene_tmp;
        gene_tmp=d;
        return gene;
    }

    /**
    交叉
    エリート保存交叉を行います。
    遺伝子集団を適応度の順に並べ替え、0番（先頭）を除いて、
    1とN-1、2とN-2のように交叉します。
    Nが偶数の場合、先頭以外に中央の遺伝子も交叉されません。
     */
    public void crossover(int ty){
        QSort.sort(gene);
        int N=gene.length;
        switch(ty){
        case 0:
            for(int i=1;i<N/2;i++)
                Gene.onePointCrossover(gene[i],gene[N-i]);
            break;
        case 1:
            for(int i=1;i<N/2;i++)
                Gene.flatCrossover(gene[i],gene[N-i],0.5);
            break;
        }
    }

    /**
    突然変異
    遺伝子は適応度の大きい順に並んでいるものとします。
    エリートを除く遺伝子の中からランダムに n 個を選んで、
    その中の染色体を m 個だけランダムに変化させます。
     */
    public void mutate(int n,int m){
        for(int i=0;i<n;i++){
            int r=(int)(Math.random()*(gene.length-1))+1;
            gene[r].mutate(m);
        }
    }

    /**
    全遺伝子の表示
     */
    public void print(PrintStream ps){
        for(int i=0;i<gene.length;i++)
            ps.println("# "+i+":"+gene[i]+" "+gene[i].getFitness());
    }
    
    /**
     * 全遺伝子情報の書き出し
     */
    public void out(PrintStream ps){
        for(int i=0;i<gene.length;i++) {
            ps.print("# "+i+":"+gene[i]);
            for (int j = 0; j < gene[i].chromosome.length; j++) {
                ps.print(":" + gene[i].chromosome[j]);
            }
            ps.println(" "+gene[i].getFitness());
        }
    }
}