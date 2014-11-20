package org.arong.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * ArrayUtil.java
 *
 * @desc 数组操作工具
 * @author Guoxp,dipoo
 * @datatime Apr 7, 2013 4:03:49 PM
 *
 */
public class ArrayUtil { 
   
    /**
    * 排序算法的分类如下：
    * 1.插入排序（直接插入排序、折半插入排序、希尔排序）；
    * 2.交换排序（冒泡泡排序、快速排序）；
    * 3.选择排序（直接选择排序、堆排序）； 
    * 4.归并排序； 
    * 5.基数排序。
    *
    * 关于排序方法的选择：
    * (1)若n较小(如n≤50)，可采用直接插入或直接选择排序。
    * (2)若文件初始状态基本有序(指正序)，则应选用直接插人、冒泡或随机的快速排序为宜；
    * (3)若n较大，则应采用时间复杂度为O(nlgn)的排序方法：快速排序、堆排序或归并排序。
    *
    */ 
       
     /**
     * 交换数组中两元素
     *
     * @since 1.1
     * @param ints
     *            需要进行交换操作的数组
     * @param x
     *            数组中的位置1
     * @param y
     *            数组中的位置2
     * @return 交换后的数组
     */ 
    public static int[] swap(int[] ints, int x, int y) { 
        int temp = ints[x];
        ints[x] = ints[y]; 
        ints[y] = temp; 
        return ints; 
    } 
   
    /**
     * 冒泡排序 方法：相邻两元素进行比较 性能：比较次数O(n^2),n^2/2；交换次数O(n^2),n^2/4
     *
     * @since 1.1
     * @param source
     *            需要进行排序操作的数组
     * @return 排序后的数组
     */ 
    public static int[] bubbleSort(int[] source) { 
        for (int i = 1; i < source.length; i++) { 
            for (int j = 0; j < i; j++) { 
                if (source[j] > source[j + 1]) { 
                    swap(source, j, j + 1); 
                } 
            } 
        } 
        return source; 
    } 
   
    /**
     * 直接选择排序法 方法：每一趟从待排序的数据元素中选出最小（或最大）的一个元素， 顺序放在已排好序的数列的最后，直到全部待排序的数据元素排完。
     * 性能：比较次数O(n^2),n^2/2 交换次数O(n),n
     * 交换次数比冒泡排序少多了，由于交换所需CPU时间比比较所需的CUP时间多，所以选择排序比冒泡排序快。
     * 但是N比较大时，比较所需的CPU时间占主要地位，所以这时的性能和冒泡排序差不太多，但毫无疑问肯定要快些。
     *
     * @since 1.1
     * @param source
     *            需要进行排序操作的数组
     * @return 排序后的数组
     */ 
    public static int[] selectSort(int[] source) { 
   
        for (int i = 0; i < source.length; i++) { 
            for (int j = i + 1; j < source.length; j++) { 
                if (source[i] > source[j]) { 
                    swap(source, i, j); 
                } 
            } 
        } 
        return source; 
    } 
   
    /**
     * 插入排序 方法：将一个记录插入到已排好序的有序表（有可能是空表）中,从而得到一个新的记录数增1的有序表。 性能：比较次数O(n^2),n^2/4
     * 复制次数O(n),n^2/4 比较次数是前两者的一般，而复制所需的CPU时间较交换少，所以性能上比冒泡排序提高一倍多，而比选择排序也要快。
     *
     * @since 1.1
     * @param source
     *            需要进行排序操作的数组
     * @return 排序后的数组
     */ 
    public static int[] insertSort(int[] source) { 
   
        for (int i = 1; i < source.length; i++) { 
            for (int j = i; (j > 0) && (source[j] < source[j - 1]); j--) { 
                swap(source, j, j - 1); 
            } 
        } 
        return source; 
    } 
   
    /**
     * 快速排序 快速排序使用分治法（Divide and conquer）策略来把一个序列（list）分为两个子序列（sub-lists）。 步骤为：
     * 1. 从数列中挑出一个元素，称为 "基准"（pivot）， 2.
     * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面
     * （相同的数可以到任一边）。在这个分割之后，该基准是它的最后位置。这个称为分割（partition）操作。 3.
     * 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
     * 递回的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了
     * 。虽然一直递回下去，但是这个算法总会结束，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。
     *
     * @since 1.1
     * @param source
     *            需要进行排序操作的数组
     * @return 排序后的数组
     */ 
    public static int[] quickSort(int[] source) { 
        return qsort(source, 0, source.length - 1); 
    } 
   
