/******
File:       ArrayIndexComparator.java
Author:     Somayeh Kafaie
Date:       October, 2017
Purpose:    compare values in an array
******/

package Filtering;

import java.util.*;


public class ArrayIndexComparator implements Comparator<Integer>
{
    private final double[] array;

    public ArrayIndexComparator(double[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
    	if (array[index1] == array[index2])
    		return 0;
    	else if (array[index1] > array[index2])
    		return 1;
    	else
    		return -1;
    }
}
