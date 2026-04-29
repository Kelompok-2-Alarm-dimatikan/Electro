package View;

import Model.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindows extends JFrame {
    private ArrayList<Device> deviceList;
    private DefaultTableModel tableModel;
    private JTable table;

    public MainWindows(ArrayList<Device> initialData) {
        this.deviceList = initialData;

        setTitle("Demon God Device Store");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Setup Tabel
        String[] columns = {"No", "Kategori", "Nama Device", "Harga"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Atas (Pencarian & Sorting)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search Harga");
        JButton btnSort = new JButton("Sort by Harga");
        JButton btnReset = new JButton("Reset/Refresh");

        topPanel.add(new JLabel("Cari Harga: "));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(btnSort);
        topPanel.add(btnReset);
        add(topPanel, BorderLayout.NORTH);

        // Panel Bawah (Aksi CRUD)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        bottomPanel.add(btnTambah);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnHapus);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load awal data ke tabel
        refreshTable(deviceList);

        // --- EVENT LISTENERS ---

        // Tombol Tambah
        btnTambah.addActionListener(e -> {
            tambahDevice();
        });

        // Tombol Edit
        btnEdit.addActionListener(e -> {
            editDevice();
        });

        // Tombol Hapus
        btnHapus.addActionListener(e -> {
            hapusDevice();
        });

        // Tombol Sortir
        btnSort.addActionListener(e -> {
            ArrayList<Device> sortedList = new ArrayList<>(deviceList);
            Collections.sort(sortedList, Comparator.comparingDouble(Device::getHarga));
            refreshTable(sortedList);
        });

        // Tombol Search
        btnSearch.addActionListener(e -> {
            try {
                double targetHarga = Double.parseDouble(searchField.getText());
                ArrayList<Device> searchResult = new ArrayList<>();
                for (Device d : deviceList) {
                    if (d.getHarga() == targetHarga) {
                        searchResult.add(d);
                    }
                }
                refreshTable(searchResult);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan angka harga yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tombol Reset
        btnReset.addActionListener(e -> {
            searchField.setText("");
            refreshTable(deviceList);
        });
    }

    // Method untuk memperbarui tampilan tabel
    private void refreshTable(ArrayList<Device> list) {
        tableModel.setRowCount(0); // Hapus semua baris
        int no = 1;
        for (Device d : list) {
            Object[] row = {no++, d.getKategori(), d.getNama(), d.getHarga()};
            tableModel.addRow(row);
        }
    }

    // Method Tambah Data
    private void tambahDevice() {
        String[] kategori = {"Hp", "Laptop", "Tablet"};
        JComboBox<String> cbKategori = new JComboBox<>(kategori);
        JTextField txtNama = new JTextField();
        JTextField txtHarga = new JTextField();

        Object[] message = {
            "Kategori:", cbKategori,
            "Nama Device:", txtNama,
            "Harga:", txtHarga
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Tambah Device", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nama = txtNama.getText();
                double harga = Double.parseDouble(txtHarga.getText());
                String pilihan = (String) cbKategori.getSelectedItem();

                if (pilihan.equals("Hp")) deviceList.add(new Hp(nama, harga));
                else if (pilihan.equals("Laptop")) deviceList.add(new Laptop(nama, harga));
                else if (pilihan.equals("Tablet")) deviceList.add(new Tablet(nama, harga));

                refreshTable(deviceList);
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method Edit Data
    private void editDevice() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil nama dari baris yang dipilih untuk dicari di list
        String namaLama = tableModel.getValueAt(selectedRow, 2).toString();
        Device deviceToEdit = null;
        for (Device d : deviceList) {
            if (d.getNama().equals(namaLama)) {
                deviceToEdit = d;
                break;
            }
        }

        if (deviceToEdit != null) {
            JTextField txtNama = new JTextField(deviceToEdit.getNama());
            JTextField txtHarga = new JTextField(String.valueOf(deviceToEdit.getHarga()));

            Object[] message = {
                "Nama Device Baru:", txtNama,
                "Harga Baru:", txtHarga
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Device", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    deviceToEdit.setNama(txtNama.getText());
                    deviceToEdit.setHarga(Double.parseDouble(txtHarga.getText()));
                    refreshTable(deviceList);
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Method Hapus Data
    private void hapusDevice() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String namaHapus = tableModel.getValueAt(selectedRow, 2).toString();
            deviceList.removeIf(d -> d.getNama().equals(namaHapus));
            refreshTable(deviceList);
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        }
    }
}