    /**
     * 快速排序的具体实现，排正序
     *
     * @since 1.1
     * @param source
     *            需要进行排序操作的数组
     * @param low
     *            开始低位
     * @param high
     *            结束高位
     * @return 排序后的数组
     */ 
    private static int[] qsort(int source[], int low, int high) { 
        int i, j, x; 
        if (low < high) { 
            i = low; 
            j = high; 
            x = source[i]; 
            while (i < j) { 
                while (i < j && source[j] > x) { 
                    j--; 
                } 
                if (i < j) { 
                    source[i] = source[j]; 
                    i++; 
                } 
                while (i < j && source[i] < x) { 
                    i++; 
                } 
                if (i < j) { 
                    source[j] = source[i]; 
                    j--; 
                } 
            } 
            source[i] = x; 
            qsort(source, low, i - 1); 
            qsort(source, i + 1, high); 
        } 
        return source; 
    } 
    /////////////////////////////////////////////// 
    //排序算法结束 
    ////////////////////////////////////////////// 
    /**
     * 二分法查找 查找线性表必须是有序列表
     *
     * @since 1.1
     * @param source
     *            需要进行查找操作的数组
     * @param key
     *            需要查找的值
     * @return 需要查找的值在数组中的位置，若未查到则返回-1
     */ 
    public static int binarySearch(int[] source, int key) { 
        int low = 0, high = source.length - 1, mid; 
        while (low <= high) { 
            mid = (low + high) >>> 1; 
            if (key == source[mid]) { 
                return mid; 
            } else if (key < source[mid]) { 
                high = mid - 1; 
            } else { 
                low = mid + 1; 
            } 
        } 
        return -1; 
    } 
   
    /**
     * 反转数组
     *
     * @since 1.1
     * @param source
     *            需要进行反转操作的数组
     * @return 反转后的数组
     */ 
    public static int[] reverse(int[] source) { 
        int length = source.length; 
        int temp = 0; 
        for (int i = 0; i < length>>1; i++) { 
            temp = source[i]; 
            source[i] = source[length - 1 - i]; 
            source[length - 1 - i] = temp; 
        } 
        return source; 
    } 
   /**
    * 在当前位置插入一个元素,数组中原有元素向后移动;
    * 如果插入位置超出原数组，则抛IllegalArgumentException异常
    * @param array
    * @param index
    * @param insertNumber
    * @return
    */ 
    public static int[] insert(int[] array, int index, int insertNumber) { 
        if (array == null || array.length == 0) { 
            throw new IllegalArgumentException(); 
        } 
        if (index-1 > array.length || index <= 0) { 
            throw new IllegalArgumentException(); 
        } 
        int[] dest=new int[array.length+1]; 
        System.arraycopy(array, 0, dest, 0, index-1); 
        dest[index-1]=insertNumber; 
        System.arraycopy(array, index-1, dest, index, dest.length-index); 
        return dest; 
    } 
       
    /**
     * 整形数组中特定位置删除掉一个元素,数组中原有元素向前移动;
     * 如果插入位置超出原数组，则抛IllegalArgumentException异常
     * @param array
     * @param index
     * @return
     */ 
    public static int[] remove(int[] array, int index) { 
        if (array == null || array.length == 0) { 
            throw new IllegalArgumentException(); 
        } 
        if (index > array.length || index <= 0) { 
            throw new IllegalArgumentException(); 
        } 
        int[] dest=new int[array.length-1]; 
        System.arraycopy(array, 0, dest, 0, index-1); 
        System.arraycopy(array, index, dest, index-1, array.length-index); 
        return dest; 
    } 
    /**
     * 2个数组合并，形成一个新的数组
     * @param array1
     * @param array2
     * @return
     */ 
    public static int[] merge(int[] array1,int[] array2) { 
        int[] dest=new int[array1.length+array2.length]; 
        System.arraycopy(array1, 0, dest, 0, array1.length); 
        System.arraycopy(array2, 0, dest, array1.length, array2.length); 
        return dest; 
    } 
   
/**
     * 数组中有n个数据，要将它们顺序循环向后移动k位，
     * 即前面的元素向后移动k位，后面的元素则循环向前移k位，
     * 例如，0、1、2、3、4循环移动3位后为2、3、4、0、1。
     * @param array
     * @param offset
     * @return
     */ 
    public static int[] offsetArray(int[] array,int offset){ 
        int length = array.length;   
        int moveLength = length - offset;  
        int[] temp = Arrays.copyOfRange(array, moveLength, length); 
        System.arraycopy(array, 0, array, offset, moveLength);   
        System.arraycopy(temp, 0, array, 0, offset); 
        return array; 
    } 
    /**
     * 随机打乱一个数组
     * @param list
     * @return
     */ 
    public static List shuffle(List list){ 
        java.util.Collections.shuffle(list); 
        return list; 
    } 
   
    /**
     * 随机打乱一个数组
     * @param array
     * @return
     */ 
    public int[] shuffle(int[] array) { 
        Random random = new Random(); 
        for (int index = array.length - 1; index >= 0; index--) { 
            // 从0到index处之间随机取一个值，跟index处的元素交换 
            exchange(array, random.nextInt(index + 1), index); 
        } 
        return array; 
    } 
   
