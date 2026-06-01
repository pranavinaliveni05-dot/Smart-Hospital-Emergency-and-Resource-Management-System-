import java.util.Scanner;

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

    AVLNode(Patient p) {
        patient = p;
        height = 1;
    }
}

public class CO1 {

    AVLNode root;

    int height(AVLNode n) {
        return (n == null) ? 0 : n.height;
    }

    int getBalance(AVLNode n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

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

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // LL Rotation
        if (balance > 1 && p.id < node.left.patient.id) {
            System.out.println("LL Rotation Performed");
            return rightRotate(node);
        }

        // RR Rotation
        if (balance < -1 && p.id > node.right.patient.id) {
            System.out.println("RR Rotation Performed");
            return leftRotate(node);
        }

        // LR Rotation
        if (balance > 1 && p.id > node.left.patient.id) {
            System.out.println("LR Rotation Performed");
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL Rotation
        if (balance < -1 && p.id < node.right.patient.id) {
            System.out.println("RL Rotation Performed");
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
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
                    node.patient.id + " "
                    + node.patient.name + " "
                    + node.patient.age);

            inorder(node.right);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CO1 avl = new CO1();

        int choice;

        do {

            System.out.println("\n===== AVL PATIENT MANAGEMENT =====");
            System.out.println("1. Construct AVL Tree");
            System.out.println("2. Insert Patient");
            System.out.println("3. Search Patient");
            System.out.println("4. Display Patients");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.print("Enter Number of Patients: ");
                    int n = sc.nextInt();

                    for (int i = 0; i < n; i++) {

                        System.out.print("Enter Patient ID: ");
                        int id = sc.nextInt();

                        sc.nextLine();

                        System.out.print("Enter Patient Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();

                        avl.root = avl.insert(
                                avl.root,
                                new Patient(id, name, age));
                    }

                    System.out.println("AVL Tree Constructed Successfully");
                    break;

                case 2:

                    System.out.print("Enter Patient ID: ");
                    int id = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter Patient Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    avl.root = avl.insert(
                            avl.root,
                            new Patient(id, name, age));

                    break;

                case 3:

                    System.out.print("Enter Patient ID to Search: ");
                    int sid = sc.nextInt();

                    Patient p = avl.search(avl.root, sid);

                    if (p != null) {

                        System.out.println("Patient Found");
                        System.out.println("ID   : " + p.id);
                        System.out.println("Name : " + p.name);
                        System.out.println("Age  : " + p.age);

                    } else {

                        System.out.println("Patient Not Found");
                    }

                    break;

                case 4:

                    System.out.println("\nPatient Records (Sorted Order)");
                    avl.inorder(avl.root);

                    break;

                case 5:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid Choice");
            }

        } while (choice != 5);

        sc.close();
    }
}