package DuGraphics.services.data.parsing;

import DuGraphics.services.data.LinkedList.LinkedList;
import DuGraphics.services.data.graph.Graph;
import DuGraphics.services.data.graph.GraphNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class GraphParser {
    private File file;
    private Graph<String> graph;

    private LinkedList<GraphNode<String>> positionalNodes;

    public GraphParser(File file) {
        reloadFile(file);
    }

    public GraphParser() {
        file = null;
    }

    public void reloadFile(File file) {
        this.file = file;

        try {
            loadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFile() throws IOException {
        graph = new Graph<>();

        BufferedReader reader;
        reader = new BufferedReader(new FileReader(file));

        String line;
        String edgesBuffer = "";
        boolean isInGroup = false;
        String groupKey = null;
        while ((line = reader.readLine()) != null) {
            if (isInGroup) {
                if (line.toLowerCase().equals(groupKey)) {
                    isInGroup = false;
                    continue;
                }

                edgesBuffer += line + "\n";
            }

            line = line.toLowerCase(Locale.ROOT);

            if (line.startsWith("vertex ")) {
                parseListNode(line);
                continue;
            }

            if (line.startsWith("start_edges")) {
                isInGroup = true;
                groupKey = "end_edges";
            }

        }

        parseEdges(edgesBuffer);

        positionalNodes = new LinkedList<>();

        int x = 400;
        int y = 400;
        for (String key : getGraph().getKeys()) {
            System.out.println("x " + x + ", y " + y);
            positionalNodes.insert(new GraphNode<>(key, x, y));

            x = (int) (Math.random() * (1000 - 200 + 1) + 400);
            y = x;
        }

    }

    public LinkedList<GraphNode<String>> getPositionalNodes() {
        if (file == null) return null;

        return positionalNodes;
    }

    private void parseListNode(String line) {
        String rawData = line.substring("vertex ".length()).strip();
        String[] data = rawData.split(" ");

        for (String vertex : data) {
            graph.addVertex(vertex);
        }
    }

    private void parseEdges(String edgesBuffer) {
        String[] pairEdges = edgesBuffer.split("\n");
        for (String pairEdge : pairEdges) {
            String[] edges = pairEdge.split(" ");
            graph.addEdge(edges[0], edges[1]);
        }
    }

    public boolean isGraphLoaded() {
        return file != null && graph != null;
    }

    public Graph<String> getGraph() {
        if (file == null) return null;

        return graph;
    }
}
