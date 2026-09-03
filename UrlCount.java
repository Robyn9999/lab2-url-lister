import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UrlCount {

  public static class UrlMapper
      extends Mapper<Object, Text, Text, IntWritable> {

    private static final IntWritable one = new IntWritable(1);
    private final Text href = new Text();

    private static final Pattern HREF_PATTERN =
        Pattern.compile("href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);

    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {

      String line = value.toString();

      Matcher matcher = HREF_PATTERN.matcher(line);

      while (matcher.find()) {
        href.set(matcher.group(1));
        context.write(href, one);
      }
    }
  }

    public static class IntSumCombiner
       extends Reducer<Text,IntWritable,Text,IntWritable> { 
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

    
  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> { 
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
        if ( sum > 5) {
      context.write(key, result);
        }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(UrlCount.class);
    job.setMapperClass(UrlMapper.class);
      
    job.setCombinerClass(IntSumCombiner.class);
      
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
