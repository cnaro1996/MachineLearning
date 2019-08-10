import java.util.*;
import java.io.*;

public class MachineLearning {

    // Number of data points/labels (per file).
    static final int TESTDIGIT = 1000;
    static final int TRAINDIGIT = 5000;
    static final int VALDIGIT = 1000;
    static final int TESTFACE = 150;
    static final int TRAINFACE = 451;
    static final int VALFACE = 301;

    public static void main(String[] args) {
        // Set the values for what kind of data we want to use.
        Type queryType = Type.TRAINING;
        boolean digits = true;

        Data[] data = scanImages(digits, 0.10f, queryType);
        addLabels(digits, data, queryType);

        for(int i=0; i<data.length; i++) {
            System.out.println(data[i].data + "\n" + data[i].label);
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
     * Adds labels to the data array from the corresponding label file.
     * @param digits
     * @param data
     * @param datatype
     */
    public static void addLabels(boolean digits, Data[] data, Type datatype) {
        String filepath = getFilePath(digits, true, datatype);
        try{
            Scanner s = new Scanner(new BufferedReader(new FileReader(filepath))).useDelimiter("\n");
            for(int i=0; i<data.length; i++) {
                data[i].label = s.nextInt();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scans a file and extracts the data from it, storing the data in a Data array.
     *
     * @param digits True if the data is images of digits, false if the data is images of faces.
     * @param percentage The percentage of data to scan.
     * @return A Data array containing all of the data points from the file.
     */
    public static Data[] scanImages(boolean digits, float percentage, Type datatype) {
        Data[] data;
        Scanner s;
        String filename = getFilePath(digits, false, datatype);
        int size = getDataSize(digits, datatype);
        int spacing = digits? 28: 70;
        String curr = "\n";
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

    /**
     * Constructs a relative file path from the type of data specified and whether the
     * data is face data or digit data.
     *
     * @param digits
     * @param datatype
     * @return Relative file path in the form of a string.
     */
    public static String getFilePath(boolean digits, boolean label, Type datatype) {
        String filepath = "";
        // Construct a file path for the FileReader.
        // Size of array depends on the file
        if(digits) {
            filepath = "data\\digitdata\\";
            switch(datatype) {
                case TEST:
                    filepath += label? "testlabels" : "testimages";
                    break;
                case TRAINING:
                    filepath += label? "traininglabels" : "trainingimages";
                    break;
                case VALIDATION:
                    filepath += label? "validationlabels" : "validationimages";
                    break;
            }
        } else {
            filepath = "data\\facedata\\";
            switch (datatype) {
                case TEST:
                    filepath += label? "facedatatestlabels" : "facedatatest";
                    break;
                case TRAINING:
                    filepath += label? "facedatatrainlabels" : "facedatatrain";
                    break;
                case VALIDATION:
                    filepath += label? "facedatavalidationlabels" : "facedatavalidation";
                    break;
            }
        }
        return filepath;
    }

    /**
     * Determines the amount of data points based on which file that is being used.
     *
     * @param digits
     * @param datatype
     * @return
     */
    public static int getDataSize(boolean digits, Type datatype) {
        // Size of array depends on the file
        if(digits) {
            switch(datatype) {
                case TEST: return TESTDIGIT;
                case TRAINING: return TRAINDIGIT;
                case VALIDATION: return VALDIGIT;
            }
        } else {
            switch (datatype) {
                case TEST: return TESTFACE;
                case TRAINING: return TRAINFACE;
                case VALIDATION: return VALFACE;
            }
        }
        return 0; // Never reached; Make the compiler happy.
    }
}
