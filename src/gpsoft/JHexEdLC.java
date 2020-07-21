package gpsoft;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * <p>Title: JHexEdLC</p>
 * <p>Description: Java Hex editor Line Command</p>
 * <p>Copyright: Copyright (c) 2020</p>
 * <p>Company: GPSoft</p>
 * @author Giovanni Palleschi
 * @version 1.0
 */

public class JHexEdLC {
    
	enum opeType {
		  INPUT,
		  UPDATE,
		  DELETE,
		  NODEFINED
	}
	
	static opeType mode = opeType.NODEFINED;
	
	static String version = "1.0";
	static String years="21/07/2020";
	static String creator="GPSoft By GNNK71";

	static String inputFile = null;
	static long lengthInputFile = -1;
	private static RandomAccessFile rafi;
	static String outputFile = null;
	static long offsetFrom = -1;
	static long offsetTo = -1;
	static String hexValue = null;
	

	
	public static String getVersion() {
	   return version;
	}

	public static String getYears() {
		return years;
	}

	public static String getCreator() {
		return creator;
	}
	
	public static void displayHeader() {
		System.out.println("\nJAsn1LC version " + getVersion() + " (" + getYears()   + ") " + getCreator() + "\n");
	}
	
	private static void displayHelp() {
		displayHeader();
        System.out.println("This is a tool to edit binary and not binary files, permits to insert,update a value in a specific offset or delete a portion of file.\n");
        System.out.println("Use: java -jar JHexEdLC.jar <Input File> [-h] [[-i] [-d] [-u]] [-h] [-b<offset start>] [-e<offset end>] [-v<value in hex format>] [-o<Output File>]\n");
        System.out.println("[...] are optional parameters\n");
        System.out.println("<Input File>          : input file to edit");
        System.out.println("[-h]                  : display help");
        System.out.println("[-i]                  : insert a value (specify -b parameter and -v for the value)");
        System.out.println("[-d]                  : delete a value (specify both -d and -e parameters)");                
        System.out.println("[-u]                  : update a value (specify both -d and -e parameters and -v for the value with the same length)"); 
        System.out.println("[-b<offset start>]    : offset from");
        System.out.println("[-e<offset end>]      : offset to");
        System.out.println("[-v<value>]           : value to update or insert");
        System.out.println("[-h]                  : show this help");
        return;
	}	
	
	private static void endPrg(int code) {
		System.exit(code);
	}
	

	private static final int BUFFER_SIZE = 4096; // 4KB
	 
