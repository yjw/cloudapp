import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    private BufferedReader bufferedReader;

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

	public String[] process() throws Exception {
		String[] ret = new String[20];
		HashSet<String> set = new HashSet<String>();
		for (String x : stopWordsArray) {
			set.add(x);
		}
		File file = new File(inputFileName);
		bufferedReader = new BufferedReader(new FileReader(file));
		List<List<String>> list = new ArrayList<List<String>>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, delimiters);
			List<String> item = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				String str = st.nextToken().trim().toLowerCase();
				if (set.contains(str)) {
					continue;
				}
				item.add(str);
			}
				list.add(item);
		}
		Integer[] inx = getIndexes();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int x : inx) {
			if (list.get(x).isEmpty()) {
				continue;
			}
			List<String> ss = list.get(x);
			for (String s : ss) {
				if (map.containsKey(s)) {
					map.put(s, map.get(s) + 1);
				}
				else {
					map.put(s, 1);
				}
			}
		}
		TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(new ValueComparator(map));
		sorted.putAll(map);
		//System.out.println(map);
		for (int i = 0; i < 20; i++) {
            ret[i] = sorted.pollFirstEntry().getKey();
        }
        return ret;
	}
	
	class ValueComparator implements Comparator<String> {
		 
		Map<String, Integer> map;
	 
		public ValueComparator(Map<String, Integer> map) {
			this.map = map;
		}
	 
		public int compare(String keyA, String keyB) {
			Integer valueA = map.get(keyA);
			Integer valueB = map.get(keyB);
			if (valueA < valueB) {
				return 1;
			}
			if (valueA > valueB) {
				return -1;
			}
			return keyA.compareTo(keyB);
		}
	}

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "/Users/yujuewang/Desktop/cloudapp/cloudapp-mp1/input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
