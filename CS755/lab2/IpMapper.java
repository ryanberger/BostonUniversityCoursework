package BU.MET.CS755;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * Mapper that takes a line from an Apache access log and emits the IP with a
 * count of 1. This can be used to count the number of times that a host has
 * hit a website.
 */
public class IpMapper extends MapReduceBase 
                        implements Mapper<LongWritable, Text, Text, IntWritable>
{

  // Regular expression to match the IP at the beginning of the line in an
  // Apache access log
  private static final Pattern ipPattern = Pattern.compile("^([\\d\\.]+)\\s");

  // Reusable IntWritable for the count
  private static final IntWritable one = new IntWritable(1);
  
  public void map(LongWritable fileOffset, Text lineContents,
      OutputCollector<Text, IntWritable> output, Reporter reporter)
      throws IOException {
    
    // apply the regex to the line of the access log
    Matcher matcher = ipPattern.matcher(lineContents.toString());
    if(matcher.find())
    {
      // grab the IP
      String ip = matcher.group(1);
      
      // output it with a count of 1
      output.collect(new Text(ip), one);
    }
  }

}
