package View;
import Model.*;
import Service.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindows extends JFrame {
    private ArrayList<Device> deviceList;
    private DefaultTableModel tableModel;
    private JTable table;

    // Method untuk memperbarui tampilan tabel
    private void refreshTable(ArrayList<Device> list) {
        tableModel.setRowCount(0); // Hapus semua baris
        int no = 1;
        for (Device d : list) {
            Object[] row = {no++, d.getKategori(), d.getNama(), d.getHarga()};
            tableModel.addRow(row);
        }
    }

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

        // Tombol search
        btnSearch.addActionListener(e -> {
           try {
               double target = Double.parseDouble(searchField.getText());
               ArrayList<Device> hasil = Search.search(deviceList, target);
               refreshTable(hasil);
           }
           catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Masukkan angka harga yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
        
        });

        // Tombol sorting
        btnSort.addActionListener(e -> {
            String[] pilihan = {"Harga Terkecil", "Harga Terbesar"};

            String hasil = (String) JOptionPane.showInputDialog(
                    this,
                    "Pilih metode sorting",
                    "Sort",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    pilihan,
                    pilihan[0]);

            if (hasil != null) {
                boolean ascending = hasil.equals("Harga Terkecil");
                refreshTable(Sort.sort(deviceList,ascending));
            }
        });

        //Tombol tambah
        btnTambah.addActionListener(e -> {
            String[] kategori = {"Hp", "Laptop", "Tablet"};

            JComboBox<String> cbKategori = new JComboBox<>(kategori);
            JTextField txtNama = new JTextField();
            JTextField txtHarga = new JTextField();

            Object[] message = {"Kategori:", cbKategori, "Nama Device:", txtNama, "Harga:", txtHarga};

            int option = JOptionPane.showConfirmDialog(this, message, "Tambah Device", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            try {
                String nama = txtNama.getText();
                double harga = Double.parseDouble(txtHarga.getText());
                String pilihan = (String) cbKategori.getSelectedItem();

                CRUD.tambahDevice(deviceList, pilihan, nama, harga);

                refreshTable(deviceList);

                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Tombol hapus
        btnHapus.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow == -1){
                JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION){
                Device device = deviceList.get(selectedRow);
                CRUD.hapusDevice(deviceList, device);

                refreshTable(deviceList);

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            }
        });

        // Tombol edit
        btnEdit.addActionListener(e ->{
            int selectedRow = table.getSelectedRow();

            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin mengedit data ini?", "konfirmasi edit", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION){
                Device device = deviceList.get(selectedRow);

                JTextField txtNama = new JTextField(device.getNama());
                JTextField txtHarga = new JTextField(
                String.valueOf(device.getHarga()));

                String namaBaru = txtNama.getText();
                double hargaBaru = Double.parseDouble(txtHarga.getText());

                CRUD.editDevice(device, namaBaru, hargaBaru);

                refreshTable(deviceList);

                JOptionPane.showMessageDialog(this, "Data berhasil diedit");
            }
        });

        // Tombol Reset
        btnReset.addActionListener(e -> {
            searchField.setText("");
            refreshTable(deviceList);
        });
    }
   
}
