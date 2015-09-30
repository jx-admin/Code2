/*
  JavaChess - The main class for the Java chess game.

  Copyright (C) 2003 The Java-Chess team <info@java-chess.de>

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package de.java_chess.javaChess;

import de.java_chess.javaChess.action.*;
import de.java_chess.javaChess.bitboard.*;
import de.java_chess.javaChess.engine.*;
import de.java_chess.javaChess.menu.*;
import de.java_chess.javaChess.notation.*;
import de.java_chess.javaChess.renderer.ChessBoardRenderer;
import de.java_chess.javaChess.renderer2d.*;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.game.GameImpl;

import java.awt.event.*;
import java.awt.event.ActionListener;

import javax.swing.border.BevelBorder;
import java.awt.*;
import javax.swing.*;

/**
 * The main class for the chess game, representing the GUI
 */
public class JavaChess extends JFrame// implements ActionListener
{
  // Static variables

  /**
   * The game instance.
   */
  static JavaChess _instance;

/**
 * Versionsnummer
 */
	public final static String VERSIONINFO = "0.1.0 pre-alpha 2";

	private JPanel contentPane;

/**
 * Statuszeilen-Panel
 */
	private JPanel jpStatus = new JPanel();

/**
 * Label für Status-Text
 */
	private JLabel jlStatus = new JLabel();

/**
 * Label für Aktions-Text
 */
	private JLabel jlAction = new JLabel();

/**
 * Label für die Versionsnummer
 */
	private JLabel jlVersion = new JLabel();

/**
 * Eigenes Panel für das Schachbrett
 */
	private JPanel jpBrett = new JPanel();

/**
 * Own panel for game notation
 */
		private NotationPanel jpNotation;

/**
 * Own panel for engine output/debug etc.
 */
		private EnginePanel jpEngine;

/**
 * Own Panel for navigation buttons
 */
		private NavigationPanel jpNavigation;

	private Font textFont = new Font( "Serif", Font.PLAIN, 12 );

/**
 * glassPane zum Reagieren auf Mausbewegungen etc.:
 * The glass pane overlays the menu bar and content pane, so it can intercept mouse movements and such.
 */
	private JComponent glasspane = new JComponent()
	{
		public void processKeyEvent(KeyEvent e)
		{
			e.consume();
		}
	};

  // Instance variables

    /**
     * The current game.
     */
    Game _game;

    /**
     * The notation of the current game.
     */
    GameNotation _gameNotation;

    /**
     * The chess board
     */
    BitBoard _board;

    /**
     * The rendering component to display the board.
     */
    ChessBoardRenderer _renderer;

    /**
     * The chess engine.
     */
    ChessEngine _engine;

    /**
     * The menu items
     */
    JMenuItem _exitItem;

    /**
     * The game controller.
     */
    GameController _controller;

    /**
     * A timer for the game.
     */
    GameTimerPanel _gameTimer;

/**
 * GridBagLayout for StatusPanel
 */
  GridBagLayout gridBagStatus = new GridBagLayout();

/**
 * GridBagLayout for chess board Panel
 */
  GridBagLayout gridBagBrett = new GridBagLayout();

/**
 * GridBagLayout for the navigation panel
 */
	GridBagLayout gridBagNavigation = new GridBagLayout();

/**
 * The Edit menu
 */
	EditMenu editMenu;

  // Constructors

/**
 * Create a new instance of this chess game.
 */
	public JavaChess()
	{
		super( "JavaChess");  // Set the title of the window.
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try
		{
			jbInit();
			glasspane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			glasspane.addMouseListener( new MouseAdapter(){} );
			setGlassPane(glasspane);
			free();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		setDefaultFrameCoordinates();

		setVisible(true);
	}

/**
 * Die GUI nach Nomenklatur des JBuilder
 *
 * @throws Exception
 */
	private void jbInit() throws Exception
	{
		_game = new GameImpl();
		_gameNotation = new GameNotationImpl();
		_board = new BitBoardImpl();
		_engine = new ChessEngineImpl( _game, _board, false);

		// Create a timer with 40 min for each player.
		_gameTimer = new GameTimerPanel( 40 * 60);

		_controller = new GameController( _game, _gameNotation, _engine, _board, _gameTimer);
		_renderer = new ChessBoardRenderer2D( _controller, _board);
		_controller.setRenderer( _renderer);
		editMenu = new EditMenu();

		this.jpNotation = new NotationPanel();
		((GameNotationImpl)_gameNotation).setNotationPanel( jpNotation);
		editMenu.setNotationPanel( jpNotation );

		this.jpEngine = new EnginePanel();
		((ChessEngineImpl)_engine).setEnginePanel( jpEngine );

		this.jpNavigation = new NavigationPanel();

//		setIconImage(Toolkit.getDefaultToolkit().createImage(VisFrame.class.getResource("jclogo.gif")));

		contentPane = (JPanel) this.getContentPane();
//		contentPane.setLayout(new BorderLayout());
		contentPane.setLayout(new GridBagLayout());

		this.setSize(new Dimension(800, 600));

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic( KeyEvent.VK_F ); // Shortcut Alt-F

    fileMenu.add( getMenuItem( new SaveGameAction( _gameNotation)));
		fileMenu.add( getMenuItem( new SaveGameAsAction( _gameNotation)));

		_exitItem = new JMenuItem( "Exit");
		// Shortcut für ALT-X zum Beenden:
		this._exitItem.setAccelerator(javax.swing.KeyStroke.
				getKeyStroke(88, java.awt.event.KeyEvent.ALT_MASK, false));
		_exitItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit( 0 );
			}
		});

		// Create a separated exit item
		fileMenu.addSeparator();
		this.jpStatus.setLayout(gridBagStatus);
    this.jpBrett.setLayout(gridBagBrett);
