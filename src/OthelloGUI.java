import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;

public class OthelloGUI extends JFrame {
    private final OthelloModel model;
    private final BoardPanel boardPanel;

    public OthelloGUI() {
        super("Othello");
        model = new OthelloModel();
        boardPanel = new BoardPanel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(boardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class BoardPanel extends JPanel {
        private static final int PREF = 600;

        public BoardPanel() {
            setPreferredSize(new Dimension(PREF, PREF));
            setBackground(Color.GREEN);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int cellSize = getWidth() / 8;
                    int row = e.getY() / cellSize;
                    int col = e.getX() / cellSize;
                    // Human move
                    if (model.getCurrentPlayer() == OthelloModel.BLACK
                            && model.makeMove(row, col)) {
                        repaint();
                        model.switchPlayer();
                        // Computer move
                        if (model.hasValidMove(OthelloModel.WHITE)) {
                            Point cm = model.getComputerMove();
                            model.makeMove(cm.x, cm.y);
                            repaint();
                        }
                        model.switchPlayer();
                    }
                    // Check end
                    if (model.isGameOver()) {
                        int b = model.getScore(OthelloModel.BLACK);
                        int w = model.getScore(OthelloModel.WHITE);
                        String msg = String.format(
                                "Game Over!\nYou (Black): %d\nComputer (White): %d\n%s",
                                b, w,
                                b > w ? "You win!" : (w > b ? "Computer wins." : "Draw.")
                        );
                        JOptionPane.showMessageDialog(BoardPanel.this, msg);
                        System.exit(0);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellSize = getWidth() / 8;
            // Grid & pieces
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    g.setColor(Color.BLACK);
                    g.drawRect(c*cellSize, r*cellSize, cellSize, cellSize);
                    int cell = model.getCell(r, c);
                    if (cell == OthelloModel.BLACK) {
                        g.fillOval(c*cellSize+4, r*cellSize+4,
                                cellSize-8, cellSize-8);
                    } else if (cell == OthelloModel.WHITE) {
                        g.setColor(Color.WHITE);
                        g.fillOval(c*cellSize+4, r*cellSize+4,
                                cellSize-8, cellSize-8);
                        g.setColor(Color.BLACK);
                        g.drawOval(c*cellSize+4, r*cellSize+4,
                                cellSize-8, cellSize-8);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OthelloGUI::new);
    }
}