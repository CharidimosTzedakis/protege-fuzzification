package org.protege.editor.owl.fuzzification;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;


import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.OWLEditorKitFactory;
import org.protege.editor.owl.model.*;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.protege.editor.core.editorkit.EditorKit;
import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.workspace.Workspace;
import org.semanticweb.owlapi.apibinding.OWLManager;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;


public class FuzzificationViewComponent extends AbstractOWLViewComponent implements ActionListener, TableModelListener {
    private static final long serialVersionUID = -4515710047558710080L;
    
    private static final Logger log = Logger.getLogger(FuzzificationViewComponent.class);
    OWLModelManager ProtegeManager;
    OWLOntology CurrentOntology;
    TableModel ModelForTable;
    AbstractSet ClassSet;
    AbstractSet <OWLNamedIndividual> IndividualSet;
    String columnNames[]= {"Individuals","GradeOfMembership"};
    Object [][] rowData;
    JTable individualsTable;
    JTable databaseTable;
    
    GroupLayout layout;
    JButton SaveButton,LoadButton;
    OWLAnnotation [] GradeOfMembershipValue;   //matrix with GradeOfMembershipValues
    Fuzzifier f;
    JComboBox TypeOfMembershipFunctionComboBox;
    JLabel MembershipFunctionTypeLabel;
    JLabel NumberOfIntervalsLabel;
    JFormattedTextField NumberofIntervalsField;
    JButton ChangeButton;
    JButton ClearButton;
    JTextArea QueryField;
    JButton executeQuery;
    JButton clearQuery;
    
    JPanel generalPanel;
    JScrollPane generalScrollPane;
    JTabbedPane fuzzificationTabbedPane;
    Panel fuzzificationTabPanel;
	Panel databaseTabPanel;
    
