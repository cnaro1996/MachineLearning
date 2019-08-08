import java.util.*;
import java.io.*;

public class MachineLearning {
    public static void main(String[] args) {
        Data[] data = scanImages(false, 0.10f, Type.VALIDATION);

        for(int i=0; i<data.length; i++) {
            System.out.println(data[i].data);
        }
    }

    /**
     * Denotes the type of data we are handling.
     */
    public enum Type {
        TEST,
        TRAINING,
        VALIDATION;
    }

    /**
     * Scans a file and extracts the data from it, storing the data in a Data array.
     * @param digits True if the data is images of digits, false if the data is images of faces.
     * @param percentage The percentage of data to scan.
     * @return A Data array containing all of the data points from the file.
     */
    public static Data[] scanImages(boolean digits, float percentage, Type datatype) {
        Data[] data;
        Scanner s;
        String filename;
        int size = 0;
        int spacing = digits? 28: 70;
        String curr = "\n";

        // Construct a file path for the FileReader.
        // Size of array depends on the file
        if(digits) {
            filename = "data\\digitdata\\";
            switch(datatype) {
                case TEST: filename += "testimages";
                    size = 1000;
                    break;
                case TRAINING: filename += "trainingimages";
                    size = 5000;
                    break;
                case VALIDATION: filename += "validationimages";
                    size = 1000;
                    break;
            }
        } else {
            filename = "data\\facedata\\";
            switch (datatype) {
                case TEST: filename += "facedatatest";
                    size = 150;
                    break;
                case TRAINING: filename += "facedatatrain";
                    size = 451;
                    break;
                case VALIDATION: filename += "facedatavalidation";
                    size = 301;
                    break;
            }
        }

        data = new Data[(int) (percentage*size)];

        try {
            s = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter("\n");
            int i = 1;
            int dataIndex = 0;
            // Data points are separated by every "spacing" lines
            while(s.hasNext()){
                curr += s.next() + "\n";
                if(i == spacing) {
                    curr = curr.replaceAll("(?m)^\\s+$", "");
                    data[dataIndex] = new Data(curr, -1);
                    if(dataIndex+1 >= ((int) (percentage*size))) break;
                    dataIndex ++;
                    curr = "\n";
                    i = 0;
                }
                i++;
            }
            s.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(NoSuchElementException f) {
            f.printStackTrace();
        }

        return data;
    }
}
