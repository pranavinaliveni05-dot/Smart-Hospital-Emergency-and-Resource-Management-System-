 import java.util.*;

class Patient {
    int id;
    String name;
    int age;

    Patient(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}

class AVLNode {
    Patient patient;
    AVLNode left, right;
    int height;

    AVLNode(Patient patient) {
        this.patient = patient;
        this.height = 1;
    }
}

class AVLTree {
    AVLNode root;

    int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    int balanceFactor(AVLNode node) {
        return node == null ? 0 :
                height(node.left) - height(node.right);
    }

    AVLNode rightRotate(AVLNode y) {
        System.out.println("RR Rotation Performed");
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left),
                height(y.right)) + 1;

        x.height = Math.max(height(x.left),
                height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        System.out.println("LR Rotation Performed");
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left),
                height(x.right)) + 1;

        y.height = Math.max(height(y.left),
                height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, Patient p) {
        if (node == null) {
            System.out.println("Patient Inserted : " + p.id);
            return new AVLNode(p);
        }

        if (p.id < node.patient.id)
            node.left = insert(node.left, p);
        else if (p.id > node.patient.id)
            node.right = insert(node.right, p);
        else
            return node;

        node.height = 1 + Math.max(
                height(node.left),
                height(node.right));

        int balance = balanceFactor(node);

        // LL Case
        if (balance > 1 && p.id < node.left.patient.id) {
            System.out.println("LL Rotation Performed");
            return rightRotate(node);
        }

        // RR Case
        if (balance < -1 && p.id > node.right.patient.id) {
            System.out.println("RR Rotation Performed");
            return leftRotate(node);
        }

        // LR Case
        if (balance > 1 && p.id > node.left.patient.id) {
            System.out.println("LR Rotation Performed");
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL Case
        if (balance < -1 && p.id < node.right.patient.id) {
            System.out.println("RL Rotation Performed");
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void addPatient(Patient p) {
        root = insert(root, p);
        displayTree();
    }

    void displayTree() {
        System.out.println("\nCurrent AVL Tree:");
        printTree(root, 0);
        System.out.println();
    }

    void printTree(AVLNode node, int level) {
        if (node == null)
            return;
        
        printTree(node.right, level + 1);
        
        for (int i = 0; i < level; i++)
            System.out.print("    ");
        
        System.out.println(node.patient.id);
        
        printTree(node.left, level + 1);
    }

    Patient search(AVLNode node, int id) {
        if (node == null)
            return null;

        if (node.patient.id == id)
            return node.patient;

        if (id < node.patient.id)
            return search(node.left, id);

        return search(node.right, id);
    }

    void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.println(
                    "ID : " + node.patient.id +
                    " Name : " + node.patient.name +
                    " Age : " + node.patient.age);
            inorder(node.right);
        }
    }

    // ========== PRIORITY CARE LIST - TOP K OLDEST PATIENTS ==========
    
    void getTopKOldestPatients(int k) {
        System.out.println("\n👴 ========== PRIORITY CARE LIST ========== 👵");
        System.out.println("Top " + k + " Oldest Patients (Need Immediate Care)\n");
        
        List<Patient> oldestPatients = new ArrayList<>();
        collectAllPatients(root, oldestPatients);
        
        if (oldestPatients.isEmpty()) {
            System.out.println("❌ No patients in the system!\n");
            return;
        }
        
        // Sort by age descending (oldest first)
        oldestPatients.sort((p1, p2) -> Integer.compare(p2.age, p1.age));
        
        if (k > oldestPatients.size()) {
            System.out.println("⚠️ Only " + oldestPatients.size() + " patients available. Showing all.\n");
            k = oldestPatients.size();
        }
        
        System.out.println("┌─────┬──────────┬────────────────┬──────┬─────────────┐");
        System.out.println("│ Rank│ Patient ID│ Name           │ Age  │ Priority    │");
        System.out.println("├─────┼──────────┼────────────────┼──────┼─────────────┤");
        
        for (int i = 0; i < k; i++) {
            Patient p = oldestPatients.get(i);
            String priority = "";
            if (i == 0) priority = "🔴 CRITICAL";
            else if (i == 1) priority = "🟠 HIGH";
            else if (i == 2) priority = "🟡 MEDIUM";
            else priority = "🟢 NORMAL";
            
            System.out.printf("│  %2d │   %6d │ %-14s │  %3d │ %-11s│\n", 
                            (i+1), p.id, p.name, p.age, priority);
        }
        
        System.out.println("└─────┴──────────┴────────────────┴──────┴─────────────┘");
        
        // Display ages array format
        System.out.print("\nTop " + k + " Patient Ages: [");
        for (int i = 0; i < k; i++) {
            System.out.print(oldestPatients.get(i).age);
            if (i < k-1) System.out.print(", ");
        }
        System.out.println("]\n");
        
        // Medical recommendations
        System.out.println("🏥 MEDICAL RECOMMENDATIONS:");
        for (int i = 0; i < k && i < 3; i++) {
            Patient p = oldestPatients.get(i);
            if (p.age >= 60) {
                System.out.println("   • " + p.name + " (Age " + p.age + ") → Regular health checkup required");
            } else if (p.age >= 50) {
                System.out.println("   • " + p.name + " (Age " + p.age + ") → Annual screening recommended");
            } else if (p.age >= 40) {
                System.out.println("   • " + p.name + " (Age " + p.age + ") → Routine checkup advised");
            }
        }
        System.out.println();
    }
    
    void collectAllPatients(AVLNode node, List<Patient> list) {
        if (node != null) {
            collectAllPatients(node.left, list);
            list.add(node.patient);
            collectAllPatients(node.right, list);
        }
    }
    
    // Get total patient count
    int getPatientCount() {
        return getCount(root);
    }
    
    int getCount(AVLNode node) {
        if (node == null) return 0;
        return 1 + getCount(node.left) + getCount(node.right);
    }
}

