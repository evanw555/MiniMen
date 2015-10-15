import java.util.ArrayList;

public class Util {
	private static ArrayList<String> listF;
    private static ArrayList<String> listS;
    
	public static int randomIntInclusive(int a, int b){
		assert b >= a;
		return (int)(Math.random()*(b-a+1))+a;
	}
	
	public static void readNames(){
		listF = new ArrayList<String>();
		listS = new ArrayList<String>();
		String data;
        FileInput inF = new FileInput("data/firstnames.txt");
        FileInput inS = new FileInput("data/surnames.txt");
        listF = new ArrayList<String>();
        listS = new ArrayList<String>();
        
        int numF = -1;
        do{
        	numF++;
	        data = inF.readLine();
	        if(data.length() > 0)
	            listF.add(data);
	    }while(!inF.eof());
        //System.out.println(numF + " first names, list size " + listF.size());
        int numS = -1;
        do{
            numS++;
            data = inS.readLine();
            if(data.length() > 0)
                listS.add(data);
        }while(!inS.eof());
        //System.out.println(numS + " surnames, list size " + listS.size());
	}
	
	public static String generateFirstname(){
		return listF.get((int)(Math.random()*listF.size()));
	}
	
	public static String generateSurname(){
		return listS.get((int)(Math.random()*listS.size()));
	}
}