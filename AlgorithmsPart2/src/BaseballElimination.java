import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/*

 */
public class BaseballElimination {
    private int numTeams;
    private int numMatchUps = 0;
    private String[] teamsByIndex;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private Map<String, Integer> teams;
    private Map<String, Bag<String>> certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new NullPointerException();

        In in = new In(filename);
        numTeams = Integer.parseInt(in.readLine());
        teamsByIndex = new String[numTeams];
        wins = new int[numTeams];
        losses = new int[numTeams];
        remaining = new int[numTeams];
        against = new int[numTeams][numTeams];
        teams = new HashMap<String, Integer>(numTeams);
        certificates = new HashMap<String, Bag<String>>(numTeams);

        for (int j = 0; j < numTeams; j++) {
            if (in.hasNextLine()) parseLine(in.readLine(), j);
        }

        numMatchUps = numMatchUps/2;
    }

    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        verifyTeam(team);
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        verifyTeam(team);
        return losses[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        verifyTeam(team);
        return remaining[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        verifyTeam(team1);
        verifyTeam(team2);
        return against[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        verifyTeam(team);
        return getCertificate(team).size() > 0;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        verifyTeam(team);
        Bag<String> cert = getCertificate(team);

        if (cert.size() > 0) return cert;
        else return null;
    }

    private Bag<String> getCertificate(String team) {
        if (!certificates.containsKey(team)) {
            createCertificate(team);
        }
        return certificates.get(team);
    }

    private void createCertificate(String team) {
        Bag<String> simple = simpleElimination(team);
        if (simple.size() > 0) {
            certificates.put(team, simple);
        } else {
            certificates.put(team, nontrivialElimination(team));
        }
    }

    private Bag<String> simpleElimination(String team) {
        int teamIndex = teams.get(team);
        int bestCase = wins[teamIndex] + remaining[teamIndex];
        Bag<String> teamCertificate = new Bag<String>();

        for (String key : teams.keySet()) {
            if (teams.get(key) != teamIndex && bestCase < wins[teams.get(key)]) {
                teamCertificate.add(key);
            }
        }
        return teamCertificate;
    }

    private Bag<String> nontrivialElimination(String team) {
        Bag<String> teamCertificate = new Bag<String>();
        int teamIndex = teams.get(team);

        int numMatchUpsX = 0;
        for (int i = 0; i < numTeams; i++) {
            if (against[teamIndex][i] > 0) numMatchUpsX++;
        }

        int bestCase = wins[teamIndex] + remaining[teamIndex];
        int numVertices = 2 + numMatchUps - numMatchUpsX + numTeams;
        int sNode = 0;
        int tNode = numVertices - 1;
        FlowNetwork network = new FlowNetwork(numVertices);

        for (int i = 0; i < numTeams; i++) {
            if (i != teamIndex) {
                network.addEdge(new FlowEdge(tNode - 1 - i, tNode, Math.max(bestCase - wins[i], 0)));
            }
        }

        int nodeIndex = 0;
        for (int i = 0; i < numTeams; i++) {
            for (int j = i + 1; j < numTeams; j++) {
                if (i != teamIndex && j != teamIndex && against[i][j] > 0) {
                    nodeIndex++;
                    network.addEdge(new FlowEdge(sNode, nodeIndex, against[i][j]));
                    network.addEdge(new FlowEdge(nodeIndex, tNode - 1 - i, Double.POSITIVE_INFINITY));
                    network.addEdge(new FlowEdge(nodeIndex, tNode - 1 - j, Double.POSITIVE_INFINITY));
                }
            }
        }
        FordFulkerson ff = new FordFulkerson(network, 0, tNode);

        for (int i = 0; i < numTeams; i++) {
            if (ff.inCut(tNode - 1 - i)) teamCertificate.add(teamsByIndex[i]);
        }

        return teamCertificate;
    }

    private void parseLine(String line, int index) {
        String[] items = line.split("\\s+");
        int i = 0;
        if (items[0].length() == 0) i++;
        teamsByIndex[index] = items[i];
        teams.put(items[i++], index);
        wins[index] = Integer.parseInt(items[i++]);
        losses[index] = Integer.parseInt(items[i++]);
        remaining[index] = Integer.parseInt(items[i++]);

        for (int j = i; j < items.length; j++) {
            against[index][j - i] = Integer.parseInt(items[j]);
            if (against[index][j - i] > 0) numMatchUps++;
        }
    }

    private void verifyTeam(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Team not found: " + team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