// Class to store day-wise patient details
class DayWisePatient {
    int day;
    int patientCount;
    List<String> patientNames;
    List<Integer> patientIds;
    List<Integer> patientAges;
    
    DayWisePatient(int day) {
        this.day = day;
        this.patientCount = 0;
        this.patientNames = new ArrayList<>();
        this.patientIds = new ArrayList<>();
        this.patientAges = new ArrayList<>();
    }
    
    void addPatient(String name, int id, int age) {
        patientNames.add(name);
        patientIds.add(id);
        patientAges.add(age);
        patientCount++;
    }
    
    void displayDetails() {
        System.out.println("\n   📅 DAY " + day + " REPORT:");
        System.out.println("   ┌─────────────────────────────────────────┐");
        System.out.println("   │ Total Patients : " + patientCount);
        System.out.println("   ├─────────────────────────────────────────┤");
        for (int i = 0; i < patientNames.size(); i++) {
            System.out.printf("   │ %d. ID:%-5d | %-12s | Age:%-3d │\n", 
                            (i+1), patientIds.get(i), patientNames.get(i), patientAges.get(i));
        }
        System.out.println("   └─────────────────────────────────────────┘");
    }
}

public class SmartHospitalProject {

    static AVLTree avl = new AVLTree();
    static Scanner sc = new Scanner(System.in);
    // B+ Tree Simulation
    static HashMap<Integer, String> medicalRecords = new HashMap<>();

    // Segment Tree (Beds)
    static int[] beds = {10, 15, 8, 20, 12};
    
    // Day-wise patient statistics
    static int[] patientStats = new int[11];
    static DayWisePatient[] dayWisePatients = new DayWisePatient[11];
    
    // Initialize day-wise patients array
    static {
        for (int i = 1; i <= 10; i++) {
            dayWisePatients[i] = new DayWisePatient(i);
        }
    }

