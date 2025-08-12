import java.io.*;
import java.sql.*;
import java.util.*;

class App {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/marksheet", "root", "");
        DBUtils.createTables(con);
        System.out.println("WELCOME HERE TO MARKSHEET GENERATOR......");
        System.out.println("Are you a STUDENT or a TEACHER?");
        System.out.println("Enter 1 for STUDENT & Enter 2 for TEACHER");

        while (true) {
            int t = sc.nextInt();
            if (t == 1) {
                new StudentService(con);
                break;
            } else if (t == 2) {
                new TeacherService(con);
                break;
            } else {
                System.out.println("Invalid choice! Please enter 1 or 2.");
            }
        }
    }
}

class SPI_CGPA {

    Connection con;
    Scanner sc = new Scanner(System.in);
    String[] namesofsub;
    double[] sub_credits;
    double[] total_marks;
    double cigi;
    double total_credits;
    public double spi;
    public double cgpa;

    boolean isDuplicateEnrollment(String enrollment) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM student WHERE enrollment = ?");
            ps.setString(1, enrollment);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public SPI_CGPA(Connection con) {
        this.con = con;
    }

    public void spi() {
        cigi = 0;
        total_credits = 0;
        System.out.println("ENTER THE NUMBER OF SUBJECTS");
        int size = sc.nextInt();
        namesofsub = new String[size];
        sub_credits = new double[size];
        total_marks = new double[size];

        for (int i = 0; i < size; i++) {
            System.out.println("\nENTER DETAILS FOR SUBJECT " + (i + 1) + ":");

            System.out.println("Enter subject name: ");
            namesofsub[i] = sc.next();

            System.out.println("Enter Total Credits of " + namesofsub[i] + ":");
            sub_credits[i] = sc.nextDouble();

            System.out.println("Enter Total marks of " + namesofsub[i]);
            total_marks[i] = sc.nextDouble();
            double grade_point = grdPoint(total_marks[i]);

            cigi += (grade_point * sub_credits[i]);
            total_credits += sub_credits[i];
        }
        spi = cigi / total_credits;
    }

    public void cgpa(int sem) {
        double total_GradePoint = 0;
        double total_creditss = 0;

        for (int i = 1; i < sem; i++) {
            spi();
            total_GradePoint += cigi;
            total_creditss += total_credits;
        }
        cgpa = total_GradePoint / total_creditss;
    }

    double grdPoint(double marks) {
        if (marks < 0 || marks > 100) {
            System.out.println("Enter valid marks between 0 to 100!");
            return 0;
        }

        double[] marks_range = {34, 39, 44, 49, 54, 59, 64, 69, 74, 79, 84, 89, 94, 100};
        double[] points = {0, 4, 4.5, 5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10};

        for (int i = 0; i < marks_range.length; i++) {
            if (marks <= marks_range[i]) {
                return points[i];
            }
        }
        return 0;
    }

}

class DBUtils {

    public static void createTables(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        String createStudentTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                + "enrollment VARCHAR(20) PRIMARY KEY,"
                + "sem INT,"
                + "branch VARCHAR(50),"
                + "name VARCHAR(50),"
                + "spi DOUBLE,"
                + "cgpa DOUBLE"
                + ")";
        stmt.execute(createStudentTableSQL);

        String createRankingTableSQL = "CREATE TABLE IF NOT EXISTS student_ranking ("
                + "rank INT AUTO_INCREMENT PRIMARY KEY,"
                + "enrollment VARCHAR(20),"
                + "name VARCHAR(50),"
                + "cgpa DOUBLE"
                + ")";
        stmt.execute(createRankingTableSQL);
    }
}

class DataStorage {

    public int sem;
    public String enrollment;
    public String branch;
    public String name;
    public double spi;
    public double cgpa;

    public DataStorage(String enrollment, int sem, String branch, String name, double spi, double cgpa) {
        this.enrollment = enrollment;
        this.sem = sem;
        this.branch = branch;
        this.name = name;
        this.spi = spi;
        this.cgpa = cgpa;
    }
}

class StudentDetails {

    public HashMap<String, DataStorage> students = new HashMap<>();

    Connection con;
    Node head;

    class Node {

        double data;
        Node next;
        Node prev;

