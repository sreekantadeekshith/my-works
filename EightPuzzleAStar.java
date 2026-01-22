import java.util.*; 
 
 
public class EightPuzzleAStar { 
 
    static int[][] goal = { 
        {1,2,3}, 
        {4,5,6}, 
        {7,8,0} 
    }; 
 
    static boolean isGoal(int[][] state) { 
        return Arrays.deepEquals(state, goal); 
    } 
 
    static int[][] copy(int[][] s) { 
        int[][] c = new int[3][3]; 
        for (int i = 0; i < 3; i++) 
            c[i] = s[i].clone(); 
        return c; 
    } 
 
    static List<int[][]> successors(int[][] state) { 
        List<int[][]> list = new ArrayList<>(); 
        int x = 0, y = 0; 
 
        for (int i = 0; i < 3; i++) 
            for (int j = 0; j < 3; j++) 
                if (state[i][j] == 0) { 
                    x = i; y = j; 
                } 
 
        int[][] moves = {{-1,0},{1,0},{0,-1},{0,1}}; 
 
        for (int[] m : moves) { 
            int nx = x + m[0], ny = y + m[1]; 
            if (nx >= 0 && ny >= 0 && nx < 3 && ny < 3) { 
                int[][] ns = copy(state); 
                ns[x][y] = ns[nx][ny]; 
                ns[nx][ny] = 0; 
                list.add(ns); 
            } 
        } 
        return list; 
    } 
 
    static void printState(int[][] state) { 
        for (int[] row : state) { 
            for (int v : row) 
                System.out.print(v + " "); 
            System.out.println(); 
        } 
    } 
 
    static void printPath(Node node) { 
        Stack<Node> stack = new Stack<>(); 
        while (node != null) { 
            stack.push(node); 
            node = node.parent; 
        } 
 
        int step = 0; 
        while (!stack.isEmpty()) { 
            Node n = stack.pop(); 
            System.out.println("Step " + step++); 
            printState(n.state); 
            System.out.println("g=" + n.g + " h=" + n.h + " f=" + n.f); 
            System.out.println("--------------------"); 
        } 
    } 
 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        int[][] start = new int[3][3]; 
 
        System.out.println("Enter initial state (use 0 for blank):"); 
        for (int i = 0; i < 3; i++) 
            for (int j = 0; j < 3; j++) 
                start[i][j] = sc.nextInt(); 
 
        PriorityQueue<Node> open = 
            new PriorityQueue<>(Comparator.comparingInt(n -> n.f)); 
        Set<String> closed = new HashSet<>(); 
 
        open.add(new Node(start, 0, null)); 
 
        while (!open.isEmpty()) { 
            Node current = open.poll(); 
 
            if (isGoal(current.state)) { 
                System.out.println("\nGOAL can be REACHED!\n"); 
                printPath(current); 
                return; 
            } 
 
            closed.add(Arrays.deepToString(current.state)); 
 
            for (int[][] next : successors(current.state)) { 
                if (!closed.contains(Arrays.deepToString(next))) { 
                    open.add(new Node(next, current.g + 1, current)); 
                } 
            } 
        } 
 
        System.out.println("No solution found."); 
    } 
} 
class Node { 
    int[][] state; 
    int g, h, f; 
    Node parent; 
 
    Node(int[][] state, int g, Node parent) { 
        this.state = state; 
        this.g = g; 
        this.parent = parent; 
        this.h = heuristic(state); 
        this.f = g + h; 
    } 
 
    static int heuristic(int[][] state) { 
        int h = 0; 
        int[][] goal = { 
            {1,2,3}, 
            {4,5,6}, 
            {7,8,0} 
        }; 
 
        for (int i = 0; i < 3; i++) { 
            for (int j = 0; j < 3; j++) { 
                int val = state[i][j]; 
                if (val != 0) { 
                    for (int x = 0; x < 3; x++) { 
                        for (int y = 0; y < 3; y++) { 
                            if (goal[x][y] == val) { 
                                h += Math.abs(i - x) + Math.abs(j - y); 
                            } 
                        } 
                    } 
                } 
            } 
        } 
        return h; 
    } 
}