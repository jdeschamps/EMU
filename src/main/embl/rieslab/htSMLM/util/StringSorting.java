package main.embl.rieslab.htSMLM.util;


public class StringSorting{
	
    public static String[] sort(String[] input)
    {
        String temp;
        String[] sorted = input;
        
        if(input.length>1){
	        for(int i=0; i<input.length; i++){
	            for(int j=1; j<input.length; j++){
	            	if(sorted[j-1].compareTo(sorted[j])>0){
	                    temp=sorted[j-1];
	                    sorted[j-1]=sorted[j];
	                    sorted[j]=temp;
	                }
	            }
	        }
        }
		return sorted;
    }
}