        Node(double x) {
            data = x;
        }
    }

    void insertSorted(double x) {
        Node n = new Node(x);
        if (head == null) {
            head = n;

        } else if (head.data > x)// insert first code for both single valued and multi Valued code
        {

            n.next = head;
            head.prev = n;
            head = n;
        } else {
            Node temp = head;
            while (temp.next != null && temp.next.data < x) {
                temp = temp.next;
            }
            if (temp.next == null)//that means temp reach to last node, That means no node has greater value then x
            {
                temp.next = n;
                n.prev = temp;
            } else {
                n.prev = temp;
                n.next = temp.next;
                temp.next = n;
                if (n.next != null) {
                    n.next.prev = n;
                }
            }
        }
    }

    void insertSorted2(double x) {
        Node n = new Node(x);
        if (head == null) {
            head = n;

        } else if (head.data > x)// insert first code for both single valued and multi Valued code
        {

            n.next = head;
            head.prev = n;
            head = n;
        } else {
            Node temp = head;
            while (temp.next != null && temp.next.data < x) {
                temp = temp.next;
            }
            if (temp.next == null)//that means temp reach to last node, That means no node has greater value then x
            {
                temp.next = n;
                n.prev = temp;
            } else {
                n.prev = temp;
                n.next = temp.next;
                temp.next = n;
                if (n.next != null) {
                    n.next.prev = n;
                }
            }
        }
    }

