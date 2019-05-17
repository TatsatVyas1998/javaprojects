package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 *
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
			throws FileNotFoundException {
		Scanner scfile = new Scanner(new File(docFile));
		HashMap<String , Occurrence> fileone = new HashMap();
		String a = "";
		while(scfile.hasNext()){
			a = getKeyword(scfile.next());

			if( a==null){
				continue;
			}
			if ( a.length()>0){
				//System.out.println( " "+ "adding" + " "+a);
				if( fileone.containsKey(a) == false){
					Occurrence b = new Occurrence( docFile ,1);
					//System.out.println(b);
					fileone.put(a, b);
				} else if ( fileone.containsKey(a) == true){
					//System.out.println( " "+ "increasing frequency" + " "+a);
					fileone.get(a).frequency++;

				}
			}else{
				continue;
			}
		}


		/** COMPLETE THIS METHOD **/

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return fileone;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table.
	 * This is done by calling the insertLastOccurrence method.
	 *
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/

		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		for( String p : kws.keySet()){
			if(  keywordsIndex.containsKey(p)== false){
				occs= new ArrayList<Occurrence>();
				occs.add( kws.get(p));

				keywordsIndex.put(p, occs);
			}else if ( keywordsIndex.containsKey(p) == true){
				occs = new ArrayList<Occurrence>();
				occs= keywordsIndex.get(p);

				occs.add(kws.get(p));

				insertLastOccurrence(occs); // to make use of the method and to arrange the occs elements in descending order
				keywordsIndex.put(p,occs);

			}

		}

	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 *
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 *
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {

		word = word.toLowerCase();
		word = word.replaceAll("\\s*\\p{Punct}+\\s*$", "");
		char [] punctuation = {'.' , ',' , ';' , ':', '?' , '!' , '"' , '\'' , ')' , '(', '-'};


		for (int i = 0; i < punctuation.length;i++){
			for (int j =0; j<word.length();j++){
				if ( punctuation[i] == word.charAt(j)){
					return null;
				}
			}
		}
		if(noiseWords.contains(word) == false){
			return word;
		}



		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return null;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 *
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/


		ArrayList<Integer> x = new ArrayList<Integer>();
		if( occs.size() == 1){
			return null;
		}else if( occs.size() ==2 ) {
			if( occs.get(0).frequency >= occs.get(1).frequency ){
				x.add(0);


			} else{
				Occurrence temp = occs.get(1);

				occs.remove(occs.get(1));

				occs.add(0,temp);
				x.add(0);

			}

		}else{
			Occurrence temp = occs.get(occs.size()-1);
			occs.remove(occs.get(occs.size()-1));
			int left = 0;
			int right = occs.size()-1;
			int mid = 0;
			while ( left <=right){
				mid = (left+right)/2;

				x.add(mid);
				if( occs.get(mid).frequency == temp.frequency){
					occs.add(mid,temp);
					temp= null;
					break;
				}
				if(occs.get(mid).frequency> temp.frequency){
					left = mid+1;
				}else{
					right = mid-1;
				}

			}

			if( temp!=null && occs!=null){
				if(temp.frequency>occs.get(mid).frequency && mid>0){

					occs.add(mid-1,temp);
				}else if (temp.frequency<occs.get(mid).frequency  && mid!= occs.size()-1){

					occs.add(mid+1,temp);
				}else if ( temp.frequency<occs.get(mid).frequency  && mid== occs.size()-1){
					occs.add(temp);
				}else if (temp.frequency>occs.get(mid).frequency && mid==0){
					occs.add(mid,temp);
				}
			}


		}


		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return x;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 *
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile)
			throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	private ArrayList<Occurrence> sort( ArrayList<Occurrence> occs){

		ArrayList<Occurrence> a = new ArrayList<Occurrence>();
		Occurrence largest = occs.get(0);
		int i =0;
		int j=1;
		while(occs.size() != 0){
			j=1;
			while( j<occs.size()){
				if( largest.frequency< occs.get(j).frequency){
					largest = occs.get(j);
				}



				j++;
			}

			a.add(largest);
			occs.remove(largest);
			if(occs.size()!=0){
				largest=occs.get(0);
			}
		}
		return a;
	}
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result.
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 *
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		kw1 = kw1.replaceAll("\\s+","");
		kw2 = kw2.replaceAll("\\s+","");
		kw1=kw1.toLowerCase();
		kw2=kw2.toLowerCase();
		ArrayList<Occurrence> a = keywordsIndex.get(kw1);
		ArrayList<Occurrence> b = keywordsIndex.get(kw2);

		ArrayList<Occurrence> finalarr = new ArrayList<Occurrence>();
		ArrayList<String> s = new ArrayList<String>();

		if(b == null || a == null){
			if( b==null && a!=null){
				int i =0;
				while(i!=a.size() && i<5 ){
					s.add(a.get(i).document);
					i++;
				}
			}else if ( b!=null && a==null){
				int i =0;
				while(i!=b.size() && i<5){
					s.add(b.get(i).document);
					i++;
				}
			}else if( a==null && b==null){
				return null;
			}
		} else{
			int count =0;
			int i =0;
			while( i<a.size()){
				Occurrence get = a.get(i);
				int j =0;
				while( j<b.size()){
					if( get.document.equalsIgnoreCase(b.get(j).document)){
						if( get.frequency< b.get(j).frequency){
							get=b.get(j);
						}
						finalarr.add(get);
						a.remove(i);
						b.remove(j);
						count++;
					}
					j++;
				}
				if( count<1){
					finalarr.add(get);
					a.remove(i);

				}
				count = 0;
			}
			if( b.size()>0){
				int j =0;
				while(j<b.size()){
					finalarr.add(b.get(j));
					b.remove(j);
				}

			}
		}
		if( finalarr.size()>0){
			finalarr = sort(finalarr);
		}
		int i =0;
		while( i<finalarr.size() && i<5){
			//System.out.print(finalarr.get(i).document +  "- " + finalarr.get(i).frequency + ", " );
			s.add(finalarr.get(i).document);
			i++;
		}

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code

		return s;

	}
}
