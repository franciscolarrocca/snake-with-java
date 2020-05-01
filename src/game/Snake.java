
package game;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Francisco Larrocca
 */
public class Snake extends JFrame {

    private int width = 640;
    private int height = 480;

    private Point snake;
    private Point comida;

    private int widthPoint = 10, heightPoint = 10;
    private ImagenSnake imagenSnake;

    private int direccion = KeyEvent.VK_LEFT;

    //Frecuencia de actualizacion del juego cada 30ml:
    private long frecuencia = 30;

    ArrayList<Point> lista = new ArrayList<Point>();

    private boolean gameOver = false;

    public Snake() {
        this.setTitle("Snake");
        this.setSize(width, height);

        //Tomar tamaño de la ventana y ubicar al centro de la pantalla:
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);

        //Cerrar ventana:
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Configurar teclas y eventos:
        Teclas teclas = new Teclas();
        this.addKeyListener(teclas);

        comenzarJuego();

        //Agregar Contenedor:
        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        this.setVisible(true);

        //Hilo para el manejo del juego:
        Momento momento = new Momento();
        Thread thread = new Thread(momento);
        thread.start();
    }

    public void comenzarJuego() {
        comida = new Point(200, 200);
        snake = new Point(width / 2, height / 2);

        lista = new ArrayList<Point>();
        lista.add(snake);

        crearComida();
    }

    public void crearComida() {
        Random rnd = new Random();

        comida.x = rnd.nextInt(width);
        if ((comida.x % 5) > 0) {
            comida.x = comida.x - (comida.x % 5);
        }
        if (comida.x < 5) {
            comida.x = comida.x + 10;
        }
        comida.y = rnd.nextInt(height);
        if ((comida.y % 5) > 0) {
            comida.y = comida.y - (comida.y % 5);
        }
        if (comida.y < 5) {
            comida.y = comida.y + 10;
        }

    }

    public static void main(String[] args) {
        Snake snk = new Snake();
    }

    public class Teclas extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (direccion != KeyEvent.VK_DOWN) {
                    direccion = KeyEvent.VK_UP;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (direccion != KeyEvent.VK_UP) {
                    direccion = KeyEvent.VK_DOWN;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (direccion != KeyEvent.VK_RIGHT) {
                    direccion = KeyEvent.VK_LEFT;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (direccion != KeyEvent.VK_LEFT) {
                    direccion = KeyEvent.VK_RIGHT;
                }
            }
        }
    }

    public class ImagenSnake extends JPanel {

        public void paintComponent(Graphics g) {
            this.setBackground(Color.DARK_GRAY);
            super.paintComponent(g);

            g.setColor(Color.CYAN);
            g.fillRect(snake.x, snake.y, widthPoint, heightPoint);

            for (int i = 0; i < lista.size(); i++) {
                Point p = lista.get(i);
                g.fillRect(p.x, p.y, widthPoint, heightPoint);
            }

            g.setColor(Color.ORANGE);
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            if (gameOver == true) {
                g.setColor(Color.RED);
                g.drawString("GAME OVER", 200, 200);
            }
        }
    }

    public class Momento extends Thread {

        long last = 0;

        public void run() {
            while (true) {
                if ((java.lang.System.currentTimeMillis() - last) > frecuencia) {
                    if (!gameOver) {
                        if (direccion == KeyEvent.VK_UP) {
                            snake.y = snake.y - heightPoint;
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                        } else if (direccion == KeyEvent.VK_DOWN) {
                            snake.y = snake.y + heightPoint;
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                        } else if (direccion == KeyEvent.VK_LEFT) {
                            snake.x = snake.x - widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                            if (snake.x > width) {
                                snake.x = 0;
                            }
                        } else if (direccion == KeyEvent.VK_RIGHT) {
                            snake.x = snake.x + widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                            if (snake.x > width) {
                                snake.x = 0;
                            }
                        }

                        actualizar();
                        last = java.lang.System.currentTimeMillis();
                    }
                }
            }
        }

        public void actualizar() {
            imagenSnake.repaint();

            lista.add(0, new Point(snake.x, snake.y));
            lista.remove((lista.size() - 1));

            for (int i = 1; i < lista.size(); i++) {
                Point p = lista.get(i);
                if (snake.x == p.x && snake.y == p.y) {
                    gameOver = true;
                }
            }
            if ((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10)) && (snake.y < (comida.y + 10))) {
                lista.add(0, new Point(snake.x, snake.y));
                crearComida();
            }
        }

    }
}
