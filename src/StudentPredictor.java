import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class StudentPredictor {
	
	private HashSet<ArrayList<Double>> dataset;
	private ArrayList<ArrayList<Double>> columns;	// needed to normalise values & calculate weightings
	private static double [] euclideanWeighting;
	private static DecimalFormat df; // limits to 4 decimal places
	
	
	private StudentPredictor() {
		dataset = new HashSet<>();
		columns = new ArrayList<>();
		// euclideanWeighting =  new double [] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}; // left at 1 for now until we do algoroithm
		// euclideanWeighting =  new double [] {2,2,2,1,2,3,4,4,3,3,1,2,1,3,4,2,3,2,2,1,4,3,1,2,2,2,4,3,1,4}; // my weightings - tried these weights that i just guessed - 60% accuracy, not as good as corellation
		euclideanWeighting =  new double [30];
		df = new DecimalFormat("#.####"); // limits decimals to 4 decimal places
		for (int i = 0; i < 33; i++ ) { // this just instantiates the blank arraylists in columns so that readDataset works
			ArrayList<Double> x = new ArrayList<>();
			columns.add(x);
		}
	}
	
	private void readDataset(String path) {
		String currentRow = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String headings = br.readLine(); // this takes the column headings out, the only non numeric values in file
			while ((currentRow = br.readLine()) != null) {
				ArrayList<Double> row = toDoubleArrayList(currentRow.split(",")); // splits string into string [] then into ArrayList<double>
				dataset.add(row);
				// add to columns
				for (int i = 0; i < 33; i++) {
					columns.get(i).add(row.get(i)); // e.g this will add the school attribute to the arrilist of all the school values for each row
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void classifyDataset() { // this will add a 34th attribute - classification, 1 = (0-6), 2 = (7-13), 3 = (14-20)
		Iterator<ArrayList<Double>> iter4 = dataset.iterator();
		while (iter4.hasNext()) { // loop through rows
			ArrayList<Double> current = iter4.next(); // current row
			Double grade = current.get(32);
			if (grade < 7)
				current.add(1.0);
			else if (grade < 14)
				current.add(2.0);
			else
				current.add(3.0);
			// System.out.println(current.get(33));
		}
	}
	
	private void calculateWeightings() {

		for (int j = 0; j < 30; j++) { // for each of 30 values calculate corellation with final grade
			double sx = 0.0; // sum x
			double sy = 0.0; // sum y
			double sxx = 0.0; // sum x squared
			double syy = 0.0; // sum y squared
			double sxy = 0.0; // sum x * y
			int n = columns.get(j).size();
			
			for (int i = 0; i < n; i++) {
				double x = columns.get(j).get(i); // compares this attribute value
				double y = columns.get(32).get(i); // with its corresponding final grade
				sx += x;
				sy += y;
				sxx += (x * x);
				syy += (y * y);
				sxy += (x * y);
			}
			
			double cov = sxy / n - sx * sy / n / n; // covariation
			double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);  // standard error of x
			double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);  // standard error of y
			double correl =  (cov / sigmax / sigmay); // corellation
			euclideanWeighting[j] = correl;
			// System.out.println("Column " + j + " correl: " + correl);
		} // weightings now contains array of positve and negative correlations, we want absalute value because we want influece not direction, need all values to sum to 1
		
		double totalWeight = 0;
		for (double x: euclideanWeighting) {
			totalWeight += (Math.abs(x)); // we want absalute value
		}
		// System.out.println(totalWeight);
		
		for (int i = 0; i < euclideanWeighting.length; i++) { // this will change it so all weights are positive and now sum to 1
			euclideanWeighting[i] = Math.abs(euclideanWeighting[i]/totalWeight);
		}
	}
	
	private void normaliseDataset() { // all values in the range 0-1 to stop skewing by attributes such as age
		double [] maxValues = new double [30];
		double [] minValues = new double [30]; // these will hold max and min values for each column
		for (int i = 0; i < 30; i++) {
			maxValues[i] = Collections.max(columns.get(i));
			minValues[i] = Collections.min(columns.get(i));
		}
		
		Iterator<ArrayList<Double>> iter = dataset.iterator();
		while (iter.hasNext()) { // loop through rows
			ArrayList<Double> current = iter.next(); // current row
			for (int i = 0; i < 30; i++) { // loop through first 30 values in row to be normalised
				double oldValue = current.get(i);
				double normValue = Double.parseDouble(df.format((oldValue - minValues[i])/(maxValues[i]- minValues[i]))); // normalisation formula from notes
				current.set(i, normValue);
			}
		}
	}
	
	private static double weightedEuclideanDistance(ArrayList<Double> a, ArrayList<Double> b) {
		double total = 0;
		for (int i = 0; i < 30; i++) {
			double difference = (a.get(i) - b.get(i));
			total += (euclideanWeighting[i] * difference * difference);
		}
		return Double.parseDouble(df.format(Math.sqrt(total)));
	}
	
	private static ArrayList<Double> toDoubleArrayList(String [] values) {
		ArrayList<Double> result = new ArrayList<>();
		for (String a: values) {
			result.add(Double.parseDouble(a));
		}
		return result;
	}
	
	private void predictClassKNN() { // train on first 200, run on second 195
		HashSet<ArrayList<Double>> trainingSet = new HashSet<>();
		HashSet<ArrayList<Double>> validationSet = new HashSet<>();
		Iterator<ArrayList<Double>> iter5 = dataset.iterator(); // splits into two sets
        int rnd;
		while (iter5.hasNext()) { // loop through rows
			rnd = ThreadLocalRandom.current().nextInt(1, 11);
			if (rnd <= 5)
				trainingSet.add(iter5.next()); // 200 rows
			else
				validationSet.add(iter5.next());// 195
		}
		HashMap<ArrayList<Double>,Double> resultsMap = new HashMap<>(); // this will hold 195 (row, predictedClass) prediction pairs.
		
		Iterator<ArrayList<Double>> iter6 = validationSet.iterator();
		while (iter6.hasNext()) { // classify validation set one by one using knn and add to resultsMap
			TreeMap<Double,Double> distanceMap = new TreeMap<Double,Double>(); // this will hold 200(dist, class) pairs. TreeMap keep them in sorted order - can use pollFirstEntry
			ArrayList<Double> current = iter6.next(); // current validation row
			Iterator<ArrayList<Double>> iter7 = trainingSet.iterator(); // calculate distance from this to each row in training set
			while (iter7.hasNext()) {
				ArrayList<Double> trainRow = iter7.next();
				// System.out.println(trainRow.get(33));
				double dist = weightedEuclideanDistance(current, trainRow);
				// System.out.println(dist);
				distanceMap.put(dist, trainRow.get(33)); //  index 33 holds clasification value
			}

			int [] classCount = new int[4]; // we wont use index 0.
			
			for (int k = 0; k < 5; k++) { // 5 nearest neighbours
				Map.Entry<Double,Double> a = distanceMap.pollFirstEntry(); // returns and removes smallest key entry in treemap - closest neighbour
				classCount[Integer.valueOf(a.getValue().intValue())]++;  // converts double to int and increments index 1,2 or 3 of classCount
				// System.out.print("(" + a.getKey() + "," + a.getValue() + ") ");
			}
			
			if (!isOneMax(classCount)) { // in the event of 2 way tie(2,2,1) - increase to 6 nearest neighbours
				Map.Entry<Double,Double> a = distanceMap.pollFirstEntry();
				classCount[Integer.valueOf(a.getValue().intValue())]++;
				// System.out.print("(" + a.getKey() + "," + a.getValue() + ") ");
				if (!isOneMax(classCount)) { // in the event of 3 way tie(2,2,2) - increase to 7 nearest neighbours
					Map.Entry<Double,Double> b = distanceMap.pollFirstEntry();
					classCount[Integer.valueOf(b.getValue().intValue())]++;
					// System.out.print("(" + b.getKey() + "," + b.getValue() + ") ");
				}
			} // there is now definately only one max
			// System.out.println();
				
			if ((classCount[1] > classCount[2]) && (classCount[1] > classCount[3]))
				resultsMap.put(current, 1.0);
			else if ((classCount[2] > classCount[1]) && (classCount[2] > classCount[3]))
				resultsMap.put(current, 2.0);
			else 
				resultsMap.put(current, 3.0);
		}
		
		// next step is check success rate, compare prediction with actual
		double rightCount = 0;
		int g = 0;
		Iterator it = resultsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry prediction = (Map.Entry)it.next();
			// System.out.println("Actual: " + ((ArrayList<Double>)prediction.getKey()).get(33) + " Predicted: " + prediction.getValue());
			if ((((ArrayList<Double>)prediction.getKey()).get(33)).equals(prediction.getValue()))				// if predicted class = real class
				rightCount++;
			g++;
		}
		double success = Double.parseDouble(df.format((rightCount/195)*100)); // percentage success rounded to 4 places
		System.out.println(rightCount + " predictions out of 195 correct");
		System.out.println(success + "% of predictions correct");
		// System.out.println("total " + g);
	}
	
	private static boolean isOneMax(int [] arr) {
		if ((arr[1] > arr[2]) && (arr[1] > arr[3]))
			return true;
		else if ((arr[2] > arr[3]) && (arr[2] > arr[1]))
			return true;
		else if ((arr[3] > arr[2]) && (arr[3] > arr[1]))
			return true;
		else
			return false;
	}
	
	public static void main(String [] args) {
		StudentPredictor z = new StudentPredictor();
		z.readDataset(args[0]);
		z.normaliseDataset();
		z.classifyDataset();
		z.calculateWeightings();
		z.predictClassKNN();
        for (double d: z.euclideanWeighting) {
            System.out.println(d);
        }

		// Iterator<ArrayList<Double>> iter2 = z.dataset.iterator();
		// System.out.println(weightedEuclideanDistance(iter2.next(),iter2.next()));
	}
}
 