    static void patientModule() {
        while (true) {
            System.out.println("\n--- AVL PATIENT MANAGEMENT ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Display Patients");
            System.out.println("4. 🚨 PRIORITY CARE LIST (Top K Oldest)");
            System.out.println("5. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter ID : ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name : ");
                    String name = sc.nextLine();
                    System.out.print("Enter Age : ");
                    int age = sc.nextInt();
                    avl.addPatient(new Patient(id, name, age));
                    System.out.println("Patient Added Successfully");
                    break;

                case 2:
                    System.out.print("Enter Patient ID : ");
                    int sid = sc.nextInt();
                    Patient p = avl.search(avl.root, sid);
                    if (p != null)
                        System.out.println("Found : " + p.name);
                    else
                        System.out.println("Patient Not Found");
                    break;

                case 3:
                    if (avl.root == null) {
                        System.out.println("No patients in the system");
                    } else {
                        avl.inorder(avl.root);
                    }
                    break;

                case 4:
                    if (avl.root == null) {
                        System.out.println("\n❌ No patients in the system!");
                        System.out.println("💡 Please add patients first using option 1\n");
                        break;
                    }
                    System.out.print("\nEnter number of patients for Priority Care List: ");
                    int k = sc.nextInt();
                    if (k <= 0) {
                        System.out.println("❌ Please enter a positive number");
                    } else {
                        avl.getTopKOldestPatients(k);
                    }
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    static void medicalRecordModule() {
        while (true) {
            System.out.println("\n--- MEDICAL RECORDS MODULE ---");
            System.out.println("1. Add Record");
            System.out.println("2. Search Record");
            System.out.println("3. Display All Records");
            System.out.println("4. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Patient ID : ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Medical History : ");
                    String history = sc.nextLine();
                    medicalRecords.put(id, history);
                    System.out.println("Record Added");
                    break;

                case 2:
                    System.out.print("Enter Patient ID : ");
                    int sid = sc.nextInt();
                    if (medicalRecords.containsKey(sid)) {
                        System.out.println("History : " + medicalRecords.get(sid));
                    } else {
                        System.out.println("Record Not Found");
                    }
                    break;

                case 3:
                    for (Integer key : medicalRecords.keySet()) {
                        System.out.println("ID : " + key + " History : " + medicalRecords.get(key));
                    }
                    break;

                case 4:
                    return;
            }
        }
    }
    
    static void bedModule() {
        while (true) {
            System.out.println("\n--- BED MANAGEMENT ---");
            System.out.println("1. View Beds");
            System.out.println("2. Update Ward Beds");
            System.out.println("3. Total Beds");
            System.out.println("4. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    for (int i = 0; i < beds.length; i++) {
                        System.out.println("Ward " + (i + 1) + " Beds : " + beds[i]);
                    }
                    break;

                case 2:
                    System.out.print("Enter Ward Number : ");
                    int ward = sc.nextInt();
                    System.out.print("Enter Beds : ");
                    int count = sc.nextInt();
                    beds[ward - 1] = count;
                    System.out.println("Updated");
                    break;

                case 3:
                    int total = 0;
                    for (int b : beds)
                        total += b;
                    System.out.println("Total Beds : " + total);
                    break;

                case 4:
                    return;
            }
        }
    }
    
