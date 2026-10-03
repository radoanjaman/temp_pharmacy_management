import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PharmacyApp extends JFrame {

    // 1. Data Lists
    private List<Medicine> medicineList = new ArrayList<>();
    private List<CartItem> cartList = new ArrayList<>();
    private int nextId = 101;
    private int nextInvoice = 1001;

    // 2. Main Navigation Panel (CardLayout)
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    // 3. Dashboard Components
    private JLabel lblTotalMedicines = new JLabel();
    private DefaultTableModel dashboardTableModel;
    private JTable dashboardTable;

    // 4. Sell Screen Components
    private JComboBox<String> cmbCategory = new JComboBox<>();
    private DefaultTableModel sellTableModel;
    private JTable sellTable;
    private JSpinner spinQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel lblCartTotal = new JLabel("Total: $0.00");

    public PharmacyApp() {
        // Basic Window Setup
        setTitle("Pharmacy Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add Initial Sample Medicines
        addSampleMedicines();

        // Build GUI Panels
        mainContainer.add(createDashboardPanel(), "DASHBOARD");
        mainContainer.add(createSellPanel(), "SELL");
        add(mainContainer);

        // Show Dashboard first
        showDashboard();
    }

    // Add Default Sample Data
    private void addSampleMedicines() {
        medicineList.add(new Medicine("MED-101", "Paracetamol 500mg", "Painkillers", 4.50, 100));
        medicineList.add(new Medicine("MED-102", "Ibuprofen 400mg", "Painkillers", 7.00, 50));
        medicineList.add(new Medicine("MED-103", "Amoxicillin 500mg", "Antibiotics", 12.50, 40));
        medicineList.add(new Medicine("MED-104", "Vitamin C 1000mg", "Vitamins", 10.00, 80));
        medicineList.add(new Medicine("MED-105", "Cetirizine 10mg", "Allergy", 8.00, 60));
        medicineList.add(new Medicine("MED-106", "Omeprazole 20mg", "Digestive", 14.00, 30));
        nextId = 107;
    }


    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top Header
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Pharmacy Dashboard");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        // Action Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnAdd = createButton("+ Add Medicine", new Color(0, 130, 130), Color.WHITE);
        btnAdd.addActionListener(e -> openAddMedicineDialog());

        JButton btnSell = createButton("Sell Medicine", new Color(40, 167, 69), Color.WHITE);
        btnSell.addActionListener(e -> showSellScreen());

        btnPanel.add(btnAdd);
        btnPanel.add(btnSell);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);

        // Total Count Label
        lblTotalMedicines.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalMedicines.setForeground(new Color(0, 102, 102));
        lblTotalMedicines.setBorder(new EmptyBorder(10, 0, 5, 0));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(lblTotalMedicines, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // Current Medicine Table
        String[] columns = {"Medicine ID", "Name", "Category", "Price ($)", "Stock"};
        dashboardTableModel = new DefaultTableModel(columns, 0);
        dashboardTable = new JTable(dashboardTableModel);
        dashboardTable.setRowHeight(26);

        panel.add(new JScrollPane(dashboardTable), BorderLayout.CENTER);
        return panel;
    }

    // SCREEN 2: SELL MEDICINE PANEL

    private JPanel createSellPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top Header: Title + Back Button
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Sell Medicine");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnBack = createButton("Back to Dashboard", new Color(108, 117, 125), Color.WHITE);
        btnBack.addActionListener(e -> showDashboard());

        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(btnBack, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Main Center:
        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 15, 0));

        // LEFT: Available Medicines 
        JPanel leftPanel = new JPanel(new BorderLayout(5, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Medicines"));

        // Category Dropdown
        JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        catPanel.add(new JLabel("Category:"));
        cmbCategory.addActionListener(e -> refreshSellTable());
        catPanel.add(cmbCategory);
        leftPanel.add(catPanel, BorderLayout.NORTH);

        // Available Table
        String[] sellCols = {"ID", "Name", "Price ($)", "Available Stock"};
        sellTableModel = new DefaultTableModel(sellCols, 0);
        sellTable = new JTable(sellTableModel);
        sellTable.setRowHeight(24);
        leftPanel.add(new JScrollPane(sellTable), BorderLayout.CENTER);

        // Quantity & Add to Cart
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(new JLabel("Quantity:"));
        spinQuantity.setPreferredSize(new Dimension(60, 26));
        addPanel.add(spinQuantity);

        JButton btnAddToCart = createButton("+ Add to Cart", new Color(0, 130, 130), Color.WHITE);
        btnAddToCart.addActionListener(e -> addToCart());
        addPanel.add(btnAddToCart);
        leftPanel.add(addPanel, BorderLayout.SOUTH);

        // RIGHT: Current Cart 
        JPanel rightPanel = new JPanel(new BorderLayout(5, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Cart & Checkout"));

        String[] cartCols = {"Medicine", "Price", "Qty", "Total"};
        cartTableModel = new DefaultTableModel(cartCols, 0);
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(24);
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Total & Checkout Button
        JPanel checkoutPanel = new JPanel(new BorderLayout(5, 10));
        lblCartTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblCartTotal.setForeground(new Color(0, 102, 102));
        checkoutPanel.add(lblCartTotal, BorderLayout.NORTH);

        JButton btnCheckout = createButton("Checkout & Produce Bill", new Color(40, 167, 69), Color.WHITE);
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setPreferredSize(new Dimension(0, 38));
        btnCheckout.addActionListener(e -> checkout());
        checkoutPanel.add(btnCheckout, BorderLayout.SOUTH);

        rightPanel.add(checkoutPanel, BorderLayout.SOUTH);

        centerGrid.add(leftPanel);
        centerGrid.add(rightPanel);
        panel.add(centerGrid, BorderLayout.CENTER);

        return panel;
    }

    
    // CORE ACTIONS: ADD MEDICINE, ADD TO CART, CHECKOUT

    // 1. Open Add Medicine Dialog
    private void openAddMedicineDialog() {
        JDialog dialog = new JDialog(this, "Add New Medicine", true);
        dialog.setSize(350, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField txtName = new JTextField();
        JTextField txtCategory = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtStock = new JTextField();

        dialog.add(new JLabel("  Medicine Name:"));
        dialog.add(txtName);
        dialog.add(new JLabel("  Category:"));
        dialog.add(txtCategory);
        dialog.add(new JLabel("  Price ($):"));
        dialog.add(txtPrice);
        dialog.add(new JLabel("  Stock Quantity:"));
        dialog.add(txtStock);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnSave = createButton("Save", new Color(0, 130, 130), Color.WHITE);
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String category = txtCategory.getText().trim();
                double price = Double.parseDouble(txtPrice.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());

                if (name.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                    return;
                }

                String id = "MED-" + (nextId++);
                medicineList.add(new Medicine(id, name, category, price, stock));

                JOptionPane.showMessageDialog(dialog, "Medicine added successfully!");
                dialog.dispose();

                refreshDashboardTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for Price and Stock.");
            }
        });

        dialog.add(btnCancel);
        dialog.add(btnSave);
        dialog.setVisible(true);
    }

    // 2. Add Selected Medicine to Cart 
    private void addToCart() {
        int selectedRow = sellTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine from the table first.");
            return;
        }

        String medId = (String) sellTableModel.getValueAt(selectedRow, 0);
        Medicine selectedMed = findMedicineById(medId);
        int qty = (int) spinQuantity.getValue();

        // Calculate how many are already in cart
        int alreadyInCart = 0;
        CartItem existingItem = null;
        for (CartItem item : cartList) {
            if (item.medicine.id.equals(medId)) {
                alreadyInCart = item.quantity;
                existingItem = item;
                break;
            }
        }

        int remainingStock = selectedMed.stock - alreadyInCart;
        if (qty > remainingStock) {
            JOptionPane.showMessageDialog(this, "Not enough stock! Only " + remainingStock + " available.");
            return;
        }

        if (existingItem != null) {
            existingItem.quantity += qty;
        } else {
            cartList.add(new CartItem(selectedMed, qty));
        }

        spinQuantity.setValue(1);
        refreshCartTable();
        refreshSellTable(); // Available stock in table immediately updates!
    }

    // 3. Checkout 
    private void checkout() {
        if (cartList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Please add medicines first.");
            return;
        }

        // 1. Deduct stock permanently
        for (CartItem item : cartList) {
            item.medicine.stock -= item.quantity;
        }

        // 2. Build Bill String
        StringBuilder bill = new StringBuilder();
        bill.append("=========================================\n");
        bill.append("           PHARMACY SALE BILL            \n");
        bill.append("=========================================\n");
        bill.append(" Invoice No: INV-").append(nextInvoice++).append("\n");
        bill.append(" Date      : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        bill.append("-----------------------------------------\n");
        bill.append(String.format(" %-18s %-5s %-8s %-8s\n", "Item", "Qty", "Price", "Total"));
        bill.append("-----------------------------------------\n");

        double grandTotal = 0;
        for (CartItem item : cartList) {
            bill.append(String.format(" %-18s %-5d $%-7.2f $%-7.2f\n",
                    item.medicine.name, item.quantity, item.medicine.price, item.getTotal()));
            grandTotal += item.getTotal();
        }

        bill.append("-----------------------------------------\n");
        bill.append(String.format(" TOTAL AMOUNT DUE:              $%-7.2f\n", grandTotal));
        bill.append("=========================================\n");
        bill.append("       Thank you for your purchase!      \n");
        bill.append("=========================================\n");

        // 3. Show Bill Dialog
        JTextArea txtBill = new JTextArea(bill.toString());
        txtBill.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtBill.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(txtBill), "Receipt Bill", JOptionPane.INFORMATION_MESSAGE);

        // 4. Clear Cart and Refresh UI
        cartList.clear();
        refreshCartTable();
        refreshSellTable();
        refreshDashboardTable();
    }

    
    // HELPER METHODS: NAVIGATION & TABLE UPDATES
    

    public void showDashboard() {
        refreshDashboardTable();
        cardLayout.show(mainContainer, "DASHBOARD");
    }

    public void showSellScreen() {
        updateCategoryDropdown();
        refreshSellTable();
        refreshCartTable();
        cardLayout.show(mainContainer, "SELL");
    }

    private void refreshDashboardTable() {
        dashboardTableModel.setRowCount(0);
        int totalUnits = 0;
        for (Medicine m : medicineList) {
            dashboardTableModel.addRow(new Object[]{m.id, m.name, m.category, String.format("%.2f", m.price), m.stock});
            totalUnits += m.stock;
        }
        lblTotalMedicines.setText("Total Medicines: " + medicineList.size() + " Types  (" + totalUnits + " Total Units in Stock)");
    }

    private void updateCategoryDropdown() {
        String prev = (String) cmbCategory.getSelectedItem();
        cmbCategory.removeAllItems();
        cmbCategory.addItem("All Categories");

        for (Medicine m : medicineList) {
            boolean exists = false;
            for (int i = 0; i < cmbCategory.getItemCount(); i++) {
                if (cmbCategory.getItemAt(i).equalsIgnoreCase(m.category)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                cmbCategory.addItem(m.category);
            }
        }

        if (prev != null) cmbCategory.setSelectedItem(prev);
    }

    private void refreshSellTable() {
        String selectedCat = (String) cmbCategory.getSelectedItem();
        sellTableModel.setRowCount(0);

        for (Medicine m : medicineList) {
            if (selectedCat == null || selectedCat.equals("All Categories") || m.category.equalsIgnoreCase(selectedCat)) {
                // Calculate remaining stock considering what is already in cart
                int inCart = 0;
                for (CartItem item : cartList) {
                    if (item.medicine.id.equals(m.id)) {
                        inCart += item.quantity;
                    }
                }
                int available = m.stock - inCart;
                sellTableModel.addRow(new Object[]{m.id, m.name, String.format("%.2f", m.price), available});
            }
        }
    }

    private void refreshCartTable() {
        cartTableModel.setRowCount(0);
        double total = 0;
        for (CartItem item : cartList) {
            cartTableModel.addRow(new Object[]{
                    item.medicine.name,
                    String.format("%.2f", item.medicine.price),
                    item.quantity,
                    String.format("%.2f", item.getTotal())
            });
            total += item.getTotal();
        }
        lblCartTotal.setText(String.format("Total: $%.2f", total));
    }

    private Medicine findMedicineById(String id) {
        for (Medicine m : medicineList) {
            if (m.id.equals(id)) return m;
        }
        return null;
    }

    //button customization
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
