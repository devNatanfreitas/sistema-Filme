import db.DB;
import entities.*;
import jdbc.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

public class MainFrame extends JFrame {

    // DAOs
    private final FilmeDaoJDBC filmeDao;
    private final AtorDaoJDBC atorDao;
    private final DiretorDaoJDBC diretorDao;
    private final GeneroDaoJDBC generoDao;
    private final AvaliacaoDaoJDBC avaliacaoDao;

    // --- Componentes ---
    private JTextField filmeTituloField, filmeDuracaoField, filmeAnoField;
    private JTable filmeTable;
    private DefaultTableModel filmeTableModel;
    private JButton filmeEditButton, filmeDeleteButton;

    private JComboBox<Filme> filmeSelectorAtor;
    private JTextField atorNomeField;
    private JTable atorTable;
    private DefaultTableModel atorTableModel;
    private JButton atorEditButton, atorDeleteButton;

    private JComboBox<Filme> filmeSelectorDiretor;
    private JTextField diretorNomeField;
    private JTable diretorTable;
    private DefaultTableModel diretorTableModel;
    private JButton diretorEditButton, diretorDeleteButton;

    private JComboBox<Filme> filmeSelectorGenero;
    private JTextField generoNomeField;
    private JTable generoTable;
    private DefaultTableModel generoTableModel;
    private JButton generoEditButton, generoDeleteButton;

    private JComboBox<Filme> filmeSelectorAvaliacao;
    private JTextField avaliacaoNomeAvaliadorField, avaliacaoNotaField;
    private JTextArea avaliacaoComentarioArea;
    private JTable avaliacaoTable;
    private DefaultTableModel avaliacaoTableModel;
    private JButton avaliacaoEditButton, avaliacaoDeleteButton;


