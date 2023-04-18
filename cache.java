import java.util.*;

public class cache {
    private int size;
    private int associativity;
    private int wordBlocks;
    private ArrayList<Integer> indices1 = new ArrayList<>();
    private ArrayList<Integer> indices2 = new ArrayList<>();
    private ArrayList<Integer> indices3 = new ArrayList<>();
    private ArrayList<Integer> indices4 = new ArrayList<>();
    private HashMap<Integer, ArrayList<Integer>> LRU =new HashMap<>();
    private float hits = 0;
    private float total = 0;


    public cache(int size, int associativity, int wordBlocks) {
        this.size = size;
        this.associativity = associativity;
        this.wordBlocks = wordBlocks;
        if (associativity == 1) {
            for (int i = 0; i < size/wordBlocks/4; i++) {
                indices1.add(0); }
        } else if (associativity == 2) {
            for (int i = 0; i < size/wordBlocks/4/2; i++) {
                indices1.add(0); }
            for (int i = size/wordBlocks/4/2; i < size/wordBlocks/4; i++) {
                indices2.add(0); }
        } else {
            for (int i = 0; i < size/wordBlocks/4 * .25; i++) {
                indices1.add(0); }
            for (int i = (int) (size/wordBlocks/4 * .25); i < size/wordBlocks/4 * .50; i++) {
                indices2.add(0); }
            for (int i = (int) (size/wordBlocks/4 * .50); i < size/wordBlocks/4 * .75; i++) {
                indices3.add(0); }
            for (int i = (int) (size/wordBlocks/4 * .75); i < size/wordBlocks/4; i++) {
                indices4.add(0); }
        }
    }

    public void add(int address) {
        total++;
        if (associativity == 1){
            if (wordBlocks == 1) {
                int byteOffset = address % 4;
                address = address / 4;
                int index = (int) (address % (size/4));
                int tag = (int) (address / (size/4));
                if (indices1.get(index)==tag){
                    hits++;
                }
                indices1.set(index, tag);
            } else if (wordBlocks == 2) {
                int byteOffset = address % 4;
                address = address / 4;
                int blockOffset = address % 2;
                address = address / 2;
                int index = (int) (address % Math.pow(2, 8));
                int tag = (int) (address / Math.pow(2, 8));
                if (indices1.get(index)==tag){
                    hits++;
                }
                indices1.set(index, tag);
            } else {
                int byteOffset = address % 4;
                address = address / 4;
                int blockOffset = address % 4;
                address = address / 4;
                int index = (int) (address % Math.pow(2, 7));
                int tag = (int) (address / Math.pow(2, 7));
                if (indices1.get(index)==tag){
                    hits++;
                }
                indices1.set(index, tag);
            }
        } else if (associativity == 2){
            int byteOffset = address % 4;
            address = address / 4;
            int index = (address % (size/8));
            int tag = (address / (size/8));
            if (indices1.get(index) == tag){
                hits++;
                updateLRU(index, 1);

            } else if (indices2.get(index) == tag){
                hits++;
                updateLRU(index, 2);

            } else {
                int lru = minLRU(LRU.get(index));
                if (lru == 1) { //3
                    indices1.set(index, tag);
                    updateLRU(index, 1);
                } else { //4
                    indices2.set(index, tag);
                    updateLRU(index, 2);
                }
            }
        } else { // associativity = 4
            if (wordBlocks == 1) {
                int byteOffset = address % 4;
                address = address / 4;
                int index = (address % (size / 16));
                int tag = (address / (size / 16));
                if (indices1.get(index) == tag) {
                    hits++;
                    updateLRU(index, 1);
                } else if (indices2.get(index) == tag) {
                    hits++;
                    updateLRU(index, 2);
                } else if (indices3.get(index) == tag) {
                    hits++;
                    updateLRU(index, 3);
                } else if (indices4.get(index) == tag) {
                    hits++;
                    updateLRU(index, 4);
                } else {
                    int lru = minLRU(LRU.get(index));
                    if (lru == 1) {
                        indices1.set(index, tag);
                        updateLRU(index, 1);
                    } else if (lru == 2) {
                        indices2.set(index, tag);
                        updateLRU(index, 2);
                    } else if (lru == 3) {
                        indices3.set(index, tag);
                        updateLRU(index, 3);
                    } else {
                        indices4.set(index, tag);
                        updateLRU(index, 4);
                    }
                }
            } else { // wordBlocks = 4
                int byteOffset = address % 4;
                address = address / 4;
                int blockOffset = address % 4; // 2 bits for block offset
                address = address / 4;
                int index = (address % (32));
                int tag = (address / (32));
                if (indices1.get(index) == tag) {
                    hits++;
                    updateLRU(index, 1);
                } else if (indices2.get(index) == tag) {
                    hits++;
                    updateLRU(index, 2);
                } else if (indices3.get(index) == tag) {
                    hits++;
                    updateLRU(index, 3);
                } else if (indices4.get(index) == tag) {
                    hits++;
                    updateLRU(index, 4);
                } else {
                    int lru = minLRU(LRU.get(index));
                    if (lru == 1) {
                        indices1.set(index, tag);
                        updateLRU(index, 1);
                    } else if (lru == 2) {
                        indices2.set(index, tag);
                        updateLRU(index, 2);
                    } else if (lru == 3) {
                        indices3.set(index, tag);
                        updateLRU(index, 3);
                    } else {
                        indices4.set(index, tag);
                        updateLRU(index, 4);
                    }
                }
            }
        }
    }
    public void print(String name) {
        System.out.println(name);
        System.out.println("Cache size: " + size + "B\tAssociativity: " + associativity + "\tBlock size: " + wordBlocks);
        System.out.print("Hits: " + (int)hits + "    Hit Rate: ");
        System.out.printf("%.2f", hits/total*100);
        System.out.println("%");
        System.out.println("---------------------------");
    }
    private int minLRU(ArrayList<Integer> LRU){
        if (LRU == null) {
            return 1;
        }
        int minIndex = 1;
        int minValue = LRU.get(1);
        for (int i = 2; i < LRU.size(); i++){
            int val = LRU.get(i);
            if (val < minValue) {
                minIndex = i;
                minValue = val;
            }
        }
        return minIndex;
    }
    private void updateLRU(int index, int num){
        ArrayList<Integer> list = LRU.get(index);
        if (list == null){
            list = new ArrayList<>();
            int i = 0;
            while (i<=associativity){
               list.add(0);
               i++;
            }
            list.set(num, (int)total);
        } else {
            list.set(num, (int)total);
        }
        LRU.put(index, list);
    }
}
