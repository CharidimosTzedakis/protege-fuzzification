package org.protege.editor.owl.fuzzification;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Fuzzifier extends JPanel {

	private int pixelsPerUnit;
	private String MembershipFunction;
	private int numberOfFuzzySets;
	
	public void paintComponent (Graphics comp){
				
	   Graphics2D comp2D= (Graphics2D) comp;		
	   comp2D.setBackground( Color.black );
	   comp2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	   comp.setColor(Color.white);
	   comp.fillRect(0, 0, 500, 200);
	   comp.setColor(Color.black);
	   comp2D.drawLine(20, 170, 480, 170);
	   comp2D.drawLine(20, 20, 20, 170 );
	
	   //number of PartionPoints    
       int partitionPoints[];	   	   
	   partitionPoints=new int[numberOfFuzzySets+1]; 	   
	   for (int i=0;i<partitionPoints.length;i++) partitionPoints[i]=20*i;
		   
	   		   
	   if (MembershipFunction.equals("Trapezoidal") ){
		
		   comp.setColor(Color.blue);		   		   
		   for (int i=0;i<numberOfFuzzySets-1; i=i+2 ){
			   
			   //draw the tree egdes of the trapezoidal
			   comp2D.drawLine(  20+3*(Math.round(  partitionPoints[i])) , 120, 20+ 3*(Math.round(  partitionPoints[i]+20  ) ), 120);
			   comp2D.drawLine( 20+ 3*(Math.round(  partitionPoints[i]+20 )  ), 120, 20+3*Math.round( partitionPoints[i]+40 ), 170);
			   comp2D.drawLine(20+ 3*(Math.round( partitionPoints[i]+40  )), 120, 20+3*( Math.round( partitionPoints[i])+20 ), 170);
			   
		   }	
		   comp2D.drawLine((20+3*Math.round(partitionPoints[partitionPoints.length-2])) , 120, 20+3*Math.round(partitionPoints[partitionPoints.length-2]+20),120);
		   comp2D.drawLine(20+ 3*(Math.round(partitionPoints[partitionPoints.length-1])), 170, 20+3*Math.round(partitionPoints[partitionPoints.length-1]),120);
	
		   }
		
	   if (MembershipFunction.equals("Triangular") ){
			
			comp.setColor(Color.red);
			for (int i=0;i<numberOfFuzzySets; i=i+1 ){
   
			   comp2D.drawLine( 20+ 3*(Math.round(  partitionPoints[i] )  ), 120, 20+3*Math.round( partitionPoints[i]+20 ), 170);
			   comp2D.drawLine(20+ 3*(Math.round( partitionPoints[i]+20  )), 120, 20+3*( Math.round( partitionPoints[i]) ), 170);
			      
			}
			   comp2D.drawLine(20+ 3*(Math.round(partitionPoints[partitionPoints.length-1])), 170, 20+3*Math.round(partitionPoints[partitionPoints.length-1]),120);

						
		}		
	}
		
	Fuzzifier (int numberOfFuzzySets, String MembershipFunction){
		
		
		int lowerBoundSet;     
		int upperBoundSet;
	
		float definiteIntervalLength;
		float fuzzyIntervalLength;
		// y=22.5-0.1*x
				
		definiteIntervalLength=15.0f;
		fuzzyIntervalLength=27.5f;
	
		int IntervalLength=100;
		pixelsPerUnit=300/IntervalLength;
		this.MembershipFunction=MembershipFunction;
		this.numberOfFuzzySets=numberOfFuzzySets;
		
	}
}
