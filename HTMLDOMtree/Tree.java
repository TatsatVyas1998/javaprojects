package structures;

import java.util.*;

import org.w3c.dom.Text;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 *
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root=null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 *
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object.
	 *
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		String rootstring = sc.nextLine();
		root = new TagNode( rootstring.substring(1,rootstring.length()-1),null,null);
		Stack<String> tags = new Stack<String>();
		tags.push(root.tag);
		TagNode ptr = root;
		helpbuild(ptr, 1, tags);


	}
	public void helpbuild(TagNode root, int level , Stack<String> tags){

		String currentline = "";
		String tag = "";
		int start;
		int end;
		TagNode ptr=root;

		while( sc.hasNextLine() == true){
			currentline = sc.nextLine();
			if(currentline.charAt(0) == '<' && currentline.charAt(1) != '/'){

				start =1;
				end = currentline.indexOf('>');
				tag = currentline.substring(start, end);
				TagNode f = new TagNode(tag,null,null);


				if ( level == tags.size()){
					ptr.firstChild=f;
					tags.push(f.tag);
					ptr = ptr.firstChild;
					level++;
					helpbuild(ptr,level,tags);

				}else{
					tags.push(f.tag);
					ptr.sibling=f;
					ptr =ptr.sibling;
					helpbuild(ptr,level,tags);

				}

			}
			else if (currentline.charAt(0) == '<' && currentline.charAt(1) == '/') {
				tags.pop();
				break;
			}else{
				tag = currentline;
				TagNode f = new TagNode(tag,null,null);
				if ( level== tags.size()){
					ptr.firstChild = f;
					ptr = ptr.firstChild;
					level++;

					helpbuild(ptr,level,tags);

					break;
				}else{
					ptr.sibling=f;
					ptr=ptr.sibling;
					helpbuild(ptr,level,tags);

					break;

				}
			}
		}
	}

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 *
	 *
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */

	public void replaceTag(String oldTag, String newTag) {

		replaceTaghelp(root,oldTag,newTag);



	}

	public void replaceTaghelp(TagNode root, String oldTag, String newTag){
		TagNode ptr = root;

		if ( ptr == null){
			return;
		}
		while( ptr!=null){
			if ( ptr.tag.equals(oldTag)){
				ptr.tag = newTag;

			}  if ( ptr.firstChild !=null){
				ptr = ptr.firstChild;
				replaceTaghelp(ptr,oldTag, newTag);

			} if ( ptr.sibling != null){
				ptr = ptr.sibling;
				replaceTaghelp( ptr,oldTag , newTag);


			}
			ptr = ptr.sibling;
		}
	}
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 *
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {

		int count = 1;
		TagNode ptr = root;
		while( count <= row ){
			ptr = find ( "tr" , null ,ptr , true);

			count++;
			if ( count <= row){
				ptr = ptr.sibling;
			}
		}
		if ( ptr!=null){
			ptr= ptr.firstChild;
			while ( ptr != null){
				if ( ptr.firstChild.tag.equals("b")){
					ptr = ptr.sibling;
					continue;
				}
				else{
					TagNode f = new TagNode( "b" ,ptr.firstChild,null );
					ptr.firstChild = f;
					ptr = ptr.sibling;
				}
			}
		}
	}

	public TagNode find(String Tag , TagNode prev, TagNode root , boolean x){
		TagNode ptr = root;
		TagNode target = null;
		TagNode ptr1 = prev;
		while( ptr!=null){
			StringTokenizer st = new StringTokenizer(ptr.tag);
			while( st.hasMoreTokens()){
				if ( st.nextToken().equals(Tag)){
					if ( x == true){
						target = ptr;
					}
					if ( x== false){
						target = ptr1;
					}
					return target;
				}
			}  if ( ptr.firstChild !=null){
				ptr1=ptr;
				ptr = ptr.firstChild;
				target =   find( Tag, ptr1, ptr , x);
				if ( target != null){
					return target;
				}

			} if ( ptr.sibling != null){
				ptr1=ptr;
				ptr = ptr.sibling;
				target = find( Tag, ptr1, ptr , x);
				if ( target!= null){
					return target;
				}

			}
			ptr1=ptr;
			ptr = ptr.sibling;


		}



		return target;
	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and,
	 * in addition, all the li tags immediately under the removed tag are converted to p tags.
	 *
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		TagNode	ptr = root;
		TagNode ptr2 = null;
		while( ptr != null){
			ptr = find( tag, null,root, false );

			if( ptr!=null){
				try {
					if ( ptr.firstChild.firstChild.tag.equals("li")){

						ptr.firstChild.tag = "p";
						ptr2 = ptr.firstChild.sibling;
						ptr.firstChild= ptr.firstChild.firstChild;
						ptr = ptr.firstChild;

						while( ptr != null){
							if ( ptr.tag.equals("li")){
								ptr.tag = "p";

							}
							if ( ptr.sibling ==null){
								ptr.sibling=ptr2;
								break;
							}
							ptr=ptr.sibling;
						}

					}else{
						ptr2 = ptr.firstChild.sibling;
						ptr.firstChild=ptr.firstChild.firstChild;
						while( ptr!=null){
							if ( ptr.sibling==null){
								ptr.sibling=ptr2;
								break;
							}
							ptr=ptr.sibling;
						}
					}
				} catch(NullPointerException e ){
					if ( ptr.sibling.firstChild.tag.equals("li")){

						ptr.sibling.tag = "p";
						ptr2 = ptr.sibling.sibling;
						ptr.sibling.sibling=ptr.sibling.firstChild.sibling;
						ptr.sibling.firstChild = ptr.sibling.firstChild.firstChild;

						while( ptr != null){
							if ( ptr.tag.equals("li")){
								ptr.tag = "p";

							}
							if ( ptr.sibling ==null){
								ptr.sibling=ptr2;
								break;
							}
							ptr=ptr.sibling;
						}

					}else{

						ptr2 = ptr.sibling.sibling;


						ptr.sibling=ptr.sibling.firstChild;
						while( ptr!=null){
							if ( ptr.sibling==null){
								ptr.sibling=ptr2;
								break;
							}
							ptr=ptr.sibling;
						}
					}
				}
			}
		}


	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 *
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public TagNode addfind( String word , TagNode root , TagNode pre , String tag ){
		TagNode ptr = root;
		TagNode target = null;
		TagNode prev = pre;

		while ( ptr !=null){
			StringTokenizer st = new StringTokenizer(ptr.tag);

			while( st.hasMoreTokens()){
				String str = st.nextToken();
				char c = str.charAt(str.length()-1);
				if ( c == '.' || c == ',' || c == '?' || c == '!' || c == ':' || c == ';' ){
					if ( str.substring(0,str.length()-1).equalsIgnoreCase(word)){

						if ( prev.tag.equals(tag) && prev.firstChild.tag.substring(0,prev.firstChild.tag.length()-1).equalsIgnoreCase(word)){



							break;



						} else{
							target = prev;
							return target;
						}
					}
				}else{
					if ( str.equalsIgnoreCase(word)){
						if ( prev.tag.equals(tag)){
							if ( prev.firstChild.tag.equalsIgnoreCase(word)){
								break;
							}
						}else{
							target = prev;
							return target;
						}
					}
				}
			}
			if ( ptr.firstChild !=null){

				prev=ptr;
				ptr = ptr.firstChild;
				target = addfind(  word ,  ptr ,prev , tag) ;
				if ( target!=null){
					break;
				}

			} if ( ptr.sibling != null){

				prev=ptr;
				ptr = ptr.sibling;
				target = addfind(  word ,  ptr , prev, tag) ;
				if ( target!=null){
					break;
				}
			}



			prev=ptr;
			ptr = ptr.sibling;





		}
		return target;
	}

	public void addTag(String word, String tag) {
		TagNode ptr = root;
		boolean x = false;
		int count = 0;
		while( ptr!= null){
			ptr = addfind( word ,root,null, tag);
			count=0;
			if (ptr!=null){

				try {
					StringTokenizer st = new StringTokenizer(ptr.firstChild.tag);
					String fir = "";
					while(st.hasMoreTokens()){

						String str = st.nextToken();

						count++;
						char c = str.charAt(str.length()-1);
						String str1= null;
						if ( c == '.' || c == ',' || c == '?' || c == '!' || c == ':' || c == ';' ){
							str1=str;
							str = str.substring(0, str.length()-1);
						}else{
							str1=str;
						}
						if (str.equalsIgnoreCase(word) != true && st.hasMoreTokens() != true){
							throw new NullPointerException();
						}
						if ( str.equalsIgnoreCase(word) && st.hasMoreTokens() != true && count ==1){
							if(ptr.firstChild.sibling ==null){
								TagNode g = new TagNode(ptr.firstChild.tag,null,null);
								TagNode f = new TagNode( tag, g,null);
								ptr.firstChild = f;
								break;
							}else{
								TagNode g = new TagNode(ptr.firstChild.tag,null,null);
								TagNode f = new TagNode( tag, g,ptr.firstChild.sibling);
								ptr.firstChild = f;
								break;
							}
						}  else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() == true && count ==1 ){
							String s = "";
							while( st.hasMoreTokens()){

								s += st.nextToken() + " ";
							}
							TagNode rst = new TagNode ( s, null , null);
							TagNode f1 = new TagNode( str1 , null , null);
							TagNode f = new TagNode( tag, f1 , rst);
							ptr.firstChild = f;


						} else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() != true && count !=1){
							TagNode cur = new TagNode ( str1, null , null);
							TagNode f = new TagNode ( tag, cur , null);
							TagNode fr = new TagNode ( fir, null , f);
							ptr.firstChild=fr;

						} else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() == true && count !=1 ){
							String after = "";
							TagNode cur = new TagNode ( str1, null , null);
							while( st.hasMoreTokens() != false){
								after += st.nextToken() + " ";
							}
							TagNode aff = new TagNode( after , null,ptr.firstChild.sibling);
							TagNode f = new TagNode ( tag, cur , aff);
							TagNode fr = new TagNode ( fir, null , f);
							ptr.firstChild=fr;

						}
						fir += str + " ";

					}
				} catch( NullPointerException e ){
					count =0;
					StringTokenizer st = new StringTokenizer(ptr.sibling.tag);
					String fir = "";
					while(st.hasMoreTokens()){

						String str = st.nextToken();
						count++;
						fir += str;
						char c = str.charAt(str.length()-1);
						String str1= null;
						if ( c == '.' || c == ',' || c == '?' || c == '!' || c == ':' || c == ';' ){
							str1=str;
							str = str.substring(0, str.length()-1);
						}
						if ( str.equalsIgnoreCase(word)&& st.hasMoreTokens() != true && count ==1){
							TagNode f = new TagNode( tag, ptr.sibling,null);
							ptr.sibling = f;
							break;
						}  else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() == true && count ==1 ){
							String s = "";
							while( st.hasMoreTokens()){

								s += st.nextToken() + " ";
							}
							TagNode rst = new TagNode ( s, null , null);
							TagNode f1 = new TagNode( str1 , null , null);
							TagNode f = new TagNode( tag, f1 , rst);
							ptr.sibling = f;
						} else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() != true && count !=1){
							TagNode cur = new TagNode ( str1, null , null);
							TagNode f = new TagNode ( tag, cur , null);
							TagNode fr = new TagNode ( fir, null , f);
							ptr.sibling=fr;
						} else if (str.equalsIgnoreCase(word) && st.hasMoreTokens() == true && count !=1 ){
							String after = "";
							TagNode cur = new TagNode ( str1, null , null);
							while( st.hasMoreTokens() != false){
								after += st.nextToken() + " ";
							}
							TagNode aff = new TagNode( after , null,ptr.firstChild.sibling);
							TagNode f = new TagNode ( tag, cur , aff);
							TagNode fr = new TagNode ( fir, null , f);
							ptr.sibling=fr;
						}

						fir += str + " ";
					}
				}

			}
		}
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 *
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");
			}
		}
	}

	/**
	 * Prints the DOM tree.
	 *
	 */
	public void print() {
		print(root, 1);
	}

	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
