package photosort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Sorter {
	
	public static void main(String[] args){
		File[] files = getFiles();
		ArrayList<Pics> pics = makeListPics(files);
		HashMap<Integer, HashMap<Integer, ArrayList<Pics>>> sorted = sort(pics);
		putInFolders(sorted);
	}
	
	private static void putInFolders(
			HashMap<Integer, HashMap<Integer, ArrayList<Pics>>> sorted) {
		
		File sortedPics = new File("sortedPics/");
		if(!sortedPics.exists()){
			sortedPics.mkdir();
		}
		
		File nullMonth = new File("sortedPics/0-0/");
		if(!nullMonth.exists()){
			nullMonth.mkdir();
		}
		
		for(int y: sorted.keySet()){
			for(int m: sorted.get(y).keySet()){
				File monthFolder = new File("sortedPics/"+y+"-"+m+"/");
				if(!monthFolder.exists()){
					monthFolder.mkdir();
				}
				for(Pics p: sorted.get(y).get(m)){
					File pic = new File(p.getPath());
					if(pic.renameTo(new File(monthFolder.getPath()+"/"+pic.getName()))){
						System.out.println("successfully moved: "+ pic.getName());
					}else{
						System.out.println("ERROR-unable to sort: "+pic.getName());
						pic.renameTo(new File(nullMonth.getPath()+"/"+pic.getName()));
					}
				}
			}
		}
	}

	private static File[] getFiles(){
		File folder = new File("unsorted/");
		if(!folder.exists()){
			folder.mkdir();
		}
		return folder.listFiles();
	}
	
	private static ArrayList<Pics> makeListPics(File[] files){
		ArrayList<Pics> pics = new ArrayList<Pics>();
		for(File file: files){
			pics.add(new Pics(file));
		}
		return pics;
	}
	
	private static HashMap<Integer, HashMap<Integer, ArrayList<Pics>>> sort(ArrayList<Pics> pics) {
		HashMap<Integer, HashMap<Integer, ArrayList<Pics>>> years = 
				new HashMap<Integer, HashMap<Integer, ArrayList<Pics>>>();
		
		for(Pics p: pics){
			if(years.containsKey(p.getYear())){
				HashMap<Integer, ArrayList<Pics>> months = years.get(p.getYear());
				if(months.containsKey(p.getMonth())){
					ArrayList<Pics> cluster = months.get(p.getMonth());
					cluster.add(p);
				}else{
					ArrayList<Pics> cluster = new ArrayList<Pics>();
					cluster.add(p);
					months.put(p.getMonth(), cluster);
				}
			}else{
				HashMap<Integer, ArrayList<Pics>> months = 
						new HashMap<Integer, ArrayList<Pics>>();
				ArrayList<Pics> cluster = new ArrayList<Pics>();
				cluster.add(p);
				months.put(p.getMonth(), cluster);
				years.put(p.getYear(), months);
			}
		}
		
		return years;
	}
	
	/*private static void test(File f){
		File jpegFile = new File(f.getPath());
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
			
			for(Directory d: metadata.getDirectories()){
				System.out.println("  "+d.getName());
			    for(Tag t: d.getTags()){
			    	System.out.println("     "+ t.getTagName()+ "  "+t.getDescription());
			    }
			}
		} catch (JpegProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}*/
}
