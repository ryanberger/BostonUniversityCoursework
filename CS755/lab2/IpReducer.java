package BU.MET.CS755;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Counts all of the hits for an ip. Outputs all ip's
 */
public class IpReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> 
{

  public void reduce(Text ip, Iterator<IntWritable> counts,
      OutputCollector<Text, IntWritable> output, Reporter reporter)
      throws IOException {
    
    int totalCount = 0;
    
    // loop over the count and tally it up
    while (counts.hasNext())
    {
      IntWritable count = counts.next();
      totalCount += count.get();
    }
    
    output.collect(ip, new IntWritable(totalCount));
  }

}
