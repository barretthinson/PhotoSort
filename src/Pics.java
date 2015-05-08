package photosort;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class Pics {
	private final String name;
	private final String dateTaken;
	private final String path;
	private final int year;
	private final int month;
	
	public Pics(File file){
		name = file.getName();
		dateTaken = findDate(file);
		path = file.getPath();
		if(dateTaken != null){
			String delims = "[ :\\/]+";
			String[] parts = dateTaken.split(delims);
			year = Integer.parseInt(parts[0]);
			month = Integer.parseInt(parts[1]);
		}else{
			year = 0;
			month = 0;
		}
	}

	private String findDate(File file) {
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(file);
			Directory exifD = metadata.getDirectory(com.drew.metadata.exif.ExifIFD0Directory.class);
			Directory exifDS = metadata.getDirectory(com.drew.metadata.exif.ExifSubIFDDirectory.class);
			if(exifDS != null){
				for(Tag t: exifDS.getTags()){
					if(t.getTagName().contains("Date")){
						return t.getDescription();
					}
				}
			}
			if(exifD != null){
				for(Tag t: exifD.getTags()){
					if(t.getTagName().contains("Date")){
						return t.getDescription();
					}
				}
			}
			return null;
		} catch (JpegProcessingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public String getName(){
		return name;
	}
	
	public String getDateTaken(){
		return dateTaken;
	}
	
	public String getPath(){
		return path;
	}
	
	public int getYear(){
		return year;
	}
	
	public int getMonth(){
		return month;
	}
}