	public static void elabFile() {
		long byteReaded = 0;
		byte[] bytesToWrite = null;
        int byteRead;
        boolean bWrite = true;
        try (
              InputStream inputStream = new FileInputStream(inputFile);
              OutputStream outputStream = new FileOutputStream(outputFile);
            ) {
                while ((byteRead = inputStream.read()) != -1) {
                	byteReaded++;	
                    bWrite = true;
                	// DELETE
                    if ( mode == opeType.DELETE && byteReaded >= offsetFrom && byteReaded <= offsetTo ) {
                		bWrite = false;
                	}
                	// UPDATE
                    if ( (mode == opeType.UPDATE || mode == opeType.INPUT) && offsetFrom == byteReaded ) {
                 	   bytesToWrite = Utility.hexStringToByteArray(hexValue);
                	   if ( bytesToWrite.length <= 0 ) {
                			System.out.println("Error in hexStringToByteArray for String <" + hexValue + ">");
                			inputStream.close();
                			outputStream.close();
                			endPrg(1);
                	   }       
                	   outputStream.write(bytesToWrite,0,bytesToWrite.length);
                    }
                    
                    if ( mode == opeType.UPDATE && byteReaded >= offsetFrom && byteReaded < (offsetFrom + hexValue.length()/2) ) {
                		bWrite = false;
                    }
                	
                	if ( bWrite ) outputStream.write(byteRead);
              }
              inputStream.close();
              outputStream.close();               
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

	public static void main(String[] args) throws IOException {

		boolean bHelp = false;
	    boolean bErr = false;
	    boolean bNoOutFile = true;
		
        if ( args.length == 0 )
        {
            bHelp = true;
        	displayHelp();
        }
        
	    for(String arg : args) 
    	{
	    	// Modality : i or u or d
	    	
	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-i") == 0 ) {
	    		if ( mode != opeType.NODEFINED ) {
	    		  System.out.println("\n Permitted only one moodality -i or -o or -d.\n\n");
	    		  bErr = true;
	              break;		  
	    		}
	    		mode = opeType.INPUT;
	    		continue;
	    	}

	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-d") == 0 ) {
	    		if ( mode != opeType.NODEFINED ) {
	    		  System.out.println("\n Permitted only one moodality -i or -o or -d.\n\n");
	    		  bErr = true;
	              break;		  
	    		}
	    		mode = opeType.DELETE;
	    		continue;
	    	}
	    	
	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-u") == 0 ) {
	    		if ( mode != opeType.NODEFINED ) {
	    		  System.out.println("\n Permitted only one moodality -i or -o or -d.\n\n");
	    		  bErr = true;
	              break;		  
	    		}
	    		mode = opeType.UPDATE;
	    		continue;
	    	}
	    	
	    	// Value
	    	if ( arg.length() > 1 && arg.substring(0,2).compareTo("-v") == 0 ) {
	    		
	    		if ( arg.length() == 2 ) {
	    		   System.out.println("\n Specify an hexadecimal value after -v parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		
	    		if ( Utility.isHexadecimal(arg.substring(2)) == false ) {
		    	   System.out.println("\n Value <" + arg.substring(2) + "> not in hexadecimal format.\n\n");
	    		   bErr = true;
		           break;		    			
	    		}
	    		
	    		if ( arg.substring(2).length()%2 != 0 ) {
	    		  hexValue = "0" + arg.substring(2);
	    		} else {
	    		  hexValue = arg.substring(2);
	    		}
	    		
	    		continue;
	    	}

	    	// OffSet From 
	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-b") == 0 ) {
	    	
	    		if ( arg.length() == 2 ) {
	    		   System.out.println("\n Specify a offset value after -b parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		
	    		if ( Utility.isNumeric(arg.substring(2)) == false ) {
	    		   System.out.println("\n Specify a valid offset value after -b parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		
	    		offsetFrom = Long.parseLong(arg.substring(2));
	    		continue;
	    	}

	    	// OffSet To 
	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-e") == 0 ) {
	    	
	    		if ( arg.length() == 2 ) {
	    		   System.out.println("\n Specify a offset value after -e parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		
	    		if ( Utility.isNumeric(arg.substring(2)) == false ) {
	    		   System.out.println("\n Specify a valid offset value after -e parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		
	    		offsetTo = Long.parseLong(arg.substring(2));
	    		continue;
	    	}	    	
	    	
	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-h") == 0 ) {
	            bHelp = true;
	        	displayHelp();
	        	break;
	    	}

	    	if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-o") == 0 ) {

	    		if ( arg.length() == 2 ) {
	    		   System.out.println("\n Specify an output file name after -o parameter.\n\n");
	    		   bErr = true;
	               break;		  
	    		}
	    		outputFile = arg.substring(2);
	    		continue;
	    	}
	    	
	    	if ( Utility.isFile(arg) == false ) {
	    		   System.out.println("\n File Specify <" + arg + " is not a valid file.\n\n");
	    		   bErr = true;
	    		   break;
	    	}
	    	
	    	inputFile = arg;
    	}
	    
	    if ( bErr == false && bHelp == false ) {
	    	// Check if modality is specified 
	    	if ( mode == opeType.NODEFINED ) {
               System.out.println("\n No Specified Modality : -i or -o or -d.\n\n");
	    	   endPrg(1);	
	    	}
	    	
	    	// input file no specified
	    	if ( inputFile == null ) {
               System.out.println("\n No Specified Input File.\n\n");
	    	   endPrg(1);	
	    	} else {
	    		
	    		try {
					rafi = new RandomAccessFile(inputFile,"rw");
					lengthInputFile = rafi.length();
					rafi.close();
				} catch (FileNotFoundException e) {
                    System.out.println("\n Error opening input file <" + inputFile + ">.\n\n");
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	if ( offsetFrom != -1 && offsetTo != -1 ) {
	    	    if ( offsetTo < offsetFrom ) {
                  System.out.println("\n Specified offset from greater than offset to.\n\n");
	    		  endPrg(1);
	    	    }
	    	    
	    	    if ( offsetTo == offsetFrom ) {
                  System.out.println("\n Specified offset from equal to offset to.\n\n");
	    		  endPrg(1);
	    	    }
	    	}
	    	
	    	if ( offsetFrom != -1 && offsetFrom > lengthInputFile ) {
               System.out.println("\n Offset from <" + offsetFrom + "> is greater input file length <" + lengthInputFile + ">.\n\n");
	    	   endPrg(1);
	    	}
	    	
	    	if ( offsetTo != -1 && offsetTo > lengthInputFile ) {
               System.out.println("\n Offset to <" + offsetTo + "> is greater input file length <" + lengthInputFile + ">.\n\n");
	    	   endPrg(1);
	    	}
	    	
	    	if ( (mode == opeType.INPUT || mode == opeType.UPDATE) && offsetFrom == -1 ) {
               System.out.println("\n In INPUT or UPDATE modality specify Offset from.\n\n");
	    	   endPrg(1);
	    	}	
	    	
	    	if ( mode == opeType.UPDATE && ((offsetFrom + hexValue.length()/2)-1) > lengthInputFile ) {
	               System.out.println("\n In UPDATE modality specify a value length " + hexValue.length()/2 + " from offset " + offsetFrom + 
	            		              " than excedeed the input file length " + lengthInputFile + ".\n\n");
		    	   endPrg(1);
	    	}	

	    	// -d 
	    	if ( mode == opeType.DELETE && (offsetFrom == -1 || offsetTo == -1) ) {
               System.out.println("\n In DELETE modality is necessary specify both offset from and offset to.\n\n");
	    	   endPrg(1);	
	    	}
	    	
	    	if ( (mode == opeType.INPUT || mode == opeType.UPDATE) && hexValue == null ) {
               System.out.println("\n In INPUT or UPDATE modality specify hex value.\n\n");
	    	   endPrg(1);
	    	}
	    	
	    	
	    	if ( outputFile == null ) {
	          outputFile = inputFile + ".tmp"; 	
	    	} else {
	    	  bNoOutFile = false;	
	    	}
	    		
            elabFile();
            
            if ( bNoOutFile == true ) {
            	Files.move(Paths.get(outputFile), Paths.get(inputFile), StandardCopyOption.ATOMIC_MOVE);
            } 
	    } 
	    	
	    endPrg(bErr?1:0);	
	}

}