    public MainFrame() {
        Connection conn = DB.getConnection();
        filmeDao = new FilmeDaoJDBC(conn);
        atorDao = new AtorDaoJDBC(conn);
        diretorDao = new DiretorDaoJDBC(conn);
        generoDao = new GeneroDaoJDBC(conn);
        avaliacaoDao = new AvaliacaoDaoJDBC(conn);

        setTitle("Sistema de Gerenciamento de Filmes");
        setSize(950, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Filmes", createFilmePanel());
        tabbedPane.addTab("Atores", createAtorPanel());
        tabbedPane.addTab("Diretores", createDiretorPanel());
        tabbedPane.addTab("Gêneros", createGeneroPanel());
        tabbedPane.addTab("Avaliações", createAvaliacaoPanel());
        add(tabbedPane, BorderLayout.CENTER);

        loadAllData();
    }

    private void loadAllData() {
        List<Filme> filmes = filmeDao.findAll();
        loadFilmsIntoTable(filmes);
        loadFilmsIntoComboBoxes(filmes);
        loadAtoresIntoTable();
        loadDiretoresIntoTable();
        loadGenerosIntoTable();
        loadAvaliationsIntoTable();
    }

    // ==========================================================
    // CRUD: FILMES
    // ==========================================================
    private JPanel createFilmePanel() {
        JPanel mainPanel = createStandardPanel();
        filmeTableModel = createTableModel(new String[]{"ID", "Título", "Duração (min)", "Ano"});
        filmeTable = new JTable(filmeTableModel);
        setupTable(filmeTable);
        hideTableColumn(filmeTable, 0);
        mainPanel.add(new JScrollPane(filmeTable), BorderLayout.CENTER);

        JPanel formPanel = createFormPanel("Adicionar Filme");
        filmeTituloField = new JTextField();
        filmeDuracaoField = new JTextField();
        filmeAnoField = new JTextField();
        formPanel.add(new JLabel("Título:")); formPanel.add(filmeTituloField);
        formPanel.add(new JLabel("Duração (min):")); formPanel.add(filmeDuracaoField);
        formPanel.add(new JLabel("Ano:")); formPanel.add(filmeAnoField);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JButton addButton = createButton("Adicionar", "add.png", e -> addFilme());
        filmeEditButton = createButton("Editar", "edit.png", e -> editFilme());
        filmeDeleteButton = createButton("Excluir", "delete.png", e -> deleteFilme());
        mainPanel.add(createButtonPanel(addButton, filmeEditButton, filmeDeleteButton), BorderLayout.SOUTH);

        setupButtonEnabling(filmeTable, filmeEditButton, filmeDeleteButton);
        return mainPanel;
    }

    private void loadFilmsIntoTable(List<Filme> filmes) {
        filmeTableModel.setRowCount(0);
        for (Filme f : filmes) {
            filmeTableModel.addRow(new Object[]{f.getId(), f.getTitulo(), f.getDuracao(), f.getAnoLancamento()});
        }
    }

    private void addFilme() {
        try {
            String titulo = filmeTituloField.getText().trim();
            if (titulo.isEmpty()) { showError("O título não pode estar vazio."); return; }
            int duracao = Integer.parseInt(filmeDuracaoField.getText());
            int ano = Integer.parseInt(filmeAnoField.getText());

            filmeDao.insert(new Filme(titulo, duracao, ano));
            showMessage("Filme adicionado com sucesso!");
            clearFields(filmeTituloField, filmeDuracaoField, filmeAnoField);
            loadAllData();
        } catch (NumberFormatException ex) {
            showError("Duração e Ano devem ser números válidos.");
        } catch (Exception ex) {
            showError("Erro ao adicionar filme: " + ex.getMessage());
        }
    }

    private void editFilme() {
        getSelectedEntity(filmeTable, filmeDao::findById, filme -> {
            try {
                JTextField titulo = new JTextField(filme.getTitulo());
                JTextField duracao = new JTextField(String.valueOf(filme.getDuracao()));
                JTextField ano = new JTextField(String.valueOf(filme.getAnoLancamento()));
                final JComponent[] inputs = {new JLabel("Título:"), titulo, new JLabel("Duração:"), duracao, new JLabel("Ano:"), ano};

                if (JOptionPane.showConfirmDialog(this, inputs, "Editar Filme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                    filme.setTitulo(titulo.getText().trim());
                    filme.setDuracao(Integer.parseInt(duracao.getText()));
                    filme.setAnoLancamento(Integer.parseInt(ano.getText()));
                    if (filme.getTitulo().isEmpty()) { showError("O título não pode estar vazio."); return; }
                    filmeDao.update(filme);
                    showMessage("Filme atualizado com sucesso!");
                    loadAllData();
                }
            } catch (NumberFormatException ex) { showError("Duração e Ano devem ser números válidos."); }
        });
    }

    private void deleteFilme() {
        getSelectedEntity(filmeTable, filmeDao::findById, filme -> {
            if (JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o filme '" + filme.getTitulo() + "'?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                filmeDao.deleteById(filme.getId());
                showMessage("Filme excluído com sucesso!");
                loadAllData();
            }
        });
    }

    // ==========================================================
    // CRUD: ATORES
    // ==========================================================
    private JPanel createAtorPanel() {
        JPanel mainPanel = createStandardPanel();
        atorTableModel = createTableModel(new String[]{"ID", "Nome do Ator", "Filme Associado"});
        atorTable = new JTable(atorTableModel);
        setupTable(atorTable);
        hideTableColumn(atorTable, 0);
        mainPanel.add(new JScrollPane(atorTable), BorderLayout.CENTER);

        JPanel formPanel = createFormPanel("Adicionar Ator");
        filmeSelectorAtor = new JComboBox<>();
        atorNomeField = new JTextField();
        formPanel.add(new JLabel("Selecione o Filme:")); formPanel.add(filmeSelectorAtor);
        formPanel.add(new JLabel("Nome do Ator:")); formPanel.add(atorNomeField);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JButton addButton = createButton("Adicionar", "add.png", e -> addAtor());
        atorEditButton = createButton("Editar", "edit.png", e -> editAtor());
        atorDeleteButton = createButton("Excluir", "delete.png", e -> deleteAtor());
        mainPanel.add(createButtonPanel(addButton, atorEditButton, atorDeleteButton), BorderLayout.SOUTH);

        setupButtonEnabling(atorTable, atorEditButton, atorDeleteButton);
        return mainPanel;
    }

    private void loadAtoresIntoTable() {
        try {
            atorTableModel.setRowCount(0);
            for (Ator a : atorDao.findAll()) {
                Filme f = filmeDao.findById(a.getIdFilme());
                atorTableModel.addRow(new Object[]{a.getId(), a.getNome(), f != null ? f.toString() : "N/A"});
            }
        } catch (Exception ex) { showError("Erro ao carregar atores: " + ex.getMessage()); }
    }

    private void addAtor() {
        try {
            Filme f = (Filme) filmeSelectorAtor.getSelectedItem();
            String nome = atorNomeField.getText().trim();
            if (f == null || nome.isEmpty()) { showError("Selecione um filme e insira o nome do ator."); return; }

            atorDao.insert(new Ator(nome, f.getId()));
            showMessage("Ator adicionado com sucesso!");
            atorNomeField.setText("");
            loadAllData();
        } catch (Exception ex) { showError("Erro ao adicionar ator: " + ex.getMessage()); }
    }

    private void editAtor() {
        getSelectedEntity(atorTable, atorDao::findById, ator -> {
            JTextField nome = new JTextField(ator.getNome());
            JComboBox<Filme> filmeSelector = createFilmeComboBoxForEdit(ator.getIdFilme());
            final JComponent[] inputs = {new JLabel("Nome do Ator:"), nome, new JLabel("Filme:"), filmeSelector};

            if (JOptionPane.showConfirmDialog(this, inputs, "Editar Ator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                Filme f = (Filme) filmeSelector.getSelectedItem();
                String newNome = nome.getText().trim();
                if (f == null || newNome.isEmpty()){ showError("Nome e filme são obrigatórios."); return; }

                ator.setNome(newNome);
                ator.setIdFilme(f.getId());
                atorDao.update(ator);
                showMessage("Ator atualizado com sucesso!");
                loadAllData();
            }
        });
    }

    private void deleteAtor() {
        getSelectedEntity(atorTable, atorDao::findById, ator -> {
            if (JOptionPane.showConfirmDialog(this, "Excluir o ator '" + ator.getNome() + "'?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                atorDao.deleteById(ator.getId());
                showMessage("Ator excluído com sucesso!");
                loadAllData();
            }
        });
    }

    // ==========================================================
    // CRUD: DIRETORES
    // ==========================================================
    private JPanel createDiretorPanel() {
        JPanel mainPanel = createStandardPanel();
        diretorTableModel = createTableModel(new String[]{"ID", "Nome do Diretor", "Filme Associado"});
        diretorTable = new JTable(diretorTableModel);
        setupTable(diretorTable);
        hideTableColumn(diretorTable, 0);
        mainPanel.add(new JScrollPane(diretorTable), BorderLayout.CENTER);

        JPanel formPanel = createFormPanel("Adicionar Diretor");
        filmeSelectorDiretor = new JComboBox<>();
        diretorNomeField = new JTextField();
        formPanel.add(new JLabel("Selecione o Filme:")); formPanel.add(filmeSelectorDiretor);
        formPanel.add(new JLabel("Nome do Diretor:")); formPanel.add(diretorNomeField);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JButton addButton = createButton("Adicionar", "add.png", e -> addDiretor());
        diretorEditButton = createButton("Editar", "edit.png", e -> editDiretor());
        diretorDeleteButton = createButton("Excluir", "delete.png", e -> deleteDiretor());
        mainPanel.add(createButtonPanel(addButton, diretorEditButton, diretorDeleteButton), BorderLayout.SOUTH);

        setupButtonEnabling(diretorTable, diretorEditButton, diretorDeleteButton);
        return mainPanel;
    }

    private void loadDiretoresIntoTable() {
        try {
            diretorTableModel.setRowCount(0);
            for (Diretor d : diretorDao.findAll()) {
                Filme f = filmeDao.findById(d.getIdFilme());
                diretorTableModel.addRow(new Object[]{d.getId(), d.getNome(), f != null ? f.toString() : "N/A"});
            }
        } catch (Exception ex) { showError("Erro ao carregar diretores: " + ex.getMessage()); }
    }

    private void addDiretor() {
        try {
            Filme f = (Filme) filmeSelectorDiretor.getSelectedItem();
            String nome = diretorNomeField.getText().trim();
            if (f == null || nome.isEmpty()) { showError("Selecione um filme e insira o nome do diretor."); return; }

            diretorDao.insert(new Diretor(nome, f.getId()));
            showMessage("Diretor adicionado com sucesso!");
            diretorNomeField.setText("");
            loadAllData();
        } catch (Exception ex) { showError("Erro ao adicionar diretor: " + ex.getMessage()); }
    }

    private void editDiretor() {
        getSelectedEntity(diretorTable, diretorDao::findById, diretor -> {
            JTextField nome = new JTextField(diretor.getNome());
            JComboBox<Filme> filmeSelector = createFilmeComboBoxForEdit(diretor.getIdFilme());
            final JComponent[] inputs = {new JLabel("Nome do Diretor:"), nome, new JLabel("Filme:"), filmeSelector};

            if (JOptionPane.showConfirmDialog(this, inputs, "Editar Diretor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                Filme f = (Filme) filmeSelector.getSelectedItem();
                String newNome = nome.getText().trim();
                if(f == null || newNome.isEmpty()){ showError("Nome e filme são obrigatórios."); return; }

                diretor.setNome(newNome);
                diretor.setIdFilme(f.getId());
                diretorDao.update(diretor);
                showMessage("Diretor atualizado com sucesso!");
                loadAllData();
            }
        });
    }

    private void deleteDiretor() {
        getSelectedEntity(diretorTable, diretorDao::findById, diretor -> {
            if (JOptionPane.showConfirmDialog(this, "Excluir o diretor '" + diretor.getNome() + "'?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                diretorDao.deleteById(diretor.getId());
                showMessage("Diretor excluído com sucesso!");
                loadAllData();
            }
        });
    }

    // ==========================================================
    // CRUD: GÊNEROS
    // ==========================================================
    private JPanel createGeneroPanel() {
        JPanel mainPanel = createStandardPanel();
        generoTableModel = createTableModel(new String[]{"ID", "Nome do Gênero", "Filme Associado"});
        generoTable = new JTable(generoTableModel);
        setupTable(generoTable);
        hideTableColumn(generoTable, 0);
        mainPanel.add(new JScrollPane(generoTable), BorderLayout.CENTER);

        JPanel formPanel = createFormPanel("Adicionar Gênero");
        filmeSelectorGenero = new JComboBox<>();
        generoNomeField = new JTextField();
        formPanel.add(new JLabel("Selecione o Filme:")); formPanel.add(filmeSelectorGenero);
        formPanel.add(new JLabel("Nome do Gênero:")); formPanel.add(generoNomeField);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JButton addButton = createButton("Adicionar", "add.png", e -> addGenero());
        generoEditButton = createButton("Editar", "edit.png", e -> editGenero());
        generoDeleteButton = createButton("Excluir", "delete.png", e -> deleteGenero());
        mainPanel.add(createButtonPanel(addButton, generoEditButton, generoDeleteButton), BorderLayout.SOUTH);

        setupButtonEnabling(generoTable, generoEditButton, generoDeleteButton);
        return mainPanel;
    }

    private void loadGenerosIntoTable() {
        try {
            generoTableModel.setRowCount(0);
            for (Genero g : generoDao.findAll()) {
                Filme f = filmeDao.findById(g.getIdFilme());
                generoTableModel.addRow(new Object[]{g.getId(), g.getNome(), f != null ? f.toString() : "N/A"});
            }
        } catch (Exception ex) { showError("Erro ao carregar gêneros: " + ex.getMessage()); }
    }

    private void addGenero() {
        try {
            Filme f = (Filme) filmeSelectorGenero.getSelectedItem();
            String nome = generoNomeField.getText().trim();
            if (f == null || nome.isEmpty()) { showError("Selecione um filme e insira o nome do gênero."); return; }

            generoDao.insert(new Genero(nome, f.getId()));
            showMessage("Gênero adicionado com sucesso!");
            generoNomeField.setText("");
            loadAllData();
        } catch (Exception ex) { showError("Erro ao adicionar gênero: " + ex.getMessage()); }
    }

    private void editGenero() {
        getSelectedEntity(generoTable, generoDao::findById, genero -> {
            JTextField nome = new JTextField(genero.getNome());
            JComboBox<Filme> filmeSelector = createFilmeComboBoxForEdit(genero.getIdFilme());
            final JComponent[] inputs = {new JLabel("Nome do Gênero:"), nome, new JLabel("Filme:"), filmeSelector};

            if (JOptionPane.showConfirmDialog(this, inputs, "Editar Gênero", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                Filme f = (Filme) filmeSelector.getSelectedItem();
                String newNome = nome.getText().trim();
                if (f==null || newNome.isEmpty()){ showError("Nome e filme são obrigatórios."); return;}

                genero.setNome(newNome);
                genero.setIdFilme(f.getId());
                generoDao.update(genero);
                showMessage("Gênero atualizado com sucesso!");
                loadAllData();
            }
        });
    }

    private void deleteGenero() {
        getSelectedEntity(generoTable, generoDao::findById, genero -> {
            if (JOptionPane.showConfirmDialog(this, "Excluir o gênero '" + genero.getNome() + "'?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                generoDao.deleteById(genero.getId());
                showMessage("Gênero excluído com sucesso!");
                loadAllData();
            }
        });
    }

    // ==========================================================
    // CRUD: AVALIAÇÕES
    // ==========================================================
    private JPanel createAvaliacaoPanel() {
        JPanel mainPanel = createStandardPanel();
        avaliacaoTableModel = createTableModel(new String[]{"ID", "Filme", "Avaliador", "Nota", "Comentário"});
        avaliacaoTable = new JTable(avaliacaoTableModel);
        setupTable(avaliacaoTable);
        hideTableColumn(avaliacaoTable, 0);
        avaliacaoTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Avaliador
        avaliacaoTable.getColumnModel().getColumn(3).setPreferredWidth(40); // Nota
        avaliacaoTable.getColumnModel().getColumn(4).setPreferredWidth(350); // Comentário
        mainPanel.add(new JScrollPane(avaliacaoTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Nova Avaliação"));

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        filmeSelectorAvaliacao = new JComboBox<>();
        avaliacaoNomeAvaliadorField = new JTextField();
        avaliacaoNotaField = new JTextField();
        fieldsPanel.add(new JLabel("Selecione o Filme:")); fieldsPanel.add(filmeSelectorAvaliacao);
        fieldsPanel.add(new JLabel("Nome do Avaliador:")); fieldsPanel.add(avaliacaoNomeAvaliadorField);
        fieldsPanel.add(new JLabel("Nota (0 a 10):")); fieldsPanel.add(avaliacaoNotaField);
        formPanel.add(fieldsPanel, BorderLayout.NORTH);

        avaliacaoComentarioArea = new JTextArea(4, 30);
        JScrollPane commentPane = new JScrollPane(avaliacaoComentarioArea);
        commentPane.setBorder(BorderFactory.createTitledBorder("Comentário"));
        formPanel.add(commentPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JButton addButton = createButton("Adicionar", "add.png", e -> addAvaliacao());
        avaliacaoEditButton = createButton("Editar", "edit.png", e -> editAvaliacao());
        avaliacaoDeleteButton = createButton("Excluir", "delete.png", e -> deleteAvaliacao());
        mainPanel.add(createButtonPanel(addButton, avaliacaoEditButton, avaliacaoDeleteButton), BorderLayout.SOUTH);

        setupButtonEnabling(avaliacaoTable, avaliacaoEditButton, avaliacaoDeleteButton);
        return mainPanel;
    }

    private void loadAvaliationsIntoTable() {
        try {
            avaliacaoTableModel.setRowCount(0);
            for (Avaliacao a : avaliacaoDao.findAll()) {
                Filme f = filmeDao.findById(a.getIdFilme());
                avaliacaoTableModel.addRow(new Object[]{a.getId(), f != null ? f.toString() : "N/A", a.getNomeAvaliador(), a.getNota(), a.getComentario()});
            }
        } catch (Exception ex) { showError("Erro ao carregar avaliações: " + ex.getMessage()); }
    }

    private void addAvaliacao() {
        try {
            Filme f = (Filme) filmeSelectorAvaliacao.getSelectedItem();
            String nomeAvaliador = avaliacaoNomeAvaliadorField.getText().trim();
            int nota = Integer.parseInt(avaliacaoNotaField.getText());
            if (f == null || nomeAvaliador.isEmpty() || nota < 0 || nota > 10) { 
                showError("Selecione um filme, insira o nome do avaliador e uma nota de 0 a 10."); 
                return; 
            }

            avaliacaoDao.insert(new Avaliacao(nomeAvaliador, f.getId(), nota, avaliacaoComentarioArea.getText()));
            showMessage("Avaliação adicionada com sucesso!");
            avaliacaoNomeAvaliadorField.setText("");
            avaliacaoNotaField.setText("");
            avaliacaoComentarioArea.setText("");
            loadAllData();
        } catch (NumberFormatException ex) {
            showError("A nota deve ser um número válido.");
        } catch (Exception ex) {
            showError("Erro ao adicionar avaliação: " + ex.getMessage());
        }
    }

    private void editAvaliacao() {
        getSelectedEntity(avaliacaoTable, avaliacaoDao::findById, avaliacao -> {
            try {
                JComboBox<Filme> filmeSelector = createFilmeComboBoxForEdit(avaliacao.getIdFilme());
                JTextField nomeAvaliador = new JTextField(avaliacao.getNomeAvaliador());
                JTextField nota = new JTextField(String.valueOf(avaliacao.getNota()));
                JTextArea comentario = new JTextArea(avaliacao.getComentario(), 5, 30);

                final JComponent[] inputs = {new JLabel("Filme:"), filmeSelector, new JLabel("Nome do Avaliador:"), nomeAvaliador, new JLabel("Nota (0 a 10):"), nota, new JLabel("Comentário:"), new JScrollPane(comentario)};

                if (JOptionPane.showConfirmDialog(this, inputs, "Editar Avaliação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                    Filme f = (Filme) filmeSelector.getSelectedItem();
                    String nomeAvaliadorText = nomeAvaliador.getText().trim();
                    int newNota = Integer.parseInt(nota.getText());

                    if (f == null || nomeAvaliadorText.isEmpty() || newNota < 0 || newNota > 10) { 
                        showError("Dados inválidos. Selecione um filme, insira o nome do avaliador e uma nota de 0 a 10."); 
                        return; 
                    }

                    avaliacao.setIdFilme(f.getId());
                    avaliacao.setNomeAvaliador(nomeAvaliadorText);
                    avaliacao.setNota(newNota);
                    avaliacao.setComentario(comentario.getText());
                    avaliacaoDao.update(avaliacao);

                    showMessage("Avaliação atualizada com sucesso!");
                    loadAllData();
                }
            } catch (NumberFormatException ex) {
                showError("A nota deve ser um número válido.");
            }
        });
    }

    private void deleteAvaliacao() {
        getSelectedEntity(avaliacaoTable, avaliacaoDao::findById, avaliacao -> {
            if (JOptionPane.showConfirmDialog(this, "Excluir esta avaliação?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                avaliacaoDao.deleteById(avaliacao.getId());
                showMessage("Avaliação excluída com sucesso!");
                loadAllData();
            }
        });
    }

    // ==========================================================
    // MÉTODOS DE APOIO E UTILITÁRIOS
    // ==========================================================
    private void loadFilmsIntoComboBoxes(List<Filme> filmes) {
        Vector<Filme> filmeVector = new Vector<>(filmes);
        if (filmeSelectorAtor != null) filmeSelectorAtor.setModel(new DefaultComboBoxModel<>(filmeVector));
        if (filmeSelectorDiretor != null) filmeSelectorDiretor.setModel(new DefaultComboBoxModel<>(filmeVector));
        if (filmeSelectorGenero != null) filmeSelectorGenero.setModel(new DefaultComboBoxModel<>(filmeVector));
        if (filmeSelectorAvaliacao != null) filmeSelectorAvaliacao.setModel(new DefaultComboBoxModel<>(filmeVector));
    }

    private void showMessage(String message) { JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String message) { JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE); }
    private void clearFields(JTextField... fields) { for (JTextField field : fields) field.setText(""); }

    private JPanel createStandardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
    }
    private void setupTable(JTable table) {
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }
    private JPanel createFormPanel(String title) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(title));
        return formPanel;
    }
    private JButton createButton(String text, String iconName, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text, new ImageIcon("icons/" + iconName));
        button.addActionListener(listener);
        return button;
    }
    private JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        for(JButton b : buttons) panel.add(b);
        return panel;
    }
    private void setupButtonEnabling(JTable table, JButton... buttons) {
        for(JButton b : buttons) b.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean isSelected = table.getSelectedRow() != -1;
            for (JButton btn : buttons) btn.setEnabled(isSelected);
        });
    }
    private <T> void getSelectedEntity(JTable table, java.util.function.Function<Integer, T> finder, java.util.function.Consumer<T> callback) {
        int row = table.getSelectedRow();
        if (row == -1) { showError("Por favor, selecione um item na tabela."); return; }
        try {
            Integer id = (Integer) table.getValueAt(row, 0); // O ID ainda está na coluna 0 do modelo
            T entity = finder.apply(id);
            if (entity != null) callback.accept(entity);
        } catch (Exception ex) {
            showError("Erro ao buscar item selecionado: " + ex.getMessage());
        }
    }
    private JComboBox<Filme> createFilmeComboBoxForEdit(int selectedFilmeId) {
        JComboBox<Filme> filmeSelector = new JComboBox<>(new Vector<>(filmeDao.findAll()));
        for (int i = 0; i < filmeSelector.getItemCount(); i++) {
            if (filmeSelector.getItemAt(i).getId() == selectedFilmeId) {
                filmeSelector.setSelectedIndex(i);
                break;
            }
        }
        return filmeSelector;
    }
    private void hideTableColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}