    void revDisplay() {
        if (head == null) {
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        while (temp != null) {
            System.out.print(temp.data + "-->");
            temp = temp.prev; // check for previous links
        }
        System.out.println(" ");
    }

    void revDisplay2() {
        if (head == null) {
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        while (temp != null) {
            System.out.print(temp.data + "-->");
            temp = temp.prev; // check for previous links
        }
        System.out.println(" ");
    }

    public StudentDetails(Connection con) {
        this.con = con;
    }

    public void entries(DataStorage ds) {

        students.put(ds.enrollment, ds);
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO student (enrollment, sem, branch, name, spi, cgpa) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, ds.enrollment);
            ps.setInt(2, ds.sem);
            ps.setString(3, ds.branch);
            ps.setString(4, ds.name);
            ps.setDouble(5, ds.spi);
            ps.setDouble(6, ds.cgpa);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void display(DataStorage ds) {
        System.out.println(students.get(ds.enrollment));
    }

    public void updateDetails(String enroll) {
        Scanner sc = new Scanner(System.in);
        try {
            String NewEnroll = enroll;
            System.out.println("Enter New value of Semester for Updation :");
            int newSem = sc.nextInt();
            System.out.println("Enter Branch For Updation :");
            String newBRanch = sc.next();
            System.out.println("Enter Name For Updation :");
            String NewName = sc.next();
            System.out.println("Enter SPI for Updation :");
            double newSpi = sc.nextDouble();
            System.out.println("Enter CGPA for Updation :");
            double newCpga = sc.nextDouble();
            String query = "UPDATE student SET sem = ?,branch=?,name=?,spi=?,cgpa=? WHERE enrollment = ?";
            PreparedStatement pst = con.prepareCall(query);
            pst.setInt(1, newSem);
            pst.setString(2, newBRanch);
            pst.setString(3, NewName);
            pst.setDouble(4, newSpi);
            pst.setDouble(5, newCpga);
            pst.setString(6, NewEnroll);
            System.out.println("Updating Your MarkSheet....");
            Thread.sleep(1000);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void view(String enrollment, int sem) throws Exception {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM student WHERE enrollment = ? AND sem=?");
            ps.setString(1, enrollment);
            ps.setInt(2, sem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Enrollment: " + rs.getString("enrollment"));
                System.out.println("Semester  : " + rs.getInt("sem"));
                System.out.println("Branch    : " + rs.getString("branch"));
                System.out.println("Name      : " + rs.getString("name"));
                System.out.println("SPI       : " + rs.getDouble("spi"));
                System.out.println("CGPA      : " + rs.getDouble("cgpa"));
            } else {
                System.out.println("No records found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search2(String enrollment) throws Exception {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM student WHERE enrollment = ?");
            ps.setString(1, enrollment);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Enrollment: " + rs.getString("enrollment"));
                System.out.println("Semester  : " + rs.getInt("sem"));
                System.out.println("Branch    : " + rs.getString("branch"));
                System.out.println("Name      : " + rs.getString("name"));
                System.out.println("SPI       : " + rs.getDouble("spi"));
                System.out.println("CGPA      : " + rs.getDouble("cgpa"));
            } else {
                System.out.println("No records found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllStudents() throws Exception {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");
            while (rs.next()) {
                System.out.println("Enrollment: " + rs.getString("enrollment"));
                System.out.println("Semester  : " + rs.getInt("sem"));
                System.out.println("Branch    : " + rs.getString("branch"));
                System.out.println("Name      : " + rs.getString("name"));
                System.out.println("SPI       : " + rs.getDouble("spi"));
                System.out.println("CGPA      : " + rs.getDouble("cgpa"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewRankedStudents() throws Exception {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM student_ranking");
        while (rs.next()) {
            System.out.println("Rank      : " + rs.getInt("rank"));
            System.out.println("Enrollment: " + rs.getString("enrollment"));
            System.out.println("Name      : " + rs.getString("name"));
            System.out.println("CGPA      : " + rs.getDouble("cgpa"));
            System.out.println("---------------------------");
        }
    }

    public void deleteStudent(String enroll) {
        try {
            String query = "Delete from student where enrollment=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, enroll);
            pst.executeUpdate();
            System.out.println("Student is Deleted Sucessfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Linkedlist() {
        try {
            String sql = "SELECT spi FROM student ";
            PreparedStatement cst = con.prepareStatement(sql);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                insertSorted(rs.getDouble("spi"));
            }
            System.out.println("This is the list of SPI'S OF STUDENT ");
            System.out.println();
            System.out.println("[-------HERE IS YOUR LIST-------]");
            System.out.println();
            System.out.println("Preparing List...");
            Thread.sleep(1000);
            revDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql2 = "SELECT cgpa FROM student";
            PreparedStatement pst = con.prepareStatement(sql2);
            ResultSet rs2 = pst.executeQuery();
            while (rs2.next()) {
                insertSorted2(rs2.getDouble("cgpa"));
            }
            System.out.println();
            System.out.println("This is the list of CGPA'S & SPI'S OF STUDENT ");
            System.out.println();
            System.out.println("[-------HERE IS YOUR LIST-------]");
            System.out.println();
            System.out.println("Preparing List...");
            Thread.sleep(1000);
            revDisplay2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateMarksheet(String enrollm, String filename) throws Exception {
        try {
            String sql = "Select*from student where enrollment=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, enrollm);
            ResultSet rs = ps.executeQuery();
            FileWriter bw = new FileWriter(filename + ".txt");
            while (rs.next()) {
                System.out.println("Generating Marksheet...");
                Thread.sleep(1000);
                bw.write("        ------------------------------\n");
                bw.write("[[[      ***Here is Your MarkSheet***      ]]]\n");
                bw.write("        ------------------------------\n");
                bw.write("\nEnrollment: ");
                bw.write(rs.getString("enrollment"));
                bw.write("\nSemester  : ");
                bw.write(rs.getInt("sem") + '0');
                bw.write("\nBranch    : ");
                bw.write(rs.getString("branch"));
                bw.write("\nname      : ");
                bw.write(rs.getString("name"));
                bw.write("\nSPI       : ");
                bw.write((int) rs.getDouble("spi") + '0');
                bw.write("\nCGPA      : ");
                bw.write((int) rs.getDouble("cgpa") + '0');
                System.out.println();
                System.out.println("Your MarkSheet Text File Is Created You Can see Your Marksheet---");
            }
            bw.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class StudentService {

    public StudentService(Connection con) throws Exception {

        Scanner sc = new Scanner(System.in);
        SPI_CGPA spicgpa = new SPI_CGPA(con);
        Boolean b = true;
        StudentDetails studentDetails = new StudentDetails(con);

        while (b) {
            System.out.println("\n1. ENTER YOUR DETAILS TO INSERT INTO DATABASE");
            System.out.println("2. UPDATE YOUR DETAILS");
            System.out.println("3. SEARCH YOUR DETAILS");
            System.out.println("4. VIEW YOUR MARKSHEET");
            System.out.println("5. DISPLAYING SPIS & CGPA'S IN DESCENDING ORDER FROM STUDENT");
            System.out.println("6. EXIT");
            int choice = sc.nextInt();
            String enrollment = null;

            switch (choice) {
                case 1:
                    int i = 1;
                    if (i == 1) {
                        while (true) {
                            System.out.print("Enter your enrollment number: ");
                            enrollment = sc.next();
                            if (enrollment.length() != 14) {
                                System.out.println("Enter Enrollment number which contains exactly 14 digits!");
                                continue;
                            }
                            boolean allDigits = true;
                            for (int j = 0; j < enrollment.length(); j++) {
                                if (!Character.isDigit(enrollment.charAt(j))) {
                                    allDigits = false;
                                    break;
                                }
                            }
                            if (allDigits) {
                                if (spicgpa.isDuplicateEnrollment(enrollment)) {
                                    System.out
                                            .println("Enrollment number already exists! Please enter a unique enrollment number.");
                                    continue;
                                }
                                System.out.println("Enrollment number accepted!");
                                break;
                            } else {
                                System.out.println("Enter digits only!");
                            }
                        }

                        System.out.println("Enter semester number");
                        int sem = sc.nextInt();

                        System.out.println("Enter your branch");
                        String branch = sc.next();

                        System.out.println("Enter your name");
                        String name = sc.next();

                        DataStorage ds = new DataStorage(enrollment, sem, branch, name, i, sem);
                        spicgpa.cgpa(sem);
                        studentDetails.entries(ds);
                    }
                    break;

                case 2:
                    System.out.println("Enter enrollment number for Updation of Detail : ");
                    String enrollUpdate = sc.next();
                    studentDetails.updateDetails(enrollUpdate);
                    break;

                case 4:
                    System.out.println("Enter enrollment number to View Marksheet: ");
                    String enrollSearch = sc.next();
                    System.out.println("Enter Semester Student :");
                    int sem = sc.nextInt();
                    studentDetails.view(enrollSearch, sem);
                    break;
                case 5:
                    studentDetails.Linkedlist();
                    break;
                case 3:
                    System.out.println("Enter Enrollment No:");
                    String s = sc.next();
                    studentDetails.search2(s);
                    break;
                case 6:
                    System.out.println("Thank you for Visit...!");
                    return;
                default:
                    System.out.println("Invalid choice..! Please try again.");
                    break;
            }
        }
    }
}

class TeacherService {

    public TeacherService(Connection con) throws Exception {
        SPI_CGPA s = new SPI_CGPA(con);
        StudentDetails studentDetails = new StudentDetails(con);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. VIEW ALL STUDENTS");
            System.out.println("2. VIEW RANKED STUDENTS BY CGPA");
            System.out.println("3. GENERATE MARKSHEETS FOR STUDENTS.");
            System.out.println("4. UPDATE STUDENT DETAILS");
            System.out.println("5  DELETING STUDENT FROM DATABASE ");
            System.out.println("6. EXIT");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    studentDetails.viewAllStudents();
                    break;

                case 2:
                    studentDetails.viewRankedStudents();
                    break;

                case 3:
                    System.out.println("Enter enrollment number: ");
                    String enrollment = sc.next();
                    System.out.println("Enter semester: ");
                    int sem = sc.nextInt();
                    System.out.println("Enter branch: ");
                    String branch = sc.next();
                    System.out.println("Enter name: ");
                    String name = sc.next();
                    System.out.println("Enter Name for Marksheet File:");
                    String filename = sc.next();
                    studentDetails.generateMarksheet(enrollment, filename);
                    s.cgpa(sem);
                    DataStorage ds = new DataStorage(enrollment, sem, branch, name, s.spi, s.cgpa);
                    studentDetails.entries(ds);

                    break;

                case 4:
                    System.out.println("Enter enrollment number to update: ");
                    String enrollUpdate = sc.next();
                    studentDetails.updateDetails(enrollUpdate);
                    break;
                case 5:
                    System.out.println("Enter Enrollment number For Deleting Student:");
                    String sss = sc.next();
                    studentDetails.deleteStudent(sss);
                    break;
                case 6:
                    System.out.println("THANK YOU FOR YOUR VISIT..!");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
