package hashcode2019;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Hashcode {
    public static String PATH_A = "src\\hashcode2019\\a_example.txt";
    public static String PATH_B = "src\\hashcode2019\\b_example.txt";
    public static String PATH_C = "src\\hashcode2019\\c_example.txt";
    public static String PATH_D = "src\\hashcode2019\\d_example.txt";
    public static String PATH_E = "src\\hashcode2019\\e_example.txt";
    public static String PATH_A_OUTPUT = "src\\hashcode2019\\output\\a_output.txt";
    public static String PATH_B_OUTPUT = "src\\hashcode2019\\output\\b_output.txt";
    public static String PATH_C_OUTPUT = "src\\hashcode2019\\output\\c_output.txt";
    public static String PATH_D_OUTPUT = "src\\hashcode2019\\output\\d_output.txt";
    public static String PATH_E_OUTPUT = "src\\hashcode2019\\output\\e_output.txt";
    public static String[] paths = new String[] { PATH_A, PATH_B, PATH_C, PATH_D, PATH_E };
    public static String[] paths_output = new String[] { PATH_A_OUTPUT, PATH_B_OUTPUT, PATH_C_OUTPUT, PATH_D_OUTPUT, PATH_E_OUTPUT };

    public static void main(String[] args) throws IOException {
        int ind = 0;
        for (String path : paths) {
            System.out.println(ind);
            Scanner scanner = createScaner(path);
            ArrayList<Photo> photos = createPhotos(scanner);
            
            photos.removeIf(photo -> photo.type == 'V');
            Collections.shuffle(photos);
            
            ArrayList<Photo> solution = solution1(photos);
            writeToFile(paths_output[ind], solution);
            ind++;
        }
    }
    
    public static ArrayList<Photo> solution1(ArrayList<Photo> photos) {
        int maxDiff = 5;
        ArrayList<Photo> solution = new ArrayList<>();
        Photo current = photos.get(0);
        current.used = true;
        solution.add(current);
        while (solution.size() < photos.size()) {
            if(solution.size() % 500 == 0)
                System.out.println(solution.size());
            boolean found = false;
            for (Photo photo : photos) {
                if (!photo.used) {
                    int diff = calculateMinimun(current, photo);
                    if (diff >= maxDiff) {
                        solution.add(photo);
                        current = photo;
                        found = true;
                        photo.used = true;
                        break;
                    }
                }
            }
            if (!found) {
                maxDiff--;
            }
        }
        return solution;
    }
    public static ArrayList<Photo> createPhotos(Scanner scanner) {
        int N;
        String nphotos = scanner.nextLine();
        N = Integer.valueOf(nphotos);
        ArrayList<Photo> photos = new ArrayList<Photo>();
        for (int i = 0; i < N; i++) {
            String photo = scanner.nextLine();
            String[] split = photo.split(" ");
            char type = split[0].charAt(0);
            int nTags = Integer.valueOf(split[1]);
            ArrayList<String> tags = new ArrayList<>();
            for (int j = 0; j < nTags; j++) {
                tags.add(split[j + 2]);
            }
            photos.add(new Photo(String.valueOf(i), type, nTags, tags));
        }
        return photos;
    }
    public static Scanner createScaner(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        return sc;
    }
    public static void writeToFile(String path, ArrayList<Photo> solution) throws IOException {
        File fout = new File(path);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(String.valueOf(solution.size()));
        bw.newLine();
        for (int i = 0; i < solution.size(); i++) {
            bw.write(String.valueOf(solution.get(i).id));
            bw.newLine();
        }
        bw.close();
    }
    public static Photo merge(Photo a, Photo b) {
        
        ArrayList<String> newTags = new ArrayList<>();
        newTags.addAll(b.tags);
        
        for (String tag: a.tags) {
            if (!b.tags.contains(tag)) {
                newTags.add(tag);
            }
        }
        return new Photo(a.id + " " + b.id, 'H', newTags.size(), newTags);
    }
    public static int calculateMinimun(Photo pic1, Photo pic2) {
        ArrayList<String> tags1 = pic1.tags;
        ArrayList<String> tags2 = pic2.tags;
        ArrayList<String> commonTags = new ArrayList<String>();
        for (int i = 0; i < tags1.size(); i++) {
            if (tags2.contains(tags1.get(i))) {
                commonTags.add(tags1.get(i));
            }
            ;
        }
        return Integer.min(Integer.min(tags1.size() - commonTags.size(), tags2.size() - commonTags.size()),
                commonTags.size());
    }
}
class Photo {
    public String id;
    public char type;
    public int nTags;
    public ArrayList<String> tags;
    public boolean used;
    public Photo(String id, char type, int nTags, ArrayList<String> tags) {
        this.id = id;
        this.type = type;
        this.nTags = nTags;
        this.tags = tags;
        this.used = false;
    }
    @Override
    public String toString() {
        return "Photo [type=" + type + ", nTags=" + nTags + ", tags=" + tags + "]";
    }
}