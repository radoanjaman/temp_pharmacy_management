# 🏥 Pharmacy Management System (Java Swing)

A lightweight, beginner-friendly desktop application for managing pharmacy inventory, dispensing medicines, and generating sale invoices, built entirely using **Java Swing**.

---

## 🌟 Key Features

### 1. 📊 Interactive Dashboard
- **Live Inventory Statistics**: Displays the total number of medicine types and total stock units available.
- **Current Stock Table**: Clear table showing all medicines with ID, Name, Category, Price ($), and Current Stock.
- **Quick Action Buttons**:
  - `+ Add Medicine`: Quickly register new medicines to the inventory.
  - `Sell Medicine`: Switch directly to the Point of Sale (POS) terminal.

### 2. 💊 Add Medicine
- Simple modal dialog to input:
  - **Medicine Name** (e.g., `Paracetamol 500mg`)
  - **Category** (e.g., `Painkillers`, `Antibiotics`)
  - **Unit Price** (e.g., `4.50`)
  - **Initial Stock Quantity** (e.g., `100`)
- Validates numeric inputs and updates the inventory immediately upon saving.

### 3. 🛒 Sell Medicine (Point of Sale & Cart)
- **Category Filter**: Dropdown menu to filter medicines by therapeutic class (or view all).
- **Real-Time Stock Deduction**: As items are selected and added to the cart, the **"Available Stock"** column in the medicine table immediately updates.
- **Cart Management**: Itemized list showing medicine name, unit price, quantity, and line total with a live grand total calculation.
- **Checkout & Bill Generation**:
  - Automatically deducts the purchased quantities permanently from inventory.
  - Generates an itemized receipt bill with invoice number, date/time, items, quantities, prices, and grand total.

---

## 📁 Project Structure

```
PharmacyManagementSystem/
│
├── src/
│   ├── Main.java              # Entry point to launch the application
│   ├── Medicine.java          # Model class representing a medicine
│   ├── CartItem.java          # Model class representing an item in the sale cart
│   └── PharmacyApp.java       # Main UI frame, event handling, and billing logic
│
├── .gitignore                 # Git ignore file for Java projects
└── README.md                  # Project documentation
```

---

## 🛠️ Code Breakdown & Explanation

- **[`Medicine.java`](src/Medicine.java)**:
  Contains fields for `id`, `name`, `category`, `price`, and `stock`.

- **[`CartItem.java`](src/CartItem.java)**:
  Holds a reference to a `Medicine` and the `quantity` being purchased, with a `getTotal()` method (`quantity * price`).

- **[`PharmacyApp.java`](src/PharmacyApp.java)**:
  The core GUI application built using Swing:
  - **`CardLayout`**: Switches seamlessly between the `DASHBOARD` and `SELL` screens.
  - **`createDashboardPanel()`**: Builds the main overview screen with stock metrics and inventory table.
  - **`createSellPanel()`**: Builds the 2-column POS interface with available stock on the left and active cart on the right.
  - **`addToCart()`**: Validates available stock, calculates remaining units, and updates the cart.
  - **`checkout()`**: Permanently deducts stock and displays the formatted invoice receipt dialog.

- **[`Main.java`](src/Main.java)**:
  Uses `SwingUtilities.invokeLater()` to launch the GUI thread safely.

---

## 🚀 How to Run

### Prerequisites
- **Java Development Kit (JDK 8 or higher)** installed.
- Verify installation by running `java -version` and `javac -version` in your terminal.

### Compilation and Execution

#### On Windows (PowerShell):
```powershell
# Navigate to the project directory
cd d:\Projects\PharmacyManagementSystem

# Compile all source files into the 'bin' folder
javac -d bin (Get-ChildItem -Recurse -Filter *.java -Path src | Select-Object -ExpandProperty FullName)

# Run the application
java -cp bin Main
```

#### On Windows (Command Prompt - CMD):
```cmd
cd d:\Projects\PharmacyManagementSystem
if not exist bin mkdir bin
javac -d bin src\*.java
java -cp bin Main
```

#### On macOS / Linux:
```bash
cd PharmacyManagementSystem
mkdir -p bin
javac -d bin src/*.java
java -cp bin Main
```

---

## 📋 Sample Pre-Loaded Inventory

The application comes pre-loaded with realistic pharmaceutical data:
| ID | Medicine Name | Category | Unit Price | Stock |
| :--- | :--- | :--- | :--- | :--- |
| **MED-101** | Paracetamol 500mg | Painkillers | $4.50 | 100 |
| **MED-102** | Ibuprofen 400mg | Painkillers | $7.00 | 50 |
| **MED-103** | Amoxicillin 500mg | Antibiotics | $12.50 | 40 |
| **MED-104** | Vitamin C 1000mg | Vitamins | $10.00 | 80 |
| **MED-105** | Cetirizine 10mg | Allergy | $8.00 | 60 |
| **MED-106** | Omeprazole 20mg | Digestive | $14.00 | 30 |

---

## 📜 License
This project is open-source and available under the [MIT License](LICENSE).
