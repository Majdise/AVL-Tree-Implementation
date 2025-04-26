import java.util.ArrayList;
import java.util.List;


/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

    final static int ZERO_REBALANCES = 0;
    final static int ONE_REBALANCE = 1;
    final static int TWO_REBALANCES = 2;
	final static int THREE_REBALANCES= 3;
	final static int Five_REBALANCES= 5;
	final static int SIX_REBALANCES= 6;

    AVLNode EXTERNAL_NODE;
    AVLNode Root;
    AVLNode Max;
    AVLNode Min;
 	   
       
    enum Edge {
    	Type10, Type01, Type11, Type20, Type02, Type12, Type21, 
    	Type22, Type13, Type31, whatever,
 
    }

    /**
     * AVLTree sole constructor
     */
    public AVLTree() {
        EXTERNAL_NODE = new AVLNode();
        Root = EXTERNAL_NODE;
        Max = EXTERNAL_NODE;
        Min = EXTERNAL_NODE;

    }
    
 
  /**
   * public boolean empty()
   * returns true if and only if the tree is empty
   *
   * Complexity : O(1)
   *
   */
  public boolean empty() { 
      return size() == 0;
  }

  
 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   * 
   * Complexity : O(log(n))
   */
  public String search(int k) 
  {
	  AVLNode p =helpSearch(Root,k);
	  if (p.key==k){
		  return p.value;
	  }
	  return null;
  }
  
  
  /**
   * private AVLNode helpSearch
   * This function uses binary search concept to find the desired key
   * 
   * Complexity : O(log(n))
   * 
   */
  private AVLNode helpSearch(AVLNode node, int k) {
      AVLNode currentNode = node;//Starts from Root
      while (node.isRealNode()) {//checks if not a leaf
    	  currentNode = node;
          if (k == node.key) {
              return node;
        	 }
          else if (k > node.key) {
              node = node.right;
          	}
          else 
          	{
              node = node.left;
          }
      }
      return currentNode;}




  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   * Complexity : O(log(n))
   */
   public int insert(int k, String i) {//Lecture AVL Page 16
	     if (empty()) { //if tree is empty
	    	  AVLNode StartNode = new AVLNode(k, i, EXTERNAL_NODE);
	    	  Root = StartNode;
	    	  Max = StartNode;
	    	  Min = StartNode;
	       }
	       AVLNode Loc2Insert = helpSearch(Root, k);
	       if (Loc2Insert.key == k) { //if node with the same key is found
	    	   return (-1);
	       }
	       AVLNode FreshNode = new AVLNode(k, i, Loc2Insert);
	       if (k < Loc2Insert.key ) { //if key is smaller then pick left child
	    	   Loc2Insert.left = FreshNode;
	       }
	       if (k > Loc2Insert.key ) { //if key is bigger then pick right child
	    	   Loc2Insert.right = FreshNode;
	       }
	       NewInsertSize(Loc2Insert);//update size
	       NewInsertMaxMin( FreshNode);//update max min
	       return InsertReBalance(Loc2Insert);
	   }
   
   
   /**
    * after insertion, NewInsertSize updates the size fields in nodes that start from point till we reach root 
    * Complexity : O(log(n))
    */
   private void NewInsertSize(AVLNode point) {
       while (point.isRealNode()) {
    	   point.size += 1;
    	   point = point.parent;
       }
   }
   
   
   /**
    * after insertion, function NewInsertMaxMin updates Max and Min fields  
    * Complexity : O(1)
    */
	private void NewInsertMaxMin(AVLNode node) {
	    if (node.key >= Max.key) {
	 	   Max = node;
	    }
	    if (node.key <= Min.key) {
	 	   Min = node;
	    }
	}

   /**
    * InsertReBalance makes sure tree is rebalanced after the insertion
    * Complexity : O(log(n))
    */
   private int InsertReBalance(AVLNode point) {
       if (point.isRealNode()==false) {
           return ZERO_REBALANCES;
       }
       Edge edges = point.TypesOfEdges();
       if ((edges == Edge.Type10 )|| edges ==Edge.Type01) { //AVL Lecture slide 21
    	   point.promote();
           return (InsertReBalance(point.parent)+1); //Problem is either fixed or moved up
       }
       else if ((edges == Edge.Type20 )) {
           return InsertReBalanceType20(point);

       }
       else if ((edges == Edge.Type02 )) {
           return InsertReBalanceType02(point);

       }
       else {
    	   return ZERO_REBALANCES;  
       }
   }
   
   
   /**
    * Function InsertReBalance20 Rebalances tree if condition (edges == Edge.Type20 ) %n
    * is fulfilled in InsertReBalance function
    * Complexity : O(1)
    */
   private int InsertReBalanceType20(AVLNode point) {
       AVLNode right_son = point.right;
       Edge right_edges = right_son.TypesOfEdges(); //what type of rank differences?
       if ((Edge.Type21 ==right_edges  )) {
    	   LeftRotate( point);
              point.demote(); //demote the node which previously was the main root
              return TWO_REBALANCES; // one rebalance complete
       }
       else if ((Edge.Type12==right_edges )) {
               AVLNode FreshRoot = right_son.left; //the anticipated New root of balanced tree
               RightRotate( right_son);
               LeftRotate( point);
               FreshRoot.promote();
               point.demote();
               right_son.demote();
               return Five_REBALANCES;
       }
       else {
               return ZERO_REBALANCES;
       }	   
   
   }
   
   
   /**
    * Function InsertReBalance02 Rebalances tree if condition (edges == Edge.Type02 ) %n
    * is fulfilled in InsertReBalance function
    * Complexity : O(1)
    */
   private int InsertReBalanceType02(AVLNode point) { //AVL Lecture slide 22
       AVLNode left_son = point.left;
       Edge left_edges = left_son.TypesOfEdges(); //what type of rank differences?
       if ((Edge.Type12 ==left_edges  )) {
    	   RightRotate( point);
              point.demote(); //demote the node which previously was the main root
              return TWO_REBALANCES; // one rebalance complete
       }
       else if ((Edge.Type21==left_edges )) {
               AVLNode FreshRoot = left_son.right; //the anticipated New root of balanced tree
               LeftRotate( left_son);
               RightRotate( point);
               FreshRoot.promote();
               point.demote();
               left_son.demote();
               return Five_REBALANCES;
       }
       else {
               return ZERO_REBALANCES;
       }
   }

  
   /**
    * RightRotate function rotates the subtree of given node to the right
    * Complexity : O(1)
    */
   private void RightRotate(AVLNode DesiredRotatePoint) {
	      AVLNode A = DesiredRotatePoint;
	       AVLNode B = A.left;
	       AVLNode C = A.right;
	       AVLNode D = B.left;
	       AVLNode E = B.right;	
	       if (A == Root) { //we assure that A has no parent,no we don't have to move up
	           Root = B;
	       } 
	       else {
	    	   if (A.IsLeftSon()){
	    			A.parent.setLeft(B);
	    	   }
	    		else{
	    			A.parent.setRight(B);
	    		}
	    	   }
	       A.left = E;
	       B.right = A;
	       B.parent = A.parent;
	       A.parent = B;
	       E.parent = A;
	       A.size = (1 + C.size +E.size );
	       B.size = (1+ A.size + D.size );
	       }
   
 
   /**
    * LeftRotate function rotates the subtree of given node to the left
    * Complexity : O(1)
    */
   private void LeftRotate(AVLNode DesiredRotatePoint) {
       AVLNode A = DesiredRotatePoint;
       AVLNode B = A.right;
       AVLNode C = A.left;
       AVLNode D = B.left;
       AVLNode E = B.right;	
       if (A == Root) { //we assure that A has no parent,no we don't have to move up
           Root = B;
       } 
       else {
    	   if (A.IsLeftSon()){
    			A.parent.setLeft(B);
    	   }
    		else{
    			A.parent.setRight(B);
    		}
       }
       B.left = A;
       B.parent = A.parent;
       A.right = D;
       A.parent = B;
       D.parent = A;
       A.size = (1 + C.size +D.size );
       B.size = (1+ A.size + E.size );
       }
   
	   
   

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   * complexity : O(log(n))

   */
   public int delete(int k)
   {
       AVLNode nodeToDelete = helpSearch(Root, k);
       if (!nodeToDelete.isRealNode()) {
           return (-1);
       }
       NewDeleteMaxMin( nodeToDelete);
       AVLNode deletionPoint = deleteThis(nodeToDelete);//returns parent
       NewDeleteSize( deletionPoint);
       return rebalanceAfterDeletion(deletionPoint);
	}
   
   /**
    * before deletion, function NewDeleteMaxMin updates Max and Min fields  
    * Complexity : O(logn)
    */
   private void NewDeleteMaxMin(AVLNode node) {
       if (node.key == Max.key) {
               if (node.left.isRealNode()) {
                   AVLNode nodeAfter = node.left;
                   while (nodeAfter.right.isRealNode()) {
                	   nodeAfter = nodeAfter.right;
                   }
                   Max = nodeAfter;
               } 
               else 
               {
                   Max = node.parent;
               }
           
    	   Max = Max.parent;
       }
       if (node.key == Min.key) {
    	   Min = findSuccessor(Min);
       }
   }
      
   /**
    * deleteThis function deletes the desired node from tree;
    * Complexity: O(log(n))
    */   
    private AVLNode deleteThis(AVLNode node) {
       if (node.right.isRealNode()==false && node.left.isRealNode()==false) {//Leaf Node

		   if (Root==node) {//the leaf we want to delete is the only node in tree
			   Root = EXTERNAL_NODE;
			   Min = EXTERNAL_NODE;
			   Max = EXTERNAL_NODE;
		   }
		   else 
		   {
			   if (node.IsLeftSon()) {//if node is left son
				   node.parent.setLeft(EXTERNAL_NODE);//replace node with external leaf
			   }
			   else {//if node is right son
				   node.parent.setRight(EXTERNAL_NODE);//replace node with external leaf
			   }
		   }
		   return node.parent;
       }
		else if (node.right.isRealNode()==true && node.left.isRealNode()==false) {//Unary on right side //O(1) Complexity
    	   AVLNode exactSon = (AVLNode)node.getRight();//gets right son
		   exactSon.parent=node.parent;
		   return UnaryDeletion( node,  exactSon);  
	   }
		else if (node.right.isRealNode()==false && node.left.isRealNode()==true) {//Unary on left side //O(1) Complexity
			   AVLNode exactSon = (AVLNode)node.getLeft();//gets left son
			   exactSon.parent=node.parent;
			   return UnaryDeletion( node,  exactSon);

		   }
       else if (node.right.isRealNode()==true && node.left.isRealNode()==true) {//internal Node
	        AVLNode succ =findSuccessor(node);
	        if (succ.parent==null) 
	        {
	        	Root=succ.right;
	        	succ.right.parent=succ.parent;
	        }
	        else 
	        {
		        succ.changeParent(succ.right);
	        	succ.right.parent=succ.parent;
	        }
            node.value = succ.value;
            node.key = succ.key;
            node = succ.right;
	        return node.parent;
	   }
       throw new RuntimeException();
       }
    

   /**
    * UnaryDeletion deletes the unary node
    * Complexity : O(1)
    */	  
   private AVLNode UnaryDeletion(AVLNode node, AVLNode exactSon) {
	   if (Root==node) {//top to down arrow connection keeps
		   Root=exactSon;
	   }
	   else {
		   if (node.IsLeftSon()) {//if node is left son
			   node.parent.setLeft(exactSon);
		   }
		   else {//if node is right son
			   node.parent.setRight(exactSon);
		   }  
	   }
	   return node.parent;
   }
   
   /**
    * findSuccessor finds the successor of a given node pnode 
    * Complexity : O(log(n))
    */
   private AVLNode findSuccessor(AVLNode pnode) {
       AVLNode node;
       if (pnode.right.isRealNode()) {
           node = pnode.right;
           while (node.left.isRealNode())
               node = node.left;
       } 
       else {
           node = pnode;
           while ((node.IsLeftSon() ==false) &&(node.parent != null))
               node = node.parent;
           node = node.parent;
       }
       return node;
   }
   
   /**
    * after deletion, NewDeleteSize updates the size fields in nodes that start from point till we reach root 
    * Complexity : O(log(n))
    */
   private void NewDeleteSize(AVLNode point) {
       while (point.isRealNode()) {
    	   point.size =point.size- 1;
    	   point = point.parent;
       }
   } 

   /**
    * rebalanceAfterDeletion function rebalances tree after deletion
    * Complexity : O(log(n))
    */
   private int rebalanceAfterDeletion(AVLNode point) {
       if (point.isRealNode()==false) {
           return ZERO_REBALANCES;
       }
       Edge edges = point.TypesOfEdges();

       if ((edges == Edge.Type22)) { //Case1
           if (point.right.isRealNode()==false && point.left.isRealNode()==false){
                   point.demote();
                   return ONE_REBALANCE + rebalanceAfterDeletion(point.parent);
               }
           if (point.right.isRealNode()==true && point.left.isRealNode()==true){
            	   point.demote();
            	   if (point==Root) {
            		   return ONE_REBALANCE;
            	   }
                   return ONE_REBALANCE + rebalanceAfterDeletion(point.parent);
               }
           return ZERO_REBALANCES; 
               
       }
       else if ((edges == Edge.Type31)) {
               return rebalanceAfterDeletionType31(point);}
        else if ((edges == Edge.Type13)) {
               return rebalanceAfterDeletionType13(point);}
               return ZERO_REBALANCES;
       }
   

   /**
    * This function rebalances our tree when it reaches case 4 deletion learnt in class
    * Complexity : O(log(n))
    * */
   private int rebalanceAfterDeletionType31(AVLNode point) {
       AVLNode right_son = point.right;
       Edge right_edges = right_son.TypesOfEdges(); //what type of rank differences?
       if ((Edge.Type11 ==right_edges  )) {//Case2
    	   LeftRotate(point);
    	   point.demote();
    	   right_son.promote();
    	   return THREE_REBALANCES;
       }
       else if ((Edge.Type21 ==right_edges  )) {//Case3
    	   LeftRotate(point);
    	   point.demote();
    	   point.demote();
    	   return THREE_REBALANCES+ rebalanceAfterDeletion(point.parent);////goes up to rebalancedelete and not 31
       }
       else if ((Edge.Type12 ==right_edges  )) {//Case4
           AVLNode FreshRoot = right_son.left;
           RightRotate(right_son);
           LeftRotate(point);
           FreshRoot.promote();
           point.demote();
           point.demote();
           right_son.demote();
           return SIX_REBALANCES + rebalanceAfterDeletion(point.parent);
       }
       return ZERO_REBALANCES;
   }

   /**
    * This function rebalances our tree when it reaches case 4 deletion learnt in class, symmetric to rebalanceAfterDeletionType31 function
    * Complexity : O(log(n))
    * */

   private int rebalanceAfterDeletionType13(AVLNode point) {
       AVLNode left_son = point.left;
       Edge left_edges = left_son.TypesOfEdges(); //what type of rank differences?
       if ((Edge.Type11 ==left_edges  )) {//Case2
    	   RightRotate(point);
    	   point.demote();
    	   left_son.promote();
    	   return THREE_REBALANCES;
       }
       else if ((Edge.Type12 ==left_edges  )) {//Case3
    	   RightRotate(point);
    	   point.demote();
    	   point.demote();
    	   return THREE_REBALANCES+rebalanceAfterDeletion(point.parent); 
       }
       else if ((Edge.Type21 ==left_edges  )) {//Case4
           AVLNode FreshRoot = left_son.right;
           LeftRotate(left_son);
           RightRotate(point);
           FreshRoot.promote();
           point.demote();
           point.demote();
           left_son.demote();
           return SIX_REBALANCES+rebalanceAfterDeletion(point.parent);
       }
       return ZERO_REBALANCES;
   }   
   
   /**
    * public String min()
    *
    * Complexity : O(1)
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
       return Min.value;
   }

   /**
    * public String max()
    * 
    * Complexity : O(1)
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
       return Max.value;
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * Complexity : O(n)
   */
  public int[] keysToArray()
  {
	  List<Integer> list = new ArrayList<Integer>();
	  list = helpKeysToArray(Root, list);
      int size = size();
	  int[] arr = new int[size];
	  for(int i = 0; i < size; i++) {
		  arr[i] = list.get(i);}
	  return arr;            
  }
  
  /**
   * this recursive function goes through all of the nodes in the tree from smallest to biggest 
   * and retrun a string with all of their keys in that order 
   * Complexity : O(n)
   */  
  private List<Integer> helpKeysToArray(AVLNode node, List<Integer> list) {
	  if (node == EXTERNAL_NODE){
		  return list; 
	  }
	  list = helpKeysToArray((AVLNode) node.getLeft(), list);
	  list.add(node.getKey());
	  return helpKeysToArray((AVLNode) node.getRight(), list);
  }
  
  
  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * Complexity : O(n)
   */
  public String[] infoToArray()
  {
	  List<String> list = new ArrayList<String>();
	  list = helpInfoToArray(Root,list);
	  int size = size();
	  String[] arr = new String[size];
	  for(int i = 0; i < size; i++) {
		  arr[i] = list.get(i);}
	  return arr;                 
  }
  
  /**
   * this recursive function goes through all of the nodes in the tree from smallest to biggest 
   * and retrun a string with all of their keys in that order 
   * Complexity : O(n)
   */  
  private List<String> helpInfoToArray(AVLNode node, List<String> list) {
	  if (node == EXTERNAL_NODE){
		  return list; 
	  }
	  list = helpInfoToArray((AVLNode) node.getLeft(), list);
	  list.add(node.getValue());
	  return helpInfoToArray((AVLNode) node.getRight(), list);
  }
  
  
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    * Complexity: O(1)
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
       return Root.size;
   }
   
     /**
    * public int getRoot()
    * Complexity: O(1)
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
	   {
		   if (empty()){
			   return null;
		   }
		   return Root;
	   } 
   
   
   /**
  * public string split(int x)
  *
  * splits the tree into 2 trees according to the key x. 
  * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
  * postcondition: none
  * Complexity: O(log(n))
  */   
 public AVLTree[] split(int x){
 {
	   IAVLNode help = find(x);
	   AVLTree treeOnLeft = new AVLTree() , treeOnRight= new AVLTree();
	   if(!help.getLeft().isRealNode()) 
		   treeOnLeft = new AVLTree();
	   else {
		   treeOnLeft.Root = twin((AVLNode) help.getLeft());
		   treeOnLeft.Root.rank = help.getLeft().getHeight();
		   treeOnLeft.Root.size = ((AVLNode)help.getLeft()).size;
	   }
	   if(!help.getRight().isRealNode()) 
		   treeOnRight =new AVLTree();
	   else {
		   treeOnRight.Root = twin((AVLNode) help.getRight());
		   treeOnRight.Root.rank = help.getRight().getHeight();
		   treeOnRight.Root.size = ((AVLNode)help.getRight()).size;
	   }
	   try {
	   while (help != Root){
		   if(help.getParent().getLeft().equals(help)) {
			   help=help.getParent();
			   IAVLNode helpsecond =new AVLNode(new AVLNode(),new AVLNode(),help.getKey(),help.getValue());
			   AVLTree helptree = new AVLTree();
			   helptree.Root=twin((AVLNode) help.getRight());
			   if(!helptree.Root.isRealNode())
				   helptree = new AVLTree();
			   treeOnRight.join(helpsecond, helptree);
		   
		   }
		   else {
			   help = help.getParent();
			   IAVLNode helpsecond =new AVLNode(new AVLNode(),new AVLNode(),help.getKey(),help.getValue());
			   AVLTree helptree = new AVLTree();
			   helptree.Root=twin((AVLNode) help.getLeft());
			  
			   if(!helptree.Root.isRealNode())
				   helptree = new AVLTree();
			   treeOnLeft.join(helpsecond, helptree);   
		   }  
		   
	   }}
	   catch (Exception e) {
	   } 
	   AVLTree[] myArray = new AVLTree[2];
	   myArray[0] = treeOnLeft;
	   myArray[1] = treeOnRight;
	   return myArray; 
	   
 	}		}
	   
	/**
	 * returns the node of an item with key k if it exists in the tree.otherwise, returns null
	 * Complexity: O(log(n))
	 */
	 private AVLNode find(int k) {
		   AVLNode later=Root;
		   if(later !=null) {
			  while (later.isRealNode()){
				  if (k == later.getKey()) 
					  return later;
				  else
				  {
					  if (k < later.getKey()) 
						  later=later.left;
					  else 
						  later = later.right;
				  }
			  }
		   }
		   return null;
	 }
	 
	 
	private AVLNode twin(AVLNode node) {
	     if (node == null) {
	         return null;
	     }
	     AVLNode copied = new AVLNode(node.right,node.left,node.key, node.value);
	     copied.setLeft(twin((AVLNode) node.getLeft()));
	     copied.setRight(twin((AVLNode) node.getRight()));
	     return copied;
	 }  


   /**
    * public join(IAVLNode x, AVLTree t)
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	* precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    * Complexity: O(log(n))
    */   
	public int join(IAVLNode x, AVLTree t)
	   {
		   IAVLNode first , secondb;
		   int cnt;
		   if(this.Root==null) {
			   t.insert(x.getKey(), x.getValue());
			   Root=t.Root;
			   return t.Root.getHeight()+1;
		   }
		   if(t.Root ==null) {
			   this.insert(x.getKey(), x.getValue());
			   return Root.getHeight()+1;
		   }
		   int huge1=Root.size;
		   int huge2 = t.size();
		   if((Math.abs(t.getRoot().getHeight() - Root.getHeight())) <= 1) {
			   if(t.getRoot().getKey() > Root.getKey()) {
				   x.setLeft(Root);
				   x.setRight(t.getRoot());
				   newHeight((AVLNode)x);
				   this.Root= (AVLNode) x;
				   t.Root = (AVLNode) x; //
				   ((AVLNode)x).size = (huge1+huge2+1);
				   return 1;
			   }
			   else {
				   x.setLeft(t.getRoot());
				   x.setRight(Root);
				   newHeight((AVLNode)x);
				   this.Root= (AVLNode) x;
				   t.Root = (AVLNode) x; //
				   ((AVLNode)x).size = (huge1+huge2+1);
				   return 1;
				   
			   }
		   }
		   int p= Math.abs(t.getRoot().getHeight() - Root.getHeight());
		   if (t.getRoot().getHeight() > Root.getHeight()) {
			   first = t.getRoot();
			   if(t.getRoot().getKey() > Root.getKey()) {
				   while (first.getHeight() > Root.getHeight()) {
					   first = first.getLeft();
				   }
				   cnt = ((AVLNode)first).size;
				   secondb = first.getParent();
				   x.setLeft(Root);
				   x.setRight(first);
				   x.setParent(secondb);
				   secondb.setLeft(x);
				   newHeight((AVLNode) x);
				   ((AVLNode)x).size = cnt+huge1+1;
				   Root = (AVLNode) t.getRoot();
				   plus((AVLNode)x.getParent());
				   InsertReBalance((AVLNode)x.getParent());

				   return (p+1);
			   }
			   else {
				   while (first.getHeight() > Root.getHeight()) {
					   first = first.getRight();
				   }
				   cnt = ((AVLNode)first).size;
					   secondb = first.getParent();
					   x.setLeft(first);
					   x.setRight(Root);
					   x.setParent(secondb);
					   secondb.setRight(x);
					   newHeight((AVLNode) x);
					   Root = (AVLNode) t.getRoot();
					   ((AVLNode)x).size= (cnt+huge1+1);
					   plus((AVLNode)x.getParent());
					   InsertReBalance((AVLNode)x.getParent());
					   return (p+1);
					   
				   }
			   }
		   else {
			   first=Root;
			   if(t.getRoot().getKey() < Root.getKey()) {
				   while (first.getHeight() > t.getRoot().getHeight()) {
					   first = first.getLeft();
				   }

				   cnt = ((AVLNode)first).size;
				   secondb=first.getParent();
				   x.setLeft(t.getRoot());
				   x.setRight(first);
				   x.setParent(secondb);
				   secondb.setLeft(x);
				   newHeight((AVLNode) x);
				   ((AVLNode)x).size = (cnt+huge2+1);
				   plus((AVLNode)x.getParent());
				   InsertReBalance((AVLNode)x.getParent());

				   return (p+1);
				   
			   }
			   else {
				   while (first.getHeight() > t.getRoot().getHeight()) {
					   first = first.getRight();
				   }

				   cnt = ((AVLNode)first).size;
				   secondb=first.getParent();
				   x.setLeft(first);
				   x.setRight(t.getRoot());
				   x.setParent(secondb);
				   secondb.setRight(x);
				   newHeight((AVLNode) x);
				   ((AVLNode)x).size = (cnt+huge2+1);
				   plus((AVLNode)x.getParent());
				   InsertReBalance((AVLNode)x.getParent());
				   return (p+1);
				   
			   }
		   }

	   }   
	   
	 /**
	  * updates the height of the nodes after joining them from x to root
	  * Complexity: O(log(n))
	  */
	 
	 private void newHeight( AVLNode node )
	 {
	    while ( node != null )
	    {
	       node.rank = Math.max(node.left.rank, node.right.rank ) + 1 ;     
	       node = node.parent;            
	    }
	    
	 }
	 /**

	  * updates the size of the nodes after joining them,from node x to root
	  * Complexity: O(log(n))
	  */
	 private void plus(AVLNode node) {
		   while (node != null) {
			   node.size = node.left.size + node.right.size + 1;
			   node = node.parent;
		   }
	 }	   

   
   /***************************************IAVLNode
   
   
   
	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}

	
	/***************************************IAVLNode
	
	
   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{

      Integer key;
      String value;
      int size;
      int rank;
      AVLNode parent;
      AVLNode left;
      AVLNode right;
      
  
	      /**
	        first constructor 
	        Complexity : O(1) 
	       */
	     private AVLNode() { 
	    	  key = -1;
	          rank = -1;
	          value = null;
	          parent =null;
	          size = 0;
	          left = null;
	          right = null;
	          
	      }
	      /**
	        second constructor 
	        Complexity : O(1) 
	       */
	     private AVLNode(AVLNode node) {
				this.key=-1;
				this.size=0;
				this.rank=-1;
				this.parent=node;
				this.left=null;
				this.right=null;}
	     
	      /**
	        third constructor 
	        Complexity : O(1) 
	       */
	    private AVLNode(int key, String value, AVLNode parent) {
	        this.key = key;
	        this.value = value;
	        this.parent = parent;
	        size = 1;
	        rank = 0;
	        left = EXTERNAL_NODE;
	        right = EXTERNAL_NODE;
	    }


	      /**
        fourth constructor 
        Complexity : O(1) 
       */
	      private AVLNode(AVLNode right, AVLNode left,int key, String value) {
				this.right=right;
				this.left=left;
				this.key=key;
				this.value=value;
				this.size=1;
				this.rank=0;
				if (this.left==null)
					this.left=new AVLNode(this);
				if(this.right==null)
					this.right=new AVLNode(this);
				if (!this.left.isRealNode())
					this.left=new AVLNode(this);
				if (!this.right.isRealNode())
					this.right=new AVLNode(this);

				this.parent=null;
			}
	      
	      
	    /**
	    Complexity : O(1)
	    */
		public int getKey()
			{
	            return key;
	        }
			

        /**
           Complexity : O(1)
         */		
		public String getValue()
		{
            return value;
        }



		 /**
           Complexity : O(1)
         */
		public void setLeft(IAVLNode node)
		{
			left = (AVLNode) node;
		}
		
		
		
        /**
           Complexity : O(1)
         */
		public IAVLNode getLeft()
		{
            return left;
		}
		
		
		 /**
           Complexity : O(1)
         */
		public void setRight(IAVLNode node)
		{
			right = (AVLNode) node; 
		}
		
		
		
        /**
           Complexity : O(1)
         */		
		public IAVLNode getRight()
		{
			return right; 
		}
		
		
		 /**
           Complexity : O(1)
         */
		public void setParent(IAVLNode node)
		{
			parent = (AVLNode) node; 
		}
		
		
		
        /**
           Complexity : O(1)
         */
		public IAVLNode getParent()
		{
			return parent; 
		}
		
		
		
		// Returns True if this is a non-virtual AVL node
        /**
           Complexity : O(1)
         */
		public boolean isRealNode()
		{
            return (this!=EXTERNAL_NODE);
		}
		
		
		 /**
           Complexity : O(1)
         */
		public void setHeight(int height)
		{
			rank = height; 
		}
    
    
		 /**
          Complexity : O(1)
         */
		public int getHeight()
		{
			return rank; 
		}
    
    
    
    //***************************Extra PRIVATE Functions
		private Edge TypesOfEdges() {
			int EdgeIsRight = (rank - right.rank);//rank difference between current node and its right son
			int EdgeIsLeft = (rank - left.rank); //rank difference between current node and its left son
			if (EdgeIsLeft == 1) {//1-0
				if (EdgeIsRight == 0) {
				return Edge.Type10;
			}}
			if (EdgeIsLeft == 0) {//0-1
				if (EdgeIsRight == 1) {
				return Edge.Type01;
			}}
			if (EdgeIsLeft == 1) {//1-1
				if ( EdgeIsRight == 1) {
				return Edge.Type11;
			}}
			if (EdgeIsLeft == 2) {//2-0
				if(EdgeIsRight == 0) {
				return Edge.Type20;
			}}
			if (EdgeIsLeft == 0) {//0-2
				if( EdgeIsRight == 2) {
				return Edge.Type02;
			}}
			if (EdgeIsLeft == 1) {//1-2
				if( EdgeIsRight == 2) {
				return Edge.Type12;
			}}
			if (EdgeIsLeft == 2) {//2-1
				if( EdgeIsRight == 1) {
				return Edge.Type21;
			}}
			
			
			//added cases at deletion
			if (EdgeIsLeft == 2) {//2-2
				if( EdgeIsRight == 2) {
				return Edge.Type22;
			}}
			if (EdgeIsLeft == 1) {//1-3
				if( EdgeIsRight == 3) {
				return Edge.Type13;
			}}
			
			if (EdgeIsLeft == 3) {//3-1
				if( EdgeIsRight == 1) {
				return Edge.Type31;
			}}			

			
			return Edge.whatever;
		}
    
 
    /**
     * IsLeftSon function return if the node is its parents left son
     * Complexity : O(1)
     */    
		
    private boolean IsLeftSon() { 
        return this == parent.left ;
        }
    
    /**
     * promote function promotes the AVLNode's rank by 1
     * Complexity : O(1)
     */
    private void promote() {
  		rank=rank+1;
    }
    
    
    /**
     * demote function decreases the AVLNode's rank by 1
     * Complexity : O(1)
     */
    private void demote() {
  		rank=rank-1;
    }
    /**
     * Updates the son of parent
     * Complexity : O(1) 
     */
    private void changeParent(AVLNode node) {
            if (IsLeftSon()) {
                parent.left = node;
            } 
            else 
            {
                parent.right = node;
            }
    }  
}
}