    static void statisticsModule() {
        while (true) {
            System.out.println("\n--- 📊 STATISTICS MODULE ---");
            System.out.println("1. Add Daily Patient Count");
            System.out.println("2. Add Patient Details for a Day");
            System.out.println("3. View Day-wise Summary (Counts)");
            System.out.println("4. View Day-wise Patient Details");
            System.out.println("5. View Total Patients (All Days)");
            System.out.println("6. View Weekly Report");
            System.out.println("7. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Day (1-10): ");
                    int day = sc.nextInt();
                    System.out.print("Patient Count: ");
                    int count = sc.nextInt();
                    if (day >= 1 && day <= 10) {
                        patientStats[day] += count;
                        System.out.println("✅ Count added for Day " + day);
                    } else {
                        System.out.println("❌ Invalid day! Please enter 1-10");
                    }
                    break;

                case 2:
                    System.out.print("Enter Day (1-10): ");
                    int dayNum = sc.nextInt();
                    if (dayNum < 1 || dayNum > 10) {
                        System.out.println("❌ Invalid day! Please enter 1-10");
                        break;
                    }
                    
                    System.out.print("Enter Patient ID: ");
                    int pId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Patient Name: ");
                    String pName = sc.nextLine();
                    System.out.print("Enter Patient Age: ");
                    int pAge = sc.nextInt();
                    
                    dayWisePatients[dayNum].addPatient(pName, pId, pAge);
                    patientStats[dayNum]++;
                    System.out.println("✅ Patient details added for Day " + dayNum);
                    break;

                case 3:
                    System.out.println("\n📊 ========== DAY-WISE SUMMARY ==========");
                    System.out.println("┌─────┬────────────────┬─────────────┐");
                    System.out.println("│ Day │ Patient Count  │ Status      │");
                    System.out.println("├─────┼────────────────┼─────────────┤");
                    for (int i = 1; i <= 10; i++) {
                        String status = patientStats[i] > 0 ? "✅ Active" : "❌ No Data";
                        System.out.printf("│  %2d │      %5d     │ %-11s│\n", i, patientStats[i], status);
                    }
                    System.out.println("└─────┴────────────────┴─────────────┘\n");
                    break;

                case 4:
                    System.out.print("\nEnter Day (1-10) to view details: ");
                    int viewDay = sc.nextInt();
                    if (viewDay >= 1 && viewDay <= 10) {
                        if (dayWisePatients[viewDay].patientCount > 0) {
                            dayWisePatients[viewDay].displayDetails();
                        } else {
                            System.out.println("\n❌ No patient data for Day " + viewDay);
                        }
                    } else {
                        System.out.println("❌ Invalid day!");
                    }
                    break;

                case 5:
                    int total = 0;
                    for (int i = 1; i <= 10; i++) {
                        total += patientStats[i];
                    }
                    System.out.println("\n📈 Total Patients (All Days): " + total);
                    
                    // Day with most patients
                    int maxDay = 1, maxCount = patientStats[1];
                    for (int i = 2; i <= 10; i++) {
                        if (patientStats[i] > maxCount) {
                            maxCount = patientStats[i];
                            maxDay = i;
                        }
                    }
                    if (maxCount > 0) {
                        System.out.println("📅 Day with most patients: Day " + maxDay + " (" + maxCount + " patients)");
                    }
                    System.out.println();
                    break;

                case 6:
                    System.out.println("\n📅 ========== WEEKLY REPORT ==========");
                    System.out.println("┌─────────┬────────────────┬────────────────────┐");
                    System.out.println("│ Week    │ Total Patients │ Average per Day    │");
                    System.out.println("├─────────┼────────────────┼────────────────────┤");
                    
                    // Week 1 (Days 1-7)
                    int week1Total = 0;
                    for (int i = 1; i <= 7; i++) {
                        week1Total += patientStats[i];
                    }
                    double week1Avg = week1Total / 7.0;
                    System.out.printf("│ Week 1  │      %5d     │       %.2f        │\n", week1Total, week1Avg);
                    
                    // Week 2 (Days 8-10)
                    int week2Total = 0;
                    for (int i = 8; i <= 10; i++) {
                        week2Total += patientStats[i];
                    }
                    double week2Avg = week2Total / 3.0;
                    System.out.printf("│ Week 2  │      %5d     │       %.2f        │\n", week2Total, week2Avg);
                    
                    System.out.println("└─────────┴────────────────┴────────────────────┘\n");
                    
                    // Trend analysis
                    System.out.println("📈 TREND ANALYSIS:");
                    if (week1Total > week2Total && week2Total > 0) {
                        System.out.println("   📉 Patient count decreased in Week 2");
                    } else if (week2Total > week1Total) {
                        System.out.println("   📈 Patient count increased in Week 2");
                    } else if (week1Total == week2Total && week1Total > 0) {
                        System.out.println("   ➡️ Patient count remained stable");
                    } else {
                        System.out.println("   ❌ Insufficient data for trend analysis");
                    }
                    System.out.println();
                    break;

                case 7:
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    static void bfsModule() {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            graph.add(new ArrayList<>());

        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(3);
        graph.get(2).add(4);

        boolean[] visited = new boolean[5];
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        visited[0] = true;

        System.out.print("BFS : ");
        while (!q.isEmpty()) {
            int node = q.poll();
            System.out.print(node + " ");
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    q.add(next);
                }
            }
        }
        System.out.println();
    }
    
