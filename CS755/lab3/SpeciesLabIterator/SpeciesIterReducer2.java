// 
 // Author - Jack Hebert (jhebert@cs.washington.edu) 
 // Copyright 2007 
 // Distributed under GPLv3 
 // 
// Modified - Dino Konstantopoulos (dinok@bu.edu)
// Copyright 2010, BU MET CS 755 Cloud Computing
// Distributed under the "If it works, remolded by Dino Konstantopoulos, 
// otherwise no idea who did! And by the way, you're free to do whatever 
// you want to with it" dinolicense
// 
package BU.MET.CS755;

 import java.io.IOException; 
 import java.util.Iterator; 
  
 import org.apache.hadoop.io.WritableComparable; 
 import org.apache.hadoop.mapred.MapReduceBase; 
 import org.apache.hadoop.mapred.OutputCollector; 
 import org.apache.hadoop.mapred.Reducer; 
 import org.apache.hadoop.mapred.Reporter; 
 import org.apache.hadoop.io.Text; 
  
 public class SpeciesIterReducer2 extends MapReduceBase implements Reducer<WritableComparable, Text, Text, Text> { 
  
   public void reduce(WritableComparable key, Iterator values, 
                      OutputCollector output, Reporter reporter) throws IOException { 
     double score = 0; 
     String outLinks = ""; 
  
     while (values.hasNext()) { 
       String curr = ((Text)values.next()).toString(); 
       int colon = curr.indexOf(":"); 
       int space = curr.indexOf(" "); 

       if ((colon > -1)) { 
         String presScore = curr.substring(0, colon); 
         try { 
           score += Double.parseDouble(presScore); 
           outLinks = curr.substring(colon + 1); 
           continue; 
         } catch (Exception e) { 
           ; 
         } 
       } 

       if (space > -1) { 
         outLinks = curr; 
       } else { 
         score += Double.parseDouble(curr); 
       } 
     } 

     String toEmit; 
     if (outLinks.length() > 0) { 
       toEmit = (new Double(score)).toString() + ":" + outLinks; 
     } else { 
       toEmit = (new Double(score)).toString(); 
     } 
     output.collect(key, new Text(toEmit)); 
   } 
 } 
 