    // 交换位置 
    private void exchange(int[] array, int p1, int p2) { 
        int temp = array[p1]; 
        array[p1] = array[p2]; 
        array[p2] = temp; 
    } 
    /**
     * 对两个有序数组进行合并,并将重复的数字将其去掉
     * 
     * @param a：已排好序的数组a
     * @param b：已排好序的数组b
     * @return 合并后的排序数组
     */ 
    private static List<Integer> mergeByList(int[] a, int[] b) { 
        // 用于返回的新数组，长度可能不为a,b数组之和，因为可能有重复的数字需要去掉 
        List<Integer> c = new ArrayList<Integer>(); 
        // a数组下标 
        int aIndex = 0; 
        // b数组下标 
        int bIndex = 0; 
        // 对a、b两数组的值进行比较，并将小的值加到c，并将该数组下标+1， 
        // 如果相等，则将其任意一个加到c，两数组下标均+1 
        // 如果下标超出该数组长度，则退出循环 
        while (true) { 
            if (aIndex > a.length - 1 || bIndex > b.length - 1) { 
                break; 
            } 
            if (a[aIndex] < b[bIndex]) { 
                c.add(a[aIndex]); 
                aIndex++; 
            } else if (a[aIndex] > b[bIndex]) { 
                c.add(b[bIndex]); 
                bIndex++; 
            } else { 
                c.add(a[aIndex]); 
                aIndex++; 
                bIndex++; 
            } 
        } 
        // 将没有超出数组下标的数组其余全部加到数组c中 
        // 如果a数组还有数字没有处理 
        if (aIndex <= a.length - 1) { 
            for (int i = aIndex; i <= a.length - 1; i++) { 
                c.add(a[i]); 
            } 
            // 如果b数组中还有数字没有处理 
        } else if (bIndex <= b.length - 1) { 
            for (int i = bIndex; i <= b.length - 1; i++) { 
                c.add(b[i]); 
            } 
        } 
        return c; 
    } 
    /**
     * 对两个有序数组进行合并,并将重复的数字将其去掉
     * @param a:已排好序的数组a
     * @param b:已排好序的数组b
     * @return合并后的排序数组,返回数组的长度=a.length + b.length,不足部分补0
     */ 
    private static int[] mergeByArray(int[] a, int[] b){ 
        int[] c = new int[a.length + b.length]; 
   
        int i = 0, j = 0, k = 0; 
   
        while (i < a.length && j < b.length) { 
            if (a[i] <= b[j]) { 
                if (a[i] == b[j]) { 
                    j++; 
                } else { 
                    c[k] = a[i]; 
                    i++; 
                    k++; 
                } 
            } else { 
                c[k] = b[j]; 
                j++; 
                k++; 
            } 
        } 
        while (i < a.length) { 
            c[k] = a[i]; 
            k++; 
            i++; 
        } 
        while (j < b.length) { 
            c[k] = b[j]; 
            j++; 
            k++; 
        } 
        return c; 
    } 
    /**
     * 对两个有序数组进行合并,并将重复的数字将其去掉
     * @param a：可以是没有排序的数组
     * @param b：可以是没有排序的数组
     * @return合并后的排序数组
     * 打印时可以这样：
     * Map<Integer,Integer> map=sortByTreeMap(a,b);
        Iterator iterator =  map.entrySet().iterator();   
        while (iterator.hasNext()) {   
           Map.Entry mapentry = (Map.Entry)iterator.next();   
           System.out.print(mapentry.getValue()+" ");   
        }
     */ 
    private static Map<Integer,Integer> mergeByTreeMap(int[] a, int[] b) { 
        Map<Integer,Integer> map=new TreeMap<Integer,Integer>(); 
        for(int i=0;i<a.length;i++){ 
            map.put(a[i], a[i]); 
        } 
        for(int i=0;i<b.length;i++){ 
            map.put(b[i], b[i]); 
        } 
        return map; 
    } 
    /**
     * 在控制台打印数组，之间用逗号隔开,调试时用
     * @param array
     */ 
    public static String print(Object[] array){ 
        StringBuffer sb=new StringBuffer(); 
        for(int i=0;i<array.length;i++){ 
            sb.append(","+array[i]); 
        } 
        System.out.println(sb.toString().substring(1)); 
        return sb.toString().substring(1); 
    } 
    
    /*字符串数组操作*/
    /**
     * 指定的数据中是否存在指定的字符串
     * @param arr
     * @param s
     */
    public static boolean exists(String[] arr, String s){
    	if(arr != null && s != null){
	    	for(int i = 0; i < arr.length; i ++){
	    		if(s.equals(arr[i]))
	    			return true;
	    	}
    	}	
    	return false;
    }
    
    
    /*public static void main(String[] args){ 
        ArrayUtil util=new ArrayUtil(); 
        int[] array0={21,24,13,46,35,26,14,43,11}; 
        int[] array1={1,2,3,4,5,6}; 
        int[] array2={11,22,33,44,55,66}; 
        int[] temp=util.quickSort(array0); 
        print(temp); 
    } */
}