    static void dfsUtil(List<List<Integer>> graph, int node, boolean[] visited) {
        visited[node] = true;
        System.out.print(node + " ");
        for (int next : graph.get(node)) {
            if (!visited[next])
                dfsUtil(graph, next, visited);
        }
    }

    static void dfsModule() {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            graph.add(new ArrayList<>());

        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(3);
        graph.get(2).add(4);

        boolean[] visited = new boolean[5];
        System.out.print("DFS : ");
        dfsUtil(graph, 0, visited);
        System.out.println();
    }
    
    static void primModule() {
        int[][] graph = {
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {6, 8, 0, 0, 9},
            {0, 5, 7, 9, 0}
        };

        int V = 5;
        int[] key = new int[V];
        boolean[] mst = new boolean[V];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        int cost = 0;

        for (int count = 0; count < V; count++) {
            int u = -1;
            for (int i = 0; i < V; i++) {
                if (!mst[i] && (u == -1 || key[i] < key[u])) {
                    u = i;
                }
            }
            mst[u] = true;
            cost += key[u];
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mst[v] && graph[u][v] < key[v]) {
                    key[v] = graph[u][v];
                }
            }
        }
        System.out.println("Minimum Hospital Network Cost = " + cost);
    }
    
    static class Edge {
        int src, dest, weight;
        Edge(int s, int d, int w) {
            src = s;
            dest = d;
            weight = w;
        }
    }

    static int find(int[] parent, int i) {
        if (parent[i] == i)
            return i;
        return find(parent, parent[i]);
    }

    static void union(int[] parent, int x, int y) {
        int xroot = find(parent, x);
        int yroot = find(parent, y);
        parent[xroot] = yroot;
    }

    static void kruskalModule() {
        Edge[] edges = {
            new Edge(0, 1, 2),
            new Edge(1, 2, 3),
            new Edge(0, 3, 6),
            new Edge(1, 4, 5),
            new Edge(2, 4, 7)
        };

        Arrays.sort(edges, Comparator.comparingInt(e -> e.weight));
        int[] parent = new int[5];
        for (int i = 0; i < 5; i++) {
            parent[i] = i;
        }
        int cost = 0;
        for (Edge e : edges) {
            int x = find(parent, e.src);
            int y = find(parent, e.dest);
            if (x != y) {
                cost += e.weight;
                union(parent, x, y);
            }
        }
        System.out.println("Minimum Multi-Hospital Network Cost = " + cost);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== SMART HOSPITAL SYSTEM =====");
            System.out.println("1. AVL Tree - Patient Records");
            System.out.println("2. B+ Tree - Medical Records");
            System.out.println("3. Segment Tree - Beds");
            System.out.println("4. 📊 Statistics (Day-wise Patient Details)");
            System.out.println("5. BFS");
            System.out.println("6. DFS");
            System.out.println("7. Prim's Algorithm");
            System.out.println("8. Kruskal's Algorithm");
            System.out.println("9. Exit");
            System.out.print("Enter Choice : ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    patientModule();
                    break;
                case 2:
                    medicalRecordModule();
                    break;
                case 3:
                    bedModule();
                    break;
                case 4:
                    statisticsModule();
                    break;
                case 5:
                    bfsModule();
                    break;
                case 6:
                    dfsModule();
                    break;
                case 7:
                    primModule();
                    break;
                case 8:
                    kruskalModule();
                    break;
                case 9:
                    System.exit(0);
                default:
                    System.out.println("Will be added in next part");
            }
        }
    }
}
 
 
 