    protected void LayoutComponents(GroupLayout grouplayout , JTable individualsTable,JButton save, JButton load, JPanel PlotPanel,
    								JComboBox TypeOfMembershipFunction, JButton change, JButton clear, JFormattedTextField NumberofIntervalsFieldComp,
    								JTable databaseTable) 
    {
            	
    	fuzzificationTabPanel.removeAll();
        
        //fuzzificationTabPanel = new Panel();
		databaseTabPanel = new Panel();
		fuzzificationTabbedPane = new JTabbedPane();
        generalScrollPane =new JScrollPane(fuzzificationTabPanel);

        
		fuzzificationTabbedPane.addTab("Database", null, databaseTabPanel,"Retrieve data for fuzzification");
        fuzzificationTabbedPane.addTab("Fuzzification", null, fuzzificationTabPanel,"configure fuzzification parameters");
        generalPanel.add(fuzzificationTabbedPane);
        //fuzzificationTabPanel.add(generalScrollPane);

        
    	//this.removeAll();
        individualsTable.setGridColor(Color.black);
        individualsTable.setFillsViewportHeight(false);
        individualsTable.setShowGrid(true);
        
        databaseTable.setGridColor(Color.black);
        databaseTable.setFillsViewportHeight(false);
        databaseTable.setShowGrid(true);
            	
    	
        JScrollPane individualsScrollPane = new JScrollPane(individualsTable);
        JScrollPane databaseScrollPane = new JScrollPane(databaseTable);
        JScrollPane QueryScollPane = new JScrollPane(QueryField);
                
    	
        PlotPanel.setBackground( Color.black );
    	PlotPanel.setMaximumSize( new Dimension(500, 200)  );
    	   	   	    	
    	MembershipFunctionTypeLabel= new JLabel("Membership Function Type:");
    	NumberOfIntervalsLabel=new JLabel("Number of Intervals:");
    	
    	// H O R I Z O N T A L
    	grouplayout.setHorizontalGroup(grouplayout.createSequentialGroup()
    			
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,10,20)
                .addGroup(grouplayout.createParallelGroup()
                	.addComponent(individualsScrollPane,GroupLayout.PREFERRED_SIZE, 500 ,GroupLayout.PREFERRED_SIZE)
                    .addGroup(grouplayout.createSequentialGroup()
                			.addComponent(save)
                			.addComponent(load)                	                              
                	)
                	.addComponent(QueryScollPane)
                	.addGroup(grouplayout.createSequentialGroup()
                    	    .addComponent(executeQuery)
                        	.addComponent(clearQuery)
                    )
                	.addGroup(grouplayout.createSequentialGroup()
                			.addComponent(databaseScrollPane)
                    )
                )                                    	
                //distance between the two panels
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,180,180)
                .addGroup(grouplayout.createParallelGroup()
                	.addComponent(PlotPanel,GroupLayout.PREFERRED_SIZE, 500 ,GroupLayout.PREFERRED_SIZE)                		               	        
                    .addGroup(grouplayout.createSequentialGroup()
                    		.addComponent(MembershipFunctionTypeLabel)
                    		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,40,40)
                    		.addComponent(TypeOfMembershipFunctionComboBox)                                   			               	               	 
                    )
                    .addGroup(grouplayout.createSequentialGroup()
                    		.addComponent(NumberOfIntervalsLabel)
                    		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,40,40)
                    		.addComponent(NumberofIntervalsFieldComp,100,100,100)
                    )
                    .addGroup(grouplayout.createSequentialGroup()
                    		.addComponent(change)
                    		.addComponent(clear)
                    )                
                )
    	);          
    	grouplayout.linkSize(SwingConstants.HORIZONTAL, save, load);
    	grouplayout.linkSize(SwingConstants.HORIZONTAL, MembershipFunctionTypeLabel, NumberOfIntervalsLabel);
    	grouplayout.linkSize(SwingConstants.HORIZONTAL, TypeOfMembershipFunctionComboBox, NumberofIntervalsFieldComp);
    	grouplayout.linkSize(SwingConstants.HORIZONTAL, individualsScrollPane, QueryScollPane);
    	

    	
    	// V E R T I C A L   	
            grouplayout.setVerticalGroup(grouplayout.createSequentialGroup()	
            	
            	.addGroup(grouplayout.createParallelGroup()       
            			.addComponent(individualsScrollPane,GroupLayout.PREFERRED_SIZE, 200 ,GroupLayout.PREFERRED_SIZE)
       		            .addComponent(PlotPanel,GroupLayout.PREFERRED_SIZE, 200 ,GroupLayout.PREFERRED_SIZE)
            	)
            	.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,40,40)
                .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(save)
                        .addComponent(load)
                	    .addComponent(MembershipFunctionTypeLabel)
                        .addComponent(TypeOfMembershipFunctionComboBox)
                ) 
               .addGap(10)
               .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                	    .addComponent(NumberOfIntervalsLabel)
                    	.addComponent(NumberofIntervalsFieldComp,35,35,35) 	                	               	     
                )                                                                                                          
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,40,40)
                .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                	    .addComponent(change)
                    	.addComponent(clear)
                    	.addComponent(QueryScollPane,100,100,100)
                )            
                .addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                	    .addComponent(executeQuery)
                    	.addComponent(clearQuery)
                )
                .addGap(80)
            	.addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    	.addComponent(databaseScrollPane,150,150,150)
                )           
            );
              
       //fuzzificationTabPanel.setLayout(grouplayout);
       //generalPanel.setLayout(new BorderLayout() );
       setLayout(new BorderLayout());
       add(generalPanel);

    }
    
    @Override
    protected void disposeOWLView() {
    }

    
    @Override
    protected void initialiseOWLView() throws Exception {
        log.info("Example View Component initialized");
     
        OWLEditorKit EditorKit=this.getOWLEditorKit();
        ProtegeManager = EditorKit.getOWLModelManager();
        CurrentOntology =ProtegeManager.getActiveOntology();
		ClassSet= (AbstractSet) CurrentOntology.getClassesInSignature();
        IndividualSet= (AbstractSet) CurrentOntology.getIndividualsInSignature();
		               
        //initialize GradeOfMembershipValue
        OWLDataFactory df = ProtegeManager.getOWLDataFactory();
        OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        
        GradeOfMembershipValue=new OWLAnnotation[IndividualSet.size() ];
        for(int i=0;i<GradeOfMembershipValue.length;i++)
        GradeOfMembershipValue[i]= df.getOWLAnnotation(label, df.getOWLLiteral("0.000",df.getFloatOWLDatatype() ) );

        //read from file        
        //File file = new File("/Users/haridimos/Desktop/pizza.owl");  
        //OWLManager manager= new OWLManager();
        //OWLOntologyManager manager =manager.createOWLOntologyManager(); 
        //OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);
        //OWLManager=CurrentOntology.getOWLOntologyManager();
        
        //set containing classes in the Ontology
               
        SaveButton = new JButton("Save"); 
        SaveButton.addActionListener(this);
        SaveButton.setActionCommand("Save");
        
        LoadButton = new JButton("Load"); 
        LoadButton.addActionListener(this);
        LoadButton.setActionCommand("Load");
        
        ChangeButton = new JButton("Change"); 
        ChangeButton.addActionListener(this);
        ChangeButton.setActionCommand("Change");
        
        ClearButton = new JButton("Clear"); 
        ClearButton.addActionListener(this);
        ClearButton.setActionCommand("Clear");
        
        executeQuery = new JButton("Query");
        executeQuery.addActionListener(this);
        executeQuery.setActionCommand("executeQuery");

        clearQuery = new JButton("Clear");
        clearQuery.addActionListener(this);
        clearQuery.setActionCommand("clearQuery");
        
    	QueryField= new JTextArea(30,20);
       
        String[] FunctionTypes={"Trapezoidal","Triangular" };       
        TypeOfMembershipFunctionComboBox = new JComboBox(FunctionTypes);
        TypeOfMembershipFunctionComboBox.setMaximumSize(new Dimension(20,100) );
        TypeOfMembershipFunctionComboBox.setActionCommand("SetMembershipFunction");
        TypeOfMembershipFunctionComboBox.addActionListener(this);
               
    	//NumberFormat SmallInteger=new NumberFormat();
    	NumberofIntervalsField= new JFormattedTextField ();
    	NumberofIntervalsField.addActionListener(this);
    	//NumberofIntervalsField.setActionCommand("Change Interval");
        
                
        ModelForTable=new AbstractTableModel() {
            		
        					
        			public String getColumnName(int col) {
            			return columnNames[col].toString();
            		}
            		public int getRowCount() { return rowData.length; }
            		public int getColumnCount() { return columnNames.length; }
            		public Object getValueAt(int row, int col) {
            			return rowData[row][col];
            		}
            		public boolean isCellEditable(int row, int col)
            			{ return true; }
            		public void setValueAt(Object value, int row, int col) {
            			rowData[row][col] = value;
            			fireTableCellUpdated(row, col);
            }
        };
        ModelForTable.addTableModelListener(this);

        String [][]initialDataForIndividualsTable=new String [4][2];
        individualsTable=new JTable(initialDataForIndividualsTable,columnNames);
        
        String [][]initialDataForDatabaseTable=new String [4][3];
        String [] initialColumns = new String [3];
        for (int i=0;i<initialColumns.length;i++) initialColumns[i]="";
        databaseTable = new JTable(initialDataForDatabaseTable,initialColumns);
        
        //table.setGridColor(Color.black);
        //Dimension d= new Dimension(50,50);
        //table.setPreferredSize(d ); 
        //table.setPreferredScrollableViewportSize(new Dimension(50, 70));
        
        //layout.setAutoCreateGaps(true);
        //layout.setAutoCreateContainerGaps(true);
        
        generalPanel = new JPanel(new BorderLayout());
        //generalPanel.setBackground( new Color(237,237,237) );
        fuzzificationTabPanel = new Panel();
        layout = new GroupLayout (fuzzificationTabPanel);
        f=new Fuzzifier(0,"initialize");       
        
        this.LayoutComponents(layout, individualsTable, SaveButton, LoadButton,f,TypeOfMembershipFunctionComboBox,
        		              ChangeButton, ClearButton,NumberofIntervalsField, databaseTable);
                
        //this.setLayout(new BorderLayout());
        //this.add(generalScrollPane);

                   
        //old code
        //layout = new GroupLayout(this);
        //this.setLayout(layout);     
    }
    
    public void actionPerformed(ActionEvent e) {
      	
		
		ClassSet= (AbstractSet) CurrentOntology.getClassesInSignature();
        IndividualSet= (AbstractSet) CurrentOntology.getIndividualsInSignature();
        Iterator i=ClassSet.iterator();
        Iterator IndividualIterator=IndividualSet.iterator();
        
		OWLDataFactory df = ProtegeManager.getOWLDataFactory();
        OWLAnnotationProperty label = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        OWLAnnotationProperty comment=df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());

   		
    	if ("Save".equals(e.getActionCommand())) {
    	           
    				//GradeOfMembership comment annotation for annotationAssertionAxiom  				
    				OWLAnnotation GradeOfMembershipName=df.getOWLAnnotation(comment, df.getOWLLiteral("GradeOfMembership", "en" ) ); 
    				
    				//adds the annotation to the individuals
            		IRI CurrentIndividualIRI;
            		int k=0;
            		for (OWLNamedIndividual individual:IndividualSet ) {
            			  
            			  CurrentIndividualIRI=individual.getIRI();
    	            	  log.info("The Iri of the individual is: "+ CurrentIndividualIRI.toString() );
    	            		  	            	 	  	            	
    	            	  //set that contains all Annotation Axioms of the current Individual  
    	            	  Set <OWLAnnotationAssertionAxiom> AxiomSet=CurrentOntology.getAnnotationAssertionAxioms(CurrentIndividualIRI);
    	        		      	        		  
    	            	  //remove the old GradeOfMemberShip Annotation
    	        		  if (AxiomSet!=null)   
    	        		     for (OWLAnnotationAssertionAxiom Axiom:AxiomSet ) {
    	        			     Set <OWLAnnotation> set= Axiom.getAnnotations();
    	        		
    	        			     if (set!=null)
    	        			        for (OWLAnnotation annotation : set){
    	        				        if (annotation.getValue() instanceof OWLLiteral) {
              		                       OWLLiteral literal = (OWLLiteral) annotation.getValue();
              		                       log.info( "The size of the AnnotationSet is:"+set.size() );
              		                       log.info( literal.toString() );
              		                       if ( literal.equals( df.getOWLLiteral("GradeOfMembership", "en" ) ) ) { 
              		                	       log.info("The individual has GradeOfMemberShip Annotation");
              		                    	   ( CurrentOntology.getOWLOntologyManager() ).removeAxiom(CurrentOntology,Axiom); 
              		                       
              		                       }
    	        				        }
    	        			        }
    	        		   } 	
    	            	//the individual doesnt have any annotation, so we must initialize GradeOfMembership Annotation  	            		  
    	            	OWLAxiom AnnotationAxiom1 = df.getOWLAnnotationAssertionAxiom(CurrentIndividualIRI, GradeOfMembershipValue[k] );	
    	            		
    	            	//Annotated AnnotationAssertionAxiom    	            		
    	            	AbstractSet <OWLAnnotation>AnnotationSet2 = new HashSet<OWLAnnotation>();
    	            	AnnotationSet2.add(GradeOfMembershipName);
    	            	OWLAxiom AnnotatedAxiom=AnnotationAxiom1.getAnnotatedAxiom(AnnotationSet2);
    	            	ProtegeManager.applyChange(new AddAxiom(CurrentOntology, AnnotatedAxiom));
            		    k++; //counter for the number of the individuals
            		}		
    	}
    	
    	
    	if ( "Load".equals(e.getActionCommand()) ) {
    		//put the value of the individuals in the array to show
    		int count=0;
    		while (IndividualIterator.hasNext() ) { count++; IndividualIterator.next(); }
    		rowData=new Object[count][2];

    		int k=0;
            //load the AssertionAnnotationAxiom that has comment "GradeOfMembership",@en
    		for (OWLNamedIndividual indv : CurrentOntology.getIndividualsInSignature()) {
            		          	
            	   //set that contains all Annotation Axioms of the current Individual  
          	       Set <OWLAnnotationAssertionAxiom> AxiomSet=CurrentOntology.getAnnotationAssertionAxioms( indv.getIRI() );
      		      	        		  
          	       //remove the old GradeOfMemberShip Annotation
      		       if (AxiomSet!=null)   
      		          for (OWLAnnotationAssertionAxiom Axiom:AxiomSet ) {
      			          Set <OWLAnnotation> set= Axiom.getAnnotations();
      		
      			          if (set!=null)
      			             for (OWLAnnotation annotation : set){
      				             if (annotation.getValue() instanceof OWLLiteral) {
    		                            OWLLiteral literal = (OWLLiteral) annotation.getValue();
    		                            log.info( "The size of the AnnotationSet is:"+set.size() );
    		                            log.info( literal.toString() );
    		                            if ( literal.equals( df.getOWLLiteral("GradeOfMembership", "en" ) ) ) { 
    		                	            log.info("The individual has GradeOfMemberShip Annotation");
    		                    	        //get the value of the AnnotationAssertionAxiom (GradeOfMemberShip Annotation)
    		                	            OWLLiteral val = (OWLLiteral) Axiom.getValue();
    		                	            //.....
    		                	            if (val.isFloat() && k<count) {
                		            	  		log.info("Iteration Number:"+k);
                		                		StringBuffer s= new StringBuffer ( indv.getIRI().toQuotedString() );
                		            	  		int l=0;
                		            	  		while (l<s.length() ) {
                		            	  			
                		            	  			char c=s.charAt(l);
                		            	  			if (c=='#') { s.delete(0,l+1);l=0; }
                		            	  			if ( l==s.length()-1 )  s.deleteCharAt(l); 
                		            	  			l++;
                		            	  		}
                		            	  			
                		                		rowData[k][0]=s;
                		                        rowData[k][1]=val;
                		                        k++;         		                        
                		                  }
                		                }  	                 
    		                       }
      				             }
      			             }
      		        }
            	  	
    		        //checking to see if the data is in rowData,in order to be put in a matrix
                    OWLLiteral literal;
                    for (int p=0;p<count;p++) {
            	         literal=(OWLLiteral) rowData[p][1];
            	         log.info("The value of the Individuals is:"+literal.parseFloat()  );
                    }
            
                    individualsTable= new JTable(ModelForTable);
                        
                    literal=(OWLLiteral) individualsTable.getValueAt(1,1);
                    log.info("The value that JTable contains in (1,1) is:"+literal.parseFloat()  );
                       
                    this.LayoutComponents(layout, individualsTable, SaveButton, LoadButton,f, TypeOfMembershipFunctionComboBox,
                    		ChangeButton, ClearButton, NumberofIntervalsField, databaseTable);	    
    	   }     
    
    	  // if ( "SetMembershipFunction".equals(e.getActionCommand())  ){
    		
    		   //JComboBox cb = (JComboBox)e.getSource();
    		   //String TypeOfMembershipFunction = (String)cb.getSelectedItem();
    		   //create the cuurent fizifier
    		   //f=new Fuzzifier(3, TypeOfMembershipFunction);
               //this.LayoutComponents(layout, table, SaveButton, LoadButton,f, TypeOfMembershipFunctionComboBox, ChangeButton, ClearButton);	    
               
    	  //}   
    	  //Change button pressed 
    	   if ( "Change".equals(e.getActionCommand())  ){
    		   
    		  String NumberOfIntervalsString = NumberofIntervalsField.getText();
    		  String TypeOfMembershipFunction = (String) TypeOfMembershipFunctionComboBox.getSelectedItem();
    		  
    		  if ( !NumberOfIntervalsString.equals("") ) {
    			  int NumberOfIntervals = Integer.parseInt(NumberOfIntervalsString );  		  
    			  f=new Fuzzifier(NumberOfIntervals, TypeOfMembershipFunction);
    			  this.LayoutComponents(layout, individualsTable, SaveButton, LoadButton,f, TypeOfMembershipFunctionComboBox,
    					  ChangeButton, ClearButton,NumberofIntervalsField,databaseTable);	        		 
    		  }
    	   }
    	  
    	  //Clear Button pressed
    	  if ( "Clear".equals(e.getActionCommand())  ){
    		
    		  f=new Fuzzifier(0,"initialize");
    		  NumberofIntervalsField.setText(null);
    	      this.LayoutComponents(layout, individualsTable, SaveButton, LoadButton,f,TypeOfMembershipFunctionComboBox,
    	    		  ChangeButton, ClearButton,NumberofIntervalsField,databaseTable );

    	  }
       
    	  if ( "executeQuery".equals(e.getActionCommand())  ){
      		
    		  DatabaseConnection dbConnection = new DatabaseConnection();
    		  String sqlstatement = QueryField.getText();   		  
    		  dbConnection.SQLQuery(sqlstatement);
    		  
    		  databaseTable = new JTable(dbConnection.data,dbConnection.columnNames);
    		  this.LayoutComponents(layout, individualsTable, SaveButton, LoadButton,f,TypeOfMembershipFunctionComboBox,
    	    		  ChangeButton, ClearButton,NumberofIntervalsField,databaseTable );

    	  }
       
    	  if ( "clearQuery".equals(e.getActionCommand())  ){
    		  QueryField.setText("");   		  
    	  }   
    }
        
    public void tableChanged (TableModelEvent e){
    
    	int row =e.getFirstRow();
    	int column=e.getColumn();
    	OWLDataFactory df = ProtegeManager.getOWLDataFactory();
    	
    	TableModel model= (TableModel) e.getSource();   	
    	String s=(String) model.getValueAt(row,column);
    	
    	StringBuffer buffer= new StringBuffer ( s );
  		int l=0;
  		while (l<buffer.length() ) {
  			
  			char c=buffer.charAt(l);
  			if (c=='\"') buffer.deleteCharAt(l);
  			if (c=='^') { buffer.delete(l-1,buffer.length() ); } 
  			l++;
  		}
    	log.info("The buffer is:"+ buffer.toString() );
  		
    	OWLLiteral data=df.getOWLLiteral( buffer.toString() ,df.getFloatOWLDatatype()  );
    	GradeOfMembershipValue[row]= df.getOWLAnnotation( df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()), data ) ;   			                                  
    }
}
