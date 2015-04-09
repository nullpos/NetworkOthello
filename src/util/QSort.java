package util;

public class QSort {

  static int partition(Sortable[] a,int i,int j){
    int l=i,r=j;
    Sortable d=a[(i+j)/2];
    while(l<=r){
      while(l<=j && a[l].isBefore(d)) l++;
      while(r>=i && a[r].isAfter(d)) r--;
      if(l>r) break;
      Sortable t=a[l];
      a[l]=a[r];
      a[r]=t;
      l++; r--;
    }
    return l;
  }

  static void quickSort(Sortable[] a,int l,int r){
    if(l>=r) return;
    int k=partition(a,l,r);
    quickSort(a,l,k-1);
    quickSort(a,k,r);
  }

  public static void sort(Sortable[] a){
    quickSort(a,0,a.length-1);
  }
}