//		this.jpBrett.setPreferredSize( new Dimension(520,520) );
//		this.jpBrett.setMaximumSize( new Dimension(520,520) );
//		this.jpBrett.setMinimumSize( new Dimension(520,520) );
		this.jpBrett.setPreferredSize( new Dimension(430,430) );
//		this.jpBrett.setMaximumSize( new Dimension(400,400) );
		this.jpBrett.setMinimumSize( new Dimension(430,430) );

		this.jpStatus.setPreferredSize( new Dimension(780, 20) );
		this.jpStatus.setMinimumSize( new Dimension(780, 20) );
		this.jpStatus.setMaximumSize( new Dimension(780, 20) );

		this.jpEngine.setPreferredSize( new Dimension(780, 150) );
		this.jpEngine.setMinimumSize( new Dimension(780, 150) );
		this.jpEngine.setMaximumSize( new Dimension(780, 150) );

		this.jpNavigation.setLayout( gridBagNavigation );

		jlAction.setBorder(BorderFactory.createLoweredBevelBorder());
    fileMenu.add( _exitItem);

		// Add the file menu to the menu bar.
		menuBar.add( fileMenu);

		// Get the Edit menu from the according class and add to the menu bar.
		menuBar.add( editMenu.getMenu() );

		// Get the menu from the chess engine and add it to the menu bar.
		menuBar.add( _engine.getMenu());

		// Create and add a help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add( new HelpAction());
		helpMenu.addSeparator();
		helpMenu.add( new AboutAction());
		menuBar.add(Box.createHorizontalGlue());  // Move the help menu to the right.
		menuBar.add( helpMenu);

		// Create and set the menu.
		this.setJMenuBar( menuBar );

		this.jlStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
																											 Color.white, new Color(148, 145, 140),
																											 new Color(103, 101, 98)));
		this.jlStatus.setText(" Placeholder for status text ");
		this.jlStatus.setForeground( Color.black );
		this.jlStatus.setFont( this.textFont );

		this.jlAction.setText("Placeholder for action(s) in progress");
		this.jlAction.setFont( this.textFont );
		this.jlAction.setForeground( Color.black );

		this.jlVersion.setBorder(BorderFactory.createLoweredBevelBorder());
		this.jlVersion.setText( "Version: " + this.VERSIONINFO );
		this.jlVersion.setFont( this.textFont );
		this.jlVersion.setForeground( Color.black );

		contentPane.setForeground( Color.black );

		contentPane.add(jpBrett,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0)); // Brett
		contentPane.add(_gameTimer,   new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0)); // The Clock
		contentPane.add(jpEngine,     new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0)); // Engineoutput
		contentPane.add(jpStatus,     new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0)); // Statuszeile
		contentPane.add(jpNotation,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    contentPane.add(jpNavigation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0)); // Notation etc.

//		this.jpNotation.setPreferredSize( new Dimension( 100,100 ));
		jpStatus.add(jlAction,  new GridBagConstraints(0, 0, 1, 1, 0.6, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jpStatus.add(jlStatus,  new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpStatus.add(jlVersion,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpStatus.setBackground( Color.lightGray );

		jpBrett.add((ChessBoardRenderer2D)_renderer,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpBrett.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)));

		this.pack();
		this.setVisible( true);
  }


    // Methods

    /**
     * The main method.
     *
     * @param args The commandline arguments.
     */
    public static void main( String [] args) {
	_instance = new JavaChess();
    }

    /**
     * Create a return a menu item for a given action.
     *
     * @param action The action.
     *
     * @return The menu item for this action.
     */
    private JMenuItem getMenuItem( JavaChessAction action) {
	JMenuItem item = new JMenuItem( action.getName());

	item.addActionListener( action);

	return item;
    }

	private void setDefaultFrameCoordinates()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = packFrame();

		setFrameSize(screenSize, frameSize);

		placeFrame(screenSize, frameSize);
	}

	private void placeFrame(Dimension screenSize, Dimension frameSize)
	{
		setLocation(
			(screenSize.width - frameSize.width) / 2,
			(screenSize.height - frameSize.height) / 2);
	}

	private Dimension packFrame()
	{
		boolean packFrame = true;

		if (packFrame)
		{
			pack();
		}
		else
		{
			validate();
		}

		return this.getSize();
	}

	private void setFrameSize(Dimension screenSize, Dimension frameSize)
	{
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
	}

	private void busy()
	{
		glasspane.requestFocus();
		glasspane.setVisible(true);
	}

	private void free()
	{
		glasspane.setVisible(false);
	}

}
