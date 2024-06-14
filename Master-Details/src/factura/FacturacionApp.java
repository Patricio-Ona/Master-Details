package factura;

import Conexion.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FacturacionApp extends JFrame {

    private Connection connection;
    private JTable facturasTable;
    private DefaultTableModel facturasTableModel;

    public FacturacionApp() {
        // Configuración de la ventana
        setTitle("Facturas Generadas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar conexión a la base de datos
        Conexion conexion = new Conexion();
        connection = conexion.getConnection();

        // Crear la tabla de facturas generadas
        String[] columnNames = {"Numero", "Cliente", "Fecha", "Subtotal", "Descuento", "IVA", "ICE", "Forma Pago", "Status"};
        facturasTableModel = new DefaultTableModel(columnNames, 0);
        facturasTable = new JTable(facturasTableModel);
        JScrollPane scrollPane = new JScrollPane(facturasTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton btnNuevaFactura = new JButton("Nueva Factura");
        JButton btnVerFactura = new JButton("Ver Factura");
        JButton btnEliminarFactura = new JButton("Eliminar Factura");

        btnNuevaFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Interfaz(connection);
                dispose();
            }
        });

        btnVerFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verFactura();
            }
        });

        btnEliminarFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFactura();
            }
        });

        buttonPanel.add(btnNuevaFactura);
        buttonPanel.add(btnVerFactura);
        buttonPanel.add(btnEliminarFactura);

        add(buttonPanel, BorderLayout.SOUTH);

        cargarFacturas();
        setVisible(true);
    }

    private void cargarFacturas() {
        facturasTableModel.setRowCount(0); // Limpiar tabla antes de cargar datos

        try {
            String query = "SELECT FACNUMERO, CLIIDENTIFICACION, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACICE, FACFORMAPAGO, FACSTATUS FROM FACTURAS";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                facturasTableModel.addRow(new Object[]{
                        rs.getString("FACNUMERO"),
                        rs.getString("CLIIDENTIFICACION"),
                        rs.getDate("FACFECHA"),
                        rs.getDouble("FACSUBTOTAL"),
                        rs.getDouble("FACDESCUENTO"),
                        rs.getDouble("FACIVA"),
                        rs.getDouble("FACICE"),
                        rs.getString("FACFORMAPAGO"),
                        rs.getString("FACSTATUS")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class FacturaGeneradaForm extends JFrame {
        public FacturaGeneradaForm(String facturaNo, String cliente, String fecha, String suma, String iva0, String ice, String iva, String total, DefaultTableModel tableModel) {
            setTitle("Factura Generada");
            setSize(900, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel panelInfo = new JPanel(new GridLayout(5, 2));
            panelInfo.setBackground(new Color(252, 243, 207)); // Verde claro pastel

            JLabel lblFacturaNo = new JLabel("Factura No.: " + facturaNo);
            JLabel lblCliente = new JLabel("Cliente: " + cliente);
            JLabel lblFecha = new JLabel("Fecha: " + fecha);
            JLabel lblCodigoSRITitle = new JLabel("Código SRI: 001-001-009");

            panelInfo.add(lblFacturaNo);
            panelInfo.add(new JLabel(""));
            panelInfo.add(lblCliente);
            panelInfo.add(new JLabel(""));
            panelInfo.add(lblFecha);
            panelInfo.add(new JLabel(""));
            panelInfo.add(lblCodigoSRITitle);
            panelInfo.add(new JLabel(""));

            String[] columnNames = {"Producto", "Descripcion", "Uni Med", "Cantidad", "Val Uni", "IVA %", "Subtotal"};
            DefaultTableModel newTableModel = new DefaultTableModel(columnNames, 0);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                newTableModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4),
                        tableModel.getValueAt(i, 5),
                        tableModel.getValueAt(i, 6)
                });
            }

            JTable newTable = new JTable(newTableModel);
            JScrollPane scrollPane = new JScrollPane(newTable);

            JPanel panelTotal = new JPanel(new GridBagLayout());
            panelTotal.setBackground(new Color(252, 243, 207)); // Verde claro pastel

            JLabel lblSumaTitle = new JLabel("SUMA:");
            JLabel lblSumaValue = new JLabel(suma);
            JLabel lblIVA0Title = new JLabel("IVA 0%:");
            JLabel lblIVA0Value = new JLabel(iva0);
            JLabel lblICETitle = new JLabel("ICE:");
            JLabel lblICEValue = new JLabel(ice);
            JLabel lblIVATitle = new JLabel("IVA 15%:");
            JLabel lblIVAValue = new JLabel(iva);
            JLabel lblTotalTitle = new JLabel("TOTAL:");
            JLabel lblTotalValue = new JLabel(total);

            lblSumaTitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVA0Title.setFont(new Font("Arial", Font.BOLD, 14));
            lblICETitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVATitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblTotalTitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblSumaValue.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVA0Value.setFont(new Font("Arial", Font.BOLD, 14));
            lblICEValue.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVAValue.setFont(new Font("Arial", Font.BOLD, 14));
            lblTotalValue.setFont(new Font("Arial", Font.BOLD, 14));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 5, 5, 20);

            panelTotal.add(lblSumaTitle, gbc);
            panelTotal.add(lblSumaValue, gbc);
            panelTotal.add(lblIVA0Title, gbc);
            panelTotal.add(lblIVA0Value, gbc);
            panelTotal.add(lblICETitle, gbc);
            panelTotal.add(lblICEValue, gbc);
            panelTotal.add(lblIVATitle, gbc);
            panelTotal.add(lblIVAValue, gbc);
            panelTotal.add(lblTotalTitle, gbc);
            panelTotal.add(lblTotalValue, gbc);

            add(panelInfo, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(panelTotal, BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    private void verFactura() {
        int selectedRow = facturasTable.getSelectedRow();
        if (selectedRow != -1) {
            String facturaNo = (String) facturasTableModel.getValueAt(selectedRow, 0);
            String cliente = (String) facturasTableModel.getValueAt(selectedRow, 1);
            String fecha = facturasTableModel.getValueAt(selectedRow, 2).toString();
            String suma = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 3));
            String descuento = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 4));
            String iva = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 5));
            String ice = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 6));
            String total = String.format("%.2f", (Double) facturasTableModel.getValueAt(selectedRow, 3) - (Double) facturasTableModel.getValueAt(selectedRow, 4) + (Double) facturasTableModel.getValueAt(selectedRow, 5) + (Double) facturasTableModel.getValueAt(selectedRow, 6));

            String[] columnNames = {"Producto", "Descripcion", "Uni Med", "Cantidad", "Val Uni", "IVA %", "Subtotal"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            try {
                String query = "SELECT P.PROCODIGO, P.PRODESCRIPCION, P.PROUNIDADMEDIDA, X.PXFCANTIDAD, X.PXFVALOR, 15 AS IVA, X.PXFSUBTOTAL " +
                        "FROM PXF X INNER JOIN PRODUCTOS P ON X.PROCODIGO = P.PROCODIGO WHERE X.FACNUMERO = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, facturaNo);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("PROCODIGO"),
                            rs.getString("PRODESCRIPCION"),
                            rs.getString("PROUNIDADMEDIDA"),
                            rs.getDouble("PXFCANTIDAD"),
                            rs.getDouble("PXFVALOR"),
                            rs.getInt("IVA"),
                            rs.getDouble("PXFSUBTOTAL")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            new FacturaGeneradaForm(facturaNo, cliente, fecha, suma, "0.00", ice, iva, total, tableModel);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una factura para ver.");
        }
    }

    private void eliminarFactura() {
        int selectedRow = facturasTable.getSelectedRow();
        if (selectedRow != -1) {
            String facturaNo = (String) facturasTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea anular la factura?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String updateQuery = "UPDATE FACTURAS SET FACSTATUS = 'ANU' WHERE FACNUMERO = ?";
                    PreparedStatement ps = connection.prepareStatement(updateQuery);
                    ps.setString(1, facturaNo);
                    ps.executeUpdate();
                    facturasTableModel.setValueAt("ANU", selectedRow, 8);
                    JOptionPane.showMessageDialog(this, "Factura anulada exitosamente.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al anular la factura.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una factura para anular.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FacturacionApp::new);
    }

    public class Interfaz extends JFrame {

        private Connection connection;
        private JTextField txtFacturaNo, txtClienteID, txtFecha, txtClienteNombre;
        private JTable table;
        private DefaultTableModel tableModel;
        private JLabel lblSuma, lblIVA, lblIVA0, lblICE, lblTotal, lblCodigoSRI;
        private JComboBox<String> comboFormaPago;
        private double suma = 0.0, iva = 0.0, total = 0.0;

        public Interfaz(Connection connection) {
            this.connection = connection;

            // Configuración de la ventana
            setTitle("FACTURA");
            setSize(900, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            getContentPane().setBackground(Color.WHITE);

            // Crear paneles para la interfaz
            JPanel panelInfo = new JPanel(new GridLayout(6, 2)); // Aumentado a 6 filas para incluir forma de pago
            panelInfo.setBackground(new Color(252, 243, 207)); // Verde claro pastel
            JPanel panelTable = new JPanel(new BorderLayout());
            panelTable.setBackground(Color.WHITE);
            JPanel panelButtons = new JPanel(new GridLayout(6, 1, 5, 5)); // Se eliminó un botón
            panelButtons.setBackground(Color.WHITE);
            JPanel panelTotal = new JPanel(new GridBagLayout());
            panelTotal.setBackground(new Color(252, 243, 207)); // Verde claro pastel

            // Información de la factura
            JLabel lblFacturaNo = new JLabel("Factura No.: ");
            JLabel lblClienteID = new JLabel("ID Cliente: ");
            JLabel lblClienteNombre = new JLabel("Nombre Cliente: ");
            JLabel lblFecha = new JLabel("Fecha: ");
            JLabel lblFormaPago = new JLabel("Forma de Pago: ");
            JLabel lblCodigoSRITitle = new JLabel("Código SRI: ");
            lblCodigoSRI = new JLabel("001-001-009");

            txtFacturaNo = new JTextField();
            txtFacturaNo.setEditable(false);
            txtClienteID = new JTextField();
            txtClienteNombre = new JTextField();
            txtClienteNombre.setEditable(false);
            txtFecha = new JTextField();
            txtFecha.setEditable(false);

            // ComboBox para la forma de pago
            comboFormaPago = new JComboBox<>(new String[]{"EFECT", "TARCR", "TARDB", "TRANS", "CHEQ"});

            panelInfo.add(lblFacturaNo);
            panelInfo.add(txtFacturaNo);
            panelInfo.add(lblClienteID);
            panelInfo.add(txtClienteID);
            panelInfo.add(lblClienteNombre);
            panelInfo.add(txtClienteNombre);
            panelInfo.add(lblFecha);
            panelInfo.add(txtFecha);
            panelInfo.add(lblFormaPago);
            panelInfo.add(comboFormaPago);
            panelInfo.add(lblCodigoSRITitle);
            panelInfo.add(lblCodigoSRI);

            // Botones para guardar, cancelar, actualizar y eliminar productos de la factura, y para generar la factura
            JButton btnGuardarFactura = new JButton("Guardar Factura");
            JButton btnCancelarFactura = new JButton("Cancelar Factura");
            JButton btnAddProduct = new JButton("Añadir Producto");
            JButton btnEliminarProducto = new JButton("Eliminar Producto");
            JButton btnGenerarFactura = new JButton("Generar Factura");
            JButton btnVerFacturas = new JButton("Ver Facturas");

            panelButtons.add(btnGuardarFactura);
            panelButtons.add(btnCancelarFactura);
            panelButtons.add(btnAddProduct);
            panelButtons.add(btnEliminarProducto);
            panelButtons.add(btnGenerarFactura);
            panelButtons.add(btnVerFacturas);

            btnGuardarFactura.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarFactura();
                    limpiarCampos();
                }
            });

            btnCancelarFactura.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelarFactura();
                }
            });

            btnAddProduct.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ProductoDialog(Interfaz.this, connection);
                }
            });

            btnEliminarProducto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmarEliminacionProducto();
                }
            });

            btnGenerarFactura.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generarFactura();
                }
            });

            btnVerFacturas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new VerFacturasForm(connection);
                }
            });

            txtClienteID.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    buscarClientePorID(txtClienteID.getText().trim());
                }
            });

            // Crear la tabla de detalles de la factura
            String[] columnNames = {"Producto", "Descripcion", "Uni Med", "Cantidad", "Val Uni", "IVA %", "Subtotal"};
            tableModel = new DefaultTableModel(columnNames, 0);
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            panelTable.add(scrollPane, BorderLayout.CENTER);

            // Total de la factura
            lblSuma = new JLabel("SUMA: $0.00");
            lblIVA = new JLabel("IVA 15%: $0.00");
            lblIVA0 = new JLabel("IVA 0%: $0.00");
            lblICE = new JLabel("ICE: $0.00");
            lblTotal = new JLabel("TOTAL: $0.00");

            lblSuma.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVA.setFont(new Font("Arial", Font.BOLD, 14));
            lblIVA0.setFont(new Font("Arial", Font.BOLD, 14));
            lblICE.setFont(new Font("Arial", Font.BOLD, 14));
            lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1; // Cambiado para alinear a la derecha
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.anchor = GridBagConstraints.EAST; // Cambiado para alinear a la derecha
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 5, 5, 20); // Añadir margen derecho

            panelTotal.add(lblSuma, gbc);
            panelTotal.add(lblIVA0, gbc);
            panelTotal.add(lblICE, gbc);
            panelTotal.add(lblIVA, gbc);
            panelTotal.add(lblTotal, gbc);

            // Agregar paneles a la ventana
            add(panelInfo, BorderLayout.NORTH);
            add(panelTable, BorderLayout.CENTER);
            add(panelButtons, BorderLayout.WEST);
            add(panelTotal, BorderLayout.SOUTH);

            // Establecer el número de factura y la fecha actual
            setFacturaNo();
            setFechaActual();

            setVisible(true);
        }

        private void setFacturaNo() {
            String query = "SELECT MAX(FACNUMERO) FROM FACTURAS";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getString(1) != null) {
                    String maxFacturaNo = rs.getString(1);
                    int num = Integer.parseInt(maxFacturaNo.substring(3)) + 1;
                    txtFacturaNo.setText(String.format("FAC%06d", num));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtFacturaNo.setText("FAC000001");
            }
        }

        private void setFechaActual() {
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            txtFecha.setText(fechaActual.format(formatter));
        }

        public void addProduct(String producto, String descripcion, String uniMed, double cantidad, double valUni, int ivaPorcentaje) {
            double subtotal = cantidad * valUni;
            tableModel.addRow(new Object[]{producto, descripcion, uniMed, cantidad, valUni, ivaPorcentaje, subtotal});
            suma += cantidad * valUni;
            iva += cantidad * valUni * (ivaPorcentaje / 100.0);
            total = suma + iva;
            updateTotalLabels();
        }

        private void updateTotalLabels() {
            lblSuma.setText(String.format("SUMA: $%.2f", suma));
            lblIVA.setText(String.format("IVA 15%%: $%.2f", iva));
            lblIVA0.setText(String.format("IVA 0%%: $0.00")); // Valor fijo para IVA 0%
            lblICE.setText(String.format("ICE: $0.00")); // Valor fijo para ICE
            lblTotal.setText(String.format("TOTAL: $%.2f", total));
        }

        private void confirmarEliminacionProducto() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el producto?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    eliminarProducto(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            }
        }

        private void eliminarProducto(int selectedRow) {
            double subtotal = (Double) tableModel.getValueAt(selectedRow, 6);
            double cantidad = (Double) tableModel.getValueAt(selectedRow, 3);
            double valUni = (Double) tableModel.getValueAt(selectedRow, 4);
            int ivaPorcentaje = (Integer) tableModel.getValueAt(selectedRow, 5);
            suma -= cantidad * valUni;
            iva -= cantidad * valUni * (ivaPorcentaje / 100.0);
            total = suma + iva;
            tableModel.removeRow(selectedRow);
            updateTotalLabels();
        }

        private void guardarFactura() {
            String clienteID = txtClienteID.getText().trim();
            String facturaNo = txtFacturaNo.getText().trim();
            String fecha = txtFecha.getText().trim();
            String formaPago = comboFormaPago.getSelectedItem().toString();

            if (clienteID.isEmpty() || facturaNo.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos de la factura.");
                return;
            }

            if (clienteExiste(clienteID)) {
                try {
                    connection.setAutoCommit(false);

                    // Insertar la factura
                    String insertFacturaQuery = "INSERT INTO FACTURAS (FACNUMERO, CLIIDENTIFICACION, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACICE, FACFORMAPAGO, FACSTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmtFactura = connection.prepareStatement(insertFacturaQuery)) {
                        stmtFactura.setString(1, facturaNo);
                        stmtFactura.setString(2, clienteID);
                        stmtFactura.setString(3, fecha);
                        stmtFactura.setDouble(4, suma);
                        stmtFactura.setDouble(5, 0.0);
                        stmtFactura.setDouble(6, iva);
                        stmtFactura.setDouble(7, 0.0);
                        stmtFactura.setString(8, formaPago);
                        stmtFactura.setString(9, "PAG");
                        stmtFactura.executeUpdate();
                    }

                    // Insertar los detalles de la factura
                    String insertDetalleQuery = "INSERT INTO PXF (FACNUMERO, PROCODIGO, PXFCANTIDAD, PXFVALOR, PXFSUBTOTAL, PXFSTATUS) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmtDetalle = connection.prepareStatement(insertDetalleQuery)) {
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            double cantidad = (Double) tableModel.getValueAt(i, 3);
                            double valor = (Double) tableModel.getValueAt(i, 4);
                            double subtotal = cantidad * valor;
                            stmtDetalle.setString(1, facturaNo);
                            stmtDetalle.setString(2, (String) tableModel.getValueAt(i, 0));
                            stmtDetalle.setDouble(3, cantidad);
                            stmtDetalle.setDouble(4, valor);
                            stmtDetalle.setDouble(5, subtotal);
                            stmtDetalle.setString(6, "ACT");
                            stmtDetalle.addBatch();
                        }
                        stmtDetalle.executeBatch();
                    }

                    connection.commit();
                    JOptionPane.showMessageDialog(this, "Factura guardada exitosamente.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al guardar la factura.");
                    try {
                        connection.rollback();
                    } catch (SQLException rollbackEx) {
                        rollbackEx.printStackTrace();
                    }
                } finally {
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                int option = JOptionPane.showConfirmDialog(this, "El cliente no existe. ¿Desea registrarlo?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    registrarCliente(clienteID);
                }
            }
        }

        private void cancelarFactura() {
            tableModel.setRowCount(0);
            suma = 0.0;
            iva = 0.0;
            total = 0.0;
            updateTotalLabels();
            JOptionPane.showMessageDialog(this, "Factura cancelada.");
        }

        private void limpiarCampos() {
            txtClienteID.setText("");
            txtClienteNombre.setText("");
            tableModel.setRowCount(0);
            suma = 0.0;
            iva = 0.0;
            total = 0.0;
            updateTotalLabels();
            setFacturaNo();
            setFechaActual();
        }

        private boolean clienteExiste(String clienteID) {
            boolean existe = false;
            String query = "SELECT COUNT(*) FROM CLIENTES WHERE CLIIDENTIFICACION = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, clienteID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return existe;
        }

        private void buscarClientePorID(String identificacion) {
            String query = "SELECT CONCAT(CLINOMBRE1, ' ', IFNULL(CLINOMBRE2, ''), ' ', CLIAPELLIDO1, ' ', IFNULL(CLIAPELLIDO2, '')) AS nombre FROM CLIENTES WHERE CLIIDENTIFICACION = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, identificacion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    txtClienteNombre.setText(rs.getString("nombre"));
                } else {
                    txtClienteNombre.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void registrarCliente(String clienteID) {
            RegistrarClienteForm form = new RegistrarClienteForm(clienteID, connection);
            form.setVisible(true);
        }

        private void generarFactura() {
            new FacturaGeneradaForm(txtFacturaNo.getText(), txtClienteID.getText(), txtFecha.getText(), lblSuma.getText(), lblIVA0.getText(), lblICE.getText(), lblIVA.getText(), lblTotal.getText(), tableModel);
        }

        // Clase interna para el diálogo de producto
        public class ProductoDialog extends JDialog {

            private JTextField txtProducto, txtDescripcion, txtUniMed, txtCantidad, txtValUni;
            private JComboBox<String> comboIVAPorcentaje;
            private Interfaz parent;
            private Connection connection;

            public ProductoDialog(Interfaz parent, Connection connection) {
                super(parent, "Añadir Producto", true);
                this.parent = parent;
                this.connection = connection;
                setLayout(new GridLayout(7, 2));
                setSize(400, 300);

                JLabel lblProducto = new JLabel("Producto: ");
                JLabel lblDescripcion = new JLabel("Descripcion: ");
                JLabel lblUniMed = new JLabel("Uni Med: ");
                JLabel lblCantidad = new JLabel("Cantidad: ");
                JLabel lblValUni = new JLabel("Val Uni: ");
                JLabel lblIVAPorcentaje = new JLabel("IVA %: ");

                txtProducto = new JTextField();
                txtDescripcion = new JTextField();
                txtDescripcion.setEditable(false);
                txtUniMed = new JTextField();
                txtUniMed.setEditable(false);
                txtCantidad = new JTextField();
                txtValUni = new JTextField();
                txtValUni.setEditable(false);

                // ComboBox para IVA %
                comboIVAPorcentaje = new JComboBox<>(new String[]{"0", "8", "15"});

                // Validaciones
                txtCantidad.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.') {
                            e.consume();
                        }
                        if (c == '.' && txtCantidad.getText().contains(".")) {
                            e.consume();
                        }
                    }
                });

                txtProducto.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String producto = txtProducto.getText();
                        if (!producto.isEmpty()) {
                            cargarDatosProducto(producto);
                        }
                    }
                });

                JButton btnAdd = new JButton("Añadir");
                btnAdd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (txtCantidad.getText().isEmpty() || comboIVAPorcentaje.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(ProductoDialog.this, "Por favor, complete todos los campos.");
                            return;
                        }
                        String producto = txtProducto.getText();
                        String descripcion = txtDescripcion.getText();
                        String uniMed = txtUniMed.getText();
                        double cantidad = Double.parseDouble(txtCantidad.getText());
                        double valUni = Double.parseDouble(txtValUni.getText());
                        int ivaPorcentaje = Integer.parseInt((String) comboIVAPorcentaje.getSelectedItem());
                        parent.addProduct(producto, descripcion, uniMed, cantidad, valUni, ivaPorcentaje);
                        dispose();
                    }
                });

                add(lblProducto);
                add(txtProducto);
                add(lblDescripcion);
                add(txtDescripcion);
                add(lblUniMed);
                add(txtUniMed);
                add(lblCantidad);
                add(txtCantidad);
                add(lblValUni);
                add(txtValUni);
                add(lblIVAPorcentaje);
                add(comboIVAPorcentaje);
                add(new JLabel(""));  // Empty label for layout purpose
                add(btnAdd);

                setLocationRelativeTo(parent);
                setVisible(true);
            }

            private void cargarDatosProducto(String producto) {
                String query = "SELECT PRODESCRIPCION, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PROCODIGO = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, producto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            txtDescripcion.setText(rs.getString("PRODESCRIPCION"));
                            txtUniMed.setText(rs.getString("PROUNIDADMEDIDA"));
                            txtValUni.setText(rs.getString("PROPRECIOUM"));
                        } else {
                            txtDescripcion.setText("");
                            txtUniMed.setText("");
                            txtValUni.setText("");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Clase interna para actualizar un producto
        public class ActualizarProductoDialog extends JDialog {

            private JTextField txtProducto, txtDescripcion, txtUniMed, txtCantidad, txtValUni;
            private JComboBox<String> comboIVAPorcentaje;
            private Interfaz parent;
            private Connection connection;
            private int selectedRow;

            public ActualizarProductoDialog(Interfaz parent, Connection connection, int selectedRow, String producto, String descripcion, String uniMed, double cantidad, double valUni, int ivaPorcentaje) {
                super(parent, "Actualizar Producto", true);
                this.parent = parent;
                this.connection = connection;
                this.selectedRow = selectedRow;
                setLayout(new GridLayout(7, 2));
                setSize(400, 300);

                JLabel lblProducto = new JLabel("Producto: ");
                JLabel lblDescripcion = new JLabel("Descripcion: ");
                JLabel lblUniMed = new JLabel("Uni Med: ");
                JLabel lblCantidad = new JLabel("Cantidad: ");
                JLabel lblValUni = new JLabel("Val Uni: ");
                JLabel lblIVAPorcentaje = new JLabel("IVA %: ");

                txtProducto = new JTextField(producto);
                txtDescripcion = new JTextField(descripcion);
                txtDescripcion.setEditable(false);
                txtUniMed = new JTextField(uniMed);
                txtUniMed.setEditable(false);
                txtCantidad = new JTextField(String.valueOf(cantidad));
                txtValUni = new JTextField(String.valueOf(valUni));
                txtValUni.setEditable(false);
                comboIVAPorcentaje = new JComboBox<>(new String[]{"0", "8", "15"});
                comboIVAPorcentaje.setSelectedItem(String.valueOf(ivaPorcentaje));

                // Validaciones
                txtCantidad.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.') {
                            e.consume();
                        }
                        if (c == '.' && txtCantidad.getText().contains(".")) {
                            e.consume();
                        }
                    }
                });

                txtProducto.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String producto = txtProducto.getText();
                        if (!producto.isEmpty()) {
                            cargarDatosProducto(producto);
                        }
                    }
                });

                JButton btnUpdate = new JButton("Actualizar");
                btnUpdate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (txtCantidad.getText().isEmpty() || comboIVAPorcentaje.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(ActualizarProductoDialog.this, "Por favor, complete todos los campos.");
                            return;
                        }
                        actualizarProducto();
                        dispose();
                    }
                });

                add(lblProducto);
                add(txtProducto);
                add(lblDescripcion);
                add(txtDescripcion);
                add(lblUniMed);
                add(txtUniMed);
                add(lblCantidad);
                add(txtCantidad);
                add(lblValUni);
                add(txtValUni);
                add(lblIVAPorcentaje);
                add(comboIVAPorcentaje);
                add(new JLabel(""));  // Empty label for layout purpose
                add(btnUpdate);

                setLocationRelativeTo(parent);
                setVisible(true);
            }

            private void cargarDatosProducto(String producto) {
                String query = "SELECT PRODESCRIPCION, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PROCODIGO = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, producto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            txtDescripcion.setText(rs.getString("PRODESCRIPCION"));
                            txtUniMed.setText(rs.getString("PROUNIDADMEDIDA"));
                            txtValUni.setText(rs.getString("PROPRECIOUM"));
                        } else {
                            txtDescripcion.setText("");
                            txtUniMed.setText("");
                            txtValUni.setText("");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void actualizarProducto() {
                String producto = txtProducto.getText();
                String descripcion = txtDescripcion.getText();
                String uniMed = txtUniMed.getText();
                double cantidad = Double.parseDouble(txtCantidad.getText());
                double valUni = Double.parseDouble(txtValUni.getText());
                int ivaPorcentaje = Integer.parseInt((String) comboIVAPorcentaje.getSelectedItem());

                double subtotal = cantidad * valUni;
                parent.tableModel.setValueAt(producto, selectedRow, 0);
                parent.tableModel.setValueAt(descripcion, selectedRow, 1);
                parent.tableModel.setValueAt(uniMed, selectedRow, 2);
                parent.tableModel.setValueAt(cantidad, selectedRow, 3);
                parent.tableModel.setValueAt(valUni, selectedRow, 4);
                parent.tableModel.setValueAt(ivaPorcentaje, selectedRow, 5);
                parent.tableModel.setValueAt(subtotal, selectedRow, 6);

                parent.suma = 0.0;
                parent.iva = 0.0;
                for (int i = 0; i < parent.tableModel.getRowCount(); i++) {
                    parent.suma += (Double) parent.tableModel.getValueAt(i, 3) * (Double) parent.tableModel.getValueAt(i, 4);
                    parent.iva += (Double) parent.tableModel.getValueAt(i, 3) * (Double) parent.tableModel.getValueAt(i, 4) * ((Integer) parent.tableModel.getValueAt(i, 5) / 100.0);
                }
                parent.total = parent.suma + parent.iva;
                parent.updateTotalLabels();
            }
        }

        // Clase interna para registrar un cliente
        public class RegistrarClienteForm extends JFrame {
            private JTextField txtClienteID;
            private JTextField txtIdentificacion;
            private JTextField txtNombre1;
            private JTextField txtNombre2;
            private JTextField txtApellido1;
            private JTextField txtApellido2;
            private JTextField txtDireccion;
            private JTextField txtTelefono;
            private JTextField txtCelular;
            private JTextField txtEmail;
            private JComboBox<String> comboTipo;
            private JTextField txtStatus;
            private JButton btnGuardar;
            private Connection connection;

            public RegistrarClienteForm(String identificacion, Connection connection) {
                setTitle("Registrar Cliente");
                setSize(400, 500);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setLocationRelativeTo(null);
                this.connection = connection;

                // Crear componentes
                txtClienteID = new JTextField(15);
                txtClienteID.setEditable(false);
                txtIdentificacion = new JTextField(identificacion, 15);
                txtIdentificacion.setEditable(false); // Bloquear el campo de Identificación
                txtNombre1 = new JTextField(15);
                txtNombre2 = new JTextField(15);
                txtApellido1 = new JTextField(15);
                txtApellido2 = new JTextField(15);
                txtDireccion = new JTextField(15);
                txtTelefono = new JTextField(15);
                txtCelular = new JTextField(15);
                txtEmail = new JTextField(15);
                comboTipo = new JComboBox<>(new String[]{"VIP", "NOR", "EMP"});
                txtStatus = new JTextField("ACT", 3);
                txtStatus.setEditable(false);
                btnGuardar = new JButton("Guardar");

                // Configuración del layout
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.WHITE);
                panel.add(new JLabel("Cliente ID:"));
                panel.add(txtClienteID);
                panel.add(new JLabel("Identificación:"));
                panel.add(txtIdentificacion);
                panel.add(new JLabel("Primer Nombre:"));
                panel.add(txtNombre1);
                panel.add(new JLabel("Segundo Nombre:"));
                panel.add(txtNombre2);
                panel.add(new JLabel("Primer Apellido:"));
                panel.add(txtApellido1);
                panel.add(new JLabel("Segundo Apellido:"));
                panel.add(txtApellido2);
                panel.add(new JLabel("Dirección:"));
                panel.add(txtDireccion);
                panel.add(new JLabel("Teléfono:"));
                panel.add(txtTelefono);
                panel.add(new JLabel("Celular:"));
                panel.add(txtCelular);
                panel.add(new JLabel("Email:"));
                panel.add(txtEmail);
                panel.add(new JLabel("Tipo:"));
                panel.add(comboTipo);
                panel.add(new JLabel("Estado:"));
                panel.add(txtStatus);
                panel.add(btnGuardar);

                add(panel);

                // Establecer el Cliente ID auto incrementado
                setClienteID();

                // Añadir listener al botón
                btnGuardar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guardarCliente();
                    }
                });

                setVisible(true);
            }

            private void setClienteID() {
                String query = "SELECT MAX(CLICODIGO) FROM CLIENTES";
                try (PreparedStatement stmt = connection.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getString(1) != null) {
                        String maxClienteID = rs.getString(1);
                        int num = Integer.parseInt(maxClienteID.substring(3)) + 1;
                        txtClienteID.setText(String.format("CLI%04d", num));
                    } else {
                        txtClienteID.setText("CLI0001");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    txtClienteID.setText("CLI0001");
                }
            }

            private void guardarCliente() {
                String clienteID = txtClienteID.getText().trim();
                String identificacion = txtIdentificacion.getText().trim();
                String nombre1 = txtNombre1.getText().trim();
                String nombre2 = txtNombre2.getText().trim();
                String apellido1 = txtApellido1.getText().trim();
                String apellido2 = txtApellido2.getText().trim();
                String direccion = txtDireccion.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String celular = txtCelular.getText().trim();
                String email = txtEmail.getText().trim();
                String tipo = comboTipo.getSelectedItem().toString();
                String status = txtStatus.getText().trim();

                if (identificacion.isEmpty() || nombre1.isEmpty() || apellido1.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || celular.isEmpty() || tipo.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.");
                    return;
                }

                String query = "INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS) VALUES (?, ?, ?, ?, ?, CONCAT(?, ' ', IFNULL(?, '')), ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, clienteID);
                    stmt.setString(2, nombre1);
                    stmt.setString(3, nombre2);
                    stmt.setString(4, apellido1);
                    stmt.setString(5, apellido2);
                    stmt.setString(6, nombre1);
                    stmt.setString(7, nombre2.isEmpty() ? null : nombre2);
                    stmt.setString(8, identificacion);
                    stmt.setString(9, direccion);
                    stmt.setString(10, telefono);
                    stmt.setString(11, celular);
                    stmt.setString(12, email.isEmpty() ? null : email);
                    stmt.setString(13, tipo);
                    stmt.setString(14, status);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
                    dispose();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
                }
            }
        }

        // Clase interna para mostrar todas las facturas generadas
        public class VerFacturasForm extends JFrame {
            private JTable facturasTable;
            private DefaultTableModel facturasTableModel;
            private Connection connection;

            public VerFacturasForm(Connection connection) {
                this.connection = connection;

                setTitle("Facturas Generadas");
                setSize(900, 600);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setLayout(new BorderLayout());

                String[] columnNames = {"Numero", "Cliente", "Fecha", "Subtotal", "Descuento", "IVA", "ICE", "Forma Pago", "Status"};
                facturasTableModel = new DefaultTableModel(columnNames, 0);
                facturasTable = new JTable(facturasTableModel);
                JScrollPane scrollPane = new JScrollPane(facturasTable);
                add(scrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                JButton btnVerFactura = new JButton("Ver Factura");
                JButton btnEliminarFactura = new JButton("Eliminar Factura");

                btnVerFactura.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        verFactura();
                    }
                });

                btnEliminarFactura.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        eliminarFactura();
                    }
                });

                buttonPanel.add(btnVerFactura);
                buttonPanel.add(btnEliminarFactura);

                add(buttonPanel, BorderLayout.SOUTH);

                cargarFacturas();

                setVisible(true);
            }

            private void cargarFacturas() {
                facturasTableModel.setRowCount(0); // Limpiar tabla antes de cargar datos

                try {
                    String query = "SELECT FACNUMERO, CLIIDENTIFICACION, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACICE, FACFORMAPAGO, FACSTATUS FROM FACTURAS";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        facturasTableModel.addRow(new Object[]{
                                rs.getString("FACNUMERO"),
                                rs.getString("CLIIDENTIFICACION"),
                                rs.getDate("FACFECHA"),
                                rs.getDouble("FACSUBTOTAL"),
                                rs.getDouble("FACDESCUENTO"),
                                rs.getDouble("FACIVA"),
                                rs.getDouble("FACICE"),
                                rs.getString("FACFORMAPAGO"),
                                rs.getString("FACSTATUS")
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void verFactura() {
                int selectedRow = facturasTable.getSelectedRow();
                if (selectedRow != -1) {
                    String facturaNo = (String) facturasTableModel.getValueAt(selectedRow, 0);
                    String cliente = (String) facturasTableModel.getValueAt(selectedRow, 1);
                    String fecha = facturasTableModel.getValueAt(selectedRow, 2).toString();
                    String suma = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 3));
                    String descuento = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 4));
                    String iva = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 5));
                    String ice = String.format("%.2f", facturasTableModel.getValueAt(selectedRow, 6));
                    String total = String.format("%.2f", (Double) facturasTableModel.getValueAt(selectedRow, 3) - (Double) facturasTableModel.getValueAt(selectedRow, 4) + (Double) facturasTableModel.getValueAt(selectedRow, 5) + (Double) facturasTableModel.getValueAt(selectedRow, 6));

                    String[] columnNames = {"Producto", "Descripcion", "Uni Med", "Cantidad", "Val Uni", "IVA %", "Subtotal"};
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

                    try {
                        String query = "SELECT P.PROCODIGO, P.PRODESCRIPCION, P.PROUNIDADMEDIDA, X.PXFCANTIDAD, X.PXFVALOR, 15 AS IVA, X.PXFSUBTOTAL " +
                                "FROM PXF X INNER JOIN PRODUCTOS P ON X.PROCODIGO = P.PROCODIGO WHERE X.FACNUMERO = ?";
                        PreparedStatement ps = connection.prepareStatement(query);
                        ps.setString(1, facturaNo);
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            tableModel.addRow(new Object[]{
                                    rs.getString("PROCODIGO"),
                                    rs.getString("PRODESCRIPCION"),
                                    rs.getString("PROUNIDADMEDIDA"),
                                    rs.getDouble("PXFCANTIDAD"),
                                    rs.getDouble("PXFVALOR"),
                                    rs.getInt("IVA"),
                                    rs.getDouble("PXFSUBTOTAL")
                            });
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    new FacturaGeneradaForm(facturaNo, cliente, fecha, suma, "0.00", ice, iva, total, tableModel);
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione una factura para ver.");
                }
            }

            private void eliminarFactura() {
                int selectedRow = facturasTable.getSelectedRow();
                if (selectedRow != -1) {
                    String facturaNo = (String) facturasTableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea anular la factura?", "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String updateQuery = "UPDATE FACTURAS SET FACSTATUS = 'ANU' WHERE FACNUMERO = ?";
                            PreparedStatement ps = connection.prepareStatement(updateQuery);
                            ps.setString(1, facturaNo);
                            ps.executeUpdate();
                            facturasTableModel.setValueAt("ANU", selectedRow, 8);
                            JOptionPane.showMessageDialog(this, "Factura anulada exitosamente.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error al anular la factura.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione una factura para anular.");
                }
            }
        }
    }
}
