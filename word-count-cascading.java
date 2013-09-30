package org.myorg;

import java.util.Properties;

import cascading.flow.*;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.aggregator.Count;
import cascading.operation.regex.*;
import cascading.pipe.*;
import cascading.property.AppProps;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class Main {  
    public static void main( String[] args ) {
        String docPath = args[0];
        String wcPath = args[1];

        Properties properties = new Properties();
        AppProps.setApplicationJarClass(properties, Main.class);
        HadoopFlowConnector flowConnector = new HadoopFlowConnector(properties);

        Tap docTap = new Hfs(new TextDelimited(true, "\t"), docPath);
        Tap wcTap = new Hfs(new TextDelimited(true, "\t"), wcPath);

        Fields token = new Fields("token");
        Fields text = new Fields("text");
        RegexSplitGenerator splitter = new RegexSplitGenerator(token, "[ \\[\\]\\(\\),.]");
        Pipe docPipe = new Each("token", text, splitter, Fields.RESULTS);

        Pipe wcPipe = new Pipe("wc", docPipe);
        wcPipe = new GroupBy(wcPipe, token);
        wcPipe = new Every(wcPipe, Fields.ALL, new Count(), Fields.ALL);

        FlowDef flowDef = FlowDef.flowDef()
                                 .setName("wc")
                                 .addSource(docPipe, docTap)
                                 .addTailSink(wcPipe, wcTap);

        Flow wcFlow = flowConnector.connect(flowDef);
        wcFlow.complete();
    }
}